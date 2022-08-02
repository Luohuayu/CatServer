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
        if (minecraftVersion == null || mcpVersion == null || forgeVersion == null) {
            throw new RuntimeException(String.format("Missing version data: [minecraft: %s, mcp: %s, forge: %s]", minecraftVersion, mcpVersion, forgeVersion));
        }

        URL[] libInstallerTools = new URL[] {
                Utils.pathToURL("libraries/net/minecraftforge/installertools/1.1.11/installertools-1.1.11.jar"),
                /* fastcsv */
                Utils.pathToURL("libraries/de/siegmar/fastcsv/1.0.2/fastcsv-1.0.2.jar"),
                /* gson */
                Utils.pathToURL("libraries/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar"),
                /* guava */
                Utils.pathToURL("libraries/com/google/guava/guava/25.1-jre/guava-25.1-jre.jar"),
                Utils.pathToURL("libraries/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar"),
                Utils.pathToURL("libraries/com/google/errorprone/error_prone_annotations/2.1.3/error_prone_annotations-2.1.3.jar"),
                Utils.pathToURL("libraries/com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar"),
                Utils.pathToURL("libraries/org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar"),
                /* SpecialSource */
                Utils.pathToURL("libraries/net/md-5/SpecialSource/1.8.5/SpecialSource-1.8.5.jar"),
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
                Utils.pathToURL("libraries/net/sf/opencsv/opencsv/2.3/opencsv-2.3.jar"),
                /* asm */
                Utils.pathToURL("libraries/org/ow2/asm/asm-commons/9.1/asm-commons-9.1.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-analysis/6.1.1/asm-analysis-9.1.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-tree/9.1/asm-tree-9.1jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm/9.1/asm-9.1.jar")
        };

        URL[] libJarSplitter = new URL[] {
                Utils.pathToURL("libraries/net/minecraftforge/jarsplitter/1.1.2/jarsplitter-1.1.2.jar"),
                /* jopt-simple */
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
        };

        URL[] libSpecialSource = new URL[] {
                Utils.pathToURL("libraries/net/md-5/SpecialSource/1.8.5/SpecialSource-1.8.5.jar"),
                /* jopt-simple */
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
                /* opencsv */
                Utils.pathToURL("libraries/net/sf/opencsv/opencsv/2.3/opencsv-2.3.jar"),
                /* guava */
                Utils.pathToURL("libraries/com/google/guava/guava/25.1-jre/guava-25.1-jre.jar"),
                Utils.pathToURL("libraries/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar"),
                Utils.pathToURL("libraries/com/google/errorprone/error_prone_annotations/2.1.3/error_prone_annotations-2.1.3.jar"),
                Utils.pathToURL("libraries/com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar"),
                Utils.pathToURL("libraries/org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar"),
                /* asm */
                Utils.pathToURL("libraries/org/ow2/asm/asm-commons/6.1.1/asm-commons-6.1.1.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-analysis/6.1.1/asm-analysis-6.1.1.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm-tree/6.1.1/asm-tree-6.1.1.jar"),
                Utils.pathToURL("libraries/org/ow2/asm/asm/6.1.1/asm-6.1.1.jar")
        };

        URL[] libBinaryPatcher = new URL[] {
                Utils.pathToURL("libraries/net/minecraftforge/binarypatcher/1.0.12/binarypatcher-1.0.12.jar"),
                /* commons-io */
                Utils.pathToURL("libraries/commons-io/commons-io/2.4/commons-io-2.4.jar"),
                /* jopt-simple */
                Utils.pathToURL("libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"),
                /* guava */
                Utils.pathToURL("libraries/com/google/guava/guava/25.1-jre/guava-25.1-jre.jar"),
                Utils.pathToURL("libraries/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar"),
                Utils.pathToURL("libraries/com/google/errorprone/error_prone_annotations/2.1.3/error_prone_annotations-2.1.3.jar"),
                Utils.pathToURL("libraries/com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar"),
                Utils.pathToURL("libraries/org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar"),
                /* lzma */
                Utils.pathToURL("libraries/com/github/jponge/lzma-java/1.3/lzma-java-1.3.jar"),
                Utils.pathToURL("libraries/com/nothome/javaxdelta/2.0.1/javaxdelta-2.0.1.jar"),
                Utils.pathToURL("libraries/org/checkerframework/checker-qual/2.0.0/checker-qual-2.0.0.jar"),
                Utils.pathToURL("libraries/trove/trove/1.0.2/trove-1.0.2.jar")
        };

        File installHASH = new File("foxlaunch-data/install.dat");
        File installLog = new File("foxlaunch-data/install.log");

        File serverJar = Utils.findServerJar();
        File serverLZMA = new File("foxlaunch-data/server.lzma");

        File mcpZip = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s.zip", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
        File mcpMappings = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s-mappings.txt", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));

        File minecraftServer = new File("foxlaunch-data/minecraft_server." + minecraftVersion + ".jar");

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
                        return;
                    }
                }
            }
        } catch (Exception ignored) {}


        PrintStream originOutPrint = System.out;
        try (PrintStream logOutPrint = new PrintStream(new BufferedOutputStream(new FileOutputStream(installLog)))) {
            System.setOut(logOutPrint);

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

            logOutPrint.flush();

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
                                minecraftServer.getAbsolutePath(),
                                "--slim",
                                minecraftSlim.getAbsolutePath(),
                                "--extra",
                                minecraftExtra.getAbsolutePath(),
                                "--srg",
                                mcpMappings.getAbsolutePath()
                        },
                        true
                );
            }

            logOutPrint.flush();

            // SpecialSource
            originOutPrint.println("[FoxLaunch] Remapping server jar..");

            if(Utils.isJarCorrupted(minecraftSrg)) {
                minecraftSrg.delete();
            }

            if(!minecraftSrg.exists()) {
                Utils.relaunch(
                        "net.md_5.specialsource.SpecialSource",
                        libSpecialSource,
                        new String[] {
                                "--in-jar",
                                minecraftSlim.getAbsolutePath(),
                                "--out-jar",
                                minecraftSrg.getAbsolutePath(),
                                "--srg-in",
                                mcpMappings.getAbsolutePath()
                        },
                        true
                );
            }

            logOutPrint.flush();

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
        } finally {
            System.setOut(originOutPrint);
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(installHASH)))) {
            writer.write( Objects.requireNonNull(Utils.getFileSHA256(serverJar), serverJar.getName()) + Objects.requireNonNull(Utils.getFileSHA256(serverLZMA), serverLZMA.getName()));
        }
    }
}
