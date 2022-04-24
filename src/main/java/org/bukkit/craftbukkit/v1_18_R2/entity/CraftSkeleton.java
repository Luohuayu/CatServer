package org.bukkit.craftbukkit.v1_18_R2.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class CraftSkeleton extends CraftAbstractSkeleton implements Skeleton {

    public CraftSkeleton(CraftServer server, net.minecraft.world.entity.monster.Skeleton entity) {
        super(server, entity);
    }

    @Override
    public boolean isConverting() {
        return this.getHandle().isShaking(); // PAIL rename isStrayConverting
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(this.isConverting(), "Entity is not converting");
        return this.getHandle().conversionTime;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            this.getHandle().conversionTime = -1;
            this.getHandle().getEntityData().set(net.minecraft.world.entity.monster.Skeleton.DATA_STRAY_CONVERSION_ID, false);
        } else {
            this.getHandle().startFreezeConversion(time); // PAIL rename startStrayConversion
        }
    }

    @Override
    public net.minecraft.world.entity.monster.Skeleton getHandle() {
        return (net.minecraft.world.entity.monster.Skeleton) entity;
    }

    @Override
    public String toString() {
        return "CraftSkeleton";
    }

    @Override
    public EntityType getType() {
        return EntityType.SKELETON;
    }

    @Override
    public SkeletonType getSkeletonType() {
       return SkeletonType.NORMAL;
    }
}
