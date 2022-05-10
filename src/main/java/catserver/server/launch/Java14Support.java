package catserver.server.launch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.jar.JarOutputStream;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.io.FileUtils;

public class Java14Support {
    public final static String PACK200_LIB_NAME = "catserver-pack200-jdk11.jar";

    public static void unpack(InputStream in, JarOutputStream out) throws IOException {
        try {
            Object unpacker = Class.forName("java.util.jar.Pack200")
                    .getMethod("newUnpacker")
                    .invoke(null);
            unpacker.getClass()
                    .getMethod("unpack", InputStream.class, JarOutputStream.class)
                    .invoke(unpacker, in, out);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            try {
                File filePack200Lib = new File("libraries", PACK200_LIB_NAME);
                if (!filePack200Lib.exists()) {
                    FileUtils.copyToFile(Java14Support.class.getResourceAsStream("/libs/" + PACK200_LIB_NAME), filePack200Lib);
                }

                LaunchClassLoader launchClassLoader = ((LaunchClassLoader) Thread.currentThread().getContextClassLoader());
                launchClassLoader.addURL(filePack200Lib.toURI().toURL());
                launchClassLoader.addTransformerExclusion("catserver.libs.");

                Class<?> unpackClass = launchClassLoader.loadClass("catserver.libs.pack.UnpackerImpl");
                unpackClass.getMethod("unpack", InputStream.class, JarOutputStream.class).invoke(unpackClass.newInstance(), in, out);
            } catch (InvocationTargetException e2) {
                if (e2.getCause() instanceof IOException) {
                    throw (IOException)e.getCause();
                } else {
                    throw new RuntimeException(e2.getMessage(), e2.getCause());
                }
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException)e.getCause();
            } else {
                throw new RuntimeException(e.getMessage(), e.getCause());
            }
        }
    }

    public static void setup() {
        try {
            String classVersion = System.getProperty("java.class.version");
            try {
                if (Integer.parseInt(classVersion.split("\\.")[0]) < 58) return; // jdk14: 58
            } catch (Exception e) {
                System.out.println("Unknown java version: " + classVersion);
            }

            JVMHack.addModuleOptionDynamic("addExportsToAllUnnamed", "java.base", "sun.security.util", null);
            JVMHack.addModuleOptionDynamic("addOpensToAllUnnamed", "java.base", "java.util.jar", null);
            JVMHack.addModuleOptionDynamic("addOpensToAllUnnamed", "java.base", "java.lang", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // https://github.com/Luohuayu/CatServer/blob/1.16.5/src/fmllauncher/java/foxlaunch/legacy/LegacyLauncher.java
    static class JVMHack {
        protected static boolean init = false;

        static {
            try {
                Class<?> internalModulesClass = Class.forName("jdk.internal.module.Modules");
                Object jdkInternalModule = Class.class.getMethod("getModule").invoke(internalModulesClass);

                Field moduleField = Class.class.getDeclaredField("module");
                Java11Support.unsafe.getAndSetObject(JVMHack.class, Java11Support.unsafe.objectFieldOffset(moduleField), jdkInternalModule);

                init = true;
            } catch (Throwable throwable) {
                System.out.println("Failed to initialize Java14Support: " + throwable.toString());
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
