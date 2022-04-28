package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.SmokerTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Smoker;

public class CraftSmoker extends CraftFurnace implements Smoker {

    public CraftSmoker(Block block) {
        super(block, SmokerTileEntity.class);
    }

    public CraftSmoker(Material material, SmokerTileEntity te) {
        super(material, te);
    }
}
