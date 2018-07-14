// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

import joptsimple.OptionException;
import joptsimple.OptionSet;
//import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
//import org.bukkit.craftbukkit.libs.joptsimple.OptionException;
import net.minecraft.server.MinecraftServer;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;

public class ShortConsoleLogFormatter extends Formatter
{
    private final SimpleDateFormat date;
    
    public ShortConsoleLogFormatter(final MinecraftServer server) {
        final OptionSet options = server.options;
        SimpleDateFormat date = null;
        if (options.has("date-format")) {
            try {
                final Object object = options.valueOf("date-format");
                if (object != null && object instanceof SimpleDateFormat) {
                    date = (SimpleDateFormat)object;
                }
            }
            catch (OptionException ex) {
                System.err.println("Given date format is not valid. Falling back to default.");
            }
        }
        else if (options.has("nojline")) {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        if (date == null) {
            date = new SimpleDateFormat("HH:mm:ss");
        }
        this.date = date;
    }
    
    @Override
    public String format(final LogRecord record) {
        final StringBuilder builder = new StringBuilder();
        final Throwable ex = record.getThrown();
        builder.append(this.date.format(record.getMillis()));
        builder.append(" [");
        builder.append(record.getLevel().getLocalizedName().toUpperCase());
        builder.append("] ");
        builder.append(this.formatMessage(record));
        builder.append('\n');
        if (ex != null) {
            final StringWriter writer = new StringWriter();
            ex.printStackTrace(new PrintWriter(writer));
            builder.append(writer);
        }
        return builder.toString();
    }
}
