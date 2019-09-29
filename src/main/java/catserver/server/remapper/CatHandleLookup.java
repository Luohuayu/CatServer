package catserver.server.remapper;

import catserver.server.CatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CatHandleLookup {
    private static HashMap<String, String> map = new HashMap<>();

    static {
        try {
            CatHandleLookup.loadMappings(new BufferedReader(new InputStreamReader(MappingLoader.class.getClassLoader().getResourceAsStream("mappings/" + CatServer.getNativeVersion() + "/cb2srg.srg"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MethodHandle findSpecial(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type, Class<?> specialCaller) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, type.parameterArray());
        }
        return lookup.findSpecial(refc, name, type, specialCaller);
    }

    public static MethodHandle findVirtual(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType oldType) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, oldType.parameterArray());
        } else if (refc.getName().equals("java.lang.Class") || refc.getName().equals("java.lang.ClassLoader")) {
            switch (name) {
                case "getField":
                case "getDeclaredField":
                case "getMethod":
                case "getDeclaredMethod":
                case "getSimpleName":
                case "getName":
                case "loadClass":
                    Class<?>[] newParArr = new Class<?>[oldType.parameterArray().length + 1];

                    if (refc.getName().equals("java.lang.Class"))
                        newParArr[0] = Class.class;
                    else
                        newParArr[0] = ClassLoader.class;

                    System.arraycopy(oldType.parameterArray(), 0 , newParArr, 1, oldType.parameterArray().length);

                    MethodType newType = MethodType.methodType(oldType.returnType(), newParArr);


                    MethodHandle handle = lookup.findStatic(ReflectionMethods.class, name, newType);
                    return handle;
            }
        }
        return lookup.findVirtual(refc, name, oldType);
    }

    public static MethodHandle findStatic(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith("net.minecraft.")) {
            name = RemapUtils.mapMethod(refc, name, type.parameterArray());
        } else if (refc.getName().equals("java.lang.Class") && name.equals("forName")) {
            refc = ReflectionMethods.class;
        }
        return lookup.findStatic(refc, name, type);
    }

    public static MethodType fromMethodDescriptorString(String descriptor, ClassLoader loader) {
        String remapDesc = map.getOrDefault(descriptor, descriptor);
        return MethodType.fromMethodDescriptorString(remapDesc, loader);
    }

    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method m) throws IllegalAccessException {
        if (m.getDeclaringClass().getName().equals("java.lang.Class")) {
            switch (m.getName()) {
                case "forName":
                    return getClassReflectionMethod(lookup, m.getName(), String.class);
                case "getField":
                case "getDeclaredField":
                    return getClassReflectionMethod(lookup, m.getName(), Class.class, String.class);
                case "getMethod":
                case "getDeclaredMethod":
                    return getClassReflectionMethod(lookup, m.getName(), Class.class, String.class, Class[].class);
                case "getSimpleName":
                    return getClassReflectionMethod(lookup, m.getName(), Class.class);
            }
        } else if (m.getName().equals("getName")) {

            if (m.getDeclaringClass().getName().equals("java.lang.reflect.Field")) {
                return getClassReflectionMethod(lookup, m.getName(), Field.class);
            } else if (m.getDeclaringClass().getName().equals("java.lang.reflect.Method")) {
                return getClassReflectionMethod(lookup, m.getName(), Method.class);
            }

        } else if (m.getName().equals("loadClass") && m.getDeclaringClass().getName().equals("java.lang.ClassLoader")) {
            return getClassReflectionMethod(lookup, m.getName(), ClassLoader.class, String.class);
        }

        return lookup.unreflect(m);
    }

    private static MethodHandle getClassReflectionMethod(MethodHandles.Lookup lookup, String name, Class<?>... p) {
        try {
            return lookup.unreflect(ReflectionMethods.class.getMethod(name, p));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void loadMappings(BufferedReader reader) throws IOException {
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
