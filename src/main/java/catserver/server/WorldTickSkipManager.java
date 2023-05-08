package catserver.server;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class WorldTickSkipManager {
    public static boolean shouldSkipEntity = false;
    public static boolean shouldSkipTileEntity = false;
    public static boolean shouldSkipWorldEntityTick = false;

    public static void update(WorldServer world) {
        shouldSkipEntity = catserver.server.CatServer.getConfig().enableSkipEntityTick && MinecraftServer.currentTick % 10 > 0;
        shouldSkipTileEntity = catserver.server.CatServer.getConfig().enableSkipTileEntityTick && MinecraftServer.currentTick % 10 > 0;
        shouldSkipWorldEntityTick = catserver.server.CatServer.getConfig().enableSkipEntityTick && MinecraftServer.currentTick % 20 > 0;

        if (MinecraftServer.currentTick % 50 == 0) {
            PlayerChunkMap playerChunkMap = world.getPlayerChunkMap();

            if (CatServer.getConfig().enableSkipEntityTick) {
                for (Entity entity : world.loadedEntityList) {
                    BlockPos pos = entity.getPosition();
                    entity.skipTick = !playerChunkMap.contains(pos.getX() >> 4, pos.getZ() >> 4);
                }
            }

            if (CatServer.getConfig().enableSkipTileEntityTick) {
                for (TileEntity tileEntity : world.tickableTileEntities) {
                    BlockPos pos = tileEntity.getPos();
                    tileEntity.skipTick = !playerChunkMap.contains(pos.getX() >> 4, pos.getZ() >> 4);
                }
            }
        }
    }
}
