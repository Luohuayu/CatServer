package foxlaunch;

import foxlaunch.legacy.InstallTool;
import foxlaunch.legacy.LegacyLauncher;
import java.util.Arrays;

public class FoxServerLauncher {
    public static void main(String[] args) throws Throwable {
        if (!checkJavaVersion()) {
            System.out.println(String.format(LanguageUtils.I18nToString("launch.java_wrong"), System.getProperty("java.version")));
            Thread.sleep(5000);
            return;
        }

        System.out.println("Loading libraries, please wait...");

        DataManager.setupLibrariesMap();
        DataManager.unpackData();
        DataManager.downloadLibraries();

        InstallTool.install(DataManager.getVersionData("minecraft"), DataManager.getVersionData("mcp"), DataManager.getVersionData("forge"));

        if (!LegacyLauncher.tryLaunch(Arrays.asList(args), DataManager.getLaunchArgs(), DataManager::gc)) {
            DataManager.generateLaunchScript(args);
        }
    }

    private static boolean checkJavaVersion() {
        String classVersion = System.getProperty("java.class.version");
        try {
            return Integer.parseInt(classVersion.split("\\.")[0]) >= 61; // jdk17: 61
        } catch (Exception e) {
            System.out.println("Unknown java version: " + classVersion);
        }
        return false;
    }
}
