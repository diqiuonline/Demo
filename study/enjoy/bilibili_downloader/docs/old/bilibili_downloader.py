#!/usr/bin/env python3
"""
Bilibili 视频下载器
仿照 bilibili-video-downloader (Rust/Tauri) 的下载逻辑，
用纯 Python 实现：解析 BV 链接 -> 获取最高画质音视频 -> 分片下载 -> 完整性校验 -> FFmpeg 合并。

用法:
    python bilibili_downloader.py --links links.txt --cookie cookie.json --output ./downloads

依赖:
    pip install aiohttp
    确保 FFmpeg 在 PATH 中，或将 --ffmpeg 指向可执行文件路径。
"""

import argparse
import asyncio
import base64
import hashlib
import hmac
import json
import logging
import math
import os
import pathlib
import re
import struct
import sys
import time
import urllib.parse
from dataclasses import dataclass, field
from typing import Any, Optional

import aiohttp

# ---------------------------------------------------------------------------
# 常量
# ---------------------------------------------------------------------------

USER_AGENT = (
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36"
)
REFERRER = "https://www.bilibili.com/"
CHUNK_SIZE = 2 * 1024 * 1024  # 2 MB

# 画质优先级 (数字越大优先级越低)
QUALITY_ORDER = {
    127: 0,   # 8K
    126: 1,   # 杜比视界
    125: 2,   # HDR
    120: 3,   # 4K
    116: 4,   # 1080P60
    112: 5,   # 1080P+
    80: 6,    # 1080P
    100: 7,   # AI 修复
    74: 8,    # 720P60
    64: 9,    # 720P
    32: 10,   # 480P
    16: 11,   # 360P
    6: 12,    # 240P
}

# 音质优先级
AUDIO_QUALITY_ORDER = {
    30251: 0,   # Hi-Res
    30250: 1,   # 杜比全景声
    30280: 2,   # 192K
    30232: 3,   # 132K
    30216: 4,   # 64K
}

WBI_MIXIN_KEY_ENC_TAB = [
    46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35,
    27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13,
    37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60, 51, 30, 4,
    22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36, 20, 34, 44, 52,
]

logger = logging.getLogger("bilibili_downloader")

# ---------------------------------------------------------------------------
# WBI 签名
# ---------------------------------------------------------------------------


def get_wbi_keys(img_url: str, sub_url: str) -> str:
    """从 img/sub URL 中提取文件名作为混入密钥。"""
    def take_filename(url: str) -> str:
        parts = url.rsplit("/", 1)
        if len(parts) < 2:
            return ""
        fname = parts[1]
        dot = fname.rfind(".")
        return fname[:dot] if dot > 0 else fname

    return take_filename(img_url) + take_filename(sub_url)


def mixin_key(orig: str) -> str:
    return "".join(orig[WBI_MIXIN_KEY_ENC_TAB[i]] for i in range(32))


def url_encode(s: str) -> str:
    """仿照 Rust 端的 get_url_encoded：保留 alphanum -_.~，过滤 !'()*"""
    reserved = set("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~")
    extra_skip = set("!'()*")
    out = []
    for c in s:
        if c in reserved:
            out.append(c)
        elif c in extra_skip:
            continue
        else:
            out.append(f"%{c.encode('utf-8').hex().upper()}")
    return "".join(out)


async def wbi_sign(params: dict[str, str], session: aiohttp.ClientSession, cookie: str) -> dict[str, str]:
    """为参数字典添加 wts + w_rid 签名。"""
    # 获取 WBI 密钥
    nav_url = "https://api.bilibili.com/x/web-interface/nav"
    headers = {
        "User-Agent": USER_AGENT,
        "Referer": REFERRER,
        "Cookie": cookie,
    }
    async with session.get(nav_url, headers=headers) as resp:
        data = await resp.json()
    wbi_img = data["data"]["wbi_img"]
    img_key = wbi_img["img_url"].rsplit("/", 1)[-1].rsplit(".", 1)[0]
    sub_key = wbi_img["sub_url"].rsplit("/", 1)[-1].rsplit(".", 1)[0]
    mixin_key_str = img_key + sub_key

    # 添加时间戳并排序
    wts = int(time.time())
    params["wts"] = str(wts)
    params = dict(sorted(params.items()))

    # 拼接 query string
    query = "&".join(f"{url_encode(k)}={url_encode(v)}" for k, v in params.items())
    w_rid = hashlib.md5((query + mixin_key_str).encode()).hexdigest()
    params["w_rid"] = w_rid
    return params


