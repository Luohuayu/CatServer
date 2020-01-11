package catserver.server;

import catserver.server.utils.LanguageUtils;

import java.net.URLClassLoader;

public class CatServerLaunch {
    public static void main(String[] args) throws Throwable {
        checkJavaVersion();
        LibrariesManager.checkLibraries();
        Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[] { args });
    }

    public static void checkJavaVersion() {
        if (!(CatServerLaunch.class.getClassLoader() instanceof URLClassLoader)) {
            System.out.println(String.format(LanguageUtils.I18nToString("launch.java_wrong"), System.getProperty("java.version")));
            System.exit(0);
        }
    }
}
