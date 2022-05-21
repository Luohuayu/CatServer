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
    public List<Integer> autoUnloadDimensions = Lists.<Integer>newArrayList(99999999);
    public boolean enableRealtime = false;
    public boolean forceSaveOnWatchdog = true;
    public int maxEntityCollision = 8;
    public boolean saveBukkitWorldDimensionId = true;

    public List<String> fakePlayerPermissions = Lists.<String>newArrayList("essentials.build");
    public boolean fakePlayerEventPass = false;

    public boolean enableDynmapCompatible = true;
    public boolean enableCoreProtectModBlockCompatible = true;
    public boolean enableEssentialsNewVersionCompatible = true;
    public boolean enableMythicMobsPatcherCompatible = true;
    public boolean enableWorldEditCompatible = true;
    public boolean enableCitizensCompatible = true;
    public List<String> disableHopperMoveEventWorlds = Lists.<String>newArrayList();
    public boolean defaultInstallPluginSpark = true;

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
        // enableSkipEntityTick = getOrWriteBooleanConfig("world.enableSkipEntityTick", enableSkipEntityTick); // TODO
        // enableSkipTileEntityTick = getOrWriteBooleanConfig("world.enableSkipTileEntityTick", enableSkipTileEntityTick); // TODO
        // worldGenMaxTickTime = getOrWriteIntConfig("world.worldGenMaxTick", worldGenMaxTickTime); // TODO
        // disableForgeGenerateWorlds = getOrWriteStringListConfig("world.disableForgeGenerateWorlds", disableForgeGenerateWorlds); // TODO
        // autoUnloadDimensions = getOrWriteIntegerListConfig("world.autoUnloadDimensions", autoUnloadDimensions); // TODO
        // enableRealtime = getOrWriteBooleanConfig("world.enableRealtime", enableRealtime); // TODO
        forceSaveOnWatchdog = getOrWriteBooleanConfig("world.forceSaveOnWatchdog", forceSaveOnWatchdog);
        // maxEntityCollision = getOrWriteIntConfig("world.maxEntityCollision", maxEntityCollision); // TODO
        // saveBukkitWorldDimensionId = getOrWriteBooleanConfig("world.saveBukkitWorldDimensionId", saveBukkitWorldDimensionId); // TODO: Is it necessary on 1.16+?
        // fakeplayer
        fakePlayerPermissions = getOrWriteStringListConfig("fakePlayer.permissions", fakePlayerPermissions);
        fakePlayerEventPass = getOrWriteBooleanConfig("fakePlayer.eventPass", fakePlayerEventPass);
        // plugin
        enableDynmapCompatible = getOrWriteBooleanConfig("plugin.patcher.enableDynmapCompatible", enableDynmapCompatible);
        // enableCoreProtectModBlockCompatible = getOrWriteBooleanConfig("plugin.patcher.enableCoreProtectModBlockCompatible", enableCoreProtectModBlockCompatible); // TODO
        enableEssentialsNewVersionCompatible = getOrWriteBooleanConfig("plugin.patcher.enableEssentialsNewVersionCompatible", enableEssentialsNewVersionCompatible);
        enableMythicMobsPatcherCompatible = getOrWriteBooleanConfig("plugin.patcher.enableMythicMobsPatcherCompatible", enableMythicMobsPatcherCompatible);
        enableWorldEditCompatible = getOrWriteBooleanConfig("plugin.patcher.enableWorldEditCompatible", enableWorldEditCompatible);
        // enableCitizensCompatible = getOrWriteBooleanConfig("plugin.patcher.enableCitizensCompatible", enableCitizensCompatible); // TODO
        // disableHopperMoveEventWorlds = getOrWriteStringListConfig("plugin.disableHopperMoveEventWorlds", disableHopperMoveEventWorlds); // TODO
        // defaultInstallPluginSpark = getOrWriteBooleanConfig("plugin.defaultInstall.spark", defaultInstallPluginSpark); // TODO
        // general
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
