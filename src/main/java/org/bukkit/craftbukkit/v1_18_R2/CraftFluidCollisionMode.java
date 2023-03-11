package org.bukkit.craftbukkit.v1_18_R2;

import net.minecraft.world.level.ClipContext;
import org.bukkit.FluidCollisionMode;

public final class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static ClipContext.Fluid toNMS(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) return null;

        switch (fluidCollisionMode) {
            case ALWAYS:
                return ClipContext.Fluid.ANY;
            case SOURCE_ONLY:
                return ClipContext.Fluid.SOURCE_ONLY;
            case NEVER:
                return ClipContext.Fluid.NONE;
            default:
                return null;
        }
    }
}
