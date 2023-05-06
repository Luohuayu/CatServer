package foxlaunch;

import foxlaunch.legacy.InstallTool;
import foxlaunch.legacy.LegacyLauncher;

public class FoxServerLauncher {
    private static final boolean skipCheckLibraries = Boolean.parseBoolean(System.getProperty("catserver.skipCheckLibraries"));

    public static void main(String[] args) throws Throwable {
        System.out.println("Loading libraries, please wait...");

        if (!checkJavaVersion() && !LegacyLauncher.setup()) {
            System.out.println(
                    "The current Java version may not be compatible, you may need to add these parameters before -jar: \n" +
                            "--add-exports=java.base/sun.security.util=ALL-UNNAMED --add-opens=java.base/java.util.jar=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED"
            );
        }

        DataManager.setup();
        if (!skipCheckLibraries) {
            DataManager.downloadLibraries();
        }
        InstallTool.install(DataManager.getVersionData("minecraft"), DataManager.getVersionData("mcp"), DataManager.getVersionData("forge"));

        LegacyLauncher.loadJars();
        FoxServerLauncher.class.getClassLoader().loadClass("com.google.gson.internal.bind.TypeAdapters$EnumTypeAdapter"); // Load gson patch
        FoxServerLauncher.class.getClassLoader().loadClass("net.minecraftforge.eventbus.EventBus"); // Load EventBus patch

        catserver.server.utils.Log4j2_3201_Fixer.disableJndiLookup();

        DataManager.gc();

        Class.forName("net.minecraftforge.server.ServerMain").getMethod("main", String[].class).invoke(null, new Object[]{ args });
    }

    private static boolean checkJavaVersion() {
        String classVersion = System.getProperty("java.class.version");
        try {
            int version = Integer.parseInt(classVersion.split("\\.")[0]);
            if (version > 60) { // jdk16: 60
                System.out.println(String.format(LanguageUtils.I18nToString("launch.java_wrong"), System.getProperty("java.version")));
            }
            return version < 58; // jdk14: 58
        } catch (Exception e) {
            System.out.println("Unknown java version: " + classVersion);
        }
        return false;
    }
}
