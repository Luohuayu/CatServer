package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.ElderGuardian;

public class CraftElderGuardian extends CraftGuardian implements ElderGuardian {

    public CraftElderGuardian(CraftServer server, net.minecraft.world.entity.monster.ElderGuardian entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftElderGuardian";
    }

    @Override
    public boolean isElder() {
        return true;
    }
}
