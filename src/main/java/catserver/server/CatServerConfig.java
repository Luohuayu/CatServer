package catserver.server;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CatServerConfig {
    private final File configFile;
    private YamlConfiguration config;

    public List<String> fakePlayerPermissions = Lists.<String>newArrayList("essentials.build");
    public boolean versionCheck = true;

    public CatServerConfig(String file) {
        this.configFile = new File(file);
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        // fakeplayer
        fakePlayerPermissions = getOrWriteStringListConfig("fakePlayer.permissions", fakePlayerPermissions);
        // general
        versionCheck = getOrWriteBooleanConfig("versionCheck", versionCheck);
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
