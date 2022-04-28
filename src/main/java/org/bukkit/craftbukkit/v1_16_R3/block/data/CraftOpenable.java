package org.bukkit.craftbukkit.v1_16_R3.block.data;

import org.bukkit.block.data.Openable;

public abstract class CraftOpenable extends CraftBlockData implements Openable {

    private static final net.minecraft.state.BooleanProperty OPEN = getBoolean("open");

    @Override
    public boolean isOpen() {
        return get(OPEN);
    }

    @Override
    public void setOpen(boolean open) {
        set(OPEN, open);
    }
}
