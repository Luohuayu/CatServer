package catserver.server.threads;

import catserver.server.async.AsyncChunkGenerator;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.world.WorldServer;

public class AsyncChunkGeneratorThread {
    private final ThreadPoolExecutor pool;

    public AsyncChunkGeneratorThread(WorldServer world) {
        pool = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Async Chunk Generator Thread - " + world.getWorld().getName() + "#%d").build());
    }

    public void shutdown() {
        try {
            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
            AsyncChunkGenerator.tick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submit(Runnable runnable) {
        pool.submit(runnable);
    }
}
