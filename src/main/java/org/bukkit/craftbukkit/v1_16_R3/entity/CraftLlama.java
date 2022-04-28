package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.horse.LlamaEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryLlama;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.LlamaInventory;

public class CraftLlama extends CraftChestedHorse implements Llama {

    public CraftLlama(CraftServer server, LlamaEntity entity) {
        super(server, entity);
    }

    @Override
    public LlamaEntity getHandle() {
        return (LlamaEntity) super.getHandle();
    }

    @Override
    public Color getColor() {
        return Color.values()[getHandle().getVariant()];
    }

    @Override
    public void setColor(Color color) {
        Preconditions.checkArgument(color != null, "color");

        getHandle().setVariant(color.ordinal());
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
        getHandle().setStrength(strength);
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

    @Override
    public EntityType getType() {
        return EntityType.LLAMA;
    }
}
