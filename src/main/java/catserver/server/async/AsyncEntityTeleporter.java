package catserver.server.async;

import java.util.concurrent.CompletableFuture;
import net.minecraft.util.math.MathHelper;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class AsyncEntityTeleporter {
    public static CompletableFuture<Boolean> teleport(Entity entity, Location loc, PlayerTeleportEvent.TeleportCause cause) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        try {
            if (net.minecraftforge.common.ForgeChunkManager.asyncChunkLoading) {
                int chunkX = MathHelper.floor(loc.getX()) >> 4;
                int chunkZ = MathHelper.floor(loc.getZ()) >> 4;
                if (entity instanceof Player) {
                    ((CraftWorld)loc.getWorld()).getHandle().getPlayerChunkMap().prepareForAsync(chunkX, chunkZ);
                    future.complete(entity.teleport(loc, cause));
                } else {
                    ((CraftWorld)loc.getWorld()).getHandle().getChunkProvider().loadChunk(chunkX, chunkZ, () -> {
                        future.complete(entity.teleport(loc, cause));
                    });
                }
            } else {
                future.complete(entity.teleport(loc, cause));
            }
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }
}
