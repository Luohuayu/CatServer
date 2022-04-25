package foxlaunch.legacy;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LegacyLauncher {
    public static boolean tryLaunch(List<String> userArgs, List<String> fmlArgs, Runnable callbackGC) throws Throwable {
        if (true) return false; // TODO: 未完成
        if (JarLoader.inst == null || !JVMHack.init) return false;

        try {
            for (String fmlArg : fmlArgs) {
                if (fmlArg.startsWith("-D")) {
                    String[] param = fmlArg.substring(2).split("=");
                    if (param.length == 2) {
                        System.setProperty(param[0], param[1]);
                    }
                } else if (fmlArg.startsWith("--add-opens ")) {
                    String[] param = fmlArg.substring(12).split("=");
                    String[] param2 = param[0].split("/");
                    if (param2.length == 2) {
                        JVMHack.addModuleOptionDynamic("addOpens", param2[0], param2[1], param[1]);
                    }
                } else if (fmlArg.startsWith("--add-exports ")) {
                    String[] param = fmlArg.substring(14).split("=");
                    String[] param2 = param[0].split("/");
                    if (param2.length == 2) {
                        JVMHack.addModuleOptionDynamic("addExports", param2[0], param2[1], param[1]);
                    }
                } else if (fmlArg.startsWith("-p ")) {
                    for (String cp : fmlArg.substring(3).replace("\\:", "\\;").split(";")) {
                        JarLoader.loadJar(new File(cp));
                    }
                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }

        List<String> args = new ArrayList<>();
        args.addAll(
                fmlArgs.stream()
                        .filter(s ->
                                s.startsWith("--launchTarget") ||
                                s.startsWith("--fml.forgeVersion") ||
                                s.startsWith("--fml.mcVersion") ||
                                s.startsWith("--fml.forgeGroup") ||
                                s.startsWith("--fml.mcpVersion")

                        )
                        .collect(Collectors.toList())
        );
        args.addAll(userArgs);

        callbackGC.run();
        Class.forName("cpw.mods.bootstraplauncher.BootstrapLauncher").getMethod("main", String[].class).invoke(null, new Object[] { args.toArray(new String[0]) } );

        return true;
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
