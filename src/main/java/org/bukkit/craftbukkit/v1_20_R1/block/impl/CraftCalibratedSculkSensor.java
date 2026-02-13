/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_20_R1.block.impl;

public final class CraftCalibratedSculkSensor extends org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.CalibratedSculkSensor, org.bukkit.block.data.Directional, org.bukkit.block.data.type.SculkSensor, org.bukkit.block.data.AnaloguePowerable, org.bukkit.block.data.Waterlogged {

    public CraftCalibratedSculkSensor() {
        super();
    }

    public CraftCalibratedSculkSensor(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_20_R1.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.CalibratedSculkSensorBlock.class, "facing");

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

    // org.bukkit.craftbukkit.v1_20_R1.block.data.type.CraftSculkSensor

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> PHASE = getEnum(net.minecraft.world.level.block.CalibratedSculkSensorBlock.class, "sculk_sensor_phase");

    @Override
    public org.bukkit.block.data.type.SculkSensor.Phase getPhase() {
        return get(PHASE, org.bukkit.block.data.type.SculkSensor.Phase.class);
    }

    @Override
    public void setPhase(org.bukkit.block.data.type.SculkSensor.Phase phase) {
        set(PHASE, phase);
    }

    // org.bukkit.craftbukkit.v1_20_R1.block.data.CraftAnaloguePowerable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty POWER = getInteger(net.minecraft.world.level.block.CalibratedSculkSensorBlock.class, "power");

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

    // org.bukkit.craftbukkit.v1_20_R1.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.CalibratedSculkSensorBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
