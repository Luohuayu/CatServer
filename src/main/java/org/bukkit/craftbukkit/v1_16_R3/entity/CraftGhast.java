package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.GhastEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;

public class CraftGhast extends CraftFlying implements Ghast {

    public CraftGhast(CraftServer server, GhastEntity entity) {
        super(server, entity);
    }

    @Override
    public GhastEntity getHandle() {
        return (GhastEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftGhast";
    }

    @Override
    public EntityType getType() {
        return EntityType.GHAST;
    }
}
