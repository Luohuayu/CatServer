// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import java.util.logging.ConsoleHandler;

public class ForwardLogHandler extends ConsoleHandler
{
    private Map<String, Logger> cachedLoggers;
    
    public ForwardLogHandler() {
        this.cachedLoggers = new ConcurrentHashMap<String, Logger>();
    }
    
    private Logger getLogger(final String name) {
        Logger logger = this.cachedLoggers.get(name);
        if (logger == null) {
            logger = LogManager.getLogger(name);
            this.cachedLoggers.put(name, logger);
        }
        return logger;
    }
    
    @Override
    public void publish(final LogRecord record) {
        final Logger logger = this.getLogger(String.valueOf(record.getLoggerName()));
        final Throwable exception = record.getThrown();
        final Level level = record.getLevel();
        final String message = this.getFormatter().formatMessage(record);
        if (level == Level.SEVERE) {
            logger.error(message, exception);
        }
        else if (level == Level.WARNING) {
            logger.warn(message, exception);
        }
        else if (level == Level.INFO) {
            logger.info(message, exception);
        }
        else if (level == Level.CONFIG) {
            logger.debug(message, exception);
        }
        else {
            logger.trace(message, exception);
        }
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public void close() throws SecurityException {
    }
}