# ---------------------------------------------------------------------------
# URL 解析
# ---------------------------------------------------------------------------


def extract_bvid(url: str) -> Optional[str]:
    """从 URL 路径中提取 BVxxx。"""
    parsed = urllib.parse.urlparse(url)
    for seg in parsed.path.strip("/").split("/"):
        if seg.upper().startswith("BV"):
            return seg
    return None


def extract_aid(url: str) -> Optional[int]:
    """从 URL 路径中提取 Avxxx 的 aid。"""
    parsed = urllib.parse.urlparse(url)
    for seg in parsed.path.strip("/").split("/"):
        if seg.lower().startswith("av"):
            try:
                return int(seg[2:])
            except ValueError:
                pass
    return None


def extract_cid(url: str) -> Optional[int]:
    """从 URL 查询参数中提取 cid。"""
    parsed = urllib.parse.urlparse(url)
    qs = dict(urllib.parse.parse_qsl(parsed.query))
    qs_params = dict(urllib.parse.parse_qsl(parsed.params))
    val = qs.get("cid") or qs_params.get("cid")
    return int(val) if val else None


def parse_links_file(filepath: str) -> list[dict[str, str]]:
    """
    解析 links.txt，支持两种格式：
    1) 纯 URL 行: https://www.bilibili.com/video/BV1xxxx/
    2) 结构化块:
       1. 【标题】
          发布日期 : 2026-06-27
          动态链接 : ...
          视频访问 : https://www.bilibili.com/video/BV1xxxx/
    返回 [(date, title, bvid, cid, p), ...]
    """
    with open(filepath, "r", encoding="utf-8") as f:
        text = f.read()

    results = []
    current_date = ""
    current_title = ""
    current_url = ""

    for line in text.splitlines():
        stripped = line.strip()
        if not stripped or stripped.startswith("#"):
            continue

        # 匹配 "发布日期 : YYYY-MM-DD"
        m = re.match(r"发布日期\s*:\s*(\d{4}-\d{2}-\d{2})", stripped)
        if m:
            current_date = m.group(1)
            continue

        # 匹配 "N. 【标题】" 或 "【标题】"
        m2 = re.match(r"\d*\.\s*【(.+?)】", stripped)
        if not m2:
            m2 = re.match(r"【(.+?)】", stripped)
        if m2:
            current_title = m2.group(1)
            continue

        # 匹配 "视频访问 : URL"
        m3 = re.match(r"视频访问\s*:\s*(.+)", stripped)
        if m3:
            current_url = m3.group(1).strip()
            # 立即提取 bvid 并保存
            bvid = extract_bvid(current_url)
            if bvid:
                results.append({
                    "date": current_date or "",
                    "title": current_title or "",
                    "url": current_url,
                    "bvid": bvid,
                })
            # 重置
            current_date = ""
            current_title = ""
            current_url = ""
            continue

        # 纯 URL 行（无标题/日期）
        m_url = re.match(r"(https?://[^\s]+)", stripped)
        if m_url:
            url = m_url.group(1)
            bvid = extract_bvid(url)
            if bvid:
                results.append({
                    "date": current_date or "",
                    "title": current_title or "",
                    "url": url,
                    "bvid": bvid,
                })
            # 重置
            current_date = ""
            current_title = ""
            current_url = ""
            continue

    return results


def extract_ep_id(url: str) -> Optional[int]:
    parsed = urllib.parse.urlparse(url)
    for seg in parsed.path.strip("/").split("/"):
        if seg.lower().startswith("ep"):
            try:
                return int(seg[2:])
            except ValueError:
                pass
    return None


# ---------------------------------------------------------------------------
# 下载工具
# ---------------------------------------------------------------------------


