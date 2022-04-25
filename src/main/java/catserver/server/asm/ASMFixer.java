package catserver.server.asm;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class ASMFixer {
    public static class EntityPlayerMPFixer {
        public static boolean isEntityPlayerMP(Object object) {
            return object instanceof EntityPlayerMP;
        }

        public static CraftPlayer getBukkitEntity(Object object) {
            return ((EntityPlayerMP) object).getBukkitEntity();
        }

        public static NetHandlerPlayServer getConnection(Object object) {
            return ((EntityPlayerMP) object).connection;
        }
    }
}
