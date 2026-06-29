import re
from datetime import datetime, timedelta
import sys

# 设置输出编码
sys.stdout.reconfigure(encoding='utf-8')

def parse_bilibili_dynamics(html_file: str, output_file: str = "parsed_links_final.txt"):
    print("正在读取文件...")
    try:
        with open(html_file, 'r', encoding='utf-8') as f:
            content = f.read()
        print(f"文件读取完成，大小: {len(content)} 字符")
    except Exception as e:
        print(f"读取文件失败: {e}")
        return

    # 找到动态列表的起始位置，跳过页面头部
    dyn_list_start = content.find('bili-dyn-list__items')
    if dyn_list_start == -1:
        dyn_list_start = 0
    
    dyn_content = content[dyn_list_start:]
    
    # 按 bili-dyn-list__item 分割
    print("正在分割动态块...")
    blocks = re.split(r'<div class="bili-dyn-list__item"', dyn_content)
    now = datetime.now()

    print(f"分割出 {len(blocks)} 个动态块")

    video_list = []
    opus_list = []
    vip_video_list = []
    vip_opus_list = []

    for idx, block in enumerate(blocks):
        if idx == 0 or len(block.strip()) < 100:
            continue

        # 提取动态ID (支持多种格式)
        dyn_id = None
        dyn_url_type = None
        
        # 尝试 bilibili.com/opus/ 格式
        id_match = re.search(r'bilibili\.com/opus/(\d{16,20})', block)
        if id_match:
            dyn_id = id_match.group(1)
            dyn_url_type = 'opus'
        else:
            # 尝试 t.bilibili.com 格式
            id_match = re.search(r't\.bilibili\.com/(\d{16,20})', block)
            if id_match:
                dyn_id = id_match.group(1)
                dyn_url_type = 't'

        # 判断是否是充电/VIP内容
        is_vip = False
        
        # 视频充电标识：充电专属 badge
        has_charge_badge = '充电专属' in block
        
        # 图文充电标识：参考Java代码逻辑，检查时间附近的 IN4E1b8HNg 图标
        # 先找到时间模块
        time_module_match = re.search(r'<div data-module="time"[^>]*>.*?</div>', block, re.DOTALL)
        if time_module_match:
            time_end = time_module_match.end()
            # 检查时间后面的内容是否有 IN4E1b8HNg
            after_time = block[time_end:time_end+500]
            if 'IN4E1b8HNg' in after_time:
                is_vip = True
        
        # 视频如果有"充电专属"badge，肯定是充电视频
        if has_charge_badge:
            is_vip = True

        # 提取时间
        time_text = ""
        time_match = re.search(r'<div data-module="time"[^>]*>(.*?)</div>', block, re.DOTALL)
        if time_match:
            time_text = re.sub(r'<[^>]+>', '', time_match.group(1)).strip()

        date_str = "未知日期"
        if time_text:
            m_full = re.search(r'(\d{4})\s*年\s*(\d{1,2})\s*月\s*(\d{1,2})\s*日', time_text)
            m_md = re.search(r'(\d{1,2})\s*月\s*(\d{1,2})\s*日', time_text)
            m_days = re.search(r'(\d+)\s*天前', time_text)

            if m_full:
                date_str = f"{m_full.group(1)}-{int(m_full.group(2)):02d}-{int(m_full.group(3)):02d}"
            elif m_md:
                date_str = f"{now.year}-{int(m_md.group(1)):02d}-{int(m_md.group(2)):02d}"
            elif m_days:
                date_str = (now - timedelta(days=int(m_days.group(1)))).strftime("%Y-%m-%d")
            elif "昨天" in time_text:
                date_str = (now - timedelta(days=1)).strftime("%Y-%m-%d")
            elif "前天" in time_text:
                date_str = (now - timedelta(days=2)).strftime("%Y-%m-%d")
            elif any(k in time_text for k in ["小时前", "分钟前", "刚刚"]):
                date_str = now.strftime("%Y-%m-%d")

        # 判断是否是视频动态
        is_video = 'bili-dyn-card-video' in block

        if is_video:
            # 提取 BV 号
            bv_matches = re.findall(r'(BV[a-zA-Z0-9]{10})', block)
            if not bv_matches:
                continue
            
            bv_id = bv_matches[0]
            
            # 提取视频标题
            title = f"视频_{bv_id}"
            title_match = re.search(r'class="bili-dyn-card-video__title[^"]*"[^>]*>(.*?)</div>', block, re.DOTALL)
            if title_match:
                title = re.sub(r'<[^>]+>|\s+', ' ', title_match.group(1)).strip()
                if not title:
                    title = f"视频_{bv_id}"
            
            # 构建动态链接
            if dyn_id:
                if dyn_url_type == 't':
                    dyn_url = f"https://t.bilibili.com/{dyn_id}"
                else:
                    dyn_url = f"https://www.bilibili.com/opus/{dyn_id}"
            else:
                dyn_url = "（动态ID未在HTML中直接显示）"
            
            video_info = {
                'dyn_id': dyn_id or "未知",
                'dyn_url': dyn_url,
                'video_url': f"https://www.bilibili.com/video/{bv_id}/",
                'title': title,
                'date': date_str,
                'bv': bv_id
            }
            
            if is_vip:
                vip_video_list.append(video_info)
            else:
                video_list.append(video_info)
            continue

        # 图文动态
        # 判断是否是图文动态（有 dyn-card-opus 类或 new_dyn 图片）
        is_opus = 'dyn-card-opus' in block or 'new_dyn' in block
        
        if not is_opus:
            continue
        
        # 构建动态链接
        if dyn_id:
            if dyn_url_type == 't':
                opus_url = f"https://t.bilibili.com/{dyn_id}"
            else:
                opus_url = f"https://www.bilibili.com/opus/{dyn_id}"
        else:
            opus_url = "（动态ID未在HTML中直接显示）"
        
        # 提取图文标题
        title = "无标题"
        title_match = re.search(r'class="dyn-card-opus__title[^"]*"[^>]*>(.*?)</div>', block, re.DOTALL)
        if title_match:
            title = re.sub(r'<[^>]+>|\s+', ' ', title_match.group(1)).strip()
            if not title:
                title = "无标题"
        
        # 提取文字内容
        text = "无摘要内容"
        text_match = re.search(r'class="dyn-card-opus__summary[^"]*"[^>]*>(.*?)</div>', block, re.DOTALL)
        if text_match:
            cleaned = re.sub(r'<[^>]+>|\s+', ' ', text_match.group(1)).strip()
            if cleaned and len(cleaned) > 2:
                text = cleaned

        # 提取图片链接
        raw_imgs = re.findall(r'//[^"\s]+hdslb\.com/bfs/(?:new_dyn|album)/[^"\s@]+\.(?:jpg|png|webp|jpeg)', block, re.IGNORECASE)
        clean_imgs = []
        for img in raw_imgs:
            # 去掉 @ 后面的参数
            clean_img = img.split('@')[0]
            if not clean_img.startswith('http'):
                clean_img = f"https:{clean_img}"
            clean_imgs.append(clean_img)
        
        # 去重
        unique_imgs = list(dict.fromkeys(clean_imgs))
        
        # 过滤头像
        filtered_imgs = [img for img in unique_imgs if 'face' not in img]

        # 所有图文动态都保留（包括纯文字动态）
        opus_info = {
            'dyn_id': dyn_id or "未知",
            'dyn_url': opus_url,
            'title': title,
            'text': text,
            'date': date_str,
            'images': filtered_imgs
        }
        
        if is_vip:
            vip_opus_list.append(opus_info)
        else:
            opus_list.append(opus_info)

    # 去重
    video_list = list({v['bv']: v for v in video_list}.values())
    vip_video_list = list({v['bv']: v for v in vip_video_list}.values())
    # 图文动态保留所有，即使没有dyn_id（用内容和日期作为去重依据）
    def dedup_opus(opus_list):
        seen = set()
        result = []
        for o in opus_list:
            # 有 dyn_id 时用 ID+日期+图片数；没有时用内容摘要前30字+日期+图片数
            if o['dyn_id'] and o['dyn_id'] != "未知":
                key = f"{o['dyn_id']}_{o['date']}_{len(o['images'])}"
            else:
                text_key = o.get('text', '')[:30] if o.get('text') and o['text'] != '无摘要内容' else ''
                key = f"_{o['date']}_{len(o['images'])}_{text_key}"
            if key not in seen:
                seen.add(key)
                result.append(o)
        return result
    
    opus_list = dedup_opus(opus_list)
    vip_opus_list = dedup_opus(vip_opus_list)

    # 输出写入
    print("正在写入结果文件...")
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('=' * 85 + '\n')
        f.write(f'一、 普通视频投稿动态列表 （共计: {len(video_list)} 个）\n')
        f.write('=' * 85 + '\n\n')

        for i, v in enumerate(video_list, 1):
            f.write(f'{i}. 【{v["title"]}】\n')
            f.write(f'   发布日期 : {v["date"]}\n')
            f.write(f'   动态链接 : {v["dyn_url"]}\n')
            f.write(f'   视频访问 : {v["video_url"]}\n\n')

        f.write('=' * 85 + '\n')
        f.write(f'二、 充电/付费视频动态列表 （共计: {len(vip_video_list)} 个）\n')
        f.write('=' * 85 + '\n\n')

        for i, v in enumerate(vip_video_list, 1):
            f.write(f'{i}. 【{v["title"]}】\n')
            f.write(f'   发布日期 : {v["date"]}\n')
            f.write(f'   动态链接 : {v["dyn_url"]}\n')
            f.write(f'   视频访问 : {v["video_url"]}\n\n')

        f.write('=' * 85 + '\n')
        f.write(f'三、 普通图文 / 日常动态列表 （共计: {len(opus_list)} 个）\n')
        f.write('=' * 85 + '\n\n')

        for i, o in enumerate(opus_list, 1):
            f.write(f'{i}. 【{o["title"]}】\n')
            f.write(f'   发布日期 : {o["date"]}\n')
            f.write(f'   动态链接 : {o["dyn_url"]}\n')
            if o['text'] and o['text'] != '无摘要内容':
                text_preview = o["text"][:80] + "..." if len(o["text"]) > 80 else o["text"]
                f.write(f'   内容摘要 : {text_preview}\n')
            if o['images']:
                f.write(f'   包含图片 ({len(o["images"])}张):\n')
                for img_url in o['images']:
                    f.write(f'     -> {img_url}\n')
            else:
                f.write('   包含图片 : [纯文字动态，无图片]\n')
            f.write('\n')

        f.write('=' * 85 + '\n')
        f.write(f'四、 充电/付费图文动态列表 （共计: {len(vip_opus_list)} 个）\n')
        f.write('=' * 85 + '\n\n')

        for i, o in enumerate(vip_opus_list, 1):
            f.write(f'{i}. 【{o["title"]}】\n')
            f.write(f'   发布日期 : {o["date"]}\n')
            f.write(f'   动态链接 : {o["dyn_url"]}\n')
            if o['text'] and o['text'] != '无摘要内容':
                text_preview = o["text"][:80] + "..." if len(o["text"]) > 80 else o["text"]
                f.write(f'   内容摘要 : {text_preview}\n')
            if o['images']:
                f.write(f'   包含图片 ({len(o["images"])}张):\n')
                for img_url in o['images']:
                    f.write(f'     -> {img_url}\n')
            else:
                f.write('   包含图片 : [纯文字动态，无图片]\n')
            f.write('\n')

        # 汇总统计
        f.write('=' * 85 + '\n')
        f.write('五、 统计汇总\n')
        f.write('=' * 85 + '\n\n')
        f.write(f'普通视频动态: {len(video_list)} 个\n')
        f.write(f'充电视频动态: {len(vip_video_list)} 个\n')
        f.write(f'普通图文动态: {len(opus_list)} 个\n')
        f.write(f'充电图文动态: {len(vip_opus_list)} 个\n')
        total_images = sum(len(o['images']) for o in opus_list) + sum(len(o['images']) for o in vip_opus_list)
        f.write(f'图片总数: {total_images} 张\n')
        
        # 说明
        f.write('\n说明：\n')
        f.write('1. 视频动态的动态ID在HTML源码中未直接显示，仅提取了视频BV号和访问链接\n')
        f.write('2. 图文动态链接为 bilibili.com/opus/ 格式\n')
        f.write('3. 充电视频通过"充电专属"标识判断\n')
        f.write('4. 充电图文通过时间旁的 IN4E1b8HNg 图标徽章判断（参考Java代码逻辑）\n')

    print(f"\n解析完毕！")
    print(f"  普通视频: {len(video_list)} 个")
    print(f"  充电视频: {len(vip_video_list)} 个")
    print(f"  普通图文: {len(opus_list)} 个")
    print(f"  充电图文: {len(vip_opus_list)} 个")
    total_images = sum(len(o['images']) for o in opus_list) + sum(len(o['images']) for o in vip_opus_list)
    print(f"  图片总数: {total_images} 张")
    print(f"\n数据已整齐输出至 -> {output_file}")

if __name__ == "__main__":
    parse_bilibili_dynamics("src.txt")
