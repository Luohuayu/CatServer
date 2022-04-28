package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftRespawnAnchor extends CraftBlockData implements RespawnAnchor {

    private static final net.minecraft.state.IntegerProperty CHARGES = getInteger("charges");

    @Override
    public int getCharges() {
        return get(CHARGES);
    }

    @Override
    public void setCharges(int charges) {
        set(CHARGES, charges);
    }

    @Override
    public int getMaximumCharges() {
        return getMax(CHARGES);
    }
}
