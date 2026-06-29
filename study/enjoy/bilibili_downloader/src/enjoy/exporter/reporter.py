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

    # --- 三、普通图文 ---
    lines.extend(["=" * 77, f"三、 普通图文 / 日常动态列表 （共计: {len(normal_draws)} 个）", "=" * 77, ""])
    for idx, item in enumerate(normal_draws, 1):
        lines.append(f"{idx}. 【{item.title}】")
        lines.append(f"   发布日期 : {item.pub_date}")
        lines.append(f"   动态链接 : https://www.bilibili.com/opus/{item.id_str}")

        sum_text = item.summary.strip()
        if sum_text:
            indented_sum = sum_text.replace("\n", "\n             ")
            lines.append(f"   内容摘要 : {indented_sum}")
        else:
            lines.append("   内容摘要 : [无文字摘要]")

        if item.pics:
            lines.append(f"   包含图片 ({len(item.pics)}张):")
            for p in item.pics:
                lines.append(f"     -> {p}")
        else:
            lines.append("   包含图片 : [纯文字动态，无图片]")
        lines.append("")

    # --- 四、充电图文 ---
    lines.extend(["=" * 77, f"四、 充电/付费图文动态列表 （共计: {len(charged_draws)} 个）", "=" * 77, ""])
    for idx, item in enumerate(charged_draws, 1):
        lines.append(f"{idx}. 【{item.title}】")
        lines.append(f"   发布日期 : {item.pub_date}")
        lines.append(f"   动态链接 : https://www.bilibili.com/opus/{item.id_str}")

        sum_text = item.summary.strip()
        if sum_text:
            indented_sum = sum_text.replace("\n", "\n             ")
            lines.append(f"   内容摘要 : {indented_sum}")
        else:
            lines.append("   内容摘要 : [无文字摘要]")

        if item.pics:
            lines.append(f"   包含图片 ({len(item.pics)}张):")
            for p in item.pics:
                lines.append(f"     -> {p}")
        else:
            lines.append("   包含图片 : [纯文字动态，无图片]")
        lines.append("")

    # --- 五、汇总 ---
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
