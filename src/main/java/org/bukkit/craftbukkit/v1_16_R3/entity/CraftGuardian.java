package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.GuardianEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

public class CraftGuardian extends CraftMonster implements Guardian {

    public CraftGuardian(CraftServer server, GuardianEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public boolean isElder() {
        return false;
    }

    @Override
    public void setElder(boolean shouldBeElder) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
