"""音视频轨道选择 + 视频信息获取 + 单视频下载流程。"""

import asyncio
import logging
import sys
from dataclasses import dataclass, field
from pathlib import Path
from typing import Any, Optional

import aiohttp

from enjoy.downloader.extractor import extract_bvid
from enjoy.downloader.fetcher import (
    CHUNK_SIZE,
    download_file_chunks,
    find_ffmpeg,
    get_url_with_backup,
    merge_with_ffmpeg,
)
from enjoy.downloader.signer import wbi_sign
from enjoy.utils.helpers import filename_filter

logger = logging.getLogger("bilibili_downloader")

# 画质优先级 (数字越小优先级越高)
QUALITY_ORDER = {
    127: 0,   # 8K
    126: 1,   # 杜比视界
    125: 2,   # HDR
    120: 3,   # 4K
    116: 4,   # 1080P60
    112: 5,   # 1080P+
    80: 6,    # 1080P
    100: 7,   # AI 修复
    74: 8,    # 720P60
    64: 9,    # 720P
    32: 10,   # 480P
    16: 11,   # 360P
    6: 12,    # 240P
}

# 音质优先级
AUDIO_QUALITY_ORDER = {
    30251: 0,   # Hi-Res
    30250: 1,   # 杜比全景声
    30280: 2,   # 192K
    30232: 3,   # 132K
    30216: 4,   # 64K
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
    duration: int  # 毫秒
    bvid: str = ""
    aid: int = 0


@dataclass
class VideoInfo:
    bvid: str
    title: str
    owner_name: str
    pic: str
    pages: list[PageInfo]
    ugc_season: Optional[dict] = None  # 合集信息


async def get_video_info(session: aiohttp.ClientSession, cookie: str,
                         bvid: str, cid: Optional[int] = None) -> VideoInfo:
    """调用 view API 获取视频信息。"""
    params = {"bvid": bvid}
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                       "(KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36",
        "Referer": "https://www.bilibili.com/",
        "Cookie": cookie,
    }

    async with session.get(
        "https://api.bilibili.com/x/web-interface/view",
        params=params,
        headers=headers,
    ) as resp:
        data = await resp.json()

    if data.get("code") != 0:
        raise RuntimeError(f"获取视频信息失败: {data.get('message')} / {data}")

    info = data["data"]

    # 构建 pages
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


async def get_playurl(session: aiohttp.ClientSession, cookie: str,
                      bvid: str, cid: int) -> tuple[list[VideoTrack], list[AudioTrack]]:
    """调用 playurl API 获取音视频轨道列表。"""
    # 先签 WBI
    params = {
        "bvid": bvid,
        "cid": str(cid),
        "qn": "127",
        "fnval": "4048",
    }
    signed = await wbi_sign(params, session, cookie)

    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                       "(KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36",
        "Referer": "https://www.bilibili.com/",
        "Cookie": cookie,
    }

    async with session.get(
        "https://api.bilibili.com/x/player/wbi/playurl",
        params=signed,
        headers=headers,
    ) as resp:
        data = await resp.json()

    if data.get("code") != 0:
        raise RuntimeError(f"获取播放地址失败: {data.get('message')} / {data}")

    dash = data["data"]["dash"]
    video_tracks: list[VideoTrack] = []
    audio_tracks: list[AudioTrack] = []

    # 解析视频轨道
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

    # 解析音频轨道
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
        # 编码偏好: AVC(7) < HEVC(12) < AV1(13)
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


async def resolve_track_url(session: aiohttp.ClientSession,
                            track: Any) -> Optional[tuple[str, int]]:
    """为轨道解析出可用的下载 URL + 文件大小。"""
    result = await get_url_with_backup(session, track.base_url, track.backup_urls)
    return result


