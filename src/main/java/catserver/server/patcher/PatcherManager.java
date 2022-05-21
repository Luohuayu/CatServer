package catserver.server.patcher;

import catserver.server.CatServer;
import catserver.server.patcher.plugin.DynmapPatcher;
import catserver.server.patcher.plugin.EssentialsPatcher;
import catserver.server.patcher.plugin.MythicMobsPatcher;
import catserver.server.patcher.plugin.ProtocolBCDisablePatcher;
import catserver.server.patcher.plugin.WorldEditPatcher;

import java.util.HashMap;
import java.util.Map;

public class PatcherManager {
    private static Map<String, IPatcher> pluginPatcher = new HashMap<>();

    static {
        registerPluginPatcher("ProtocolLib", new ProtocolBCDisablePatcher()); // Must disable background compiler, asm generator isn't work on forge
        if (CatServer.getConfig().enableEssentialsNewVersionCompatible) registerPluginPatcher("Essentials", new EssentialsPatcher());
        if (CatServer.getConfig().enableMythicMobsPatcherCompatible) registerPluginPatcher("MythicMobs", new MythicMobsPatcher());
        if (CatServer.getConfig().enableWorldEditCompatible) registerPluginPatcher("WorldEdit", new WorldEditPatcher());
        if (CatServer.getConfig().enableDynmapCompatible) registerPluginPatcher("dynmap", new DynmapPatcher());
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
