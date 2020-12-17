package catserver.server.utils;

import catserver.server.CatServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Loader;
import org.bukkit.craftbukkit.entity.CraftPlayer;

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

    public static void hookFirstAidHealthUpdate(EntityPlayer player, DataParameter key, Object value) {
        if (key.equals(EntityPlayer.HEALTH)) {
            float health = (float)value;
            if (player instanceof EntityPlayerMP) {
                final CraftPlayer cbPlayer = ((EntityPlayerMP)player).getBukkitEntity();
                if (health < 0.0f) {
                    cbPlayer.setRealHealth(0.0);
                }
                else if (health > cbPlayer.getMaxHealth()) {
                    cbPlayer.setRealHealth(cbPlayer.getMaxHealth());
                }
                else {
                    cbPlayer.setRealHealth(health);
                }
            }
        }
    }
}
