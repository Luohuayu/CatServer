<p align="center">
<img src="https://i.imgur.com/REaCITJ.png" width="512" height="512"/>
</p>
<p align="center">
<a href="#"><img title="CatServer (1.12.2)" src="https://img.shields.io/badge/CatServer-green?colorA=%23ff0000&colorB=%23017e40&style=for-the-badge"></a>
</p>
<p align="center">
<a href="https://github.com/Luohuayu/"><img title="Author" src="https://img.shields.io/badge/AUTHOR-Luohuayu-orange.svg?style=for-the-badge&logo=github"></a>
</p>
<p align="center">
<a href="https://github.com/Luohuayu/followers"><img title="Followers" src="https://img.shields.io/github/followers/Luohuayu?color=blue&style=flat-square"></a>
<a href="https://github.com/Luohuayu/CatServer/stargazers/"><img title="Stars" src="https://img.shields.io/github/stars/Luohuayu/CatServer?color=red&style=flat-square"></a>
<a href="https://github.com/Luohuayu/CatServer/network/members"><img title="Forks" src="https://img.shields.io/github/forks/Luohuayu/CatServer?color=red&style=flat-square"></a>
<a href="https://github.com/Luohuayu/CatServer/watchers"><img title="Watching" src="https://img.shields.io/github/watchers/Luohuayu/CatServer?label=Watchers&color=blue&style=flat-square"></a>
</p>

# CatServer (1.16.5)
![](https://img.shields.io/badge/Minecraft-1.16.5-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-36.2.35-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.16.5-brightgreen.svg?colorB=469C00)

##### 选择其他语言简介 (Select Other Language README): [English](README_EN.md) | [Русский](README_RU.md) | [Português](README_BR.md)

CatServer是一个高性能的Forge+Bukkit+Spigot服务端核心<br>
早期的1.16.5版本交付 LoliServer 项目组并共同开发, 目前已支持大部分MOD和插件同时稳定运行<br>
现在将作为 LoliServer 的分支继续维护, 提供更好的优化和兼容性<br>
你可以从 [构建站](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.16.5/lastSuccessfulBuild/) 下载到最新版本<br>

CatServer所有版本:
|     版本      |     状态      |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  可用           |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  长期支持       |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  长期支持/稳定  |

QQ群: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [点击加入](https://discord.gg/wvBJN4d)

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
3. 首次启动请耐心等待库文件下载完成

##### 推荐使用Java8或11运行, 虽然支持Java12-17及更高版本但MOD和插件可能存在兼容性问题

### 构建方法
- 1.克隆1.16.5分支: `git clone -b 1.16.5 https://github.com/Luohuayu/CatServer.git`
- 2.切换目录: `cd CatServer`
- 3.配置项目: `gradlew setup`
- 4.生成补丁(如果你修改了Minecraft代码): `gradlew genPatches`
- 5.编译项目: `gradlew buildCatServer`

### 生成依赖库
##### TODO

### 赞助商
[7yPAY支付商品寄售平台 ｜ 费率低至2.5% | 无需营业执照可对接微信一清](https://pay.7ycloud.cn/help?catgithub)
