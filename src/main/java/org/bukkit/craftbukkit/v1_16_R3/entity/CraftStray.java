package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.StrayEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Stray;

public class CraftStray extends CraftSkeleton implements Stray {

    public CraftStray(CraftServer server, StrayEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftStray";
    }

    @Override
    public EntityType getType() {
        return EntityType.STRAY;
    }

    @Override
    public SkeletonType getSkeletonType() {
        return SkeletonType.STRAY;
    }
}
