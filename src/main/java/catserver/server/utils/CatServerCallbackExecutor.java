package catserver.server.utils;

import java.util.concurrent.Executor;

public class CatServerCallbackExecutor implements Executor, Runnable {
    private Runnable queued;

    @Override
    public void execute(Runnable runnable) {
        if (queued != null) {
            throw new IllegalStateException("Already queued");
        }
        queued = runnable;
    }

    @Override
    public void run() {
        Runnable task = queued;
        queued = null;
        if (task != null) {
            task.run();
        }
    }
}
