package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.EntityAmbientCreature;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.EntityType;

public class CraftAmbient extends CraftLivingEntity implements Ambient {
    public CraftAmbient(CraftServer server, EntityAmbientCreature entity) {
        super(server, entity);
    }

    @Override
    public EntityAmbientCreature getHandle() {
        return (EntityAmbientCreature) entity;
    }

    @Override
    public String toString() {
        return "CraftAmbient{name=" + this.entityName + "}"; // CatServer
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
