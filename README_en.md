# CatServer
![Minecraft Version](https://camo.githubusercontent.com/c0e3577c768e5e75babe16439b3103451e74da0d/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4d696e6563726166742d312e31322e322d627269676874677265656e2e7376673f636f6c6f72423d343639433030)
![Forge Version](https://camo.githubusercontent.com/b6425e7a30e2455617fb01349e2bc40c41d024ca/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f466f7267652d31342e32332e352e323834372d627269676874677265656e2e7376673f636f6c6f72423d343639433030)
![Spigot Version](https://camo.githubusercontent.com/150a941bd9e0f7cf2e88dc4c7e0e83b1c7f40b34/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537069676f742d312e31322e322532306c61746573742d627269676874677265656e2e7376673f636f6c6f72423d343639433030)

CatServer is a Minecraft server software supporting plugins and mods.

# Features
## Multiples APIs support
CatServer supports these APIs:
- Spigot (plugins)
- Forge (mods)
- Sponge throught SpongeForge (plugins)

## Multithreaded
CatServer has a multithreaded version named `async` using multiples threads unlike Forge/Spigot/Vanilla servers. It allow your server to do resources-intensives tasks without blocking it entirely.

Note this version is less-stable than the `universal` one.

# Install
This software requires Java 8 or later. Note Java is retrocompatible.

[Download](https://github.com/Luohuayu/CatServer/releases) and place the server jar in an empty directory. Now launch it using the `java` command:
```sh
java -jar yourJar.jar
```
See also:
- [Configuring JVM arguments](https://docs.oracle.com/cd/E22289_01/html/821-1274/configuring-the-default-jvm-and-java-arguments.html)
- [Spigot configuration](https://www.spigotmc.org/wiki/spigot-configuration/)
- [Vanilla server's properties](https://www.spigotmc.org/wiki/spigot-configuration-server-properties/)

# Contribute
You can contribute to this project by:
- Opening issues when you find a bug/missing feature
- Forking it and send pull requests

# More informations
- Telegram: https://t.me/CatServer
- Website: http://catserver.moe
