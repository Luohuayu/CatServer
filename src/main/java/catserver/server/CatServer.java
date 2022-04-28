package catserver.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CatServer {
    public static final Logger log = LogManager.getLogger("CatServer");
    private static final String version = "1.16.5";
    private static final String native_version = "v1_16_R3";

    private static final CatServerConfig config = new CatServerConfig("catserver.yml");

    @Deprecated
    public static String getVersion(){
        return version;
    }

    public static String getNativeVersion() {
        return native_version;
    }

    public static CatServerConfig getConfig() {
        return config;
    }
}
