package catserver.server.remapper.target;

import catserver.server.CatServer;
import catserver.server.remapper.MappingLoader;
import catserver.server.remapper.RemapRules;
import catserver.server.remapper.RemapUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MethodHandleMethods {
    private static HashMap<String, String> map = new HashMap<>();

    static {
        try {
            MethodHandleMethods.loadMappings(new BufferedReader(new InputStreamReader(MappingLoader.class.getClassLoader().getResourceAsStream("mappings/" + CatServer.getNativeVersion() + "/cb2srg.srg"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // MethodHandles$Lookup.findStatic
    public static MethodHandle findStatic(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, type.parameterArray());
        } else {
            Class<?> remappedClass = RemapRules.getStaticMethodTarget((refc.getName().replace(".", "/") + ";" + name));
            if (remappedClass != null) {
                refc = remappedClass;
            }
        }
        return lookup.findStatic(refc, name, type);
    }

    // MethodHandles$Lookup.findVirtual
    public static MethodHandle findVirtual(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType oldType) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, oldType.parameterArray());
        } else {
            Class<?> remappedClass = RemapRules.getVirtualMethodToStaticTarget((refc.getName().replace(".", "/") + ";" + name));
            if (remappedClass != null) {
                Class<?>[] newParArr = new Class<?>[oldType.parameterArray().length + 1];
                newParArr[0] = refc;
                System.arraycopy(oldType.parameterArray(), 0 , newParArr, 1, oldType.parameterArray().length);

                MethodType newType = MethodType.methodType(oldType.returnType(), newParArr);
                MethodHandle handle = lookup.findStatic(remappedClass, name, newType);

                return handle;
            }
        }
        return lookup.findVirtual(refc, name, oldType);
    }

    // MethodHandles$Lookup.findSpecial
    public static MethodHandle findSpecial(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type, Class<?> specialCaller) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, type.parameterArray());
        }
        return lookup.findSpecial(refc, name, type, specialCaller);
    }

    // MethodHandles$Lookup.findGetter
    public static MethodHandle findGetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapFieldName(refc, name);
        }
        return lookup.findGetter(refc, name, type);
    }

    // MethodHandles$Lookup.findSetter
    public static MethodHandle findSetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapFieldName(refc, name);
        }
        return lookup.findSetter(refc, name, type);
    }

    // MethodHandles$Lookup.findStaticGetter
    public static MethodHandle findStaticGetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapFieldName(refc, name);
        }
        return lookup.findStaticGetter(refc, name, type);
    }

    // MethodHandles$Lookup.findStaticSetter
    public static MethodHandle findStaticSetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapFieldName(refc, name);
        }
        return lookup.findStaticSetter(refc, name, type);
    }

    // MethodType.fromMethodDescriptorString
    public static MethodType fromMethodDescriptorString(String descriptor, ClassLoader loader) {
        String remapDesc = map.getOrDefault(descriptor, descriptor);
        return MethodType.fromMethodDescriptorString(remapDesc, loader);
    }

    // MethodHandles$Lookup.unreflect
    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method m) throws IllegalAccessException {
        Class<?> remappedClass = RemapRules.getVirtualMethodToStaticTarget((m.getDeclaringClass().getName().replace(".", "/") + ";" + m.getName()));
        if (remappedClass != null) {
            try {
                return lookup.unreflect(getClassReflectionMethod(lookup, remappedClass, m));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return lookup.unreflect(m);
    }

    private static Method getClassReflectionMethod(MethodHandles.Lookup lookup, Class<?> remappedClass, Method originalMethod) throws NoSuchMethodException {
        Class<?>[] oldParArr = originalMethod.getParameterTypes();
        Class<?>[] newParArr = new Class<?>[oldParArr.length + 1];
        newParArr[0] = originalMethod.getDeclaringClass();
        System.arraycopy(oldParArr, 0 , newParArr, 1, oldParArr.length);

        return remappedClass.getMethod(originalMethod.getName(), newParArr);
    }

    private static void loadMappings(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            int commentIndex = line.indexOf('#');
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }
            if (line.isEmpty() || !line.startsWith("MD: ")) {
                continue;
            }
            String[] sp = line.split("\\s+");
            String firDesc = sp[2];
            String secDesc = sp[4];
            map.put(firDesc, secDesc);
        }
    }
}
