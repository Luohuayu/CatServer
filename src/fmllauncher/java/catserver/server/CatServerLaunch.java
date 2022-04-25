package catserver.server;

import catserver.server.shadow.ServerMain;
import moe.loliserver.LanguagesMap;
import moe.loliserver.legacy.util.EulaUtil;
import moe.loliserver.legacy.util.InstallUtils;
import moe.loliserver.legacy.util.JarLoader;
import moe.loliserver.legacy.util.JarTool;
import moe.loliserver.libraries.CustomLibraries;
import moe.loliserver.libraries.DefaultLibraries;

public class CatServerLaunch {
    private static final boolean skipCheckLibraries = Boolean.parseBoolean(System.getProperty("catserver.skipCheckLibraries"));

    public static void main(String[] args) throws Throwable {
        if (!checkJavaVersion() && !Java14Launcher.setup()) {
            System.out.println(
                    "The current Java version may not be compatible, you may need to add these parameters before -jar: \n" +
                    "--add-exports=java.base/sun.security.util=ALL-UNNAMED --add-opens=java.base/java.util.jar=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED"
            );
        }

        if (!skipCheckLibraries) {
            DefaultLibraries.run();
            InstallUtils.startInstallation();
        }

        initEnv();
        loadGsonPatch();

        acceptEULA();

        ServerMain.main(args);
    }

    public static boolean checkJavaVersion() {
        String classVersion = System.getProperty("java.class.version");
        try {
            return Integer.parseInt(classVersion.split("\\.")[0]) < 58; // jdk14: 58
        } catch (Exception e) {
            System.out.println("Unknown java version: " + classVersion);
        }
        return false;
    }

    private static void initEnv() throws Exception {
        String path = JarTool.getJarPath();
        if (path != null && (path.contains("+") || path.contains("!"))) {
            System.out.println("[ERROR] Unsupported characters have been detected in your server path. \nPlease remove + or ! in your server's folder name (in the folder which contains this character).\nPath : "+path);
            System.exit(0);
        }

        CustomLibraries.loadCustomLibs();
        new JarLoader().loadJar(InstallUtils.extra);
    }

    private static void loadGsonPatch() throws Exception {
        CatServerLaunch.class.getClassLoader().loadClass("com.google.gson.internal.bind.TypeAdapters$EnumTypeAdapter");
    }

    private static void acceptEULA() throws Exception {
        if (!EulaUtil.hasAcceptedEULA()) {
            System.out.println(LanguagesMap.i18nGet("loli.eula"));
            Thread.sleep(5000);
            EulaUtil.writeInfos();
        }
    }
}
