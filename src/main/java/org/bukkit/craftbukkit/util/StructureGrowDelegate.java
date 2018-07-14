// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.Iterator;
import org.bukkit.material.MaterialData;
import java.util.ArrayList;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import java.util.List;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.BlockChangeDelegate;

public class StructureGrowDelegate implements BlockChangeDelegate
{
    private final CraftWorld world;
    private final List<BlockState> blocks;
    
    public StructureGrowDelegate(final World world) {
        this.blocks = new ArrayList<BlockState>();
        this.world = world.getWorld();
    }
    
    @Override
    public boolean setRawTypeId(final int x, final int y, final int z, final int type) {
        return this.setRawTypeIdAndData(x, y, z, type, 0);
    }
    
    @Override
    public boolean setRawTypeIdAndData(final int x, final int y, final int z, final int type, final int data) {
        final BlockState state = this.world.getBlockAt(x, y, z).getState();
        state.setTypeId(type);
        state.setData(new MaterialData(type, (byte)data));
        this.blocks.add(state);
        return true;
    }
    
    @Override
    public boolean setTypeId(final int x, final int y, final int z, final int typeId) {
        return this.setRawTypeId(x, y, z, typeId);
    }
    
    @Override
    public boolean setTypeIdAndData(final int x, final int y, final int z, final int typeId, final int data) {
        return this.setRawTypeIdAndData(x, y, z, typeId, data);
    }
    
    @Override
    public int getTypeId(final int x, final int y, final int z) {
        for (final BlockState state : this.blocks) {
            if (state.getX() == x && state.getY() == y && state.getZ() == z) {
                return state.getTypeId();
            }
        }
        return this.world.getBlockTypeIdAt(x, y, z);
    }
    
    @Override
    public int getHeight() {
        return this.world.getMaxHeight();
    }
    
    public List<BlockState> getBlocks() {
        return this.blocks;
    }
    
    @Override
    public boolean isEmpty(final int x, final int y, final int z) {
        for (final BlockState state : this.blocks) {
            if (state.getX() == x && state.getY() == y && state.getZ() == z) {
                return Block.getBlockById(state.getTypeId()) == Blocks.AIR;
            }
        }
        return this.world.getBlockAt(x, y, z).isEmpty();
    }
}
