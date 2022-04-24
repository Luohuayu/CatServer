package catserver.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class WeakWorldSaveManager {
    private static final Queue<WorldServer> saveTaskQueue = new catserver.server.utils.CachedSizeConcurrentLinkedQueue<>();

    public static void saveAllWorlds() {
        List<WorldServer> alreadySavedLagWorld = null;

        if (saveTaskQueue.size() > 0) {
            MinecraftServer.LOGGER.warn("[WeakWorldSaveManager] World save lag! Remaining count: " + saveTaskQueue.size());
            alreadySavedLagWorld = new ArrayList<>();
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
                    alreadySavedLagWorld.add(worldServer);
                }
            }
        }

        for (WorldServer worldServer : MinecraftServer.getServerInst().worldServerList) {
            if (worldServer != null) {
                if (alreadySavedLagWorld != null && alreadySavedLagWorld.contains(worldServer)) continue;
                saveTaskQueue.add(worldServer);
            }
        }
    }

    public static void onTick() {
        WorldServer worldServer = saveTaskQueue.poll();
        for(;;) {
            if (worldServer != null && MinecraftServer.getServerInst().worldServerList.contains(worldServer) /* Is unloaded? */) {
                try {
                    worldServer.saveAllChunks(true, null);
                } catch (MinecraftException minecraftexception) {
                    MinecraftServer.LOGGER.warn(minecraftexception.getMessage());
                }
                break;
            }
            if (saveTaskQueue.size() > 0) {
                worldServer = saveTaskQueue.poll();
            }
        }
    }

    public static boolean isNeedTick() {
        return saveTaskQueue.size() > 0;
    }
}
