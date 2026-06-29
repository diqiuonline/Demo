# -*- coding: utf-8 -*-
import time
import datetime
import requests

# ==============================================================================
# 1. 配置区域 (请填入你登录B站后的完整 Cookie)
# ==============================================================================
MY_COOKIE = "buvid3=25BB8BD1-C2E8-6928-7E16-5B28604CFD2362201infoc; b_nut=17774..."

TARGET_MID = "2073801516"  # 目标UP主：言言抱枕
OUTPUT_FILE = "言言抱枕_全量动态汇总报告.txt"

HEADERS = {
    "User-Agent": "Mozilla/50.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
    "Cookie": MY_COOKIE
}

FEED_URL = "https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space"
DETAIL_URL = "https://api.bilibili.com/x/polymer/web-dynamic/v1/detail"


# ==============================================================================
# 2. 核心函数解析区域
# ==============================================================================
def fetch_single_detail_fallback(dyn_id_str):
    """【方案B狙击手】当列表页被阉割正文时，强行敲开单条详情页扣出文案"""
    try:
        res = requests.get(DETAIL_URL, params={"id": dyn_id_str}, headers=HEADERS, timeout=5).json()
        if res.get("code") == 0:
            item = (res.get("data") or {}).get("item") or {}
            modules = item.get("modules") or []
            if modules and isinstance(modules, list):
                dyn = (modules[0].get("module_dynamic") or {})
                desc = dyn.get("desc") or {}
                text = desc.get("text", "") if isinstance(desc, dict) else ""
                
                major = dyn.get("major") or {}
                title = ""
                if major.get("type") == "MAJOR_TYPE_OPUS":
                    opus = major.get("opus") or {}
                    title = opus.get("title", "")
                    if not text:
                        text = (opus.get("summary") or {}).get("text", "")
                return title, text
    except Exception:
        pass
    return "", ""


def parse_item(item):
    """单条动态基因智能解析器"""
    id_str = item.get("id_str", "")
    basic = item.get("basic") or {}
    is_only_fans = basic.get("is_only_fans", False)

    # 安全解析日期
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

    # ---------------------------------------------------------
    # 分支一：视频投稿
    # ---------------------------------------------------------
    if major_type == "MAJOR_TYPE_ARCHIVE":
        is_video = True
        arc = major.get("archive") or {}
        title = arc.get("title", "无题视频")
        bvid = arc.get("bvid", "")
        video_url = f"https://www.bilibili.com/video/{bvid}/" if bvid else "链接解析失败"

    # ---------------------------------------------------------
    # 分支二：图文 / 日常
    # ---------------------------------------------------------
    else:
        is_video = False
        # 尝试拿新版 Opus 专栏图文
        if major_type == "MAJOR_TYPE_OPUS":
            opus = major.get("opus") or {}
            title = opus.get("title", "")
            summary = (opus.get("summary") or {}).get("text", "")
            pics = [p.get("url") for p in opus.get("pics", []) if isinstance(p, dict) and p.get("url")]

        # 尝试拿常规 Draw 相簿图文
        elif major_type == "MAJOR_TYPE_DRAW":
            draw_items = (major.get("draw") or {}).get("items", [])
            pics = [p.get("src") for p in draw_items if isinstance(p, dict) and p.get("src")]

        # 兜底拿常规文字摘要
        if not summary:
            desc_obj = dyn.get("desc")
            if desc_obj and isinstance(desc_obj, dict):
                summary = desc_obj.get("text", "")

        # 【触发方案B补漏】：如果是图文，且连正文摘要都是空的，发单条请求去“扣”！
        if not summary and not title:
            print(f"   ⚠️ [狙击手启动] 发现疑似阉割动态 {id_str}，正在回溯详情页...")
            time.sleep(0.4)
            sniped_title, sniped_summary = fetch_single_detail_fallback(id_str)
            if sniped_title: title = sniped_title
            if sniped_summary: summary = sniped_summary

        # 智能生成标题
        if not title:
            if summary:
                clean_sum = summary.strip().replace("\n", " ")
                title = clean_sum[:16] + "..." if len(clean_sum) > 16 else clean_sum
            else:
                title = "分享图片" if pics else "日常动态"

    return {
        "id_str": id_str,
        "is_video": is_video,
        "is_only_fans": is_only_fans,
        "pub_date": pub_date,
        "title": title,
        "summary": summary,
        "video_url": video_url,
        "pics": pics
    }


