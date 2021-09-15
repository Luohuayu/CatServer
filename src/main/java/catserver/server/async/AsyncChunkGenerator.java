package catserver.server.async;

import catserver.server.CatServer;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

public class AsyncChunkGenerator {
    private static final Map<QueuedChunk, ChunkGeneratorTask> tasks = Maps.newConcurrentMap();

    public static Chunk syncChunkGenerate(WorldServer world, ChunkProviderServer chunkProviderServer, int chunkX, int chunkZ) {
        ChunkGeneratorTask task = tasks.remove(new QueuedChunk(chunkX, chunkZ, world));
        if (task != null) {
            synchronized(task) {
                while (!task.isAsyncFinished()) {
                    try {
                        task.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            task.syncChunkGenerate();
            return task.getChunk();
        } else {
            return null;
        }
    }

    public static void queueChunkGenerate(WorldServer world, ChunkProviderServer chunkProviderServer, int chunkX, int chunkZ, Runnable callback) {
        QueuedChunk key = new QueuedChunk(chunkX, chunkZ, world);
        ChunkGeneratorTask task = tasks.get(key);
        if (task == null) {
            task = new ChunkGeneratorTask(chunkX, chunkZ, chunkProviderServer);
            task.addCallback(callback);
            tasks.put(key, task);
            world.asyncChunkGeneratorThread.submit(task);
        } else {
            task.addCallback(callback);
        }
    }

    public static void tick() {
        org.spigotmc.SlackActivityAccountant activityAccountant = net.minecraft.server.MinecraftServer.getServerInst().slackActivityAccountant;
        activityAccountant.startActivity(Math.min(catserver.server.CatServer.getConfig().worldGenMaxTickTime, 18.75) / (18.75 * 2)); // CatServer - 50 * 0.375 = 18.75

        int limitCount = 49;

        Iterator<ChunkGeneratorTask> itr = tasks.values().iterator();
        while (itr.hasNext()) {
            ChunkGeneratorTask task = itr.next();
            if (task.isAsyncFinished()) {
                task.syncChunkGenerate();
                itr.remove();
            }

            if (limitCount-- < 0 || activityAccountant.activityTimeIsExhausted()) break;
        }

        activityAccountant.endActivity();
    }

    private static class ChunkGeneratorTask implements Runnable {
        private final long chunkKey;
        private final int chunkX;
        private final int chunkZ;
        private final ChunkProviderServer chunkProviderServer;
        private final ConcurrentLinkedQueue<Runnable> callbacks = new ConcurrentLinkedQueue<Runnable>();
        private Chunk chunk;
        private ReportedException exception;
        private boolean asyncFinished;

        private ChunkGeneratorTask(int chunkX, int chunkZ, ChunkProviderServer chunkProviderServer) {
            this.chunkKey = ChunkPos.asLong(chunkX, chunkZ);
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.chunkProviderServer = chunkProviderServer;
        }

        public void addCallback(Runnable callback) {
            callbacks.add(callback);
        }

        public void asyncChunkGenerate() {
            try {
                synchronized (chunkProviderServer.chunkGenerator) {
                    chunk = chunkProviderServer.chunkGenerator.generateChunk(chunkX, chunkZ);
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk (CatServer-Async | Please report to https://github.com/Luohuayu/CatServer)");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                crashreportcategory.addCrashSection("Location", String.format("%d,%d", chunkX, chunkZ));
                crashreportcategory.addCrashSection("Position hash", Long.valueOf(chunkKey));
                crashreportcategory.addCrashSection("Generator", chunkProviderServer.chunkGenerator);
                exception = new ReportedException(crashreport);
            } finally {
                asyncFinished = true;
                synchronized (this) {
                    notifyAll();
                }
            }
        }

        public void syncChunkGenerate() {
            if (exception == null) {
                chunk.world.timings.syncChunkLoadTimer.startTiming();
                chunkProviderServer.id2ChunkMap.put(chunkKey, chunk);
                chunk.onLoad();
                chunk.populateCB(chunkProviderServer, chunkProviderServer.chunkGenerator, true);
                chunk.world.timings.syncChunkLoadTimer.stopTiming();

                for (Runnable runnable : callbacks) {
                    runnable.run();
                }
                callbacks.clear();
            } else {
                throw exception;
            }
        }

        public boolean isAsyncFinished() {
            return asyncFinished;
        }

        public Chunk getChunk() {
            return chunk;
        }

        @Override
        public void run() {
            asyncChunkGenerate();
        }
    }

    static class QueuedChunk {
        final int x;
        final int z;
        final World world;

        public QueuedChunk(int x, int z, World world) {
            this.x = x;
            this.z = z;
            this.world = world;
        }

        @Override
        public int hashCode() {
            return (x * 31 + z * 29) ^ world.hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof QueuedChunk) {
                QueuedChunk other = (QueuedChunk) object;
                return x == other.x && z == other.z && world == other.world;
            }

            return false;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            String NEW_LINE = System.getProperty("line.separator");

            result.append(this.getClass().getName() + " {" + NEW_LINE);
            result.append(" x: " + x + NEW_LINE);
            result.append(" z: " + z + NEW_LINE);
            result.append(" world: " + world.getWorldInfo().getWorldName() + NEW_LINE);
            result.append(" dimension: " + world.provider.getDimension() + NEW_LINE);
            result.append(" provider: " + world.provider.getClass().getName() + NEW_LINE);
            result.append("}");

            return result.toString();
        }
    }
}
