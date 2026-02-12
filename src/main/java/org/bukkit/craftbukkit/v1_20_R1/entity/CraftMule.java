package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Mule;

public class CraftMule extends CraftChestedHorse implements Mule {

    public CraftMule(CraftServer server, net.minecraft.world.entity.animal.horse.Mule entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMule";
    }

    @Override
    public Variant getVariant() {
        return Variant.MULE;
    }
}
