package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.GiantEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;

public class CraftGiant extends CraftMonster implements Giant {

    public CraftGiant(CraftServer server, GiantEntity entity) {
        super(server, entity);
    }

    @Override
    public GiantEntity getHandle() {
        return (GiantEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftGiant";
    }

    @Override
    public EntityType getType() {
        return EntityType.GIANT;
    }
}
