package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.bukkit.World;
import org.bukkit.block.SuspiciousSand;

public class CraftSuspiciousSand extends CraftBrushableBlock implements SuspiciousSand {

    public CraftSuspiciousSand(World world, BrushableBlockEntity tileEntity) {
        super(world, tileEntity);
    }
}
