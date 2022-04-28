package catserver.server.remapper.proxy;

import catserver.server.remapper.ArrayHandle;
import catserver.server.remapper.ReflectionTransformer;
import catserver.server.remapper.ReflectionUtils;
import catserver.server.remapper.RemapRules;
import catserver.server.remapper.RemapUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyReflection {

    private final static ConcurrentHashMap<Field, String> fieldGetNameCache = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Method, String> methodGetNameCache = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Class<?>, String> simpleNameGetNameCache = new ConcurrentHashMap<>();

    // Class.forName
    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, ReflectionUtils.getCallerClassloader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
        if (ArrayHandle.isArray(className)) {
            try {
                ArrayHandle arrayHandle = new ArrayHandle(className);
                if (RemapRules.isNeedRemapClass(arrayHandle.originClassName)) {
                    String remapped = ReflectionTransformer.jarMapping.classes.getOrDefault(arrayHandle.originClassName.replace('.', '/'), arrayHandle.originClassName).replace('/', '.');
                    className = arrayHandle.arrayStart + remapped + arrayHandle.arrayEnd;
                }
            } catch (IllegalArgumentException e) {
                // ignored
            }
        } else {
            if (RemapRules.isNeedRemapClass(className)) {
                className = ReflectionTransformer.jarMapping.classes.getOrDefault(className.replace('.', '/'), className).replace('/', '.');
            }
        }

        Class<?> result = null;
        try {
            result = Class.forName(className, initialize, classLoader);
        } catch (NullPointerException e) {
            throw new ClassNotFoundException(className);
        }
        return result;
    }

    // Class.getSimpleName
    public static String getSimpleName(Class<?> inst) {
        if (!RemapUtils.isNeedRemapClass(inst, false)) return inst.getSimpleName();
        String cache = simpleNameGetNameCache.get(inst);
        if (cache != null) return cache;
        String[] name = RemapUtils.reverseMapExternal(inst).split("\\.");
        String retn = name[name.length - 1];
        if (retn.contains("$")) {
            int count = 0;
            int index = 0;
            String defaultSimpleName = inst.getSimpleName();
            while ((index = defaultSimpleName.indexOf("$", index)) != -1) {
                index ++;
                count++;
            }
            name = retn.split("\\$");
            if (name.length < count) {
                simpleNameGetNameCache.put(inst, retn);
                return retn;
            }
            retn = name[name.length - count-- - 1];
            for (; count >= 0;) {
                retn += name[name.length - count-- - 1];
                retn += "$";
            }
        }
        simpleNameGetNameCache.put(inst, retn);
        return retn;
    }

    // Class.getName
    public static String getName(Class<?> inst) {
        if (!RemapUtils.isNeedRemapClass(inst, true)) return inst.getName();
        return RemapUtils.reverseMap(inst).replace("/", ".");
    }

    // Class.getDeclaredField
    public static Field getDeclaredField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.isNeedRemapClass(inst, false))
            name = ReflectionTransformer.remapper.mapFieldName(RemapUtils.reverseMap(inst), name, null);
        return inst.getDeclaredField(name);
    }

    // Class.getDeclaredMethod
    public static Method getDeclaredMethod(Class<?> inst, String name, Class<?>...parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.isNeedRemapClass(inst, true))
            name = RemapUtils.mapMethod(inst, name, parameterTypes);
        try {
            return inst.getDeclaredMethod(name, parameterTypes);
        } catch (NoClassDefFoundError e) {
            throw new NoSuchMethodException(e.toString());
        }
    }

    // Class.getDeclaredMethods
    public static Method[] getDeclaredMethods(Class<?> inst) {
        try {
            return inst.getDeclaredMethods();
        } catch (NoClassDefFoundError e) {
            return new Method[]{};
        }
    }

    // Class.getField
    public static Field getField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.isNeedRemapClass(inst, true))
            name = RemapUtils.mapFieldName(inst, name);
        return inst.getField(name);
    }

    // Class.getMethod
    public static Method getMethod(Class<?> inst, String name, Class<?>...parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.isNeedRemapClass(inst, true))
            name = RemapUtils.mapMethod(inst, name, parameterTypes);
        try {
            return inst.getMethod(name, parameterTypes);
        } catch (NoClassDefFoundError e) {
            throw new NoSuchMethodException(e.toString());
        }
        
    }

    // Field.getName
    public static String getName(Field field) {
        if (!RemapUtils.isNeedRemapClass(field.getDeclaringClass(), false)) return field.getName();
        String cache = fieldGetNameCache.get(field);
        if (cache != null) return cache;
        String retn = RemapUtils.reverseFiled(field);
        fieldGetNameCache.put(field, retn);
        return retn;
    }

    // Method.getName
    public static String getName(Method method) {
        if (!RemapUtils.isNeedRemapClass(method.getDeclaringClass(), true)) return method.getName();
        String cache = methodGetNameCache.get(method);
        if (cache != null) return cache;
        String retn = RemapUtils.reverseMethodName(method);
        methodGetNameCache.put(method, retn);
        return retn;
    }

    // ClassLoader.loadClass
    public static Class<?> loadClass(ClassLoader inst, String className) throws ClassNotFoundException {
        if (RemapRules.isNeedRemapClass(className) || className.startsWith("net.minecraft."))
            className = RemapUtils.fixPackageAndMapClass(className.replace('.', '/')).replace('/', '.');
        return inst.loadClass(className);
    }
}
