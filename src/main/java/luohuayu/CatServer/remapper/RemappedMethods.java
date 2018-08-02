package luohuayu.CatServer.remapper;

import luohuayu.CatServer.CatServer;

public class RemappedMethods {
    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, ReflectionUtils.getCallerClassloader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
        if (!className.startsWith("net.minecraft.server."+CatServer.getNativeVersion())) return Class.forName(className, initialize, classLoader);
        className = Transformer.jarMapping.classes.get(className.replace('.', '/')).replace('/', '.');
        return Class.forName(className, initialize, classLoader);
    }
}
