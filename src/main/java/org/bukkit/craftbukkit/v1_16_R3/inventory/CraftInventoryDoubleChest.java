package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.block.ChestBlock;
import net.minecraft.inventory.DoubleSidedInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import org.bukkit.Location;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory {
    public INamedContainerProvider tile;
    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(ChestBlock.DoubleInventory block) {
        super(block.inventorylargechest);
        this.tile = block;
        this.left = new CraftInventory(block.inventorylargechest.container1);
        this.right = new CraftInventory(block.inventorylargechest.container2);
    }

    public CraftInventoryDoubleChest(DoubleSidedInventory largeChest) {
        super(largeChest);
        if (largeChest.container1 instanceof DoubleSidedInventory) {
            left = new CraftInventoryDoubleChest((DoubleSidedInventory) largeChest.container1);
        } else {
            left = new CraftInventory(largeChest.container1);
        }
        if (largeChest.container2 instanceof DoubleSidedInventory) {
            right = new CraftInventoryDoubleChest((DoubleSidedInventory) largeChest.container2);
        } else {
            right = new CraftInventory(largeChest.container2);
        }
    }

    @Override
    public Inventory getLeftSide() {
        return left;
    }

    @Override
    public Inventory getRightSide() {
        return right;
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getInventory().getContainerSize() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getInventory().getContainerSize() + " or less");
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
