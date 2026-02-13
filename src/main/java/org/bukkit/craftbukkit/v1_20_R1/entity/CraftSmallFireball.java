package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.SmallFireball;

public class CraftSmallFireball extends CraftSizedFireball implements SmallFireball {
    public CraftSmallFireball(CraftServer server, net.minecraft.world.entity.projectile.SmallFireball entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.SmallFireball getHandle() {
        return (net.minecraft.world.entity.projectile.SmallFireball) entity;
    }

    @Override
    public String toString() {
        return "CraftSmallFireball";
    }
}
