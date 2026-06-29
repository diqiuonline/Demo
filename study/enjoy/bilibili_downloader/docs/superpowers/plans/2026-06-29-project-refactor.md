# Project Refactor Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor two monolithic Python scripts (`export_data.py`, `bilibili_downloader.py`) into a standard `src/` structured Python package with unified CLI entry point.

**Architecture:** Extract shared logic into modular packages under `src/enjoy/`, create a CLI dispatcher with subcommands (`export`, `download`), move configuration to `config/config.yaml`.

**Tech Stack:** Python 3.12+, aiohttp, requests, PyYAML

## Global Constraints

- Preserve all business logic and output formats exactly (TXT report format, download behavior)
- Support both `python -m enjoy` and direct script invocation (`python src/enjoy/__main__.py`)
- CLI arguments override config.yaml defaults
- Keep original files temporarily during transition (marked deprecated)

---

### Task 1: Scaffold project structure and configuration

**Files:**
- Create: `config/config.yaml`
- Create: `config/config.example.yaml`
- Create: `requirements.txt`
- Create: `README.md`

**Interfaces:**
- Produces: Configuration schema that all later tasks depend on

- [ ] **Step 1: Create config/config.yaml**

```yaml
# Bilibili 工具配置
bilibili:
  mid: "2073801516"          # 目标 UP 主 ID
  output_file: "言言抱枕_全量动态汇总报告.txt"
  cookie: ""                 # 留空则在 CLI 中传入

download:
  output_dir: "./downloads"
  concurrency: 16
  timeout_sec: 60
  ffmpeg: ""                 # 留空则自动检测
```

- [ ] **Step 2: Create config/config.example.yaml**

```yaml
# 配置模板 - 复制为 config.yaml 并填入实际值
bilibili:
  mid: "YOUR_UP主_MID"
  output_file: "动态汇总报告.txt"
  cookie: ""

download:
  output_dir: "./downloads"
  concurrency: 16
  timeout_sec: 60
  ffmpeg: ""
```

- [ ] **Step 3: Create requirements.txt**

```
requests>=2.31
aiohttp>=3.9
PyYAML>=6.0
```

- [ ] **Step 4: Create README.md**

```markdown
# Enjoy - Bilibili 工具集

B站 UP 主动态导出 + 视频下载工具。

## 安装

```bash
pip install -r requirements.txt
```

## 配置

```bash
cp config/config.example.yaml config/config.yaml
# 编辑 config.yaml 填入 cookie 等信息
```

## 用法

```bash
# 导出动态汇总
python -m enjoy export --mid 2073801516

# 下载视频
python -m enjoy download --links links.txt --cookie cookie.json

# 查看帮助
python -m enjoy --help
```
```

- [ ] **Step 5: Commit**

```bash
git add config/ requirements.txt README.md
git commit -m "chore: add project scaffolding and configuration"
```

---

### Task 2: Create package skeleton and config loader

**Files:**
- Create: `src/enjoy/__init__.py`
- Create: `src/enjoy/config_loader.py`

**Interfaces:**
- Consumes: `config/config.yaml` from Task 1
- Produces: `load_config() -> dict` used by all CLI commands

- [ ] **Step 1: Create src/enjoy/__init__.py**

```python
"""Enjoy - Bilibili 工具集"""

__version__ = "0.1.0"
```

- [ ] **Step 2: Create src/enjoy/config_loader.py**

```python
"""配置加载器 - 从 config.yaml 加载默认配置"""

import os
from pathlib import Path
from typing import Any

import yaml


def _config_path() -> Path:
    """查找配置文件路径。"""
    candidates = [
        Path(__file__).parent.parent.parent / "config" / "config.yaml",
        Path.cwd() / "config" / "config.yaml",
    ]
    for p in candidates:
        if p.exists():
            return p
    # 默认使用 example
    fallback = Path(__file__).parent.parent.parent / "config" / "config.example.yaml"
    if fallback.exists():
        return fallback
    raise FileNotFoundError("未找到 config.yaml，请复制 config.example.yaml 并配置")


def load_config() -> dict[str, Any]:
    """加载并返回配置字典。"""
    path = _config_path()
    with open(path, "r", encoding="utf-8") as f:
        return yaml.safe_load(f)


def get_cookie(config: dict[str, Any], cookie_cli: str | None = None) -> str:
    """
    获取 Cookie 字符串。
    优先级: CLI 参数 > config.yaml > 环境变量
    """
    if cookie_cli:
        return cookie_cli
    env_cookie = os.environ.get("BILIBILI_COOKIE")
    if env_cookie:
        return env_cookie
    bilibili_cfg = config.get("bilibili", {})
    cookie = bilibili_cfg.get("cookie", "")
    if not cookie:
        raise ValueError(
            "未提供 Cookie。请在 config.yaml 中设置，或通过 --cookie 参数传入，"
            "或设置环境变量 BILIBILI_COOKIE"
        )
    return cookie
```

- [ ] **Step 3: Commit**

```bash
git add src/enjoy/__init__.py src/enjoy/config_loader.py
git commit -m "feat: add package skeleton and config loader"
```

---

### Task 3: Create shared utilities

**Files:**
- Create: `src/enjoy/utils/__init__.py`
- Create: `src/enjoy/utils/helpers.py`

**Interfaces:**
- Produces: `filename_filter()`, `setup_logging()` used by all modules

- [ ] **Step 1: Create src/enjoy/utils/__init__.py**

```python
"""通用工具模块"""
```

- [ ] **Step 2: Create src/enjoy/utils/helpers.py**

```python
"""文件名过滤、日志配置等通用工具。"""

import logging
import sys
from pathlib import Path


def filename_filter(s: str) -> str:
    """过滤文件名中的非法字符。"""
    s = s.replace("\\", " ").replace("/", " ").replace("\n", " ")
    s = s.replace(":", "：").replace("*", "★").replace("?", "？")
    s = s.replace('"', "'").replace("<", "《").replace(">", "》").replace("|", "「")
    return s.strip().rstrip(".").strip()


def setup_logging(verbose: bool = False) -> None:
    """配置全局日志。"""
    level = logging.DEBUG if verbose else logging.INFO
    logging.basicConfig(
        level=level,
        format="%(asctime)s [%(levelname)s] %(name)s - %(message)s",
        handlers=[logging.StreamHandler(sys.stdout)],
    )


def ensure_dir(path: Path) -> Path:
    """确保目录存在，不存在则创建。"""
    path.mkdir(parents=True, exist_ok=True)
    return path
```

