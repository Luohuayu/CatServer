package org.bukkit.craftbukkit.v1_20_R1.util;

import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import org.bukkit.util.BlockVector;

public final class CraftBlockVector {

    private CraftBlockVector() {
    }

    public static BlockPos toBlockPosition(BlockVector blockVector) {
        return new BlockPos(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    public static BlockVector toBukkit(Vec3i baseBlockPosition) {
        return new BlockVector(baseBlockPosition.getX(), baseBlockPosition.getY(), baseBlockPosition.getZ());
    }
}
