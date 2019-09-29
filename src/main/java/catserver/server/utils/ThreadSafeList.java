package catserver.server.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;
import org.bukkit.Bukkit;
import org.spigotmc.AsyncCatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.Predicate;

public class ThreadSafeList<E> extends Vector<E> {
    private static final String message = "插件/MOD尝试异步操作List已拦截,请与插件/MOD作者反馈!";
    private final boolean print;

    public ThreadSafeList(boolean print) {
        this.print = print;
    }

    @Override
    public boolean add(E e) {
        if (checkThread()) {
            switchPrimaryThread(() -> super.add(e));
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return true;
        }
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        if (checkThread()) {
            switchPrimaryThread(() -> super.add(index, element));
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return;
        }
        super.add(index, element);
    }

    @Override
    public boolean remove(Object o) {
        if (checkThread()) {
            switchPrimaryThread(() -> super.remove(o));
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return super.contains(o);
        }
        return super.remove(o);
    }

    @Override
    public synchronized E remove(int index) {
        if (checkThread()) {
            E removeE = super.get(index);
            switchPrimaryThread(() -> super.remove(removeE));
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return removeE;
        }
        return super.remove(index);
    }

    @Override
    public void clear() {
        if (checkThread()) {
            switchPrimaryThread(super::clear);
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return;
        }
        super.clear();
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> c) {
        if (checkThread()) {
            switchPrimaryThread(() -> super.addAll(c));
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return true;
        }
        return super.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends E> c) {
        if (checkThread()) {
            switchPrimaryThread(() -> super.addAll(index, c));
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return true;
        }
        return super.addAll(index, c);
    }

    @Override
    public synchronized void addElement(E obj) {
        if (checkThread()) {
            switchPrimaryThread(() -> super.addElement(obj));
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return;
        }
        super.addElement(obj);
    }

    @Override
    public synchronized void removeElementAt(int index) {
        if (checkThread()) {
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return;
        }
        super.removeElementAt(index);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        if (checkThread()) {
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return false;
        }
        return super.removeAll(c);
    }

    @Override
    public synchronized void removeAllElements() {
        if (checkThread()) {
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return;
        }
        super.removeAllElements();
    }

    @Override
    public synchronized boolean removeElement(Object obj) {
        if (checkThread()) {
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return false;
        }
        return super.removeElement(obj);
    }

    @Override
    public synchronized boolean removeIf(Predicate<? super E> filter) {
        if (checkThread()) {
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return false;
        }
        return super.removeIf(filter);
    }

    @Override
    public synchronized Iterator<E> iterator() {
        if (checkThread()) {
            if (print)
                FMLLog.log.debug(new UnsupportedOperationException(message));
            return new ArrayList<E>(this).iterator();
        }
        return super.iterator();
    }

    private boolean checkThread() {
        return AsyncCatcher.enabled && !Bukkit.isPrimaryThread();
    }

    private void switchPrimaryThread(Runnable runnable) {
        MinecraftServer.getServerInst().addScheduledTask(runnable);
    }
}
