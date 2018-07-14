// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scheduler;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.IllegalPluginAccessException;
import java.util.logging.Level;
import java.util.Collection;
import org.bukkit.scheduler.BukkitWorker;
import org.apache.commons.lang.Validate;
import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.Map;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.Plugin;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.scheduler.BukkitScheduler;

public class CraftScheduler implements BukkitScheduler
{
    private final AtomicInteger ids;
    private volatile CraftTask head;
    private final AtomicReference<CraftTask> tail;
    private final PriorityQueue<CraftTask> pending;
    private final List<CraftTask> temp;
    private final ConcurrentHashMap<Integer, CraftTask> runners;
    private volatile int currentTick;
    private final Executor executor;
    private CraftAsyncDebugger debugHead;
    private CraftAsyncDebugger debugTail;
    private static final int RECENT_TICKS;
    
    static {
        RECENT_TICKS = 30;
    }
    
    public CraftScheduler() {
        this.ids = new AtomicInteger(1);
        this.head = new CraftTask();
        this.tail = new AtomicReference<CraftTask>(this.head);
        this.pending = new PriorityQueue<CraftTask>(10, new Comparator<CraftTask>() {
            @Override
            public int compare(final CraftTask o1, final CraftTask o2) {
                return (int)(o1.getNextRun() - o2.getNextRun());
            }
        });
        this.temp = new ArrayList<CraftTask>();
        this.runners = new ConcurrentHashMap<Integer, CraftTask>();
        this.currentTick = -1;
        this.executor = Executors.newCachedThreadPool();
        this.debugHead = new CraftAsyncDebugger(-1, (Plugin)null, (Class)null) {
            @Override
            StringBuilder debugTo(final StringBuilder string) {
                return string;
            }
        };
        this.debugTail = this.debugHead;
    }
    
    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final Runnable task) {
        return this.scheduleSyncDelayedTask(plugin, task, 0L);
    }
    
    @Override
    public BukkitTask runTask(final Plugin plugin, final Runnable runnable) {
        return this.runTaskLater(plugin, runnable, 0L);
    }
    
    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(final Plugin plugin, final Runnable task) {
        return this.scheduleAsyncDelayedTask(plugin, task, 0L);
    }
    
    @Override
    public BukkitTask runTaskAsynchronously(final Plugin plugin, final Runnable runnable) {
        return this.runTaskLaterAsynchronously(plugin, runnable, 0L);
    }
    
    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final Runnable task, final long delay) {
        return this.scheduleSyncRepeatingTask(plugin, task, delay, -1L);
    }
    
    @Override
    public BukkitTask runTaskLater(final Plugin plugin, final Runnable runnable, final long delay) {
        return this.runTaskTimer(plugin, runnable, delay, -1L);
    }
    
    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(final Plugin plugin, final Runnable task, final long delay) {
        return this.scheduleAsyncRepeatingTask(plugin, task, delay, -1L);
    }
    
    @Override
    public BukkitTask runTaskLaterAsynchronously(final Plugin plugin, final Runnable runnable, final long delay) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, -1L);
    }
    
    @Override
    public int scheduleSyncRepeatingTask(final Plugin plugin, final Runnable runnable, final long delay, final long period) {
        return this.runTaskTimer(plugin, runnable, delay, period).getTaskId();
    }
    
    @Override
    public BukkitTask runTaskTimer(final Plugin plugin, final Runnable runnable, long delay, long period) {
        validate(plugin, runnable);
        if (delay < 0L) {
            delay = 0L;
        }
        if (period == 0L) {
            period = 1L;
        }
        else if (period < -1L) {
            period = -1L;
        }
        return this.handle(new CraftTask(plugin, runnable, this.nextId(), period), delay);
    }
    
    @Deprecated
    @Override
    public int scheduleAsyncRepeatingTask(final Plugin plugin, final Runnable runnable, final long delay, final long period) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
    }
    
    @Override
    public BukkitTask runTaskTimerAsynchronously(final Plugin plugin, final Runnable runnable, long delay, long period) {
        validate(plugin, runnable);
        if (delay < 0L) {
            delay = 0L;
        }
        if (period == 0L) {
            period = 1L;
        }
        else if (period < -1L) {
            period = -1L;
        }
        return this.handle(new CraftAsyncTask(this.runners, plugin, runnable, this.nextId(), period), delay);
    }
    
    @Override
    public <T> Future<T> callSyncMethod(final Plugin plugin, final Callable<T> task) {
        validate(plugin, task);
        final CraftFuture<T> future = new CraftFuture<T>(task, plugin, this.nextId());
        this.handle(future, 0L);
        return future;
    }
    
    @Override
    public void cancelTask(final int taskId) {
        if (taskId <= 0) {
            return;
        }
        CraftTask task = this.runners.get(taskId);
        if (task != null) {
            task.cancel0();
        }
        task = new CraftTask(new Runnable() {
            @Override
            public void run() {
                if (!this.check(CraftScheduler.this.temp)) {
                    this.check(CraftScheduler.this.pending);
                }
            }
            
            private boolean check(final Iterable<CraftTask> collection) {
                final Iterator<CraftTask> tasks = collection.iterator();
                while (tasks.hasNext()) {
                    final CraftTask task = tasks.next();
                    if (task.getTaskId() == taskId) {
                        task.cancel0();
                        tasks.remove();
                        if (task.isSync()) {
                            CraftScheduler.this.runners.remove(taskId);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        this.handle(task, 0L);
        for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                return;
            }
            if (taskPending.getTaskId() == taskId) {
                taskPending.cancel0();
            }
        }
    }
    
    @Override
    public void cancelTasks(final Plugin plugin) {
        Validate.notNull((Object)plugin, "Cannot cancel tasks of null plugin");
        final CraftTask task = new CraftTask(new Runnable() {
            @Override
            public void run() {
                this.check(CraftScheduler.this.pending);
                this.check(CraftScheduler.this.temp);
            }
            
            void check(final Iterable<CraftTask> collection) {
                final Iterator<CraftTask> tasks = collection.iterator();
                while (tasks.hasNext()) {
                    final CraftTask task = tasks.next();
                    if (task.getOwner().equals(plugin)) {
                        task.cancel0();
                        tasks.remove();
                        if (!task.isSync()) {
                            continue;
                        }
                        CraftScheduler.this.runners.remove(task.getTaskId());
                    }
                }
            }
        });
        this.handle(task, 0L);
        for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                return;
            }
            if (taskPending.getTaskId() != -1 && taskPending.getOwner().equals(plugin)) {
                taskPending.cancel0();
            }
        }
        for (final CraftTask runner : this.runners.values()) {
            if (runner.getOwner().equals(plugin)) {
                runner.cancel0();
            }
        }
    }
    
    @Override
    public void cancelAllTasks() {
        final CraftTask task = new CraftTask(new Runnable() {
            @Override
            public void run() {
                final Iterator<CraftTask> it = CraftScheduler.this.runners.values().iterator();
                while (it.hasNext()) {
                    final CraftTask task = it.next();
                    task.cancel0();
                    if (task.isSync()) {
                        it.remove();
                    }
                }
                CraftScheduler.this.pending.clear();
                CraftScheduler.this.temp.clear();
            }
        });
        this.handle(task, 0L);
        for (CraftTask taskPending = this.head.getNext(); taskPending != null && taskPending != task; taskPending = taskPending.getNext()) {
            taskPending.cancel0();
        }
        for (final CraftTask runner : this.runners.values()) {
            runner.cancel0();
        }
    }
    
    @Override
    public boolean isCurrentlyRunning(final int taskId) {
        final CraftTask task = this.runners.get(taskId);
        if (task == null || task.isSync()) {
            return false;
        }
        final CraftAsyncTask asyncTask = (CraftAsyncTask)task;
        synchronized (asyncTask.getWorkers()) {
            // monitorexit(asyncTask.getWorkers())
            return asyncTask.getWorkers().isEmpty();
        }
    }
    
    @Override
    public boolean isQueued(final int taskId) {
        if (taskId <= 0) {
            return false;
        }
        for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() == taskId) {
                return task.getPeriod() >= -1L;
            }
        }
        CraftTask task = this.runners.get(taskId);
        return task != null && task.getPeriod() >= -1L;
    }
    
    @Override
    public List<BukkitWorker> getActiveWorkers() {
        final ArrayList<BukkitWorker> workers = new ArrayList<BukkitWorker>();
        for (final CraftTask taskObj : this.runners.values()) {
            if (taskObj.isSync()) {
                continue;
            }
            final CraftAsyncTask task = (CraftAsyncTask)taskObj;
            synchronized (task.getWorkers()) {
                workers.addAll(task.getWorkers());
            }
            // monitorexit(task.getWorkers())
        }
        return workers;
    }
    
    @Override
    public List<BukkitTask> getPendingTasks() {
        final ArrayList<CraftTask> truePending = new ArrayList<CraftTask>();
        for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() != -1) {
                truePending.add(task);
            }
        }
        final ArrayList<BukkitTask> pending = new ArrayList<BukkitTask>();
        for (final CraftTask task2 : this.runners.values()) {
            if (task2.getPeriod() >= -1L) {
                pending.add(task2);
            }
        }
        for (final CraftTask task2 : truePending) {
            if (task2.getPeriod() >= -1L && !pending.contains(task2)) {
                pending.add(task2);
            }
        }
        return pending;
    }
    
    public void mainThreadHeartbeat(final int currentTick) {
        this.currentTick = currentTick;
        final List<CraftTask> temp = this.temp;
        this.parsePending();
        while (this.isReady(currentTick)) {
            final CraftTask task = this.pending.remove();
            if (task.getPeriod() < -1L) {
                if (task.isSync()) {
                    this.runners.remove(task.getTaskId(), task);
                }
                this.parsePending();
            }
            else {
                if (task.isSync()) {
                    try {
                        task.run();
                    }
                    catch (Throwable throwable) {
                        task.getOwner().getLogger().log(Level.WARNING, String.format("Task #%s for %s generated an exception", task.getTaskId(), task.getOwner().getDescription().getFullName()), throwable);
                    }
                    this.parsePending();
                }
                else {
                    this.debugTail = this.debugTail.setNext(new CraftAsyncDebugger(currentTick + CraftScheduler.RECENT_TICKS, task.getOwner(), task.getTaskClass()));
                    this.executor.execute(task);
                }
                final long period = task.getPeriod();
                if (period > 0L) {
                    task.setNextRun(currentTick + period);
                    temp.add(task);
                }
                else {
                    if (!task.isSync()) {
                        continue;
                    }
                    this.runners.remove(task.getTaskId());
                }
            }
        }
        this.pending.addAll(temp);
        temp.clear();
        this.debugHead = this.debugHead.getNextHead(currentTick);
    }
    
    private void addTask(final CraftTask task) {
        AtomicReference<CraftTask> tail;
        CraftTask tailTask;
        for (tail = this.tail, tailTask = tail.get(); !tail.compareAndSet(tailTask, task); tailTask = tail.get()) {}
        tailTask.setNext(task);
    }
    
    private CraftTask handle(final CraftTask task, final long delay) {
        task.setNextRun(this.currentTick + delay);
        this.addTask(task);
        return task;
    }
    
    private static void validate(final Plugin plugin, final Object task) {
        Validate.notNull((Object)plugin, "Plugin cannot be null");
        Validate.notNull(task, "Task cannot be null");
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }
    }
    
    private int nextId() {
        return this.ids.incrementAndGet();
    }
    
    private void parsePending() {
        CraftTask head = this.head;
        CraftTask task = head.getNext();
        CraftTask lastTask = head;
        while (task != null) {
            if (task.getTaskId() == -1) {
                task.run();
            }
            else if (task.getPeriod() >= -1L) {
                this.pending.add(task);
                this.runners.put(task.getTaskId(), task);
            }
            task = (lastTask = task).getNext();
        }
        for (task = head; task != lastTask; task = head) {
            head = task.getNext();
            task.setNext(null);
        }
        this.head = lastTask;
    }
    
    private boolean isReady(final int currentTick) {
        return !this.pending.isEmpty() && this.pending.peek().getNextRun() <= currentTick;
    }
    
    @Override
    public String toString() {
        final int debugTick = this.currentTick;
        final StringBuilder string = new StringBuilder("Recent tasks from ").append(debugTick - CraftScheduler.RECENT_TICKS).append('-').append(debugTick).append('{');
        this.debugHead.debugTo(string);
        return string.append('}').toString();
    }
    
    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final BukkitRunnable task, final long delay) {
        return this.scheduleSyncDelayedTask(plugin, (Runnable)task, delay);
    }
    
    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(final Plugin plugin, final BukkitRunnable task) {
        return this.scheduleSyncDelayedTask(plugin, (Runnable)task);
    }
    
    @Deprecated
    @Override
    public int scheduleSyncRepeatingTask(final Plugin plugin, final BukkitRunnable task, final long delay, final long period) {
        return this.scheduleSyncRepeatingTask(plugin, (Runnable)task, delay, period);
    }
    
    @Deprecated
    @Override
    public BukkitTask runTask(final Plugin plugin, final BukkitRunnable task) throws IllegalArgumentException {
        return this.runTask(plugin, (Runnable)task);
    }
    
    @Deprecated
    @Override
    public BukkitTask runTaskAsynchronously(final Plugin plugin, final BukkitRunnable task) throws IllegalArgumentException {
        return this.runTaskAsynchronously(plugin, (Runnable)task);
    }
    
    @Deprecated
    @Override
    public BukkitTask runTaskLater(final Plugin plugin, final BukkitRunnable task, final long delay) throws IllegalArgumentException {
        return this.runTaskLater(plugin, (Runnable)task, delay);
    }
    
    @Deprecated
    @Override
    public BukkitTask runTaskLaterAsynchronously(final Plugin plugin, final BukkitRunnable task, final long delay) throws IllegalArgumentException {
        return this.runTaskLaterAsynchronously(plugin, (Runnable)task, delay);
    }
    
    @Deprecated
    @Override
    public BukkitTask runTaskTimer(final Plugin plugin, final BukkitRunnable task, final long delay, final long period) throws IllegalArgumentException {
        return this.runTaskTimer(plugin, (Runnable)task, delay, period);
    }
    
    @Deprecated
    @Override
    public BukkitTask runTaskTimerAsynchronously(final Plugin plugin, final BukkitRunnable task, final long delay, final long period) throws IllegalArgumentException {
        return this.runTaskTimerAsynchronously(plugin, (Runnable)task, delay, period);
    }
}
