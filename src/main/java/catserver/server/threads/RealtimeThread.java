package catserver.server.threads;

import net.minecraft.server.MinecraftServer;

public class RealtimeThread extends Thread {
    public static int currentTick = (int) (System.currentTimeMillis() / 50);

    public RealtimeThread() {
        super("RealtimeThread");
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
