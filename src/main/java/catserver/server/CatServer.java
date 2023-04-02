package catserver.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CatServer {
    public static final Logger LOGGER = LogManager.getLogger("CatServer");
    public static final String NATIVE_VERSION = "v1_18_R2";
    private static final CatServerConfig config = new CatServerConfig("catserver.yml");

    public static CatServerConfig getConfig() {
        return config;
    }
}
