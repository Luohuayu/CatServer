package catserver.server.utils;

import catserver.server.CatServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import org.spigotmc.AsyncCatcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ServerUtils {
    public static void forceSaveWorlds() {
        CatServer.log.info("Force save worlds:");
        boolean oldAsyncCatcher = AsyncCatcher.enabled;
        AsyncCatcher.enabled = false;

        try {
            CatServer.log.info("Force saving players..");
            MinecraftServer.getServerInst().getPlayerList().saveAllPlayerData();

            CatServer.log.info("Force saving chunks..");
            for (WorldServer worldServer : MinecraftServer.getServerInst().worldServerList) {
                try {
                    worldServer.saveAllChunks(true, null);
                    worldServer.flushToDisk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AsyncCatcher.enabled = oldAsyncCatcher;
        CatServer.log.info("Force save complete!");
    }

    public static void acceptEula() {
        Properties properties = new Properties();
        try (FileInputStream fileinputstream = new FileInputStream("eula.txt")) {
            properties.load(fileinputstream);
        } catch (Exception ignored) { }

        if (!"true".equals(properties.getProperty("eula"))) {
            try (FileOutputStream fileoutputstream = new FileOutputStream("eula.txt")) {
                properties.setProperty("eula", "true");
                properties.store(fileoutputstream, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
            } catch (Exception e) {
                CatServer.log.warn(e.toString());
            }
        }
    }
}
