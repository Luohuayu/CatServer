package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.item.EnderPearlEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;

public class CraftEnderPearl extends CraftThrowableProjectile implements EnderPearl {
    public CraftEnderPearl(CraftServer server, EnderPearlEntity entity) {
        super(server, entity);
    }

    @Override
    public EnderPearlEntity getHandle() {
        return (EnderPearlEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderPearl";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_PEARL;
    }
}
