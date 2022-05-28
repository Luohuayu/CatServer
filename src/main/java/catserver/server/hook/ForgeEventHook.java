package catserver.server.hook;

import catserver.api.bukkit.ForgeEventV2;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBusInvokeDispatcher;
import net.minecraftforge.eventbus.api.IEventListener;
import org.bukkit.Bukkit;

public class ForgeEventHook implements IEventBusInvokeDispatcher {
    public static void initHook() {
        try {
            Class.forName("net.minecraftforge.eventbus.EventBus").getMethod("setHookDispatcher", IEventBusInvokeDispatcher.class).invoke(null, new ForgeEventHook());
        } catch (Exception e) {
            throw new RuntimeException("Failed to hook ForgeEvent (patch failed?)");
        }
    }

    @Override
    public void invoke(IEventListener listener, Event event) {
        if (ForgeEventV2.getHandlerList().getRegisteredListeners().length > 0) {
            Bukkit.getPluginManager().callEvent(new ForgeEventV2(event));
        }
    }
}
