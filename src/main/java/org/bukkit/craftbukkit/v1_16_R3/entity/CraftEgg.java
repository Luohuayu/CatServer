package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.EggEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;

public class CraftEgg extends CraftThrowableProjectile implements Egg {
    public CraftEgg(CraftServer server, EggEntity entity) {
        super(server, entity);
    }

    @Override
    public EggEntity getHandle() {
        return (EggEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }

    @Override
    public EntityType getType() {
        return EntityType.EGG;
    }
}
