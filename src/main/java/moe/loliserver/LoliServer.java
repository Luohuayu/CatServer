package moe.loliserver;

import catserver.server.CatServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoliServer {
    public static final String NAME = LoliServer.class.getSimpleName();
    @Deprecated
    public static final Logger LOGGER = CatServer.log;
    @Deprecated
    public static final String NATIVE_VERSION = CatServer.getNativeVersion();
}
