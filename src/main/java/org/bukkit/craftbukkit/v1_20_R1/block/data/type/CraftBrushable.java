package org.bukkit.craftbukkit.v1_20_R1.block.data.type;

import org.bukkit.block.data.Brushable;
import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData;

public abstract class CraftBrushable extends CraftBlockData implements Brushable {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DUSTED = getInteger("dusted");

    @Override
    public int getDusted() {
        return get(DUSTED);
    }

    @Override
    public void setDusted(int dusted) {
        set(DUSTED, dusted);
    }

    @Override
    public int getMaximumDusted() {
        return getMax(DUSTED);
    }
}
