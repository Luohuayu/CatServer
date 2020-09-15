package catserver.server;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class WorldTickSkipManager {
    public static void update(WorldServer world) {
        if (MinecraftServer.currentTick % 50 == 0) {
            if (CatServer.getConfig().enableSkipEntityTick) {
                for (Entity entity : world.loadedEntityList) {
                    BlockPos pos = entity.getPosition();
                    entity.skipTick = !world.getPlayerChunkMap().contains(pos.getX() >> 4, pos.getZ() >> 4);
                }
            }

            if (CatServer.getConfig().enableSkipTileEntityTick) {
                for (TileEntity tileEntity : world.tickableTileEntities) {
                    BlockPos pos = tileEntity.getPos();
                    tileEntity.skipTick = !world.getPlayerChunkMap().contains(pos.getX() >> 4, pos.getZ() >> 4);
                }
            }
        }
    }
}
