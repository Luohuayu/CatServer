package catserver.server;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.spigotmc.AsyncCatcher;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CatServer {
    public static final Logger log = LogManager.getLogger("CatServer");
    private static final String version = "2.1.0";
    private static final String native_version = "v1_12_R1";

    private static CatServerConfig config = new CatServerConfig("catserver.yml");

    public static String getVersion(){
        return version;
    }

    public static String getNativeVersion() {
        return native_version;
    }

    public static boolean asyncCatch(String reason) {
        if (AsyncCatcher.enabled && Thread.currentThread() != MinecraftServer.getServerInst().primaryThread) {
            log.debug(new IllegalStateException("Try to asynchronously " + reason + ", caught!"));
            return true;
        }
        return false;
    }

    public static boolean asyncCatch(String reason, Runnable runnable) {
        if (asyncCatch(reason)) {
            postPrimaryThread(runnable);
            return true;
        }
        return false;
    }

    public static void runWatchdogForceExitTask() {
        MinecraftServer.getServerInst().primaryThread.suspend();
        new Timer("WatchdogForceExitTask").schedule(new TimerTask() {
            public void run() {
                Runtime.getRuntime().exit(0);
            }
        }, 300 * 1000);
    }

    public static boolean isSendDataSerializers(Map<String, String> modList) {
        String forgeVersion = modList.get("forge");
        if (forgeVersion != null) {
            try {
                if (Integer.parseInt(forgeVersion.split("\\.")[3]) < 2826) {
                    return false;
                }
            } catch (Exception ignored) {}
        }
        return true;
    }

    public static CatServerConfig getConfig() {
        return config;
    }

    public static void postPrimaryThread(Runnable runnable) {
        MinecraftServer.getServerInst().addScheduledTask(runnable);
    }
}
