package catserver.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import org.bukkit.util.NumberConversions;

public class PlayerDataFixer {
    public static void checkVector(Entity entity) {
        if (!NumberConversions.isFinite(entity.motionX)) entity.motionX = 0;
        if (!NumberConversions.isFinite(entity.motionY)) entity.motionY = 0;
        if (!NumberConversions.isFinite(entity.motionZ)) entity.motionZ = 0;
    }

    public static void checkLocation(EntityPlayer entity) {
        if (!NumberConversions.isFinite(entity.posX) || !NumberConversions.isFinite(entity.posY) || !NumberConversions.isFinite(entity.posZ)) {
            BlockPos pos = entity.getEntityWorld().getSpawnPoint();
            entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public static void checkHealth(EntityPlayer entity) {
        if (!NumberConversions.isFinite(entity.getHealth())) {
            entity.setHealth(0.0F);
        }
    }

}
