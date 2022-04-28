package org.bukkit.craftbukkit.v1_16_R3.generator;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage.Carving;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator {

    private final net.minecraft.world.gen.ChunkGenerator delegate;
    private final ChunkGenerator generator;
    private final ServerWorld world;
    private final Random random = new Random();

    private class CustomBiomeGrid implements BiomeGrid {

        private final BiomeContainer biome; // SPIGOT-5529: stored in 4x4 grid

        public CustomBiomeGrid(BiomeContainer biome) {
            this.biome = biome;
        }

        @Override
        public Biome getBiome(int x, int z) {
            return getBiome(x, 0, z);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
            for (int y = 0; y < world.getWorld().getMaxHeight(); y += 4) {
                setBiome(x, y, z, bio);
            }
        }

        @Override
        public Biome getBiome(int x, int y, int z) {
            return CraftBlock.biomeBaseToBiome((Registry<net.minecraft.world.biome.Biome>) biome.biomeRegistry, biome.getNoiseBiome(x >> 2, y >> 2, z >> 2));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome bio) {
            Preconditions.checkArgument(bio != Biome.CUSTOM, "Cannot set the biome to %s", bio);
            biome.setBiome(x >> 2, y >> 2, z >> 2, CraftBlock.biomeToBiomeBase((Registry<net.minecraft.world.biome.Biome>) biome.biomeRegistry, bio));
        }
    }

    public CustomChunkGenerator(ServerWorld world, net.minecraft.world.gen.ChunkGenerator delegate, ChunkGenerator generator) {
        super(delegate.getBiomeSource(), delegate.getSettings());

        this.world = world;
        this.delegate = delegate;
        this.generator = generator;
    }

    @Override
    public void createBiomes(Registry<net.minecraft.world.biome.Biome> p_242706_1_, IChunk p_242706_2_) {
        // Don't allow the server to override any custom biomes that have been set
    }

    @Override
    public BiomeProvider getBiomeSource() {
        return delegate.getBiomeSource();
    }

    @Override
    public void createReferences(ISeedReader p_235953_1_, StructureManager p_235953_2_, IChunk p_235953_3_) {
        delegate.createReferences(p_235953_1_, p_235953_2_, p_235953_3_);
    }

    @Override
    public int getSeaLevel() {
        return delegate.getSeaLevel();
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {
        // Call the bukkit ChunkGenerator before structure generation so correct biome information is available.
        int x = p_225551_2_.getPos().x;
        int z = p_225551_2_.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid(new BiomeContainer(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), p_225551_2_.getPos(), this.getBiomeSource()));

        ChunkData data;
        if (generator.isParallelCapable()) {
            data = generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
        } else {
            synchronized (this) {
                data = generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
            }
        }

        Preconditions.checkArgument(data instanceof CraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        CraftChunkData craftData = (CraftChunkData) data;
        ChunkSection[] sections = craftData.getRawChunkData();

        ChunkSection[] csect = p_225551_2_.getSections();
        int scnt = Math.min(csect.length, sections.length);

        // Loop through returned sections
        for (int sec = 0; sec < scnt; sec++) {
            if (sections[sec] == null) {
                continue;
            }
            ChunkSection section = sections[sec];

            csect[sec] = section;
        }

        // Set biome grid
        ((ChunkPrimer) p_225551_2_).setBiomes(biomegrid.biome);

        if (craftData.getTiles() != null) {
            for (BlockPos pos : craftData.getTiles()) {
                int tx = pos.getX();
                int ty = pos.getY();
                int tz = pos.getZ();
                Block block = craftData.getTypeId(tx, ty, tz).getBlock();

                if (block.isEntityBlock()) {
                    TileEntity tile = ((ITileEntityProvider) block).newBlockEntity(world);
                    p_225551_2_.setBlockEntity(new BlockPos((x << 4) + tx, ty, (z << 4) + tz), tile);
                }
            }
        }
    }

    @Override
    public void createStructures(DynamicRegistries p_242707_1_, StructureManager p_242707_2_, IChunk p_242707_3_, TemplateManager p_242707_4_, long p_242707_5_) {
        if (generator.shouldGenerateStructures()) {
            // Still need a way of getting the biome of this chunk to pass to createStructures
            // Using default biomes for now.
            delegate.createStructures(p_242707_1_, p_242707_2_, p_242707_3_, p_242707_4_, p_242707_5_);
        }

    }

    @Override
    public void applyCarvers(long p_230350_1_, BiomeManager p_230350_3_, IChunk p_230350_4_, Carving p_230350_5_) {
        if (generator.shouldGenerateCaves()) {
            delegate.applyCarvers(p_230350_1_, p_230350_3_, p_230350_4_, p_230350_5_);
        }
    }

    @Override
    public void fillFromNoise(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {
		// Disable vanilla generation
    }

    @Override
    public int getBaseHeight(int p_222529_1_, int p_222529_2_, Type heightmapType) {
        return delegate.getBaseHeight(p_222529_1_, p_222529_2_, heightmapType);
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion p_230351_1_, StructureManager p_230351_2_) {
        if (generator.shouldGenerateDecorations()) {
            delegate.applyBiomeDecoration(p_230351_1_, p_230351_2_);
        }
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion p_230354_1_) {
        if (generator.shouldGenerateMobs()) {
            delegate.spawnOriginalMobs(p_230354_1_);
        }
    }

    @Override
    public int getSpawnHeight() {
        return delegate.getSpawnHeight();
    }

    @Override
    public int getGenDepth() {
        return delegate.getGenDepth();
    }

    @Override
    public IBlockReader getBaseColumn(int p_230348_1_, int p_230348_2_) {
        return delegate.getBaseColumn(p_230348_1_, p_230348_2_);
    }

    @Override
    protected Codec<? extends net.minecraft.world.gen.ChunkGenerator> codec() {
        throw new UnsupportedOperationException("Cannot serialize CustomChunkGenerator");
    }

    @Override
    public net.minecraft.world.gen.ChunkGenerator withSeed(long p_230349_1_) {
        return null;
    }
}
