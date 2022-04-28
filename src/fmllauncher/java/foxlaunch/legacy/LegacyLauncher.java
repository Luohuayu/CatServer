package foxlaunch.legacy;

import foxlaunch.DataManager;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public class LegacyLauncher {
    public static boolean setup() {
        if (!JVMHack.init) return false;

        try {
            JVMHack.addModuleOptionDynamic("addExportsToAllUnnamed", "java.base", "sun.security.util", null);
            JVMHack.addModuleOptionDynamic("addOpensToAllUnnamed", "java.base", "java.util.jar", null);
            JVMHack.addModuleOptionDynamic("addOpensToAllUnnamed", "java.base", "java.lang", null);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static void loadJars() throws Exception {
        for (Map.Entry<String, File> entry : DataManager.getLibrariesMap().entrySet()) {
            JarLoader.loadJar(new File(entry.getValue(), entry.getKey()));
        }

        String minecraftVersion = DataManager.getVersionData("minecraft"), mcpVersion = DataManager.getVersionData("mcp");
        JarLoader.loadJar(new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-extra.jar", DataManager.getVersionData("minecraft"), mcpVersion, minecraftVersion, mcpVersion)));
        JarLoader.loadJar(new File("libraries/com/google/guava/guava/25.1-jre/guava-25.1-jre.jar"));
    }

    static class JVMHack {
        protected static boolean init = false;

        static {
            try {
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

                Class<?> internalModulesClass = Class.forName("jdk.internal.module.Modules");
                Object jdkInternalModule = Class.class.getMethod("getModule").invoke(internalModulesClass);

                Field moduleField = Class.class.getDeclaredField("module");
                unsafe.getAndSetObject(JVMHack.class, unsafe.objectFieldOffset(moduleField), jdkInternalModule);

                init = true;
            } catch (Throwable throwable) {
                System.out.println("Failed to initialize LegacyLauncher, will generate script for startup: " + throwable.toString());
            }
        }

        static void addModuleOptionDynamic(String option, String module, String pkg, String targetModule) throws Exception {
            if (!init) throw new RuntimeException();

            Class<?> internalModulesClass = Class.forName("jdk.internal.module.Modules");
            Class<?> moduleClass = Class.forName("java.lang.Module");

            Optional<Object> foundModuleOptional = (Optional<Object>) internalModulesClass.getMethod("findLoadedModule", String.class).invoke(null, module);
            Object foundModule = foundModuleOptional.orElseThrow(IllegalArgumentException::new);

            if (targetModule != null) {
                Optional<Object> foundTargetModuleOptional = (Optional<Object>) internalModulesClass.getMethod("findLoadedModule", String.class).invoke(null, targetModule);
                Object foundTargetModule = foundTargetModuleOptional.orElseThrow(IllegalArgumentException::new);

                internalModulesClass.getMethod(option, moduleClass, String.class, moduleClass).invoke(null, foundModule, pkg, foundTargetModule);
            } else {
                internalModulesClass.getMethod(option, moduleClass, String.class).invoke(null, foundModule, pkg);
            }
        }
    }
}
