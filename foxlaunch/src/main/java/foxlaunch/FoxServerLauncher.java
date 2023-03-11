package foxlaunch;

import foxlaunch.legacy.InstallTool;
import foxlaunch.legacy.LegacyLauncher;

import java.lang.reflect.Array;
import java.util.Arrays;

public class FoxServerLauncher {
    private static final boolean skipCheckLibraries = Boolean.parseBoolean(System.getProperty("catserver.skipCheckLibraries"));

    public static void main(String[] args) throws Throwable {
        System.out.println("Loading libraries, please wait...");

        if (!checkJavaVersion()) {
            System.out.println(String.format(LanguageUtils.I18nToString("launch.java_wrong"), System.getProperty("java.version")));
            Thread.sleep(5000);
            return;
        }

        if (!LegacyLauncher.setup()) {
            System.out.println(
                    "The current Java version may not be compatible, you may need to add these parameters before -jar: \n" +
                            "--add-exports=java.base/sun.security.util=ALL-UNNAMED --add-opens=java.base/java.util.jar=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED"
            );
        }

        DataManager.setup();
        if (!skipCheckLibraries) {
            DataManager.downloadLibraries();
        }
        args = FoxServerLauncher.removeArg(args, "nogui");
        if (InstallTool.install(DataManager.getVersionData("minecraft"), DataManager.getVersionData("mcp"), DataManager.getVersionData("forge"))) {
            System.out.println(LanguageUtils.I18nToString("launch.server_installed"));
        }

        LegacyLauncher.loadJars();

        System.setProperty("java.net.preferIPv6Addresses", "system");
        System.setProperty("ignoreList", "bootstraplauncher-1.0.0.jar,securejarhandler-1.0.3.jar,asm-commons-9.2.jar,asm-util-9.2.jar,asm-analysis-9.2.jar,asm-tree-9.2.jar,asm-9.2.jar");
        System.setProperty("libraryDirectory", "libraries");
        System.setProperty("legacyClassPath", String.join(";", DataManager.getLibrariesMap().entrySet().stream().map(entry -> entry.getValue().getAbsolutePath() + "/" + entry.getKey()).toArray(String[]::new)));

        String[] launchArgs = new String[] {
                "--launchTarget",
                "forgeserver",
                "--fml.forgeVersion",
                DataManager.getVersionData("forge"),
                "--fml.mcVersion",
                DataManager.getVersionData("minecraft"),
                "--fml.forgeGroup",
                "net.minecraftforge",
                "--fml.mcpVersion",
                DataManager.getVersionData("mcp")
        };

        launchArgs = Arrays.copyOf(launchArgs, launchArgs.length + args.length);
        System.arraycopy(args, 0, launchArgs, launchArgs.length, args.length);

        DataManager.gc();
        System.setProperty("log4j.configurationFile", "log4j2-catserver.xml");

        Class.forName("cpw.mods.bootstraplauncher.BootstrapLauncher").getMethod("main", String[].class).invoke(null, new Object[] { launchArgs } );
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

    private static String[] removeArg(String [] mainArgs, String arg) {
        if (Arrays.stream(mainArgs).anyMatch(unlessArg -> unlessArg.equalsIgnoreCase(arg))) {
            if (mainArgs.length > 0) {
                int index = -1;
                for (int i = 0; i < mainArgs.length; i++) {
                    if (mainArgs[i].equalsIgnoreCase(arg)) {
                        index = i;
                        break;
                    }
                }
                if (index >= 0) {
                    String[] newArgs = (String[]) Array.newInstance(mainArgs.getClass().getComponentType(), mainArgs.length - 1);
                    if (newArgs.length > 0) {
                        System.arraycopy(mainArgs, 0, newArgs, 0, index);
                        System.arraycopy(mainArgs, index + 1, newArgs, index, newArgs.length - index);
                    }
                    mainArgs = newArgs;
                    System.out.println(String.format("[FoxLaunch] Incompatible parameters detected: %s , removed..", arg));
                }
            }
        }
        return mainArgs;
    }
}
