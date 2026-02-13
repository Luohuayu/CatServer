package org.bukkit.craftbukkit.v1_20_R1.entity;

import net.minecraft.world.entity.projectile.ThrownEgg;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Egg;

public class CraftEgg extends CraftThrowableProjectile implements Egg {
    public CraftEgg(CraftServer server, ThrownEgg entity) {
        super(server, entity);
    }

    @Override
    public ThrownEgg getHandle() {
        return (ThrownEgg) entity;
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }
}
