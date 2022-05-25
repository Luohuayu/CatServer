package catserver.server.hook;

import catserver.api.bukkit.ForgeEventV2;
import net.minecraftforge.eventbus.api.Event;
import org.bukkit.Bukkit;

public class ForgeEventHook {
    public static void post(Event event) {
        if (Bukkit.getServer() != null && ForgeEventV2.getHandlerList().getRegisteredListeners().length > 0) {
            Bukkit.getPluginManager().callEvent(new ForgeEventV2(event));
        }
    }
}
