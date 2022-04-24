package org.bukkit.craftbukkit.v1_18_R2.entity;

import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftThrowableProjectile implements ThrownExpBottle {
    public CraftThrownExpBottle(CraftServer server, net.minecraft.world.entity.projectile.ThrownExperienceBottle entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.ThrownExperienceBottle getHandle() {
        return (net.minecraft.world.entity.projectile.ThrownExperienceBottle) entity;
    }

    @Override
    public String toString() {
        return "net.minecraft.world.entity.projectile.ThrownExperienceBottle";
    }

    @Override
    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}
