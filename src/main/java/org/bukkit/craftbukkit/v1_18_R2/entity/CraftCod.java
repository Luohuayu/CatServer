package org.bukkit.craftbukkit.v1_18_R2.entity;

import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Cod;
import org.bukkit.entity.EntityType;

public class CraftCod extends CraftFish implements Cod {

    public CraftCod(CraftServer server, net.minecraft.world.entity.animal.Cod entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Cod getHandle() {
        return (net.minecraft.world.entity.animal.Cod) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftCod";
    }

    @Override
    public EntityType getType() {
        return EntityType.COD;
    }
}
