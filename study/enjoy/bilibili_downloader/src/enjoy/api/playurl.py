"""播放地址 API + WBI 签名封装。"""

from typing import Any


async def get_playurl_api(session: Any, bvid: str, cid: int, cookie: str) -> dict[str, Any]:
    """调用 playurl API 获取播放地址。"""
    from enjoy.downloader.signer import wbi_sign

    params = {
        "bvid": bvid,
        "cid": str(cid),
        "qn": "127",
        "fnval": "4048",
    }
    signed = await wbi_sign(params, session, cookie)

    async with session.get(
        "https://api.bilibili.com/x/player/wbi/playurl",
        params=signed,
    ) as resp:
        data = await resp.json()

    if data.get("code") != 0:
        raise RuntimeError(f"获取播放地址失败: {data.get('message')}")

    return data["data"]
