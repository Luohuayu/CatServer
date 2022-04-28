package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.WolfEntity;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
    public CraftWolf(CraftServer server, WolfEntity wolf) {
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
    public WolfEntity getHandle() {
        return (WolfEntity) entity;
    }

    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        getHandle().setCollarColor(net.minecraft.item.DyeColor.byId(color.getWoolData()));
    }
}
