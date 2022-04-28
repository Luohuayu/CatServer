package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.StonecuttingRecipe;
import org.jetbrains.annotations.NotNull;

public class PlayerStonecutterRecipeSelectEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;
    private final StonecutterInventory stonecutterInventory;
    private StonecuttingRecipe stonecuttingRecipe;

    public PlayerStonecutterRecipeSelectEvent(@NotNull Player player, @NotNull StonecutterInventory stonecutterInventory, @NotNull StonecuttingRecipe stonecuttingRecipe) {
        super(player);
        this.stonecutterInventory = stonecutterInventory;
        this.stonecuttingRecipe = stonecuttingRecipe;
    }

    @NotNull
    public StonecutterInventory getStonecutterInventory() {
        return stonecutterInventory;
    }

    @NotNull
    public StonecuttingRecipe getStonecuttingRecipe() {
        return stonecuttingRecipe;
    }

    public void setStonecuttingRecipe(@NotNull StonecuttingRecipe stonecuttingRecipe) {
        this.stonecuttingRecipe = stonecuttingRecipe;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
