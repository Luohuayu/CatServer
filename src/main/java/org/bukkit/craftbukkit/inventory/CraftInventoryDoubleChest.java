package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.world.ILockableContainer;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;

public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory {
    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(CraftInventory left, CraftInventory right) {
        super(new InventoryLargeChest("Large chest", (ILockableContainer) left.getInventory(), (ILockableContainer) right.getInventory()));
        this.left = left;
        this.right = right;
    }

    public CraftInventoryDoubleChest(InventoryLargeChest largeChest) {
        super(largeChest);
        if (largeChest.upperChest instanceof InventoryLargeChest) {
            left = new CraftInventoryDoubleChest((InventoryLargeChest) largeChest.upperChest);
        } else {
            left = new CraftInventory(largeChest.upperChest);
        }
        if (largeChest.lowerChest instanceof InventoryLargeChest) {
            right = new CraftInventoryDoubleChest((InventoryLargeChest) largeChest.lowerChest);
        } else {
            right = new CraftInventory(largeChest.lowerChest);
        }
    }

    public Inventory getLeftSide() {
        return left;
    }

    public Inventory getRightSide() {
        return right;
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getInventory().getSizeInventory() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getInventory().getSizeInventory() + " or less");
        }
        ItemStack[] leftItems = new ItemStack[left.getSize()], rightItems = new ItemStack[right.getSize()];
        System.arraycopy(items, 0, leftItems, 0, Math.min(left.getSize(), items.length));
        left.setContents(leftItems);
        if (items.length >= left.getSize()) {
            System.arraycopy(items, left.getSize(), rightItems, 0, Math.min(right.getSize(), items.length - left.getSize()));
            right.setContents(rightItems);
        }
    }

    @Override
    public DoubleChest getHolder() {
        return new DoubleChest(this);
    }

    @Override
    public Location getLocation() {
        return getLeftSide().getLocation().add(getRightSide().getLocation()).multiply(0.5);
    }
}
