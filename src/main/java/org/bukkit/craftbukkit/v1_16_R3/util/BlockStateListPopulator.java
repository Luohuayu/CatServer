package org.bukkit.craftbukkit.v1_16_R3.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockState;

public class BlockStateListPopulator extends DummyGeneratorAccess {
    private final World world;
    private final LinkedHashMap<BlockPos, CraftBlockState> list;

    public BlockStateListPopulator(World world) {
        this(world, new LinkedHashMap<>());
    }

    public BlockStateListPopulator(World world, LinkedHashMap<BlockPos, CraftBlockState> list) {
        this.world = world;
        this.list = list;
    }

    @Override
    public net.minecraft.block.BlockState getBlockState(BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle() : world.getBlockState(bp);
    }

    @Override
    public FluidState getFluidState(BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle().getFluidState() : world.getFluidState(bp);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState newState, int flags) {
        CraftBlockState state = CraftBlockState.getBlockState(world, pos, flags);
        state.setData(newState);
        list.remove(pos);
        list.put(pos.immutable(), state);
        return true;
    }

    public void updateList() {
        for (org.bukkit.block.BlockState state : list.values()) {
            state.update(true);
        }
    }

    public Set<BlockPos> getBlocks() {
        return list.keySet();
    }

    public List<CraftBlockState> getList() {
        return new ArrayList<>(list.values());
    }

    public World getWorld() {
        return world;
    }
}
