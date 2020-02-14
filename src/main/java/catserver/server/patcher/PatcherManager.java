package catserver.server.patcher;

import java.util.HashMap;
import java.util.Map;

public class PatcherManager {
    private static Map<String, IPatcher> pluginPatcher = new HashMap<>();

    static {
        // registerPluginPatcher("CatTestPlugin", new TestPatcher());
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
