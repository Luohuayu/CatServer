package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftProjectile implements ThrownExpBottle {
    public CraftThrownExpBottle(CraftServer server, EntityExpBottle entity) {
        super(server, entity);
    }

    @Override
    public EntityExpBottle getHandle() {
        return (EntityExpBottle) entity;
    }

    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }

    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}
