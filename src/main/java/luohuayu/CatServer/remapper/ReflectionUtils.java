package luohuayu.CatServer.remapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Based on Apache's ReflectionUtil
 * @author Maxqia
 */
public class ReflectionUtils {

    private static final Method GET_CALLER_CLASS;
    private static final int JDK_7u25_OFFSET;

    static {
        try {
            int jdkOffset = 0;
            final Class<?> reflection = Class.forName("sun.reflect.Reflection");
            Method getCallerClass = reflection.getMethod("getCallerClass", int.class);

            Object test = getCallerClass.invoke(null, 1);
            if (test.equals(reflection)) {
                System.out.println("WARNING : You are using Java 1.7.0_25 which has a broken implementation of Reflection.getCallerClass.");
                System.out.println("WARNING : You should upgrade to at least Java 1.7.0_40 or later.");
                jdkOffset = 1;
            }

            GET_CALLER_CLASS = getCallerClass;
            JDK_7u25_OFFSET = jdkOffset;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("getCallerClass doesn't exist!");
        }
    }

    public static Class<?> getCallerClass(int skip) {
        if (GET_CALLER_CLASS != null) {
            try {
                return (Class<?>) GET_CALLER_CLASS.invoke(null, skip + 1 + JDK_7u25_OFFSET);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e.getMessage());
            }
        }
        throw new RuntimeException("getCallerClass doesn't exist!");
    }

    public static ClassLoader getCallerClassloader() {
        return ReflectionUtils.getCallerClass(3).getClassLoader(); // added one due to it being the caller of the caller;
    }
}
