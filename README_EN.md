# CatServer
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2860-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2%20latest-brightgreen.svg?colorB=469C00)

[Website](https://catmc.org/)
[Sponsor](https://paypal.me/LHYCAT)

CatServer is 1.12.2 version Forge+Bukkit+Spigot server<br>
The longest continuous update time, supporting most mods and plugins to run stably<br>

[Download (Github)](https://github.com/Luohuayu/CatServer/releases)
[Download (Mirror)](https://catserver.moe/download/universal)

### Features
Use self-writing optimization and some Paper optimization to greatly improve performance<br>
Have a powerful Remap system to ensure good plugin compatibility<br>
Good compatibility with FakePlayer<br>
Fix most vanilla bug to safety<br>
Configuration provide user-friendly options<br>
Provide API to allow plugins to interact with mods easily<br>

### Installation
1. Download or build the latest server
2. Create startup script and run (Sample script: java -Xmx2G -jar CatServer-f2bc11f-universal.jar)
3. Wait for the libraries file download complete on the first startup<br>

### Building
- 1.Setup project: gradlew setup<br>
  To import IDEA or Eclipse open: projects/Catserver/.project
- 2.Generate patch: gradlew genPatches<br>
- 3.Build: gradlew build<br>

### Generate dependency
Use [CatServerSRG-Generator](https://github.com/Luohuayu/CatServerSRG-Generator) to generate dependency, which can be used to write plugins to operate NMS or modify MOD

### Chat
Telegram: [@CatServer](https://t.me/CatServer)<br>
Discord: [Click to join](https://discord.gg/wvBJN4d)<br>
