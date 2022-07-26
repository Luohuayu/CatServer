package org.spigotmc;

import catserver.server.CatServer;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;

public class WatchdogThread extends Thread {

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart) {
        super("Spigot Watchdog Thread");
        this.timeoutTime = timeoutTime;
        this.restart = restart;
    }

    private static long monotonicMillis() {
        return System.nanoTime() / 1000000L;
    }

    public static void doStart(int timeoutTime, boolean restart) {
        if (instance == null) {
            instance = new WatchdogThread(timeoutTime * 1000L, restart);
            instance.start();
        }
    }

    public static void tick() {
        instance.lastTick = monotonicMillis();
    }

    public static void doStop() {
        if (instance != null) {
            instance.stopping = true;
        }
    }

    @Override
    public void run() {
        while (!stopping) {
            if (lastTick != 0 && monotonicMillis() > lastTick + timeoutTime) {
                Logger log = Bukkit.getServer().getLogger();
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "The server has stopped responding! This is (probably) not a Spigot bug.");
                log.log(Level.SEVERE, "If you see a plugin in the Server thread dump below, then please report it to that author");
                log.log(Level.SEVERE, "\t *Especially* if it looks like HTTP or MySQL operations are occurring");
                log.log(Level.SEVERE, "If you see a world save or edit, then it means you did far more than your server can handle at once");
                log.log(Level.SEVERE, "\t If this is the case, consider increasing timeout-time in spigot.yml but note that this will replace the crash with LARGE lag spikes");
                // CatServer remove contact us Spigotmc
                log.log(Level.SEVERE, "Spigot version: " + Bukkit.getServer().getVersion());
                // CatServer start
                java.util.Map<Long, Throwable> workerExceptions = catserver.server.utils.WorkerExceptionLogger.getExceptions();
                if (!workerExceptions.isEmpty()) {
                    log.log(Level.SEVERE, "------------------------------");
                    log.log(Level.SEVERE, "Worker thread exceptions:");
                    workerExceptions.forEach((time, exception) -> {
                        log.log(Level.SEVERE, "Time: " + time + ", Cause:" + exception.getMessage());
                        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
                            log.log(Level.SEVERE, stackTraceElement.toString());
                        }
                    });
                }
                // CatServer end
                if (net.minecraft.world.World.lastPhysicsProblem != null) {
                    log.log(Level.SEVERE, "------------------------------");
                    log.log(Level.SEVERE, "During the run of the server, a physics stackoverflow was supressed");
                    log.log(Level.SEVERE, "near " + net.minecraft.world.World.lastPhysicsProblem);
                }
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Server thread dump (Look for plugins here before reporting to CatServer!):"); // CatServer change to CatServer
                dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServer().serverThread.getId(), Integer.MAX_VALUE), log);
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Entire Thread Dump:");
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                for (ThreadInfo thread : threads) {
                    dumpThread(thread, log);
                }
                log.log(Level.SEVERE, "------------------------------");

                if (restart && !MinecraftServer.getServer().hasStopped()) {
                    // CatServer start
                    MinecraftServer.getServer().serverThread.suspend(); // Make sure thread safe
                    new java.util.Timer("WatchdogForceExitTask").schedule(new java.util.TimerTask() {
                        public void run() {
                            Runtime.getRuntime().halt(0);
                        }
                    }, 300 * 1000);
                    if (CatServer.getConfig().forceSaveOnWatchdog) catserver.server.utils.ServerUtils.forceSaveWorlds();
                    // CatServer end
                    RestartCommand.restart();
                }
                break;
            }

            try {
                sleep(10000);
            } catch (InterruptedException ex) {
                interrupt();
            }
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log) {
        log.log(Level.SEVERE, "------------------------------");
        log.log(Level.SEVERE, "Current Thread: " + thread.getThreadName());
        log.log(Level.SEVERE, "\tPID: " + thread.getThreadId() +
                " | Suspended: " + thread.isSuspended() +
                " | Native: " + thread.isInNative() +
                " | State: " + thread.getThreadState());
        if ((thread.getLockedMonitors()).length != 0) {
            log.log(Level.SEVERE, "\tThread is waiting on monitor(s):");
            byte b1;
            int j;
            MonitorInfo[] arrayOfMonitorInfo;
            for (j = (arrayOfMonitorInfo = thread.getLockedMonitors()).length, b1 = 0; b1 < j; ) {
                MonitorInfo monitor = arrayOfMonitorInfo[b1];
                log.log(Level.SEVERE, "\t\tLocked on:" + monitor.getLockedStackFrame());
                b1++;
            }
        }
        log.log(Level.SEVERE, "\tStack:");
        byte b;
        int i;
        StackTraceElement[] arrayOfStackTraceElement;
        for (i = (arrayOfStackTraceElement = thread.getStackTrace()).length, b = 0; b < i; ) {
            StackTraceElement stack = arrayOfStackTraceElement[b];
            log.log(Level.SEVERE, "\t\t" + stack);
            b++;
        }
    }
}
