"""WBI 签名算法。"""

import hashlib
import time
from typing import Any


WBI_MIXIN_KEY_ENC_TAB = [
    46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35,
    27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13,
    37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60, 51, 30, 4,
    22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36, 20, 34, 44, 52,
]


def mixin_key(orig: str) -> str:
    """从原始密钥中提取混入密钥。"""
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


async def wbi_sign(params: dict[str, str], session: Any, cookie: str) -> dict[str, str]:
    """为参数字典添加 wts + w_rid 签名。"""
    from enjoy.api.client import USER_AGENT, REFERRER

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
