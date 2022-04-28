package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.WitchEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;

public class CraftWitch extends CraftRaider implements Witch {
    public CraftWitch(CraftServer server, WitchEntity entity) {
        super(server, entity);
    }

    @Override
    public WitchEntity getHandle() {
        return (WitchEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITCH;
    }
}
