// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.generator;

import org.bukkit.craftbukkit.block.CraftBlock;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import org.bukkit.generator.BlockPopulator;
import java.util.List;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStronghold;
import java.util.Random;
import net.minecraft.world.WorldServer;
import org.bukkit.generator.ChunkGenerator;

public class CustomChunkGenerator extends InternalChunkGenerator
{
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final Random random;
    private final MapGenStronghold strongholdGen;
    
    public CustomChunkGenerator(final World world, final long seed, final ChunkGenerator generator) {
        this.strongholdGen = new MapGenStronghold();
        this.world = (WorldServer)world;
        this.generator = generator;
        this.random = new Random(seed);
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        this.random.setSeed(x * 341873128712L + z * 132897987541L);
        final CustomBiomeGrid biomegrid = new CustomBiomeGrid();
        biomegrid.biome = new Biome[256];
        this.world.getBiomeProvider().getBiomesForGeneration(biomegrid.biome, x << 4, z << 4, 16, 16);
        final CraftChunkData data = (CraftChunkData)this.generator.generateChunkData(this.world.getWorld(), this.random, x, z, biomegrid);
        Chunk chunk;
        if (data != null) {
            final char[][] sections = data.getRawChunkData();
            chunk = new Chunk(this.world, x, z);
            final ExtendedBlockStorage[] csect = chunk.getBlockStorageArray();
            for (int scnt = Math.min(csect.length, sections.length), sec = 0; sec < scnt; ++sec) {
                if (sections[sec] != null) {
                    final char[] section = sections[sec];
                    char emptyTest = '\0';
                    for (int i = 0; i < 4096; ++i) {
                        if (Block.BLOCK_STATE_IDS.getByValue(section[i]) == null) {
                            section[i] = '\0';
                        }
                        emptyTest |= section[i];
                    }
                    if (emptyTest != '\0') {
                        csect[sec] = new ExtendedBlockStorage(sec << 4, true, section);
                    }
                }
            }
        }
        else {
            final short[][] xbtypes = this.generator.generateExtBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
            if (xbtypes != null) {
                chunk = new Chunk(this.world, x, z);
                final ExtendedBlockStorage[] csect = chunk.getBlockStorageArray();
                for (int scnt = Math.min(csect.length, xbtypes.length), sec = 0; sec < scnt; ++sec) {
                    if (xbtypes[sec] != null) {
                        final char[] secBlkID = new char[4096];
                        final short[] bdata = xbtypes[sec];
                        for (int i = 0; i < bdata.length; ++i) {
                            final Block b = Block.getBlockById(bdata[i]);
                            secBlkID[i] = (char)Block.BLOCK_STATE_IDS.get(b.getDefaultState());
                        }
                        csect[sec] = new ExtendedBlockStorage(sec << 4, true, secBlkID);
                    }
                }
            }
            else {
                final byte[][] btypes = this.generator.generateBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
                if (btypes != null) {
                    chunk = new Chunk(this.world, x, z);
                    final ExtendedBlockStorage[] csect2 = chunk.getBlockStorageArray();
                    for (int scnt2 = Math.min(csect2.length, btypes.length), sec2 = 0; sec2 < scnt2; ++sec2) {
                        if (btypes[sec2] != null) {
                            final char[] secBlkID2 = new char[4096];
                            for (int i = 0; i < secBlkID2.length; ++i) {
                                final Block b = Block.getBlockById(btypes[sec2][i] & 0xFF);
                                secBlkID2[i] = (char)Block.BLOCK_STATE_IDS.get(b.getDefaultState());
                            }
                            csect2[sec2] = new ExtendedBlockStorage(sec2 << 4, true, secBlkID2);
                        }
                    }
                }
                else {
                    final byte[] types = this.generator.generate(this.world.getWorld(), this.random, x, z);
                    final int ydim = types.length / 256;
                    int scnt3 = ydim / 16;
                    chunk = new Chunk(this.world, x, z);
                    final ExtendedBlockStorage[] csect3 = chunk.getBlockStorageArray();
                    scnt3 = Math.min(scnt3, csect3.length);
                    for (int sec3 = 0; sec3 < scnt3; ++sec3) {
                        char[] csbytes = null;
                        for (int cy = 0; cy < 16; ++cy) {
                            final int cyoff = cy | sec3 << 4;
                            for (int cx = 0; cx < 16; ++cx) {
                                final int cxyoff = cx * ydim * 16 + cyoff;
                                for (int cz = 0; cz < 16; ++cz) {
                                    final byte blk = types[cxyoff + cz * ydim];
                                    if (blk != 0) {
                                        if (csbytes == null) {
                                            csbytes = new char[4096];
                                        }
                                        final Block b2 = Block.getBlockById(blk & 0xFF);
                                        csbytes[cy << 8 | cz << 4 | cx] = (char)Block.BLOCK_STATE_IDS.get(b2.getDefaultState());
                                    }
                                }
                            }
                        }
                        if (csbytes != null) {
                            final ExtendedBlockStorage[] array = csect3;
                            final int n = sec3;
                            final ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(sec3 << 4, true, csbytes);
                            array[n] = extendedBlockStorage;
                            final ExtendedBlockStorage cs = extendedBlockStorage;
                            cs.removeInvalidBlocks();
                        }
                    }
                }
            }
        }
        final byte[] biomeIndex = chunk.getBiomeArray();
        for (int j = 0; j < biomeIndex.length; ++j) {
            biomeIndex[j] = (byte)(Biome.REGISTRY.getIDForObject(biomegrid.biome[j]) & 0xFF);
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    @Override
    public boolean generateStructures(final Chunk chunk, final int i, final int i1) {
        return false;
    }
    
    @Override
    public byte[] generate(final org.bukkit.World world, final Random random, final int x, final int z) {
        return this.generator.generate(world, random, x, z);
    }
    
    @Override
    public byte[][] generateBlockSections(final org.bukkit.World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        return this.generator.generateBlockSections(world, random, x, z, biomes);
    }
    
    @Override
    public short[][] generateExtBlockSections(final org.bukkit.World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        return this.generator.generateExtBlockSections(world, random, x, z, biomes);
    }
    
    public Chunk getChunkAt(final int x, final int z) {
        return this.provideChunk(x, z);
    }
    
    @Override
    public boolean canSpawn(final org.bukkit.World world, final int x, final int z) {
        return this.generator.canSpawn(world, x, z);
    }
    
    @Override
    public List<BlockPopulator> getDefaultPopulators(final org.bukkit.World world) {
        return this.generator.getDefaultPopulators(world);
    }
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType type, final BlockPos position) {
        final Biome biomebase = this.world.getBiome(position);
        return (biomebase == null) ? null : biomebase.getSpawnableList(type);
    }
    
    @Override
    public BlockPos getStrongholdGen(final World world, final String type, final BlockPos position) {
        return ("Stronghold".equals(type) && this.strongholdGen != null) ? this.strongholdGen.getClosestStrongholdPos(world, position) : null;
    }
    
    @Override
    public void populate(final int i, final int j) {
    }
    
    @Override
    public void recreateStructures(final Chunk chunk, final int i, final int j) {
        this.strongholdGen.generate(this.world, i, j, null);
    }
    
    private static class CustomBiomeGrid implements BiomeGrid
    {
        Biome[] biome;
        
        @Override
        public org.bukkit.block.Biome getBiome(final int x, final int z) {
            return CraftBlock.biomeBaseToBiome(this.biome[z << 4 | x]);
        }
        
        @Override
        public void setBiome(final int x, final int z, final org.bukkit.block.Biome bio) {
            this.biome[z << 4 | x] = CraftBlock.biomeToBiomeBase(bio);
        }
    }
}
