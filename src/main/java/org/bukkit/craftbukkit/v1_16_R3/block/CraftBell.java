package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.BellTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;

public class CraftBell extends CraftBlockEntityState<BellTileEntity> implements Bell {

    public CraftBell(Block block) {
        super(block, BellTileEntity.class);
    }

    public CraftBell(Material material, BellTileEntity te) {
        super(material, te);
    }
}
