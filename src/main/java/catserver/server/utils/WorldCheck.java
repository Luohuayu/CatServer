package catserver.server.utils;

import catserver.server.CatServer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;

public class WorldCheck {
    public static boolean isServerWorld(LevelAccessor iWorld) {
        if (iWorld instanceof ServerLevelAccessor) {
            return true;
        } else if (iWorld != null) {
            CatServer.LOGGER.debug(String.format("Can't handle world: %s", iWorld.getClass().getName()));
        } else {
            CatServer.LOGGER.debug(new NullPointerException("Why iWorld is null?"));
        }
        return false;
    }
}
