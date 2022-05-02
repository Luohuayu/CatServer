# CatServer (1.12.2)
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2860-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2-brightgreen.svg?colorB=469C00)

##### 选择其他语言简介 (Select Other Language README): [English](README_EN.md) | [Русский](README_RU.md)

CatServer是一个高性能和高兼容性的Forge+Bukkit+Spigot服务端核心<br>
国内最早开发的1.12.2版本核心, 支持大部分MOD和插件同时稳定运行<br>
你可以从 [国内镜像](https://catserver.moe/download/universal) 或 [GitHub Releases](https://github.com/Luohuayu/CatServer/releases) 下载到最新版本<br>

CatServer所有版本:
|     版本      |     状态      |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  可用           |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  长期支持       |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  长期支持/稳定  |

QQ群: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [点击加入](https://discord.gg/wvBJN4d)

#### 如何支持我们
不必赞助, 只需要给本仓库点击"Star"和"Fork", 如果愿意收到通知可以点击"Watch"

### 服务端特性
使用原创优化和部分Paper优化大幅度提升性能<br>
拥有强大的Remap系统保证良好的插件兼容性<br>
对于MOD的虚拟玩家拥有良好的兼容<br>
修复大多数原版漏洞保证安全<br>
配置文件提供人性化选项<br>
提供API让插件轻松与MOD交互<br>

### 使用方法
1. 下载或自行构建最新服务端
2. 创建启动脚本并运行 (示例启动参数: java -Xmx2G -jar CatServer-xxxxxxx-universal.jar)
3. 首次启动请耐心等待库文件下载完成

##### 推荐使用Java8运行, 虽然支持Java9-17及更高版本但MOD和插件可能存在兼容性问题

### 构建方法
- 1.配置项目: `gradlew setup`<br>
- 如需导入IDEA或Eclipse打开: `projects/Catserver/.project`
- 2.生成补丁(如果你修改了Minecraft代码): `gradlew genPatches`
- 3.编译项目: `gradlew build`

### 生成依赖库
使用 [CatServerSRG-Generator](https://github.com/Luohuayu/CatServerSRG-Generator) 生成依赖库, 可用于编写插件操作NMS或直接修改MOD
