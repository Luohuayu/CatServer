package catserver.server;

import java.lang.reflect.Field;
import java.util.Optional;

public class Java14Launcher {
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

    // The source code from FoxLaunch (https://github.com/Luohuayu/CatServer/blob/1.18.2/foxlaunch/src/main/java/foxlaunch/legacy/LegacyLauncher.java)
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
                System.out.println("Failed to initialize Java14Launcher: " + throwable.toString());
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
