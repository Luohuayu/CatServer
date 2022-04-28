package org.bukkit.craftbukkit.v1_16_R3.block;

import java.util.Random;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.bukkit.Material;
import org.bukkit.block.Block;

public final class CapturedBlockState extends CraftBlockState {

    private final boolean treeBlock;

    public CapturedBlockState(Block block, int flag, boolean treeBlock) {
        super(block, flag);

        this.treeBlock = treeBlock;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        // SPIGOT-5537: Horrible hack to manually add bees given World.captureTreeGeneration does not support tiles
        if (this.treeBlock && getType() == Material.BEE_NEST) {
            IWorld generatoraccess = this.world.getHandle();
            BlockPos blockposition1 = this.getPosition();
            Random random = generatoraccess.getRandom();

            // Begin copied block from WorldGenFeatureTreeBeehive
            TileEntity tileentity = generatoraccess.getBlockEntity(blockposition1);

            if (tileentity instanceof BeehiveTileEntity) {
                BeehiveTileEntity tileentitybeehive = (BeehiveTileEntity) tileentity;
                int j = 2 + random.nextInt(2);

                for (int k = 0; k < j; ++k) {
                    BeeEntity entitybee = new BeeEntity(EntityType.BEE, generatoraccess.getMinecraftWorld());

                    tileentitybeehive.addOccupantWithPresetTicks(entitybee, false, random.nextInt(599));
                }
            }
            // End copied block
        }

        return result;
    }

    public static CapturedBlockState getBlockState(World world, BlockPos pos, int flag) {
        return new CapturedBlockState(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), flag, false);
    }

    public static CapturedBlockState getTreeBlockState(World world, BlockPos pos, int flag) {
        return new CapturedBlockState(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), flag, true);
    }
}
