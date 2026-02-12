package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an item is put in a slot for repair by an anvil.
 */
public class PrepareAnvilEvent extends PrepareInventoryResultEvent {

    private static final HandlerList handlers = new HandlerList();

    public PrepareAnvilEvent(@NotNull InventoryView inventory, @Nullable ItemStack result) {
        super(inventory, result);
    }

    @NotNull
    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
