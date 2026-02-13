package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Silverfish;

public class CraftSilverfish extends CraftMonster implements Silverfish {
    public CraftSilverfish(CraftServer server, net.minecraft.world.entity.monster.Silverfish entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Silverfish getHandle() {
        return (net.minecraft.world.entity.monster.Silverfish) entity;
    }

    @Override
    public String toString() {
        return "CraftSilverfish";
    }
}
