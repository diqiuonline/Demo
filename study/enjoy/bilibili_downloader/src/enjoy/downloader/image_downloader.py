"""图片下载模块。"""

import logging
import re
from pathlib import Path
from typing import Any

from enjoy.utils.helpers import filename_filter

logger = logging.getLogger("bilibili_downloader")


def extract_image_id(url: str) -> str:
    """从 B站图片 URL 中提取 ID 号。
    http://i0.hdslb.com/bfs/new_dyn/89b143e51eb70b03e3a7d71250e7c16a2073801516.jpg
    → 89b143e51eb70b03e3a7d71250e7c16a2073801516
    """
    # 取最后一部分（不含扩展名）
    path_part = url.rstrip("/").rsplit("/", 1)[-1]
    dot = path_part.rfind(".")
    if dot > 0:
        return path_part[:dot]
    return path_part


def _extract_ext(url: str) -> str:
    """从 URL 提取文件扩展名。"""
    path_part = url.rstrip("/").rsplit("/", 1)[-1]
    dot = path_part.rfind(".")
    if dot > 0:
        return path_part[dot:]  # 包含 .
    return ".jpg"  # 默认


async def download_image(session: Any, url: str, output_dir: str,
                         filename: str) -> str:
    """下载单张图片到指定目录，返回本地路径。"""
    import aiohttp

    ext = _extract_ext(url)
    local_name = f"{filename}{ext}"
    local_path = Path(output_dir) / local_name
    local_path.parent.mkdir(parents=True, exist_ok=True)

    if local_path.exists():
        logger.info("图片已存在，跳过: %s", local_path)
        return str(local_path)

    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
        "Referer": "https://www.bilibili.com/",
    }
    try:
        async with session.get(url, headers=headers, timeout=30) as resp:
            if resp.status == 200:
                data = await resp.read()
                local_path.write_bytes(data)
                logger.info("图片下载完成: %s (%d bytes)", local_path, len(data))
                return str(local_path)
            else:
                logger.warning("图片下载失败 %s, 状态码 %d", url, resp.status)
    except Exception as e:
        logger.error("图片下载异常 %s: %s", url, e)

    return ""
