package org.bukkit.craftbukkit.v1_16_R3.block.data;

import org.bukkit.block.data.Bisected;

public class CraftBisected extends CraftBlockData implements Bisected {

    private static final net.minecraft.state.EnumProperty<?> HALF = getEnum("half");

    @Override
    public Half getHalf() {
        return get(HALF, Half.class);
    }

    @Override
    public void setHalf(Half half) {
        set(HALF, half);
    }
}
