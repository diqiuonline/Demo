"""URL、BV、CID 等解析器。"""

import re
import urllib.parse
from typing import Optional


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


def extract_ep_id(url: str) -> Optional[int]:
    """从 URL 路径中提取 Epxxx 的 ep_id。"""
    parsed = urllib.parse.urlparse(url)
    for seg in parsed.path.strip("/").split("/"):
        if seg.lower().startswith("ep"):
            try:
                return int(seg[2:])
            except ValueError:
                pass
    return None


def parse_links_file(filepath: str) -> list[dict[str, str]]:
    """
    解析 links.txt，支持两种格式：
    1) 纯 URL 行: https://www.bilibili.com/video/BV1xxxx/
    2) 结构化块:
       1. 【标题】
          发布日期 : 2026-06-27
          动态链接 : ...
          视频访问 : https://www.bilibili.com/video/BV1xxxx/

    返回 [{'date': ..., 'title': ..., 'url': ..., 'bvid': ...}, ...]
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
