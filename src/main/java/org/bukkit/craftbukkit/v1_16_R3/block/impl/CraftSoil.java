/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftSoil extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.Farmland {

    public CraftSoil() {
        super();
    }

    public CraftSoil(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftFarmland

    private static final net.minecraft.state.IntegerProperty MOISTURE = getInteger(net.minecraft.block.FarmlandBlock.class, "moisture");

    @Override
    public int getMoisture() {
        return get(MOISTURE);
    }

    @Override
    public void setMoisture(int moisture) {
        set(MOISTURE, moisture);
    }

    @Override
    public int getMaximumMoisture() {
        return getMax(MOISTURE);
    }
}
