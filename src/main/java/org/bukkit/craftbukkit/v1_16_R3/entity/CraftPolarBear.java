package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.PolarBearEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PolarBear;

public class CraftPolarBear extends CraftAnimals implements PolarBear {

    public CraftPolarBear(CraftServer server, PolarBearEntity entity) {
        super(server, entity);
    }
    @Override
    public PolarBearEntity getHandle() {
        return (PolarBearEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftPolarBear";
    }

    @Override
    public EntityType getType() {
        return EntityType.POLAR_BEAR;
    }
}
