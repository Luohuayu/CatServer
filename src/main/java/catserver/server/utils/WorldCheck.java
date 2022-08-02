package catserver.server.utils;

import catserver.server.CatServer;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;

public class WorldCheck {
    public static boolean isServerWorld(IWorld iWorld) {
        if (iWorld instanceof IServerWorld) {
            return true;
        } else if (iWorld != null) {
            CatServer.log.debug(String.format("Can't handle world: %s", iWorld.getClass().getName()));
        } else {
            CatServer.log.debug(new NullPointerException("Why iWorld is null?"));
        }
        return false;
    }
}
