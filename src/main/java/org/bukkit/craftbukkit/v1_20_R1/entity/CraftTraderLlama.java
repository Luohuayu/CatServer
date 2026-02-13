package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.TraderLlama;

public class CraftTraderLlama extends CraftLlama implements TraderLlama {

    public CraftTraderLlama(CraftServer server, net.minecraft.world.entity.animal.horse.TraderLlama entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.horse.TraderLlama getHandle() {
        return (net.minecraft.world.entity.animal.horse.TraderLlama) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTraderLlama";
    }
}
