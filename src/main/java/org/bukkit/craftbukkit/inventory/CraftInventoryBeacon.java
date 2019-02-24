package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityBeacon;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBeacon extends CraftInventory implements BeaconInventory {
    public CraftInventoryBeacon(TileEntityBeacon beacon) {
        super(beacon);
    }
    // CatServer start
    public CraftInventoryBeacon(IInventory beacon) {
        super(beacon);
    }
    // CatServer end
    public void setItem(ItemStack item) {
        setItem(0, item);
    }

    public ItemStack getItem() {
        return getItem(0);
    }
}
