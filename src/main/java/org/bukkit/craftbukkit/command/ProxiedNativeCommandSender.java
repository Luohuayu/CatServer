// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.command;

import org.bukkit.permissions.PermissionAttachmentInfo;
import java.util.Set;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.permissions.Permission;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import net.minecraft.command.ICommandSender;
import org.bukkit.command.ProxiedCommandSender;

public class ProxiedNativeCommandSender implements ProxiedCommandSender
{
    private final ICommandSender orig;
    private final CommandSender caller;
    private final CommandSender callee;
    
    public ProxiedNativeCommandSender(final ICommandSender orig, final CommandSender caller, final CommandSender callee) {
        this.orig = orig;
        this.caller = caller;
        this.callee = callee;
    }
    
    public ICommandSender getHandle() {
        return this.orig;
    }
    
    @Override
    public CommandSender getCaller() {
        return this.caller;
    }
    
    @Override
    public CommandSender getCallee() {
        return this.callee;
    }
    
    @Override
    public void sendMessage(final String message) {
        this.getCaller().sendMessage(message);
    }
    
    @Override
    public void sendMessage(final String[] messages) {
        this.getCaller().sendMessage(messages);
    }
    
    @Override
    public Server getServer() {
        return this.getCallee().getServer();
    }
    
    @Override
    public String getName() {
        return this.getCallee().getName();
    }
    
    @Override
    public boolean isPermissionSet(final String name) {
        return this.getCaller().isPermissionSet(name);
    }
    
    @Override
    public boolean isPermissionSet(final Permission perm) {
        return this.getCaller().isPermissionSet(perm);
    }
    
    @Override
    public boolean hasPermission(final String name) {
        return this.getCaller().hasPermission(name);
    }
    
    @Override
    public boolean hasPermission(final Permission perm) {
        return this.getCaller().hasPermission(perm);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
        return this.getCaller().addAttachment(plugin, name, value);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin) {
        return this.getCaller().addAttachment(plugin);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value, final int ticks) {
        return this.getCaller().addAttachment(plugin, name, value, ticks);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
        return this.getCaller().addAttachment(plugin, ticks);
    }
    
    @Override
    public void removeAttachment(final PermissionAttachment attachment) {
        this.getCaller().removeAttachment(attachment);
    }
    
    @Override
    public void recalculatePermissions() {
        this.getCaller().recalculatePermissions();
    }
    
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.getCaller().getEffectivePermissions();
    }
    
    @Override
    public boolean isOp() {
        return this.getCaller().isOp();
    }
    
    @Override
    public void setOp(final boolean value) {
        this.getCaller().setOp(value);
    }
}
