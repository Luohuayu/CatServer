package io.papermc.paper.event.world.border;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a world border's center is changed.
 */
public class WorldBorderCenterChangeEvent extends WorldBorderEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location oldCenter;
    private Location newCenter;
    private boolean cancelled;

    public WorldBorderCenterChangeEvent(@NotNull World world, @NotNull WorldBorder worldBorder, @NotNull Location oldCenter, @NotNull Location newCenter) {
        super(world, worldBorder);
        this.oldCenter = oldCenter;
        this.newCenter = newCenter;
    }

    /**
     * Gets the original center location of the world border.
     *
     * @return the old center
     */
    @NotNull
    public Location getOldCenter() {
        return oldCenter;
    }

    /**
     * Gets the new center location for the world border.
     *
     * @return the new center
     */
    @NotNull
    public Location getNewCenter() {
        return newCenter;
    }

    /**
     * Sets the new center location for the world border. Y coordinate is ignored.
     *
     * @param newCenter the new center
     */
    public void setNewCenter(@NotNull Location newCenter) {
        this.newCenter = newCenter;
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
