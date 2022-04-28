package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.EnderChestTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<EnderChestTileEntity> implements EnderChest {

    public CraftEnderChest(final Block block) {
        super(block, EnderChestTileEntity.class);
    }

    public CraftEnderChest(final Material material, final EnderChestTileEntity te) {
        super(material, te);
    }
}
