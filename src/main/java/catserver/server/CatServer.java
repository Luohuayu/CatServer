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
            log.info("A Mod/Plugin try to async " + reason + ", it will be executed safely on the main server thread until return!");
            log.info("Please check the stack in debug.log and report the author.");
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

    public static int getCurrentTick() {
        return getConfig().enableRealtime ? RealtimeThread.currentTick : MinecraftServer.currentTick;
    }

    public static void forceSaveWorlds() {
        log.info("Force save worlds:");
        boolean oldAsyncCatcher = AsyncCatcher.enabled;
        AsyncCatcher.enabled = false;

        try {
            log.info("Force saving players..");
            MinecraftServer.getServerInst().getPlayerList().saveAllPlayerData();

            log.info("Force saving chunks..");
            for (WorldServer worldServer : MinecraftServer.getServerInst().worldServerList) {
                try {
                    worldServer.saveAllChunks(true, null);
                    worldServer.flushToDisk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AsyncCatcher.enabled = oldAsyncCatcher;
        log.info("Force save complete!");
    }

    public static void scheduleAsyncTask(Runnable runnable) {
        if (!asyncExecutor.isShutdown() && !asyncExecutor.isTerminated()) {
            asyncExecutor.execute(runnable);
        } else {
            runnable.run();
        }
    }

    public static void acceptEula() {
        Properties properties = new Properties();
        try (FileInputStream fileinputstream = new FileInputStream("eula.txt")) {
            properties.load(fileinputstream);
        } catch (Exception e) {
            log.warn(e.toString());
        }
        if (!"true".equals(properties.getProperty("eula"))) {
            try (FileOutputStream fileoutputstream = new FileOutputStream("eula.txt")) {
                properties.setProperty("eula", "true");
                properties.store(fileoutputstream, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
            } catch (Exception e) {
                log.warn(e.toString());
            }
        }
    }
}
