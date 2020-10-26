package catserver.server.remapper;

import catserver.server.CatServer;
import catserver.server.remapper.target.CatClassLoader;
import catserver.server.remapper.target.CatURLClassLoader;
import catserver.server.remapper.target.MethodHandleMethods;
import catserver.server.remapper.target.ReflectionMethods;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class RemapRules {
    private static final String NMSPackage = "net.minecraft.server." + CatServer.getNativeVersion();

    private static Set<String> remapPackages = Sets.newHashSet();

    private static Map<String, Class<?>> remapStaticMethod = Maps.newHashMap();
    private static Map<String, Class<?>> remapVirtualMethod = Maps.newHashMap();
    private static Map<String, Class<?>> remapVirtualMethodToStatic = Maps.newHashMap();
    private static Map<String, Class<?>> remapSuperClass = Maps.newHashMap();

    static {
        remapPackages.add(NMSPackage);
        remapPackages.add("org.bukkit.craftbukkit.");

        remapStaticMethod.put("java/lang/Class;forName", ReflectionMethods.class);
        remapStaticMethod.put("java/lang/invoke/MethodType;fromMethodDescriptorString", MethodHandleMethods.class);

        remapVirtualMethodToStatic.put("java/lang/Class;getField", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredField", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getMethod", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredMethod", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getSimpleName", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredMethods", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/reflect/Field;getName", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/reflect/Method;getName", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/ClassLoader;loadClass", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findStatic", MethodHandleMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findVirtual", MethodHandleMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findSpecial", MethodHandleMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findGetter", MethodHandleMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findSetter", MethodHandleMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findStaticGetter", MethodHandleMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findStaticSetter", MethodHandleMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;unreflect", MethodHandleMethods.class);

        remapSuperClass.put("java/net/URLClassLoader", CatURLClassLoader.class);
        remapSuperClass.put("java/lang/ClassLoader", CatClassLoader.class);
    }

    public static boolean isNeedRemapClass(String className) {
        className = className.replace("/", ".");
        for (String remapPackage : remapPackages) {
            if (className.startsWith(remapPackage)) return true;
        }
        return false;
    }

    public static boolean isNMSPackage(String className) {
        return className.replace("/", ".").startsWith(NMSPackage);
    }

    public static String getNMSPackage() {
        return NMSPackage;
    }

    public static Class<?> getStaticMethodTarget(String original) {
        return remapStaticMethod.get(original);
    }

    public static Class<?> getVirtualMethodTarget(String original) {
        return remapVirtualMethod.get(original);
    }

    public static Class<?> getVirtualMethodToStaticTarget(String original) {
        return remapVirtualMethodToStatic.get(original);
    }

    public static Class<?> getSuperClassTarget(String original) {
        return remapSuperClass.get(original);
    }

    public static void addVirtualMethodTarget(String original, Class<?> target) {
        remapVirtualMethod.put(original, target);
    }
}
