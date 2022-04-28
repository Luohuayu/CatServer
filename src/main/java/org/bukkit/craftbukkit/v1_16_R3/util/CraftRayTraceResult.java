package org.bukkit.craftbukkit.v1_16_R3.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class CraftRayTraceResult {

    private CraftRayTraceResult() {}

    public static RayTraceResult fromNMS(World world, net.minecraft.util.math.RayTraceResult nmsHitResult) {
        if (nmsHitResult == null || nmsHitResult.getType() == net.minecraft.util.math.RayTraceResult.Type.MISS) return null;

        Vector3d nmsHitPos = nmsHitResult.getLocation();
        Vector hitPosition = new Vector(nmsHitPos.x, nmsHitPos.y, nmsHitPos.z);
        BlockFace hitBlockFace = null;

        if (nmsHitResult.getType() ==  net.minecraft.util.math.RayTraceResult.Type.ENTITY) {
            Entity hitEntity = ((EntityRayTraceResult) nmsHitResult).getEntity().getBukkitEntity();
            return new RayTraceResult(hitPosition, hitEntity, null);
        }

        Block hitBlock = null;
        BlockPos nmsBlockPos = null;
        if (nmsHitResult.getType() == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockHitResult = (BlockRayTraceResult) nmsHitResult;
            hitBlockFace = CraftBlock.notchToBlockFace(blockHitResult.getDirection());
            nmsBlockPos = blockHitResult.getBlockPos();
        }
        if (nmsBlockPos != null && world != null) {
            hitBlock = world.getBlockAt(nmsBlockPos.getX(), nmsBlockPos.getY(), nmsBlockPos.getZ());
        }
        return new RayTraceResult(hitPosition, hitBlock, hitBlockFace);
    }
}
