package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.FurnaceTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CraftFurnaceFurnace extends CraftFurnace {

    public CraftFurnaceFurnace(Block block) {
        super(block, FurnaceTileEntity.class);
    }

    public CraftFurnaceFurnace(Material material, FurnaceTileEntity te) {
        super(material, te);
    }
}
