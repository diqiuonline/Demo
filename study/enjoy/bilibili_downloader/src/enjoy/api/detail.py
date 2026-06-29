"""动态详情 API - 对应原 export_data.py 的 DETAIL_URL 请求。"""

from typing import Any

from enjoy.api.client import BiliClient

DETAIL_URL = "https://api.bilibili.com/x/polymer/web-dynamic/v1/detail"


def fetch_detail(client: BiliClient, dyn_id: str) -> dict[str, Any] | None:
    """获取单条动态详情（用于补漏被阉割的正文）。"""
    try:
        resp = client.sync_get(DETAIL_URL, params={"id": dyn_id}, timeout=5)
        data = resp.json()
        if data.get("code") == 0:
            return data
    except Exception:
        pass
    return None
