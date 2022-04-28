package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.DragonFireballEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EntityType;

public class CraftDragonFireball extends CraftFireball implements DragonFireball {
    public CraftDragonFireball(CraftServer server, DragonFireballEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftDragonFireball";
    }

    @Override
    public EntityType getType() {
        return EntityType.DRAGON_FIREBALL;
    }
}
