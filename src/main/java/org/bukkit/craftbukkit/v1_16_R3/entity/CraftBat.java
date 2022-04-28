package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.BatEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;

public class CraftBat extends CraftAmbient implements Bat {
    public CraftBat(CraftServer server, BatEntity entity) {
        super(server, entity);
    }

    @Override
    public BatEntity getHandle() {
        return (BatEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftBat";
    }

    @Override
    public EntityType getType() {
        return EntityType.BAT;
    }

    @Override
    public boolean isAwake() {
        return !getHandle().isResting();
    }

    @Override
    public void setAwake(boolean state) {
        getHandle().setResting(!state);
    }
}