- [ ] **Step 3: Commit**

```bash
git add src/enjoy/utils/
git commit -m "feat: add shared utility functions"
```

---

### Task 4: Create API client layer

**Files:**
- Create: `src/enjoy/api/__init__.py`
- Create: `src/enjoy/api/client.py`

**Interfaces:**
- Produces: `BiliClient` class with `sync_get()`, `async_session()` methods
- Consumes: Cookie from config_loader

- [ ] **Step 1: Create src/enjoy/api/__init__.py**

```python
"""Bilibili API 客户端"""
```

- [ ] **Step 2: Create src/enjoy/api/client.py**

```python
"""统一的 B站 HTTP 客户端。"""

import asyncio
from typing import Any

import aiohttp
import requests

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

    def sync_get(self, url: str, params: dict | None = None, timeout: int = 10) -> requests.Response:
        """发送 GET 请求，返回 Response。"""
        resp = requests.get(url, params=params, headers=self._sync_headers, timeout=timeout)
        resp.raise_for_status()
        return resp

    def sync_post(self, url: str, data: dict | None = None, timeout: int = 10) -> requests.Response:
        """发送 POST 请求，返回 Response。"""
        resp = requests.post(url, data=data, headers=self._sync_headers, timeout=timeout)
        resp.raise_for_status()
        return resp

    # ---- 异步请求 (bilibili_downloader 使用) ----

    async def async_session(self) -> aiohttp.ClientSession:
        """返回带默认 headers 的 ClientSession。"""
        return aiohttp.ClientSession(headers=self._async_headers)

    async def async_get(
        self,
        session: aiohttp.ClientSession,
        url: str,
        params: dict | None = None,
        timeout: float = 30.0,
    ) -> dict[str, Any]:
        """发送异步 GET 请求，返回解析后的 JSON。"""
        async with session.get(url, params=params, timeout=aiohttp.ClientTimeout(total=timeout)) as resp:
            return await resp.json()
```

- [ ] **Step 3: Commit**

```bash
git add src/enjoy/api/
git commit -m "feat: add shared API client"
```

---

### Task 5: Create exporter module (from export_data.py)

**Files:**
- Create: `src/enjoy/exporter/__init__.py`
- Create: `src/enjoy/exporter/parser.py`
- Create: `src/enjoy/exporter/reporter.py`
- Create: `src/enjoy/api/feed.py`
- Create: `src/enjoy/api/detail.py`

**Interfaces:**
- Consumes: `BiliClient` from Task 4
- Produces: `ExportResult` dataclass, `generate_report()` function

- [ ] **Step 1: Create src/enjoy/exporter/__init__.py**

```python
"""B站动态导出模块"""
```

- [ ] **Step 2: Create src/enjoy/api/feed.py**

```python
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
```

- [ ] **Step 3: Create src/enjoy/api/detail.py**

```python
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
```

- [ ] **Step 4: Create src/enjoy/exporter/parser.py**

```python
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


def parse_item(item: dict[str, Any]) -> DynamicItem:
    """单条动态智能解析器。"""
    id_str = item.get("id_str", "")
    basic = item.get("basic") or {}
    is_only_fans = basic.get("is_only_fans", False)

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

    # 视频投稿
    if major_type == "MAJOR_TYPE_ARCHIVE":
        is_video = True
        arc = major.get("archive") or {}
        title = arc.get("title", "无题视频")
        bvid = arc.get("bvid", "")
        video_url = f"https://www.bilibili.com/video/{bvid}/" if bvid else "链接解析失败"

    # 图文 / 日常
    else:
        is_video = False
        if major_type == "MAJOR_TYPE_OPUS":
            opus = major.get("opus") or {}
            title = opus.get("title", "")
            summary = (opus.get("summary") or {}).get("text", "")
            pics = [p.get("url") for p in opus.get("pics", []) if isinstance(p, dict) and p.get("url")]
        elif major_type == "MAJOR_TYPE_DRAW":
            draw_items = (major.get("draw") or {}).get("items", [])
            pics = [p.get("src") for p in draw_items if isinstance(p, dict) and p.get("src")]

        if not summary:
            desc_obj = dyn.get("desc")
            if desc_obj and isinstance(desc_obj, dict):
                summary = desc_obj.get("text", "")

        # 补漏：正文为空时请求详情页
        if not summary and not title:
            time.sleep(0.4)
            detail_data = fetch_detail(None, id_str)  # 由 caller 注入 client
            if detail_data:
                detail_item = (detail_data.get("data") or {}).get("item") or {}
                modules = detail_item.get("modules") or []
                if modules:
                    detail_dyn = (modules[0].get("module_dynamic") or {})
                    desc = detail_dyn.get("desc") or {}
                    text = desc.get("text", "") if isinstance(desc, dict) else ""
                    detail_major = detail_dyn.get("major") or {}
                    if detail_major.get("type") == "MAJOR_TYPE_OPUS":
                        opus = detail_major.get("opus") or {}
                        title = opus.get("title", "")
                        if not text:
                            text = (opus.get("summary") or {}).get("text", "")
                    if not title:
                        title = text
                    if not summary:
                        summary = text

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
```

- [ ] **Step 5: Create src/enjoy/exporter/reporter.py**

