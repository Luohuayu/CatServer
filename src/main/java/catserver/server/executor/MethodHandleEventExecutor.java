package catserver.server.executor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public class MethodHandleEventExecutor implements EventExecutor {
    private final Class<? extends Event> eventClass;
    private final MethodHandle handle;

    public MethodHandleEventExecutor(Class<? extends Event> eventClass, MethodHandle handle) {
        this.eventClass = eventClass;
        this.handle = handle;
    }

    public MethodHandleEventExecutor(Class<? extends Event> eventClass, Method m) {
        this.eventClass = eventClass;
        try {
            m.setAccessible(true);
            this.handle = MethodHandles.lookup().unreflect(m);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Unable to set accessible", e);
        }
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (!eventClass.isInstance(event)) return;
        try {
            handle.invoke(listener, event);
        } catch (Throwable t) {
            throw new EventException(t);
        }
    }
}
