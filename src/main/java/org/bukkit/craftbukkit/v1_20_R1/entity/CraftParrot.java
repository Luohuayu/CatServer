package org.bukkit.craftbukkit.v1_20_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Parrot.Variant;

public class CraftParrot extends CraftTameableAnimal implements Parrot {

    public CraftParrot(CraftServer server, net.minecraft.world.entity.animal.Parrot parrot) {
        super(server, parrot);
    }

    @Override
    public net.minecraft.world.entity.animal.Parrot getHandle() {
        return (net.minecraft.world.entity.animal.Parrot) entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(net.minecraft.world.entity.animal.Parrot.Variant.byId(variant.ordinal()));
    }

    @Override
    public String toString() {
        return "CraftParrot";
    }

    @Override
    public boolean isDancing() {
        return getHandle().isPartyParrot();
    }
}
