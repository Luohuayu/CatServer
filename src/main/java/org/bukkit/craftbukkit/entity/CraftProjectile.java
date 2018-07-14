// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.EntityLivingBase;
import org.bukkit.projectiles.ProjectileSource;
import net.minecraft.entity.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Projectile;

public class CraftProjectile extends AbstractProjectile implements Projectile
{
    public CraftProjectile(final CraftServer server, final net.minecraft.entity.Entity entity) {
        super(server, entity);
    }
    
    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }
    
    @Override
    public void setShooter(final ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().thrower = (EntityLivingBase)((CraftLivingEntity)shooter).entity;
            if (shooter instanceof CraftHumanEntity) {
                this.getHandle().throwerName = ((CraftHumanEntity)shooter).getName();
            }
        }
        else {
            this.getHandle().thrower = null;
            this.getHandle().throwerName = null;
        }
        this.getHandle().projectileSource = shooter;
    }
    
    @Override
    public EntityThrowable getHandle() {
        return (EntityThrowable)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftProjectile";
    }
    
    @Deprecated
    @Override
    public LivingEntity _INVALID_getShooter() {
        if (this.getHandle().thrower == null) {
            return null;
        }
        return (LivingEntity)this.getHandle().thrower.getBukkitEntity();
    }
    
    @Deprecated
    @Override
    public void _INVALID_setShooter(final LivingEntity shooter) {
        if (shooter == null) {
            return;
        }
        this.getHandle().thrower = ((CraftLivingEntity)shooter).getHandle();
        if (shooter instanceof CraftHumanEntity) {
            this.getHandle().throwerName = ((CraftHumanEntity)shooter).getName();
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
