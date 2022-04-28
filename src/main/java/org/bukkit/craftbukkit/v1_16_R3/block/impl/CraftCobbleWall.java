/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftCobbleWall extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.Wall, org.bukkit.block.data.Waterlogged {

    public CraftCobbleWall() {
        super();
    }

    public CraftCobbleWall(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftWall

    private static final net.minecraft.state.BooleanProperty UP = getBoolean(net.minecraft.block.WallBlock.class, "up");
    private static final net.minecraft.state.EnumProperty<?>[] HEIGHTS = new net.minecraft.state.EnumProperty[]{
        getEnum(net.minecraft.block.WallBlock.class, "north"), getEnum(net.minecraft.block.WallBlock.class, "east"), getEnum(net.minecraft.block.WallBlock.class, "south"), getEnum(net.minecraft.block.WallBlock.class, "west")
    };

    @Override
    public boolean isUp() {
        return get(UP);
    }

    @Override
    public void setUp(boolean up) {
        set(UP, up);
    }

    @Override
    public Height getHeight(org.bukkit.block.BlockFace face) {
        return get(HEIGHTS[face.ordinal()], Height.class);
    }

    @Override
    public void setHeight(org.bukkit.block.BlockFace face, Height height) {
        set(HEIGHTS[face.ordinal()], height);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftWaterlogged

    private static final net.minecraft.state.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.block.WallBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
