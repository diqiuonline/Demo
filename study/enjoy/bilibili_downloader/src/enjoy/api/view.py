"""视频信息 API。"""

from typing import Any, Optional


async def get_video_info_api(session: Any, bvid: str, cid: Optional[int] = None) -> dict[str, Any]:
    """调用 view API 获取视频信息。"""
    params = {"bvid": bvid}
    async with session.get(
        "https://api.bilibili.com/x/web-interface/view",
        params=params,
    ) as resp:
        data = await resp.json()

    if data.get("code") != 0:
        raise RuntimeError(f"获取视频信息失败: {data.get('message')}")

    return data["data"]
