package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Ravager;

public class CraftRavager extends CraftRaider implements Ravager {

    public CraftRavager(CraftServer server, net.minecraft.world.entity.monster.Ravager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Ravager getHandle() {
        return (net.minecraft.world.entity.monster.Ravager) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftRavager";
    }
}
