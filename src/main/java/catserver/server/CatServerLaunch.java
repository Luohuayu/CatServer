package catserver.server;

import catserver.server.launch.Java11Support;
import catserver.server.launch.LibrariesManager;
import catserver.server.utils.LanguageUtils;

import java.net.URLClassLoader;

public class CatServerLaunch {
    private static final boolean skipCheckLibraries = Boolean.parseBoolean(System.getProperty("catserver.skipCheckLibraries"));

    public static void main(String[] args) throws Throwable {
        System.out.println("Loading libraries, please wait...");
        checkJavaVersion();
        if (!skipCheckLibraries) LibrariesManager.checkLibraries();
        Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[] { args });
    }

    public static void checkJavaVersion() {
        if (!(CatServerLaunch.class.getClassLoader() instanceof URLClassLoader)) {
            System.out.println(String.format(LanguageUtils.I18nToString("launch.java_wrong"), System.getProperty("java.version")));
            try {
                System.out.println(LanguageUtils.I18nToString("launch.java11_compatibility"));
                Thread.sleep(5000);
                Java11Support.setup();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
