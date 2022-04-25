package catserver.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class WeakWorldSaveManager {
    private static final Queue<WorldServer> saveTaskQueue = new catserver.server.utils.CachedSizeConcurrentLinkedQueue<>();
    private static long lastSaveTick = MinecraftServer.currentTick;

    public static void saveAllWorlds() {
        List<WorldServer> alreadySavedLagWorlds = null;

        if (saveTaskQueue.size() > 0) {
            MinecraftServer.LOGGER.warn("[WeakWorldSaveManager] World auto save lag! Remaining count: " + saveTaskQueue.size());
            alreadySavedLagWorlds = new ArrayList<>();
            WorldServer worldServer;
            while ((worldServer = saveTaskQueue.poll()) != null) {
                if (MinecraftServer.getServerInst().worldServerList.contains(worldServer) /* Is unloaded? */) {
                    MinecraftServer.LOGGER.warn("[WeakWorldSaveManager] Saving dimension: " + worldServer.dimension);
                    try {
                        worldServer.saveAllChunks(true, null);
                    } catch (MinecraftException minecraftexception) {
                        MinecraftServer.LOGGER.warn(minecraftexception.getMessage());
                    }
                    saveTaskQueue.remove(worldServer);
                    alreadySavedLagWorlds.add(worldServer);
                }
            }
        }

        for (WorldServer worldServer : MinecraftServer.getServerInst().worldServerList) {
            if (worldServer != null) {
                if (alreadySavedLagWorlds != null && alreadySavedLagWorlds.contains(worldServer)) continue;
                saveTaskQueue.add(worldServer);
            }
        }
    }

    public static void onTick() {
        long startTime = System.nanoTime();
        while (saveTaskQueue.size() > 0) {
            WorldServer worldServer = saveTaskQueue.poll();
            if (worldServer != null && MinecraftServer.getServerInst().worldServerList.contains(worldServer) /* Is unloaded? */) {
                try {
                    worldServer.saveAllChunks(true, null);
                } catch (MinecraftException minecraftexception) {
                    MinecraftServer.LOGGER.warn(minecraftexception.getMessage());
                }
                long estimatedTime = System.nanoTime() - startTime;
                if (estimatedTime > 50000000L /* 50ms */) {
                    break;
                }
            }
        }
        lastSaveTick = MinecraftServer.currentTick;
    }

    public static boolean isNeedTick() {
        return saveTaskQueue.size() > 0 && MinecraftServer.currentTick - lastSaveTick > 1 /* Idle one tick for working on other things */;
    }
}