```python
"""TXT 报告生成器。"""

from pathlib import Path
from typing import Any

from enjoy.exporter.parser import DynamicItem


def generate_report(
    items: list[DynamicItem],
    output_path: str | Path,
) -> None:
    """生成分类汇总 TXT 报告。"""
    normal_videos = [i for i in items if i.is_video and not i.is_only_fans]
    charged_videos = [i for i in items if i.is_video and i.is_only_fans]
    normal_draws = [i for i in items if not i.is_video and not i.is_only_fans]
    charged_draws = [i for i in items if not i.is_video and i.is_only_fans]
    total_pics = sum(len(i.pics) for i in items)

    lines: list[str] = []

    def write_section(title: str, items_list: list[DynamicItem]) -> None:
        lines.append("=" * 77)
        lines.append(f"{title} （共计: {len(items_list)} 个）")
        lines.append("=" * 77)
        lines.append("")
        for idx, item in enumerate(items_list, 1):
            lines.append(f"{idx}. 【{item.title}】")
            lines.append(f"   发布日期 : {item.pub_date}")
            lines.append(f"   动态链接 : https://www.bilibili.com/opus/{item.id_str}")
            if item.is_video:
                lines.append(f"   视频访问 : {item.video_url}")
            lines.append("")

    write_section("一、 普通视频投稿动态列表", normal_videos)
    write_section("二、 充电/付费视频动态列表", charged_videos)

    # 图文类
    lines.extend(["=" * 77, "三、 普通图文 / 日常动态列表 （共计: {0} 个）".format(len(normal_draws)), "=" * 77, ""])
    for idx, item in enumerate(normal_draws, 1):
        lines.append(f"{idx}. 【{item.title}】")
        lines.append(f"   发布日期 : {item.pub_date}")
        lines.append(f"   动态链接 : https://www.bilibili.com/opus/{item.id_str}")
        sum_text = item.summary.strip()
        if sum_text:
            indented = sum_text.replace("\n", "\n             ")
            lines.append(f"   内容摘要 : {indented}")
        else:
            lines.append("   内容摘要 : [无文字摘要]")
        if item.pics:
            lines.append(f"   包含图片 ({len(item.pics)}张):")
            for p in item.pics:
                lines.append(f"     -> {p}")
        else:
            lines.append("   包含图片 : [纯文字动态，无图片]")
        lines.append("")

    lines.extend(["=" * 77, "四、 充电/付费图文动态列表 （共计: {0} 个）".format(len(charged_draws)), "=" * 77, ""])
    for idx, item in enumerate(charged_draws, 1):
        lines.append(f"{idx}. 【{item.title}】")
        lines.append(f"   发布日期 : {item.pub_date}")
        lines.append(f"   动态链接 : https://www.bilibili.com/opus/{item.id_str}")
        sum_text = item.summary.strip()
        if sum_text:
            indented = sum_text.replace("\n", "\n             ")
            lines.append(f"   内容摘要 : {indented}")
        else:
            lines.append("   内容摘要 : [无文字摘要]")
        if item.pics:
            lines.append(f"   包含图片 ({len(item.pics)}张):")
            for p in item.pics:
                lines.append(f"     -> {p}")
        else:
            lines.append("   包含图片 : [纯文字动态，无图片]")
        lines.append("")

    # 汇总
    lines.extend([
        "=" * 77,
        "五、 统计汇总",
        "=" * 77,
        "",
        f"普通视频动态: {len(normal_videos)} 个",
        f"充电视频动态: {len(charged_videos)} 个",
        f"普通图文动态: {len(normal_draws)} 个",
        f"充电图文动态: {len(charged_draws)} 个",
        f"图片总数: {total_pics} 张",
    ])

    with open(output_path, "w", encoding="utf-8") as f:
        f.write("\n".join(lines) + "\n")
```

- [ ] **Step 6: Commit**

```bash
git add src/enjoy/exporter/ src/enjoy/api/feed.py src/enjoy/api/detail.py
git commit -m "feat: add exporter module with parser and reporter"
```

---

### Task 6: Create downloader module (from bilibili_downloader.py)

**Files:**
- Create: `src/enjoy/downloader/__init__.py`
- Create: `src/enjoy/downloader/signer.py`
- Create: `src/enjoy/downloader/extractor.py`
- Create: `src/enjoy/downloader/fetcher.py`
- Create: `src/enjoy/downloader/track.py`

**Interfaces:**
- Consumes: `BiliClient` from Task 4
- Produces: `download_single_video()`, `parse_links_file()`, `is_mp4_complete()`

- [ ] **Step 1: Create src/enjoy/downloader/__init__.py**

```python
"""B站视频下载模块"""
```

- [ ] **Step 2: Create src/enjoy/downloader/signer.py**

```python
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
    import aiohttp
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

    wts = int(time.time())
    params["wts"] = str(wts)
    params = dict(sorted(params.items()))

    query = "&".join(f"{url_encode(k)}={url_encode(v)}" for k, v in params.items())
    w_rid = hashlib.md5((query + mixin_key_str).encode()).hexdigest()
    params["w_rid"] = w_rid
    return params
```

- [ ] **Step 3: Create src/enjoy/downloader/extractor.py**

```python
"""URL、BV、CID 等解析器。"""

import re
import urllib.parse
from pathlib import Path
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
    1) 纯 URL 行
    2) 结构化块（含标题、日期、动态链接、视频访问）
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

        m = re.match(r"发布日期\s*:\s*(\d{4}-\d{2}-\d{2})", stripped)
        if m:
            current_date = m.group(1)
            continue

        m2 = re.match(r"\d*\.\s*【(.+?)】", stripped)
        if not m2:
            m2 = re.match(r"【(.+?)】", stripped)
        if m2:
            current_title = m2.group(1)
            continue

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
            current_date = ""
            current_title = ""
            current_url = ""
            continue

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
            current_date = ""
            current_title = ""
            current_url = ""
            continue

    return results
```

- [ ] **Step 4: Create src/enjoy/downloader/fetcher.py**

