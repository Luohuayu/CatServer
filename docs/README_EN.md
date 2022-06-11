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

# CatServer (1.12.2)
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2860-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2-brightgreen.svg?colorB=469C00)

CatServer is a high-performance and high-compatibility Forge+Bukkit+Spigot server<br>
The longest continuous update time of 1.12.2 version, supporting most mods and plugins to run stably<br>
You can download the latest version from [Mirror Site](https://catserver.moe/download/universal) or [GitHub Releases](https://github.com/Luohuayu/CatServer/releases)<br>

All versions of CatServer:
|    Version    |    Status     |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  Active      |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  LTS         |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  LTS/STABLE  |

QQ Group: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [Click to join](https://discord.gg/wvBJN4d)<br>

#### How to support us
No need to sponsor, just click "Star" and "Fork" for the repo, and click "Watch" if you want to be notified

### Features
Use self-writing optimization and some Paper optimization to greatly improve performance<br>
Have a powerful Remap system to ensure good plugin compatibility<br>
Good compatibility with FakePlayer<br>
Fix most vanilla bug to safety<br>
Configuration provide user-friendly options<br>
Provide API to allow plugins to interact with mods easily<br>

### Installation
1. Download or build the latest server
2. Create startup script and run (Sample script: java -Xmx2G -jar CatServer-xxxxxxx-universal.jar)
3. Wait for the libraries file download complete on the first startup

##### Recommended to run on Java8, although Java9-17 and later versions are supported, there may be compatibility issues with mods and plugins

### Building
- 1.Setup project: `gradlew setup`
- To import IDEA or Eclipse open: projects/Catserver/.project
- 2.Generate patch(If you modify the Minecraft code): `gradlew genPatches`
- 3.Build: `gradlew build`

### Generate dependency
Use [CatServerSRG-Generator](https://github.com/Luohuayu/CatServerSRG-Generator) to generate dependency, which can be used to write plugins to operate NMS or modify MOD
