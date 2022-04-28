package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.horse.TraderLlamaEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TraderLlama;

public class CraftTraderLlama extends CraftLlama implements TraderLlama {

    public CraftTraderLlama(CraftServer server, TraderLlamaEntity entity) {
        super(server, entity);
    }

    @Override
    public TraderLlamaEntity getHandle() {
        return (TraderLlamaEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTraderLlama";
    }

    @Override
    public EntityType getType() {
        return EntityType.TRADER_LLAMA;
    }
}
