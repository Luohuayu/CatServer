package catserver.server.asm;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class ASMFixer {
    public interface IEntityPlayerMPASMFixer {
        CraftPlayer getBukkitEntity();

        default NetHandlerPlayServer getConnection() {
            return ((EntityPlayerMP) this).connection;
        }
    }
}
