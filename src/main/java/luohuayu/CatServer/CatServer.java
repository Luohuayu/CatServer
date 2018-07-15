package luohuayu.CatServer;

import org.spigotmc.RestartCommand;

public class CatServer {
	private static final String versions = "1.0.2";

	public static String getVersions(){
		return versions;
	}

    public static void restart() {
		RestartCommand.restart();
    }
}
