/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_20_R1.block.impl;

public final class CraftPinkPetals extends org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.PinkPetals, org.bukkit.block.data.Directional {

    public CraftPinkPetals() {
        super();
    }

    public CraftPinkPetals(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_20_R1.block.data.type.CraftPinkPetals

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty FLOWER_AMOUNT = getInteger(net.minecraft.world.level.block.PinkPetalsBlock.class, "flower_amount");

    @Override
    public int getFlowerAmount() {
        return get(FLOWER_AMOUNT);
    }

    @Override
    public void setFlowerAmount(int flower_amount) {
        set(FLOWER_AMOUNT, flower_amount);
    }

    @Override
    public int getMaximumFlowerAmount() {
        return getMax(FLOWER_AMOUNT);
    }

    // org.bukkit.craftbukkit.v1_20_R1.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.PinkPetalsBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }
}
