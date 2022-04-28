package io.papermc.paper.event.packet;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Is called when a {@link org.bukkit.entity.Player} receives a {@link org.bukkit.Chunk}
 * <p>
 * Can for example be used for spawning a fake entity when the player receives a chunk.
 *
 * Should only be used for packet/clientside related stuff.
 * Not intended for modifying server side state.
 */
public class PlayerChunkLoadEvent extends ChunkEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public PlayerChunkLoadEvent(@NotNull Chunk chunk, @NotNull Player player) {
        super(chunk);
        this.player = player;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