def filename_filter(s: str) -> str:
    """过滤文件名中的非法字符。"""
    s = s.replace("\\", " ").replace("/", " ").replace("\n", " ")
    s = s.replace(":", "：").replace("*", "⭐").replace("?", "？")
    s = s.replace('"', "'").replace("<", "《").replace(">", "》").replace("|", "丨")
    return s.strip().rstrip(".").strip()


async def get_content_length(session: aiohttp.ClientSession, url: str) -> Optional[int]:
    """用 HEAD 获取 Content-Length，失败则降级 Range: bytes=0-0。"""
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
        range_headers = {
            "User-Agent": USER_AGENT,
            "Referer": REFERRER,
            "Range": "bytes=0-0",
        }
        async with session.get(url, headers=range_headers, timeout=aiohttp.ClientTimeout(total=5)) as resp:
            cr = resp.headers.get("Content-Range")
            if cr and "/" in cr:
                return int(cr.rsplit("/", 1)[1])
            cl = resp.headers.get("Content-Length")
            if cl:
                return int(cl)
    except Exception:
        pass
    return None


async def get_url_with_backup(session: aiohttp.ClientSession, base_url: str,
                              backup_urls: list[str]) -> Optional[tuple[str, int]]:
    """尝试 base_url + backup_urls，返回 (url, content_length)。"""
    urls = list(backup_urls) + [base_url]
    for u in urls:
        cl = await get_content_length(session, u)
        if cl and cl > 0:
            return (u, cl)
    return None


async def download_chunk(session: aiohttp.ClientSession, url: str,
                         start: int, end: int, file, progress_callback=None) -> bool:
    """下载单个 chunk 并写入文件指定偏移。"""
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


# ---------------------------------------------------------------------------
# MP4 完整性校验 (仿照 is_mp4_complete)
# ---------------------------------------------------------------------------


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


# ---------------------------------------------------------------------------
# 分片下载 (仿照 Rust 端的 chunk 下载)
# ---------------------------------------------------------------------------


async def download_file_chunks(session: aiohttp.ClientSession, url: str,
                               content_length: int, output_path: str,
                               concurrency: int = 16) -> bool:
    """
    将文件按 CHUNK_SIZE (2MB) 切分成多个 chunk，并发下载后合并。
    支持断点续传：如果输出文件已存在且大小匹配 content_length，则跳过已完成的 chunk。
    """
    temp_path = output_path + ".downloading"

    # 判断是否可以复用已有文件
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

    # 先创建/打开临时文件（仿照 Rust 端的 File::create + allocate）
    if not os.path.exists(temp_path):
        with open(temp_path, "wb") as f:
            f.truncate(content_length)

    async def _download(idx: int):
        async with semaphore:
            start, end = chunks[idx]
            if idx in completed_chunks:
                download_done[idx] = True
                return
            # 打开文件追加模式
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


# ---------------------------------------------------------------------------
# FFmpeg 合并
# ---------------------------------------------------------------------------


def find_ffmpeg(ffmpeg_path_arg: Optional[str]) -> str:
    """查找 ffmpeg 可执行文件。"""
    if ffmpeg_path_arg:
        return ffmpeg_path_arg

    # 尝试系统 PATH
    for name in ["ffmpeg", "ffmpeg.exe"]:
        path = shutil_which(name)
        if path:
            return path

    # 尝试项目内置 ffmpeg
    script_dir = pathlib.Path(__file__).parent.resolve()
    for candidate in script_dir.glob("src-tauri/ffmpeg/*.exe"):
        return str(candidate)

    raise FileNotFoundError(
        "未找到 ffmpeg。请安装 ffmpeg 或将 --ffmpeg 指向可执行文件路径。"
    )


def shutil_which(cmd: str) -> Optional[str]:
    """简易版 which。"""
    import shutil as _shutil
    return _shutil.which(cmd)


def merge_with_ffmpeg(ffmpeg: str, video_path: str, audio_path: str,
                      output_path: str) -> bool:
    """用 ffmpeg 合并音视频 (流复制，不重新编码)。"""
    import subprocess

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


