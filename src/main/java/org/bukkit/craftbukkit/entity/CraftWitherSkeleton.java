package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EntityWitherSkeleton;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;

public class CraftWitherSkeleton extends CraftSkeleton implements WitherSkeleton {

    public CraftWitherSkeleton(CraftServer server, EntityWitherSkeleton entity) {
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
