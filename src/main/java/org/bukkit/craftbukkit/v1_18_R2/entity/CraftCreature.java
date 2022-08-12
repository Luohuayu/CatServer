package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Creature;

public class CraftCreature extends CraftMob implements Creature {
    public CraftCreature(CraftServer server, PathfinderMob entity) {
        super(server, entity);
    }

    @Override
    public PathfinderMob getHandle() {
        return (PathfinderMob) entity;
    }

    @Override
    public String toString() {
        return "CraftCreature";
    }
}
