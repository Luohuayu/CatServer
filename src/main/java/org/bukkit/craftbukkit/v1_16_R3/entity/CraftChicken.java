package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.ChickenEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;

public class CraftChicken extends CraftAnimals implements Chicken {

    public CraftChicken(CraftServer server, ChickenEntity entity) {
        super(server, entity);
    }

    @Override
    public ChickenEntity getHandle() {
        return (ChickenEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftChicken";
    }

    @Override
    public EntityType getType() {
        return EntityType.CHICKEN;
    }
}