# ==============================================================================
# 3. 主爬取流程
# ==============================================================================
def main():
    normal_videos, charged_videos = [], []
    normal_draws, charged_draws = [], []
    total_pics_count = 0

    offset_val = ""
    page = 1

    print("🚀 开始向 B 站网关索要全量数据...")

    while True:
        print(f"正在拉取第 {page} 页 (游标: {offset_val if offset_val else '首屏'})...")
        
        # 【核心暗号注入区】features 告诉服务器：老子支持新版图文和充电专属，别给我阉割！
        params = {
            "host_mid": TARGET_MID,
            "offset": offset_val,
            "features": "itemOpusStyle,listOnlyfans,opusBigCover,onlyfansVote"
        }

        try:
            res = requests.get(FEED_URL, params=params, headers=HEADERS, timeout=10)
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
            total_pics_count += len(info["pics"])

            # 4格分类漏斗
            if info["is_video"]:
                if info["is_only_fans"]:
                    charged_videos.append(info)
                else:
                    normal_videos.append(info)
            else:
                if info["is_only_fans"]:
                    charged_draws.append(info)
                else:
                    normal_draws.append(info)

            print(f"   -> 捕获: [{info['pub_date']}] {info['title']}")

        if not (data.get("data") or {}).get("has_more"):
            print("\n✅ 全量数据网络加载完毕！正在排版生成 TXT 报告...")
            break

        offset_val = data["data"]["offset"]
        page += 1
        time.sleep(1.3)  # 防封禁呼吸间隔

    # ==========================================================================
    # 4. 报告排版生成区
    # ==========================================================================
    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        
        # --- 分类一：普通视频 ---
        f.write("=====================================================================================\n")
        f.write(f"一、 普通视频投稿动态列表 （共计: {len(normal_videos)} 个）\n")
        f.write("=====================================================================================\n\n")
        for idx, item in enumerate(normal_videos, 1):
            f.write(f"{idx}. 【{item['title']}】\n")
            f.write(f"   发布日期 : {item['pub_date']}\n")
            f.write(f"   动态链接 : https://www.bilibili.com/opus/{item['id_str']}\n")
            f.write(f"   视频访问 : {item['video_url']}\n\n")

        # --- 分类二：充电视频 ---
        f.write("=====================================================================================\n")
        f.write(f"二、 充电/付费视频动态列表 （共计: {len(charged_videos)} 个）\n")
        f.write("=====================================================================================\n\n")
        for idx, item in enumerate(charged_videos, 1):
            f.write(f"{idx}. 【{item['title']}】\n")
            f.write(f"   发布日期 : {item['pub_date']}\n")
            f.write(f"   动态链接 : https://www.bilibili.com/opus/{item['id_str']}\n")
            f.write(f"   视频访问 : {item['video_url']}\n\n")

        # --- 分类三：普通图文 ---
        f.write("=====================================================================================\n")
        f.write(f"三、 普通图文 / 日常动态列表 （共计: {len(normal_draws)} 个）\n")
        f.write("=====================================================================================\n\n")
        for idx, item in enumerate(normal_draws, 1):
            f.write(f"{idx}. 【{item['title']}】\n")
            f.write(f"   发布日期 : {item['pub_date']}\n")
            f.write(f"   动态链接 : https://www.bilibili.com/opus/{item['id_str']}\n")
            
            sum_text = item["summary"].strip()
            if sum_text:
                # 强迫症视觉缩进：将多行文本换行后自动推后13个空格
                indented_sum = sum_text.replace("\n", "\n             ")
                f.write(f"   内容摘要 : {indented_sum}\n")
            else:
                f.write("   内容摘要 : [无文字摘要]\n")

            if item["pics"]:
                f.write(f"   包含图片 ({len(item['pics'])}张):\n")
                for p in item["pics"]:
                    f.write(f"     -> {p}\n")
            else:
                f.write("   包含图片 : [纯文字动态，无图片]\n")
            f.write("\n")

        # --- 分类四：充电图文 ---
        f.write("=====================================================================================\n")
        f.write(f"四、 充电/付费图文动态列表 （共计: {len(charged_draws)} 个）\n")
        f.write("=====================================================================================\n\n")
        for idx, item in enumerate(charged_draws, 1):
            f.write(f"{idx}. 【{item['title']}】\n")
            f.write(f"   发布日期 : {item['pub_date']}\n")
            f.write(f"   动态链接 : https://www.bilibili.com/opus/{item['id_str']}\n")

            sum_text = item["summary"].strip()
            if sum_text:
                indented_sum = sum_text.replace("\n", "\n             ")
                f.write(f"   内容摘要 : {indented_sum}\n")
            else:
                f.write("   内容摘要 : [无文字摘要]\n")

            if item["pics"]:
                f.write(f"   包含图片 ({len(item['pics'])}张):\n")
                for p in item["pics"]:
                    f.write(f"     -> {p}\n")
            else:
                f.write("   包含图片 : [纯文字动态，无图片]\n")
            f.write("\n")

        # --- 分类五：汇总 ---
        f.write("=====================================================================================\n")
        f.write("五、 统计汇总\n")
        f.write("=====================================================================================\n\n")
        f.write(f"普通视频动态: {len(normal_videos)} 个\n")
        f.write(f"充电视频动态: {len(charged_videos)} 个\n")
        f.write(f"普通图文动态: {len(normal_draws)} 个\n")
        f.write(f"充电图文动态: {len(charged_draws)} 个\n")
        f.write(f"图片总数: {total_pics_count} 张\n")

    print(f"\n🎉 完美收工！汇总报告已生成至当前文件夹：{OUTPUT_FILE}")


if __name__ == "__main__":
    main()