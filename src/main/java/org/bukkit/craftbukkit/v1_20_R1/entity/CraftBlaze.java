package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Blaze;

public class CraftBlaze extends CraftMonster implements Blaze {
    public CraftBlaze(CraftServer server, net.minecraft.world.entity.monster.Blaze entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Blaze getHandle() {
        return (net.minecraft.world.entity.monster.Blaze) entity;
    }

    @Override
    public String toString() {
        return "CraftBlaze";
    }
}
