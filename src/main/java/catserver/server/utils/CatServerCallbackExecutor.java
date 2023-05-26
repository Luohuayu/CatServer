package catserver.server.utils;

import java.util.LinkedList;
import java.util.concurrent.Executor;

public class CatServerCallbackExecutor implements Executor, Runnable {
    private final LinkedList<Runnable> queued = new LinkedList<>();

    @Override
    public void execute(Runnable runnable) {
        queued.add(runnable);
    }

    @Override
    public void run() {
        Runnable task;
        while ((task = queued.pollFirst()) != null) {
            task.run();
        }
    }
}
