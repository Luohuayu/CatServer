package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().thrower = (EntityLiving) ((CraftLivingEntity) shooter).entity;
            if (shooter instanceof CraftHumanEntity) {
                getHandle().throwerName = ((CraftHumanEntity) shooter).getName();
            }
        } else {
            getHandle().thrower = null;
            getHandle().throwerName = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public EntityThrowable getHandle() {
        return (EntityThrowable) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
