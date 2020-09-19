package catserver.server.utils;

import catserver.server.CatServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Loader;

public class ModFixUtils {
    public static void func_145775_I() { }

    public static void fixNetherex() {
        if (Loader.instance().getIndexedModList().containsKey("netherex")) {
            World netherWorld = DimensionManager.getWorld(-1);
            if (netherWorld != null) {
                try {
                    netherWorld.getServer().unloadWorld(netherWorld.getWorld(), true);
                    if (!CatServer.getConfig().autoUnloadDimensions.contains(-1)) DimensionManager.initDimension(-1);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
