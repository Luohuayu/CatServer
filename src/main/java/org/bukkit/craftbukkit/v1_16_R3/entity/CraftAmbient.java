package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.AmbientEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.EntityType;

public class CraftAmbient extends CraftMob implements Ambient {
    public CraftAmbient(CraftServer server, AmbientEntity entity) {
        super(server, entity);
    }

    @Override
    public AmbientEntity getHandle() {
        return (AmbientEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftAmbient";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
