package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.Tripwire;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftTripwire extends CraftBlockData implements Tripwire {

    private static final net.minecraft.state.BooleanProperty DISARMED = getBoolean("disarmed");

    @Override
    public boolean isDisarmed() {
        return get(DISARMED);
    }

    @Override
    public void setDisarmed(boolean disarmed) {
        set(DISARMED, disarmed);
    }
}
