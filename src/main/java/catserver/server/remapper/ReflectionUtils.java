package catserver.server.remapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
    private static SecurityManager sm = new SecurityManager();

    public static Class<?> getCallerClass(int skip) {
        return sm.getCallerClass(skip);
    }

    public static ClassLoader getCallerClassloader() {
        return ReflectionUtils.getCallerClass(3).getClassLoader(); // added one due to it being the caller of the caller;
    }

    static class SecurityManager extends java.lang.SecurityManager {
        public Class<?> getCallerClass(int skip) {
            return getClassContext()[skip + 1];
        }
    }

    public static void modifyFiledFinal(Field field) throws ReflectiveOperationException {
        if (catserver.server.launch.Java11Support.enable) return;
        Field fieldModifiers = Field.class.getDeclaredField("modifiers");
        fieldModifiers.setAccessible(true);
        fieldModifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }
}
