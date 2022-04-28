/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftJigsaw extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.Jigsaw {

    public CraftJigsaw() {
        super();
    }

    public CraftJigsaw(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftJigsaw

    private static final net.minecraft.state.EnumProperty<?> ORIENTATION = getEnum(net.minecraft.block.JigsawBlock.class, "orientation");

    @Override
    public Orientation getOrientation() {
        return get(ORIENTATION, Orientation.class);
    }

    @Override
    public void setOrientation(Orientation orientation) {
        set(ORIENTATION, orientation);
    }
}
