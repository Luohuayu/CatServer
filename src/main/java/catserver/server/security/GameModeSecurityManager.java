package catserver.server.security;

import catserver.server.CatServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameType;

public class GameModeSecurityManager {
    public static void setGameModeHook(EntityPlayerMP player, GameType type) {
        if (!CatServer.getConfig().securityGameModeManager) return;

        if (type == GameType.CREATIVE) {
            CatServer.log.warn("Player {}/{} change game mode {} to {}, the stacktrace saved in debug.log", player.getName(), player.getGameProfile().getId(), player.interactionManager.getGameType().getName(), type.getName());
            CatServer.log.debug(GameModeSecurityManager.class.getName(), new Throwable());
        }
    }
}
