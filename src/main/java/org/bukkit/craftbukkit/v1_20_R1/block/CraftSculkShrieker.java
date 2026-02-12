package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import org.bukkit.World;
import org.bukkit.block.SculkShrieker;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CraftSculkShrieker extends CraftBlockEntityState<SculkShriekerBlockEntity> implements SculkShrieker {

    public CraftSculkShrieker(World world, SculkShriekerBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public int getWarningLevel() {
        return getSnapshot().warningLevel;
    }

    @Override
    public void setWarningLevel(int level) {
        getSnapshot().warningLevel = level;
    }

    @Override
    public void tryShriek(Player player) {
        requirePlaced();

        ServerPlayer entityPlayer = (player == null) ? null : ((CraftPlayer) player).getHandle();
        getTileEntity().tryShriek(world.getHandle(), entityPlayer);
    }
}
