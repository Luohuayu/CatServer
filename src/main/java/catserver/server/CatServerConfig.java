package catserver.server;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CatServerConfig {
    private final File configFile;
    private YamlConfiguration config;

    public boolean keepSpawnInMemory = true;
    public boolean enableSkipEntityTick = true;
    public boolean enableSkipTileEntityTick = false;
    public int worldGenMaxTickTime = 15;
    public List<String> disableForgeGenerateWorlds = Lists.<String>newArrayList("ExampleCustomWorld");
    public boolean preventBlockLoadChunk = false;
    public List<Integer> autoUnloadDimensions = Lists.<Integer>newArrayList(99999999);
    public boolean enableRealtime = false;
    public boolean forceSaveOnWatchdog = true;
    public int maxEntityCollision = 8;
    public boolean saveBukkitWorldDimensionId = true;

    public List<String> fakePlayerPermissions = Lists.<String>newArrayList("essentials.build");
    public boolean fakePlayerEventPass = false;

    public boolean fixPlayBossSoundToOtherWorld = true;
    public boolean fixLessCrystalRespawnDragon = false;
    public boolean preventPistonPushRail = false;
    public boolean preventPistonPushSlimeBlock = false;

    public boolean enableDynmapCompatible = true;
    public boolean enableCoreProtectModBlockCompatible = true;
    public boolean enableEssentialsNewVersionCompatible = true;
    public boolean enableMythicMobsPatcherCompatible = true;
    public boolean enableWorldEditCompatible = true;
    public boolean enableCitizensCompatible = true;
    public List<String> disableHopperMoveEventWorlds = Lists.<String>newArrayList();

    public boolean waitForgeServerChatEvent = false;

    public boolean disableUpdateGameProfile = false;
    public boolean disableFMLHandshake = false;
    public boolean disableFMLStatusModInfo = false;
    public boolean disableAsyncCatchWarn = false;
    public boolean versionCheck = true;

    public CatServerConfig(String file) {
        this.configFile = new File(file);
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        // world
        keepSpawnInMemory = getOrWriteBooleanConfig("world.keepSpawnInMemory", keepSpawnInMemory);
        enableSkipEntityTick = getOrWriteBooleanConfig("world.enableSkipEntityTick", enableSkipEntityTick);
        enableSkipTileEntityTick = getOrWriteBooleanConfig("world.enableSkipTileEntityTick", enableSkipTileEntityTick);
        worldGenMaxTickTime = getOrWriteIntConfig("world.worldGenMaxTick", worldGenMaxTickTime);
        disableForgeGenerateWorlds = getOrWriteStringListConfig("world.disableForgeGenerateWorlds", disableForgeGenerateWorlds);
        preventBlockLoadChunk = getOrWriteBooleanConfig("world.preventBlockLoadChunk", preventBlockLoadChunk);
        autoUnloadDimensions = getOrWriteIntegerListConfig("world.autoUnloadDimensions", autoUnloadDimensions);
        enableRealtime = getOrWriteBooleanConfig("world.enableRealtime", enableRealtime);
        forceSaveOnWatchdog = getOrWriteBooleanConfig("world.forceSaveOnWatchdog", forceSaveOnWatchdog);
        maxEntityCollision = getOrWriteIntConfig("world.maxEntityCollision", maxEntityCollision);
        saveBukkitWorldDimensionId = getOrWriteBooleanConfig("world.saveBukkitWorldDimensionId", saveBukkitWorldDimensionId);
        // fakeplayer
        fakePlayerPermissions = getOrWriteStringListConfig("fakePlayer.permissions", fakePlayerPermissions);
        fakePlayerEventPass = getOrWriteBooleanConfig("fakePlayer.eventPass", fakePlayerEventPass);
        // vanilla
        fixPlayBossSoundToOtherWorld = getOrWriteBooleanConfig("vanilla.fixPlayBossSoundToOtherWorld", fixPlayBossSoundToOtherWorld);
        fixLessCrystalRespawnDragon = getOrWriteBooleanConfig("vanilla.fixLessCrystalRespawnDragon", fixLessCrystalRespawnDragon);
        preventPistonPushRail = getOrWriteBooleanConfig("vanilla.preventPistonPushRail", preventPistonPushRail);
        preventPistonPushSlimeBlock = getOrWriteBooleanConfig("vanilla.preventPistonPushSlimeBlock", preventPistonPushRail);
        // plugin
        enableDynmapCompatible = getOrWriteBooleanConfig("plugin.patcher.enableDynmapCompatible", enableDynmapCompatible);
        enableCoreProtectModBlockCompatible = getOrWriteBooleanConfig("plugin.patcher.enableCoreProtectModBlockCompatible", enableCoreProtectModBlockCompatible);
        enableEssentialsNewVersionCompatible = getOrWriteBooleanConfig("plugin.patcher.enableEssentialsNewVersionCompatible", enableEssentialsNewVersionCompatible);
        enableMythicMobsPatcherCompatible = getOrWriteBooleanConfig("plugin.patcher.enableMythicMobsPatcherCompatible", enableMythicMobsPatcherCompatible);
        enableWorldEditCompatible = getOrWriteBooleanConfig("plugin.patcher.enableWorldEditCompatible", enableWorldEditCompatible);
        enableCitizensCompatible = getOrWriteBooleanConfig("plugin.patcher.enableCitizensCompatible", enableCitizensCompatible);
        disableHopperMoveEventWorlds = getOrWriteStringListConfig("plugin.disableHopperMoveEventWorlds", disableHopperMoveEventWorlds);
        // async
        waitForgeServerChatEvent = getOrWriteBooleanConfig("async.waitForgeServerChatEvent", waitForgeServerChatEvent);
        // general
        disableUpdateGameProfile = getOrWriteBooleanConfig("disableUpdateGameProfile", disableUpdateGameProfile);
        disableFMLHandshake = getOrWriteBooleanConfig("disableFMLHandshake", disableFMLHandshake);
        disableFMLStatusModInfo = getOrWriteBooleanConfig("disableFMLStatusModInfo", disableFMLStatusModInfo);
        disableAsyncCatchWarn = getOrWriteBooleanConfig("disableAsyncCatchWarn", disableAsyncCatchWarn);
        versionCheck = getOrWriteBooleanConfig("versionCheck", versionCheck);
        // save config
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean getOrWriteBooleanConfig(String path, boolean def) {
        if (config.contains(path)) {
            return config.getBoolean(path);
        }
        config.set(path, def);
        return def;
    }

    private int getOrWriteIntConfig(String path, int def) {
        if (config.contains(path)) {
            return config.getInt(path);
        }
        config.set(path, def);
        return def;
    }

    private List<String> getOrWriteStringListConfig(String path, List<String> def) {
        if (config.contains(path)) {
            return config.getStringList(path);
        }
        config.set(path, def);
        return def;
    }

    private List<Integer> getOrWriteIntegerListConfig(String path, List<Integer> def) {
        if (config.contains(path)) {
            return config.getIntegerList(path);
        }
        config.set(path, def);
        return def;
    }
}
