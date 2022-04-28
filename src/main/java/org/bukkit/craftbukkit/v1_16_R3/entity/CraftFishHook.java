package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;

public class CraftFishHook extends CraftProjectile implements FishHook {
    private double biteChance = -1;

    public CraftFishHook(CraftServer server, FishingBobberEntity entity) {
        super(server, entity);
    }

    @Override
    public FishingBobberEntity getHandle() {
        return (FishingBobberEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFishingHook";
    }

    @Override
    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }

    @Override
    public int getMinWaitTime() {
        return getHandle().minWaitTime;
    }

    @Override
    public void setMinWaitTime(int minWaitTime) {
        FishingBobberEntity hook = getHandle();
        Validate.isTrue(minWaitTime >= 0 && minWaitTime <= this.getMaxWaitTime(), "The minimum wait time should be between 0 and the maximum wait time.");
        hook.minWaitTime = minWaitTime;
    }

    @Override
    public int getMaxWaitTime() {
        return getHandle().maxWaitTime;
    }

    @Override
    public void setMaxWaitTime(int maxWaitTime) {
        FishingBobberEntity hook = getHandle();
        Validate.isTrue(maxWaitTime >= 0 && maxWaitTime >= this.getMinWaitTime(), "The maximum wait time should be higher than or equal to 0 and the minimum wait time.");
        hook.maxWaitTime = maxWaitTime;
    }

    @Override
    public boolean getApplyLure() {
        return getHandle().applyLure;
    }

    @Override
    public void setApplyLure(boolean applyLure) {
        getHandle().applyLure = applyLure;
    }

    @Override
    public double getBiteChance() {
        FishingBobberEntity hook = getHandle();

        if (this.biteChance == -1) {
            if (hook.level.isRainingAt(new BlockPos(MathHelper.floor(hook.getX()), MathHelper.floor(hook.getY()) + 1, MathHelper.floor(hook.getZ())))) {
                return 1 / 300.0;
            }
            return 1 / 500.0;
        }
        return this.biteChance;
    }

    @Override
    public void setBiteChance(double chance) {
        Validate.isTrue(chance >= 0 && chance <= 1, "The bite chance must be between 0 and 1.");
        this.biteChance = chance;
    }

    @Override
    public boolean isInOpenWater() {
        return getHandle().isOpenWaterFishing();
    }

    @Override
    public Entity getHookedEntity() {
        net.minecraft.entity.Entity hooked = getHandle().hookedIn;
        return (hooked != null) ? hooked.getBukkitEntity() : null;
    }

    @Override
    public void setHookedEntity(Entity entity) {
        FishingBobberEntity hook = getHandle();

        hook.hookedIn = (entity != null) ? ((CraftEntity) entity).getHandle() : null;
        hook.getEntityData().set(FishingBobberEntity.DATA_HOOKED_ENTITY, hook.hookedIn != null ? hook.hookedIn.getId() + 1 : 0);
    }

    @Override
    public boolean pullHookedEntity() {
        FishingBobberEntity hook = getHandle();
        if (hook.hookedIn == null) {
            return false;
        }

        hook.bringInHookedEntity();
        return true;
    }

    @Override
    public HookState getState() {
        String name = String.valueOf(getHandle().currentState);
        if (name.equals("FLYING")) {
            return HookState.UNHOOKED;
        }
        if (name.equals("HOOKED_IN_ENTITY")) {
            return HookState.HOOKED_ENTITY;
        }
        return HookState.valueOf(name);
    }
}
