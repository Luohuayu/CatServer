// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityBeacon;
import org.bukkit.inventory.BeaconInventory;

public class CraftInventoryBeacon extends CraftInventory implements BeaconInventory
{
    public CraftInventoryBeacon(final TileEntityBeacon beacon) {
        super(beacon);
    }
    
    @Override
    public void setItem(final ItemStack item) {
        this.setItem(0, item);
    }
    
    @Override
    public ItemStack getItem() {
        return this.getItem(0);
    }
}
