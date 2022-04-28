package org.bukkit.craftbukkit.v1_16_R3.util;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EmptyTickList;
import net.minecraft.world.ITickList;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import org.jetbrains.annotations.Nullable;

public class DummyGeneratorAccess implements IWorld {

    public static final IWorld INSTANCE = new DummyGeneratorAccess();

    @Override
    public ITickList<Block> getBlockTicks() {
        return EmptyTickList.empty();
    }

    @Override
    public ITickList<Fluid> getLiquidTicks() {
        return EmptyTickList.empty();
    }

    @Override
    public IWorldInfo getLevelData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractChunkProvider getChunkSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Random getRandom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playSound(@Nullable PlayerEntity player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void levelEvent(@Nullable PlayerEntity player, int type, BlockPos pos, int data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServerWorld getMinecraftWorld() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DynamicRegistries registryAccess() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<? extends T> clazz, AxisAlignedBB aabb, @Nullable Predicate<? super T> filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<? extends PlayerEntity> players() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nullable
    @Override
    public IChunk getChunk(int x, int z, ChunkStatus requiredStatus, boolean nonnull) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHeight(Heightmap.Type heightmapType, int x, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSkyDarken() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeManager getBiomeManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Biome getUncachedNoiseBiome(int x, int y, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isClientSide() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSeaLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DimensionType dimensionType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TileEntity getBlockEntity(BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isStateAtPosition(BlockPos bp, Predicate<BlockState> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags, int recursionLeft) {
        return false;
    }

    @Override
    public boolean removeBlock(BlockPos blockposition, boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getShade(Direction p_230487_1_, boolean p_230487_2_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldLightManager getLightEngine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, @Nullable Entity entity, int recursionLeft) {
        return false;
    }
}
