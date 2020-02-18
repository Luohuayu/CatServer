package catserver.api.bukkit.event;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class FakePlayerJoinEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final World world;

    public FakePlayerJoinEvent(Player who, World world) {
        super(who);
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
