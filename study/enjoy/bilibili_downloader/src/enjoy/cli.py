"""CLI 子命令分发器。"""

import argparse
import asyncio
import json
import sys
import time
from pathlib import Path

import aiohttp

from enjoy.config_loader import load_config, get_cookie
from enjoy.downloader.extractor import parse_links_file
from enjoy.downloader.fetcher import find_ffmpeg
from enjoy.downloader.image_downloader import download_image, extract_image_id
from enjoy.downloader.track import (
    download_single_video,
    get_video_info,
)
from enjoy.utils.helpers import filename_filter, setup_logging

# 分类到子目录的映射
CATEGORY_DIR = {
    "normal_video": "普通视频",
    "charged_video": "充电视频",
    "normal_image": "普通图片",
    "charged_image": "充电图片",
}


def cmd_export(args, config: dict) -> None:
    """导出动态汇总 - 对应原 export_data.py。"""
    from enjoy.exporter.parser import parse_item
    from enjoy.exporter.reporter import generate_report

    bilibili_cfg = config.get("bilibili", {})
    mid = args.mid or bilibili_cfg.get("mid", "2073801516")
    output_dir = args.output or bilibili_cfg.get("output_dir", "./output")
    cookie_raw = args.cookie

    cookie_str = get_cookie(config, cookie_raw)
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

        # 从第一页响应中取 UP 主名字
        up_name = mid
        items = (data.get("data") or {}).get("items", [])
        if items:
            first_item = items[0]
            mod_author = (first_item.get("modules") or {}).get("module_author") or {}
            up_name = mod_author.get("name", mid)
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

    output_path = Path(output_dir) / f"{up_name}_全量动态汇总报告.txt"
    output_path.parent.mkdir(parents=True, exist_ok=True)
    generate_report(all_items, str(output_path))
    print(f"\n\U0001f389 完美收工！汇总报告已生成至：{output_path}")


