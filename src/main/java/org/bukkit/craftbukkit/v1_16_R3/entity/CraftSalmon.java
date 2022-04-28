package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.fish.SalmonEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Salmon;

public class CraftSalmon extends CraftFish implements Salmon {

    public CraftSalmon(CraftServer server, SalmonEntity entity) {
        super(server, entity);
    }

    @Override
    public SalmonEntity getHandle() {
        return (SalmonEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSalmon";
    }

    @Override
    public EntityType getType() {
        return EntityType.SALMON;
    }
}
