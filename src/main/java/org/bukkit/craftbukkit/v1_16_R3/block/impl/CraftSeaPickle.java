/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftSeaPickle extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.SeaPickle, org.bukkit.block.data.Waterlogged {

    public CraftSeaPickle() {
        super();
    }

    public CraftSeaPickle(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftSeaPickle

    private static final net.minecraft.state.IntegerProperty PICKLES = getInteger(net.minecraft.block.SeaPickleBlock.class, "pickles");

    @Override
    public int getPickles() {
        return get(PICKLES);
    }

    @Override
    public void setPickles(int pickles) {
        set(PICKLES, pickles);
    }

    @Override
    public int getMinimumPickles() {
        return getMin(PICKLES);
    }

    @Override
    public int getMaximumPickles() {
        return getMax(PICKLES);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftWaterlogged

    private static final net.minecraft.state.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.block.SeaPickleBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
