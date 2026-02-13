package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Endermite;

public class CraftEndermite extends CraftMonster implements Endermite {

    public CraftEndermite(CraftServer server, net.minecraft.world.entity.monster.Endermite entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Endermite getHandle() {
        return (net.minecraft.world.entity.monster.Endermite) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEndermite";
    }

    @Override
    public boolean isPlayerSpawned() {
        return false;
    }

    @Override
    public void setPlayerSpawned(boolean playerSpawned) {
        // Nop
    }
}
