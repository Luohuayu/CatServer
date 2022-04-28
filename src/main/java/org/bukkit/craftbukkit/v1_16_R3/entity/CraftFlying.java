package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.FlyingEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Flying;

public class CraftFlying extends CraftMob implements Flying {

    public CraftFlying(CraftServer server, FlyingEntity entity) {
        super(server, entity);
    }

    @Override
    public FlyingEntity getHandle() {
        return (FlyingEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFlying";
    }
}
