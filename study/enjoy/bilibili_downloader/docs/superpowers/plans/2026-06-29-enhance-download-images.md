# 增强 download 命令：从 links.txt 下载视频和图片

> **For agentic workers:** Use superpowers:subagent-driven-development to implement this plan task-by-task.

**Goal:** 增强 `enjoy download` 命令，使其能从 `links.txt` 结构化报告中解析视频链接和图文链接，区分充电/普通，分别下载到四个子目录，并下载图文中的图片。

**Architecture:** 扩展 `parse_links_file()` 增加章节感知和图文解析；新增图片下载功能；在 `cmd_download()` 中统一处理视频和图片下载。

**Tech Stack:** Python 3.12+, aiohttp, requests

## 全局约束

- 不改变 `export` 命令的任何行为
- 保持现有 `download` 命令对纯 URL 格式的向后兼容
- 视频命名格式：`时间_标题_bv号_充电.mp4` / `时间_标题_bv号.mp4`
- 图片命名格式：`时间_标题_id号_充电.jpg` / `时间_标题_id号.jpg`
- 输出目录结构：`output/up主名/普通视频/`、`output/up主名/充电视频/`、`output/up主名/普通图片/`、`output/up主名/充电图片/`
- 图片 URL 中的 ID 号：从路径中提取，如 `http://i0.hdslb.com/bfs/new_dyn/89b143e51eb70b03e3a7d71250e7c16a2073801516.jpg` 提取 `89b143e51eb70b03e3a7d71250e7c16a2073801516`

---

### Task 1: 扩展 parse_links_file 支持章节感知和图文解析

**Files:**
- Modify: `src/enjoy/downloader/extractor.py`

**Interfaces:**
- Consumes: 现有 `parse_links_file` 的返回格式
- Produces: 增强的返回格式，包含 `category`（普通视频/充电视频/普通图片/充电图片）、`image_urls` 列表

**改动：**
1. 解析章节标题（`一、 普通视频投稿动态列表`、`二、 充电/付费视频动态列表` 等），为后续条目打上 category 标签
2. 新增解析 `图片 : URL` 行，收集图文动态的图片 URL 列表
3. 返回格式扩展：
```python
{
    "date": "2026-06-28",
    "title": "点痣点了但是被挡住了",
    "url": "https://www.bilibili.com/video/BV16sTM6CE3x/",
    "bvid": "BV16sTM6CE3x",
    "category": "normal_video",      # 新增
    "image_urls": [],                 # 新增，图文才有
    "opus_id": "",                    # 新增，图文动态链接中的 opus id
}
```

**章节映射：**
- `一、 普通视频` → `"normal_video"`
- `二、 充电/付费视频` → `"charged_video"`
- `三、 普通图文` → `"normal_image"`
- `四、 充电/付费图文` → `"charged_image"`

---

### Task 2: 新增图片下载功能

**Files:**
- Create: `src/enjoy/downloader/image_downloader.py`

**Interfaces:**
- Consumes: 图片 URL 列表、输出目录路径、分类标签
- Produces: 下载完成的本地文件路径

**新增函数：**
```python
def extract_image_id(url: str) -> str:
    """从图片 URL 提取 ID 号。
    http://i0.hdslb.com/bfs/new_dyn/89b143e51eb70b03e3a7d71250e7c16a2073801516.jpg
    → 89b143e51eb70b03e3a7d71250e7c16a2073801516
    """

async def download_image(session: Any, url: str, output_dir: str,
                         filename: str) -> str:
    """下载单张图片到指定目录，返回本地路径。"""
```

**逻辑：**
- 从 URL 提取文件扩展名（.jpg/.png/.webp 等）
- 构造文件名：`时间_标题_id号[_充电].[ext]`
- 用 aiohttp 异步下载

---

### Task 3: 修改 cmd_download 统一处理视频和图片

**Files:**
- Modify: `src/enjoy/cli.py`

**Interfaces:**
- Consumes: 增强后的 `parse_links_file` 返回、新增的 `download_image`
- Produces: 四类文件夹的下载结果

**改动：**
1. 遍历 `link_entries`，根据 `category` 分发：
   - `*_video` → 调用现有 `download_single_video()` 流程
   - `*_image` → 调用新增的图片下载流程
2. 视频输出目录：`output/up主名/普通视频/` 或 `output/up主名/充电视频/`
3. 图片输出目录：`output/up主名/普通图片/` 或 `output/up主名/充电图片/`
4. 对于图文动态，从 `动态链接` 提取 opus_id（如 `https://www.bilibili.com/opus/1218645606758612993` → `1218645606758612993`）

---

### Task 4: 更新 CLI 帮助和配置

**Files:**
- Modify: `src/enjoy/cli.py` (argparse help)
- Modify: `config/config.yaml` (可选：添加图片下载相关配置)

---

### Task 5: 验证

- [ ] `python -m enjoy download --help` 显示更新后的帮助
- [ ] 用 `links.txt` 测试解析正确性（打印解析结果）
- [ ] 验证四类输出目录结构
