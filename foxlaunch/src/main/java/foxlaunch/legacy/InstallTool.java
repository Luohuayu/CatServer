package foxlaunch.legacy;

import foxlaunch.Utils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
import java.util.Objects;

public class InstallTool {
    public static boolean install(String minecraftVersion, String mcpVersion, String forgeVersion) throws Exception {
        if (minecraftVersion == null || mcpVersion == null || forgeVersion == null) {
            throw new RuntimeException(String.format("Missing version data: [minecraft: %s, mcp: %s, forge: %s]", minecraftVersion, mcpVersion, forgeVersion));
        }

        URL[] libInstallerTools = new URL[] {
                Utils.pathToURL("foxlaunch-libs/installertools-1.2.10.jar"),
                Utils.pathToURL("foxlaunch-libs/fastcsv-2.0.0.jar"),
                Utils.pathToURL("foxlaunch-libs/srgutils-0.4.11.jar"),
                Utils.pathToURL("libraries/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar"),
                Utils.pathToURL("libraries/net/md-5/SpecialSource/1.10.0/SpecialSource-1.10.0.jar"),
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
                Utils.pathToURL("libraries/com/google/guava/guava/31.0.1-jre/guava-31.0.1-jre.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-commons/9.5/asm-commons-9.5.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-analysis/9.5/asm-analysis-9.5.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-tree/9.5/asm-tree-9.5.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm/9.5/asm-9.5.jar")
        };

        URL[] libJarSplitter = new URL[] {
                Utils.pathToURL("foxlaunch-libs/jarsplitter-1.1.4.jar"),
                Utils.pathToURL("foxlaunch-libs/srgutils-0.4.11.jar"),
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
        };

        URL[] libForgeAutoRenamingTool = new URL[] {
                Utils.pathToURL("foxlaunch-libs/ForgeAutoRenamingTool-0.1.22.jar"),
                Utils.pathToURL("foxlaunch-libs/srgutils-0.4.11.jar"),
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-commons/9.5/asm-commons-9.5.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-analysis/9.5/asm-analysis-9.5.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-tree/9.5/asm-tree-9.5.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm/9.5/asm-9.5.jar")
        };

        URL[] libBinaryPatcher = new URL[] {
                Utils.pathToURL("foxlaunch-libs/binarypatcher-1.0.12.jar"),
                Utils.pathToURL("libraries/commons-io/commons-io/2.11.0/commons-io-2.11.0.jar"),
                Utils.pathToURL("libraries/com/google/guava/guava/31.0.1-jre/guava-31.0.1-jre.jar"),
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
                Utils.pathToURL("libraries/commons-io/commons-io/2.11.0/commons-io-2.11.0.jar")
        };

        File installHASH = new File("foxlaunch-data/install.dat");
        File installLog = new File("foxlaunch-data/install.log");

        File serverJar = Utils.findServerJar();
        File serverLZMA = new File("foxlaunch-data/server.lzma");

        File mcpZip = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s.zip", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
        File mcpMappings = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s-mappings.txt", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));

        File minecraftServer = new File("foxlaunch-data/minecraft_server." + minecraftVersion + ".jar");
        File minecraftUnpack = new File(String.format("foxlaunch-data/server-%s.jar", minecraftVersion));
        File minecraftMappings = new File(String.format("foxlaunch-data/server-%s-mappings.txt", minecraftVersion));

        File mergedMappings = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-mappings-merged.txt", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));

        File minecraftExtra = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-extra.jar", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
        File minecraftSlim = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-slim.jar", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
        File minecraftSrg = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-srg.jar", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));

        File forgeUniversalJar = new File(String.format("libraries/net/minecraftforge/forge/%s-%s/forge-%s-%s-universal.jar", minecraftVersion, forgeVersion, minecraftVersion, forgeVersion));
        File forgeServerJar = new File(String.format("libraries/net/minecraftforge/forge/%s-%s/forge-%s-%s-server.jar", minecraftVersion, forgeVersion, minecraftVersion, forgeVersion));

        for (File checkFile : new File[]{ minecraftServer, mcpZip, serverLZMA, forgeUniversalJar }) {
            if (!checkFile.exists()) {
                throw new RuntimeException("Missing " + checkFile.getAbsolutePath() + " File!");
            }
        }

        try {
            if (installHASH.exists()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(installHASH)))) {
                    if (Objects.equals(reader.readLine(), Objects.requireNonNull(Utils.getFileSHA256(serverJar), serverJar.getName()) + Objects.requireNonNull(Utils.getFileSHA256(serverLZMA), serverLZMA.getName()))) {
                        return false;
                    }
                }
            }
        } catch (Exception ignored) {}

        PrintStream originOutPrint = System.out;
        PrintStream logOutPrint = new PrintStream(new BufferedOutputStream(new FileOutputStream(installLog)));
        System.setOut(logOutPrint);

        // BUNDLER_EXTRACT
        originOutPrint.println("[FoxLaunch] Unpacking vanilla server jar..");

        if(Utils.isJarCorrupted(minecraftUnpack)) {
            minecraftUnpack.delete();
        }

        if (!minecraftUnpack.exists()) {
            Utils.unpackZipEntry(minecraftServer, minecraftUnpack, "META-INF/versions/" + minecraftVersion + "/server-" + minecraftVersion + ".jar");
        }

        // MCP_DATA
        originOutPrint.println("[FoxLaunch] Initializing MCP data..");

        if(!mcpMappings.exists()) {
            Utils.relaunch(
                    "net.minecraftforge.installertools.ConsoleTool",
                    libInstallerTools,
                    new String[] {
                            "--task",
                            "MCP_DATA",
                            "--input",
                            mcpZip.getAbsolutePath(),
                            "--output",
                            mcpMappings.getAbsolutePath(),
                            "--key",
                            "mappings"
                    },
                    true
            );
        }

        // DOWNLOAD_MOJMAPS
        originOutPrint.println("[FoxLaunch] Downloading vanilla mappings..");

        if (!minecraftMappings.exists()) {
            Utils.relaunch(
                    "net.minecraftforge.installertools.ConsoleTool",
                    libInstallerTools,
                    new String[]{
                            "--task",
                            "DOWNLOAD_MOJMAPS",
                            "--version",
                            minecraftVersion,
                            "--side",
                            "server",
                            "--output",
                            minecraftMappings.getAbsolutePath()
                    },
                    true
            );
        }

        // MERGE_MAPPING
        originOutPrint.println("[FoxLaunch] Merging mappings..");

        if (!mergedMappings.exists()) {
            Utils.relaunch(
                    "net.minecraftforge.installertools.ConsoleTool",
                    libInstallerTools,
                    new String[] {
                            "--task",
                            "MERGE_MAPPING",
                            "--left",
                            mcpMappings.getAbsolutePath(),
                            "--right",
                            minecraftMappings.getAbsolutePath(),
                            "--output",
                            mergedMappings.getAbsolutePath(),
                            "--classes",
                            "--reverse-right"
                    },
                    true
            );
        }

        // JarSplitter
        originOutPrint.println("[FoxLaunch] Splitting server jar..");

        if (Utils.isJarCorrupted(minecraftSlim) || Utils.isJarCorrupted(minecraftExtra)) {
            minecraftSlim.delete();
            minecraftExtra.delete();
        }

        if(!minecraftSlim.exists() || !minecraftExtra.exists()) {
            Utils.relaunch(
                    "net.minecraftforge.jarsplitter.ConsoleTool",
                    libJarSplitter,
                    new String[] {
                            "--input",
                            minecraftUnpack.getAbsolutePath(),
                            "--slim",
                            minecraftSlim.getAbsolutePath(),
                            "--extra",
                            minecraftExtra.getAbsolutePath(),
                            "--srg",
                            mergedMappings.getAbsolutePath()
                    },
                    true
            );
        }

        // ForgeAutoRenamingTool
        originOutPrint.println("[FoxLaunch] Remapping server jar..");

        if(Utils.isJarCorrupted(minecraftSrg)) {
            minecraftSrg.delete();
        }

        if(!minecraftSrg.exists()) {
            Utils.relaunch(
                    "net.minecraftforge.fart.Main",
                    libForgeAutoRenamingTool,
                    new String[] {
                            "--input",
                            minecraftSlim.getAbsolutePath(),
                            "--output",
                            minecraftSrg.getAbsolutePath(),
                            "--names",
                            mergedMappings.getAbsolutePath(),
                            "--ann-fix",
                            "--ids-fix",
                            "--src-fix",
                            "--record-fix"
                    },
                    true
            );
        }

        // BinaryPatcher
        originOutPrint.println("[FoxLaunch] Applying patches..");

        Utils.relaunch(
                "net.minecraftforge.binarypatcher.ConsoleTool",
                libBinaryPatcher,
                new String[] {
                        "--clean",
                        minecraftSrg.getAbsolutePath(),
                        "--output",
                        forgeServerJar.getAbsolutePath(),
                        "--apply",
                        serverLZMA.getAbsolutePath()
                },
                true
        );

        logOutPrint.flush();
        logOutPrint.close();
        System.setOut(originOutPrint);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(installHASH)))) {
            writer.write( Objects.requireNonNull(Utils.getFileSHA256(serverJar), serverJar.getName()) + Objects.requireNonNull(Utils.getFileSHA256(serverLZMA), serverLZMA.getName()));
        }

        return true;
    }
}