```python
"""分片下载、MP4 完整性校验、FFmpeg 合并。"""

import asyncio
import logging
import math
import os
import pathlib
import shutil
import struct
import subprocess
import time
from typing import Any, Optional

logger = logging.getLogger("bilibili_downloader")

CHUNK_SIZE = 2 * 1024 * 1024  # 2 MB


def is_mp4_complete(file_path: str) -> bool:
    """
    检查 MP4 文件是否完整：
    1. 第一个 box 必须是 ftyp
    2. 必须包含 moov box
    3. 所有 box 的总大小必须等于文件实际大小
    4. moov box 的 size 为 0 表示延伸到文件末尾
    """
    real_size = os.path.getsize(file_path)
    with open(file_path, "rb") as f:
        total_size = 0
        has_moov = False
        is_first = True

        while True:
            header = f.read(8)
            if len(header) < 8:
                break

            box_size = struct.unpack(">I", header[:4])[0]
            box_type = header[4:8].decode("ascii", errors="replace")

            if is_first:
                if box_type != "ftyp":
                    return False
                is_first = False

            if box_type == "moov":
                has_moov = True

            if box_size == 0:
                return True

            if box_size == 1:
                large_header = f.read(8)
                if len(large_header) < 8:
                    return False
                box_size = struct.unpack(">Q", large_header)[0]
                if box_size < 16:
                    return False
                skip = box_size - 16
                f.seek(skip, 1)
                total_size += box_size
            else:
                if box_size < 8:
                    return False
                f.seek(box_size - 8, 1)
                total_size += box_size

            if total_size > real_size:
                return False

    return real_size == total_size and has_moov


async def get_content_length(session: Any, url: str) -> Optional[int]:
    """用 HEAD 获取 Content-Length，失败则降级 Range: bytes=0-0。"""
    from enjoy.api.client import USER_AGENT, REFERRER
    headers = {"User-Agent": USER_AGENT, "Referer": REFERRER}
    try:
        async with session.head(url, headers=headers, timeout=aiohttp.ClientTimeout(total=5)) as resp:
            if resp.status == 200:
                cl = resp.headers.get("Content-Length")
                if cl:
                    return int(cl)
    except Exception:
        pass
    try:
        range_headers = {
            "User-Agent": USER_AGENT,
            "Referer": REFERRER,
            "Range": "bytes=0-0",
        }
        async with session.get(url, headers=range_headers, timeout=aiohttp.ClientTimeout(total=5)) as resp:
            cr = resp.headers.get("Content-Range")
            if cr and "/" in cr:
                return int(cr.rsplit("/", 1)[1])
            cl = resp.headers.get("Content-Length")
            if cl:
                return int(cl)
    except Exception:
        pass
    return None


async def get_url_with_backup(session: Any, base_url: str, backup_urls: list[str]) -> Optional[tuple[str, int]]:
    """尝试 base_url + backup_urls，返回 (url, content_length)。"""
    urls = list(backup_urls) + [base_url]
    for u in urls:
        cl = await get_content_length(session, u)
        if cl and cl > 0:
            return (u, cl)
    return None


async def download_chunk(session: Any, url: str, start: int, end: int, file: Any, progress_callback=None) -> bool:
    """下载单个 chunk 并写入文件指定偏移。"""
    from enjoy.api.client import USER_AGENT, REFERRER
    headers = {
        "User-Agent": USER_AGENT,
        "Referer": REFERRER,
        "Range": f"bytes={start}-{end}",
    }
    try:
        async with session.get(url, headers=headers) as resp:
            if resp.status != 206:
                logger.error("chunk %d-%d 返回状态码 %d", start, end, resp.status)
                return False
            data = await resp.read()
            file.seek(start)
            file.write(data)
            if progress_callback:
                progress_callback(len(data))
            return True
    except Exception as e:
        logger.error("chunk %d-%d 下载失败: %s", start, end, e)
        return False


async def download_file_chunks(session: Any, url: str, content_length: int, output_path: str, concurrency: int = 16) -> bool:
    """
    将文件按 CHUNK_SIZE (2MB) 切分成多个 chunk，并发下载后合并。
    支持断点续传。
    """
    import aiohttp

    temp_path = output_path + ".downloading"

    chunks = []
    num_chunks = math.ceil(content_length / CHUNK_SIZE)
    for i in range(num_chunks):
        start = i * CHUNK_SIZE
        end = min(start + CHUNK_SIZE, content_length) - 1
        chunks.append((start, end))

    completed_chunks: set[int] = set()
    if os.path.exists(temp_path):
        existing_size = os.path.getsize(temp_path)
        if existing_size == content_length:
            logger.info("发现完整临时文件，跳过下载")
            os.replace(temp_path, output_path)
            return True

    semaphore = asyncio.Semaphore(concurrency)
    download_done = [False] * len(chunks)

    if not os.path.exists(temp_path):
        with open(temp_path, "wb") as f:
            f.truncate(content_length)

    async def _download(idx: int):
        async with semaphore:
            start, end = chunks[idx]
            if idx in completed_chunks:
                download_done[idx] = True
                return
            with open(temp_path, "r+b") as f:
                ok = await download_chunk(session, url, start, end, f)
                if ok:
                    completed_chunks.add(idx)
                    download_done[idx] = True

    tasks = [asyncio.create_task(_download(i)) for i in range(len(chunks))]
    await asyncio.gather(*tasks)

    if not all(download_done):
        logger.error("部分 chunk 下载失败")
        return False

    if not is_mp4_complete(temp_path):
        logger.error("文件完整性校验失败")
        if os.path.exists(temp_path):
            os.remove(temp_path)
        return False

    if os.path.exists(output_path):
        os.remove(output_path)
    os.replace(temp_path, output_path)
    return True


def find_ffmpeg(ffmpeg_path_arg: Optional[str]) -> str:
    """查找 ffmpeg 可执行文件。"""
    if ffmpeg_path_arg:
        return ffmpeg_path_arg

    path = shutil.which("ffmpeg")
    if path:
        return path

    script_dir = pathlib.Path(__file__).parent.parent.parent.resolve()
    for candidate in script_dir.glob("src-tauri/ffmpeg/*.exe"):
        return str(candidate)

    raise FileNotFoundError(
        "未找到 ffmpeg。请安装 ffmpeg 或将 --ffmpeg 指向可执行文件路径。"
    )


def merge_with_ffmpeg(ffmpeg: str, video_path: str, audio_path: str, output_path: str) -> bool:
    """用 ffmpeg 合并音视频 (流复制，不重新编码)。"""
    cmd = [
        ffmpeg, "-y",
        "-i", video_path,
        "-i", audio_path,
        "-c", "copy",
        "-map", "0:v:0",
        "-map", "1:a:0",
        output_path,
    ]

    try:
        result = subprocess.run(cmd, capture_output=True, timeout=3600)
        if result.returncode != 0:
            logger.error("ffmpeg 合并失败: %s", result.stderr.decode(errors="replace"))
            return False
        for p in [video_path, audio_path]:
            if os.path.exists(p):
                os.remove(p)
        return True
    except Exception as e:
        logger.error("ffmpeg 合并异常: %s", e)
        return False
```

