package catserver.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CatServer {
    public static final String NAME = "CatServer";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final String NATIVE_VERSION = "v1_18_R2";
    public static final String MINECRAFT_VERSION = "1.18.2";

    public static String getNativeVersion() {
        return NATIVE_VERSION;
    }
}