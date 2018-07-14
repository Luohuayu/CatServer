// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import org.bukkit.inventory.EnchantingInventory;

public class CraftInventoryEnchanting extends CraftInventory implements EnchantingInventory
{
    public CraftInventoryEnchanting(final InventoryBasic inventory) {
        super(inventory);
    }
    
    @Override
    public void setItem(final ItemStack item) {
        this.setItem(0, item);
    }
    
    @Override
    public ItemStack getItem() {
        return this.getItem(0);
    }
    
    @Override
    public InventoryBasic getInventory() {
        return (InventoryBasic)this.inventory;
    }
    
    @Override
    public void setSecondary(final ItemStack item) {
        this.setItem(1, item);
    }
    
    @Override
    public ItemStack getSecondary() {
        return this.getItem(1);
    }
}
