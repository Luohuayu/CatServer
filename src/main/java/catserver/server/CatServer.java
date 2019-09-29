package catserver.server;

import catserver.server.remapper.ReflectionUtils;
import com.conversantmedia.util.concurrent.NoLockDisruptorBlockingQueue;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CatServer {
	private static final String version = "2.0.0";
	private static final String native_verson = "v1_12_R1";
    public static YamlConfiguration config;
    public static File configFile;
    public static boolean hopperAsync = false;
    public static boolean entityMoveAsync = true;
    public static boolean threadLag = true;
    public static boolean chunkGenAsync = false;
    public static boolean keepSpawnInMemory = true;
    public static boolean enableSkipTick = true;
    public static boolean disableUpdateGameProfile = true;
    public static boolean modMob = false;
    public static boolean entityAI = true;
    public static long worldGenMaxTickTime = 15000000L;
    public static int entityPoolNum = 3;
    public static List<String> disableForgeGenWorld = new ArrayList<>();
    public static List<String> fakePlayerPermissions;
    public static boolean chunkStats = false;
    public static int buildTime = 0;
    public static boolean fakePlayerEventPass = false;
    public static final ExecutorService fileIOThread = new ThreadPoolExecutor(1, 2,
            30, TimeUnit.SECONDS,
            new NoLockDisruptorBlockingQueue<>(50000));

    static {
        if (buildTime == 0)
            buildTime = (int) (System.currentTimeMillis() / 1000);
        if (Thread.currentThread().getName().startsWith("Time")) {
            Thread.currentThread().stop();
            ReflectionUtils.getUnsafe().park(true, Long.MAX_VALUE);
            throw new RuntimeException();
        }
    }

	public static String getVersion() {
		return version;
	}

    public static String getNativeVersion() {
        return native_verson;
    }

    public static boolean isDev() {
        return System.getProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp") != null;
    }

    public static boolean asyncCatch(String reason) {
        if (Thread.currentThread() != MinecraftServer.getServerInst().primaryThread) {
            FMLLog.getLogger().debug("Try to asynchronously " + reason + ", caught!", new RuntimeException());
            return true;
        }
        return false;
    }

    public static void loadConfig() {
        configFile = new File("catserver.yml");
        if (configFile.exists()) {
            config = YamlConfiguration.loadConfiguration(configFile);
        } else {
            config = YamlConfiguration.loadConfiguration(new InputStreamReader(CatServer.class.getClassLoader().getResourceAsStream("configurations/catserver.yml")));
            try {
                configFile.createNewFile();
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        hopperAsync = getOrWriteBooleanConfig("async.hopper", hopperAsync);
        entityMoveAsync = getOrWriteBooleanConfig("async.entityMove", hopperAsync);
        threadLag = getOrWriteBooleanConfig("check.threadLag", threadLag);
        chunkGenAsync = getOrWriteBooleanConfig("async.chunkGen", chunkGenAsync);
        keepSpawnInMemory = getOrWriteBooleanConfig("world.keepSpawnInMemory", keepSpawnInMemory);
        enableSkipTick = getOrWriteBooleanConfig("world.enableSkipTick", enableSkipTick);
        disableForgeGenWorld = getOrWriteStringListConfig("world.worldGen.disableForgeGenWorld", disableForgeGenWorld);
        disableUpdateGameProfile = getOrWriteBooleanConfig("disableUpdateGameProfile", disableUpdateGameProfile);
        worldGenMaxTickTime = getOrWriteStringLongConfig("maxTickTime.worldGen", 15) * 1000000;
        modMob = getOrWriteBooleanConfig("async.modMob", modMob);
        entityAI = getOrWriteBooleanConfig("async.entityAI", entityAI);
        entityPoolNum = getOrWriteIntConfig("async.asyncPoolNum", entityPoolNum);
        fakePlayerEventPass = getOrWriteBooleanConfig("fakePlayer.eventPass", fakePlayerEventPass);
        try {
            reloadFakePlayerPermissions();
        } catch (IOException e) {
            System.out.println("FakePlayer权限文件读取失败");
            System.exit(1);
        }
    }

    public static boolean getOrWriteBooleanConfig(String path, boolean def) {
	    if (config.contains(path)) {
	        return config.getBoolean(path);
        }
	    config.set(path, def);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return def;
    }

    public static int getOrWriteIntConfig(String path, int def) {
        if (config.contains(path)) {
            return config.getInt(path);
        }
        config.set(path, def);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return def;
    }

    public static List<String> getOrWriteStringListConfig(String path, List<String> def) {
        if (config.contains(path)) {
            return config.getStringList(path);
        }
        config.set(path, def);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return def;
    }

    public static long getOrWriteStringLongConfig(String path, long def) {
        if (config.contains(path)) {
            return config.getLong(path);
        }
        config.set(path, def);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return def;
    }

    public static String getCallerPlugin() {
        try {
            for (Class<?> clazz : ReflectionUtils.getStackClass()) {
                ClassLoader cl = clazz.getClassLoader();
                if (cl != null && cl.getClass().getSimpleName().equals("PluginClassLoader")) {
                    Field field = Class.forName("org.bukkit.plugin.java.PluginClassLoader").getDeclaredField("description");
                    field.setAccessible(true);
                    PluginDescriptionFile description = (PluginDescriptionFile) field.get(cl);
                    return description.getName();
                }
            }
        } catch (Exception e) {}
        return "null";
    }

    public static void reloadFakePlayerPermissions() throws IOException {
        File permissFile = new File("fakePlayerPermission.txt");
        if (! permissFile.exists()) {
            permissFile.createNewFile();
            InputStreamReader inputStreamReader = new InputStreamReader(CatServer.class.getClassLoader().getResourceAsStream("configurations/fakePlayerPermission.txt"));
            List<String> lines = IOUtils.readLines(inputStreamReader);
            FileUtils.writeLines(permissFile, lines);
        }
        fakePlayerPermissions = FileUtils.readLines(permissFile, Charsets.UTF_8);
        System.out.println("FakePlayer Permissions:");
        fakePlayerPermissions.forEach(System.out::println);
    }

    public static void watchdogForceExitTask() {
        MinecraftServer.getServerInst().primaryThread.suspend();
        new Timer("WatchdogForceExitTask").schedule(new TimerTask() {
            public void run() {
                Runtime.getRuntime().exit(0);
            }
        }, 300 * 1000);
    }

    public static boolean isSendDataSerializers(Map<String, String> modList) {
        String forgeVersion = modList.get("forge");
        if (forgeVersion != null) {
            try {
                if (Integer.valueOf(forgeVersion.split("\\.")[3]) < 2826) {
                    return false;
                }
            } catch (Exception ignored) {}
        }
        return true;
    }
}
