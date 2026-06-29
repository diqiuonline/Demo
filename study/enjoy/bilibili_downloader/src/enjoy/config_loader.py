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
