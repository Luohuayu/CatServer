// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.permissions.PermissionAttachmentInfo;
import java.util.Set;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.permissions.Permission;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.ServerOperator;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.entity.minecart.CommandMinecart;

public class CraftMinecartCommand extends CraftMinecart implements CommandMinecart
{
    private final PermissibleBase perm;
    
    public CraftMinecartCommand(final CraftServer server, final EntityMinecartCommandBlock entity) {
        super(server, entity);
        this.perm = new PermissibleBase(this);
    }
    
    @Override
    public EntityMinecartCommandBlock getHandle() {
        return (EntityMinecartCommandBlock)this.entity;
    }
    
    @Override
    public String getCommand() {
        return this.getHandle().getCommandBlockLogic().getCommand();
    }
    
    @Override
    public void setCommand(final String command) {
        this.getHandle().getCommandBlockLogic().setCommand((command != null) ? command : "");
        this.getHandle().getDataManager().set(EntityMinecartCommandBlock.COMMAND, this.getHandle().getCommandBlockLogic().getCommand());
    }
    
    @Override
    public void setName(final String name) {
        this.getHandle().getCommandBlockLogic().setName((name != null) ? name : "@");
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MINECART_COMMAND;
    }
    
    @Override
    public void sendMessage(final String message) {
    }
    
    @Override
    public void sendMessage(final String[] messages) {
    }
    
    @Override
    public String getName() {
        return this.getHandle().getCommandBlockLogic().getName();
    }
    
    @Override
    public boolean isOp() {
        return true;
    }
    
    @Override
    public void setOp(final boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a minecart");
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
    
    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }
}
