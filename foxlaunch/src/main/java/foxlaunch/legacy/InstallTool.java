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
    public static void install(String minecraftVersion, String mcpVersion, String forgeVersion) throws Exception {
        if(minecraftVersion == null || mcpVersion == null || forgeVersion == null) {
            throw new RuntimeException(String.format("Missing version data: [minecraft: %s, mcp: %s, forge: %s]", minecraftVersion, mcpVersion, forgeVersion));
        }

        URL[] libInstallerTools = new URL[] {
                Utils.pathToURL("foxlaunch-libs/installertools-1.2.10.jar"),
                Utils.pathToURL("foxlaunch-libs/fastcsv-2.0.0.jar"),
                Utils.pathToURL("foxlaunch-libs/srgutils-0.4.11.jar"),
                Utils.pathToURL("libraries/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar"),
                Utils.pathToURL("libraries/net/md-5/SpecialSource/1.10.0/SpecialSource-1.10.0.jar"),
                Utils.pathToURL("libraries/com/opencsv/opencsv/4.4/opencsv-4.4.jar"),
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
                Utils.pathToURL("libraries/net/sf/opencsv/opencsv/4.4/opencsv-4.4.jar"),
                Utils.pathToURL("libraries/com/google/guava/guava/31.0.1-jre/guava-31.0.1-jre.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-commons/9.2/asm-commons-9.2.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-analysis/9.2/asm-analysis-9.2.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-tree/9.2/asm-tree-9.2.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm/9.2/asm-9.2.jar")
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
                Utils.pathToURL("libraries/org/ow2/asm/asm-commons/9.2/asm-commons-9.2.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-analysis/9.2/asm-analysis-9.2.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-tree/9.2/asm-tree-9.2.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm/9.2/asm-9.2.jar")
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

        File serverJar = new File(System.getProperty("java.class.path"));
        File serverLZMA = new File("foxlaunch-data/server.lzma");

        File mcpZip = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s.zip", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
        File mcpMappings = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s-mappings.txt", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));

        File minecraftServer = new File("foxlaunch-data/minecraft_server." + minecraftVersion + ".jar");
        File minecraftUnpack = new File(String.format("libraries/net/minecraft/server/%s/server-%s.jar", minecraftVersion, minecraftVersion));
        File minecraftMappings = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-mappings.txt", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));

        File mergedMappings = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-mappings-merged.txt", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));

        File minecraftExtra = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-extra.jar", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
        File minecraftSlim = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-slim.jar", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
        File minecraftSrg = new File(String.format("libraries/net/minecraft/server/%s-%s/server-%s-%s-srg.jar", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));

        File forgeUniversalJar = new File(String.format("libraries/net/minecraftforge/forge/%s-%s/forge-%s-%s-universal.jar", minecraftVersion, forgeVersion, minecraftVersion, forgeVersion));
        File forgeServerJar = new File(String.format("libraries/net/minecraftforge/forge/%s-%s/forge-%s-%s-server.jar", minecraftVersion, forgeVersion, minecraftVersion, forgeVersion));

        try {
            if (installHASH.exists()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(installHASH)))) {
                    if (Objects.equals(reader.readLine(), Objects.requireNonNull(Utils.getFileSHA256(serverJar), serverJar.getName()) + Utils.getFileSHA256(serverLZMA) + Utils.getFileSHA256(forgeUniversalJar) + Utils.getFileSHA256(forgeServerJar))) {
                        return;
                    }
                }
            }
        } catch (Exception ignored) {}

        PrintStream originOutPrint = System.out;
        PrintStream logOutPrint = new PrintStream(new BufferedOutputStream(new FileOutputStream(installLog)));
        System.setOut(logOutPrint);

        // BUNDLER_EXTRACT
        originOutPrint.println("Unpacking vanilla server jar..");

        if(Utils.isJarCorrupted(minecraftUnpack)) {
            minecraftUnpack.delete();
        }

        if (!minecraftUnpack.exists()) {
            Utils.relaunch(
                    "net.minecraftforge.installertools.ConsoleTool",
                    libInstallerTools,
                    new String[] {
                            "--task",
                            "BUNDLER_EXTRACT",
                            "--input",
                            minecraftServer.getAbsolutePath(),
                            "--output",
                            minecraftUnpack.getAbsolutePath(),
                            "--jar-only"
                    },
                    true
            );
        }

        // MCP_DATA
        originOutPrint.println("Initializing MCP data..");

        if (!mcpZip.exists()) {
            throw new RuntimeException("Missing MCP Data!");
        }

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
        originOutPrint.println("Downloading vanilla mappings..");

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
        originOutPrint.println("Merging mappings..");

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
        originOutPrint.println("Splitting server jar..");

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
        originOutPrint.println("Remapping server jar..");

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
        originOutPrint.println("Applying patches..");

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
            writer.write( Objects.requireNonNull(Utils.getFileSHA256(serverJar), serverJar.getName()) + Objects.requireNonNull(Utils.getFileSHA256(serverLZMA), serverLZMA.getName()) + Objects.requireNonNull(Utils.getFileSHA256(forgeUniversalJar), forgeUniversalJar.getName()) + Objects.requireNonNull(Utils.getFileSHA256(forgeServerJar), forgeServerJar.getName()));
        }
    }
}
