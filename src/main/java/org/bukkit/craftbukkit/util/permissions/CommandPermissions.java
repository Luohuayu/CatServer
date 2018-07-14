// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util.permissions;

import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;
import org.bukkit.permissions.Permission;

public final class CommandPermissions
{
    private static final String ROOT = "minecraft.command";
    private static final String PREFIX = "minecraft.command.";
    
    public static Permission registerPermissions(final Permission parent) {
        final Permission commands = DefaultPermissions.registerPermission("minecraft.command", "Gives the user the ability to use all vanilla minecraft commands", parent);
        DefaultPermissions.registerPermission("minecraft.command.kill", "Allows the user to commit suicide", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.me", "Allows the user to perform a chat action", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission("minecraft.command.tell", "Allows the user to privately message another player", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission("minecraft.command.say", "Allows the user to talk as the console", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.give", "Allows the user to give items to players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.teleport", "Allows the user to teleport players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.kick", "Allows the user to kick players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.stop", "Allows the user to stop the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.list", "Allows the user to list all online players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.gamemode", "Allows the user to change the gamemode of another player", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.xp", "Allows the user to give themselves or others arbitrary values of experience", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.toggledownfall", "Allows the user to toggle rain on/off for a given world", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.defaultgamemode", "Allows the user to change the default gamemode of the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.seed", "Allows the user to view the seed of the world", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.effect", "Allows the user to add/remove effects on players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.selector", "Allows the use of selectors", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("minecraft.command.trigger", "Allows the use of the trigger command", PermissionDefault.TRUE, commands);
        commands.recalculatePermissibles();
        return commands;
    }
}
