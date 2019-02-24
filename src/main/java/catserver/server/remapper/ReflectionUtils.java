package catserver.server.remapper;

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
}
