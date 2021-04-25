package catserver.server.threads;

import catserver.server.log4j.AsyncConsoleWriteQueue;

public class ConsoleWriteThread extends Thread {
    private static final ConsoleWriteThread CONSOLE_WRITE_THREAD = new ConsoleWriteThread();

    protected ConsoleWriteThread() {
        super(new AsyncConsoleWriteQueue(), "CatServer Console Write Thread");
    }

    public static void startThread() {
        CONSOLE_WRITE_THREAD.start();
    }
}
