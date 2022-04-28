package org.bukkit.craftbukkit.v1_16_R3;

import net.minecraft.util.math.RayTraceContext.FluidMode;
import org.bukkit.FluidCollisionMode;

public final class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static FluidMode toNMS(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) return null;

        switch (fluidCollisionMode) {
            case ALWAYS:
                return FluidMode.ANY;
            case SOURCE_ONLY:
                return FluidMode.SOURCE_ONLY;
            case NEVER:
                return FluidMode.NONE;
            default:
                return null;
        }
    }
}
