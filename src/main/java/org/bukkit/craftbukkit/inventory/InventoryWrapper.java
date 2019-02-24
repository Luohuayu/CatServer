package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryWrapper implements IInventory {

    private final Inventory inventory;
    private final List<HumanEntity> viewers = new ArrayList<HumanEntity>();

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSize();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return CraftItemStack.asNMSCopy(inventory.getItem(i));
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        // Copied from CraftItemStack
        ItemStack stack = getStackInSlot(i);
        ItemStack result;
        if (stack.isEmpty()) {
            return stack;
        }
        if (stack.getCount() <= j) {
            this.setInventorySlotContents(i, ItemStack.EMPTY);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, j);
            stack.shrink(j);
        }
        this.markDirty();
        return result;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        // Copied from CraftItemStack
        ItemStack stack = getStackInSlot(i);
        ItemStack result;
        if (stack.isEmpty()) {
            return stack;
        }
        if (stack.getCount() <= 1) {
            this.setInventorySlotContents(i, ItemStack.EMPTY);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, 1);
            stack.shrink(1);
        }
        return result;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        inventory.setItem(i, CraftItemStack.asBukkitCopy(itemstack));
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityhuman) {
    }

    @Override
    public void closeInventory(EntityPlayer entityhuman) {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int j) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public List<ItemStack> getContents() {
        int size = getSizeInventory();
        List<ItemStack> items = new ArrayList<ItemStack>(size);

        for (int i = 0; i < size; i++) {
            items.set(i, getStackInSlot(i));
        }

        return items;
    }

    @Override
    public InventoryHolder getOwner() {
        return inventory.getHolder();
    }

    @Override
    public void setMaxStackSize(int size) {
        inventory.setMaxStackSize(size);
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return getName() != null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return CraftChatMessage.fromString(getName())[0];
    }

    @Override
    public Location getLocation() {
        return inventory.getLocation();
    }

    @Override
    public boolean isEmpty() {
        return Iterables.any(inventory, Predicates.notNull());
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        viewers.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        viewers.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return viewers;
    }
}
