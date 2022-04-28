package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.CreatureEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Creature;

public class CraftCreature extends CraftMob implements Creature {
    public CraftCreature(CraftServer server, CreatureEntity entity) {
        super(server, entity);
    }

    @Override
    public CreatureEntity getHandle() {
        return (CreatureEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftCreature";
    }
}
