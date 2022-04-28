/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftRepeater extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.Repeater, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftRepeater() {
        super();
    }

    public CraftRepeater(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftRepeater

    private static final net.minecraft.state.IntegerProperty DELAY = getInteger(net.minecraft.block.RepeaterBlock.class, "delay");
    private static final net.minecraft.state.BooleanProperty LOCKED = getBoolean(net.minecraft.block.RepeaterBlock.class, "locked");

    @Override
    public int getDelay() {
        return get(DELAY);
    }

    @Override
    public void setDelay(int delay) {
        set(DELAY, delay);
    }

    @Override
    public int getMinimumDelay() {
        return getMin(DELAY);
    }

    @Override
    public int getMaximumDelay() {
        return getMax(DELAY);
    }

    @Override
    public boolean isLocked() {
        return get(LOCKED);
    }

    @Override
    public void setLocked(boolean locked) {
        set(LOCKED, locked);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftDirectional

    private static final net.minecraft.state.EnumProperty<?> FACING = getEnum(net.minecraft.block.RepeaterBlock.class, "facing");

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

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftPowerable

    private static final net.minecraft.state.BooleanProperty POWERED = getBoolean(net.minecraft.block.RepeaterBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return get(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        set(POWERED, powered);
    }
}
