// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.EntityThrowable;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderPearl;

public class CraftEnderPearl extends CraftProjectile implements EnderPearl
{
    public CraftEnderPearl(final CraftServer server, final EntityEnderPearl entity) {
        super(server, entity);
    }
    
    @Override
    public EntityEnderPearl getHandle() {
        return (EntityEnderPearl)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftEnderPearl";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.ENDER_PEARL;
    }
}
