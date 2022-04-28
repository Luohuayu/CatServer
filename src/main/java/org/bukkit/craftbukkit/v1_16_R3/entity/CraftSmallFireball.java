package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.SmallFireballEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SmallFireball;

public class CraftSmallFireball extends CraftSizedFireball implements SmallFireball {
    public CraftSmallFireball(CraftServer server, SmallFireballEntity entity) {
        super(server, entity);
    }

    @Override
    public SmallFireballEntity getHandle() {
        return (SmallFireballEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSmallFireball";
    }

    @Override
    public EntityType getType() {
        return EntityType.SMALL_FIREBALL;
    }
}
