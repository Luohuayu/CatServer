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

public class VersionCheck {
    private static final String api = "https://catserver.moe/api/version/?v=catserver_1_16_5";

    public VersionCheck() {
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
                    CatServer.log.info(String.format(foxlaunch.LanguageUtils.I18nToString("versioncheck.new_version"), versionData.version));
                }
                if (!Strings.isNullOrEmpty(versionData.message)) {
                    CatServer.log.info(versionData.message);
                }
            } catch (Exception e) {
                CatServer.log.warn(String.format(foxlaunch.LanguageUtils.I18nToString("versioncheck.failure"), e.toString()));
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
