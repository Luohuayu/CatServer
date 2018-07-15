// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.Location;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.world.ILockableContainer;

import org.bukkit.inventory.DoubleChestInventory;

public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory
{
    private final CraftInventory left;
    private final CraftInventory right;
    
    public CraftInventoryDoubleChest(final CraftInventory left, final CraftInventory right) {
        super(new InventoryLargeChest("Large chest", (ILockableContainer)left.getInventory(), (ILockableContainer)right.getInventory()));
        this.left = left;
        this.right = right;
    }
    
    public CraftInventoryDoubleChest(final InventoryLargeChest largeChest) {
        super(largeChest);
        if (largeChest.upperChest instanceof InventoryLargeChest) {
            this.left = new CraftInventoryDoubleChest((InventoryLargeChest)largeChest.upperChest);
        }
        else {
            this.left = new CraftInventory((IInventory) largeChest.upperChest);
        }
        if (largeChest.lowerChest instanceof InventoryLargeChest) {
            this.right = new CraftInventoryDoubleChest((InventoryLargeChest)largeChest.lowerChest);
        }
        else {
            this.right = new CraftInventory((IInventory) largeChest.lowerChest);
        }
    }
    
    @Override
    public Inventory getLeftSide() {
        return this.left;
    }
    
    @Override
    public Inventory getRightSide() {
        return this.right;
    }
    
    @Override
    public void setContents(final ItemStack[] items) {
        if (this.getInventory().getContents().length < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getInventory().getContents().length + " or less");
        }
        final ItemStack[] leftItems = new ItemStack[this.left.getSize()];
        final ItemStack[] rightItems = new ItemStack[this.right.getSize()];
        System.arraycopy(items, 0, leftItems, 0, Math.min(this.left.getSize(), items.length));
        this.left.setContents(leftItems);
        if (items.length >= this.left.getSize()) {
            System.arraycopy(items, this.left.getSize(), rightItems, 0, Math.min(this.right.getSize(), items.length - this.left.getSize()));
            this.right.setContents(rightItems);
        }
    }
    
    @Override
    public DoubleChest getHolder() {
        return new DoubleChest(this);
    }
    
    @Override
    public Location getLocation() {
        return this.getLeftSide().getLocation().add(this.getRightSide().getLocation()).multiply(0.5);
    }
}
