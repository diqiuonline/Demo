"""动态列表 API - 对应原 export_data.py 的 FEED_URL 请求。"""

from typing import Any

from enjoy.api.client import BiliClient

FEED_URL = "https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space"


def fetch_feed(client: BiliClient, host_mid: str, offset: str = "", features: str = "itemOpusStyle,listOnlyfans,opusBigCover,onlyfansVote") -> dict[str, Any]:
    """拉取 UP 主动态列表。"""
    params = {
        "host_mid": host_mid,
        "offset": offset,
        "features": features,
    }
    resp = client.sync_get(FEED_URL, params=params)
    data = resp.json()
    return data
