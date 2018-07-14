// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.util.concurrent.ExecutionException;

public abstract class Waitable<T> implements Runnable
{
    Throwable t;
    T value;
    Status status;
    
    public Waitable() {
        this.t = null;
        this.value = null;
        this.status = Status.WAITING;
    }
    
    @Override
    public final void run() {
        synchronized (this) {
            if (this.status != Status.WAITING) {
                throw new IllegalStateException("Invalid state " + this.status);
            }
            this.status = Status.RUNNING;
        }
        try {
            this.value = this.evaluate();
        }
        catch (Throwable t) {
            this.t = t;
            synchronized (this) {
                this.status = Status.FINISHED;
                this.notifyAll();
            }
        }
        finally {
            synchronized (this) {
                this.status = Status.FINISHED;
                this.notifyAll();
            }
        }
        synchronized (this) {
            this.status = Status.FINISHED;
            this.notifyAll();
        }
    }
    
    protected abstract T evaluate();
    
    public synchronized T get() throws InterruptedException, ExecutionException {
        while (this.status != Status.FINISHED) {
            this.wait();
        }
        if (this.t != null) {
            throw new ExecutionException(this.t);
        }
        return this.value;
    }
    
    private enum Status
    {
        WAITING("WAITING", 0), 
        RUNNING("RUNNING", 1), 
        FINISHED("FINISHED", 2);
        
        private Status(final String s, final int n) {
        }
    }
}
