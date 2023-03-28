package foxlaunch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DataManager {
    private static final Map<String, File> librariesMap = new TreeMap<>();
    private static final Map<String, File> librariesWithoutLaunchMap = new TreeMap<>();
    private static final Map<String, String> librariesHashMap = new TreeMap<>();
    private static final Map<String, File> foxLaunchLibsMap = new TreeMap<>();
    private static final Map<String, String> versionData = new TreeMap<>();

    public static void setup() {
        File foxLaunchLibs = new File("foxlaunch-libs");
        if (!foxLaunchLibs.exists()) {
            foxLaunchLibs.mkdirs();
        }

        File foxLaunchData = new File("foxlaunch-data");
        if (!foxLaunchData.exists()) {
            foxLaunchData.mkdirs();
        }

        try (JarFile serverJar = new JarFile(Utils.findServerJar())) {
            String classPath = Objects.requireNonNull(serverJar.getManifest().getMainAttributes().getValue("Installer-Class-Path"), "Missing MANIFEST.MF?");
            String[] libraries = classPath.split(" ");
            for (String library : libraries) {
                File file = new File(library);
                librariesWithoutLaunchMap.put(file.getName(), file.getParentFile());
            }

            if (librariesWithoutLaunchMap.size() == 0) {
                throw new RuntimeException("Installer Class-Path is empty!");
            }

            librariesWithoutLaunchMap.put("minecraft_server.1.18.2.jar", new File("foxlaunch-data/"));
            librariesWithoutLaunchMap.put("commons-lang-2.6.jar", new File("libraries/commons-lang/commons-lang/2.6/"));
            librariesMap.put("bootstraplauncher-1.0.0.jar", new File("libraries/cpw/mods/bootstraplauncher/1.0.0/"));

            versionData.put("minecraft", Objects.requireNonNull(serverJar.getManifest().getAttributes("net/minecraftforge/versions/mcp/").getValue(Attributes.Name.SPECIFICATION_VERSION)));
            versionData.put("mcp", Objects.requireNonNull(serverJar.getManifest().getAttributes("net/minecraftforge/versions/mcp/").getValue(Attributes.Name.IMPLEMENTATION_VERSION)));
            versionData.put("forge", Objects.requireNonNull(serverJar.getManifest().getAttributes("net/minecraftforge/versions/forge/").getValue(Attributes.Name.IMPLEMENTATION_VERSION)));

            Enumeration<JarEntry> entry = serverJar.entries();
            while (entry.hasMoreElements()) {
                JarEntry jarEntry = entry.nextElement();
                if (jarEntry != null) {
                    String[] name = jarEntry.getName().split("/");
                    if (name.length == 2) {
                        if (Objects.equals(name[0], "data")) {
                            if (name[1].endsWith(".jar")) {
                                File path = librariesMap.get(name[1]);

                                if (path == null) {
                                    String[] split = name[1].substring(0, name[1].length() - 4).split("-");
                                    if (split.length == 3) {
                                        if (Objects.equals(split[0], "fmlcore") || Objects.equals(split[0], "fmlloader") || Objects.equals(split[0], "javafmllanguage") || Objects.equals(split[0], "mclanguage") || Objects.equals(split[0], "lowcodelanguage")) {
                                            path = new File("libraries/net/minecraftforge/" + split[0] + "/" + split[1] + "-" + split[2]);
                                        }
                                    } else if (split.length == 4) {
                                        if (Objects.equals(split[0], "forge") && Objects.equals(split[3], "universal")) {
                                            path = new File("libraries/net/minecraftforge/" + split[0] + "/" + split[1] + "-" + split[2]);
                                        }
                                    }
                                }

                                boolean isCustomLib = false;
                                if (path == null) {
                                    path = foxLaunchLibs;
                                    foxLaunchLibsMap.put(name[1], path);
                                    isCustomLib = true;
                                } else {
                                    if (!path.exists()) {
                                        path.mkdirs();
                                    }
                                }

                                File file = new File(path, name[1]);
                                if (isCustomLib && file.exists() && !Utils.isJarCorrupted(file)) {
                                    continue;
                                }

                                try (OutputStream out = new FileOutputStream(file)) {
                                    try (InputStream in = serverJar.getInputStream(jarEntry)) {
                                        byte[] bytes = new byte[4096];
                                        int readSize;
                                        while ((readSize = in.read(bytes)) > 0) {
                                            out.write(bytes, 0, readSize);
                                        }
                                    }
                                    out.flush();
                                }
                            } else if (Objects.equals(name[1], "server.lzma")) {
                                try (OutputStream out = new FileOutputStream(new File(foxLaunchData, name[1]))) {
                                    try (InputStream in = serverJar.getInputStream(jarEntry)) {
                                        byte[] bytes = new byte[4096];
                                        int readSize;
                                        while ((readSize = in.read(bytes)) > 0) {
                                            out.write(bytes, 0, readSize);
                                        }
                                    }
                                    out.flush();
                                }
                            } else if (Objects.equals(name[1], "libraries-launch.txt")) {
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(serverJar.getInputStream(jarEntry)))) {
                                    String line = reader.readLine();
                                    if (line.startsWith("libraries/")) {
                                        String[] split = line.split(";");
                                        for (String library : split) {
                                            File file = new File(library);
                                            librariesMap.put(file.getName(), file.getParentFile());
                                        }
                                    }
                                }
                            } else if (Objects.equals(name[1], "libraries-all.txt")) {
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(serverJar.getInputStream(jarEntry)))) {
                                    String line = reader.readLine();
                                    if (Objects.equals(line, "===ALGORITHM SHA-256")) {
                                        while ((line = reader.readLine()) != null) {
                                            String[] split = line.split(" ");
                                            if (split.length == 2) {
                                                String[] split2 = split[0].split(":");
                                                if (split2.length == 3) {
                                                    librariesHashMap.put(split2[1] + "-" + split2[2] + ".jar", split[1]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not unpack data!", e);
        }
    }

    public static void downloadLibraries() {
        Map<File, String> needDownloadLibrariesMap = new TreeMap<>();
        Map<File, String> needDownloadMappingDataMap = new TreeMap<>();

        for (Map<String, File> librariesMap : new Map[]{ librariesMap, librariesWithoutLaunchMap }) {
            for (Map.Entry<String, File> libraryEntry : librariesMap.entrySet()) {
                String filename = libraryEntry.getKey();
                String sha256 = librariesHashMap.get(filename);

                sha256 = sha256 == null ? Utils.getMissingSHA256(filename) : sha256.toUpperCase();

                File file = new File(libraryEntry.getValue(), libraryEntry.getKey());
                if (!file.exists() || (sha256 != null && !Objects.equals(Utils.getFileSHA256(file), sha256))) {
                    needDownloadLibrariesMap.put(file, sha256);
                }
            }
        }

        String minecraftVersion = versionData.get("minecraft"), mcpVersion = versionData.get("mcp");
        if (minecraftVersion != null && mcpVersion != null) {
            File mcpZip = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s.zip", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
            if (!mcpZip.exists() || !Objects.equals(Utils.getFileSHA256(mcpZip), Utils.getMissingSHA256(mcpZip.getName()))) {
                needDownloadMappingDataMap.put(mcpZip, Utils.getMissingSHA256(mcpZip.getName()));
            }

            File minecraftMappings = new File(String.format("foxlaunch-data/server-%s-mappings.txt", minecraftVersion));
            if (!minecraftMappings.exists() || !Objects.equals(Utils.getFileSHA256(minecraftMappings), Utils.getMissingSHA256(minecraftMappings.getName()))) {
                needDownloadMappingDataMap.put(new File(String.format("foxlaunch-data/server-%s-mappings.packed", minecraftVersion)), Utils.getMissingSHA256(minecraftMappings.getName()));
            }
        }

        if (needDownloadLibrariesMap.size() > 0 || needDownloadMappingDataMap.size() > 0) {
            System.out.println(LanguageUtils.I18nToString("launch.lib_missing"));
            LibrariesDownloader.setupDownloadSource();
            for (Map.Entry<File, String> libraryEntry : needDownloadLibrariesMap.entrySet()) {
                if (Objects.equals(libraryEntry.getKey().getName(), "server-" + versionData.get("minecraft") + "-" + versionData.get("mcp") + "-extra.jar")) {
                    continue;
                }
                LibrariesDownloader.tryDownload(libraryEntry.getKey(), libraryEntry.getValue());
            }
            for (Map.Entry<File, String> libraryEntry : needDownloadMappingDataMap.entrySet()) {
                LibrariesDownloader.tryDownload(libraryEntry.getKey(), libraryEntry.getValue(), "mappings-data");
            }
            System.out.println(LanguageUtils.I18nToString("launch.lib_download_completed"));
        }
    }

    public static Map<String, File> getLibrariesMap() {
        return librariesMap;
    }

    public static String getVersionData(String target) {
        return versionData.get(target);
    }

    protected static void gc() {
        librariesMap.clear();
        librariesWithoutLaunchMap.clear();
        librariesHashMap.clear();
        foxLaunchLibsMap.clear();
        versionData.clear();

        System.gc();
    }
}
