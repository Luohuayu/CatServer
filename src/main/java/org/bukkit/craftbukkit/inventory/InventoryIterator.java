// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ListIterator;

public class InventoryIterator implements ListIterator<ItemStack>
{
    private final Inventory inventory;
    private int nextIndex;
    private Boolean lastDirection;
    
    InventoryIterator(final Inventory craftInventory) {
        this.inventory = craftInventory;
        this.nextIndex = 0;
    }
    
    InventoryIterator(final Inventory craftInventory, final int index) {
        this.inventory = craftInventory;
        this.nextIndex = index;
    }
    
    @Override
    public boolean hasNext() {
        return this.nextIndex < this.inventory.getSize();
    }
    
    @Override
    public ItemStack next() {
        this.lastDirection = true;
        return this.inventory.getItem(this.nextIndex++);
    }
    
    @Override
    public int nextIndex() {
        return this.nextIndex;
    }
    
    @Override
    public boolean hasPrevious() {
        return this.nextIndex > 0;
    }
    
    @Override
    public ItemStack previous() {
        this.lastDirection = false;
        final Inventory inventory = this.inventory;
        final int nextIndex = this.nextIndex - 1;
        this.nextIndex = nextIndex;
        return inventory.getItem(nextIndex);
    }
    
    @Override
    public int previousIndex() {
        return this.nextIndex - 1;
    }
    
    @Override
    public void set(final ItemStack item) {
        if (this.lastDirection == null) {
            throw new IllegalStateException("No current item!");
        }
        final int i = this.lastDirection ? (this.nextIndex - 1) : this.nextIndex;
        this.inventory.setItem(i, item);
    }
    
    @Override
    public void add(final ItemStack item) {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }
}
