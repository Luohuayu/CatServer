// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.generator;

import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import org.bukkit.generator.BlockPopulator;
import java.util.List;
import org.bukkit.craftbukkit.CraftWorld;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;

public class NormalChunkGenerator extends InternalChunkGenerator
{
    private final IChunkGenerator generator;
    
    public NormalChunkGenerator(final World world, final long seed) {
        this.generator = world.provider.createChunkGenerator();
    }
    
    @Override
    public byte[] generate(final org.bukkit.World world, final Random random, final int x, final int z) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public boolean canSpawn(final org.bukkit.World world, final int x, final int z) {
        return ((CraftWorld)world).getHandle().provider.canCoordinateBeSpawn(x, z);
    }
    
    @Override
    public List<BlockPopulator> getDefaultPopulators(final org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }
    
    @Override
    public Chunk provideChunk(final int i, final int i1) {
        return this.generator.provideChunk(i, i1);
    }
    
    @Override
    public void populate(final int i, final int i1) {
        this.generator.populate(i, i1);
    }
    
    @Override
    public boolean generateStructures(final Chunk chunk, final int i, final int i1) {
        return this.generator.generateStructures(chunk, i, i1);
    }
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType enumCreatureType, final BlockPos blockPosition) {
        return this.generator.getPossibleCreatures(enumCreatureType, blockPosition);
    }
    
    @Override
    public BlockPos getStrongholdGen(final World world, final String s, final BlockPos blockPosition) {
        return this.generator.getStrongholdGen(world, s, blockPosition);
    }
    
    @Override
    public void recreateStructures(final Chunk chunk, final int i, final int i1) {
        this.generator.recreateStructures(chunk, i, i1);
    }
}
