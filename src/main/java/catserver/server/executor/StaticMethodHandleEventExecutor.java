package catserver.server.executor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.google.common.base.Preconditions;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public class StaticMethodHandleEventExecutor implements EventExecutor {
    private final Class<? extends Event> eventClass;
    private final MethodHandle handle;

    public StaticMethodHandleEventExecutor(Class<? extends Event> eventClass, Method m) {
        Preconditions.checkArgument(Modifier.isStatic(m.getModifiers()), "Not a static method: %s", m);
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
            handle.invoke(event);
        } catch (Throwable t) {
            throw new EventException(t);
        }
    }
}
