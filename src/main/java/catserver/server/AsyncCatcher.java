package catserver.server;

import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.util.Waitable;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class AsyncCatcher {
    public static boolean isMainThread() {
        return Thread.currentThread() == MinecraftServer.getServerInst().primaryThread;
    }

    public static boolean checkAsync(String reason) {
        if (org.spigotmc.AsyncCatcher.enabled && !isMainThread()) {
            if (!CatServer.getConfig().disableAsyncCatchWarn) {
                CatServer.log.warn("A Mod/Plugin try to async " + reason + ", it will be executed safely on the main server thread until return!");
                CatServer.log.warn("Please check the stacktrace in debug.log and report the author.");
            }
            CatServer.log.debug("Try to async " + reason, new Throwable());
            return true;
        }
        return false;
    }

    public static void ensureExecuteOnPrimaryThread(Runnable runnable) {
        ensureExecuteOnPrimaryThread(() -> { runnable.run(); return null; });
    }

    public static <T> T ensureExecuteOnPrimaryThread(Supplier<T> runnable) {
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

    public static boolean checkAndPostPrimaryThread(String reason, Runnable runnable) {
        if (checkAsync(reason)) {
            MinecraftServer.getServerInst().processQueue.add(runnable);
            return true;
        }
        return false;
    }
}
