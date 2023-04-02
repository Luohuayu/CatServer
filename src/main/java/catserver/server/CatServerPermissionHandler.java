package catserver.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.handler.IPermissionHandler;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

// https://github.com/IzzelAliz/Arclight/blob/1.18/arclight-common/src/main/java/io/izzel/arclight/common/mod/server/ArclightPermissionHandler.java
public class CatServerPermissionHandler implements IPermissionHandler {

    private final IPermissionHandler permissionHandler;

    public CatServerPermissionHandler(IPermissionHandler permissionHandler) {
        Validate.notNull(permissionHandler, "PermissionHandler cannot be null.");
        this.permissionHandler = permissionHandler;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return new ResourceLocation("catserver", "permission");
    }

    @Override
    public Set<PermissionNode<?>> getRegisteredNodes() {
        return permissionHandler.getRegisteredNodes();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getPermission(ServerPlayer player, PermissionNode<T> node, PermissionDynamicContext<?>... context) {
        if (node.getType() == PermissionTypes.BOOLEAN) {
            return (T) (Object) player.getBukkitEntity().hasPermission(node.getNodeName());
        } else {
            return permissionHandler.getPermission(player, node, context);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOfflinePermission(UUID player, PermissionNode<T> node, PermissionDynamicContext<?>... context) {
        Player bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer != null && node.getType() == PermissionTypes.BOOLEAN) {
            return (T) (Object) bukkitPlayer.hasPermission(node.getNodeName());
        } else {
            return permissionHandler.getOfflinePermission(player, node, context);
        }
    }
}
