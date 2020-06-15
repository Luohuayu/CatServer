package catserver.server;

import catserver.server.threads.RealtimeThread;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spigotmc.AsyncCatcher;

public class CatServer {
    public static final Logger log = LogManager.getLogger("CatServer");
    private static final String version = "2.1.0";
    private static final String native_version = "v1_12_R1";

    private static CatServerConfig config = new CatServerConfig("catserver.yml");

    private static final RealtimeThread realtimeThread = new RealtimeThread();

    public static String getVersion(){
        return version;
    }

    public static String getNativeVersion() {
        return native_version;
    }

    public static void onServerStart() {
        realtimeThread.start();
    }

    public static boolean asyncCatch(String reason) {
        if (AsyncCatcher.enabled && Thread.currentThread() != MinecraftServer.getServerInst().primaryThread) {
            log.debug("Try to asynchronously " + reason + ", caught!", new RuntimeException());
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

    public static CatServerConfig getConfig() {
        return config;
    }

    public static void postPrimaryThread(Runnable runnable) {
        MinecraftServer.getServerInst().addScheduledTask(runnable);
    }

    public static int getCurrentTick() {
        return getConfig().enableRealtime ? RealtimeThread.currentTick : MinecraftServer.currentTick;
    }
}
