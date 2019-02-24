package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EntityStray;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Stray;

public class CraftStray extends CraftSkeleton implements Stray {

    public CraftStray(CraftServer server, EntityStray entity) {
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
