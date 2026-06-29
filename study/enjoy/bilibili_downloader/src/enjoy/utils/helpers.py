"""文件名过滤、日志配置等通用工具。"""

import logging
import sys
from datetime import datetime
from pathlib import Path


def filename_filter(s: str) -> str:
    """过滤文件名中的非法字符。"""
    s = s.replace("\\", " ").replace("/", " ").replace("\n", " ")
    s = s.replace(":", "：").replace("*", "★").replace("?", "？")
    s = s.replace('"', "'").replace("<", "《").replace(">", "》").replace("|", "「")
    return s.strip().rstrip(".").strip()


def setup_logging(verbose: bool = False, log_dir: str = "", log_level: str = "") -> None:
    """配置全局日志，同时输出到控制台和文件。

    Args:
        verbose: 是否启用详细模式（DEBUG级别）
        log_dir: 日志文件输出目录，为空则不输出到文件
        log_level: 日志级别字符串，如 "DEBUG", "INFO", "WARNING", "ERROR"
    """
    # 确定日志级别
    if verbose:
        level = logging.DEBUG
    elif log_level:
        level = getattr(logging, log_level.upper(), logging.INFO)
    else:
        level = logging.INFO

    # 根 logger 配置
    root_logger = logging.getLogger()
    root_logger.setLevel(level)

    # 清除已有的 handler，避免重复输出
    root_logger.handlers.clear()

    # 日志格式
    formatter = logging.Formatter(
        "%(asctime)s [%(levelname)s] %(name)s - %(message)s",
        datefmt="%Y-%m-%d %H:%M:%S",
    )

    # 控制台输出
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setLevel(level)
    console_handler.setFormatter(formatter)
    root_logger.addHandler(console_handler)

    # 文件输出
    if log_dir:
        log_path = Path(log_dir)
        log_path.mkdir(parents=True, exist_ok=True)

        # 按日期命名日志文件
        log_filename = f"bilibili_downloader_{datetime.now().strftime('%Y%m%d')}.log"
        log_file = log_path / log_filename

        file_handler = logging.FileHandler(str(log_file), encoding="utf-8")
        file_handler.setLevel(level)
        file_handler.setFormatter(formatter)
        root_logger.addHandler(file_handler)

        print(f"日志文件: {log_file}")


def ensure_dir(path: Path) -> Path:
    """确保目录存在，不存在则创建。"""
    path.mkdir(parents=True, exist_ok=True)
    return path
