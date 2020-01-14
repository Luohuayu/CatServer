# ParrotMC - IN DEVELOPMENT
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2847-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2%20latest-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/CatSever-20.01.11-brightgreen)

ParrotMC is a fork of [CatServer](https://github.com/Luohuayu/CatServer) using Graal technology.

# Features
## Spigot, Forge and Sponge APIs support
Like CatServer, ParrotMC supports Forge mods, Spigot and Sponge plugins stably.
- Put Forge mods in the `mods` directory
- Put Spigot plugins in the `plugins` directory
- Put Sponge plugins in the `mods/plugins` directory

# Next Fatures
**WARNING: These features will be present in the first release of this projet**
## Feather-Lightweight
ParrotMC use Graal's `native-image` tool to produce lightweight native images for Linux, Windows and Mac OS.

Generated images consume less memory and start faster. Graal is also able to compile Just In Time plugins and mods faster than the Java HotSpot VM
![Graal vs HotSpot](https://pbs.twimg.com/media/DgOjz4hVQAAWwil.png)
*Graal vs Java HotSpot performances*

## Polyglots mods and plugins
ParrotMC allow you to load and execute polyglots mods and plugins using GraalVM. You can add your own language by editing/forking [Graal's Truffle Framework](https://github.com/oracle/graal/tree/master/truffle).

The default Truffle implementation supports:
- JVM languages (Java, Kotlin, Scala, Groovy...)
- JavaScript & NodeJS
- Ruby
- R
- Python
- grCUDA
- C++
- C
- Rust
