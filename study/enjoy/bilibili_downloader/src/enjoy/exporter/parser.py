"""动态数据解析器 - 从 API 响应中提取结构化数据。"""

import datetime
import time
from dataclasses import dataclass
from typing import Any

from enjoy.api.detail import fetch_detail


@dataclass
class DynamicItem:
    id_str: str
    is_video: bool
    is_only_fans: bool
    pub_date: str
    title: str
    summary: str
    video_url: str
    pics: list[str]


def _fetch_detail_fallback(dyn_id_str: str) -> tuple[str, str]:
    """【方案B狙击手】当列表页被阉割正文时，强行敲开单条详情页扣出文案。"""
    try:
        import requests
        from enjoy.api.client import USER_AGENT
        resp = requests.get(
            "https://api.bilibili.com/x/polymer/web-dynamic/v1/detail",
            params={"id": dyn_id_str},
            headers={"User-Agent": USER_AGENT},
            timeout=5,
        )
        res = resp.json()
        if res.get("code") == 0:
            item = (res.get("data") or {}).get("item") or {}
            modules = item.get("modules") or []
            if modules and isinstance(modules, list):
                dyn = (modules[0].get("module_dynamic") or {})
                desc = dyn.get("desc") or {}
                text = desc.get("text", "") if isinstance(desc, dict) else ""

                major = dyn.get("major") or {}
                title = ""
                if major.get("type") == "MAJOR_TYPE_OPUS":
                    opus = major.get("opus") or {}
                    title = opus.get("title", "")
                    if not text:
                        text = (opus.get("summary") or {}).get("text", "")
                return title, text
    except Exception:
        pass
    return "", ""


def parse_item(item: dict[str, Any]) -> DynamicItem:
    """单条动态基因智能解析器。"""
    id_str = item.get("id_str", "")
    basic = item.get("basic") or {}
    is_only_fans = basic.get("is_only_fans", False)

    # 安全解析日期
    author_mod = (item.get("modules") or {}).get("module_author") or {}
    pub_ts = author_mod.get("pub_ts", 0)
    try:
        pub_date = datetime.datetime.fromtimestamp(int(pub_ts)).strftime("%Y-%m-%d") if pub_ts else "未知日期"
    except Exception:
        pub_date = "未知日期"

    dyn = (item.get("modules") or {}).get("module_dynamic") or {}
    major = dyn.get("major") or {}
    major_type = major.get("type", "")

    title = ""
    summary = ""
    video_url = ""
    pics = []
    is_video = False

    # ---------------------------------------------------------
    # 分支一：视频投稿
    # ---------------------------------------------------------
    if major_type == "MAJOR_TYPE_ARCHIVE":
        is_video = True
        arc = major.get("archive") or {}
        title = arc.get("title", "无题视频")
        bvid = arc.get("bvid", "")
        video_url = f"https://www.bilibili.com/video/{bvid}/" if bvid else "链接解析失败"

    # ---------------------------------------------------------
    # 分支二：图文 / 日常
    # ---------------------------------------------------------
    else:
        is_video = False
        # 尝试拿新版 Opus 专栏图文
        if major_type == "MAJOR_TYPE_OPUS":
            opus = major.get("opus") or {}
            title = opus.get("title", "")
            summary = (opus.get("summary") or {}).get("text", "")
            pics = [p.get("url") for p in opus.get("pics", []) if isinstance(p, dict) and p.get("url")]

        # 尝试拿常规 Draw 相簿图文
        elif major_type == "MAJOR_TYPE_DRAW":
            draw_items = (major.get("draw") or {}).get("items", [])
            pics = [p.get("src") for p in draw_items if isinstance(p, dict) and p.get("src")]

        # 兜底拿常规文字摘要
        if not summary:
            desc_obj = dyn.get("desc")
            if desc_obj and isinstance(desc_obj, dict):
                summary = desc_obj.get("text", "")

        # 【触发方案B补漏】：如果是图文，且连正文摘要都是空的，发单条请求去"扣"！
        if not summary and not title:
            time.sleep(0.4)
            sniped_title, sniped_summary = _fetch_detail_fallback(id_str)
            if sniped_title:
                title = sniped_title
            if sniped_summary:
                summary = sniped_summary

        # 智能生成标题
        if not title:
            if summary:
                clean_sum = summary.strip().replace("\n", " ")
                title = clean_sum[:16] + "..." if len(clean_sum) > 16 else clean_sum
            else:
                title = "分享图片" if pics else "日常动态"

    return DynamicItem(
        id_str=id_str,
        is_video=is_video,
        is_only_fans=is_only_fans,
        pub_date=pub_date,
        title=title,
        summary=summary,
        video_url=video_url,
        pics=pics,
    )
