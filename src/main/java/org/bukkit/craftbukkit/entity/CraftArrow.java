// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;
import org.apache.commons.lang.Validate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Arrow;

public class CraftArrow extends AbstractProjectile implements Arrow
{
    public CraftArrow(final CraftServer server, final EntityArrow entity) {
        super(server, entity);
    }
    
    @Override
    public void setKnockbackStrength(final int knockbackStrength) {
        Validate.isTrue(knockbackStrength >= 0, "Knockback cannot be negative");
        this.getHandle().setKnockbackStrength(knockbackStrength);
    }
    
    @Override
    public int getKnockbackStrength() {
        return this.getHandle().knockbackStrength;
    }
    
    @Override
    public boolean isCritical() {
        return this.getHandle().getIsCritical();
    }
    
    @Override
    public void setCritical(final boolean critical) {
        this.getHandle().setIsCritical(critical);
    }
    
    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }
    
    @Override
    public void setShooter(final ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            this.getHandle().shootingEntity = ((CraftLivingEntity)shooter).getHandle();
        }
        else {
            this.getHandle().shootingEntity = null;
        }
        this.getHandle().projectileSource = shooter;
    }
    
    @Override
    public EntityArrow getHandle() {
        return (EntityArrow)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftArrow";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.ARROW;
    }
    
    @Deprecated
    @Override
    public LivingEntity _INVALID_getShooter() {
        if (this.getHandle().shootingEntity == null) {
            return null;
        }
        return (LivingEntity)this.getHandle().shootingEntity.getBukkitEntity();
    }
    
    @Deprecated
    @Override
    public void _INVALID_setShooter(final LivingEntity shooter) {
        this.getHandle().shootingEntity = ((CraftLivingEntity)shooter).getHandle();
    }
}
