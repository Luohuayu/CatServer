package org.bukkit.craftbukkit.v1_16_R3.block.data;

import org.bukkit.block.data.Levelled;

public abstract class CraftLevelled extends CraftBlockData implements Levelled {

    private static final net.minecraft.state.IntegerProperty LEVEL = getInteger("level");

    @Override
    public int getLevel() {
        return get(LEVEL);
    }

    @Override
    public void setLevel(int level) {
        set(LEVEL, level);
    }

    @Override
    public int getMaximumLevel() {
        return getMax(LEVEL);
    }
}
