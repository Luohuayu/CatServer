package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.OcelotEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;

public class CraftOcelot extends CraftAnimals implements Ocelot {
    public CraftOcelot(CraftServer server, OcelotEntity ocelot) {
        super(server, ocelot);
    }

    @Override
    public OcelotEntity getHandle() {
        return (OcelotEntity) entity;
    }

    @Override
    public Type getCatType() {
        return Type.WILD_OCELOT;
    }

    @Override
    public void setCatType(Type type) {
        throw new UnsupportedOperationException("Cats are now a different entity!");
    }

    @Override
    public String toString() {
        return "CraftOcelot";
    }

    @Override
    public EntityType getType() {
        return EntityType.OCELOT;
    }
}
