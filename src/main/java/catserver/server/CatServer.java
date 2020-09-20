package catserver.server;

import catserver.server.threads.RealtimeThread;
import catserver.server.utils.VersionCheck;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.util.Waitable;
import org.spigotmc.AsyncCatcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.function.Supplier;

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
        new Metrics();
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
        if (AsyncCatcher.enabled && Thread.currentThread() != MinecraftServer.getServerInst().primaryThread) {
            if (!getConfig().disableAsyncCatchWarn) {
                log.warn("A Mod/Plugin try to async " + reason + ", it will be executed safely on the main server thread until return!");
                log.warn("Please check the stacktrace in debug.log and report the author.");
            }
            log.debug("Try to async " + reason, new RuntimeException());
            return true;
        }
        return false;
    }

    public static void postPrimaryThread(Runnable runnable) {
        postPrimaryThread(() -> { runnable.run(); return null; });
    }

    public static <T> T postPrimaryThread(Supplier<T> runnable) {
        Waitable<T> wait = new Waitable<T>() {
            @Override
            protected T evaluate() {
                return runnable.get();
            }
        };
        MinecraftServer.getServerInst().processQueue.add(wait);
        try {
            return wait.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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