def cmd_download(args, config: dict) -> None:
    """下载视频和图片 - 增强版，支持从 links.txt 结构化报告下载。"""
    download_cfg = config.get("download", {})

    # 优先级: CLI 参数 > config.yaml > 默认值
    links_file = args.links or download_cfg.get("links_file", "links.txt")
    output_dir = args.output or download_cfg.get("output_dir", "./downloads")
    ffmpeg_path = args.ffmpeg or download_cfg.get("ffmpeg", "")
    concurrency = args.concurrency or download_cfg.get("concurrency", 16)
    video_quality = download_cfg.get("video_quality", 127)
    audio_quality = download_cfg.get("audio_quality", 30251)

    # 统一获取 cookie 字符串
    cookie_raw = args.cookie
    cookie_str = get_cookie(config, cookie_raw)
    # 打印 SESSDATA 脱敏信息
    sessdata_val = ""
    if cookie_str.startswith("{"):
        try:
            cd = json.loads(cookie_str)
            for k, v in cd.items():
                if "sessdata" in k.lower():
                    sessdata_val = v
                    break
        except json.JSONDecodeError:
            pass
    if not sessdata_val:
        for part in cookie_str.split(";"):
            part = part.strip()
            if "=" in part:
                k, v = part.split("=", 1)
                if k.strip().lower() == "sessdata":
                    sessdata_val = v.strip()
                    break
    if sessdata_val:
        print(f"Cookie 已加载 (SESSDATA: {sessdata_val[:10]}...{sessdata_val[-10:]})")

    # 查找 ffmpeg
    ffmpeg_bin = find_ffmpeg(ffmpeg_path)
    print(f"FFmpeg: {ffmpeg_bin}")

    # 读取链接
    link_entries = parse_links_file(links_file)
    print(f"共 {len(link_entries)} 个条目")
    for entry in link_entries:
        cat_label = CATEGORY_DIR.get(entry["category"], entry["category"])
        if entry["bvid"]:
            print(f"  [{cat_label}] {entry['date']} | {entry['title']} | {entry['bvid']}")
        else:
            print(f"  [{cat_label}] {entry['date']} | {entry['title']} | opus:{entry['opus_id']} ({len(entry['image_urls'])} 张图片)")

    # 统计
    video_count = sum(1 for e in link_entries if e["bvid"])
    image_count = sum(1 for e in link_entries if not e["bvid"])
    print(f"  视频: {video_count} 个, 图文: {image_count} 个")

    # 从 links 文件名提取 UP 主名
    # 格式: UP主名_全量动态汇总报告.txt
    up_name = "未知UP主"
    links_filename = Path(links_file).stem
    if "_全量动态汇总报告" in links_filename:
        up_name = links_filename.rsplit("_全量动态汇总报告", 1)[0]
    print(f"UP主: {up_name}")

    # 创建 UP 主根目录
    up_root_dir = Path(output_dir) / filename_filter(up_name)
    up_root_dir.mkdir(parents=True, exist_ok=True)

    async def _run():
        async with aiohttp.ClientSession() as session:
            for i, entry in enumerate(link_entries, 1):
                raw_url = entry["url"]
                custom_date = entry["date"]
                custom_title = entry["title"]
                bvid = entry["bvid"]
                category = entry["category"]
                image_urls = entry.get("image_urls", [])
                opus_id = entry.get("opus_id", "")

                # 确定子目录: UP主名/分类名
                sub_dir_name = CATEGORY_DIR.get(category, "其他")
                target_dir = up_root_dir / sub_dir_name
                target_dir.mkdir(parents=True, exist_ok=True)

                print(f"\n[{i}/{len(link_entries)}] 处理: {raw_url}")
                print(f"  分类: {sub_dir_name}")

                if custom_date and custom_title:
                    print(f"  名称: {custom_date}_{custom_title}")

                try:
                    if bvid:
                        # === 视频下载 ===
                        info = await get_video_info(session, cookie_str, bvid)

                        # 默认下载第一 P
                        target = info.pages[0]
                        if len(info.pages) == 1:
                            print(f"  单 P 视频")
                        else:
                            print(f"  共 {len(info.pages)} P，默认下载第 1 P: {target.part}")

                        await download_single_video(
                            session=session,
                            cookie=cookie_str,
                            bvid=bvid,
                            cid=target.cid,
                            output_dir=str(target_dir),
                            ffmpeg=ffmpeg_bin,
                            concurrency=concurrency,
                            custom_date=custom_date,
                            custom_title=custom_title,
                            video_quality=video_quality,
                            audio_quality=audio_quality,
                            create_up_subdir=False,
                        )

                    elif image_urls:
                        # === 图片下载 ===
                        # 文件名: 时间_标题_id号[_充电].ext
                        tag_suffix = "_充电" if "charged" in category else ""
                        safe_title = filename_filter(custom_title or "无标题")
                        safe_date = filename_filter(custom_date or "未知日期")

                        for idx, img_url in enumerate(image_urls, 1):
                            img_id = extract_image_id(img_url)
                            img_name = f"{safe_date}_{safe_title}_{img_id}{tag_suffix}"
                            print(f"  下载图片 {idx}/{len(image_urls)}: {img_name}")
                            await download_image(session, img_url, str(target_dir), img_name)

                    else:
                        print("  跳过: 无视频或图片")

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
        description="Bilibili 工具集 - 动态导出 + 视频/图片下载",
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
    dl_parser = subparsers.add_parser("download", help="下载 B站视频和图片")
    dl_parser.add_argument("--links", default=None, help="链接文件路径 (默认: config.yaml)")
    dl_parser.add_argument("--cookie", default=None, help="Cookie 字符串 (默认: config.yaml 顶层 cookie)")
    dl_parser.add_argument("-o", "--output", default=None, help="下载目录 (默认: config.yaml)")
    dl_parser.add_argument("--ffmpeg", default=None, help="ffmpeg 可执行文件路径 (默认: 自动检测)")
    dl_parser.add_argument("--concurrency", type=int, default=None, help="分片并发数 (默认: 16)")
    dl_parser.add_argument("-v", "--verbose", action="store_true", help="详细日志")

    args = parser.parse_args()

    if not args.command:
        parser.print_help()
        sys.exit(1)

    config = load_config()

    # 日志配置
    log_cfg = config.get("log", {})
    log_dir = log_cfg.get("log_dir", "")
    log_level = log_cfg.get("log_level", "")
    setup_logging(
        verbose=getattr(args, 'verbose', False),
        log_dir=log_dir,
        log_level=log_level,
    )

    if args.command == "export":
        cmd_export(args, config)
    elif args.command == "download":
        cmd_download(args, config)
    else:
        parser.print_help()
        sys.exit(1)
