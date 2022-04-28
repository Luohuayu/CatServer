package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.Switch;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftSwitch extends CraftBlockData implements Switch {

    private static final net.minecraft.state.EnumProperty<?> FACE = getEnum("face");

    @Override
    public Face getFace() {
        return get(FACE, Face.class);
    }

    @Override
    public void setFace(Face face) {
        set(FACE, face);
    }
}
