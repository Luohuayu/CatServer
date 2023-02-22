# CatServer (1.18.2)

**警告: 当前版本未经过正式测试, 请勿用于生产环境**

**Warn: The current version has not been formally tested and do not use it in production environment**

### 稍安勿躁, 猫端(1.18.2)正在开发中.. (Don't be restless, CatServer(1.18.2) is in development..)
你可以参与开发或测试, 为项目作出贡献

You can contribute to the project by join in development or testing.

### 开发提示 (Devlop tips)
- 跳过移除 LootItemRandomChanceCondition.java.patch (Skip delete LootItemRandomChanceCondition.java.patch)
- 请勿添加/修改 /src/test/ 下任意文件 (Skip create&change package: /src/test/ all files)

#### 核心版本 (Core Version)
- CraftBukkit ([**10ba1beb642**](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/10ba1beb64286206f4edc8d4b44c6472639d044e))
- Bukkit ([**6a039faf748**](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/commits/6a039faf7488beba29527a112404cbab84d0cbd3))
- MinecraftForge ([**1.18.2-40.2.1-9ef5d54**](https://github.com/MinecraftForge/MinecraftForge/commit/b63dc48e19d31d1a469827a828046bbef0976300))

#### 开发进度 (Development progress)
- CraftBukkit Patches (Almost done)
- Spigot Patches (Almost done)
- Patches Compatibility (In progress)
- FoxLaunch (Done)
- BukkitInjector (Almost done)
- Plugin Remapper (Done)
- Plugin ASM Patcher (Done)
- MOD ASM Patcher (Not impl)
- CatServer API (Not impl)
- CatServer Eventhandler (In progress)
- CatServer Hooks (Not impl)

### 如何构建和开发? (How to build and develop?)
步骤:

1. 克隆源码: `git clone -b 1.18.2 https://github.com/Luohuayu/CatServer.git`,
2. 切换目录: `cd CatServer`
3. 初始化项目: `gradlew setup`, 然后你就可以在IDEA里打开项目了
4. 生成补丁文件 (如果你修改了Minecraft代码): `gradlew genPatches`
5. 构建项目: `gradlew buildCatServer`
6. 文件将生成在: `(项目目录)/projects/forge/build/libs/`

Step:

1. Clone the source code: `git clone -b 1.18.2 https://github.com/Luohuayu/CatServer.git`
2. Change directory: `cd CatServer`
3. Setup the project: `gradlew setup`, then you can use IDEA open the project
4. Generate patches file (If you change Minecraft code): `gradlew genPatches`
5. Build the project: `gradlew buildCatServer`
6. The file will be generated in: `(Project-Dir)/projects/forge/build/libs/`

### 如何在IDEA里调试? (How to debug in IDEA?)
运行gradle任务: `CatServer -> forge -> Tasks -> forgegradle runs -> forge_server`

然后使用IDEA调试或运行: `CatServer:projects:forge [forge_server]` (记得修改`eula.txt`同意eula)

Run gradle task: `CatServer -> forge -> Tasks -> forgegradle runs -> forge_server`

Then use IDEA to debug or run:  `CatServer:projects:forge [forge_server]` (Remember to modify `eula.txt` to agree eula)

### 为什么没有Eclipse教程? (Why is there no Eclipse introduction?)
因为我不使用Eclipse, 你需要自己研究 (可以参考[Forge开发文档](https://mcforge.readthedocs.io/en/latest/forgedev/))

I don't use Eclipse, you'll need to do your own research. (You can refer [Forge dev doc](https://mcforge.readthedocs.io/en/latest/forgedev/))

### 如何获取构建? (How to get the build?)
**提示: 当前版本不推荐使用 (Tip: The current version is not recommended)**

你可以从构建站下载: [https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/)

You can download it from our Jenkins: [https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/)
