"""分片下载、MP4 完整性校验、FFmpeg 合并。"""

import asyncio
import logging
import math
import os
import pathlib
import shutil
import struct
import subprocess
import time
from typing import Any, Optional

import aiohttp

logger = logging.getLogger("bilibili_downloader")

CHUNK_SIZE = 2 * 1024 * 1024  # 2 MB


def is_mp4_complete(file_path: str) -> bool:
    """
    检查 MP4 文件是否完整：
    1. 第一个 box 必须是 ftyp
    2. 必须包含 moov box
    3. 所有 box 的总大小必须等于文件实际大小
    4. moov box 的 size 为 0 表示延伸到文件末尾
    """
    real_size = os.path.getsize(file_path)
    with open(file_path, "rb") as f:
        total_size = 0
        has_moov = False
        is_first = True

        while True:
            header = f.read(8)
            if len(header) < 8:
                break

            box_size = struct.unpack(">I", header[:4])[0]
            box_type = header[4:8].decode("ascii", errors="replace")

            if is_first:
                if box_type != "ftyp":
                    return False
                is_first = False

            if box_type == "moov":
                has_moov = True

            # box_size == 0 表示延伸到文件末尾
            if box_size == 0:
                return True

            # box_size == 1 表示使用 64-bit large_size
            if box_size == 1:
                large_header = f.read(8)
                if len(large_header) < 8:
                    return False
                box_size = struct.unpack(">Q", large_header)[0]
                if box_size < 16:
                    return False
                skip = box_size - 16
                f.seek(skip, 1)
                total_size += box_size
            else:
                if box_size < 8:
                    return False
                f.seek(box_size - 8, 1)
                total_size += box_size

            if total_size > real_size:
                return False

    return real_size == total_size and has_moov


async def get_content_length(session: Any, url: str, cookie: str = "") -> Optional[int]:
    """用 HEAD 获取 Content-Length，失败则降级 Range: bytes=0-0。
    注意：CDN URL 不能带 Cookie，否则会被拒绝。"""
    from enjoy.api.client import USER_AGENT, REFERRER
    headers = {"User-Agent": USER_AGENT, "Referer": REFERRER}
    # HEAD
    try:
        async with session.head(url, headers=headers, timeout=aiohttp.ClientTimeout(total=5)) as resp:
            if resp.status == 200:
                cl = resp.headers.get("Content-Length")
                if cl:
                    return int(cl)
    except Exception:
        pass
    # GET Range: bytes=0-0
    try:
        range_headers = dict(headers)
        range_headers["Range"] = "bytes=0-0"
        async with session.get(url, headers=range_headers, timeout=aiohttp.ClientTimeout(total=5)) as resp:
            if resp.status == 206:
                cr = resp.headers.get("Content-Range")
                if cr and "/" in cr:
                    return int(cr.rsplit("/", 1)[1])
                cl = resp.headers.get("Content-Length")
                if cl:
                    return int(cl)
    except Exception:
        pass
    return None


async def get_url_with_backup(session: Any, base_url: str,
                              backup_urls: list[str], cookie: str = "") -> Optional[tuple[str, int]]:
    """尝试 base_url + backup_urls，返回 (url, content_length)。"""
    urls = list(backup_urls) + [base_url]
    for u in urls:
        cl = await get_content_length(session, u, cookie)
        if cl and cl > 0:
            return (u, cl)
    return None


async def download_chunk(session: Any, url: str,
                         start: int, end: int, file: Any,
                         progress_callback=None) -> bool:
    """下载单个 chunk 并写入文件指定偏移。"""
    from enjoy.api.client import USER_AGENT, REFERRER
    headers = {
        "User-Agent": USER_AGENT,
        "Referer": REFERRER,
        "Range": f"bytes={start}-{end}",
    }
    try:
        async with session.get(url, headers=headers) as resp:
            if resp.status != 206:
                logger.error("chunk %d-%d 返回状态码 %d", start, end, resp.status)
                return False
            data = await resp.read()
            file.seek(start)
            file.write(data)
            if progress_callback:
                progress_callback(len(data))
            return True
    except Exception as e:
        logger.error("chunk %d-%d 下载失败: %s", start, end, e)
        return False


