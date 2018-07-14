// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scheduler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import org.bukkit.plugin.Plugin;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

class CraftFuture<T> extends CraftTask implements Future<T>
{
    private final Callable<T> callable;
    private T value;
    private Exception exception;
    
    CraftFuture(final Callable<T> callable, final Plugin plugin, final int id) {
        super(plugin, null, id, -1L);
        this.exception = null;
        this.callable = callable;
    }
    
    @Override
    public synchronized boolean cancel(final boolean mayInterruptIfRunning) {
        if (this.getPeriod() != -1L) {
            return false;
        }
        this.setPeriod(-2L);
        return true;
    }
    
    @Override
    public boolean isCancelled() {
        return this.getPeriod() == -2L;
    }
    
    @Override
    public boolean isDone() {
        final long period = this.getPeriod();
        return period != -1L && period != -3L;
    }
    
    @Override
    public T get() throws CancellationException, InterruptedException, ExecutionException {
        try {
            return this.get(0L, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException e) {
            throw new Error(e);
        }
    }
    
    @Override
    public synchronized T get(long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        timeout = unit.toMillis(timeout);
        long period = this.getPeriod();
        long timestamp = (timeout > 0L) ? System.currentTimeMillis() : 0L;
        while (period == -1L || period == -3L) {
            this.wait(timeout);
            period = this.getPeriod();
            if (period != -1L && period != -3L) {
                break;
            }
            if (timeout == 0L) {
                continue;
            }
            timeout += timestamp - (timestamp = System.currentTimeMillis());
            if (timeout > 0L) {
                continue;
            }
            throw new TimeoutException();
        }
        if (period == -2L) {
            throw new CancellationException();
        }
        if (period != -4L) {
            throw new IllegalStateException("Expected -1 to -4, got " + period);
        }
        if (this.exception == null) {
            return this.value;
        }
        throw new ExecutionException(this.exception);
    }
    
    @Override
    public void run() {
        synchronized (this) {
            if (this.getPeriod() == -2L) {
                // monitorexit(this)
                return;
            }
            this.setPeriod(-3L);
        }
        try {
            this.value = this.callable.call();
        }
        catch (Exception e) {
            this.exception = e;
            synchronized (this) {
                this.setPeriod(-4L);
                this.notifyAll();
            }
        }
        finally {
            synchronized (this) {
                this.setPeriod(-4L);
                this.notifyAll();
            }
        }
        synchronized (this) {
            this.setPeriod(-4L);
            this.notifyAll();
        }
    }
    
    @Override
    synchronized boolean cancel0() {
        if (this.getPeriod() != -1L) {
            return false;
        }
        this.setPeriod(-2L);
        this.notifyAll();
        return true;
    }
}
