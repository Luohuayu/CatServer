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

# CatServer (1.18.2)

##### Select Other Language README: [English](README_EN.md) | [中文](README.md) | [Русский](README_RU.md) | [Português](README_BR.md)

CatServer is a high-performance Forge+Bukkit+Spigot server<br>
You can download the latest version from [Jenkins](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/)<br>

All versions of CatServer:
|    Version    |    Status     |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  Active      |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  LTS         |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  LTS/STABLE  |

QQ Group: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [点击加入](https://discord.gg/wvBJN4d)

**Warning: The current version has not been formally tested and should not be used in production environments**

### Don't be restless, CatServer(1.18.2) is in development..
You can contribute to the project in development or testing.

#### Development progress
- CraftBukkit Patches (Almost done)
- Spigot Patches (Done)
- Patches Compatibility (In progress)
- FoxLaunch (Done)
- BukkitInjector (Not impl)
- Plugin Remapper (Not impl)
- Plugin ASM Patcher (Not impl)
- MOD ASM Patcher (Not impl)
- CatServer API (Not impl)

### How to build and develop?
Step:

1. Clone the source code: `git clone -b 1.18.2 https://github.com/Luohuayu/CatServer.git`,
2. Change directory: `cd CatServer`
3. Setup the project: `gradlew setup`, then you can open the project in IDEA
4. Generate patch(If you change Minecraft code): `gradlew genPatches`
5. Build the project: `gradlew buildCatServer`
6. The file will be generated in: `(Project-Dir)/projects/forge/build/libs/`

### How to debug in IDEA?
Run gradle task: `CatServer -> forge -> Tasks -> forgegradle runs -> forge_server`

Then use IDEA to debug or run: `CatServer:projects:forge [forge_server]` (Remember to modify `eula.txt` to agree eula)

### Why is there no Eclipse introduction?
Since I don't use Eclipse, you need to do your own research. (You can refer [Forge Development Documentation](https://mcforge.readthedocs.io/en/latest/forgedev/))

### How to get the last build?
**Tip: The current version is not recommended**

You can download it from our Jenkins: [https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/)
