package foxlaunch.legacy;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class JarLoader {
    protected static Instrumentation inst = null;

    // The JRE will call method before launching your main()
    public static void agentmain(final String a, final Instrumentation inst) {
        JarLoader.inst = inst;
    }

    // Don't forget to specify -javaagent:<CatServer jar> on Java 9+,
    // if you load main CatServer jar from -cp rather than direct -jar
    public static void premain(String agentArgs, Instrumentation inst) {
        JarLoader.inst = inst;
    }

    protected static void loadJar(File path) throws Exception {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (!path.getName().equals("minecraft_server.1.16.5.jar")) {
            if (!(cl instanceof URLClassLoader)) {
                // If Java 9 or higher use Instrumentation
                inst.appendToSystemClassLoaderSearch(new JarFile(path));
            } else {
                // If Java 8 or below fallback to old method
                Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                m.setAccessible(true);
                m.invoke(cl, path.toURI().toURL());
            }
        }
    }
}
