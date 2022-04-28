package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.fish.CodEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Cod;
import org.bukkit.entity.EntityType;

public class CraftCod extends CraftFish implements Cod {

    public CraftCod(CraftServer server, CodEntity entity) {
        super(server, entity);
    }

    @Override
    public CodEntity getHandle() {
        return (CodEntity) super.getHandle();
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
