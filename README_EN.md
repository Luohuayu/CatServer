# CatServer
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2860-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2-brightgreen.svg?colorB=469C00)

CatServer is 1.12.2 version Forge+Bukkit+Spigot server<br>
The longest continuous update time, supporting most mods and plugins to run stably<br>
You can download the latest version from [Mirror Site](https://catserver.moe/download/universal) or [GitHub Releases](https://github.com/Luohuayu/CatServer/releases)<br>

All versions of CatServer:
[1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2) | [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)

Other fork:
[LoliServer (1.16.5)](https://github.com/Loli-Server/LoliServer) | [FoxServer (1.18.2)](https://github.com/Luohuayu/FoxServer)

QQ Group: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [Click to join](https://discord.gg/wvBJN4d)<br>

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
3. Wait for the libraries file download complete on the first startup<br>

### Building
- 1.Setup project: `gradlew setup`
- To import IDEA or Eclipse open: projects/Catserver/.project
- 2.Generate patch(If you modify the Minecraft code): `gradlew genPatches`
- 3.Build: `gradlew build`

### Generate dependency
Use [CatServerSRG-Generator](https://github.com/Luohuayu/CatServerSRG-Generator) to generate dependency, which can be used to write plugins to operate NMS or modify MOD
