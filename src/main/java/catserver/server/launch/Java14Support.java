package catserver.server.launch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
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
}
