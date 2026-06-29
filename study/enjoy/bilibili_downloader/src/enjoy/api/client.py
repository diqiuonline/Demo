"""统一的 B站 HTTP 客户端。"""

from typing import Any

USER_AGENT = (
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36"
)
REFERRER = "https://www.bilibili.com/"


class BiliClient:
    """Bilibili API 客户端，封装同步和异步 HTTP 请求。"""

    def __init__(self, cookie: str):
        self.cookie = cookie
        self._sync_headers = {
            "User-Agent": USER_AGENT,
            "Cookie": cookie,
        }
        self._async_headers = {
            "User-Agent": USER_AGENT,
            "Referer": REFERRER,
            "Cookie": cookie,
        }

    # ---- 同步请求 (export_data 使用) ----

    def sync_get(self, url: str, params: dict | None = None, timeout: int = 10) -> Any:
        """发送同步 GET 请求，返回 Response。"""
        import requests
        resp = requests.get(url, params=params, headers=self._sync_headers, timeout=timeout)
        resp.raise_for_status()
        return resp

    def sync_post(self, url: str, data: dict | None = None, timeout: int = 10) -> Any:
        """发送同步 POST 请求，返回 Response。"""
        import requests
        resp = requests.post(url, data=data, headers=self._sync_headers, timeout=timeout)
        resp.raise_for_status()
        return resp

    # ---- 异步请求 (bilibili_downloader 使用) ----

    async def async_session(self) -> Any:
        """返回带默认 headers 的 aiohttp ClientSession。"""
        import aiohttp
        return aiohttp.ClientSession(headers=self._async_headers)

    async def async_get(
        self,
        session: Any,
        url: str,
        params: dict | None = None,
        timeout: float = 30.0,
    ) -> dict[str, Any]:
        """发送异步 GET 请求，返回解析后的 JSON。"""
        import aiohttp
        async with session.get(url, params=params, timeout=aiohttp.ClientTimeout(total=timeout)) as resp:
            return await resp.json()
