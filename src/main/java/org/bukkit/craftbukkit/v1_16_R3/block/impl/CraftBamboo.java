/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftBamboo extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.Bamboo, org.bukkit.block.data.Ageable, org.bukkit.block.data.type.Sapling {

    public CraftBamboo() {
        super();
    }

    public CraftBamboo(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftBamboo

    private static final net.minecraft.state.EnumProperty<?> LEAVES = getEnum(net.minecraft.block.BambooBlock.class, "leaves");

    @Override
    public Leaves getLeaves() {
        return get(LEAVES, Leaves.class);
    }

    @Override
    public void setLeaves(Leaves leaves) {
        set(LEAVES, leaves);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftAgeable

    private static final net.minecraft.state.IntegerProperty AGE = getInteger(net.minecraft.block.BambooBlock.class, "age");

    @Override
    public int getAge() {
        return get(AGE);
    }

    @Override
    public void setAge(int age) {
        set(AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(AGE);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftSapling

    private static final net.minecraft.state.IntegerProperty STAGE = getInteger(net.minecraft.block.BambooBlock.class, "stage");

    @Override
    public int getStage() {
        return get(STAGE);
    }

    @Override
    public void setStage(int stage) {
        set(STAGE, stage);
    }

    @Override
    public int getMaximumStage() {
        return getMax(STAGE);
    }
}
