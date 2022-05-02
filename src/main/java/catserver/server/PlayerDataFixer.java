package catserver.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.util.NumberConversions;

public class PlayerDataFixer {
    public static void checkVector(Entity entity) {
        Vector3d vector = entity.getDeltaMovement();
        if (!NumberConversions.isFinite(vector.x) || !NumberConversions.isFinite(vector.y) || !NumberConversions.isFinite(vector.z)) {
            entity.setDeltaMovement(Vector3d.ZERO);
        }
    }

    public static void checkLocation(PlayerEntity entity) {
        Vector3d position = entity.position();
        if (!NumberConversions.isFinite(position.x) || !NumberConversions.isFinite(position.y) || !NumberConversions.isFinite(position.z)) {
            BlockPos pos = ((ServerWorld)entity.level).getSharedSpawnPos();
            entity.setPosRaw(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public static void checkHealth(PlayerEntity entity) {
        if (!NumberConversions.isFinite(entity.getHealth())) {
            entity.setHealth(0.0F);
        }
    }

}
