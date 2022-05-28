package catserver.server.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.net.JndiManager;

public class Log4j2_3201_Fixer {
    private final static Pattern REGEX = Pattern.compile("(?i)\\$\\{(jndi|ctx|date|env|event|java|jvmrunargs|log4j|lower|main|map|marker|bundle|sd|sys|upper|):[\\s\\S]*}");

    public static boolean match(String message) {
        if (message != null) {
            return REGEX.matcher(message.replaceAll("\u00a7[a-zA-Z0-9]", "").replaceAll("(\\s|\\n\\r)", "")).find();
        } else {
            return false;
        }
    }

    public static void matchThrowException(String message) throws RuntimeException {
        if (match(message)) {
            throw new RuntimeException("Detected log4j2 3201 bug! Message: " + message.replace("$", "\\u0024"));
        }
    }

    public static boolean matchPrintException(String message) {
        if (match(message)) {
            new RuntimeException("Detected log4j2 3201 bug! Message: " + message.replace("$", "\\u0024")).printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean matchPrintMessage(String message) {
        if (match(message)) {
            System.out.println("Detected log4j2 3201 bug! Message: " + message.replace("$", "\\u0024"));
            return true;
        }
        return false;
    }

    public static void disableJndiLookup() {
        try {
            Field fieldLock = AbstractManager.class.getDeclaredField("LOCK");
            Field fieldNap = AbstractManager.class.getDeclaredField("MAP");
            fieldLock.setAccessible(true);
            fieldNap.setAccessible(true);

            Lock lock = (Lock) fieldLock.get(null);
            Map<String, AbstractManager> map = (Map<String, AbstractManager>) fieldNap.get(null);

            lock.lock();
            map.put(JndiManager.class.getName(), new AbstractManager(null, JndiManager.class.getName()) {
                private final RuntimeException disallowException = new RuntimeException() {
                    public synchronized Throwable fillInStackTrace()
                    {
                        this.setStackTrace(new StackTraceElement[0]);
                        return this;
                    }
                };

                @Override
                public void updateData(Object data) {
                    throw disallowException;
                }
            });
            lock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
