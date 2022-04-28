package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.Jukebox;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftJukebox extends CraftBlockData implements Jukebox {

    private static final net.minecraft.state.BooleanProperty HAS_RECORD = getBoolean("has_record");

    @Override
    public boolean hasRecord() {
        return get(HAS_RECORD);
    }
}
