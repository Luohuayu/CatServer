package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.item.minecart.SpawnerMinecartEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.SpawnerMinecart;

final class CraftMinecartMobSpawner extends CraftMinecart implements SpawnerMinecart {
    CraftMinecartMobSpawner(CraftServer server, SpawnerMinecartEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartMobSpawner";
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART_MOB_SPAWNER;
    }
}
