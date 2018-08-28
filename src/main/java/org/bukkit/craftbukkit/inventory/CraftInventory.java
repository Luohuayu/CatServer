// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.Location;
import org.bukkit.inventory.InventoryHolder;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryCrafting;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.HumanEntity;
import java.util.List;
import java.util.ListIterator;
import java.util.HashMap;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.Inventory;

public class CraftInventory implements Inventory
{
    protected final IInventory inventory;
    
    public CraftInventory(final IInventory inventory) {
        this.inventory = inventory;
    }
    
    public IInventory getInventory() {
        return this.inventory;
    }
    
    @Override
    public int getSize() {
        return this.getInventory().getSizeInventory();
    }
    
    @Override
    public String getName() {
        return this.getInventory().getName();
    }
    
    @Override
    public ItemStack getItem(final int index) {
        final net.minecraft.item.ItemStack item = this.getInventory().getStackInSlot(index);
        return (item == null) ? null : CraftItemStack.asCraftMirror(item);
    }
    
    @Override
    public ItemStack[] getStorageContents() {
        return this.getContents();
    }
    
    @Override
    public void setStorageContents(final ItemStack[] items) throws IllegalArgumentException {
        this.setContents(items);
    }
    
    @Override
    public ItemStack[] getContents() {
        final ItemStack[] items = new ItemStack[this.getSize()];
        // CatServer start - fix AbstractMethodError
        net.minecraft.item.ItemStack[] mcItems = null;
        try {
            mcItems = this.getInventory().getContents();
        } catch (AbstractMethodError e) {
            return new ItemStack[0]; // return empty list
        }
        // CatServer end
        for (int size = Math.min(items.length, mcItems.length), i = 0; i < size; ++i) {
            items[i] = ((mcItems[i] == null) ? null : CraftItemStack.asCraftMirror(mcItems[i]));
        }
        return items;
    }
    
