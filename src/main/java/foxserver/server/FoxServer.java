package foxserver.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FoxServer {
    public static final String NAME = "FoxServer";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final String NATIVE_VERSION = "v1_18_R2";
    public static final String MINECRAFT_VERSION = "1.18.2";

    public static String getVersion() {
        return (FoxServer.class.getPackage().getImplementationVersion() != null) ? FoxServer.class.getPackage().getImplementationVersion() : "unknown";
    }
}
