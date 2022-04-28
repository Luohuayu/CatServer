package org.bukkit.craftbukkit.v1_16_R3.block.data;

import org.bukkit.block.data.Powerable;

public abstract class CraftPowerable extends CraftBlockData implements Powerable {

    private static final net.minecraft.state.BooleanProperty POWERED = getBoolean("powered");

    @Override
    public boolean isPowered() {
        return get(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        set(POWERED, powered);
    }
}