    @Override
    public void setContents(final ItemStack[] items) {
        if (this.getSize() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getSize() + " or less");
        }
        for (int i = 0; i < this.getSize(); ++i) {
            if (i >= items.length) {
                this.setItem(i, null);
            }
            else {
                this.setItem(i, items[i]);
            }
        }
    }
    
    @Override
    public void setItem(final int index, final ItemStack item) {
        this.getInventory().setInventorySlotContents(index, (item == null || item.getTypeId() == 0) ? null : CraftItemStack.asNMSCopy(item));
    }
    
    @Override
    public boolean contains(final int materialId) {
        ItemStack[] storageContents;
        for (int length = (storageContents = this.getStorageContents()).length, i = 0; i < length; ++i) {
            final ItemStack item = storageContents[i];
            if (item != null && item.getTypeId() == materialId) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final Material material) {
        Validate.notNull((Object)material, "Material cannot be null");
        return this.contains(material.getId());
    }
    
    @Override
    public boolean contains(final ItemStack item) {
        if (item == null) {
            return false;
        }
        ItemStack[] storageContents;
        for (int length = (storageContents = this.getStorageContents()).length, j = 0; j < length; ++j) {
            final ItemStack i = storageContents[j];
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final int materialId, int amount) {
        if (amount <= 0) {
            return true;
        }
        ItemStack[] storageContents;
        for (int length = (storageContents = this.getStorageContents()).length, i = 0; i < length; ++i) {
            final ItemStack item = storageContents[i];
            if (item != null && item.getTypeId() == materialId && (amount -= item.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final Material material, final int amount) {
        Validate.notNull((Object)material, "Material cannot be null");
        return this.contains(material.getId(), amount);
    }
    
    @Override
    public boolean contains(final ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        ItemStack[] storageContents;
        for (int length = (storageContents = this.getStorageContents()).length, j = 0; j < length; ++j) {
            final ItemStack i = storageContents[j];
            if (item.equals(i) && --amount <= 0) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsAtLeast(final ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        ItemStack[] storageContents;
        for (int length = (storageContents = this.getStorageContents()).length, j = 0; j < length; ++j) {
            final ItemStack i = storageContents[j];
            if (item.isSimilar(i) && (amount -= i.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public HashMap<Integer, ItemStack> all(final int materialId) {
        final HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        final ItemStack[] inventory = this.getStorageContents();
        for (int i = 0; i < inventory.length; ++i) {
            final ItemStack item = inventory[i];
            if (item != null && item.getTypeId() == materialId) {
                slots.put(i, item);
            }
        }
        return slots;
    }
    
    @Override
    public HashMap<Integer, ItemStack> all(final Material material) {
        Validate.notNull((Object)material, "Material cannot be null");
        return this.all(material.getId());
    }
    
    @Override
    public HashMap<Integer, ItemStack> all(final ItemStack item) {
        final HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            final ItemStack[] inventory = this.getStorageContents();
            for (int i = 0; i < inventory.length; ++i) {
                if (item.equals(inventory[i])) {
                    slots.put(i, inventory[i]);
                }
            }
        }
        return slots;
    }
    
    @Override
    public int first(final int materialId) {
        final ItemStack[] inventory = this.getStorageContents();
        for (int i = 0; i < inventory.length; ++i) {
            final ItemStack item = inventory[i];
            if (item != null && item.getTypeId() == materialId) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int first(final Material material) {
        Validate.notNull((Object)material, "Material cannot be null");
        return this.first(material.getId());
    }
    
    @Override
    public int first(final ItemStack item) {
        return this.first(item, true);
    }
    
    private int first(final ItemStack item, final boolean withAmount) {
        if (item == null) {
            return -1;
        }
        final ItemStack[] inventory = this.getStorageContents();
        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null) {
                if (withAmount) {
                    if (!item.equals(inventory[i])) {
                        continue;
                    }
                }
                else if (!item.isSimilar(inventory[i])) {
                    continue;
                }
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int firstEmpty() {
        final ItemStack[] inventory = this.getStorageContents();
        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] == null) {
                return i;
            }
        }
        return -1;
    }
    
    public int firstPartial(final int materialId) {
        final ItemStack[] inventory = this.getStorageContents();
        for (int i = 0; i < inventory.length; ++i) {
            final ItemStack item = inventory[i];
            if (item != null && item.getTypeId() == materialId && item.getAmount() < item.getMaxStackSize()) {
                return i;
            }
        }
        return -1;
    }
    
    public int firstPartial(final Material material) {
        Validate.notNull((Object)material, "Material cannot be null");
        return this.firstPartial(material.getId());
    }
    
    private int firstPartial(final ItemStack item) {
        final ItemStack[] inventory = this.getStorageContents();
        final ItemStack filteredItem = CraftItemStack.asCraftCopy(item);
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < inventory.length; ++i) {
            final ItemStack cItem = inventory[i];
            if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(filteredItem)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public HashMap<Integer, ItemStack> addItem(final ItemStack... items) {
        Validate.noNullElements((Object[])items, "Item cannot be null");
        final HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();
        for (int i = 0; i < items.length; ++i) {
            final ItemStack item = items[i];
            while (true) {
                final int firstPartial = this.firstPartial(item);
                if (firstPartial == -1) {
                    final int firstFree = this.firstEmpty();
                    if (firstFree == -1) {
                        leftover.put(i, item);
                        break;
                    }
                    if (item.getAmount() <= this.getMaxItemStack()) {
                        this.setItem(firstFree, item);
                        break;
                    }
                    final CraftItemStack stack = CraftItemStack.asCraftCopy(item);
                    stack.setAmount(this.getMaxItemStack());
                    this.setItem(firstFree, stack);
                    item.setAmount(item.getAmount() - this.getMaxItemStack());
                }
                else {
                    final ItemStack partialItem = this.getItem(firstPartial);
                    final int amount = item.getAmount();
                    final int partialAmount = partialItem.getAmount();
                    final int maxAmount = partialItem.getMaxStackSize();
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        this.setItem(firstPartial, partialItem);
                        break;
                    }
                    partialItem.setAmount(maxAmount);
                    this.setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        return leftover;
    }
    
    @Override
    public HashMap<Integer, ItemStack> removeItem(final ItemStack... items) {
        Validate.notNull((Object)items, "Items cannot be null");
        final HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();
        for (int i = 0; i < items.length; ++i) {
            final ItemStack item = items[i];
            int toDelete = item.getAmount();
            do {
                final int first = this.first(item, false);
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                }
                final ItemStack itemStack = this.getItem(first);
                final int amount = itemStack.getAmount();
                if (amount <= toDelete) {
                    toDelete -= amount;
                    this.clear(first);
                }
                else {
                    itemStack.setAmount(amount - toDelete);
                    this.setItem(first, itemStack);
                    toDelete = 0;
                }
            } while (toDelete > 0);
        }
        return leftover;
    }
    
    private int getMaxItemStack() {
        return this.getInventory().getInventoryStackLimit();
    }
    
    @Override
    public void remove(final int materialId) {
        final ItemStack[] items = this.getStorageContents();
        for (int i = 0; i < items.length; ++i) {
            if (items[i] != null && items[i].getTypeId() == materialId) {
                this.clear(i);
            }
        }
    }
    
    @Override
    public void remove(final Material material) {
        Validate.notNull((Object)material, "Material cannot be null");
        this.remove(material.getId());
    }
    
    @Override
    public void remove(final ItemStack item) {
        final ItemStack[] items = this.getStorageContents();
        for (int i = 0; i < items.length; ++i) {
            if (items[i] != null && items[i].equals(item)) {
                this.clear(i);
            }
        }
    }
    
    @Override
    public void clear(final int index) {
        this.setItem(index, null);
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.getSize(); ++i) {
            this.clear(i);
        }
    }
    
    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }
    
    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0) {
            index += this.getSize() + 1;
        }
        return new InventoryIterator(this, index);
    }
    
    @Override
    public List<HumanEntity> getViewers() {
        try {
            return this.inventory.getViewers();
        } catch (AbstractMethodError e) {
            return new java.util.ArrayList<HumanEntity>();
        }
    }
    
    @Override
    public String getTitle() {
        return this.inventory.getName();
    }
    
    @Override
    public InventoryType getType() {
        if (this.inventory instanceof InventoryCrafting) {
            return (this.inventory.getSizeInventory() >= 9) ? InventoryType.WORKBENCH : InventoryType.CRAFTING;
        }
        if (this.inventory instanceof InventoryPlayer) {
            return InventoryType.PLAYER;
        }
        if (this.inventory instanceof TileEntityDropper) {
            return InventoryType.DROPPER;
        }
        if (this.inventory instanceof TileEntityDispenser) {
            return InventoryType.DISPENSER;
        }
        if (this.inventory instanceof TileEntityFurnace) {
            return InventoryType.FURNACE;
        }
        if (this instanceof CraftInventoryEnchanting) {
            return InventoryType.ENCHANTING;
        }
        if (this.inventory instanceof TileEntityBrewingStand) {
            return InventoryType.BREWING;
        }
        if (this.inventory instanceof CraftInventoryCustom.MinecraftInventory) {
            return ((CraftInventoryCustom.MinecraftInventory)this.inventory).getType();
        }
        if (this.inventory instanceof InventoryEnderChest) {
            return InventoryType.ENDER_CHEST;
        }
        if (this.inventory instanceof InventoryMerchant) {
            return InventoryType.MERCHANT;
        }
        if (this.inventory instanceof TileEntityBeacon) {
            return InventoryType.BEACON;
        }
        if (this instanceof CraftInventoryAnvil) {
            return InventoryType.ANVIL;
        }
        if (this.inventory instanceof IHopper) {
            return InventoryType.HOPPER;
        }
        return InventoryType.CHEST;
    }
    
    @Override
    public InventoryHolder getHolder() {
        // CatServer start - fix AbstractMethodError
        try {
            return this.inventory.getOwner();
        } catch (AbstractMethodError e) {
            if (inventory instanceof net.minecraft.tileentity.TileEntity) {
                net.minecraft.tileentity.TileEntity tileentity = (net.minecraft.tileentity.TileEntity) inventory;
                BlockState state = tileentity.getWorld().getWorld().getBlockAt(tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ()).getState();
                return (state instanceof InventoryHolder ? (InventoryHolder) state : null);
            }else{
                return null;
            }
        }
        // CatServer end
    }
    
    @Override
    public int getMaxStackSize() {
        return this.inventory.getInventoryStackLimit();
    }
    
    @Override
    public void setMaxStackSize(final int size) {
        this.inventory.setMaxStackSize(size);
    }
    
    @Override
    public int hashCode() {
        return this.inventory.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CraftInventory && ((CraftInventory)obj).inventory.equals(this.inventory);
    }
    
    @Override
    public Location getLocation() {
        return this.inventory.getLocation();
    }
}
