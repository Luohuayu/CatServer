package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.EntityMooshroom;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;

public class CraftMushroomCow extends CraftCow implements MushroomCow {
    public CraftMushroomCow(CraftServer server, EntityMooshroom entity) {
        super(server, entity);
    }

    @Override
    public EntityMooshroom getHandle() {
        return (EntityMooshroom) entity;
    }

    @Override
    public String toString() {
        return "CraftMushroomCow";
    }

    public EntityType getType() {
        return EntityType.MUSHROOM_COW;
    }
}