async def download_single_video(session: aiohttp.ClientSession, cookie: str,
                                bvid: str, cid: int, output_dir: str,
                                ffmpeg: str, concurrency: int = 16,
                                custom_date: str = "",
                                custom_title: str = "") -> None:
    """下载单个视频的完整流程。"""
    # 确保 Unicode 输出
    if hasattr(sys.stdout, "reconfigure"):
        sys.stdout.reconfigure(encoding="utf-8")
    print(f"\n{'='*60}")
    print(f"开始下载: {bvid}  CID={cid}")
    print(f"{'='*60}")

    async with aiohttp.ClientSession() as inner_session:
        # Step 1: 获取视频信息
        print("[1/7] 获取视频信息...")
        try:
            info = await get_video_info(inner_session, cookie, bvid, cid)
        except Exception as e:
            print(f"ERROR: 获取视频信息失败: {e}")
            return

        # 选择分 P
        if cid:
            page = next((p for p in info.pages if p.cid == cid), None)
            if not page:
                print(f"ERROR: 找不到 cid={cid} 对应的分 P")
                return
        else:
            page = info.pages[0]
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
            video_tracks, audio_tracks = await get_playurl(
                inner_session, cookie, bvid, cid
            )
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

        v_quality = best_video.quality
        v_codec = best_video.codecid
        print(f"  视频: 画质={v_quality}  编码={v_codec}  "
              f"{best_video.width}x{best_video.height}  "
              f"码率={best_video.bandwidth/1000:.1f}kbps")
        if best_audio:
            a_quality = best_audio.audio_quality
            print(f"  音频: 音质={a_quality}  编码={best_audio.codecs}  "
                  f"码率={best_audio.bandwidth/1000:.1f}kbps")

        # Step 4: 解析下载 URL
        print("[4/7] 解析下载 URL...")
        v_url_info = await resolve_track_url(inner_session, best_video)
        if not v_url_info:
            print("ERROR: 无法解析视频下载 URL")
            return
        v_url, v_size = v_url_info
        print(f"  视频大小: {v_size / 1024 / 1024:.1f} MB")

        a_url = None
        a_size = 0
        if best_audio:
            a_url_info = await resolve_track_url(inner_session, best_audio)
            if a_url_info:
                a_url, a_size = a_url_info
                print(f"  音频大小: {a_size / 1024 / 1024:.1f} MB")
            else:
                print("WARNING: 无法解析音频下载 URL")

        # Step 5: 创建输出目录和文件名
        print("[5/7] 准备输出目录...")
        # 使用自定义日期和标题（来自 links.txt），否则回退到从 API 获取的信息
        if custom_date and custom_title:
            display_title = f"{custom_date}_{custom_title}_{bvid}"
        else:
            display_title = f"{info.title}_{bvid}"
        safe_display = filename_filter(display_title)
        safe_up = filename_filter(info.owner_name)

        episode_dir = Path(output_dir) / safe_up
        episode_dir.mkdir(parents=True, exist_ok=True)
        print(f"  输出目录: {episode_dir}")

        video_out = episode_dir / f"{safe_display}.mp4"
        audio_out = episode_dir / f"{safe_display}.m4a"

        # Step 6: 下载视频
        print("[6/7] 下载视频...")
        video_ok = await download_file_chunks(
            inner_session, v_url, v_size, str(video_out), concurrency
        )
        if not video_ok:
            print("ERROR: 视频下载失败")
            return
        print(f"  视频下载完成: {video_out}")

        # Step 7: 下载音频 + 合并
        if best_audio and a_url:
            print("  下载音频...")
            audio_ok = await download_file_chunks(
                inner_session, a_url, a_size, str(audio_out), concurrency
            )
            if not audio_ok:
                print("ERROR: 音频下载失败")
                return
            print(f"  音频下载完成: {audio_out}")

            # FFmpeg 合并 — 用临时文件名避免 ffmpeg 报错 "same as Input"
            print("  FFmpeg 合并音视频...")
            tmp_merged = episode_dir / f"{safe_display}.tmp.mp4"
            merge_ok = merge_with_ffmpeg(ffmpeg, str(video_out), str(audio_out), str(tmp_merged))
            if merge_ok:
                # 清理中间文件
                for p in [str(video_out), str(audio_out)]:
                    import os
                    if os.path.exists(p):
                        os.remove(p)
                # 如果最终文件已存在先删掉
                if os.path.exists(str(video_out)):
                    os.remove(str(video_out))
                # 重命名
                os.rename(str(tmp_merged), str(video_out))
                print(f"  合并完成: {video_out}")
            else:
                print("WARNING: 合并失败，保留原始音视频文件")
        else:
            print("  无音频，视频即为最终文件")

        print(f"\n  {'='*40}")
        print(f"  下载完成!")
        print(f"  {'='*40}")
