// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.GameMode;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import net.minecraft.inventory.Container;
import org.bukkit.inventory.InventoryView;

public class CraftInventoryView extends InventoryView
{
    private final Container container;
    private final CraftHumanEntity player;
    private final CraftInventory viewing;
    
    public CraftInventoryView(final HumanEntity player, final Inventory viewing, final Container container) {
        this.player = (CraftHumanEntity)player;
        this.viewing = (CraftInventory)viewing;
        this.container = container;
    }
    
    @Override
    public Inventory getTopInventory() {
        return this.viewing;
    }
    
    @Override
    public Inventory getBottomInventory() {
        return this.player.getInventory();
    }
    
    @Override
    public HumanEntity getPlayer() {
        return this.player;
    }
    
    @Override
    public InventoryType getType() {
        final InventoryType type = this.viewing.getType();
        if (type == InventoryType.CRAFTING && this.player.getGameMode() == GameMode.CREATIVE) {
            return InventoryType.CREATIVE;
        }
        return type;
    }
    
    @Override
    public void setItem(final int slot, final ItemStack item) {
        final net.minecraft.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (slot != -999) {
            this.container.getSlot(slot).putStack(stack);
        }
        else {
            this.player.getHandle().dropItem(stack, false);
        }
    }
    
    @Override
    public ItemStack getItem(final int slot) {
        if (slot == -999) {
            return null;
        }
        return CraftItemStack.asCraftMirror(this.container.getSlot(slot).getStack());
    }
    
    public boolean isInTop(final int rawSlot) {
        return rawSlot < this.viewing.getSize();
    }
    
    public Container getHandle() {
        return this.container;
    }
    
    public static InventoryType.SlotType getSlotType(final InventoryView inventory, final int slot) {
        InventoryType.SlotType type = InventoryType.SlotType.CONTAINER;
        if (slot >= 0 && slot < inventory.getTopInventory().getSize()) {
            switch (inventory.getType()) {
                case FURNACE: {
                    if (slot == 2) {
                        type = InventoryType.SlotType.RESULT;
                        break;
                    }
                    if (slot == 1) {
                        type = InventoryType.SlotType.FUEL;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case BREWING: {
                    if (slot == 3) {
                        type = InventoryType.SlotType.FUEL;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case ENCHANTING: {
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case WORKBENCH:
                case CRAFTING: {
                    if (slot == 0) {
                        type = InventoryType.SlotType.RESULT;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case MERCHANT: {
                    if (slot == 2) {
                        type = InventoryType.SlotType.RESULT;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case BEACON: {
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case ANVIL: {
                    if (slot == 2) {
                        type = InventoryType.SlotType.RESULT;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
            }
        }
        else if (slot == -999 || slot == -1) {
            type = InventoryType.SlotType.OUTSIDE;
        }
        else if (inventory.getType() == InventoryType.CRAFTING) {
            if (slot < 9) {
                type = InventoryType.SlotType.ARMOR;
            }
            else if (slot > 35) {
                type = InventoryType.SlotType.QUICKBAR;
            }
        }
        else if (slot >= inventory.countSlots() - 9) {
            type = InventoryType.SlotType.QUICKBAR;
        }
        return type;
    }
}
