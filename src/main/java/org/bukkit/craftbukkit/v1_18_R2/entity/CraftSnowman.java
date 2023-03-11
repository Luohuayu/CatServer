package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.animal.SnowGolem;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;

public class CraftSnowman extends CraftGolem implements Snowman {
    public CraftSnowman(CraftServer server, SnowGolem entity) {
        super(server, entity);
    }

    @Override
    public boolean isDerp() {
        return !getHandle().hasPumpkin();
    }

    @Override
    public void setDerp(boolean derpMode) {
        getHandle().setPumpkin(!derpMode);
    }

    @Override
    public SnowGolem getHandle() {
        return (SnowGolem) entity;
    }

    @Override
    public String toString() {
        return "CraftSnowman";
    }

    @Override
    public EntityType getType() {
        return EntityType.SNOWMAN;
    }
}
