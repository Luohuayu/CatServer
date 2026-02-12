package org.bukkit.craftbukkit.v1_20_R1.entity;

import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftThrowableProjectile implements ThrownExpBottle {
    public CraftThrownExpBottle(CraftServer server, ThrownExperienceBottle entity) {
        super(server, entity);
    }

    @Override
    public ThrownExperienceBottle getHandle() {
        return (ThrownExperienceBottle) entity;
    }

    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }
}
