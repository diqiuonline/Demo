"""配置加载器 - 从 config.yaml 加载默认配置"""

import json
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
    优先级: CLI 参数 > config.yaml 顶层 cookie > 环境变量

    支持多种格式:
    - 纯 cookie 字符串: "SESSDATA=xxx; buvid3=xxx"
    - JSON 格式: {"SESSDATA": "xxx"} 或 {"buvid3": "xxx", "SESSDATA": "xxx"}
    """
    raw = cookie_cli or config.get("cookie", "") or os.environ.get("BILIBILI_COOKIE", "")

    if not raw:
        raise ValueError(
            "未提供 Cookie。请在 config.yaml 顶层设置 cookie，"
            "或通过 --cookie 参数传入，或设置环境变量 BILIBILI_COOKIE"
        )

    # 尝试 JSON 格式
    if raw.startswith("{"):
        try:
            cookie_data = json.loads(raw)
            for k, v in cookie_data.items():
                if "sessdata" in k.lower():
                    return v
        except json.JSONDecodeError:
            pass

    # 纯 cookie 字符串
    return raw