# ---------------------------------------------------------------------------
# 核心下载逻辑
# ---------------------------------------------------------------------------


@dataclass
class VideoTrack:
    id: int
    quality: int
    codecid: int
    bandwidth: int
    codecs: str
    base_url: str
    backup_urls: list[str]
    width: int
    height: int
    frame_rate: str
    content_length: int = 0
    url: str = ""


@dataclass
class AudioTrack:
    id: int
    audio_quality: int
    bandwidth: int
    codecs: str
    base_url: str
    backup_urls: list[str]
    content_length: int = 0
    url: str = ""


@dataclass
class PageInfo:
    cid: int
    page: int
    part: str
    duration: int  # 毫秒
    bvid: str = ""
    aid: int = 0


@dataclass
class VideoInfo:
    bvid: str
    title: str
    owner_name: str
    pic: str
    pages: list[PageInfo]
    ugc_season: Optional[dict] = None  # 合集信息


async def get_video_info(session: aiohttp.ClientSession, cookie: str,
                         bvid: str, cid: Optional[int] = None) -> VideoInfo:
    """调用 view API 获取视频信息。"""
    params = {"bvid": bvid}
    headers = {
        "User-Agent": USER_AGENT,
        "Referer": REFERRER,
        "Cookie": cookie,
    }

    async with session.get(
        "https://api.bilibili.com/x/web-interface/view",
        params=params,
        headers=headers,
    ) as resp:
        data = await resp.json()

    if data.get("code") != 0:
        raise RuntimeError(f"获取视频信息失败: {data.get('message')} / {data}")

    info = data["data"]

    # 构建 pages
    pages = []
    for p in info.get("pages", []):
        pages.append(PageInfo(
            cid=p["cid"],
            page=p.get("page", 1),
            part=p.get("part", ""),
            duration=p.get("duration", 0),
            bvid=bvid,
            aid=info.get("aid", 0),
        ))

    return VideoInfo(
        bvid=info["bvid"],
        title=info["title"],
        owner_name=info["owner"]["name"],
        pic=info["pic"],
        pages=pages,
        ugc_season=info.get("ugc_season"),
    )


async def get_playurl(session: aiohttp.ClientSession, cookie: str,
                      bvid: str, cid: int) -> tuple[list[VideoTrack], list[AudioTrack]]:
    """调用 playurl API 获取音视频轨道列表。"""
    # 先签 WBI
    params = {
        "bvid": bvid,
        "cid": str(cid),
        "qn": "127",
        "fnval": "4048",
    }
    signed = await wbi_sign(params, session, cookie)

    headers = {
        "User-Agent": USER_AGENT,
        "Referer": REFERRER,
        "Cookie": cookie,
    }

    async with session.get(
        "https://api.bilibili.com/x/player/wbi/playurl",
        params=signed,
        headers=headers,
    ) as resp:
        data = await resp.json()

    if data.get("code") != 0:
        raise RuntimeError(f"获取播放地址失败: {data.get('message')} / {data}")

    dash = data["data"]["dash"]
    video_tracks: list[VideoTrack] = []
    audio_tracks: list[AudioTrack] = []

    # 解析视频轨道
    for v in dash.get("video", []):
        bt = v.get("base_url", "")
        bu = v.get("backup_url", [])
        if isinstance(bu, str):
            bu = [bu]
        vt = VideoTrack(
            id=v["id"],
            quality=v["id"],
            codecid=v.get("codecid", 0),
            bandwidth=v.get("bandwidth", 0),
            codecs=v.get("codecs", ""),
            base_url=bt,
            backup_urls=bu,
            width=v.get("width", 0),
            height=v.get("height", 0),
            frame_rate=v.get("frame_rate", ""),
        )
        video_tracks.append(vt)

    # 解析音频轨道
    for a in dash.get("audio", []):
        bt = a.get("base_url", "")
        bu = a.get("backup_url", [])
        if isinstance(bu, str):
            bu = [bu]
        at = AudioTrack(
            id=a["id"],
            audio_quality=a["id"],
            bandwidth=a.get("bandwidth", 0),
            codecs=a.get("codecs", ""),
            base_url=bt,
            backup_urls=bu,
        )
        audio_tracks.append(at)

    return video_tracks, audio_tracks


