package org.bukkit.craftbukkit.v1_16_R3.block.data.type;

import org.bukkit.block.data.type.Bed;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public abstract class CraftBed extends CraftBlockData implements Bed {

    private static final net.minecraft.state.EnumProperty<?> PART = getEnum("part");
    private static final net.minecraft.state.BooleanProperty OCCUPIED = getBoolean("occupied");

    @Override
    public Part getPart() {
        return get(PART, Part.class);
    }

    @Override
    public void setPart(Part part) {
        set(PART, part);
    }

    @Override
    public boolean isOccupied() {
        return get(OCCUPIED);
    }
}
