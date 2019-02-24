package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep {
    public CraftSheep(CraftServer server, EntitySheep entity) {
        super(server, entity);
    }

    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getHandle().getFleeceColor().getMetadata());
    }

    public void setColor(DyeColor color) {
        getHandle().setFleeceColor(EnumDyeColor.byMetadata(color.getWoolData()));
    }

    public boolean isSheared() {
        return getHandle().getSheared();
    }

    public void setSheared(boolean flag) {
        getHandle().setSheared(flag);
    }

    @Override
    public EntitySheep getHandle() {
        return (EntitySheep) entity;
    }

    @Override
    public String toString() {
        return "CraftSheep";
    }

    public EntityType getType() {
        return EntityType.SHEEP;
    }
}
