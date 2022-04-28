/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftPressurePlateWeighted extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.AnaloguePowerable {

    public CraftPressurePlateWeighted() {
        super();
    }

    public CraftPressurePlateWeighted(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftAnaloguePowerable

    private static final net.minecraft.state.IntegerProperty POWER = getInteger(net.minecraft.block.WeightedPressurePlateBlock.class, "power");

    @Override
    public int getPower() {
        return get(POWER);
    }

    @Override
    public void setPower(int power) {
        set(POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(POWER);
    }
}
