package catserver.server;

import catserver.server.utils.LanguageUtils;
import catserver.server.utils.Md5Utils;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LibrariesManager {
    private static List<String> librariesSources = new ArrayList<>();

    public static void checkLibraries() {
        File libDir = new File("libraries");
        if (!libDir.exists()) libDir.mkdir();

        InputStream listStream = ClassLoader.getSystemResourceAsStream("libraries.info");
        if (listStream == null) return;

        boolean hasException = false;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(listStream));
            String str = null;
            while((str = bufferedReader.readLine()) != null)
            {
                String[] args = str.split("\\|");
                if (args.length == 3) {
                    try {
                        checkAndDownloadLibrary(args[0], args[1], args[2]);
                    } catch (IOException e2) {
                        System.out.println(e2.toString());
                        hasException = true;
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            hasException = true;
        }
        if (hasException) {
            System.out.println(LanguageUtils.I18nToString("launch.lib_exception"));
        }
    }

    private static void checkAndDownloadLibrary(String type, String key, String value) throws IOException {
        switch (type) {
            case "source": {
                librariesSources.add(key);
                break;
            }
            case "lib": {
                File file = new File(key);
                if (!file.exists() || !Md5Utils.getFileMD5String(file).equals(value))
                {
                    tryDownload(file, value);
                }
                break;
            }
        }
    }

    private static boolean tryDownload(File file, String md5) {
        Iterator<String> iterator = librariesSources.iterator();
        while(iterator.hasNext()) {
            String downloadUrl = iterator.next() + file.getName();
            try {
                new Downloader(downloadUrl, file);

                if (!file.exists() || (md5 != null && !Md5Utils.getFileMD5String(file).equals(md5))) {
                    System.out.println(String.format(LanguageUtils.I18nToString("launch.lib_failure_check"), file.getName(), downloadUrl));
                    continue;
                }
                return true;
            } catch (IOException e) {
                System.out.println(String.format(LanguageUtils.I18nToString("launch.lib_failure_download"), e.toString(), downloadUrl));
                if (e instanceof ConnectException || e instanceof SocketTimeoutException) iterator.remove();
            }
        }
        return false;
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
