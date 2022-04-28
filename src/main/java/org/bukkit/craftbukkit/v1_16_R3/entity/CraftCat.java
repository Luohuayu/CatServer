package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.CatEntity;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;

public class CraftCat extends CraftTameableAnimal implements Cat {

    public CraftCat(CraftServer server, CatEntity entity) {
        super(server, entity);
    }

    @Override
    public CatEntity getHandle() {
        return (CatEntity) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.CAT;
    }

    @Override
    public String toString() {
        return "CraftCat";
    }

    @Override
    public Type getCatType() {
        return Type.values()[getHandle().getCatType()];
    }

    @Override
    public void setCatType(Type type) {
        Preconditions.checkArgument(type != null, "Cannot have null Type");

        getHandle().setCatType(type.ordinal());
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
