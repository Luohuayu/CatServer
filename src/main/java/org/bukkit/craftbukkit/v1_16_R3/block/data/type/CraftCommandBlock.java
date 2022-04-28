package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftCommandBlock extends CraftBlockData implements CommandBlock {

    private static final net.minecraft.state.BooleanProperty CONDITIONAL = getBoolean("conditional");

    @Override
    public boolean isConditional() {
        return get(CONDITIONAL);
    }

    @Override
    public void setConditional(boolean conditional) {
        set(CONDITIONAL, conditional);
    }
}
