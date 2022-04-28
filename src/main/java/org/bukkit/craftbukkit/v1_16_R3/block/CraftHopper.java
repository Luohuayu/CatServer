package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.HopperTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper extends CraftLootable<HopperTileEntity> implements Hopper {

    public CraftHopper(final Block block) {
        super(block, HopperTileEntity.class);
    }

    public CraftHopper(final Material material, final HopperTileEntity te) {
        super(material, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }
}
