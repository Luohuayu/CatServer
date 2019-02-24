package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;

import net.minecraft.entity.projectile.EntityArrow;
import org.apache.commons.lang3.Validate;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;

public class CraftArrow extends AbstractProjectile implements Arrow {

    public CraftArrow(CraftServer server, EntityArrow entity) {
        super(server, entity);
    }

    public void setKnockbackStrength(int knockbackStrength) {
        Validate.isTrue(knockbackStrength >= 0, "Knockback cannot be negative");
        getHandle().setKnockbackStrength(knockbackStrength);
    }

    public int getKnockbackStrength() {
        return getHandle().knockbackStrength;
    }

    public boolean isCritical() {
        return getHandle().getIsCritical();
    }

    public void setCritical(boolean critical) {
        getHandle().setIsCritical(critical);
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            getHandle().shootingEntity = ((CraftLivingEntity) shooter).getHandle();
        } else {
            getHandle().shootingEntity = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public boolean isInBlock() {
        return getHandle().onGround;
    }

    @Override
    public Block getAttachedBlock() {
        if (!isInBlock()) {
            return null;
        }

        EntityArrow handle = getHandle();
        return getWorld().getBlockAt(handle.xTile, handle.yTile, handle.zTile);
    }

    @Override
    public PickupStatus getPickupStatus() {
        return PickupStatus.values()[getHandle().pickupStatus.ordinal()];
    }

    @Override
    public void setPickupStatus(PickupStatus status) {
        Preconditions.checkNotNull(status, "status");
        getHandle().pickupStatus = EntityArrow.PickupStatus.getByOrdinal(status.ordinal());
    }

    @Override
    public EntityArrow getHandle() {
        return (EntityArrow) entity;
    }

    @Override
    public String toString() {
        return "CraftArrow";
    }

    public EntityType getType() {
        return EntityType.ARROW;
    }

    // Spigot start
    private final Arrow.Spigot spigot = new Arrow.Spigot()
    {
        @Override
        public double getDamage()
        {
            return getHandle().getDamage();
        }

        @Override
        public void setDamage(double damage)
        {
            getHandle().setDamage( damage );
        }
    };

    public Arrow.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
