package catserver.server.utils;

import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

public class NMSUtils {
    public static WorldServer toNMS(org.bukkit.World world) {
        return ((CraftWorld) world).getHandle();
    }

    public static EntityPlayerMP toNMS(org.bukkit.entity.Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static Entity toNMS(org.bukkit.entity.Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }

    public static int getIntPosOffset(BlockPos pos) {
        final int xOffset = pos.getX() % 16;
        final int y = pos.getY();
        final int zOffset = pos.getZ() % 16;
        return (xOffset << 16) + (y << 8) + zOffset;
    }
}
