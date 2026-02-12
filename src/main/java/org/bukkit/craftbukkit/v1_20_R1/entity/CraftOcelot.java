package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;

public class CraftOcelot extends CraftAnimals implements Ocelot {
    public CraftOcelot(CraftServer server, net.minecraft.world.entity.animal.Ocelot ocelot) {
        super(server, ocelot);
    }

    @Override
    public net.minecraft.world.entity.animal.Ocelot getHandle() {
        return (net.minecraft.world.entity.animal.Ocelot) entity;
    }

    @Override
    public boolean isTrusting() {
        return getHandle().isTrusting();
    }

    @Override
    public void setTrusting(boolean trust) {
        getHandle().setTrusting(trust);
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
}
