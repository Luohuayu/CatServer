// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.util.Vector;
import org.bukkit.projectiles.ProjectileSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fireball;

public class CraftFireball extends AbstractProjectile implements Fireball
{
    public CraftFireball(final CraftServer server, final EntityFireball entity) {
        super(server, entity);
    }
    
    @Override
    public float getYield() {
        return this.getHandle().bukkitYield;
    }
    
    @Override
    public boolean isIncendiary() {
        return this.getHandle().isIncendiary;
    }
    
    @Override
    public void setIsIncendiary(final boolean isIncendiary) {
        this.getHandle().isIncendiary = isIncendiary;
    }
    
    @Override
    public void setYield(final float yield) {
        this.getHandle().bukkitYield = yield;
    }
    
    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }
    
    @Override
    public void setShooter(final ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().shootingEntity = ((CraftLivingEntity)shooter).getHandle();
        }
        else {
            this.getHandle().shootingEntity = null;
        }
        this.getHandle().projectileSource = shooter;
    }
    
    @Override
    public Vector getDirection() {
        return new Vector(this.getHandle().accelerationX, this.getHandle().accelerationY, this.getHandle().accelerationZ);
    }
    
    @Override
    public void setDirection(final Vector direction) {
        Validate.notNull((Object)direction, "Direction can not be null");
        final double x = direction.getX();
        final double y = direction.getY();
        final double z = direction.getZ();
        final double magnitude = MathHelper.sqrt_double(x * x + y * y + z * z);
        this.getHandle().accelerationX = x / magnitude;
        this.getHandle().accelerationY = y / magnitude;
        this.getHandle().accelerationZ = z / magnitude;
    }
    
    @Override
    public EntityFireball getHandle() {
        return (EntityFireball)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftFireball";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
    
    @Deprecated
    @Override
    public void _INVALID_setShooter(final LivingEntity shooter) {
        this.setShooter(shooter);
    }
    
    @Deprecated
    @Override
    public LivingEntity _INVALID_getShooter() {
        if (this.getHandle().shootingEntity != null) {
            return (LivingEntity)this.getHandle().shootingEntity.getBukkitEntity();
        }
        return null;
    }
}