async def download_file_chunks(session: Any, url: str,
                               content_length: int, output_path: str,
                               concurrency: int = 16) -> bool:
    """
    将文件按 CHUNK_SIZE (2MB) 切分成多个 chunk，并发下载后合并。
    支持断点续传：如果输出文件已存在且大小匹配 content_length，则跳过已完成。
    """
    import aiohttp

    temp_path = output_path + ".downloading"

    # 切分 chunk 范围
    chunks = []
    num_chunks = math.ceil(content_length / CHUNK_SIZE)
    for i in range(num_chunks):
        start = i * CHUNK_SIZE
        end = min(start + CHUNK_SIZE, content_length) - 1
        chunks.append((start, end))

    # 读取已有文件的状态
    completed_chunks: set[int] = set()
    if os.path.exists(temp_path):
        existing_size = os.path.getsize(temp_path)
        if existing_size == content_length:
            # 文件完整，直接重命名
            logger.info("发现完整临时文件，跳过下载")
            os.replace(temp_path, output_path)
            return True

    # 并发下载
    semaphore = asyncio.Semaphore(concurrency)
    download_done = [False] * len(chunks)

    # 先创建/打开临时文件
    if not os.path.exists(temp_path):
        with open(temp_path, "wb") as f:
            f.truncate(content_length)

    async def _download(idx: int):
        async with semaphore:
            start, end = chunks[idx]
            if idx in completed_chunks:
                download_done[idx] = True
                return
            with open(temp_path, "r+b") as f:
                ok = await download_chunk(session, url, start, end, f)
                if ok:
                    completed_chunks.add(idx)
                    download_done[idx] = True

    tasks = [asyncio.create_task(_download(i)) for i in range(len(chunks))]
    await asyncio.gather(*tasks)

    # 检查全部完成
    if not all(download_done):
        logger.error("部分 chunk 下载失败")
        return False

    # 完整性校验
    if not is_mp4_complete(temp_path):
        logger.error("文件完整性校验失败")
        if os.path.exists(temp_path):
            os.remove(temp_path)
        return False

    # 重命名
    if os.path.exists(output_path):
        os.remove(output_path)
    os.replace(temp_path, output_path)
    return True


def find_ffmpeg(ffmpeg_path_arg: Optional[str]) -> str:
    """查找 ffmpeg 可执行文件。"""
    if ffmpeg_path_arg:
        return ffmpeg_path_arg

    # 尝试系统 PATH
    path = shutil.which("ffmpeg")
    if path:
        return path

    # 尝试项目内置 ffmpeg
    script_dir = pathlib.Path(__file__).parent.parent.parent.resolve()
    for candidate in script_dir.glob("src-tauri/ffmpeg/*.exe"):
        return str(candidate)

    raise FileNotFoundError(
        "未找到 ffmpeg。请安装 ffmpeg 或将 --ffmpeg 指向可执行文件路径。"
    )


def merge_with_ffmpeg(ffmpeg: str, video_path: str, audio_path: str,
                      output_path: str) -> bool:
    """用 ffmpeg 合并音视频 (流复制，不重新编码)。"""
    cmd = [
        ffmpeg, "-y",
        "-i", video_path,
        "-i", audio_path,
        "-c", "copy",
        "-map", "0:v:0",
        "-map", "1:a:0",
        output_path,
    ]

    try:
        result = subprocess.run(cmd, capture_output=True, timeout=3600)
        if result.returncode != 0:
            logger.error("ffmpeg 合并失败: %s", result.stderr.decode(errors="replace"))
            return False
        # 清理中间文件
        for p in [video_path, audio_path]:
            if os.path.exists(p):
                os.remove(p)
        return True
    except Exception as e:
        logger.error("ffmpeg 合并异常: %s", e)
        return False
