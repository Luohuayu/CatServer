package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.BlastFurnaceTileEntity;
import org.bukkit.Material;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Block;

public class CraftBlastFurnace extends CraftFurnace implements BlastFurnace {

    public CraftBlastFurnace(Block block) {
        super(block, BlastFurnaceTileEntity.class);
    }

    public CraftBlastFurnace(Material material, BlastFurnaceTileEntity te) {
        super(material, te);
    }
}
