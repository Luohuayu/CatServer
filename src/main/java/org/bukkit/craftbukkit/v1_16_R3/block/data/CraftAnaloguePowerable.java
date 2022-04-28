package org.bukkit.craftbukkit.v1_16_R3.block.data;

import org.bukkit.block.data.AnaloguePowerable;

public abstract class CraftAnaloguePowerable extends CraftBlockData implements AnaloguePowerable {

    private static final net.minecraft.state.IntegerProperty POWER = getInteger("power");

    @Override
    public int getPower() {
        return get(POWER);
    }

    @Override
    public void setPower(int power) {
        set(POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(POWER);
    }
}
