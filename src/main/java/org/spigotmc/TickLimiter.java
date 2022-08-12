package org.spigotmc;

public class TickLimiter {
    private final int maxTime;
    private long startTime;

    public TickLimiter(int maxTime) {
        this.maxTime = maxTime;
    }

    public void initTick() {
        startTime = System.currentTimeMillis();
    }

    public boolean shouldContinue() {
        long remaining = System.currentTimeMillis() - startTime;
        return remaining < maxTime;
    }
}
