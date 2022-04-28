package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.horse.MuleEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Mule;

public class CraftMule extends CraftChestedHorse implements Mule {

    public CraftMule(CraftServer server, MuleEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMule";
    }

    @Override
    public EntityType getType() {
        return EntityType.MULE;
    }

    @Override
    public Variant getVariant() {
        return Variant.MULE;
    }
}
