package org.bukkit.craftbukkit.v1_18_R2.entity;

import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull extends CraftFireball implements WitherSkull {
    public CraftWitherSkull(CraftServer server, net.minecraft.world.entity.projectile.WitherSkull entity) {
        super(server, entity);
    }

    @Override
    public void setCharged(boolean charged) {
        getHandle().setDangerous(charged);
    }

    @Override
    public boolean isCharged() {
        return getHandle().isDangerous();
    }

    @Override
    public net.minecraft.world.entity.projectile.WitherSkull getHandle() {
        return (net.minecraft.world.entity.projectile.WitherSkull) entity;
    }

    @Override
    public String toString() {
        return "CraftWitherSkull";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER_SKULL;
    }
}
