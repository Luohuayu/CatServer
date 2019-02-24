package org.bukkit.craftbukkit.generator;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.structure.MapGenStronghold;
import org.bukkit.ChunkSnapshot;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.block.CraftBlock;

public class CustomChunkGenerator extends InternalChunkGenerator {
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final Random random;
    private final MapGenStronghold strongholdGen = new MapGenStronghold();

    private static class CustomBiomeGrid implements BiomeGrid {
        net.minecraft.world.biome.Biome[] biome;

        @Override
        public Biome getBiome(int x, int z) {
            return CraftBlock.biomeBaseToBiome(biome[(z << 4) | x]);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
           biome[(z << 4) | x] = CraftBlock.biomeToBiomeBase(bio);
        }
    }

    public CustomChunkGenerator(World world, long seed, ChunkGenerator generator) {
        this.world = (WorldServer) world;
        this.generator = generator;

        this.random = new Random(seed);
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        Chunk chunk;

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid();
        biomegrid.biome = new net.minecraft.world.biome.Biome[256];
        world.getBiomeProvider().getBiomes(biomegrid.biome, x << 4, z << 4, 16, 16);

        // Try ChunkData method (1.8+)
        CraftChunkData data = (CraftChunkData) generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
        if (data != null) {
            char[][] sections = data.getRawChunkData();
            chunk = new Chunk(this.world, x, z);

            ExtendedBlockStorage[] csect = chunk.getBlockStorageArray();
            int scnt = Math.min(csect.length, sections.length);
            
            // Loop through returned sections
            for (int sec = 0; sec < scnt; sec++) {
                if(sections[sec] == null) {
                    continue;
                }
                char[] section = sections[sec];
                char emptyTest = 0;
                for (int i = 0; i < 4096; i++) {
                    // Filter invalid block id & data values.
                    if (Block.BLOCK_STATE_IDS.getByValue(section[i]) == null) {
                        section[i] = 0;
                    }
                    emptyTest |= section[i];
                }
                // Build chunk section
                if (emptyTest != 0) {
                    csect[sec] = new ExtendedBlockStorage(sec << 4, true, section);
                }
            }
        }
        else {
            // Try extended block method (1.2+)
            short[][] xbtypes = generator.generateExtBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
            if (xbtypes != null) {
                chunk = new Chunk(this.world, x, z);

                ExtendedBlockStorage[] csect = chunk.getBlockStorageArray();
                int scnt = Math.min(csect.length, xbtypes.length);
                
                // Loop through returned sections
                for (int sec = 0; sec < scnt; sec++) {
                    if (xbtypes[sec] == null) {
                        continue;
                    }
                    char[] secBlkID = new char[4096]; // Allocate blk ID bytes
                    short[] bdata = xbtypes[sec];
                    for (int i = 0; i < bdata.length; i++) {
                        Block b = Block.getBlockById(bdata[i]);
                        secBlkID[i] = (char) Block.BLOCK_STATE_IDS.get(b.getDefaultState());
                    }
                    // Build chunk section
                    csect[sec] = new ExtendedBlockStorage(sec << 4, true, secBlkID);
                }
            }
            else { // Else check for byte-per-block section data
                byte[][] btypes = generator.generateBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
                
                if (btypes != null) {
                    chunk = new Chunk(this.world, x, z);

                    ExtendedBlockStorage[] csect = chunk.getBlockStorageArray();
                    int scnt = Math.min(csect.length, btypes.length);
                    
                    for (int sec = 0; sec < scnt; sec++) {
                        if (btypes[sec] == null) {
                            continue;
                        }
                        
                        char[] secBlkID = new char[4096]; // Allocate block ID bytes
                        for (int i = 0; i < secBlkID.length; i++) {
                            Block b = Block.getBlockById(btypes[sec][i] & 0xFF);
                            secBlkID[i] = (char) Block.BLOCK_STATE_IDS.get(b.getDefaultState());
                        }
                        csect[sec] = new ExtendedBlockStorage(sec << 4, true, secBlkID);
                    }
                }
                else { // Else, fall back to pre 1.2 method
                    @SuppressWarnings("deprecation")
                            byte[] types = generator.generate(this.world.getWorld(), this.random, x, z);
                    int ydim = types.length / 256;
                    int scnt = ydim / 16;
                    
                    chunk = new Chunk(this.world, x, z); // Create empty chunk
                    
                    ExtendedBlockStorage[] csect = chunk.getBlockStorageArray();
                    
                    scnt = Math.min(scnt, csect.length);
                    // Loop through sections
                    for (int sec = 0; sec < scnt; sec++) {
                        char[] csbytes = null; // Add sections when needed
                        
                        for (int cy = 0; cy < 16; cy++) {
                            int cyoff = cy | (sec << 4);
                            
                            for (int cx = 0; cx < 16; cx++) {
                                int cxyoff = (cx * ydim * 16) + cyoff;
                                
                                for (int cz = 0; cz < 16; cz++) {
                                    byte blk = types[cxyoff + (cz * ydim)];
                                    
                                    if (blk != 0) { // If non-empty
                                        if (csbytes == null) { // If no section yet, get one
                                            csbytes = new char[16*16*16];
                                        }
                                        
                                        Block b = Block.getBlockById(blk & 0xFF);
                                        csbytes[(cy << 8) | (cz << 4) | cx] = (char) Block.BLOCK_STATE_IDS.get(b.getDefaultState());
                                    }
                                }
                            }
                        }
                        // If section built, finish prepping its state
                        if (csbytes != null) {
                            ExtendedBlockStorage cs = csect[sec] = new ExtendedBlockStorage(sec << 4, true, csbytes);
                            cs.recalculateRefCounts();
                        }
                    }
                }
            }
        }
        // Set biome grid
        byte[] biomeIndex = chunk.getBiomeArray();
        for (int i = 0; i < biomeIndex.length; i++) {
            biomeIndex[i] = (byte) (net.minecraft.world.biome.Biome.REGISTRY.getIDForObject(biomegrid.biome[i]) & 0xFF); // PAIL : rename
        }
        // Initialize lighting
        chunk.generateSkylightMap();

        return chunk;
    }

    @Override
    public boolean generateStructures(Chunk chunk, int i, int i1) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        return generator.generate(world, random, x, z);
    }

    @Override
    public byte[][] generateBlockSections(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
        return generator.generateBlockSections(world, random, x, z, biomes);
    }

    @Override
    public short[][] generateExtBlockSections(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
        return generator.generateExtBlockSections(world, random, x, z, biomes);
    }

    public Chunk getChunkAt(int x, int z) {
        return generateChunk(x, z);
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return generator.canSpawn(world, x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return generator.getDefaultPopulators(world);
    }

    @Override
    public List<net.minecraft.world.biome.Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos position) {
        net.minecraft.world.biome.Biome biomebase = world.getBiome(position);

        return biomebase == null ? null : biomebase.getSpawnableList(type);
    }

    @Override
    public boolean isInsideStructure(World world, String type, BlockPos position) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.isInsideStructure(position) : false;
    }

    @Override
    public BlockPos getNearestStructurePos(World world, String type, BlockPos position, boolean flag) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.getNearestStructurePos(world, position, flag) : null;
    }

    @Override
    public void populate(int i, int j) {}

    @Override
    public void recreateStructures(Chunk chunk, int i, int j) {
        strongholdGen.generate(this.world, i, j, null);
    }
}
