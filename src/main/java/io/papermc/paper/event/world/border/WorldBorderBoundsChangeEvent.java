package io.papermc.paper.event.world.border;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a world border changes its bounds, either over time, or instantly.
 */
public class WorldBorderBoundsChangeEvent extends WorldBorderEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Type type;
    private final double oldSize;
    private double newSize;
    private long duration;
    private boolean cancelled;

    public WorldBorderBoundsChangeEvent(@NotNull World world, @NotNull WorldBorder worldBorder, @NotNull Type type, double oldSize, double newSize, long duration) {
        super(world, worldBorder);
        this.type = type;
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.duration = duration;
    }

    /**
     * Gets if this change is an instant change or over-time change.
     *
     * @return the change type
     */
    @NotNull
    public Type getType() {
        return type;
    }

    /**
     * Gets the old size or the world border.
     *
     * @return the old size
     */
    public double getOldSize() {
        return oldSize;
    }

    /**
     * Gets the new size of the world border.
     *
     * @return the new size
     */
    public double getNewSize() {
        return newSize;
    }

    /**
     * Sets the new size of the world border.
     *
     * @param newSize the new size
     */
    public void setNewSize(double newSize) {
        // PAIL: TODO: Magic Values
        this.newSize = Math.min(6.0E7D, Math.max(1.0D, newSize));
    }

    /**
     * Gets the time in milliseconds for the change. Will be 0 if instant.
     *
     * @return the time in milliseconds for the change
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Sets the time in milliseconds for the change. Will change {@link #getType()} to return
     * {@link Type#STARTED_MOVE}.
     *
     * @param duration the time in milliseconds for the change
     */
    public void setDuration(long duration) {
        // PAIL: TODO: Magic Values
        this.duration = Math.min(9223372036854775L, Math.max(0L, duration));
        if (duration >= 0 && type == Type.INSTANT_MOVE) type = Type.STARTED_MOVE;
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

    public enum Type {
        STARTED_MOVE,
        INSTANT_MOVE
    }
}
