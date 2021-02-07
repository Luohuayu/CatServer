package catserver.server;

import catserver.server.threads.RealtimeThread;
import catserver.server.utils.VersionCheck;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class CatServer {
    public static final Logger log = LogManager.getLogger("CatServer");
    private static final String version = "2.1.0";
    private static final String native_version = "v1_12_R1";

    private static CatServerConfig config = new CatServerConfig("catserver.yml");

    private static final ExecutorService asyncExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("CatServer Async Task Handler Thread - %1$d").build());
    private static final RealtimeThread realtimeThread = new RealtimeThread();

    public static String getVersion(){
        return version;
    }

    public static String getNativeVersion() {
        return native_version;
    }

    public static void onServerStart() {
        realtimeThread.start();
        new VersionCheck();
    }

    public static void onServerStop() {
        try {
            asyncExecutor.shutdown();
            asyncExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CatServerConfig getConfig() {
        return config;
    }

    public static boolean asyncCatch(String reason) {
        return AsyncCatcher.checkAsync(reason);
    }

    public static void postPrimaryThread(Runnable runnable) {
        MinecraftServer.getServerInst().processQueue.add(runnable);
    }

    public static void scheduleAsyncTask(Runnable runnable) {
        if (!asyncExecutor.isShutdown() && !asyncExecutor.isTerminated()) {
            asyncExecutor.execute(runnable);
        } else {
            runnable.run();
        }
    }

    public static int getCurrentTick() {
        return getConfig().enableRealtime ? RealtimeThread.currentTick : MinecraftServer.currentTick;
    }
}
