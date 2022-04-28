package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftEndPortalFrame extends CraftBlockData implements EndPortalFrame {

    private static final net.minecraft.state.BooleanProperty EYE = getBoolean("eye");

    @Override
    public boolean hasEye() {
        return get(EYE);
    }

    @Override
    public void setEye(boolean eye) {
        set(EYE, eye);
    }
}
