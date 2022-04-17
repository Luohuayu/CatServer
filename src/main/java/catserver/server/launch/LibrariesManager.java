package catserver.server.launch;

import catserver.server.utils.LanguageUtils;
import catserver.server.utils.Md5Utils;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LibrariesManager {
    private static final List<String> librariesSources = new ArrayList<>();
    public static final String sparkPluginFileName = "spark-1.8.19-bukkit.jar";
    public static final String sparkPluginMD5 = "ab5e7e1cd1bcd7cc910c2b7a59e7b7e5";

    public static void checkLibraries() {
        File jarDir = findJarDir();

        File libDir = new File(jarDir, "libraries");
        if (!libDir.exists()) libDir.mkdir();

        InputStream listStream = ClassLoader.getSystemResourceAsStream("libraries.info");
        if (listStream == null) return;

        updateMCServerJar(jarDir, libDir);

        Map<File, String> librariesNeedDownload = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(listStream))) {
            String str = null;
            while((str = bufferedReader.readLine()) != null)
            {
                String[] args = str.split("\\|");
                if (args.length == 2) {
                    String key = args[0];
                    String value = args[1];

                    try {
                        File file = new File(jarDir, key);
                        if (!file.exists() || !Md5Utils.getFileMD5String(file).equals(value)) {
                            librariesNeedDownload.put(file, value);
                        }
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                }
            }
            if (Boolean.parseBoolean(System.getProperty("catserver.spark.enable", "true"))) {
                File sparkPluginFile = new File(libDir, sparkPluginFileName);
                if (!sparkPluginFile.exists() || !Md5Utils.getFileMD5String(sparkPluginFile).equals(sparkPluginMD5)) {
                    librariesNeedDownload.put(sparkPluginFile, sparkPluginMD5);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        if (librariesNeedDownload.size() > 0) {
            System.out.println(LanguageUtils.I18nToString("launch.lib_missing"));
            loadDownloadSources();
            for (Map.Entry<File, String> entry : librariesNeedDownload.entrySet()) {
                tryDownload(entry.getKey(), entry.getValue());
            }
            System.out.println(LanguageUtils.I18nToString("launch.lib_download_completed"));
        }
    }

    private static File findJarDir() {
        try {
            URL jarUrl = LibrariesManager.class.getProtectionDomain().getCodeSource().getLocation();
            File jarFile = new File(URLDecoder.decode(jarUrl.getPath(), "UTF-8"));
            if (jarFile.isFile()) {
                return jarFile.getParentFile().getAbsoluteFile();
            }
        } catch (Exception ignored) { }
        return new File(".");
    }

    private static void loadDownloadSources() {
        try {
            String str = sendRequest("https://catserver.moe/api/libraries_sources/");
            for (String s : str.split("\\|")) {
                if (s.startsWith("http://") || s.startsWith("https://")) {
                    librariesSources.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        if (librariesSources.size() == 0) {
            librariesSources.add("http://sv.catserver.moe:8001/dl/");
            librariesSources.add("http://sv2.catserver.moe:8001/dl/");
            librariesSources.add("http://cdn.catserver.moe/dl/");
        }
    }

    private static void tryDownload(File file, String md5) {
        Iterator<String> iterator = librariesSources.iterator();
        while(iterator.hasNext()) {
            String downloadUrl = iterator.next() + file.getName();
            try {
                new Downloader(downloadUrl, file);

                if (!file.exists() || (md5 != null && !Md5Utils.getFileMD5String(file).equals(md5))) {
                    System.out.println(String.format(LanguageUtils.I18nToString("launch.lib_failure_check"), file.getName(), downloadUrl));
                }
                return;
            } catch (IOException e) {
                System.out.println(String.format(LanguageUtils.I18nToString("launch.lib_failure_download"), e.toString(), downloadUrl));
                if (e instanceof ConnectException || e instanceof SocketTimeoutException) iterator.remove();
            }
        }
    }

    private static String sendRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Connection", "Close");
        connection.connect();

        String result = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }

        return result;
    }

    private static void updateMCServerJar(File currentJarPath, File librariesPath) {
        File oldMCServerJar = new File(currentJarPath, "minecraft_server.1.12.2.jar");
        File targetMCServerJar = new File(librariesPath, "minecraft_server.1.12.2.jar");
        try {
            if (oldMCServerJar.exists()) {
                if (!targetMCServerJar.exists()) {
                    oldMCServerJar.renameTo(targetMCServerJar);
                } else {
                    oldMCServerJar.delete();
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    static class Downloader {
        public Downloader(String downloadUrl, File saveFile) throws IOException {
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setRequestMethod("GET");

            System.out.println(String.format(LanguageUtils.I18nToString("launch.lib_downloading"), saveFile.getName(), getSize(connection.getContentLengthLong())));

            ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
            FileOutputStream fos = new FileOutputStream(saveFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            rbc.close();
            fos.close();

            connection.disconnect();
        }

        private String getSize(long size) {
            if (size >= 1048576L) {
                return size / 1048576.0F + " MB";
            }
            if (size >= 1024) {
                return size / 1024.0F + " KB";
            }
            return size + " B";
        }
    }
}
