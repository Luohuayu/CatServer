// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.ItemStack;

import luohuayu.CatServer.inventory.ICBInventory;
import net.minecraft.inventory.IInventory;

import org.bukkit.inventory.HorseInventory;

public class CraftInventoryHorse extends CraftInventory implements HorseInventory
{
    public CraftInventoryHorse(final ICBInventory inventory) {
        super(inventory);
    }
    
    @Override
    public ItemStack getSaddle() {
        return this.getItem(0);
    }
    
    @Override
    public ItemStack getArmor() {
        return this.getItem(1);
    }
    
    @Override
    public void setSaddle(final ItemStack stack) {
        this.setItem(0, stack);
    }
    
    @Override
    public void setArmor(final ItemStack stack) {
        this.setItem(1, stack);
    }
}
