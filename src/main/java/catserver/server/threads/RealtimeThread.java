package catserver.server.threads;

import net.minecraft.server.MinecraftServer;

public class RealtimeThread extends Thread {
    public static int currentTick = (int) (System.currentTimeMillis() / 50);

    public RealtimeThread() {
        super("RealtimeThread");
    }

    @Override
    public void run() {
        long lastTick = System.nanoTime(), catchupTime = 0, curTime, wait, tickSection = lastTick;
        while (MinecraftServer.getServerInst().isServerRunning()) {
            curTime = System.nanoTime();
            wait = MinecraftServer.TICK_TIME - (curTime - lastTick) - catchupTime;
            if (wait > 0) {
                try {
                    Thread.sleep(wait / 1000000);
                } catch (InterruptedException e) {
                    ;
                }
                catchupTime = 0;
                continue;
            } else {
                catchupTime = Math.min(1000000000, Math.abs(wait));
            }

            currentTick++;

            lastTick = curTime;
        }
    }
}
