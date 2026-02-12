package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
    public CraftWolf(CraftServer server, net.minecraft.world.entity.animal.Wolf wolf) {
        super(server, wolf);
    }

    @Override
    public boolean isAngry() {
        return getHandle().isAngry();
    }

    @Override
    public void setAngry(boolean angry) {
        if (angry) {
            getHandle().startPersistentAngerTimer();
        } else {
            getHandle().stopBeingAngry();
        }
    }

    @Override
    public net.minecraft.world.entity.animal.Wolf getHandle() {
        return (net.minecraft.world.entity.animal.Wolf) entity;
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        getHandle().setCollarColor(net.minecraft.world.item.DyeColor.byId(color.getWoolData()));
    }

    @Override
    public boolean isWet() {
        return getHandle().isWet();
    }

    @Override
    public float getTailAngle() {
        return getHandle().getTailAngle();
    }

    @Override
    public boolean isInterested() {
        return getHandle().isInterested();
    }

    @Override
    public void setInterested(boolean flag) {
        getHandle().setIsInterested(flag);
    }
}
