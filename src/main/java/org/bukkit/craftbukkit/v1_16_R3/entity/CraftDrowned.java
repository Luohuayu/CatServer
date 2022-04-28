package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.DrownedEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;

public class CraftDrowned extends CraftZombie implements Drowned {

    public CraftDrowned(CraftServer server, DrownedEntity entity) {
        super(server, entity);
    }

    @Override
    public DrownedEntity getHandle() {
        return (DrownedEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftDrowned";
    }

    @Override
    public EntityType getType() {
        return EntityType.DROWNED;
    }
}
