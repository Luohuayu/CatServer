package catserver.server.threads;

import net.minecraft.server.MinecraftServer;

public class RealtimeThread extends Thread {
    public static final RealtimeThread INSTANCE = new RealtimeThread();
    public static int currentTick = 0;

    private RealtimeThread() {
        super("Realtime Thread");
    }

    @Override
    public void run() {
        long lastTick = System.nanoTime(), curTime, wait;
        while (MinecraftServer.getServerInst().isServerRunning()) {
            curTime = System.nanoTime();
            wait = MinecraftServer.TICK_TIME - (curTime - lastTick);
            if (wait > 0) {
                try {
                    Thread.sleep(wait / 1000000);
                } catch (InterruptedException e) {
                    ;
                }
                continue;
            }

            currentTick++;

            lastTick = curTime;
        }
    }
}
