package org.bukkit.craftbukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.SpigotTimings; // Spigot
import org.spigotmc.CustomTimingsHandler; // Spigot
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


public class CraftTask implements BukkitTask, Runnable { // Spigot

    private volatile CraftTask next = null;
    public static final int ERROR = 0;
    public static final int NO_REPEATING = -1;
    public static final int CANCEL = -2;
    public static final int PROCESS_FOR_FUTURE = -3;
    public static final int DONE_FOR_FUTURE = -4;
    /**
     * -1 means no repeating <br>
     * -2 means cancel <br>
     * -3 means processing for Future <br>
     * -4 means done for Future <br>
     * Never 0 <br>
     * >0 means number of ticks to wait between each execution
     */
    private volatile long period;
    private long nextRun;
    private final Runnable task;
    private final Plugin plugin;
    private final int id;

    final CustomTimingsHandler timings; // Spigot
    CraftTask() {
        this(null, null, CraftTask.NO_REPEATING, CraftTask.NO_REPEATING);
    }

    CraftTask(final Runnable task) {
        this(null, task, CraftTask.NO_REPEATING, CraftTask.NO_REPEATING);
    }

    // Spigot start
    public String timingName = null;
    CraftTask(String timingName) {
        this(timingName, null, null, -1, -1);
    }
    CraftTask(String timingName, final Runnable task) {
        this(timingName, null, task, -1, -1);
    }
    CraftTask(String timingName, final Plugin plugin, final Runnable task, final int id, final long period) {
        this.plugin = plugin;
        this.task = task;
        this.id = id;
        this.period = period;
        this.timingName = timingName == null && task == null ? "Unknown" : timingName;
        timings = this.isSync() ? SpigotTimings.getPluginTaskTimings(this, period) : null;
    }

    CraftTask(final Plugin plugin, final Runnable task, final int id, final long period) {
        this(null, plugin, task, id, period);
    // Spigot end
    }

    public final int getTaskId() {
        return id;
    }

    public final Plugin getOwner() {
        return plugin;
    }

    public boolean isSync() {
        return true;
    }

    public void run() {
        task.run();
    }

    long getPeriod() {
        return period;
    }

    void setPeriod(long period) {
        this.period = period;
    }

    long getNextRun() {
        return nextRun;
    }

    void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    CraftTask getNext() {
        return next;
    }

    void setNext(CraftTask next) {
        this.next = next;
    }

    Class<? extends Runnable> getTaskClass() {
        return task.getClass();
    }

    @Override
    public boolean isCancelled() {
        return (period == CraftTask.CANCEL);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

    /**
     * This method properly sets the status to cancelled, synchronizing when required.
     *
     * @return false if it is a craft future task that has already begun execution, true otherwise
     */
    boolean cancel0() {
        setPeriod(CraftTask.CANCEL);
        return true;
    }

    // Spigot start
    public String getTaskName() {
        if (timingName != null) {
            return timingName;
        }
        return task.getClass().getName();
    }
    // Spigot end
}
