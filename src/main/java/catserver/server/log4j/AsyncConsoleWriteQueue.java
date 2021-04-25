package catserver.server.log4j;

import net.minecraftforge.server.terminalconsole.TerminalConsoleAppender;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncConsoleWriteQueue implements Runnable {
    public static boolean enable = false;

    private static final int MAX_CAPACITY = 250;
    private static final BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();

    public static void addLogToQueue(Object object) {
        if (queue.size() >= MAX_CAPACITY) {
            queue.clear();
        }
        queue.add(object);
    }

    public static void flush() {
        Object object;
        while ((object = queue.poll()) != null) {
            TerminalConsoleAppender.write(object);
        }
    }

    public AsyncConsoleWriteQueue() {
        enable = true;
    }

    public void run() {
        Object object;

        while (true) {
            try {
                try {
                    object = queue.take();
                } catch (InterruptedException ignored) {
                    object = null;
                }

                if (object == null) continue;

                TerminalConsoleAppender.write(object);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
}
