// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.Location;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.HumanEntity;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class CraftInventoryCustom extends CraftInventory
{
    public CraftInventoryCustom(final InventoryHolder owner, final InventoryType type) {
        super(new MinecraftInventory(owner, type));
    }
    
    public CraftInventoryCustom(final InventoryHolder owner, final InventoryType type, final String title) {
        super(new MinecraftInventory(owner, type, title));
    }
    
    public CraftInventoryCustom(final InventoryHolder owner, final int size) {
        super(new MinecraftInventory(owner, size));
    }
    
    public CraftInventoryCustom(final InventoryHolder owner, final int size, final String title) {
        super(new MinecraftInventory(owner, size, title));
    }
    
    static class MinecraftInventory implements IInventory
    {
        private final ItemStack[] items;
        private int maxStack;
        private final List<HumanEntity> viewers;
        private final String title;
        private InventoryType type;
        private final InventoryHolder owner;
        
        public MinecraftInventory(final InventoryHolder owner, final InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }
        
        public MinecraftInventory(final InventoryHolder owner, final InventoryType type, final String title) {
            this(owner, type.getDefaultSize(), title);
            this.type = type;
        }
        
        public MinecraftInventory(final InventoryHolder owner, final int size) {
            this(owner, size, "Chest");
        }
        
        public MinecraftInventory(final InventoryHolder owner, final int size, final String title) {
            this.maxStack = 64;
            Validate.notNull((Object)title, "Title cannot be null");
            this.items = new ItemStack[size];
            this.title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }
        
        @Override
        public int getSizeInventory() {
            return this.items.length;
        }
        
        @Override
        public ItemStack getStackInSlot(final int i) {
            return this.items[i];
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
            this.items[i] = itemstack;
            if (itemstack != null && this.getInventoryStackLimit() > 0 && itemstack.stackSize > this.getInventoryStackLimit()) {
                itemstack.stackSize = this.getInventoryStackLimit();
            }
        }
        
        @Override
        public int getInventoryStackLimit() {
            return this.maxStack;
        }
        
        @Override
        public void setMaxStackSize(final int size) {
            this.maxStack = size;
        }
        
        @Override
        public void markDirty() {
        }
        
        @Override
        public boolean isUseableByPlayer(final EntityPlayer entityhuman) {
            return true;
        }
        
        @Override
        public ItemStack[] getContents() {
            return this.items;
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
        
        public InventoryType getType() {
            return this.type;
        }
        
        @Override
        public InventoryHolder getOwner() {
            return this.owner;
        }
        
        @Override
        public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
            return true;
        }
        
        @Override
        public void openInventory(final EntityPlayer entityHuman) {
        }
        
        @Override
        public void closeInventory(final EntityPlayer entityHuman) {
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
        }
        
        @Override
        public String getName() {
            return this.title;
        }
        
        @Override
        public boolean hasCustomName() {
            return this.title != null;
        }
        
        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentString(this.title);
        }
        
        @Override
        public Location getLocation() {
            return null;
        }
    }
}
