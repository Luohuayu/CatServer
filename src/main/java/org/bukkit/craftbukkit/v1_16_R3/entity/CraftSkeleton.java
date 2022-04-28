package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.AbstractSkeletonEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class CraftSkeleton extends CraftMonster implements Skeleton {

    public CraftSkeleton(CraftServer server, AbstractSkeletonEntity entity) {
        super(server, entity);
    }

    @Override
    public AbstractSkeletonEntity getHandle() {
        return (AbstractSkeletonEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSkeleton";
    }

    @Override
    public EntityType getType() {
        return EntityType.SKELETON;
    }

    @Override
    public SkeletonType getSkeletonType() {
        return SkeletonType.NORMAL;
    }

    @Override
    public void setSkeletonType(SkeletonType type) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
