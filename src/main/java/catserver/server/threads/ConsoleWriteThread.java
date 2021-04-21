package catserver.server.threads;

import catserver.server.log4j.AsyncConsoleWriteQueue;

public class ConsoleWriteThread extends Thread {
    public ConsoleWriteThread() {
        super(new AsyncConsoleWriteQueue(), "CatServer Console Write Thread");
    }
}
