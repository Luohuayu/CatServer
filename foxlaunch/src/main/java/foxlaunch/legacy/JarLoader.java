package foxlaunch.legacy;

import foxlaunch.LanguageUtils;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class JarLoader {
    protected static Instrumentation inst = null;

    // The JRE will call method before launching your main()
    public static void agentmain(final String a, final Instrumentation inst) {
        JarLoader.inst = inst;
    }

    // Don't forget to specify -javaagent:<FoxServer jar> on Java 9+,
    // if you load main FoxServer jar from -cp rather than direct -jar
    public static void premain(String agentArgs, Instrumentation inst) {
        JarLoader.inst = inst;
    }

    protected static void loadJar(File path) throws Exception {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (!path.getName().equals("minecraft_server.1.18.2.jar")) {
            if (!(cl instanceof URLClassLoader)) {
                // If Java 9 or higher use Instrumentation
                inst.appendToSystemClassLoaderSearch(new JarFile(path));
            } else {
                // If Java 8 or below fallback to old method (Removed)
                // Minecraft 1.18.2 does not support Java8
                throw new RuntimeException(String.format(LanguageUtils.I18nToString("launch.java_wrong"), System.getProperty("java.version")));
            }
        }
    }
}
