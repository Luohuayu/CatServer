package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.item.FallingBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;

public class CraftFallingBlock extends CraftEntity implements FallingBlock {

    public CraftFallingBlock(CraftServer server, FallingBlockEntity entity) {
        super(server, entity);
    }

    @Override
    public FallingBlockEntity getHandle() {
        return (FallingBlockEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFallingBlock";
    }

    @Override
    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }

    @Override
    public Material getMaterial() {
        return getBlockData().getMaterial();
    }

    @Override
    public BlockData getBlockData() {
        return CraftBlockData.fromData(getHandle().getBlockState());
    }

    @Override
    public boolean getDropItem() {
        return getHandle().dropItem;
    }

    @Override
    public void setDropItem(boolean drop) {
        getHandle().dropItem = drop;
    }

    @Override
    public boolean canHurtEntities() {
        return getHandle().hurtEntities;
    }

    @Override
    public void setHurtEntities(boolean hurtEntities) {
        getHandle().hurtEntities = hurtEntities;
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for FallingBlockEntity
        getHandle().time = value;
    }
}
