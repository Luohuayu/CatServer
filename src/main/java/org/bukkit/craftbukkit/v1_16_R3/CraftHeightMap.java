package org.bukkit.craftbukkit.v1_16_R3;

import org.bukkit.HeightMap;

final class CraftHeightMap {

    private CraftHeightMap() {
    }

    public static net.minecraft.world.gen.Heightmap.Type toNMS(HeightMap bukkitHeightMap) {
        switch (bukkitHeightMap) {
            case MOTION_BLOCKING_NO_LEAVES:
                return net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;
            case OCEAN_FLOOR:
                return net.minecraft.world.gen.Heightmap.Type.OCEAN_FLOOR;
            case OCEAN_FLOOR_WG:
                return net.minecraft.world.gen.Heightmap.Type.OCEAN_FLOOR_WG;
            case WORLD_SURFACE:
                return net.minecraft.world.gen.Heightmap.Type.WORLD_SURFACE;
            case WORLD_SURFACE_WG:
                return net.minecraft.world.gen.Heightmap.Type.WORLD_SURFACE_WG;
            case MOTION_BLOCKING:
                return net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING;
            default:
                throw new EnumConstantNotPresentException(net.minecraft.world.gen.Heightmap.Type.class, bukkitHeightMap.name());
        }
    }

    public static HeightMap fromNMS(net.minecraft.world.gen.Heightmap.Type nmsHeightMapType) {
        switch (nmsHeightMapType) {
            case WORLD_SURFACE_WG:
                return HeightMap.WORLD_SURFACE_WG;
            case WORLD_SURFACE:
                return HeightMap.WORLD_SURFACE;
            case OCEAN_FLOOR_WG:
                return HeightMap.OCEAN_FLOOR_WG;
            case OCEAN_FLOOR:
                return HeightMap.OCEAN_FLOOR;
            case MOTION_BLOCKING_NO_LEAVES:
                return HeightMap.MOTION_BLOCKING_NO_LEAVES;
            case MOTION_BLOCKING:
                return HeightMap.MOTION_BLOCKING;
            default:
                throw new EnumConstantNotPresentException(HeightMap.class, nmsHeightMapType.name());
        }
    }
}
