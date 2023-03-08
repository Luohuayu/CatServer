package catserver.server.threads;

import catserver.server.CatServer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncChatThread {
    private static final ExecutorService executors = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat( "Async Chat Thread - #%d" ).build());

    public static void shutdown() {
        try {
            executors.shutdown();
            if (!executors.awaitTermination(10, TimeUnit.SECONDS)) {
                CatServer.log.warn("An AsyncChatThread shutdown timed out! (Is there a plugin to register PlayerChatEvent?)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void chat(NetHandlerPlayServer connection, String s) {
        Runnable runnable = () -> connection.chat(s, true);
        if (!executors.isShutdown() && !executors.isTerminated()) {
            executors.execute(runnable);
        } else {
            runnable.run();
        }
    }
}
