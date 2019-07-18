package catserver.server;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Timer;
import java.util.TimerTask;

public class CatServer {
	private static final String version = "2.0.0";
	private static final String native_verson = "v1_12_R1";

	public static String getVersion(){
		return version;
	}

    public static String getNativeVersion() {
        return native_verson;
    }

    public static boolean isDev() {
        return System.getProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp") != null;
    }

    public static boolean asyncCatch(String reason) {
        if (Thread.currentThread() != MinecraftServer.getServerInst().primaryThread) {
            FMLLog.getLogger().debug("Try to asynchronously " + reason + ", caught!", new RuntimeException());
            return true;
        }
        return false;
    }

    public static void watchdogForceExitTask() {
        MinecraftServer.getServerInst().primaryThread.suspend();
        new Timer("WatchdogForceExitTask").schedule(new TimerTask() {
            public void run() {
                Runtime.getRuntime().exit(0);
            }
        }, 300 * 1000);
    }
}
