package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player places an item in or takes an item out of a flowerpot.
 */
public class PlayerFlowerPotManipulateEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @NotNull
    private final Block flowerpot;
    @NotNull
    private final ItemStack item;
    private final boolean placing;

    private boolean cancel = false;

    public PlayerFlowerPotManipulateEvent(@NotNull final Player player, @NotNull final Block flowerpot, @NotNull final ItemStack item, final boolean placing) {
        super(player);
        this.flowerpot = flowerpot;
        this.item = item;
        this.placing = placing;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the flowerpot that is involved in this event.
     *
     * @return the flowerpot that is involved with this event
     */
    @NotNull
    public Block getFlowerpot() {
        return flowerpot;
    }

    /**
     * Gets the item being placed, or taken from, the flower pot.
     * Check if placing with {@link #isPlacing()}.
     *
     * @return the item placed, or taken from, the flowerpot
     */
    @NotNull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets if the item is being placed into the flowerpot.
     *
     * @return if the item is being placed into the flowerpot
     */
    public boolean isPlacing() {
        return placing;
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
