package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<ComparatorBlockEntity> implements Comparator {

    public CraftComparator(World world, ComparatorBlockEntity tileEntity) {
        super(world, tileEntity);
    }
}
