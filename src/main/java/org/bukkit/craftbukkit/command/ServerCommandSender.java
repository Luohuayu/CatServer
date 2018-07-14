// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.permissions.PermissionAttachmentInfo;
import java.util.Set;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.command.CommandSender;

public abstract class ServerCommandSender implements CommandSender
{
    private final PermissibleBase perm;
    
    public ServerCommandSender() {
        this.perm = new PermissibleBase(this);
    }
    
    @Override
    public boolean isPermissionSet(final String name) {
        return this.perm.isPermissionSet(name);
    }
    
    @Override
    public boolean isPermissionSet(final Permission perm) {
        return this.perm.isPermissionSet(perm);
    }
    
    @Override
    public boolean hasPermission(final String name) {
        return this.perm.hasPermission(name);
    }
    
    @Override
    public boolean hasPermission(final Permission perm) {
        return this.perm.hasPermission(perm);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value, final int ticks) {
        return this.perm.addAttachment(plugin, name, value, ticks);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
        return this.perm.addAttachment(plugin, ticks);
    }
    
    @Override
    public void removeAttachment(final PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }
    
    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }
    
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }
    
    public boolean isPlayer() {
        return false;
    }
    
    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }
}
