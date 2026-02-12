package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.WanderingTrader;

public class CraftWanderingTrader extends CraftAbstractVillager implements WanderingTrader {

    public CraftWanderingTrader(CraftServer server, net.minecraft.world.entity.npc.WanderingTrader entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.npc.WanderingTrader getHandle() {
        return (net.minecraft.world.entity.npc.WanderingTrader) entity;
    }

    @Override
    public String toString() {
        return "CraftWanderingTrader";
    }

    @Override
    public int getDespawnDelay() {
        return getHandle().getDespawnDelay();
    }

    @Override
    public void setDespawnDelay(int despawnDelay) {
        getHandle().setDespawnDelay(despawnDelay);
    }
}
