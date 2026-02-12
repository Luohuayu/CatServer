package org.bukkit.craftbukkit.v1_20_R1.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlockState;

public class BlockStateListPopulator extends DummyGeneratorAccess {
    private final LevelAccessor world;
    private final Map<BlockPos, net.minecraft.world.level.block.state.BlockState> dataMap = new HashMap<>();
    private final Map<BlockPos, BlockEntity> entityMap = new HashMap<>();
    private final LinkedHashMap<BlockPos, CraftBlockState> list;

    public BlockStateListPopulator(LevelAccessor world) {
        this(world, new LinkedHashMap<>());
    }

    private BlockStateListPopulator(LevelAccessor world, LinkedHashMap<BlockPos, CraftBlockState> list) {
        this.world = world;
        this.list = list;
    }

    @Override
    public net.minecraft.world.level.block.state.BlockState getBlockState(BlockPos bp) {
        net.minecraft.world.level.block.state.BlockState blockData = dataMap.get(bp);
        return (blockData != null) ? blockData : world.getBlockState(bp);
    }

    @Override
    public FluidState getFluidState(BlockPos bp) {
        net.minecraft.world.level.block.state.BlockState blockData = dataMap.get(bp);
        return (blockData != null) ? blockData.getFluidState() : world.getFluidState(bp);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos blockposition) {
        // The contains is important to check for null values
        if (entityMap.containsKey(blockposition)) {
            return entityMap.get(blockposition);
        }

        return world.getBlockEntity(blockposition);
    }

    @Override
    public boolean setBlock(BlockPos position, net.minecraft.world.level.block.state.BlockState data, int flag) {
        position = position.immutable();
        // remove first to keep insertion order
        list.remove(position);

        dataMap.put(position, data);
        if (data.hasBlockEntity()) {
            entityMap.put(position, ((EntityBlock) data.getBlock()).newBlockEntity(position, data));
        } else {
            entityMap.put(position, null);
        }

        // use 'this' to ensure that the block state is the correct TileState
        CraftBlockState state = (CraftBlockState) CraftBlock.at(this, position).getState();
        state.setFlag(flag);
        // set world handle to ensure that updated calls are done to the world and not to this populator
        state.setWorldHandle(world);
        list.put(position, state);
        return true;
    }

    @Override
    public ServerLevel getMinecraftWorld() {
        return world.getMinecraftWorld();
    }

    public void refreshTiles() {
        for (CraftBlockState state : list.values()) {
            if (state instanceof CraftBlockEntityState) {
                ((CraftBlockEntityState<?>) state).refreshSnapshot();
            }
        }
    }

    public void updateList() {
        for (BlockState state : list.values()) {
            state.update(true);
        }
    }

    public Set<BlockPos> getBlocks() {
        return list.keySet();
    }

    public List<CraftBlockState> getList() {
        return new ArrayList<>(list.values());
    }

    public LevelAccessor getWorld() {
        return world;
    }

    // For tree generation
    @Override
    public int getMinBuildHeight() {
        return getWorld().getMinBuildHeight();
    }

    @Override
    public int getHeight() {
        return getWorld().getHeight();
    }

    @Override
    public boolean isStateAtPosition(BlockPos blockposition, Predicate<net.minecraft.world.level.block.state.BlockState> predicate) {
        return predicate.test(getBlockState(blockposition));
    }

    @Override
    public boolean isFluidAtPosition(BlockPos bp, Predicate<FluidState> prdct) {
        return world.isFluidAtPosition(bp, prdct);
    }

    @Override
    public DimensionType dimensionType() {
        return world.dimensionType();
    }

    @Override
    public RegistryAccess registryAccess() {
        return world.registryAccess();
    }

    // Needed when a tree generates in water
    @Override
    public LevelData getLevelData() {
        return world.getLevelData();
    }

    @Override
    public long nextSubTickCount() {
        return world.nextSubTickCount();
    }
}
