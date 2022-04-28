package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.ExplosiveMinecart;

final class CraftMinecartTNT extends CraftMinecart implements ExplosiveMinecart {
    CraftMinecartTNT(CraftServer server, TNTMinecartEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartTNT";
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART_TNT;
    }
}
