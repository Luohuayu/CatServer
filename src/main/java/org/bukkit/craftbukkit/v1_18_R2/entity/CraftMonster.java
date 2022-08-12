package org.bukkit.craftbukkit.v1_18_R2.entity;

import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster {

    public CraftMonster(CraftServer server, net.minecraft.world.entity.monster.Monster entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Monster getHandle() {
        return (net.minecraft.world.entity.monster.Monster) entity;
    }

    @Override
    public String toString() {
        return "CraftMonster";
    }
}
