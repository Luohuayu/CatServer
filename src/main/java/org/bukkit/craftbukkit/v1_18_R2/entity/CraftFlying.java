package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.FlyingMob;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Flying;

public class CraftFlying extends CraftMob implements Flying {

    public CraftFlying(CraftServer server, FlyingMob entity) {
        super(server, entity);
    }

    @Override
    public FlyingMob getHandle() {
        return (FlyingMob) entity;
    }

    @Override
    public String toString() {
        return "CraftFlying";
    }
}
