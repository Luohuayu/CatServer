package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.FireballEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;

public class CraftLargeFireball extends CraftSizedFireball implements LargeFireball {
    public CraftLargeFireball(CraftServer server, FireballEntity entity) {
        super(server, entity);
    }

    @Override
    public void setYield(float yield) {
        super.setYield(yield);
        getHandle().explosionPower = (int) yield;
    }

    @Override
    public FireballEntity getHandle() {
        return (FireballEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftLargeFireball";
    }

    @Override
    public EntityType getType() {
        return EntityType.FIREBALL;
    }
}
