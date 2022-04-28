package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.ConduitTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Conduit;

public class CraftConduit extends CraftBlockEntityState<ConduitTileEntity> implements Conduit {

    public CraftConduit(Block block) {
        super(block, ConduitTileEntity.class);
    }

    public CraftConduit(Material material, ConduitTileEntity te) {
        super(material, te);
    }
}
