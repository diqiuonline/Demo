# Bilibili Downloader 项目重构设计

## 背景

当前项目包含两个独立的 Python 脚本：
- `export_data.py` — B站 UP 主动态导出工具
- `bilibili_downloader.py` — B站视频下载工具

两者共享 B站 API 交互逻辑，但代码耦合严重，缺乏模块化结构。

## 目标

将项目重构为标准 `src/` 规范的 Python 包工程，统一 CLI 入口，提取配置到 `config.yaml`。

## 目录结构

```
enjoy/
├── config/
│   ├── config.yaml             # 主配置
│   └── config.example.yaml     # 配置模板
├── src/
│   └── enjoy/
│       ├── __init__.py
│       ├── __main__.py         # python -m enjoy 入口
│       ├── cli.py              # CLI 子命令分发
│       ├── config_loader.py    # 配置加载
│       ├── api/                # B站 API 层
│       │   ├── __init__.py
│       │   ├── client.py       # HTTP 客户端
│       │   ├── feed.py         # 动态列表
│       │   ├── detail.py       # 动态详情
│       │   ├── view.py         # 视频信息
│       │   └── playurl.py      # 播放地址
│       ├── downloader/         # 下载模块
│       │   ├── __init__.py
│       │   ├── signer.py       # WBI 签名
│       │   ├── extractor.py    # URL/BV/CID 解析
│       │   ├── fetcher.py      # 分片下载、校验、合并
│       │   └── track.py        # 轨道选择
│       ├── exporter/           # 导出模块
│       │   ├── __init__.py
│       │   ├── parser.py       # 动态数据解析
│       │   └── reporter.py     # TXT 报告生成
│       └── utils/              # 通用工具
│           ├── __init__.py
│           └── helpers.py
├── tests/
├── requirements.txt
├── README.md
└── LICENSE
```

## CLI 设计

```bash
# 导出动态
enjoy export --mid 2073801516 --output report.txt

# 下载视频
enjoy download --links links.txt --cookie cookie.json
```

## 配置管理

`config.yaml` 存放默认值：
```yaml
bilibili:
  mid: "2073801516"
  output_file: "动态汇总报告.txt"
  cookie_file: ""

download:
  output_dir: "./downloads"
  concurrency: 16
  timeout_sec: 60
```

CLI 参数优先于配置文件。

## 模块职责

| 模块 | 职责 |
|------|------|
| `api/client.py` | 统一 aiohttp 会话、Cookie、Headers |
| `api/feed.py` | 动态列表 API 调用 |
| `api/detail.py` | 动态详情 API 调用 |
| `api/view.py` | 视频信息 API 调用 |
| `api/playurl.py` | 播放地址 API 调用 + WBI 签名 |
| `downloader/signer.py` | WBI 签名算法 |
| `downloader/extractor.py` | URL/BV/AID/CID 解析 |
| `downloader/fetcher.py` | 分片下载、MP4 校验、FFmpeg 合并 |
| `downloader/track.py` | 音视频轨道选择 |
| `exporter/parser.py` | 动态数据解析 |
| `exporter/reporter.py` | TXT 报告生成 |
| `utils/helpers.py` | 文件名过滤、日志等 |

## 依赖

- `requests>=2.31` — 同步 HTTP（export 用）
- `aiohttp>=3.9` — 异步 HTTP（download 用）
- `PyYAML>=6.0` — 配置文件解析
