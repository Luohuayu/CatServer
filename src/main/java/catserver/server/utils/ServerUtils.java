package catserver.server.utils;

import catserver.server.CatServer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import org.spigotmc.AsyncCatcher;

public class ServerUtils {
    public static void forceSaveWorlds() {
        CatServer.log.info("Force save worlds:");
        // boolean oldAsyncCatcher = AsyncCatcher.enabled;
        // AsyncCatcher.enabled = false;

        try {
            CatServer.log.info("Force saving players..");
            MinecraftServer.getServer().getPlayerList().saveAll();

            CatServer.log.info("Force saving chunks..");
            for (ServerWorld world : MinecraftServer.getServer().getAllLevels()) {
                try {
                    world.noSave = false;
                    world.save(null, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // AsyncCatcher.enabled = oldAsyncCatcher;
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
