package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Snowball;

public class CraftSnowball extends CraftThrowableProjectile implements Snowball {
    public CraftSnowball(CraftServer server, net.minecraft.world.entity.projectile.Snowball entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.Snowball getHandle() {
        return (net.minecraft.world.entity.projectile.Snowball) entity;
    }

    @Override
    public String toString() {
        return "CraftSnowball";
    }
}