- [ ] **Step 5: Create src/enjoy/downloader/track.py**

```python
"""音视频轨道选择 + 视频信息获取。"""

import asyncio
import logging
import sys
import time
from dataclasses import dataclass, field
from typing import Any, Optional

from enjoy.downloader.extractor import extract_bvid, extract_cid
from enjoy.downloader.fetcher import (
    download_file_chunks,
    find_ffmpeg,
    merge_with_ffmpeg,
)
from enjoy.downloader.signer import wbi_sign
from enjoy.utils.helpers import filename_filter

logger = logging.getLogger("bilibili_downloader")

QUALITY_ORDER = {
    127: 0, 126: 1, 125: 2, 120: 3, 116: 4, 112: 5, 80: 6,
    100: 7, 74: 8, 64: 9, 32: 10, 16: 11, 6: 12,
}

AUDIO_QUALITY_ORDER = {
    30251: 0, 30250: 1, 30280: 2, 30232: 3, 30216: 4,
}


@dataclass
class VideoTrack:
    id: int
    quality: int
    codecid: int
    bandwidth: int
    codecs: str
    base_url: str
    backup_urls: list[str]
    width: int
    height: int
    frame_rate: str
    content_length: int = 0
    url: str = ""


@dataclass
class AudioTrack:
    id: int
    audio_quality: int
    bandwidth: int
    codecs: str
    base_url: str
    backup_urls: list[str]
    content_length: int = 0
    url: str = ""


@dataclass
class PageInfo:
    cid: int
    page: int
    part: str
    duration: int
    bvid: str = ""
    aid: int = 0


@dataclass
class VideoInfo:
    bvid: str
    title: str
    owner_name: str
    pic: str
    pages: list[PageInfo]
    ugc_season: Optional[dict] = None
    pub_ts: int = 0


async def get_video_info(session: Any, cookie: str, bvid: str, cid: Optional[int] = None) -> VideoInfo:
    """调用 view API 获取视频信息。"""
    params = {"bvid": bvid}
    async with session.get(
        "https://api.bilibili.com/x/web-interface/view",
        params=params,
    ) as resp:
        data = await resp.json()

    if data.get("code") != 0:
        raise RuntimeError(f"获取视频信息失败: {data.get('message')} / {data}")

    info = data["data"]
    pages = []
    for p in info.get("pages", []):
        pages.append(PageInfo(
            cid=p["cid"],
            page=p.get("page", 1),
            part=p.get("part", ""),
            duration=p.get("duration", 0),
            bvid=bvid,
            aid=info.get("aid", 0),
        ))

    return VideoInfo(
        bvid=info["bvid"],
        title=info["title"],
        owner_name=info["owner"]["name"],
        pic=info["pic"],
        pages=pages,
        ugc_season=info.get("ugc_season"),
    )


async def get_playurl(session: Any, cookie: str, bvid: str, cid: int) -> tuple[list[VideoTrack], list[AudioTrack]]:
    """调用 playurl API 获取音视频轨道列表。"""
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
        raise RuntimeError(f"获取播放地址失败: {data.get('message')} / {data}")

    dash = data["data"]["dash"]
    video_tracks: list[VideoTrack] = []
    audio_tracks: list[AudioTrack] = []

    for v in dash.get("video", []):
        bt = v.get("base_url", "")
        bu = v.get("backup_url", [])
        if isinstance(bu, str):
            bu = [bu]
        vt = VideoTrack(
            id=v["id"],
            quality=v["id"],
            codecid=v.get("codecid", 0),
            bandwidth=v.get("bandwidth", 0),
            codecs=v.get("codecs", ""),
            base_url=bt,
            backup_urls=bu,
            width=v.get("width", 0),
            height=v.get("height", 0),
            frame_rate=v.get("frame_rate", ""),
        )
        video_tracks.append(vt)

    for a in dash.get("audio", []):
        bt = a.get("base_url", "")
        bu = a.get("backup_url", [])
        if isinstance(bu, str):
            bu = [bu]
        at = AudioTrack(
            id=a["id"],
            audio_quality=a["id"],
            bandwidth=a.get("bandwidth", 0),
            codecs=a.get("codecs", ""),
            base_url=bt,
            backup_urls=bu,
        )
        audio_tracks.append(at)

    return video_tracks, audio_tracks


def select_best_video(tracks: list[VideoTrack]) -> Optional[VideoTrack]:
    """按画质优先级 + 编码优先级选择最佳视频轨道。"""
    if not tracks:
        return None

    def sort_key(v: VideoTrack) -> tuple[int, int]:
        q = QUALITY_ORDER.get(v.quality, 999)
        codec_pref = {7: 0, 12: 1, 13: 2}.get(v.codecid, 3)
        return (q, codec_pref)

    tracks_sorted = sorted(tracks, key=sort_key)
    return tracks_sorted[0]


def select_best_audio(tracks: list[AudioTrack]) -> Optional[AudioTrack]:
    """按音质优先级选择最佳音频轨道。"""
    if not tracks:
        return None

    def sort_key(a: AudioTrack) -> int:
        return AUDIO_QUALITY_ORDER.get(a.audio_quality, 999)

    tracks_sorted = sorted(tracks, key=sort_key)
    return tracks_sorted[0]


async def resolve_track_url(session: Any, track: Any) -> Optional[tuple[str, int]]:
    """为轨道解析出可用的下载 URL + 文件大小。"""
    from enjoy.downloader.fetcher import get_url_with_backup
    result = await get_url_with_backup(session, track.base_url, track.backup_urls)
    return result


async def download_single_video(
    session: Any, cookie: str, bvid: str, cid: int, output_dir: str,
    ffmpeg: str, concurrency: int = 16,
    custom_date: str = "", custom_title: str = "",
) -> None:
    """下载单个视频的完整流程。"""
    if hasattr(sys.stdout, "reconfigure"):
        sys.stdout.reconfigure(encoding="utf-8")

    from enjoy.api.client import REFERRER

    print(f"\n{'='*60}")
    print(f"开始下载: {bvid}  CID={cid}")
    print(f"{'='*60}")

    # Step 1: 获取视频信息
    print("[1/7] 获取视频信息...")
    try:
        info = await get_video_info(session, cookie, bvid, cid)
    except Exception as e:
        print(f"ERROR: 获取视频信息失败: {e}")
        return

    page = info.pages[0]
    if cid:
        page = next((p for p in info.pages if p.cid == cid), None)
        if not page:
            print(f"ERROR: 找不到 cid={cid} 对应的分 P")
            return
    cid = page.cid

    part_title = page.part or info.title
    safe_title = filename_filter(page.part or info.title)
    safe_up = filename_filter(info.owner_name)

    print(f"  标题: {info.title}")
    print(f"  UP主: {info.owner_name}")
    print(f"  分P:  {page.page}/{len(info.pages)} - {part_title}")

    # Step 2: 获取播放地址
    print("[2/7] 获取播放地址...")
    try:
        video_tracks, audio_tracks = await get_playurl(session, cookie, bvid, cid)
    except Exception as e:
        print(f"ERROR: 获取播放地址失败: {e}")
        return

    # Step 3: 选择最佳轨道
    print("[3/7] 选择最佳画质...")
    best_video = select_best_video(video_tracks)
    best_audio = select_best_audio(audio_tracks)

    if not best_video:
        print("ERROR: 没有可用的视频轨道")
        return

    if not best_audio:
        print("WARNING: 没有可用的音频轨道，仅下载视频")

    print(f"  视频: 画质={best_video.quality}  编码={best_video.codecid}  "
          f"{best_video.width}x{best_video.height}  "
          f"码率={best_video.bandwidth/1000:.1f}kbps")
    if best_audio:
        print(f"  音频: 音质={best_audio.audio_quality}  编码={best_audio.codecs}  "
              f"码率={best_audio.bandwidth/1000:.1f}kbps")

    # Step 4: 解析下载 URL
    print("[4/7] 解析下载 URL...")
    v_url_info = await resolve_track_url(session, best_video)
    if not v_url_info:
        print("ERROR: 无法解析视频下载 URL")
        return
    v_url, v_size = v_url_info
    print(f"  视频大小: {v_size / 1024 / 1024:.1f} MB")

    a_url = None
    a_size = 0
    if best_audio:
        a_url_info = await resolve_track_url(session, best_audio)
        if a_url_info:
            a_url, a_size = a_url_info
            print(f"  音频大小: {a_size / 1024 / 1024:.1f} MB")
        else:
            print("WARNING: 无法解析音频下载 URL")

    # Step 5: 创建输出目录
    print("[5/7] 准备输出目录...")
    if custom_date and custom_title:
        display_title = f"{custom_date}_{custom_title}_{bvid}"
    else:
        display_title = f"{info.title}_{bvid}"
    safe_display = filename_filter(display_title)

    import pathlib
    episode_dir = pathlib.Path(output_dir) / safe_up
    episode_dir.mkdir(parents=True, exist_ok=True)
    print(f"  输出目录: {episode_dir}")

    video_out = episode_dir / f"{safe_display}.mp4"
    audio_out = episode_dir / f"{safe_display}.m4a"

    # Step 6: 下载视频
    print("[6/7] 下载视频...")
    video_ok = await download_file_chunks(session, v_url, v_size, str(video_out), concurrency)
    if not video_ok:
        print("ERROR: 视频下载失败")
        return
    print(f"  视频下载完成: {video_out}")

    # Step 7: 下载音频 + 合并
    if best_audio and a_url:
        print("  下载音频...")
        audio_ok = await download_file_chunks(session, a_url, a_size, str(audio_out), concurrency)
        if not audio_ok:
            print("ERROR: 音频下载失败")
            return
        print(f"  音频下载完成: {audio_out}")

        print("  FFmpeg 合并音视频...")
        tmp_merged = episode_dir / f"{safe_display}.tmp.mp4"
        merge_ok = merge_with_ffmpeg(ffmpeg, str(video_out), str(audio_out), str(tmp_merged))
        if merge_ok:
            for p in [str(video_out), str(audio_out)]:
                import os
                if os.path.exists(p):
                    os.remove(p)
            if os.path.exists(str(video_out)):
                os.remove(str(video_out))
            os.rename(str(tmp_merged), str(video_out))
            print(f"  合并完成: {video_out}")
        else:
            print("WARNING: 合并失败，保留原始音视频文件")
    else:
        print("  无音频，视频即为最终文件")

    print(f"\n  {'='*40}")
    print(f"  下载完成!")
    print(f"  {'='*40}")
```

