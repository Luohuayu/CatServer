package org.bukkit.craftbukkit.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityShulkerBox;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public class CraftInventory implements Inventory {
    protected final IInventory inventory;

    public CraftInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return inventory;
    }

    public int getSize() {
        return getInventory().getSizeInventory();
    }

    public String getName() { // CatServer - not return null
        String name = inventory.getName();
        return name != null ? name : "MODInv_" + inventory.getClass().getSimpleName();
    }

    public ItemStack getItem(int index) {
        net.minecraft.item.ItemStack item = getInventory().getStackInSlot(index);
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    protected ItemStack[] asCraftMirror(List<net.minecraft.item.ItemStack> mcItems) {
        int size = mcItems.size();
        ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            net.minecraft.item.ItemStack mcItem = mcItems.get(i);
            items[i] = (mcItem.isEmpty()) ? null : CraftItemStack.asCraftMirror(mcItem);
        }

        return items;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return getContents();
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        setContents(items);
    }

    public ItemStack[] getContents() {
        // CatServer start - fix AbstractMethodError
        List<net.minecraft.item.ItemStack> mcItems = null;
        try {
            mcItems = getInventory().getContents();
        } catch (AbstractMethodError e) {
            return new ItemStack[0]; // return empty list
        }
        return asCraftMirror(mcItems);
    }

    public void setContents(ItemStack[] items) {
        if (getSize() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getSize() + " or less");
        }

        for (int i = 0; i < getSize(); i++) {
            if (i >= items.length) {
                setItem(i, null);
            } else {
                setItem(i, items[i]);
            }
        }
    }

    public void setItem(int index, ItemStack item) {
        getInventory().setInventorySlotContents(index, CraftItemStack.asNMSCopy(item));
    }

    public boolean contains(int materialId) {
        for (ItemStack item : getStorageContents()) {
            if (item != null && item.getTypeId() == materialId) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return contains(material.getId());
    }

    public boolean contains(ItemStack item) {
        if (item == null) {
            return false;
        }
        for (ItemStack i : getStorageContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(int materialId, int amount) {
        if (amount <= 0) {
            return true;
        }
        for (ItemStack item : getStorageContents()) {
            if (item != null && item.getTypeId() == materialId) {
                if ((amount -= item.getAmount()) <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contains(Material material, int amount) {
        Validate.notNull(material, "Material cannot be null");
        return contains(material.getId(), amount);
    }

    public boolean contains(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i : getStorageContents()) {
            if (item.equals(i) && --amount <= 0) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAtLeast(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i : getStorageContents()) {
            if (item.isSimilar(i) && (amount -= i.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }

    public HashMap<Integer, ItemStack> all(int materialId) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();

        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getTypeId() == materialId) {
                slots.put(i, item);
            }
        }
        return slots;
    }

    public HashMap<Integer, ItemStack> all(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return all(material.getId());
    }

    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            ItemStack[] inventory = getStorageContents();
            for (int i = 0; i < inventory.length; i++) {
                if (item.equals(inventory[i])) {
                    slots.put(i, inventory[i]);
                }
            }
        }
        return slots;
    }

    public int first(int materialId) {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getTypeId() == materialId) {
                return i;
            }
        }
        return -1;
    }

    public int first(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return first(material.getId());
    }

    public int first(ItemStack item) {
        return first(item, true);
    }

    private int first(ItemStack item, boolean withAmount) {
        if (item == null) {
            return -1;
        }
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) continue;

            if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
                return i;
            }
        }
        return -1;
    }

    public int firstEmpty() {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public int firstPartial(int materialId) {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getTypeId() == materialId && item.getAmount() < item.getMaxStackSize()) {
                return i;
            }
        }
        return -1;
    }

    public int firstPartial(Material material) {
        Validate.notNull(material, "Material cannot be null");
        return firstPartial(material.getId());
    }

    private int firstPartial(ItemStack item) {
        ItemStack[] inventory = getStorageContents();
        ItemStack filteredItem = CraftItemStack.asCraftCopy(item);
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < inventory.length; i++) {
            ItemStack cItem = inventory[i];
            if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(filteredItem)) {
                return i;
            }
        }
        return -1;
    }

    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        Validate.noNullElements(items, "Item cannot be null");
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        /* TODO: some optimization
         *  - Create a 'firstPartial' with a 'fromIndex'
         *  - Record the lastPartial per Material
         *  - Cache firstEmpty result
         */

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = firstPartial(item);

                // Drat! no partial stack
                if (firstPartial == -1) {
                    // Find a free spot!
                    int firstFree = firstEmpty();

                    if (firstFree == -1) {
                        // No space at all!
                        leftover.put(i, item);
                        break;
                    } else {
                        // More than a single stack!
                        if (item.getAmount() > getMaxItemStack()) {
                            CraftItemStack stack = CraftItemStack.asCraftCopy(item);
                            stack.setAmount(getMaxItemStack());
                            setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - getMaxItemStack());
                        } else {
                            // Just store it
                            setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = getItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();

                    // Check if it fully fits
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        // To make sure the packet is sent to the client
                        setItem(firstPartial, partialItem);
                        break;
                    }

                    // It fits partially
                    partialItem.setAmount(maxAmount);
                    // To make sure the packet is sent to the client
                    setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        return leftover;
    }

    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        Validate.notNull(items, "Items cannot be null");
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        // TODO: optimization

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                int first = first(item, false);

                // Drat! we don't have this type in the inventory
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                } else {
                    ItemStack itemStack = getItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        // clear the slot, all used up
                        clear(first);
                    } else {
                        // split the stack and store
                        itemStack.setAmount(amount - toDelete);
                        setItem(first, itemStack);
                        toDelete = 0;
                    }
                }

                // Bail when done
                if (toDelete <= 0) {
                    break;
                }
            }
        }
        return leftover;
    }

    private int getMaxItemStack() {
        return getInventory().getInventoryStackLimit();
    }

    public void remove(int materialId) {
        ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getTypeId() == materialId) {
                clear(i);
            }
        }
    }

    public void remove(Material material) {
        Validate.notNull(material, "Material cannot be null");
        remove(material.getId());
    }

    public void remove(ItemStack item) {
        ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].equals(item)) {
                clear(i);
            }
        }
    }

    public void clear(int index) {
        setItem(index, null);
    }

    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            clear(i);
        }
    }

    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }

    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0) {
            index += getSize() + 1; // ie, with -1, previous() will return the last element
        }
        return new InventoryIterator(this, index);
    }

    public List<HumanEntity> getViewers() {
        try {
            return this.inventory.getViewers();
        } catch (AbstractMethodError e) {
            return new java.util.ArrayList<HumanEntity>();
        }
    }

    public String getTitle() { // CatServer - not return null
        String name = inventory.getName();
        return name != null ? name : "MODInv_" + inventory.getClass().getSimpleName();
    }

    public InventoryType getType() {
        // Thanks to Droppers extending Dispensers, order is important.
        if (inventory instanceof InventoryCrafting) {
            return inventory.getSizeInventory() >= 9 ? InventoryType.WORKBENCH : InventoryType.CRAFTING;
        } else if (inventory instanceof InventoryPlayer) {
            return InventoryType.PLAYER;
        } else if (inventory instanceof TileEntityDropper) {
            return InventoryType.DROPPER;
        } else if (inventory instanceof TileEntityDispenser) {
            return InventoryType.DISPENSER;
        } else if (inventory instanceof TileEntityFurnace) {
            return InventoryType.FURNACE;
        } else if (this instanceof CraftInventoryEnchanting) {
           return InventoryType.ENCHANTING;
        } else if (inventory instanceof TileEntityBrewingStand) {
            return InventoryType.BREWING;
        } else if (inventory instanceof CraftInventoryCustom.MinecraftInventory) {
            return ((CraftInventoryCustom.MinecraftInventory) inventory).getType();
        } else if (inventory instanceof InventoryEnderChest) {
            return InventoryType.ENDER_CHEST;
        } else if (inventory instanceof InventoryMerchant) {
            return InventoryType.MERCHANT;
        } else if (inventory instanceof TileEntityBeacon) {
            return InventoryType.BEACON;
        } else if (this instanceof CraftInventoryAnvil) {
           return InventoryType.ANVIL;
        } else if (inventory instanceof IHopper) {
            return InventoryType.HOPPER;
        } else if (inventory instanceof TileEntityShulkerBox) {
            return InventoryType.SHULKER_BOX;
        } else {
            return InventoryType.CHEST;
        }
    }

    public InventoryHolder getHolder() {
        // CatServer start - fix AbstractMethodError
        try {
            return inventory.getOwner();
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

    public int getMaxStackSize() {
        return inventory.getInventoryStackLimit();
    }

    public void setMaxStackSize(int size) {
        inventory.setMaxStackSize(size);
    }

    @Override
    public int hashCode() {
        return inventory.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CraftInventory && ((CraftInventory) obj).inventory.equals(this.inventory);
    }

    @Override
    public Location getLocation() {
        return inventory.getLocation();
    }
}
