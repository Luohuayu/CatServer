package catserver.server;

import catserver.server.threads.AsyncChatThread;
import catserver.server.threads.AsyncTaskThread;
import catserver.server.threads.RealtimeThread;
import catserver.server.utils.VersionCheck;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CatServer {
    public static final Logger log = LogManager.getLogger("CatServer");
    private static final String version = "2.1.0";
    private static final String native_version = "v1_12_R1";

    private static final CatServerConfig config = new CatServerConfig("catserver.yml");

    public static String getVersion(){
        return version;
    }

    public static String getNativeVersion() {
        return native_version;
    }

    public static void onServerStart() {
        RealtimeThread.INSTANCE.start();
        new VersionCheck();
    }

    public static void onServerStop() {
        AsyncTaskThread.shutdown();
        AsyncChatThread.shutdown();
    }

    public static void onWorldDataLave(SaveHandler handler, WorldInfo worldInfo, NBTTagCompound tagCompound) {
        NBTTagCompound catserverData = tagCompound.getCompoundTag("catserver");
        BukkitWorldDimensionManager.load(catserverData);
    }

    public static void onWorldDataSave(SaveHandler handler, WorldInfo worldInfo, NBTTagCompound tagCompound) {
        NBTTagCompound catserverData = new NBTTagCompound();
        BukkitWorldDimensionManager.save(catserverData);
        tagCompound.setTag("catserver", catserverData);
    }

    public static CatServerConfig getConfig() {
        return config;
    }

    public static boolean asyncCatch(String reason) {
        return AsyncCatcher.checkAsync(reason);
    }

    public static void postPrimaryThread(Runnable runnable) {
        MinecraftServer.getServerInst().processQueue.add(runnable);
    }

    public static void scheduleAsyncTask(Runnable runnable) {
        AsyncTaskThread.schedule(runnable);
    }

    public static int getCurrentTick() {
        return getConfig().enableRealtime ? RealtimeThread.currentTick : MinecraftServer.currentTick;
    }
}