- [ ] **Step 6: Commit**

```bash
git add src/enjoy/downloader/
git commit -m "feat: add downloader module with signer, extractor, fetcher, track"
```

---

### Task 7: Create API modules for downloader

**Files:**
- Create: `src/enjoy/api/view.py`
- Create: `src/enjoy/api/playurl.py`

**Interfaces:**
- Consumes: `BiliClient` from Task 4
- Produces: `get_video_info()`, `get_playurl()` convenience functions

- [ ] **Step 1: Create src/enjoy/api/view.py**

```python
"""视频信息 API。"""

from typing import Any, Optional

from enjoy.api.client import BiliClient


async def get_video_info(client: BiliClient, session: Any, bvid: str, cid: Optional[int] = None) -> dict[str, Any]:
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
```

- [ ] **Step 2: Create src/enjoy/api/playurl.py**

```python
"""播放地址 API + WBI 签名封装。"""

from typing import Any, Optional

from enjoy.api.client import BiliClient
from enjoy.downloader.signer import wbi_sign


async def get_playurl(client: BiliClient, session: Any, bvid: str, cid: int, cookie: str) -> dict[str, Any]:
    """调用 playurl API 获取播放地址。"""
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
```

- [ ] **Step 3: Commit**

```bash
git add src/enjoy/api/view.py src/enjoy/api/playurl.py
git commit -m "feat: add video info and playurl API modules"
```

