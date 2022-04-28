package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.CreeperEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreeperPowerEvent;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, CreeperEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isPowered() {
        return getHandle().isPowered();
    }

    @Override
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

        getHandle().maxSwell = ticks;
    }

    @Override
    public int getMaxFuseTicks() {
        return getHandle().maxSwell;
    }

    @Override
    public void setFuseTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks < 0");
        Preconditions.checkArgument(ticks <= getMaxFuseTicks(), "ticks > maxFuseTicks");

        getHandle().swell = ticks;
    }

    @Override
    public int getFuseTicks() {
        return getHandle().swell;
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
    public void explode() {
        getHandle().explodeCreeper();
    }

    @Override
    public void ignite() {
        getHandle().ignite();
    }

    @Override
    public CreeperEntity getHandle() {
        return (CreeperEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftCreeper";
    }

    @Override
    public EntityType getType() {
        return EntityType.CREEPER;
    }
}
