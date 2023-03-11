package org.bukkit.craftbukkit.v1_18_R2.block;

import net.minecraft.world.level.block.entity.BellBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Bell;

public class CraftBell extends CraftBlockEntityState<BellBlockEntity> implements Bell {

    public CraftBell(World world, BellBlockEntity tileEntity) {
        super(world, tileEntity);
    }
}
