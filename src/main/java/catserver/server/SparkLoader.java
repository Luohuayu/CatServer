package catserver.server;

import catserver.server.launch.LibrariesManager;
import java.io.File;
import java.nio.file.Files;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class SparkLoader {
    private static final boolean enableSpark = Boolean.parseBoolean(System.getProperty("catserver.spark.enable", "true"));
    private static boolean sparkPluginEnabled = false;

    public static boolean isEnableSpark() {
        return enableSpark;
    }

    public static boolean isSparkPluginEnabled() {
        Plugin sparkPlugin = Bukkit.getServer().getPluginManager().getPlugin("spark");
        return sparkPlugin != null && sparkPlugin.isEnabled();
    }

    public static void tryLoadSparkPlugin(SimplePluginManager pluginManager) {
        if (!isEnableSpark() || pluginManager.getPlugin("spark") != null) return;
        try {
            File sparkPluginOriginFile = new File("libraries", LibrariesManager.sparkPluginFileName);
            File sparkPluginFile = new File("plugins", sparkPluginOriginFile.getName());
            if (sparkPluginOriginFile.exists() && !sparkPluginFile.exists()) {
                Files.copy(sparkPluginOriginFile.toPath(), sparkPluginFile.toPath());
            }
            Plugin sparkPlugin = pluginManager.loadPlugin(sparkPluginFile);
            sparkPlugin.onLoad();
            pluginManager.enablePlugin(sparkPlugin);
        } catch (Exception e) {
            new RuntimeException("Failed to load spark!", e).printStackTrace();
        }
    }
}
