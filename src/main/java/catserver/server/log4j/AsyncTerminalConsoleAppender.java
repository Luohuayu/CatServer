package catserver.server.log4j;

import net.minecraftforge.server.terminalconsole.TerminalConsoleAppender;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Plugin(name = "Async" + TerminalConsoleAppender.PLUGIN_NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class AsyncTerminalConsoleAppender extends TerminalConsoleAppender {
    private static final BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();

    protected AsyncTerminalConsoleAppender(String name, @Nullable Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void append(LogEvent event) {
        queue.add(getLayout().toSerializable(event));
    }

    public static void flush() {
        Object object;
        while ((object = queue.poll()) != null) {
            write(object);
        }
    }

    public static void writeNextLog() {
        try {
            write(queue.take());
        } catch (final InterruptedException ignored) { }
    }
}
