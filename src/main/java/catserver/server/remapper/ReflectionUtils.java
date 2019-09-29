package catserver.server.remapper;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class ReflectionUtils {
    private static SecurityManager sm = new SecurityManager();

    public static Class<?> getCallerClass(int skip) {
        return sm.getCallerClass(skip);
    }

    public static ClassLoader getCallerClassloader() {
        return ReflectionUtils.getCallerClass(3).getClassLoader(); // added one due to it being the caller of the caller;
    }

    public static Class<?>[] getStackClass() {
        return sm.getStackClass();
    }

    static class SecurityManager extends java.lang.SecurityManager {
        public Class<?> getCallerClass(int skip) {
            return getClassContext()[skip + 1];
        }
        public Class<?>[] getStackClass() {
            return getClassContext();
        }
    }

    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
