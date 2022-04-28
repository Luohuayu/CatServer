/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftRedstoneWire extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.RedstoneWire, org.bukkit.block.data.AnaloguePowerable {

    public CraftRedstoneWire() {
        super();
    }

    public CraftRedstoneWire(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftRedstoneWire

    private static final net.minecraft.state.EnumProperty<?> NORTH = getEnum(net.minecraft.block.RedstoneWireBlock.class, "north");
    private static final net.minecraft.state.EnumProperty<?> EAST = getEnum(net.minecraft.block.RedstoneWireBlock.class, "east");
    private static final net.minecraft.state.EnumProperty<?> SOUTH = getEnum(net.minecraft.block.RedstoneWireBlock.class, "south");
    private static final net.minecraft.state.EnumProperty<?> WEST = getEnum(net.minecraft.block.RedstoneWireBlock.class, "west");

    @Override
    public Connection getFace(org.bukkit.block.BlockFace face) {
        switch (face) {
            case NORTH:
                return get(NORTH, Connection.class);
            case EAST:
                return get(EAST, Connection.class);
            case SOUTH:
                return get(SOUTH, Connection.class);
            case WEST:
                return get(WEST, Connection.class);
            default:
                throw new IllegalArgumentException("Cannot have face " + face);
        }
    }

    @Override
    public void setFace(org.bukkit.block.BlockFace face, Connection connection) {
        switch (face) {
            case NORTH:
                set(NORTH, connection);
                break;
            case EAST:
                set(EAST, connection);
                break;
            case SOUTH:
                set(SOUTH, connection);
                break;
            case WEST:
                set(WEST, connection);
                break;
            default:
                throw new IllegalArgumentException("Cannot have face " + face);
        }
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getAllowedFaces() {
        return com.google.common.collect.ImmutableSet.of(org.bukkit.block.BlockFace.NORTH, org.bukkit.block.BlockFace.EAST, org.bukkit.block.BlockFace.SOUTH, org.bukkit.block.BlockFace.WEST);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftAnaloguePowerable

    private static final net.minecraft.state.IntegerProperty POWER = getInteger(net.minecraft.block.RedstoneWireBlock.class, "power");

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
