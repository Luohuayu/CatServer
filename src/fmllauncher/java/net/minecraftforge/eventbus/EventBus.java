/*
 * Minecraft Forge
 * Copyright (c) 2016.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.eventbus;

import net.jodah.typetools.TypeResolver;
import net.minecraftforge.eventbus.api.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraftforge.eventbus.LogMarkers.EVENTBUS;

public class EventBus implements IEventExceptionHandler, IEventBus {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final boolean checkTypesOnDispatch = Boolean.parseBoolean(System.getProperty("eventbus.checkTypesOnDispatch", "false"));
    private static AtomicInteger maxID = new AtomicInteger(0);
    private final boolean trackPhases;


    private ConcurrentHashMap<Object, List<IEventListener>> listeners = new ConcurrentHashMap<>();
    private final int busID = maxID.getAndIncrement();
    private final IEventExceptionHandler exceptionHandler;
    private volatile boolean shutdown = false;

    private final Class<?> baseType;

    private EventBus()
    {
        ListenerList.resize(busID + 1);
        exceptionHandler = this;
        this.trackPhases = true;
        this.baseType = Event.class;
    }

    private EventBus(final IEventExceptionHandler handler, boolean trackPhase, boolean startShutdown, Class<?> baseType)
    {
        ListenerList.resize(busID + 1);
        if (handler == null) exceptionHandler = this;
        else exceptionHandler = handler;
        this.trackPhases = trackPhase;
        this.shutdown = startShutdown;
        this.baseType = baseType;
    }

    public EventBus(final BusBuilder busBuilder) {
        this(busBuilder.getExceptionHandler(), busBuilder.getTrackPhases(), busBuilder.isStartingShutdown(), busBuilder.getMarkerType());
    }

    private void registerClass(final Class<?> clazz) {
        Arrays.stream(clazz.getMethods()).
                filter(m->Modifier.isStatic(m.getModifiers())).
                filter(m->m.isAnnotationPresent(SubscribeEvent.class)).
                forEach(m->registerListener(clazz, m, m));
    }

    private Optional<Method> getDeclMethod(final Class<?> clz, final Method in) {
        try {
            return Optional.of(clz.getDeclaredMethod(in.getName(), in.getParameterTypes()));
        } catch (NoSuchMethodException nse) {
            return Optional.empty();
        }

    }
    private void registerObject(final Object obj) {
        final HashSet<Class<?>> classes = new HashSet<>();
        typesFor(obj.getClass(), classes);
        Arrays.stream(obj.getClass().getMethods()).
                filter(m->!Modifier.isStatic(m.getModifiers())).
                forEach(m -> classes.stream().
                        map(c->getDeclMethod(c, m)).
                        filter(rm -> rm.isPresent() && rm.get().isAnnotationPresent(SubscribeEvent.class)).
                        findFirst().
                        ifPresent(rm->registerListener(obj, m, rm.get())));
    }


    private void typesFor(final Class<?> clz, final Set<Class<?>> visited) {
        if (clz.getSuperclass() == null) return;
        typesFor(clz.getSuperclass(),visited);
        Arrays.stream(clz.getInterfaces()).forEach(i->typesFor(i, visited));
        visited.add(clz);
    }

    @Override
    public void register(final Object target)
    {
        if (listeners.containsKey(target))
        {
            return;
        }

        if (target.getClass() == Class.class) {
            registerClass((Class<?>) target);
        } else {
            registerObject(target);
        }
    }

    private void registerListener(final Object target, final Method method, final Method real) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1)
        {
            throw new IllegalArgumentException(
                    "Method " + method + " has @SubscribeEvent annotation. " +
                            "It has " + parameterTypes.length + " arguments, " +
                            "but event handler methods require a single argument only."
            );
        }

        Class<?> eventType = parameterTypes[0];

        if (!Event.class.isAssignableFrom(eventType))
        {
            throw new IllegalArgumentException(
                    "Method " + method + " has @SubscribeEvent annotation, " +
                            "but takes an argument that is not an Event subtype : " + eventType);
        }
        if (baseType != Event.class && !baseType.isAssignableFrom(eventType))
        {
            throw new IllegalArgumentException(
                    "Method " + method + " has @SubscribeEvent annotation, " +
                            "but takes an argument that is not a subtype of the base type " + baseType + ": " + eventType);
        }

        register(eventType, target, real);
    }

    private <T extends Event> Predicate<T> passCancelled(final boolean ignored) {
        return e-> ignored || !e.isCancelable() || !e.isCanceled();
    }

    private <T extends GenericEvent<? extends F>, F> Predicate<T> passGenericFilter(Class<F> type) {
        return e->e.getGenericType() == type;
    }

    private void checkNotGeneric(final Consumer<? extends Event> consumer) {
        checkNotGeneric(getEventClass(consumer));
    }

    private void checkNotGeneric(final Class<? extends Event> eventType) {
        if (GenericEvent.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException("Cannot register a generic event listener with addListener, use addGenericListener");
        }
    }

    @Override
    public <T extends Event> void addListener(final Consumer<T> consumer) {
        checkNotGeneric(consumer);
        addListener(EventPriority.NORMAL, consumer);
    }

    @Override
    public <T extends Event> void addListener(final EventPriority priority, final Consumer<T> consumer) {
        checkNotGeneric(consumer);
        addListener(priority, false, consumer);
    }

    @Override
    public <T extends Event> void addListener(final EventPriority priority, final boolean receiveCancelled, final Consumer<T> consumer) {
        checkNotGeneric(consumer);
        addListener(priority, passCancelled(receiveCancelled), consumer);
    }

    @Override
    public <T extends Event> void addListener(final EventPriority priority, final boolean receiveCancelled, final Class<T> eventType, final Consumer<T> consumer) {
        checkNotGeneric(eventType);
        addListener(priority, passCancelled(receiveCancelled), eventType, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(final Class<F> genericClassFilter, final Consumer<T> consumer) {
        addGenericListener(genericClassFilter, EventPriority.NORMAL, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(final Class<F> genericClassFilter, final EventPriority priority, final Consumer<T> consumer) {
        addGenericListener(genericClassFilter, priority, false, consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(final Class<F> genericClassFilter, final EventPriority priority, final boolean receiveCancelled, final Consumer<T> consumer) {
        addListener(priority, passGenericFilter(genericClassFilter).and(passCancelled(receiveCancelled)), consumer);
    }

    @Override
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(final Class<F> genericClassFilter, final EventPriority priority, final boolean receiveCancelled, final Class<T> eventType, final Consumer<T> consumer) {
        addListener(priority, passGenericFilter(genericClassFilter).and(passCancelled(receiveCancelled)), eventType, consumer);
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> Class<T> getEventClass(Consumer<T> consumer) {
        final Class<T> eventClass = (Class<T>) TypeResolver.resolveRawArgument(Consumer.class, consumer.getClass());
        if ((Class<?>)eventClass == TypeResolver.Unknown.class) {
            LOGGER.error(EVENTBUS, "Failed to resolve handler for \"{}\"", consumer.toString());
            throw new IllegalStateException("Failed to resolve consumer event type: " + consumer.toString());
        }
        return eventClass;
    }

    private <T extends Event> void addListener(final EventPriority priority, final Predicate<? super T> filter, final Consumer<T> consumer) {
        Class<T> eventClass = getEventClass(consumer);
        if (Objects.equals(eventClass, Event.class))
            LOGGER.warn(EVENTBUS,"Attempting to add a Lambda listener with computed generic type of Event. " +
                    "Are you sure this is what you meant? NOTE : there are complex lambda forms where " +
                    "the generic type information is erased and cannot be recovered at runtime.");
        addListener(priority, filter, eventClass, consumer);
    }

    private <T extends Event> void addListener(final EventPriority priority, final Predicate<? super T> filter, final Class<T> eventClass, final Consumer<T> consumer) {
        if (baseType != Event.class && !baseType.isAssignableFrom(eventClass)) {
            throw new IllegalArgumentException(
                    "Listener for event " + eventClass + " takes an argument that is not a subtype of the base type " + baseType);
        }
        addToListeners(consumer, eventClass, NamedEventListener.namedWrapper(e-> doCastFilter(filter, eventClass, consumer, e), consumer.getClass()::getName), priority);
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void doCastFilter(final Predicate<? super T> filter, final Class<T> eventClass, final Consumer<T> consumer, final Event e) {
        T cast = (T)e;
        if (filter.test(cast))
        {
            consumer.accept(cast);
        }
    }

    private void register(Class<?> eventType, Object target, Method method)
    {
        try {
            final ASMEventHandler asm = new ASMEventHandler(target, method, IGenericEvent.class.isAssignableFrom(eventType));

            addToListeners(target, eventType, asm, asm.getPriority());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.error(EVENTBUS,"Error registering event handler: {} {}", eventType, method, e);
        }
    }

    private void addToListeners(final Object target, final Class<?> eventType, final IEventListener listener, final EventPriority priority) {
        ListenerList listenerList = EventListenerHelper.getListenerList(eventType);
        listenerList.register(busID, priority, listener);
        List<IEventListener> others = listeners.computeIfAbsent(target, k -> Collections.synchronizedList(new ArrayList<>()));
        others.add(listener);
    }

    @Override
    public void unregister(Object object)
    {
        List<IEventListener> list = listeners.remove(object);
        if(list == null)
            return;
        for (IEventListener listener : list)
        {
            ListenerList.unregisterAll(busID, listener);
        }
    }

    @Override
    public boolean post(Event event) {
        return post(event, (IEventListener::invoke));
    }

    @Override
    public boolean post(Event event, IEventBusInvokeDispatcher wrapper)
    {
        if (shutdown) return false;
        if (EventBus.checkTypesOnDispatch && !baseType.isInstance(event))
        {
            throw new IllegalArgumentException("Cannot post event of type " + event.getClass().getSimpleName() + " to this event. Must match type: " + baseType.getSimpleName());
        }

        IEventListener[] listeners = event.getListenerList().getListeners(busID);
        int index = 0;
        try
        {
            if (hookDisPatcher != null) hookDisPatcher.invoke(null, event); // CatServer
            for (; index < listeners.length; index++)
            {
                if (!trackPhases && Objects.equals(listeners[index].getClass(), EventPriority.class)) continue;
                wrapper.invoke(listeners[index], event);
            }
        }
        catch (Throwable throwable)
        {
            exceptionHandler.handleException(this, event, listeners, index, throwable);
            throw throwable;
        }
        return event.isCancelable() && event.isCanceled();
    }

    @Override
    public void handleException(IEventBus bus, Event event, IEventListener[] listeners, int index, Throwable throwable)
    {
        LOGGER.error(EVENTBUS, ()->new EventBusErrorMessage(event, index, listeners, throwable));
    }

    @Override
    public void shutdown()
    {
        LOGGER.fatal(EVENTBUS, "EventBus {} shutting down - future events will not be posted.", busID, new Exception("stacktrace"));
        this.shutdown = true;
    }

    @Override
    public void start() {
        this.shutdown = false;
    }

    // CatServer start
    private static IEventBusInvokeDispatcher hookDisPatcher = null;
    public static void setHookDispatcher(IEventBusInvokeDispatcher hookDisPatcher) {
        if (EventBus.hookDisPatcher != null) {
            throw new RuntimeException("hookDisPatcher already set!");
        }
        EventBus.hookDisPatcher = hookDisPatcher;
    }
    // CatServer end
}
