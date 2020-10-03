package catserver.server.utils;

import com.mojang.util.QueueLogAppender;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;

public class Log4jUitls {
    public static void flush() {
        try {
            Field filed_QUEUES = QueueLogAppender.class.getDeclaredField("QUEUES");
            filed_QUEUES.setAccessible(true);

            Field filed_QUEUE_LOCK = QueueLogAppender.class.getDeclaredField("QUEUE_LOCK");
            filed_QUEUE_LOCK.setAccessible(true);

            Map<String, BlockingQueue<String>> QUEUES = (Map<String, BlockingQueue<String>>) filed_QUEUES.get(null);
            ReadWriteLock QUEUE_LOCK = (ReadWriteLock) filed_QUEUE_LOCK.get(null);

            QUEUE_LOCK.readLock().lock();
            final BlockingQueue<String> queue = QUEUES.get("TerminalConsole");
            if (queue != null) {
                String message;
                while ((message = queue.poll()) != null) {
                    System.out.write(message.getBytes());
                    System.out.flush();
                }
            }
            QUEUE_LOCK.readLock().unlock();
        } catch (Exception ignored) { }
    }
}
