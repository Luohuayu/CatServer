// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory
{
    public CraftInventoryBrewer(final IInventory inventory) {
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
