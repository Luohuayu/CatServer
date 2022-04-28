package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.PillagerEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pillager;
import org.bukkit.inventory.Inventory;

public class CraftPillager extends CraftIllager implements Pillager {

    public CraftPillager(CraftServer server, PillagerEntity entity) {
        super(server, entity);
    }

    @Override
    public PillagerEntity getHandle() {
        return (PillagerEntity) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.PILLAGER;
    }

    @Override
    public String toString() {
        return "CraftPillager";
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(getHandle().inventory);
    }
}
