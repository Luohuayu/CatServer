package org.bukkit.craftbukkit.v1_16_R3.block.data;

import org.bukkit.block.data.Waterlogged;

public abstract class CraftWaterlogged extends CraftBlockData implements Waterlogged {

    private static final net.minecraft.state.BooleanProperty WATERLOGGED = getBoolean("waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
