package catserver.server.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ConcurrentMap;

public class WorkerExceptionLogger {
    private final static Cache<Long, Throwable> exceptionCache = CacheBuilder.newBuilder().initialCapacity(1).maximumSize(64).build();

    public static void addException(Throwable throwable) {
        exceptionCache.put(System.currentTimeMillis(), throwable);
    }

    public static ConcurrentMap<Long, Throwable> getExceptions() {
        return exceptionCache.asMap();
    }
}
