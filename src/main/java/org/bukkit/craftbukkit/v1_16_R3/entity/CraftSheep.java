package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.SheepEntity;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep {
    public CraftSheep(CraftServer server, SheepEntity entity) {
        super(server, entity);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getHandle().getColor().getId());
    }

    @Override
    public void setColor(DyeColor color) {
        getHandle().setColor(net.minecraft.item.DyeColor.byId(color.getWoolData()));
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
    public SheepEntity getHandle() {
        return (SheepEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSheep";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHEEP;
    }
}
