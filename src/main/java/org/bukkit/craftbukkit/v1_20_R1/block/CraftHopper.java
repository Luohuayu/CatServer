package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper extends CraftLootable<HopperBlockEntity> implements Hopper {

    public CraftHopper(World world, HopperBlockEntity tileEntity) {
        super(world, tileEntity);
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
