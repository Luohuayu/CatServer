package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftSeaPickle extends CraftBlockData implements SeaPickle {

    private static final net.minecraft.state.IntegerProperty PICKLES = getInteger("pickles");

    @Override
    public int getPickles() {
        return get(PICKLES);
    }

    @Override
    public void setPickles(int pickles) {
        set(PICKLES, pickles);
    }

    @Override
    public int getMinimumPickles() {
        return getMin(PICKLES);
    }

    @Override
    public int getMaximumPickles() {
        return getMax(PICKLES);
    }
}
