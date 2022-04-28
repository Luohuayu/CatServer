/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftTurtleEgg extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.TurtleEgg {

    public CraftTurtleEgg() {
        super();
    }

    public CraftTurtleEgg(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftTurtleEgg

    private static final net.minecraft.state.IntegerProperty EGGS = getInteger(net.minecraft.block.TurtleEggBlock.class, "eggs");
    private static final net.minecraft.state.IntegerProperty HATCH = getInteger(net.minecraft.block.TurtleEggBlock.class, "hatch");

    @Override
    public int getEggs() {
        return get(EGGS);
    }

    @Override
    public void setEggs(int eggs) {
        set(EGGS, eggs);
    }

    @Override
    public int getMinimumEggs() {
        return getMin(EGGS);
    }

    @Override
    public int getMaximumEggs() {
        return getMax(EGGS);
    }

    @Override
    public int getHatch() {
        return get(HATCH);
    }

    @Override
    public void setHatch(int hatch) {
        set(HATCH, hatch);
    }

    @Override
    public int getMaximumHatch() {
        return getMax(HATCH);
    }
}
