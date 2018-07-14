// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderEye;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderSignal;

public class CraftEnderSignal extends CraftEntity implements EnderSignal
{
    public CraftEnderSignal(final CraftServer server, final EntityEnderEye entity) {
        super(server, entity);
    }
    
    @Override
    public EntityEnderEye getHandle() {
        return (EntityEnderEye)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftEnderSignal";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.ENDER_SIGNAL;
    }
}
