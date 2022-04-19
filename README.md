# CatServer (1.16.5)
![](https://img.shields.io/badge/Minecraft-1.16.5-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-36.2.33-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.16.5-brightgreen.svg?colorB=469C00)

##### TODO: 选择其他语言简介 (Select Other Language README): [English](README_EN.md) | [Русский](README_RU.md)

CatServer是一个高性能的Forge+Bukkit+Spigot服务端核心<br>
早期的1.16.5版本交付 LoliServer 项目组并共同开发, 目前已支持大部分MOD和插件同时稳定运行<br>
现在将作为 LoliServer 的分支继续维护, 提供更好的优化和兼容性<br>

QQ群: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [点击加入](https://discord.gg/wvBJN4d)

其他版本:
[LoliServer (1.16.5)](https://github.com/Loli-Server/LoliServer) | [FoxServer (1.18.2)](https://github.com/Luohuayu/FoxServer)

### 服务端特性
使用原创优化和部分Paper优化大幅度提升性能<br>
拥有强大的Remap系统保证良好的插件兼容性<br>
对于MOD的虚拟玩家拥有良好的兼容<br>
修复大多数原版漏洞保证安全<br>
配置文件提供人性化选项<br>
提供API让插件轻松与MOD交互<br>

### 使用方法
1. 下载或自行构建最新服务端
2. 创建启动脚本并运行 (示例启动参数: java -jar CatServer-1.16.5-xxxxxxx-server.jar)
3. 首次启动请耐心等待库文件下载完成<br>

### 构建方法
- 1.配置项目: gradlew setup<br>
- 2.生成补丁(如果你修改了Minecraft代码): gradlew genPatches<br>
- 3.编译项目: gradlew buildCatServer<br>

### 生成依赖库
##### TODO
