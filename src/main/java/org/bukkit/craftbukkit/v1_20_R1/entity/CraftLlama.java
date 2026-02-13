package org.bukkit.craftbukkit.v1_20_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryLlama;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Llama.Color;
import org.bukkit.inventory.LlamaInventory;

public class CraftLlama extends CraftChestedHorse implements Llama {

    public CraftLlama(CraftServer server, net.minecraft.world.entity.animal.horse.Llama entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.horse.Llama getHandle() {
        return (net.minecraft.world.entity.animal.horse.Llama) super.getHandle();
    }

    @Override
    public Color getColor() {
        return Color.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setColor(Color color) {
        Preconditions.checkArgument(color != null, "color");

        getHandle().setVariant(net.minecraft.world.entity.animal.horse.Llama.Variant.byId(color.ordinal()));
    }

    @Override
    public LlamaInventory getInventory() {
        return new CraftInventoryLlama(getHandle().inventory);
    }

    @Override
    public int getStrength() {
       return getHandle().getStrength();
    }

    @Override
    public void setStrength(int strength) {
        Preconditions.checkArgument(1 <= strength && strength <= 5, "strength must be [1,5]");
        if (strength == getStrength()) return;
        getHandle().setStrengthPublic(strength);
        getHandle().createInventory();
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.LLAMA;
    }

    @Override
    public String toString() {
        return "CraftLlama";
    }
}
