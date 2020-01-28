# CatServer
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
