package org.bukkit.craftbukkit.v1_16_R3.util;


import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class CraftDimensionUtil {

    private CraftDimensionUtil() {
    }

    public static RegistryKey<World> getMainDimensionKey(World world) {
        RegistryKey<DimensionType> typeKey = world.getTypeKey();
        if (typeKey == DimensionType.OVERWORLD_LOCATION) {
            return World.OVERWORLD;
        } else if (typeKey == DimensionType.NETHER_LOCATION) {
            return World.NETHER;
        } else if (typeKey == DimensionType.END_LOCATION) {
            return World.END;
        }

        return world.dimension();
    }
}