package org.bukkit.craftbukkit.v1_16_R3.block.data;

import org.bukkit.block.data.Attachable;

public abstract class CraftAttachable extends CraftBlockData implements Attachable {

    private static final net.minecraft.state.BooleanProperty ATTACHED = getBoolean("attached");

    @Override
    public boolean isAttached() {
        return get(ATTACHED);
    }

    @Override
    public void setAttached(boolean attached) {
        set(ATTACHED, attached);
    }
}
