package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.Snow;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public class CraftSnow extends CraftBlockData implements Snow {

    private static final net.minecraft.state.IntegerProperty LAYERS = getInteger("layers");

    @Override
    public int getLayers() {
        return get(LAYERS);
    }

    @Override
    public void setLayers(int layers) {
        set(LAYERS, layers);
    }

    @Override
    public int getMinimumLayers() {
        return getMin(LAYERS);
    }

    @Override
    public int getMaximumLayers() {
        return getMax(LAYERS);
    }
}
