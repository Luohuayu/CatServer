package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Stray;

public class CraftStray extends CraftAbstractSkeleton implements Stray {

    public CraftStray(CraftServer server, net.minecraft.world.entity.monster.Stray entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftStray";
    }

    @Override
    public SkeletonType getSkeletonType() {
        return SkeletonType.STRAY;
    }
}
