package io.papermc.paper.event.player;

import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.LoomInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player selects a banner patten in a loom inventory.
 */
public class PlayerLoomPatternSelectEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;
    private final LoomInventory loomInventory;
    private PatternType patternType;

    public PlayerLoomPatternSelectEvent(@NotNull Player player, @NotNull LoomInventory loomInventory, @NotNull PatternType patternType) {
        super(player);
        this.loomInventory = loomInventory;
        this.patternType = patternType;
    }

    /**
     * Gets the loom inventory involved.
     *
     * @return the loom inventory
     */
    @NotNull
    public LoomInventory getLoomInventory() {
        return loomInventory;
    }

    /**
     * Gets the pattern type selected.
     *
     * @return the pattern type
     */
    @NotNull
    public PatternType getPatternType() {
        return patternType;
    }

    /**
     * Sets the pattern type selected.
     *
     * @param patternType the pattern type
     */
    public void setPatternType(@NotNull PatternType patternType) {
        this.patternType = patternType;
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
