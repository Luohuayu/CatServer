package catserver.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
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
        Waitable<T> waitable = new Waitable<T>() {
            @Override
            protected T evaluate() {
                return runnable.get();
            }
        };
        MinecraftServer.getServerInst().processQueue.add(waitable);
        try {
            return waitable.get();
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

    public static Chunk asyncLoadChunkCaught(World world, AnvilChunkLoader loader, ChunkProviderServer provider, int x, int z) {
        if (net.minecraftforge.common.ForgeChunkManager.asyncChunkLoading) {
            Waitable<Chunk> waitable = new Waitable<Chunk>() {
                @Override
                protected Chunk evaluate() {
                    return provider.getChunkIfLoaded(x, z);
                }
            };

            net.minecraftforge.common.chunkio.ChunkIOExecutor.queueChunkLoad(world, loader, provider, x, z, waitable);

            try {
                return waitable.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            return ensureExecuteOnPrimaryThread(() -> net.minecraftforge.common.chunkio.ChunkIOExecutor.syncChunkLoad(world, loader, provider, x, z));
        }
    }
}
