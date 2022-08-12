package catserver.server.remapper;

import catserver.server.CatServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import net.md_5.specialsource.JarMapping;

public class MappingLoader {
    private static final String org_bukkit_craftbukkit = new String(new char[] {'o','r','g','/','b','u','k','k','i','t','/','c','r','a','f','t','b','u','k','k','i','t'});
    private static final JarMapping globalJarMapping = new JarMapping();

    private static Field fieldPackages;
    private static Field fieldClasses;
    private static Field fieldFields;
    private static Field fieldMethods;

    static {
        initGlobalMapping();
    }

    private static void initGlobalMapping() {
        try {
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/com/google/gson", "com/google/gson");
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/it/unimi/dsi/fastutil", "it/unimi/dsi/fastutil");
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/jline", "jline");
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/joptsimple", "joptsimple");
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/org/apache", "org/apache");
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/org/objectweb/asm", "org/objectweb/asm");

            loadNmsMappings(globalJarMapping, CatServer.NATIVE_VERSION);

            fieldPackages = JarMapping.class.getDeclaredField("packages");
            fieldPackages.setAccessible(true);

            fieldClasses = JarMapping.class.getDeclaredField("classes");
            fieldClasses.setAccessible(true);

            fieldFields = JarMapping.class.getDeclaredField("fields");
            fieldFields.setAccessible(true);

            fieldMethods = JarMapping.class.getDeclaredField("methods");
            fieldMethods.setAccessible(true);
            try {
                ReflectionUtils.modifyFiledFinal(fieldFields);
                ReflectionUtils.modifyFiledFinal(fieldClasses);
                ReflectionUtils.modifyFiledFinal(fieldPackages);
                ReflectionUtils.modifyFiledFinal(fieldMethods);
            } catch (Exception ignored) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadNmsMappings(JarMapping jarMapping, String obfVersion) throws IOException {
        jarMapping.loadMappings(
                new BufferedReader(new InputStreamReader(MappingLoader.class.getClassLoader().getResourceAsStream("mappings/spigot2srg.srg"))),
                null, null, false);
    }

    public static JarMapping loadMapping() {
        JarMapping jarMapping = new JarMapping();
        try {
            fieldPackages.set(jarMapping, fieldPackages.get(globalJarMapping));
            fieldClasses.set(jarMapping, fieldClasses.get(globalJarMapping));
            fieldFields.set(jarMapping, fieldFields.get(globalJarMapping));
            fieldMethods.set(jarMapping, fieldMethods.get(globalJarMapping));
        } catch (Exception ignored) {
            try {
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                unsafe.putObject(jarMapping, unsafe.objectFieldOffset(fieldPackages), fieldPackages.get(globalJarMapping));
                unsafe.putObject(jarMapping, unsafe.objectFieldOffset(fieldClasses), fieldClasses.get(globalJarMapping));
                unsafe.putObject(jarMapping, unsafe.objectFieldOffset(fieldFields), fieldFields.get(globalJarMapping));
                unsafe.putObject(jarMapping, unsafe.objectFieldOffset(fieldMethods), fieldMethods.get(globalJarMapping));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jarMapping;
    }


}
