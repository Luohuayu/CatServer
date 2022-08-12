package org.bukkit.craftbukkit.v1_18_R2.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vex;

public class CraftVex extends CraftMonster implements Vex {

    public CraftVex(CraftServer server, net.minecraft.world.entity.monster.Vex entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Vex getHandle() {
        return (net.minecraft.world.entity.monster.Vex) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftVex";
    }

    @Override
    public EntityType getType() {
        return EntityType.VEX;
    }

    @Override
    public boolean isCharging() {
        return getHandle().isCharging();
    }

    @Override
    public void setCharging(boolean charging) {
        getHandle().setIsCharging(charging);
    }

    @Override
    public Location getBound() {
        BlockPos blockPosition = getHandle().getBoundOrigin();
        return (blockPosition == null) ? null : new Location(getWorld(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    @Override
    public void setBound(Location location) {
        if (location == null) {
            getHandle().setBoundOrigin(null);
        } else {
            Preconditions.checkArgument(getWorld().equals(location.getWorld()), "The bound world cannot be different to the entity's world.");
            getHandle().setBoundOrigin(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }
    }

    @Override
    public int getLifeTicks() {
        return getHandle().limitedLifeTicks;
    }

    @Override
    public void setLifeTicks(int lifeTicks) {
        getHandle().setLimitedLife(lifeTicks);
        if (lifeTicks < 0) {
            getHandle().hasLimitedLife = false;
        }
    }

    @Override
    public boolean hasLimitedLife() {
        return getHandle().hasLimitedLife;
    }
}
