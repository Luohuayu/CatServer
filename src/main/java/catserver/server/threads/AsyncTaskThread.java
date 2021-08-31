package catserver.server.threads;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncTaskThread {
    private static final ExecutorService asyncExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("CatServer Async Task Handler Thread - %1$d").build());

    public static void shutdown() {
        try {
            asyncExecutor.shutdown();
            asyncExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void schedule(Runnable runnable) {
        if (!asyncExecutor.isShutdown() && !asyncExecutor.isTerminated()) {
            asyncExecutor.execute(runnable);
        } else {
            runnable.run();
        }
    }
}
