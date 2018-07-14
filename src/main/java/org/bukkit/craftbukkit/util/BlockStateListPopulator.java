// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import java.util.ArrayList;
import org.bukkit.block.BlockState;
import java.util.List;
import org.bukkit.World;

public class BlockStateListPopulator
{
    private final World world;
    private final List<BlockState> list;
    
    public BlockStateListPopulator(final World world) {
        this(world, new ArrayList<BlockState>());
    }
    
    public BlockStateListPopulator(final World world, final List<BlockState> list) {
        this.world = world;
        this.list = list;
    }
    
    public void setTypeAndData(final int x, final int y, final int z, final Block block, final int data, final int light) {
        final BlockState state = this.world.getBlockAt(x, y, z).getState();
        state.setTypeId(Block.getIdFromBlock(block));
        state.setRawData((byte)data);
        this.list.add(state);
    }
    
    public void setTypeId(final int x, final int y, final int z, final int type) {
        final BlockState state = this.world.getBlockAt(x, y, z).getState();
        state.setTypeId(type);
        this.list.add(state);
    }
    
    public void setTypeUpdate(final int x, final int y, final int z, final Block block) {
        this.setType(x, y, z, block);
    }
    
    public void setTypeUpdate(final BlockPos position, final IBlockState data) {
        this.setTypeAndData(position.getX(), position.getY(), position.getZ(), data.getBlock(), data.getBlock().getMetaFromState(data), 0);
    }
    
    public void setType(final int x, final int y, final int z, final Block block) {
        final BlockState state = this.world.getBlockAt(x, y, z).getState();
        state.setTypeId(Block.getIdFromBlock(block));
        this.list.add(state);
    }
    
    public void updateList() {
        for (final BlockState state : this.list) {
            state.update(true);
        }
    }
    
    public List<BlockState> getList() {
        return this.list;
    }
    
    public World getWorld() {
        return this.world;
    }
}
