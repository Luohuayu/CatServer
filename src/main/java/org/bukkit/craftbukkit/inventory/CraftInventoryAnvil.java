// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.ItemStack;
import net.minecraft.inventory.IInventory;

import org.bukkit.Location;
import org.bukkit.inventory.AnvilInventory;

public class CraftInventoryAnvil extends CraftInventory implements AnvilInventory
{
    private final Location location;
    private final IInventory resultInventory;
    
    public CraftInventoryAnvil(final Location location, final IInventory inventory, final IInventory resultInventory) {
        super(inventory);
        this.location = location;
        this.resultInventory = resultInventory;
    }
    
    public IInventory getResultInventory() {
        return this.resultInventory;
    }
    
    public IInventory getIngredientsInventory() {
        return this.inventory;
    }
    
    @Override
    public ItemStack getItem(final int slot) {
        if (slot < this.getIngredientsInventory().getSizeInventory()) {
            final net.minecraft.item.ItemStack item = this.getIngredientsInventory().getStackInSlot(slot);
            return (item == null) ? null : CraftItemStack.asCraftMirror(item);
        }
        final net.minecraft.item.ItemStack item = this.getResultInventory().getStackInSlot(slot - this.getIngredientsInventory().getSizeInventory());
        return (item == null) ? null : CraftItemStack.asCraftMirror(item);
    }
    
    @Override
    public void setItem(final int index, final ItemStack item) {
        if (index < this.getIngredientsInventory().getSizeInventory()) {
            this.getIngredientsInventory().setInventorySlotContents(index, (item == null) ? null : CraftItemStack.asNMSCopy(item));
        }
        else {
            this.getResultInventory().setInventorySlotContents(index - this.getIngredientsInventory().getSizeInventory(), (item == null) ? null : CraftItemStack.asNMSCopy(item));
        }
    }
    
    @Override
    public int getSize() {
        return this.getResultInventory().getSizeInventory() + this.getIngredientsInventory().getSizeInventory();
    }
    
    @Override
    public Location getLocation() {
        return this.location;
    }
}
