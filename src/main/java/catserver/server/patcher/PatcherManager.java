package catserver.server.patcher;

import catserver.server.CatServer;
import catserver.server.patcher.plugin.CoreProtectPatcher;
import catserver.server.patcher.plugin.DisablePluginPatcher;
import catserver.server.patcher.plugin.DynmapPacher;
import catserver.server.patcher.plugin.EssentialsPatcher;

import java.util.HashMap;
import java.util.Map;

public class PatcherManager {
    private static Map<String, IPatcher> pluginPatcher = new HashMap<>();

    static {
        if (CatServer.getConfig().enableDynmapCompatible) registerPluginPatcher("dynmap", new DynmapPacher());
        if (CatServer.getConfig().enableCoreProtectModBlockCompatible) registerPluginPatcher("CoreProtect", new CoreProtectPatcher());
        if (CatServer.getConfig().enableEssentialsNewVersionCompatible) registerPluginPatcher("Essentials", new EssentialsPatcher());
    }

    public static IPatcher getPluginPatcher(String pluginName) {
        return pluginPatcher.get(pluginName);
    }

    public static boolean registerPluginPatcher(String pluginName, IPatcher patcher) {
        if (!pluginPatcher.containsKey(pluginName) && patcher != null) {
            pluginPatcher.put(pluginName, patcher);
            return true;
        }
        return false;
    }
}