def select_best_video(tracks: list[VideoTrack]) -> Optional[VideoTrack]:
    """按画质优先级 + 编码优先级选择最佳视频轨道。"""
    if not tracks:
        return None

    def sort_key(v: VideoTrack) -> tuple[int, int]:
        q = QUALITY_ORDER.get(v.quality, 999)
        # 编码偏好: AVC(7) < HEVC(12) < AV1(13)
        codec_pref = {7: 0, 12: 1, 13: 2}.get(v.codecid, 3)
        return (q, codec_pref)

    tracks_sorted = sorted(tracks, key=sort_key)
    return tracks_sorted[0]


def select_best_audio(tracks: list[AudioTrack]) -> Optional[AudioTrack]:
    """按音质优先级选择最佳音频轨道。"""
    if not tracks:
        return None

    def sort_key(a: AudioTrack) -> int:
        return AUDIO_QUALITY_ORDER.get(a.audio_quality, 999)

    tracks_sorted = sorted(tracks, key=sort_key)
    return tracks_sorted[0]


async def resolve_track_url(session: aiohttp.ClientSession,
                            track) -> Optional[tuple[str, int]]:
    """为轨道解析出可用的下载 URL + 文件大小。"""
    result = await get_url_with_backup(session, track.base_url, track.backup_urls)
    return result


