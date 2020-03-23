package catserver.server.remapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import catserver.server.CatServer;

public class ReflectionMethods {

    private final static ConcurrentHashMap<Field, String> fieldGetNameCache = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Method, String> methodGetNameCache = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Class<?>, String> simpleNameGetNameCache = new ConcurrentHashMap<>();

    // Class.forName
    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, ReflectionUtils.getCallerClassloader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
        if (className.startsWith("net.minecraft.server." + CatServer.getNativeVersion()) || className.startsWith("org.bukkit.craftbukkit."))
            className = ReflectionTransformer.jarMapping.classes.getOrDefault(className.replace('.', '/'), className).replace('/', '.');
        return Class.forName(className, initialize, classLoader);
    }

    // Get Fields
    public static Field getField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true))
            name = RemapUtils.mapFieldName(inst, name);
        return inst.getField(name);
    }

    public static Field getDeclaredField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, false))
            name = ReflectionTransformer.remapper.mapFieldName(RemapUtils.reverseMap(inst), name, null);
        return inst.getDeclaredField(name);
    }

    // Get Methods
    public static Method getMethod(Class<?> inst, String name, Class<?>...parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true))
            name = RemapUtils.mapMethod(inst, name, parameterTypes);
        try {
            return inst.getMethod(name, parameterTypes);
        } catch (NoClassDefFoundError e) {
            throw new NoSuchMethodException(e.toString());
        }
        
    }

    public static Method getDeclaredMethod(Class<?> inst, String name, Class<?>...parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true))
            name = RemapUtils.mapMethod(inst, name, parameterTypes);
        try {
            return inst.getDeclaredMethod(name, parameterTypes);
        } catch (NoClassDefFoundError e) {
            throw new NoSuchMethodException(e.toString());
        }
    }

    // getName
    public static String getName(Field field) {
        if (!RemapUtils.isClassNeedRemap(field.getDeclaringClass(), false)) return field.getName();
        String cache = fieldGetNameCache.get(field);
        if (cache != null) return cache;
        String retn = RemapUtils.demapFieldName(field);
        fieldGetNameCache.put(field, retn);
        return retn;
    }

    public static String getName(Method method) {
        if (!RemapUtils.isClassNeedRemap(method.getDeclaringClass(), true)) return method.getName();
        String cache = methodGetNameCache.get(method);
        if (cache != null) return cache;
        String retn = RemapUtils.demapMethodName(method);
        methodGetNameCache.put(method, retn);
        return retn;
    }

    // getSimpleName
    public static String getSimpleName(Class<?> inst) {
        if (!RemapUtils.isClassNeedRemap(inst, false)) return inst.getSimpleName();
        String cache = simpleNameGetNameCache.get(inst);
        if (cache != null) return cache;
        String[] name = RemapUtils.reverseMapExternal(inst).split("\\.");
        String retn = name[name.length - 1];
        simpleNameGetNameCache.put(inst, retn);
        return retn;
    }

    // getDeclaredMethods
    public static Method[] getDeclaredMethods(Class<?> inst) {
        try {
            return inst.getDeclaredMethods();
        } catch (NoClassDefFoundError e) {
            return new Method[]{};
        }
    }

    // ClassLoader.loadClass
    public static Class<?> loadClass(ClassLoader inst, String className) throws ClassNotFoundException {
        if (className.startsWith("net.minecraft."))
            className = RemapUtils.mapClass(className.replace('.', '/')).replace('/', '.');
        return inst.loadClass(className);
    }
}
