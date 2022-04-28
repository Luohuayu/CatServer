package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.ComparatorTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<ComparatorTileEntity> implements Comparator {

    public CraftComparator(final Block block) {
        super(block, ComparatorTileEntity.class);
    }

    public CraftComparator(final Material material, final ComparatorTileEntity te) {
        super(material, te);
    }
}
