package io.papermc.paper.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when resources such as datapacks are reloaded (e.g. /minecraft:reload)
 * <p>
 *     Intended for use to re-register custom recipes, advancements that may be lost during a reload like this.
 * </p>
 */
public class ServerResourcesReloadedEvent extends ServerEvent {

    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final Cause cause;

    public ServerResourcesReloadedEvent(@NotNull Cause cause) {
        this.cause = cause;
    }

    /**
     * Gets the cause of the resource reload.
     *
     * @return the reload cause
     */
    @NotNull
    public Cause getCause() {
        return cause;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public enum Cause {
        COMMAND,
        PLUGIN,
    }
}
