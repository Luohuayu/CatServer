// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scheduler;

import java.util.Iterator;
import org.apache.commons.lang.UnhandledException;
import org.bukkit.plugin.Plugin;
import java.util.Map;
import org.bukkit.scheduler.BukkitWorker;
import java.util.LinkedList;

class CraftAsyncTask extends CraftTask
{
    private final LinkedList<BukkitWorker> workers;
    private final Map<Integer, CraftTask> runners;
    
    CraftAsyncTask(final Map<Integer, CraftTask> runners, final Plugin plugin, final Runnable task, final int id, final long delay) {
        super(plugin, task, id, delay);
        this.workers = new LinkedList<BukkitWorker>();
        this.runners = runners;
    }
    
    @Override
    public boolean isSync() {
        return false;
    }
    
    @Override
    public void run() {
        final Thread thread = Thread.currentThread();
        synchronized (this.workers) {
            if (this.getPeriod() == -2L) {
                // monitorexit(this.workers)
                return;
            }
            this.workers.add(new BukkitWorker() {
                @Override
                public Thread getThread() {
                    return thread;
                }
                
                @Override
                public int getTaskId() {
                    return CraftAsyncTask.this.getTaskId();
                }
                
                @Override
                public Plugin getOwner() {
                    return CraftAsyncTask.this.getOwner();
                }
            });
        }
        // monitorexit(this.workers)
        Throwable thrown = null;
        try {
            super.run();
        }
        catch (Throwable t) {
            thrown = t;
            throw new UnhandledException(String.format("Plugin %s generated an exception while executing task %s", this.getOwner().getDescription().getFullName(), this.getTaskId()), thrown);
        }
        finally {
            synchronized (this.workers) {
                try {
                    final Iterator<BukkitWorker> workers = this.workers.iterator();
                    boolean removed = false;
                    while (workers.hasNext()) {
                        if (workers.next().getThread() == thread) {
                            workers.remove();
                            removed = true;
                            break;
                        }
                    }
                    if (!removed) {
                        throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getDescription().getFullName()), thrown);
                    }
                }
                finally {
                    if (this.getPeriod() < 0L && this.workers.isEmpty()) {
                        this.runners.remove(this.getTaskId());
                    }
                }
                if (this.getPeriod() < 0L && this.workers.isEmpty()) {
                    this.runners.remove(this.getTaskId());
                }
            }
            // monitorexit(this.workers)
        }
        synchronized (this.workers) {
            try {
                final Iterator<BukkitWorker> workers = this.workers.iterator();
                boolean removed = false;
                while (workers.hasNext()) {
                    if (workers.next().getThread() == thread) {
                        workers.remove();
                        removed = true;
                        break;
                    }
                }
                if (!removed) {
                    throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getDescription().getFullName()), thrown);
                }
            }
            finally {
                if (this.getPeriod() < 0L && this.workers.isEmpty()) {
                    this.runners.remove(this.getTaskId());
                }
            }
            if (this.getPeriod() < 0L && this.workers.isEmpty()) {
                this.runners.remove(this.getTaskId());
            }
        }
        // monitorexit(this.workers)
    }
    
    LinkedList<BukkitWorker> getWorkers() {
        return this.workers;
    }
    
    @Override
    boolean cancel0() {
        synchronized (this.workers) {
            this.setPeriod(-2L);
            if (this.workers.isEmpty()) {
                this.runners.remove(this.getTaskId());
            }
        }
        // monitorexit(this.workers)
        return true;
    }
}
