package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.PiglinBrute;

public class CraftPiglinBrute extends CraftPiglinAbstract implements PiglinBrute {

    public CraftPiglinBrute(CraftServer server, net.minecraft.world.entity.monster.piglin.PiglinBrute entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.piglin.PiglinBrute getHandle() {
        return (net.minecraft.world.entity.monster.piglin.PiglinBrute) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftPiglinBrute";
    }
}
