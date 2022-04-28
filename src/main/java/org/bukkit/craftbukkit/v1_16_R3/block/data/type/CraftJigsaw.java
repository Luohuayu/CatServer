package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftJigsaw extends CraftBlockData implements Jigsaw {

    private static final net.minecraft.state.EnumProperty<?> ORIENTATION = getEnum("orientation");

    @Override
    public Orientation getOrientation() {
        return get(ORIENTATION, Orientation.class);
    }

    @Override
    public void setOrientation(Orientation orientation) {
        set(ORIENTATION, orientation);
    }
}
