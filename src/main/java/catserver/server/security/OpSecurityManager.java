package catserver.server.security;

import catserver.server.AsyncCatcher;
import catserver.server.CatServer;
import catserver.server.remapper.ReflectionUtils;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Arrays;
import java.util.Map;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandOp;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.craftbukkit.CraftOfflinePlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.plugin.java.PluginClassLoader;

public class OpSecurityManager {
    private static final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    private static final Map<GameProfile, Class<?>[]> profileStackMap = new Object2ObjectArrayMap<>();

    public static void addOpHook(GameProfile profile) {
        if (!CatServer.getConfig().securityOpManager) return;

        if (!AsyncCatcher.isMainThread()) throw new RuntimeException("Async add op!");

        Class<?>[] callerStack = ReflectionUtils.getCallerStack();
        callerStack = Arrays.copyOfRange(callerStack, 4 /* DedicatedPlayerList */ /* ReflectionUtils: 2 OpSecurityManager: 1 DedicatedPlayerList: 1 */, callerStack.length - 1);

        if (callerStack[0] == CommandOp.class) {
            CatServer.log.info("Player {}/{} got op, the stacktrace saved in debug.log", profile.getName(), profile.getId());
            CatServer.log.debug(OpSecurityManager.class.getName(), new Throwable());
            return;
        }

        if (callerStack[0] == CraftPlayer.class || callerStack[0] == CraftOfflinePlayer.class) {
            callerStack = Arrays.copyOfRange(callerStack, 1 /* CraftPlayer/CraftOfflinePlayer: 1 */, callerStack.length - 1);
        }

        profileStackMap.put(profile, callerStack);
    }

    public static void removeOpHook(GameProfile profile) {
        if (!CatServer.getConfig().securityOpManager) return;

        if (!AsyncCatcher.isMainThread()) {
            server.addScheduledTask(() -> server.getPlayerList().removeOp(profile));
            throw new RuntimeException("Async de-op!");
        }

        Class<?>[] callerStack = ReflectionUtils.getCallerStack();
        callerStack = Arrays.copyOfRange(callerStack, 4 /* DedicatedPlayerList */ /* ReflectionUtils: 2 OpSecurityManager: 1 DedicatedPlayerList: 1 */, callerStack.length - 1);

        if (callerStack[0] == CommandDeOp.class) {
            CatServer.log.info("Player {}/{} de-op, the stacktrace saved in debug.log", profile.getName(), profile.getId());
            CatServer.log.debug(OpSecurityManager.class.getName(), new Throwable());
            return;
        }

        profileStackMap.remove(profile);
    }

    public static void tick() {
        if (!CatServer.getConfig().securityOpManager) return;

        profileStackMap.forEach((profile, callerStack) -> {
            try {
                CatServer.log.warn("Detected player {}/{} got op without command. Please check server security!", profile.getName(), profile.getId());

                for (Class<?> stack : callerStack) {
                    if (stack.getClassLoader() instanceof PluginClassLoader) {
                        PluginClassLoader pluginClassLoader = (PluginClassLoader) stack.getClassLoader();
                        CatServer.log.warn("Stacktrace: {} (Plugin: {})", stack.getName(), pluginClassLoader.getDescription().getName());
                    } else {
                        CatServer.log.warn("Stacktrace: {}", stack.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                server.getPlayerList().getBannedPlayers().addEntry(new UserListBansEntry(profile, null, OpSecurityManager.class.getName(), null, null));
                server.getPlayerList().removeOp(profile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(profile.getId());
                if (player != null && player.connection != null) {
                    TextComponentTranslation chatcomponenttext = new TextComponentTranslation("");
                    if (player.connection.netManager != null) {
                        player.connection.netManager.closeChannel(chatcomponenttext);
                        player.connection.netManager.disableAutoRead();
                    }
                    player.connection.onDisconnect(chatcomponenttext);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        profileStackMap.clear();
    }
}
