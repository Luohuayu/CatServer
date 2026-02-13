package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Squid;

public class CraftSquid extends CraftWaterMob implements Squid {

    public CraftSquid(CraftServer server, net.minecraft.world.entity.animal.Squid entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Squid getHandle() {
        return (net.minecraft.world.entity.animal.Squid) entity;
    }

    @Override
    public String toString() {
        return "CraftSquid";
    }
}
