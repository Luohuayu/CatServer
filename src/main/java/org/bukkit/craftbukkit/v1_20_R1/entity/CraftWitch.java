package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Witch;

public class CraftWitch extends CraftRaider implements Witch {
    public CraftWitch(CraftServer server, net.minecraft.world.entity.monster.Witch entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Witch getHandle() {
        return (net.minecraft.world.entity.monster.Witch) entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    @Override
    public boolean isDrinkingPotion() {
        return getHandle().isDrinkingPotion();
    }
}
