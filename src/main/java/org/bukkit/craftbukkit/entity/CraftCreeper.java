package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;

import net.minecraft.entity.monster.EntityCreeper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreeperPowerEvent;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, EntityCreeper entity) {
        super(server, entity);
    }

    public boolean isPowered() {
        return getHandle().getPowered();
    }

    public void setPowered(boolean powered) {
        CraftServer server = this.server;
        Creeper entity = (Creeper) this.getHandle().getBukkitEntity();

        if (powered) {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_ON);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                getHandle().setPowered(true);
            }
        } else {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_OFF);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                getHandle().setPowered(false);
            }
        }
    }

    @Override
    public void setMaxFuseTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks < 0");

        getHandle().fuseTime = ticks;
    }

    @Override
    public int getMaxFuseTicks() {
        return getHandle().fuseTime;
    }

    @Override
    public void setExplosionRadius(int radius) {
        Preconditions.checkArgument(radius >= 0, "radius < 0");

        getHandle().explosionRadius = radius;
    }

    @Override
    public int getExplosionRadius() {
        return getHandle().explosionRadius;
    }

    @Override
    public EntityCreeper getHandle() {
        return (EntityCreeper) entity;
    }

    @Override
    public String toString() {
        return "CraftCreeper";
    }

    public EntityType getType() {
        return EntityType.CREEPER;
    }
}
