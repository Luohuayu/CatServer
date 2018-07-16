package luohuayu.CatServer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class CatServerLogger {
    public static Logger getLogger(String name) {
        Logger logger=Logger.getLogger(name);
        logger.setUseParentHandlers(false);
        
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new LoggerHander(name));
        logger.addHandler(handler);
        
        return logger;
    }
}

class LoggerHander extends Formatter {
    private final DateFormat df = new SimpleDateFormat("hh:mm:ss");
    private final String name;
    
    public LoggerHander(String name) {
        this.name=name;
    }
    
    public String format(LogRecord record) {
        return "["+df.format(new Date())+"] ["+name+"/"+record.getLevel()+"]: "+record.getMessage()+"\n";
    }
}