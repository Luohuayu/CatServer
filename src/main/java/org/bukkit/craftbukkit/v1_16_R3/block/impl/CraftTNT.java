/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftTNT extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.TNT {

    public CraftTNT() {
        super();
    }

    public CraftTNT(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftTNT

    private static final net.minecraft.state.BooleanProperty UNSTABLE = getBoolean(net.minecraft.block.TNTBlock.class, "unstable");

    @Override
    public boolean isUnstable() {
        return get(UNSTABLE);
    }

    @Override
    public void setUnstable(boolean unstable) {
        set(UNSTABLE, unstable);
    }
}
