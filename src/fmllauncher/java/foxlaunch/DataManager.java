package foxlaunch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
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

        try (JarFile serverJar = new JarFile(System.getProperty("java.class.path"))) {
            String classPath = Objects.requireNonNull(serverJar.getManifest().getMainAttributes().getValue(Attributes.Name.CLASS_PATH), "Missing MANIFEST.MF?");
            String[] libraries = classPath.split(" ");
            for (String library : libraries) {
                File file = new File(library);
                librariesMap.put(file.getName(), file.getParentFile());
            }

            if (librariesMap.size() == 0) {
                throw new RuntimeException("Class-Path is empty!");
            }

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
                                        // Only for 1.18+
                                        /*
                                        if (Objects.equals(split[0], "fmlcore") || Objects.equals(split[0], "fmlloader") || Objects.equals(split[0], "javafmllanguage") || Objects.equals(split[0], "mclanguage")) {
                                            path = new File("libraries/net/minecraftforge/" + split[0] + "/" + split[1] + "-" + split[2]);
                                        }
                                        */
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
                            } else if (Objects.equals(name[1], "libraries.txt")) {
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(serverJar.getInputStream(jarEntry)))) {
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        String[] split = line.split("\\|");
                                        if (split.length == 2) {
                                            File libFile = new File(split[0]);
                                            librariesHashMap.put(libFile.getName(), split[1].toUpperCase());
                                            if (!librariesMap.containsKey((libFile.getName())) && libFile.getName().endsWith(".jar")) {
                                                librariesWithoutLaunchMap.put(libFile.getName(), libFile.getParentFile());
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
                String md5 = librariesHashMap.get(filename);

                File file = new File(libraryEntry.getValue(), libraryEntry.getKey());
                if (!file.exists() || (md5 != null && !Objects.equals(Utils.getFileMD5(file), md5))) {
                    needDownloadLibrariesMap.put(file, md5);
                }
            }
        }

        String minecraftVersion = versionData.get("minecraft"), mcpVersion = versionData.get("mcp");
        if (minecraftVersion != null && mcpVersion != null) {
            File mcpZip = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s.zip", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
            if (!mcpZip.exists() || !Objects.equals(Utils.getFileMD5(mcpZip), librariesHashMap.get(mcpZip.getName()))) {
                needDownloadMappingDataMap.put(mcpZip, librariesHashMap.get(mcpZip.getName()));
            }

            File mcpMappings = new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s-mappings.txt", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion));
            String mcpMappingsMD5 = "428EBB172F1E2D80FCC03331DE0EB42E";
            if (!mcpMappings.exists() || !Objects.equals(Utils.getFileMD5(mcpMappings), mcpMappingsMD5)) {
                needDownloadMappingDataMap.put(new File(String.format("libraries/de/oceanlabs/mcp/mcp_config/%s-%s/mcp_config-%s-%s-mappings.packed", minecraftVersion, mcpVersion, minecraftVersion, mcpVersion)), mcpMappingsMD5);
            }
        }

        if (needDownloadLibrariesMap.size() > 0 || needDownloadMappingDataMap.size() > 0) {
            System.out.println(LanguageUtils.I18nToString("launch.lib_missing"));
            LibrariesDownloader.setupDownloadSource();
            for (Map.Entry<File, String> libraryEntry : needDownloadLibrariesMap.entrySet()) {
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

    @Deprecated
    protected static void gc() {
        librariesMap.clear();
        librariesWithoutLaunchMap.clear();
        librariesHashMap.clear();
        foxLaunchLibsMap.clear();
        versionData.clear();

        System.gc();
    }
}
