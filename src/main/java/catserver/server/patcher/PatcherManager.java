package catserver.server.patcher;

import catserver.server.CatServer;
import catserver.server.patcher.plugin.*;

import java.util.HashMap;
import java.util.Map;

public class PatcherManager {
    private static Map<String, IPatcher> pluginPatcher = new HashMap<>();

    static {
        if (CatServer.getConfig().enableDynmapCompatible) registerPluginPatcher("dynmap", new DynmapPacher());
        if (CatServer.getConfig().enableCoreProtectModBlockCompatible) registerPluginPatcher("CoreProtect", new CoreProtectPatcher());
        if (CatServer.getConfig().enableEssentialsNewVersionCompatible) registerPluginPatcher("Essentials", new EssentialsPatcher());
        if (CatServer.getConfig().enableMythicMobsPatcherCompatible) registerPluginPatcher("MythicMobs", new MythicMobsPatcher());
        if (CatServer.getConfig().enableWorldEditCompatible) registerPluginPatcher("WorldEdit", new WorldEditPatcher());
        if (CatServer.getConfig().enableCitizensCompatible) registerPluginPatcher("Citizens", new CitizensPatcher());
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
