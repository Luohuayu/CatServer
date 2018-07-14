// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import net.minecraft.util.text.ITextComponent;

import org.bukkit.inventory.InventoryHolder;

import luohuayu.CatServer.inventory.ICBInventory;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import org.bukkit.entity.HumanEntity;
import java.util.List;
import org.bukkit.inventory.Inventory;
import net.minecraft.inventory.IInventory;

public class InventoryWrapper implements ICBInventory
{
    private final Inventory inventory;
    private final List<HumanEntity> viewers;
    
    public InventoryWrapper(final Inventory inventory) {
        this.viewers = new ArrayList<HumanEntity>();
        this.inventory = inventory;
    }
    
    @Override
    public int getSizeInventory() {
        return this.inventory.getSize();
    }
    
    @Override
    public ItemStack getStackInSlot(final int i) {
        return CraftItemStack.asNMSCopy(this.inventory.getItem(i));
    }
    
    @Override
    public ItemStack decrStackSize(final int i, final int j) {
        final ItemStack stack = this.getStackInSlot(i);
        if (stack == null) {
            return null;
        }
        ItemStack result;
        if (stack.stackSize <= j) {
            this.setInventorySlotContents(i, null);
            result = stack;
        }
        else {
            result = CraftItemStack.copyNMSStack(stack, j);
            final ItemStack itemStack = stack;
            itemStack.stackSize -= j;
        }
        this.markDirty();
        return result;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int i) {
        final ItemStack stack = this.getStackInSlot(i);
        if (stack == null) {
            return null;
        }
        ItemStack result;
        if (stack.stackSize <= 1) {
            this.setInventorySlotContents(i, null);
            result = stack;
        }
        else {
            result = CraftItemStack.copyNMSStack(stack, 1);
            final ItemStack itemStack = stack;
            --itemStack.stackSize;
        }
        return result;
    }
    
    @Override
    public void setInventorySlotContents(final int i, final ItemStack itemstack) {
        this.inventory.setItem(i, CraftItemStack.asBukkitCopy(itemstack));
    }
    
    @Override
    public int getInventoryStackLimit() {
        return this.inventory.getMaxStackSize();
    }
    
    @Override
    public void markDirty() {
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer entityhuman) {
        return true;
    }
    
    @Override
    public void openInventory(final EntityPlayer entityhuman) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer entityhuman) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }
    
    @Override
    public int getField(final int i) {
        return 0;
    }
    
    @Override
    public void setField(final int i, final int j) {
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        this.inventory.clear();
    }
    
    @Override
    public ItemStack[] getContents() {
        final int size = this.getSizeInventory();
        final ItemStack[] items = new ItemStack[size];
        for (int i = 0; i < size; ++i) {
            items[i] = this.getStackInSlot(i);
        }
        return items;
    }
    
    @Override
    public void onOpen(final CraftHumanEntity who) {
        this.viewers.add(who);
    }
    
    @Override
    public void onClose(final CraftHumanEntity who) {
        this.viewers.remove(who);
    }
    
    @Override
    public List<HumanEntity> getViewers() {
        return this.viewers;
    }
    
    @Override
    public InventoryHolder getOwner() {
        return this.inventory.getHolder();
    }
    
    @Override
    public void setMaxStackSize(final int size) {
        this.inventory.setMaxStackSize(size);
    }
    
    @Override
    public String getName() {
        return this.inventory.getName();
    }
    
    @Override
    public boolean hasCustomName() {
        return this.getName() != null;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return CraftChatMessage.fromString(this.getName())[0];
    }
    
    @Override
    public Location getLocation() {
        return this.inventory.getLocation();
    }
}