---

### Task 8: Create CLI dispatcher and __main__ entry

**Files:**
- Create: `src/enjoy/cli.py`
- Create: `src/enjoy/__main__.py`

**Interfaces:**
- Consumes: All modules from Tasks 1-7
- Produces: `python -m enjoy export|download` commands

- [ ] **Step 1: Create src/enjoy/__main__.py**

```python
"""python -m enjoy 入口。"""

from enjoy.cli import main

if __name__ == "__main__":
    main()
```

- [ ] **Step 2: Create src/enjoy/cli.py**

```python
"""CLI 子命令分发器。"""

import argparse
import asyncio
import json
import sys
import time
from pathlib import Path

from enjoy.api.client import BiliClient
from enjoy.config_loader import load_config
from enjoy.downloader.extractor import parse_links_file, extract_bvid
from enjoy.downloader.fetcher import find_ffmpeg
from enjoy.downloader.track import download_single_video, get_video_info
from enjoy.utils.helpers import filename_filter, setup_logging


def cmd_export(args, config: dict) -> None:
    """导出动态汇总 - 对应原 export_data.py。"""
    import requests
    from enjoy.exporter.parser import parse_item
    from enjoy.exporter.reporter import generate_report

    bilibili_cfg = config.get("bilibili", {})
    mid = args.mid or bilibili_cfg.get("mid", "2073801516")
    output_file = args.output or bilibili_cfg.get("output_file", "动态汇总报告.txt")
    cookie_raw = args.cookie or bilibili_cfg.get("cookie", "")

    if not cookie_raw:
        print("ERROR: 需要提供 Cookie。请使用 --cookie 参数或在 config.yaml 中配置。")
        sys.exit(1)

    # 提取 SESSDATA
    sessdata = ""
    if cookie_raw.startswith("{"):
        try:
            cookie_data = json.loads(cookie_raw)
            for k, v in cookie_data.items():
                if "sessdata" in k.lower():
                    sessdata = v
                    break
        except json.JSONDecodeError:
            pass
    if not sessdata:
        for part in cookie_raw.split(";"):
            part = part.strip()
            if "=" in part:
                k, v = part.split("=", 1)
                if k.strip().lower() == "sessdata":
                    sessdata = v.strip()
                    break

    if not sessdata:
        print("ERROR: 没有找到 SESSDATA")
        sys.exit(1)

    cookie_str = f"SESSDATA={sessdata}"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
        "Cookie": cookie_str,
    }

    FEED_URL = "https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space"
    DETAIL_URL = "https://api.bilibili.com/x/polymer/web-dynamic/v1/detail"

    all_items = []
    offset_val = ""
    page = 1

    print("\U0001F680 开始向 B 站网关索要全量数据...")

    while True:
        print(f"正在拉取第 {page} 页 (游标: {offset_val if offset_val else '首屏'})...")

        params = {
            "host_mid": mid,
            "offset": offset_val,
            "features": "itemOpusStyle,listOnlyfans,opusBigCover,onlyfansVote",
        }

        try:
            res = requests.get(FEED_URL, params=params, headers=headers, timeout=10)
        except Exception as e:
            print(f"❌ 网络请求崩溃: {e}")
            break

        if res.status_code != 200:
            print(f"❌ HTTP 状态码异常: {res.status_code}")
            break

        data = res.json()
        if data.get("code") != 0:
            print(f"❌ 被B站网关拦截，提示语：{data.get('message')}")
            break

        items = (data.get("data") or {}).get("items", [])
        for raw in items:
            info = parse_item(raw)
            all_items.append(info)
            print(f"   -> 捕获: [{info.pub_date}] {info.title}")

        if not (data.get("data") or {}).get("has_more"):
            print("\n✅ 全量数据网络加载完毕！正在排版生成 TXT 报告...")
            break

        offset_val = data["data"]["offset"]
        page += 1
        time.sleep(1.3)

    generate_report(all_items, output_file)
    print(f"\n\U0001F389 完美收工！汇总报告已生成至当前文件夹：{output_file}")


def cmd_download(args, config: dict) -> None:
    """下载视频 - 对应原 bilibili_downloader.py。"""
    download_cfg = config.get("download", {})

    links_file = args.links
    cookie_file = args.cookie
    output_dir = args.output or download_cfg.get("output_dir", "./downloads")
    ffmpeg_path = args.ffmpeg or download_cfg.get("ffmpeg", "")
    concurrency = args.concurrency or download_cfg.get("concurrency", 16)

    # 加载 cookie
    with open(cookie_file, "r", encoding="utf-8") as f:
        raw = f.read().strip()

    sessdata = ""
    if raw.startswith("{"):
        try:
            cookie_data = json.loads(raw)
            for k, v in cookie_data.items():
                if "sessdata" in k.lower():
                    sessdata = v
                    break
        except json.JSONDecodeError:
            pass
    if not sessdata:
        for part in raw.split(";"):
            part = part.strip()
            if "=" in part:
                k, v = part.split("=", 1)
                if k.strip().lower() == "sessdata":
                    sessdata = v.strip()
                    break

    if not sessdata:
        print("ERROR: cookie.json 中没有找到 SESSDATA")
        sys.exit(1)

    cookie_str = f"SESSDATA={sessdata}"
    print(f"Cookie 已加载 (SESSDATA: {sessdata[:10]}...{sessdata[-10:]})")

    # 查找 ffmpeg
    ffmpeg_bin = find_ffmpeg(ffmpeg_path)
    print(f"FFmpeg: {ffmpeg_bin}")

    # 读取链接
    link_entries = parse_links_file(links_file)
    print(f"共 {len(link_entries)} 个链接")
    for entry in link_entries:
        print(f"  {entry['date']} | {entry['title']} | {entry['bvid']}")

    import aiohttp

    async def _run():
        async with aiohttp.ClientSession() as session:
            for i, entry in enumerate(link_entries, 1):
                raw_url = entry["url"]
                custom_date = entry["date"]
                custom_title = entry["title"]
                bvid = entry["bvid"]

                print(f"\n[{i}/{len(link_entries)}] 处理: {raw_url}")
                if custom_date and custom_title:
                    print(f"  自定义名称: {custom_date}_{custom_title}_{bvid}")

                try:
                    info = await get_video_info(session, cookie_str, bvid)
                    target = info.pages[0]
                    if len(info.pages) == 1:
                        print(f"  单 P 视频")
                    else:
                        print(f"  共 {len(info.pages)} P，默认下载第 1 P: {target.part}")
                        for p in info.pages[1:]:
                            print(f"    P{p.page}: {p.part}")

                    await download_single_video(
                        session=session,
                        cookie=cookie_str,
                        bvid=bvid,
                        cid=target.cid,
                        output_dir=output_dir,
                        ffmpeg=ffmpeg_bin,
                        concurrency=concurrency,
                        custom_date=custom_date,
                        custom_title=custom_title,
                    )
                except Exception as e:
                    print(f"  ERROR: 处理失败: {e}")
                    import traceback
                    traceback.print_exc()
                    continue

                await asyncio.sleep(1)

            print(f"\n{'='*60}")
            print("所有任务完成!")
            print(f"{'='*60}")

    asyncio.run(_run())


def main() -> None:
    """CLI 主入口。"""
    parser = argparse.ArgumentParser(
        prog="enjoy",
        description="Bilibili 工具集 - 动态导出 + 视频下载",
    )
    subparsers = parser.add_subparsers(dest="command", help="可用命令")

    # export 子命令
    export_parser = subparsers.add_parser("export", help="导出 UP 主动态汇总")
    export_parser.add_argument("--mid", default=None, help="目标 UP 主 MID")
    export_parser.add_argument("--output", default=None, help="输出文件路径")
    export_parser.add_argument("--cookie", default=None, help="Cookie 字符串或 JSON")

    # download 子命令
    dl_parser = subparsers.add_parser("download", help="下载 B站视频")
    dl_parser.add_argument("--links", required=True, help="链接文件路径")
    dl_parser.add_argument("--cookie", required=True, help="cookie.json 文件路径")
    dl_parser.add_argument("-o", "--output", default=None, help="下载目录")
    dl_parser.add_argument("--ffmpeg", default=None, help="ffmpeg 路径")
    dl_parser.add_argument("--concurrency", type=int, default=None, help="分片并发数")

    args = parser.parse_args()

    if not args.command:
        parser.print_help()
        sys.exit(1)

    config = load_config()
    setup_logging()

    if args.command == "export":
        cmd_export(args, config)
    elif args.command == "download":
        cmd_download(args, config)
    else:
        parser.print_help()
        sys.exit(1)
```

