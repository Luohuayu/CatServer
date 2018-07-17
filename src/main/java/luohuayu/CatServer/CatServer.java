package luohuayu.CatServer;

import org.spigotmc.RestartCommand;

public class CatServer {
	private static final String versions = "1.0.6";

	public static String getVersions(){
		return versions;
	}

    public static void restart() {
		RestartCommand.restart();
    }
    
    public static String getNativeVersion() {
        return "v1_10_R1";
    }
}
