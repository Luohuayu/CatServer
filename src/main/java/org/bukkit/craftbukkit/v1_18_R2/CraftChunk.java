package org.bukkit.craftbukkit.v1_18_R2;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public class CraftChunk implements Chunk {
    private WeakReference<net.minecraft.world.level.chunk.LevelChunk> weakChunk;
    private final ServerLevel worldServer;
    private final int x;
    private final int z;
    private static final PalettedContainer<net.minecraft.world.level.block.state.BlockState> emptyBlockIDs = new PalettedContainer<>(net.minecraft.world.level.block.Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES);
    private static final byte[] emptyLight = new byte[2048];

    public CraftChunk(net.minecraft.world.level.chunk.LevelChunk chunk) {
        this.weakChunk = new WeakReference<net.minecraft.world.level.chunk.LevelChunk>(chunk);

        worldServer = (ServerLevel) getHandle().level;
        x = getHandle().getPos().x;
        z = getHandle().getPos().z;
    }

    public CraftChunk(ServerLevel worldServer, int x, int z) {
        this.weakChunk = new WeakReference<>(null);
        this.worldServer = worldServer;
        this.x = x;
        this.z = z;
    }

    @Override
    public World getWorld() {
        return worldServer.getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) getWorld();
    }

    public net.minecraft.world.level.chunk.LevelChunk getHandle() {
        net.minecraft.world.level.chunk.LevelChunk c = weakChunk.get();

        if (c == null) {
            c = worldServer.getChunk(x, z);

            weakChunk = new WeakReference<net.minecraft.world.level.chunk.LevelChunk>(c);
        }

        return c;
    }

    void breakLink() {
        weakChunk.clear();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        validateChunkCoordinates(getHandle().getMinBuildHeight(), getHandle().getMaxBuildHeight(), x, y, z);

        return new CraftBlock(worldServer, new BlockPos((this.x << 4) | x, y, (this.z << 4) | z));
    }

    @Override
    public boolean isEntitiesLoaded() {
        return getCraftWorld().getHandle().entityManager.areEntitiesLoaded(ChunkPos.asLong(x, z)); // PAIL rename isEntitiesLoaded
    }

    @Override
    public Entity[] getEntities() {
        if (!isLoaded()) {
            getWorld().getChunkAt(x, z); // Transient load for this tick
        }

        net.minecraft.world.level.entity.PersistentEntitySectionManager<net.minecraft.world.entity.Entity> entityManager = getCraftWorld().getHandle().entityManager;
        long pair = ChunkPos.asLong(x, z);

        if (entityManager.areEntitiesLoaded(pair)) { // PAIL rename isEntitiesLoaded
            return entityManager.getEntities(new ChunkPos(x, z)).stream()
                    .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                    .filter(Objects::nonNull).toArray(Entity[]::new);
        }

        entityManager.ensureChunkQueuedForLoad(pair); // Start entity loading

        // SPIGOT-6772: Use entity mailbox and re-schedule entities if they get unloaded
        ProcessorMailbox<Runnable> mailbox = ((EntityStorage) entityManager.permanentStorage).entityDeserializerQueue;
        BooleanSupplier supplier = () -> {
            // only execute inbox if our entities are not present
            if (entityManager.areEntitiesLoaded(pair)) {
                return true;
            }

            if (!entityManager.isPending(pair)) {
                // Our entities got unloaded, this should normally not happen.
                entityManager.ensureChunkQueuedForLoad(pair); // Re-start entity loading
            }

            // tick loading inbox, which loads the created entities to the world
            // (if present)
            entityManager.tick();
            // check if our entities are loaded
            return entityManager.areEntitiesLoaded(pair);
        };

        // now we wait until the entities are loaded,
        // the converting from NBT to entity object is done on the main Thread which is why we wait
        while (!supplier.getAsBoolean()) {
            if (mailbox.size() != 0) {
                mailbox.run();
            } else {
                Thread.yield();
                LockSupport.parkNanos("waiting for entity loading", 100000L);
            }
        }

        return entityManager.getEntities(new ChunkPos(x, z)).stream()
                .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                .filter(Objects::nonNull).toArray(Entity[]::new);
    }

    @Override
    public BlockState[] getTileEntities() {
        if (!isLoaded()) {
            getWorld().getChunkAt(x, z); // Transient load for this tick
        }
        int index = 0;
        net.minecraft.world.level.chunk.LevelChunk chunk = getHandle();

        BlockState[] entities = new BlockState[chunk.blockEntities.size()];

        for (Object obj : chunk.blockEntities.keySet().toArray()) {
            if (!(obj instanceof BlockPos)) {
                continue;
            }

            BlockPos position = (BlockPos) obj;
            entities[index++] = worldServer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
        }

        return entities;
    }

    @Override
    public boolean isLoaded() {
        return getWorld().isChunkLoaded(this);
    }

    @Override
    public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
    }

    @Override
    public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
    }

    @Override
    public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
    }

    @Override
    public boolean isSlimeChunk() {
        // 987234911L is deterimined in Slime when seeing if a slime can spawn in a chunk
        return WorldgenRandom.seedSlimeChunk(getX(), getZ(), getWorld().getSeed(), 987234911L).nextInt(10) == 0;
    }

    @Override
    public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
    }

    @Override
    public boolean isForceLoaded() {
        return getWorld().isChunkForceLoaded(getX(), getZ());
    }

    @Override
    public void setForceLoaded(boolean forced) {
        getWorld().setChunkForceLoaded(getX(), getZ(), forced);
    }

    @Override
    public boolean addPluginChunkTicket(Plugin plugin) {
        return getWorld().addPluginChunkTicket(getX(), getZ(), plugin);
    }

    @Override
    public boolean removePluginChunkTicket(Plugin plugin) {
        return getWorld().removePluginChunkTicket(getX(), getZ(), plugin);
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets() {
        return getWorld().getPluginChunkTickets(getX(), getZ());
    }

    @Override
    public long getInhabitedTime() {
        return getHandle().getInhabitedTime();
    }

    @Override
    public void setInhabitedTime(long ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative");

        getHandle().setInhabitedTime(ticks);
    }

    @Override
    public boolean contains(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        Predicate<net.minecraft.world.level.block.state.BlockState> nms = Predicates.equalTo(((CraftBlockData) block).getState());
        for (LevelChunkSection section : getHandle().getSections()) {
            if (section != null && section.getStates().maybeHas(nms)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        net.minecraft.world.level.chunk.LevelChunk chunk = getHandle();

        LevelChunkSection[] cs = chunk.getSections();
        PalettedContainer[] sectionBlockIDs = new PalettedContainer[cs.length];
        byte[][] sectionSkyLights = new byte[cs.length][];
        byte[][] sectionEmitLights = new byte[cs.length][];
        boolean[] sectionEmpty = new boolean[cs.length];
        PalettedContainer<Holder<Biome>>[] biome = (includeBiome || includeBiomeTempRain) ? new PalettedContainer[cs.length] : null;

        net.minecraft.core.Registry<Biome> iregistry = worldServer.registryAccess().registryOrThrow(net.minecraft.core.Registry.BIOME_REGISTRY);
        Codec<PalettedContainer<Holder<Biome>>> biomeCodec = PalettedContainer.codec(iregistry.asHolderIdMap(), iregistry.holderByNameCodec(), PalettedContainer.Strategy.SECTION_BIOMES, iregistry.getHolderOrThrow(Biomes.PLAINS));

        for (int i = 0; i < cs.length; i++) {
            CompoundTag data = new CompoundTag();

            data.put("block_states", ChunkSerializer.BLOCK_STATE_CODEC.encodeStart(NbtOps.INSTANCE, cs[i].getStates()).get().left().get());
            sectionBlockIDs[i] = ChunkSerializer.BLOCK_STATE_CODEC.parse(NbtOps.INSTANCE, data.getCompound("block_states")).get().left().get();

            LevelLightEngine lightengine = chunk.level.getLightEngine();
            DataLayer skyLightArray = lightengine.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(x, i, z));
            if (skyLightArray == null) {
                sectionSkyLights[i] = emptyLight;
            } else {
                sectionSkyLights[i] = new byte[2048];
                System.arraycopy(skyLightArray.getData(), 0, sectionSkyLights[i], 0, 2048);
            }
            DataLayer emitLightArray = lightengine.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(x, i, z));
            if (emitLightArray == null) {
                sectionEmitLights[i] = emptyLight;
            } else {
                sectionEmitLights[i] = new byte[2048];
                System.arraycopy(emitLightArray.getData(), 0, sectionEmitLights[i], 0, 2048);
            }

            if (biome != null) {
                data.put("biomes", biomeCodec.encodeStart(NbtOps.INSTANCE, cs[i].getBiomes()).get().left().get());
                biome[i] = biomeCodec.parse(NbtOps.INSTANCE, data.getCompound("biomes")).get().left().get();
            }
        }

        Heightmap hmap = null;

        if (includeMaxBlockY) {
            hmap = new Heightmap(chunk, Heightmap.Types.MOTION_BLOCKING);
            hmap.setRawData(chunk, Heightmap.Types.MOTION_BLOCKING, chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).getRawData());
        }

        World world = getWorld();
        return new CraftChunkSnapshot(getX(), getZ(), chunk.getMinBuildHeight(), chunk.getMaxBuildHeight(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, iregistry, biome);
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return getHandle().persistentDataContainer;
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        ChunkAccess actual = world.getHandle().getChunk(x, z, ChunkStatus.EMPTY);

        /* Fill with empty data */
        int hSection = actual.getSectionsCount();
        PalettedContainer[] blockIDs = new PalettedContainer[hSection];
        byte[][] skyLight = new byte[hSection][];
        byte[][] emitLight = new byte[hSection][];
        boolean[] empty = new boolean[hSection];
        net.minecraft.core.Registry<Biome> iregistry = world.getHandle().registryAccess().registryOrThrow(net.minecraft.core.Registry.BIOME_REGISTRY);
        PalettedContainer<Holder<Biome>>[] biome = (includeBiome || includeBiomeTempRain) ? new PalettedContainer[hSection] : null;

        for (int i = 0; i < hSection; i++) {
            blockIDs[i] = emptyBlockIDs;
            skyLight[i] = emptyLight;
            emitLight[i] = emptyLight;
            empty[i] = true;

            if (biome != null) {
                biome[i] = new PalettedContainer<>(iregistry.asHolderIdMap(), iregistry.getHolderOrThrow(Biomes.PLAINS), PalettedContainer.Strategy.SECTION_BIOMES);
            }
        }

        return new CraftChunkSnapshot(x, z, world.getMinHeight(), world.getMaxHeight(), world.getName(), world.getFullTime(), blockIDs, skyLight, emitLight, empty, new Heightmap(actual, Heightmap.Types.MOTION_BLOCKING), iregistry, biome);
    }

    static void validateChunkCoordinates(int minY, int maxY, int x, int y, int z) {
        Preconditions.checkArgument(0 <= x && x <= 15, "x out of range (expected 0-15, got %s)", x);
        Preconditions.checkArgument(minY <= y && y <= maxY, "y out of range (expected %s-%s, got %s)", minY, maxY, y);
        Preconditions.checkArgument(0 <= z && z <= 15, "z out of range (expected 0-15, got %s)", z);
    }

    static {
        Arrays.fill(emptyLight, (byte) 0xFF);
    }
}