- [ ] **Step 3: Commit**

```bash
git add src/enjoy/cli.py src/enjoy/__main__.py
git commit -m "feat: add unified CLI entry with export and download subcommands"
```

---

### Task 9: Final cleanup and verification

**Files:**
- Modify: `config/config.yaml` (ensure it's correct)
- Create: `src/enjoy/api/__init__.py` (if not created)
- Create: `src/enjoy/downloader/__init__.py` (if not created)
- Create: `src/enjoy/exporter/__init__.py` (if not created)
- Create: `src/enjoy/utils/__init__.py` (if not created)

**Interfaces:**
- Consumes: All files from Tasks 1-8
- Produces: Fully working `python -m enjoy` package

- [ ] **Step 1: Ensure all __init__.py files exist**

Verify/create these files:
- `src/enjoy/__init__.py`
- `src/enjoy/api/__init__.py`
- `src/enjoy/downloader/__init__.py`
- `src/enjoy/exporter/__init__.py`
- `src/enjoy/utils/__init__.py`

- [ ] **Step 2: Verify import structure works**

```bash
cd "D:/Develop/response/Demo/study/enjoy/bilibili_downloader"
python -c "from enjoy.cli import main; print('OK')"
```

- [ ] **Step 3: Verify CLI help**

```bash
python -m enjoy --help
python -m enjoy export --help
python -m enjoy download --help
```

- [ ] **Step 4: Commit**

```bash
git add src/enjoy/
git commit -m "chore: finalize package structure and verify imports"
```

---

## Summary of file changes

| New File | Purpose |
|----------|---------|
| `config/config.yaml` | 主配置文件 |
| `config/config.example.yaml` | 配置模板 |
| `requirements.txt` | Python 依赖 |
| `README.md` | 项目说明 |
| `src/enjoy/__init__.py` | 包标识 |
| `src/enjoy/__main__.py` | `python -m enjoy` 入口 |
| `src/enjoy/cli.py` | CLI 子命令分发 |
| `src/enjoy/config_loader.py` | 配置加载 |
| `src/enjoy/api/__init__.py` | API 包 |
| `src/enjoy/api/client.py` | 统一 HTTP 客户端 |
| `src/enjoy/api/feed.py` | 动态列表 API |
| `src/enjoy/api/detail.py` | 动态详情 API |
| `src/enjoy/api/view.py` | 视频信息 API |
| `src/enjoy/api/playurl.py` | 播放地址 API |
| `src/enjoy/downloader/__init__.py` | 下载包 |
| `src/enjoy/downloader/signer.py` | WBI 签名 |
| `src/enjoy/downloader/extractor.py` | URL 解析 |
| `src/enjoy/downloader/fetcher.py` | 分片下载/校验/合并 |
| `src/enjoy/downloader/track.py` | 轨道选择 + 下载流程 |
| `src/enjoy/exporter/__init__.py` | 导出包 |
| `src/enjoy/exporter/parser.py` | 动态解析 |
| `src/enjoy/exporter/reporter.py` | 报告生成 |
| `src/enjoy/utils/__init__.py` | 工具包 |
| `src/enjoy/utils/helpers.py` | 文件名过滤/日志 |
