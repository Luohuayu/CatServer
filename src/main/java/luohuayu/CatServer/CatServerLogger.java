package luohuayu.CatServer;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;

public class CatServerLogger {
    public static Logger getLogger(String name) {
        Logger logger=Logger.getLogger(name);
        logger.setUseParentHandlers(false);
        
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new LoggerHander());
        logger.addHandler(handler);
        
        return logger;
    }
}

class LoggerHander extends Formatter {
    public LoggerHander() {
        super();
    }

    public String format(LogRecord record) {
        try {
            MinecraftServer.LOG.log(org.apache.logging.log4j.Level.toLevel(record.getLevel().toString()), record.getMessage());
        }catch (Exception e){
            System.out.println(record.getMessage());
        }
        return "";
    }
}