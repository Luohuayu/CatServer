// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import org.bukkit.inventory.FurnaceInventory;

public class CraftInventoryFurnace extends CraftInventory implements FurnaceInventory
{
    public CraftInventoryFurnace(final TileEntityFurnace inventory) {
        super(inventory);
    }
    
    @Override
    public ItemStack getResult() {
        return this.getItem(2);
    }
    
    @Override
    public ItemStack getFuel() {
        return this.getItem(1);
    }
    
    @Override
    public ItemStack getSmelting() {
        return this.getItem(0);
    }
    
    @Override
    public void setFuel(final ItemStack stack) {
        this.setItem(1, stack);
    }
    
    @Override
    public void setResult(final ItemStack stack) {
        this.setItem(2, stack);
    }
    
    @Override
    public void setSmelting(final ItemStack stack) {
        this.setItem(0, stack);
    }
    
    @Override
    public Furnace getHolder() {
        return (Furnace)this.inventory.getOwner();
    }
}
