// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.EntityLivingBase;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ShulkerBullet;

public class CraftShulkerBullet extends AbstractProjectile implements ShulkerBullet
{
    public CraftShulkerBullet(final CraftServer server, final EntityShulkerBullet entity) {
        super(server, entity);
    }
    
    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }
    
    @Override
    public void setShooter(final ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            this.getHandle().setShooter(((CraftLivingEntity)shooter).getHandle());
        }
        else {
            this.getHandle().setShooter(null);
        }
        this.getHandle().projectileSource = shooter;
    }
    
    @Override
    public org.bukkit.entity.Entity getTarget() {
        return (this.getHandle().getTarget() != null) ? this.getHandle().getTarget().getBukkitEntity() : null;
    }
    
    @Override
    public void setTarget(final org.bukkit.entity.Entity target) {
        this.getHandle().setTarget((target == null) ? null : ((CraftEntity)target).getHandle());
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SHULKER_BULLET;
    }
    
    @Override
    public EntityShulkerBullet getHandle() {
        return (EntityShulkerBullet)this.entity;
    }
    
    @Deprecated
    @Override
    public LivingEntity _INVALID_getShooter() {
        if (this.getHandle().getShooter() == null) {
            return null;
        }
        return (LivingEntity)this.getHandle().getShooter().getBukkitEntity();
    }
    
    @Deprecated
    @Override
    public void _INVALID_setShooter(final LivingEntity shooter) {
        this.getHandle().setShooter(((CraftLivingEntity)shooter).getHandle());
    }
}
