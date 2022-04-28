package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.WitherSkeletonEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;

public class CraftWitherSkeleton extends CraftSkeleton implements WitherSkeleton {

    public CraftWitherSkeleton(CraftServer server, WitherSkeletonEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftWitherSkeleton";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER_SKELETON;
    }

    @Override
    public SkeletonType getSkeletonType() {
        return SkeletonType.WITHER;
    }
}
