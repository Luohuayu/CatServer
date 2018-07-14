// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.ItemStack;

import luohuayu.CatServer.inventory.ICBInventory;
import net.minecraft.inventory.IInventory;

import org.bukkit.inventory.BrewerInventory;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory
{
    public CraftInventoryBrewer(final ICBInventory inventory) {
        super(inventory);
    }
    
    @Override
    public ItemStack getIngredient() {
        return this.getItem(3);
    }
    
    @Override
    public void setIngredient(final ItemStack ingredient) {
        this.setItem(3, ingredient);
    }
    
    @Override
    public BrewingStand getHolder() {
        return (BrewingStand)this.inventory.getOwner();
    }
    
    @Override
    public ItemStack getFuel() {
        return this.getItem(4);
    }
    
    @Override
    public void setFuel(final ItemStack fuel) {
        this.setItem(4, fuel);
    }
}
