package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.MooshroomEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;

public class CraftMushroomCow extends CraftCow implements MushroomCow {
    public CraftMushroomCow(CraftServer server, MooshroomEntity entity) {
        super(server, entity);
    }

    @Override
    public MooshroomEntity getHandle() {
        return (MooshroomEntity) entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getMushroomType().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setMushroomType(MooshroomEntity.Type.values()[variant.ordinal()]);
    }

    @Override
    public String toString() {
        return "CraftMushroomCow";
    }

    @Override
    public EntityType getType() {
        return EntityType.MUSHROOM_COW;
    }
}
