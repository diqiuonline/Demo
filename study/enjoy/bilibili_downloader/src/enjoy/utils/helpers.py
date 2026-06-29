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
