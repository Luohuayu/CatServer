package catserver.server;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CatServer {
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
        if (Thread.currentThread() != MinecraftServer.getServerInst().primaryThread) {
            FMLLog.getLogger().debug("Try to asynchronously " + reason + ", caught!", new RuntimeException());
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
                if (Integer.valueOf(forgeVersion.split("\\.")[3]) < 2826) {
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