async def download_single_video(session: aiohttp.ClientSession, cookie: str,
                                bvid: str, cid: int, output_dir: str,
                                ffmpeg: str, concurrency: int = 16,
                                timeout_sec: int = 60,
                                custom_date: str = "",
                                custom_title: str = ""):
    """下载单个视频的完整流程。"""
    # 确保 Unicode 输出
    if hasattr(sys.stdout, "reconfigure"):
        sys.stdout.reconfigure(encoding="utf-8")
    print(f"\n{'='*60}")
    print(f"开始下载: {bvid}  CID={cid}")
    print(f"{'='*60}")

    async with aiohttp.ClientSession() as inner_session:
        # Step 1: 获取视频信息
        print("[1/7] 获取视频信息...")
        try:
            info = await get_video_info(inner_session, cookie, bvid, cid)
        except Exception as e:
            print(f"ERROR: 获取视频信息失败: {e}")
            return

        # 选择分 P
        if cid:
            page = next((p for p in info.pages if p.cid == cid), None)
            if not page:
                print(f"ERROR: 找不到 cid={cid} 对应的分 P")
                return
        else:
            page = info.pages[0]
            cid = page.cid

        part_title = page.part or info.title
        safe_title = filename_filter(page.part or info.title)
        safe_collection = filename_filter(info.title)
        safe_up = filename_filter(info.owner_name)

        print(f"  标题: {info.title}")
        print(f"  UP主: {info.owner_name}")
        print(f"  分P:  {page.page}/{len(info.pages)} - {part_title}")

        # Step 2: 获取播放地址
        print("[2/7] 获取播放地址...")
        try:
            video_tracks, audio_tracks = await get_playurl(
                inner_session, cookie, bvid, cid
            )
        except Exception as e:
            print(f"ERROR: 获取播放地址失败: {e}")
            return

        # Step 3: 选择最佳轨道
        print("[3/7] 选择最佳画质...")
        best_video = select_best_video(video_tracks)
        best_audio = select_best_audio(audio_tracks)

        if not best_video:
            print("ERROR: 没有可用的视频轨道")
            return

        if not best_audio:
            print("WARNING: 没有可用的音频轨道，仅下载视频")

        v_quality = best_video.quality
        v_codec = best_video.codecid
        print(f"  视频: 画质={v_quality}  编码={v_codec}  "
              f"{best_video.width}x{best_video.height}  "
              f"码率={best_video.bandwidth/1000:.1f}kbps")
        if best_audio:
            a_quality = best_audio.audio_quality
            print(f"  音频: 音质={a_quality}  编码={best_audio.codecs}  "
                  f"码率={best_audio.bandwidth/1000:.1f}kbps")

        # Step 4: 解析下载 URL
        print("[4/7] 解析下载 URL...")
        v_url_info = await resolve_track_url(inner_session, best_video)
        if not v_url_info:
            print("ERROR: 无法解析视频下载 URL")
            return
        v_url, v_size = v_url_info
        print(f"  视频大小: {v_size / 1024 / 1024:.1f} MB")

        a_url = None
        a_size = 0
        if best_audio:
            a_url_info = await resolve_track_url(inner_session, best_audio)
            if a_url_info:
                a_url, a_size = a_url_info
                print(f"  音频大小: {a_size / 1024 / 1024:.1f} MB")
            else:
                print("WARNING: 无法解析音频下载 URL")

        # Step 5: 创建输出目录和文件名
        print("[5/7] 准备输出目录...")
        # 使用自定义日期和标题（来自 links.txt），否则回退到从 API 获取的信息
        if custom_date and custom_title:
            display_title = f"{custom_date}_{custom_title}_{bvid}"
        else:
            display_title = f"{info.pub_ts}_{info.title}_{bvid}"
        safe_display = filename_filter(display_title)
        safe_up = filename_filter(info.owner_name)

        episode_dir = pathlib.Path(output_dir) / safe_up
        episode_dir.mkdir(parents=True, exist_ok=True)
        print(f"  输出目录: {episode_dir}")

        video_out = episode_dir / f"{safe_display}.mp4"
        audio_out = episode_dir / f"{safe_display}.m4a"

        # Step 6: 下载视频
        print("[6/7] 下载视频...")
        video_ok = await download_file_chunks(
            inner_session, v_url, v_size, str(video_out), concurrency
        )
        if not video_ok:
            print("ERROR: 视频下载失败")
            return
        print(f"  视频下载完成: {video_out}")

        # Step 7: 下载音频 + 合并
        if best_audio and a_url:
            print("  下载音频...")
            audio_ok = await download_file_chunks(
                inner_session, a_url, a_size, str(audio_out), concurrency
            )
            if not audio_ok:
                print("ERROR: 音频下载失败")
                return
            print(f"  音频下载完成: {audio_out}")

            # FFmpeg 合并 — 用临时文件名避免 ffmpeg 报错 "same as Input"
            print("  FFmpeg 合并音视频...")
            tmp_merged = episode_dir / f"{safe_display}.tmp.mp4"
            merge_ok = merge_with_ffmpeg(ffmpeg, str(video_out), str(audio_out), str(tmp_merged))
            if merge_ok:
                # 清理中间文件
                for p in [str(video_out), str(audio_out)]:
                    if os.path.exists(p):
                        os.remove(p)
                # 如果最终文件已存在先删掉
                if os.path.exists(str(video_out)):
                    os.remove(str(video_out))
                # 重命名
                os.rename(str(tmp_merged), str(video_out))
                print(f"  合并完成: {video_out}")
            else:
                print("WARNING: 合并失败，保留原始音视频文件")
        else:
            print("  无音频，视频即为最终文件")

        print(f"\n  {'='*40}")
        print(f"  下载完成!")
        print(f"  {'='*40}")


