/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftStepAbstract extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.Slab, org.bukkit.block.data.Waterlogged {

    public CraftStepAbstract() {
        super();
    }

    public CraftStepAbstract(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftSlab

    private static final net.minecraft.state.EnumProperty<?> TYPE = getEnum(net.minecraft.block.SlabBlock.class, "type");

    @Override
    public Type getType() {
        return get(TYPE, Type.class);
    }

    @Override
    public void setType(Type type) {
        set(TYPE, type);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftWaterlogged

    private static final net.minecraft.state.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.block.SlabBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
