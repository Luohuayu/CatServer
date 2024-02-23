package catserver.server.utils;

import catserver.server.CatServer;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class VersionChecker {
    private static final String api = "https://catserver.moe/api/version/?v=catserver_1_18_2";

    public VersionChecker() {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (catserver.server.CatServer.getConfig().versionCheck) {
                    versionCheck();
                }
            }
        }, 60 * 1000 , 6 * 3600 * 1000);
    }

    private void versionCheck() {
        String currentVersion = getCurrentVersion();
        if (currentVersion != null) {
            try {
                VersionData versionData = new Gson().fromJson(sendSSLRequest(api), VersionData.class);
                if (!Strings.isNullOrEmpty(versionData.version) && !currentVersion.equals(versionData.version)) {
                    CatServer.LOGGER.info(String.format("Check CatServer has a new version: %s, you can download the update from https://catserver.moe/download/catserver_1_18_2 or set jvm param to disable the version check (-Dcatserver.disableVersionCheck=false)", versionData.version));
                }
                if (!Strings.isNullOrEmpty(versionData.message)) {
                    CatServer.LOGGER.info(versionData.message);
                }
            } catch (Exception e) {
                CatServer.LOGGER.warn(String.format("VersionCheck exception: %s", e.toString()));
            }
        }
    }

    private String getCurrentVersion() {
        String implementationVersion = CatServer.class.getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            String[] split = implementationVersion.split("-");
            if (split.length == 2) {
                return split[1];
            }
        }
        return null;
    }

    private String sendSSLRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Connection", "Close");
        connection.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String result = "";
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        in.close();
        return result;
    }

    private static class VersionData {
        public String version;
        public String message;
    }
}
