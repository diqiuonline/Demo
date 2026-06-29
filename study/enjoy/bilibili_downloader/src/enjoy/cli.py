"""CLI 子命令分发器。"""

import argparse
import asyncio
import json
import sys
import time
from pathlib import Path

import aiohttp

from enjoy.config_loader import load_config
from enjoy.downloader.extractor import parse_links_file
from enjoy.downloader.fetcher import find_ffmpeg
from enjoy.downloader.track import (
    download_single_video,
    get_video_info,
    select_best_audio,
    select_best_video,
)
from enjoy.utils.helpers import filename_filter, setup_logging


def cmd_export(args, config: dict) -> None:
    """导出动态汇总 - 对应原 export_data.py。"""
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

    all_items = []
    offset_val = ""
    page = 1

    print("\U0001f680 开始向 B 站网关索要全量数据...")

    while True:
        print(f"正在拉取第 {page} 页 (游标: {offset_val if offset_val else '首屏'})...")

        params = {
            "host_mid": mid,
            "offset": offset_val,
            "features": "itemOpusStyle,listOnlyfans,opusBigCover,onlyfansVote",
        }

        try:
            import requests
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
    print(f"\n\U0001f389 完美收工！汇总报告已生成至当前文件夹：{output_file}")


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

                    # 默认下载第一 P
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

                # 间隔，避免请求过快
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
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
示例:
  python -m enjoy export --mid 2073801516 --cookie "SESSDATA=xxx"
  python -m enjoy download --links links.txt --cookie cookie.json
        """,
    )
    subparsers = parser.add_subparsers(dest="command", help="可用命令")

    # export 子命令
    export_parser = subparsers.add_parser("export", help="导出 UP 主动态汇总")
    export_parser.add_argument("--mid", default=None, help="目标 UP 主 MID (默认: config.yaml 或 2073801516)")
    export_parser.add_argument("--output", default=None, help="输出文件路径 (默认: config.yaml)")
    export_parser.add_argument("--cookie", default=None, help="Cookie 字符串或 JSON 文件路径")

    # download 子命令
    dl_parser = subparsers.add_parser("download", help="下载 B站视频")
    dl_parser.add_argument("--links", required=True, help="链接文件路径 (支持纯 URL 或结构化格式)")
    dl_parser.add_argument("--cookie", required=True, help="cookie.json 文件路径")
    dl_parser.add_argument("-o", "--output", default=None, help="下载目录 (默认: config.yaml)")
    dl_parser.add_argument("--ffmpeg", default=None, help="ffmpeg 可执行文件路径 (默认: 自动检测)")
    dl_parser.add_argument("--concurrency", type=int, default=None, help="分片并发数 (默认: 16)")
    dl_parser.add_argument("-v", "--verbose", action="store_true", help="详细日志")

    args = parser.parse_args()

    if not args.command:
        parser.print_help()
        sys.exit(1)

    config = load_config()
    setup_logging(verbose=getattr(args, 'verbose', False))

    if args.command == "export":
        cmd_export(args, config)
    elif args.command == "download":
        cmd_download(args, config)
    else:
        parser.print_help()
        sys.exit(1)
