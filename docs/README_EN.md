# CatServer (1.16.5)
![](https://img.shields.io/badge/Minecraft-1.16.5-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-36.2.33-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.16.5-brightgreen.svg?colorB=469C00)

CatServer is a high-performance Forge+Bukkit+Spigot server<br>
The early 1.16.5 version was co-developed with the LoliServer project team. Currently supports most mods and plugins to run stably stably<br>
It will now continue to be maintained as a fork of LoliServer, providing better optimization and compatibility<br>
You can download the latest version from [Jenkins](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.16.5/lastSuccessfulBuild/)<br>

All versions of CatServer:
|    Version    |    Status     |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  Active      |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  LTS         |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  LTS/STABLE  |

QQ Group: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [点击加入](https://discord.gg/wvBJN4d)

### Features
Use self-writing optimization and some Paper optimization to greatly improve performance<br>
Have a powerful Remap system to ensure good plugin compatibility<br>
Good compatibility with FakePlayer<br>
Fix most vanilla bug to safety<br>
Configuration provide user-friendly options<br>
Provide API to allow plugins to interact with mods easily<br>

### Installation
1. Download or build the latest server
2. Create startup script and run (Sample script: java -Xmx2G -jar CatServer-1.16.5-xxxxxxx-server.jar)
3. Wait for the libraries file download complete on the first startup

##### Recommended to run on Java8, although Java9-17 and later versions are supported, there may be compatibility issues with mods and plugins

### Building
- 1.Clone 1.16.5 branch: `git clone -b 1.16.5 https://github.com/Luohuayu/CatServer.git`
- 2.Change directory: `cd CatServer`
- 3.Setup project: `gradlew setup`
- 4.Generate patch(If you modify the Minecraft code): `gradlew genPatches`
- 5.Build: `gradlew buildCatServer`

### Generate dependency
##### TODO