async def main(links_file: str, cookie_file: str, output_dir: str,
               ffmpeg: str, concurrency: int, timeout_sec: int):
    """主入口：读取链接列表，逐个下载。"""
    # 加载 cookie — 支持 JSON 格式和 raw cookie 字符串格式
    with open(cookie_file, "r", encoding="utf-8") as f:
        raw = f.read().strip()

    sessdata = ""
    # 尝试 JSON 格式
    if raw.startswith("{"):
        try:
            cookie_data = json.loads(raw)
            for k, v in cookie_data.items():
                if "sessdata" in k.lower():
                    sessdata = v
                    break
        except json.JSONDecodeError:
            pass
    # 尝试 raw cookie 字符串 (key=value; key=value)
    if not sessdata:
        try:
            cookie_data = json.loads(raw)
            if isinstance(cookie_data, dict):
                for k, v in cookie_data.items():
                    if "sessdata" in k.lower():
                        sessdata = v
                        break
        except json.JSONDecodeError:
            pass
    if not sessdata:
        for part in raw.split(";"):
            part = part.strip()
            if "=" in part:
                k, v = part.split("=", 1)
                if k.strip().lower() == "sessdata":
                    sessdata = v.strip()
                    break

    if not sessdata:
        print("ERROR: cookie.json 中没有找到 SESSDATA")
        sys.exit(1)

    cookie_str = f"SESSDATA={sessdata}"
    print(f"Cookie 已加载 (SESSDATA: {sessdata[:10]}...{sessdata[-10:]})")

    # 查找 ffmpeg
    ffmpeg_bin = find_ffmpeg(ffmpeg)
    print(f"FFmpeg: {ffmpeg_bin}")

    # 读取链接 — 支持结构化格式和纯 URL 格式
    link_entries = parse_links_file(links_file)

    print(f"共 {len(link_entries)} 个链接")
    for entry in link_entries:
        print(f"  {entry['date']} | {entry['title']} | {entry['bvid']}")

    for i, entry in enumerate(link_entries, 1):
        raw_url = entry["url"]
        custom_date = entry["date"]
        custom_title = entry["title"]
        bvid = entry["bvid"]

        print(f"\n[{i}/{len(link_entries)}] 处理: {raw_url}")
        if custom_date and custom_title:
            print(f"  自定义名称: {custom_date}_{custom_title}_{bvid}")

        try:
            async with aiohttp.ClientSession() as session:
                info = await get_video_info(session, cookie_str, bvid)

                # 默认下载第一 P
                target = info.pages[0]
                if len(info.pages) == 1:
                    print(f"  单 P 视频")
                else:
                    print(f"  共 {len(info.pages)} P，默认下载第 1 P: {target.part}")
                    for p in info.pages[1:]:
                        print(f"    P{p.page}: {p.part}")

                await download_single_video(
                    session=session,
                    cookie=cookie_str,
                    bvid=bvid,
                    cid=target.cid,
                    output_dir=output_dir,
                    ffmpeg=ffmpeg_bin,
                    concurrency=concurrency,
                    timeout_sec=timeout_sec,
                    custom_date=custom_date,
                    custom_title=custom_title,
                )

        except Exception as e:
            print(f"  ERROR: 处理失败: {e}")
            import traceback
            traceback.print_exc()
            continue

        # 间隔，避免请求过快
        await asyncio.sleep(1)

    print(f"\n{'='*60}")
    print("所有任务完成!")
    print(f"{'='*60}")


# ---------------------------------------------------------------------------
# CLI
# ---------------------------------------------------------------------------

def main_cli():
    parser = argparse.ArgumentParser(
        description="Bilibili 视频下载器 (仿 bilibili-video-downloader)",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
示例:
  python bilibili_downloader.py --links links.txt --cookie cookie.json
  python bilibili_downloader.py --links links.txt --cookie cookie.json -o ./videos --concurrency 8
        """,
    )
    parser.add_argument("--links", required=True, help="链接文件路径 (支持纯 URL 或结构化格式)")
    parser.add_argument("--cookie", required=True, help="cookie.json 文件路径")
    parser.add_argument("-o", "--output", default="./downloads", help="下载目录 (默认: ./downloads)")
    parser.add_argument("--ffmpeg", default=None, help="ffmpeg 可执行文件路径 (默认: 自动检测)")
    parser.add_argument("--concurrency", type=int, default=16, help="分片并发数 (默认: 16)")
    parser.add_argument("-v", "--verbose", action="store_true", help="详细日志")
    args = parser.parse_args()

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s [%(levelname)s] %(name)s - %(message)s",
        handlers=[logging.StreamHandler(sys.stdout)],
    )

    output = pathlib.Path(args.output)
    output.mkdir(parents=True, exist_ok=True)

    asyncio.run(main(
        links_file=args.links,
        cookie_file=args.cookie,
        output_dir=str(output),
        ffmpeg=args.ffmpeg,
        concurrency=args.concurrency,
        timeout_sec=60,
    ))


if __name__ == "__main__":
    main_cli()
