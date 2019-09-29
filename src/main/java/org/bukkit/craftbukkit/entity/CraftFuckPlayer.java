package org.bukkit.craftbukkit.entity;

import catserver.server.CatServer;
import net.minecraftforge.common.util.FakePlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

// FIXME: THIS CLASS IS BY CatServer
public class CraftFuckPlayer extends CraftPlayer {
    private Player realPlayer = null;

    public CraftFuckPlayer(CraftServer server, FakePlayer entity) {
        super(server, entity);
        realPlayer = getRealPlayer();
    }

    @Override
    public boolean hasPermission(String name) {
        if (CatServer.fakePlayerPermissions.contains(name))
            return true;
        final Player realPlayer = getRealPlayer();
        if (realPlayer == null)
            return super.hasPermission(name);
        return realPlayer.hasPermission(name);
    }

    @Override
    public boolean isPermissionSet(String name) {
        if (CatServer.fakePlayerPermissions.contains(name))
            return true;
        final Player realPlayer = getRealPlayer();
        if (realPlayer == null)
            return super.isPermissionSet(name);
        return realPlayer.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        if (CatServer.fakePlayerPermissions.contains(perm.getName()))
            return true;
        final Player realPlayer = getRealPlayer();
        if (realPlayer == null)
            return super.isPermissionSet(perm);
        return realPlayer.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        if (CatServer.fakePlayerPermissions.contains(perm.getName()))
            return true;
        final Player realPlayer = getRealPlayer();
        if (realPlayer == null)
            return super.hasPermission(perm);
        return realPlayer.hasPermission(perm);
    }

    private Player getRealPlayer() {
        if (this.realPlayer != null && this.realPlayer.isOnline())
            return this.realPlayer;
        if (realPlayer != null)
            realPlayer = null;
        final String myName = getHandle().getName();
        final Player getRealPlayer = server.getPlayer(myName);
        if (getRealPlayer instanceof CraftFuckPlayer)
            return null;
        if (getRealPlayer != null)
            realPlayer = getRealPlayer;
        return getRealPlayer;
    }



    //==================Connect


    @Override
    public void updateInventory() {
        //Do Nothing
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        //Do Nothing
    }
}
