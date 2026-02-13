package org.bukkit.craftbukkit.v1_20_R1.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep {
    public CraftSheep(CraftServer server, net.minecraft.world.entity.animal.Sheep entity) {
        super(server, entity);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getHandle().getColor().getId());
    }

    @Override
    public void setColor(DyeColor color) {
        getHandle().setColor(net.minecraft.world.item.DyeColor.byId(color.getWoolData()));
    }

    @Override
    public boolean isSheared() {
        return getHandle().isSheared();
    }

    @Override
    public void setSheared(boolean flag) {
        getHandle().setSheared(flag);
    }

    @Override
    public net.minecraft.world.entity.animal.Sheep getHandle() {
        return (net.minecraft.world.entity.animal.Sheep) entity;
    }

    @Override
    public String toString() {
        return "CraftSheep";
    }
}
