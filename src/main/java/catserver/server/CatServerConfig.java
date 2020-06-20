package catserver.server;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CatServerConfig {
    private final File configFile;
    private YamlConfiguration config;

    public boolean keepSpawnInMemory = true;
    public boolean enableSkipEntityTick = true;
    public boolean enableSkipTileEntityTick = false;
    public long worldGenMaxTickTime = 15000000L;
    public List<String> disableForgeGenerateWorlds = Arrays.asList("ExampleCustomWorld");
    public boolean preventBlockLoadChunk = false;
    public List<Integer> autoUnloadDimensions = Arrays.asList(99999999);
    public boolean enableRealtime = false;
    public boolean forceSaveOnWatchdog = true;

    public List<String> fakePlayerPermissions = Arrays.asList("essentials.build");
    public boolean fakePlayerEventPass = false;

    public boolean disableUpdateGameProfile = false;
    public boolean disableFMLHandshake = false;
    public boolean disableFMLStatusModInfo = false;

    public CatServerConfig(String file) {
        this.configFile = new File(file);
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        // world
        keepSpawnInMemory = getOrWriteBooleanConfig("world.keepSpawnInMemory", keepSpawnInMemory);
        enableSkipEntityTick = getOrWriteBooleanConfig("world.enableSkipEntityTick", enableSkipEntityTick);
        enableSkipTileEntityTick = getOrWriteBooleanConfig("world.enableSkipTileEntityTick", enableSkipTileEntityTick);
        worldGenMaxTickTime = getOrWriteIntConfig("world.worldGenMaxTick", 15) * 1000000;
        disableForgeGenerateWorlds = getOrWriteStringListConfig("world.disableForgeGenerateWorlds", disableForgeGenerateWorlds);
        preventBlockLoadChunk = getOrWriteBooleanConfig("world.preventBlockLoadChunk", preventBlockLoadChunk);
        autoUnloadDimensions = getOrWriteIntegerListConfig("world.autoUnloadDimensions", autoUnloadDimensions);
        enableRealtime = getOrWriteBooleanConfig("world.enableRealtime", enableRealtime);
        forceSaveOnWatchdog = getOrWriteBooleanConfig("world.forceSaveOnWatchdog", forceSaveOnWatchdog);
        // fakeplayer
        fakePlayerPermissions = getOrWriteStringListConfig("fakePlayer.permissions", fakePlayerPermissions);
        fakePlayerEventPass = getOrWriteBooleanConfig("fakePlayer.eventPass", fakePlayerEventPass);
        // general
        disableUpdateGameProfile = getOrWriteBooleanConfig("disableUpdateGameProfile", disableUpdateGameProfile);
        disableFMLHandshake = getOrWriteBooleanConfig("disableFMLHandshake", disableFMLHandshake);
        disableFMLStatusModInfo = getOrWriteBooleanConfig("disableFMLStatusModInfo", disableFMLStatusModInfo);
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
