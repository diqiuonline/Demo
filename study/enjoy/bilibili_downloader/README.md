# Enjoy - Bilibili 工具集

一个功能强大的 Bilibili 工具集，支持 UP 主动态导出、视频下载、图片下载等功能。

## ✨ 功能特性

- 📤 **动态导出**：一键导出 UP 主全部动态，支持普通视频、充电视频、普通图文、充电图文分类
- 🎬 **视频下载**：支持多画质视频下载，最高 8K，分片并发下载，速度快
- 🖼️ **图片下载**：支持图文动态中的图片批量下载
- 🔐 **WBI 签名**：内置 WBI 签名算法，无需额外配置
- 📁 **分类存储**：按 UP 主 + 分类自动组织文件结构
- 📝 **日志记录**：支持日志文件输出，方便排查问题
- ⚡ **并发下载**：支持分片并发下载，充分利用带宽

## 📦 安装

### 环境要求

- Python 3.12+
- FFmpeg（用于音视频合并）

### 安装步骤

```bash
# 克隆项目
git clone <repository-url>
cd bilibili_downloader

# 安装依赖
pip install -r requirements.txt
```

或者使用 pip 安装：

```bash
pip install -e .
```

### FFmpeg 安装

视频下载需要 FFmpeg 进行音视频合并，请确保已安装：

- **Windows**: 下载 [FFmpeg](https://ffmpeg.org/download.html) 并配置到环境变量，或在配置文件中指定路径
- **macOS**: `brew install ffmpeg`
- **Linux**: `sudo apt install ffmpeg`

## ⚙️ 配置

### 配置文件

复制示例配置文件并修改：

```bash
cp config/config.example.yaml config/config.yaml
```

### 配置项说明

```yaml
# B站 Cookie（必填）
cookie: "你的B站Cookie字符串"

# 日志配置
log:
  log_dir: "./output/logs"   # 日志文件输出目录，留空则不输出日志文件
  log_level: "INFO"          # 日志级别: DEBUG, INFO, WARNING, ERROR

# 动态导出配置
bilibili:
  mid: "目标UP主MID"         # UP 主的 MID（数字 ID）
  output_dir: "./output"     # 导出文件输出目录

# 下载配置
download:
  links_file: "links.txt"    # 链接文件路径（由 export 命令生成）
  output_dir: "./downloads"  # 下载文件输出目录
  ffmpeg: ""                 # FFmpeg 可执行文件路径（留空则自动检测）
  concurrency: 16            # 分片并发数
  video_quality: 127         # 视频画质（127=8K, 120=4K, 80=1080P, 64=720P）
  audio_quality: 30251       # 音频音质（30251=Hi-Res, 30280=192K, 30216=64K）
```

### 获取 Cookie

1. 浏览器登录 B 站
2. 按 F12 打开开发者工具
3. 切换到 Network（网络）标签
4. 刷新页面，找到任意一个请求
5. 在请求头中找到 `Cookie` 字段，复制完整值

## 🚀 使用方法

### 1. 导出 UP 主动态

```bash
# 使用配置文件中的 mid
python -m enjoy export

# 指定 mid
python -m enjoy export --mid 2073801516

# 指定输出目录
python -m enjoy export --mid 2073801516 --output ./output
```

导出完成后，会在输出目录生成 `UP主名_全量动态汇总报告.txt` 文件，包含所有动态的详细信息。

### 2. 下载视频和图片

```bash
# 使用配置文件中的 links_file
python -m enjoy download

# 指定链接文件
python -m enjoy download --links ./output/UP主名_全量动态汇总报告.txt

# 指定下载目录
python -m enjoy download --links links.txt -o ./downloads

# 指定并发数
python -m enjoy download --links links.txt --concurrency 32

# 详细日志模式
python -m enjoy download --links links.txt -v
```

### 3. 查看帮助

```bash
# 查看所有命令
python -m enjoy --help

# 查看 export 命令帮助
python -m enjoy export --help

# 查看 download 命令帮助
python -m enjoy download --help
```

## 📁 目录结构

### 导出文件结构

```
output/
└── UP主名_全量动态汇总报告.txt
```

### 下载文件结构

```
downloads/
└── UP主名/
    ├── 普通视频/
    │   ├── 日期_标题_BV号.mp4
    │   └── ...
    ├── 充电视频/
    │   └── ...
    ├── 普通图片/
    │   ├── 日期_标题_图片ID.jpg
    │   └── ...
    └── 充电图片/
        └── ...
```

## 🎯 画质说明

### 视频画质 (qn)

| 值   | 画质    |
| ---- | ------- |
| 127  | 8K      |
| 126  | 杜比视界 |
| 125  | HDR     |
| 120  | 4K      |
| 116  | 1080P60 |
| 112  | 1080P+  |
| 80   | 1080P   |
| 64   | 720P    |
| 32   | 480P    |
| 16   | 360P    |

### 音频音质

| 值    | 音质   |
| ----- | ------ |
| 30251 | Hi-Res |
| 30250 | 杜比全景声 |
| 30280 | 192K   |
| 30232 | 132K   |
| 30216 | 64K    |

## 🏗️ 项目结构

```
bilibili_downloader/
├── config/                    # 配置文件目录
│   ├── config.example.yaml    # 配置示例
│   └── config.yaml            # 实际配置
├── src/
│   └── enjoy/
│       ├── api/               # API 客户端
│       │   ├── client.py      # HTTP 客户端封装
│       │   ├── view.py        # 视频信息 API
│       │   ├── playurl.py     # 播放地址 API
│       │   └── feed.py        # 动态 Feed API
│       ├── downloader/        # 下载器
│       │   ├── extractor.py   # URL、BV、CID 解析器
│       │   ├── fetcher.py     # 分片下载、FFmpeg 合并
│       │   ├── track.py       # 音视频轨道选择 + 下载流程
│       │   ├── signer.py      # WBI 签名
│       │   └── image_downloader.py  # 图片下载
│       ├── exporter/          # 导出器
│       │   ├── parser.py      # 动态解析
│       │   └── reporter.py    # TXT 报告生成
│       ├── utils/             # 工具函数
│       │   └── helpers.py     # 通用工具
│       ├── cli.py             # CLI 入口
│       ├── config_loader.py   # 配置加载
│       └── __main__.py        # 模块入口
├── output/                    # 输出目录
│   ├── downloads/             # 下载文件
│   └── logs/                  # 日志文件
├── requirements.txt           # 依赖列表
├── pyproject.toml             # 项目配置
└── README.md                  # 项目说明
```

## ❓ 常见问题

### 1. 提示 "无法解析视频下载 URL"

- 检查 Cookie 是否有效
- 检查网络连接是否正常
- 尝试降低视频画质

### 2. FFmpeg 相关错误

- 确保 FFmpeg 已正确安装
- 在配置文件中指定 FFmpeg 的完整路径

### 3. 下载速度慢

- 增加 `concurrency` 并发数
- 检查网络带宽
- 尝试降低视频画质

### 4. Cookie 过期

- 重新登录 B 站获取新的 Cookie
- 更新配置文件中的 cookie 值

## ⚠️ 免责声明

- 本工具仅供学习和个人使用
- 请遵守 B 站的用户协议和相关法律法规
- 请勿用于商业用途或大规模下载
- 下载的内容版权归原作者所有

## 📄 License

MIT License
