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


def extract_opus_id(url: str) -> Optional[str]:
    """从 opus URL 中提取 opus ID。
    https://www.bilibili.com/opus/1218645606758612993 -> 1218645606758612993
    """
    parsed = urllib.parse.urlparse(url)
    parts = parsed.path.strip("/").split("/")
    for i, seg in enumerate(parts):
        if seg.lower() == "opus" and i + 1 < len(parts):
            return parts[i + 1]
    return None


def _classify_chapter(line: str) -> str | None:
    """从章节标题行推断分类。"""
    # 去掉分隔线和括号内容，只保留核心文本
    core = re.sub(r"[\(\（].*?[\)\）]", "", line)
    core = re.sub(r"^.{1,3}[、.]\s*", "", core)  # 去掉 "一、" 前缀

    if "充电" in core and "视频" in core:
        return "charged_video"
    elif "普通视频" in core or "视频投稿" in core:
        return "normal_video"
    elif "充电" in core and "图文" in core:
        return "charged_image"
    elif "普通图文" in core or "图文" in core:
        return "normal_image"
    return None


def parse_links_file(filepath: str) -> list[dict[str, str]]:
    """
    解析 links.txt，支持两种格式：
    1) 纯 URL 行: https://www.bilibili.com/video/BV1xxxx/
    2) 结构化块 (来自 export_data.py 生成的报告):
       1. 【标题】
          发布日期 : 2026-06-28
          动态链接 : https://www.bilibili.com/opus/1218892597437661192
          视频访问 : https://www.bilibili.com/video/BV16sTM6CE3x/
          包含图片 (1张):
            -> http://i0.hdslb.com/bfs/new_dyn/xxx.jpg

    返回列表，每项包含:
      - date: 发布日期
      - title: 标题
      - url: 视频/动态链接
      - bvid: BV 号 (视频才有)
      - category: normal_video / charged_video / normal_image / charged_image
      - image_urls: 图片 URL 列表 (图文才有)
      - opus_id: 动态 ID (图文才有)
    """
    with open(filepath, "r", encoding="utf-8") as f:
        text = f.read()

    results = []
    current_date = ""
    current_title = ""
    current_url = ""
    current_category = ""
    current_image_urls: list[str] = []
    current_is_video = False

    for line in text.splitlines():
        stripped = line.strip()
        if not stripped or stripped.startswith("#"):
            continue

        # 匹配章节标题: "一、 普通视频投稿动态列表 （共计: 286 个）"
        # 也匹配: "三、 普通图文 / 日常动态列表 （共计: 76 个）"
        if re.match(r"^[一二三四五]+[、.]\s*", stripped) and ("动态列表" in stripped or "列表" in stripped):
            cat = _classify_chapter(stripped)
            if cat:
                current_category = cat
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
            current_is_video = True
            bvid = extract_bvid(current_url)
            if bvid:
                results.append({
                    "date": current_date or "",
                    "title": current_title or "",
                    "url": current_url,
                    "bvid": bvid,
                    "category": current_category,
                    "image_urls": [],
                    "opus_id": "",
                })
            current_date = ""
            current_title = ""
            current_url = ""
            current_is_video = False
            continue

        # 匹配 "动态链接 : URL"
        m_dyn = re.match(r"动态链接\s*:\s*(.+)", stripped)
        if m_dyn:
            current_url = m_dyn.group(1).strip()
            opus_id = extract_opus_id(current_url)
            # 只在图文类别中保存（视频条目中动态链接行被跳过，因为后面有视频访问）
            if current_category in ("normal_image", "charged_image"):
                results.append({
                    "date": current_date or "",
                    "title": current_title or "",
                    "url": current_url,
                    "bvid": "",
                    "category": current_category,
                    "image_urls": [],
                    "opus_id": opus_id or "",
                })
                current_date = ""
                current_title = ""
            current_url = ""
            continue

        # 匹配 "-> URL" (图片行)
        m_pic = re.match(r"->\s*(https?://[^\s]+)", stripped)
        if m_pic:
            pic_url = m_pic.group(1).strip()
            if results:
                results[-1]["image_urls"].append(pic_url)
            continue

        # 纯 URL 行（无标题/日期）— 向后兼容
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
                    "category": current_category or "normal_video",
                    "image_urls": [],
                    "opus_id": "",
                })
            current_date = ""
            current_title = ""
            current_url = ""
            continue

    return results
