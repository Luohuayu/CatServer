package catserver.api.bukkit;

import net.minecraftforge.eventbus.api.Event;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class ForgeEventV2 extends org.bukkit.event.Event {
    private static final HandlerList handlers = new HandlerList();
    private final Event forgeEvent;

    public ForgeEventV2(Event forgeEvent) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.forgeEvent = forgeEvent;
    }

    public Event getForgeEvent() {
        return this.forgeEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
