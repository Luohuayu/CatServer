# CatServer
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2860-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2-brightgreen.svg?colorB=469C00)

[官网](https://catmc.org/)
[赞助](https://afdian.net/@Luohuayu)
[English](README_EN.md)
[Русский](README_RU.md)

CatServer是1.12.2的Forge+Bukkit+Spigot服务端核心<br>
国内最早开发的高版本核心, 支持大部分MOD和插件同时稳定运行<br>

[下载(国内镜像)](https://catserver.moe/download/universal)
[下载(Github)](https://github.com/Luohuayu/CatServer/releases)

### 服务端特性
使用原创优化和部分Paper优化大幅度提升性能<br>
拥有强大的Remap系统保证良好的插件兼容性<br>
对于MOD的虚拟玩家拥有良好的兼容<br>
修复大多数原版漏洞保证安全<br>
配置文件提供人性化选项<br>
提供API让插件轻松与MOD交互<br>

### 使用方法
1. 下载或自行构建最新服务端
2. 创建启动脚本并运行 (示例启动参数: java -Xmx2G -jar CatServer-f2bc11f-universal.jar)
3. 首次启动请耐心等待库文件下载完成<br>

### 构建方法
- 1.配置项目: gradlew setup<br>
  如需导入IDEA或Eclipse打开: projects/Catserver/.project
- 2.生成补丁: gradlew genPatches<br>
- 3.编译项目: gradlew build<br>

### 生成依赖库
使用 [CatServerSRG-Generator](https://github.com/Luohuayu/CatServerSRG-Generator) 生成依赖库, 可用于编写插件操作NMS或直接修改MOD

### 加入交流群
QQ群: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW)<br>
Telegram: [@CatServer](https://t.me/CatServer)<br>
Discord: [点击加入](https://discord.gg/wvBJN4d)<br>
