package org.bukkit.craftbukkit.v1_16_R3.util;

import net.minecraft.util.math.vector.Vector3d;

public final class CraftVector {

    private CraftVector() {
    }

    public static org.bukkit.util.Vector toBukkit(Vector3d nms) {
        return new org.bukkit.util.Vector(nms.x, nms.y, nms.z);
    }

    public static Vector3d toNMS(org.bukkit.util.Vector bukkit) {
        return new Vector3d(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
}
