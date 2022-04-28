/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftJukeBox extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.Jukebox {

    public CraftJukeBox() {
        super();
    }

    public CraftJukeBox(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftJukebox

    private static final net.minecraft.state.BooleanProperty HAS_RECORD = getBoolean(net.minecraft.block.JukeboxBlock.class, "has_record");

    @Override
    public boolean hasRecord() {
        return get(HAS_RECORD);
    }
}
