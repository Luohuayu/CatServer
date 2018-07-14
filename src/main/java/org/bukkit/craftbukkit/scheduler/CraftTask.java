// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.CustomTimingsHandler;

public class CraftTask implements BukkitTask, Runnable
{
    private volatile CraftTask next;
    private volatile long period;
    private long nextRun;
    private final Runnable task;
    private final Plugin plugin;
    private final int id;
    public CustomTimingsHandler timings;
    public String timingName;

    CraftTask() {
        this(null, null, -1, -1L);
    }
    
    CraftTask(final Runnable task) {
        this(null, task, -1, -1L);
    }

    CraftTask(final String timingName) {
        this(timingName, null, null, -1, -1L);
    }

    CraftTask(final String timingName, final Runnable task) {
        this(timingName, null, task, -1, -1L);
    }
    
    CraftTask(final String timingName, final Plugin plugin, final Runnable task, final int id, final long period) {
        this.next = null;
        this.plugin = plugin;
        this.task = task;
        this.id = id;
        this.period = period;
        this.timingName = ((timingName == null && task == null) ? "Unknown" : timingName);
        this.timings = (this.isSync() ? org.bukkit.craftbukkit.SpigotTimings.getPluginTaskTimings(this, period) : null);
    }

    CraftTask(final Plugin plugin, final Runnable task, final int id, final long period) {
        this(null, plugin, task, id, period);
    }
    
    @Override
    public final int getTaskId() {
        return this.id;
    }
    
    @Override
    public final Plugin getOwner() {
        return this.plugin;
    }
    
    @Override
    public boolean isSync() {
        return true;
    }
    
    @Override
    public void run() {
        this.task.run();
    }
    
    long getPeriod() {
        return this.period;
    }
    
    void setPeriod(final long period) {
        this.period = period;
    }
    
    long getNextRun() {
        return this.nextRun;
    }
    
    void setNextRun(final long nextRun) {
        this.nextRun = nextRun;
    }
    
    CraftTask getNext() {
        return this.next;
    }
    
    void setNext(final CraftTask next) {
        this.next = next;
    }
    
    Class<? extends Runnable> getTaskClass() {
        return this.task.getClass();
    }
    
    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.id);
    }
    
    boolean cancel0() {
        this.setPeriod(-2L);
        return true;
    }

    public String getTaskName() {
        if (this.timingName != null) {
            return this.timingName;
        }
        return this.task.getClass().getName();
    }
}
