# Enjoy - Bilibili 工具集

B站 UP 主动态导出 + 视频下载工具。

## 安装

```bash
pip install -r requirements.txt
```

## 配置

```bash
cp config/config.example.yaml config/config.yaml
# 编辑 config.yaml 填入 cookie 等信息
```

## 用法

```bash
# 导出动态汇总
python -m enjoy export --mid 2073801516

# 下载视频
python -m enjoy download --links links.txt --cookie cookie.json

# 查看帮助
python -m enjoy --help
```
