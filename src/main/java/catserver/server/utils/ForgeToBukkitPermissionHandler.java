package catserver.server.utils;

import com.mojang.authlib.GameProfile;
import moe.loliserver.BukkitInjector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.IPermissionHandler;
import net.minecraftforge.server.permission.context.IContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

public class ForgeToBukkitPermissionHandler implements IPermissionHandler {

    @Override
    public void registerNode(String node, DefaultPermissionLevel level, String desc) {
        BukkitInjector.registerDefaultPermission(node, level, desc);
    }

    @Override
    public Collection<String> getRegisteredNodes() {
        return Bukkit.getPluginManager().getPermissions().stream().map(Permission::getName).collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission(GameProfile profile, String node, @Nullable IContext context) {
        if (context != null) {
            PlayerEntity player = context.getPlayer();
            if (player != null) {
                return player.getBukkitEntity().hasPermission(node);
            }
        }
        Player player = Bukkit.getPlayer(profile.getId());
        if (player != null) {
            return player.hasPermission(node);
        } else {
            Permission permission = Bukkit.getPluginManager().getPermission(node);
            boolean op = MinecraftServer.getServer().getPlayerList().isOp(profile);
            if (permission != null) {
                return permission.getDefault().getValue(op);
            } else {
                return Permission.DEFAULT_PERMISSION.getValue(op);
            }
        }
    }

    @Override
    public String getNodeDescription(String node) {
        return Bukkit.getPluginManager().getPermission(node) != null ? Bukkit.getPluginManager().getPermission(node).getDescription() : "";
    }
}
