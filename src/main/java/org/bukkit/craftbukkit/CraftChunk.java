// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.NibbleArray;
import org.bukkit.ChunkSnapshot;
import net.minecraft.util.math.BlockPos;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.block.Block;
import org.bukkit.World;
import java.util.Arrays;
import net.minecraft.world.WorldServer;
import java.lang.ref.WeakReference;
import org.bukkit.Chunk;

public class CraftChunk implements Chunk
{
    private WeakReference<net.minecraft.world.chunk.Chunk> weakChunk;
    private final WorldServer worldServer;
    private final int x;
    private final int z;
    private static final byte[] emptyData;
    private static final short[] emptyBlockIDs;
    private static final byte[] emptySkyLight;
    
    static {
        emptyData = new byte[2048];
        emptyBlockIDs = new short[4096];
        Arrays.fill(emptySkyLight = new byte[2048], (byte)(-1));
    }
    
    public CraftChunk(final net.minecraft.world.chunk.Chunk chunk) {
        this.weakChunk = new WeakReference<net.minecraft.world.chunk.Chunk>(chunk);
        this.worldServer = (WorldServer)this.getHandle().worldObj;
        this.x = this.getHandle().xPosition;
        this.z = this.getHandle().zPosition;
    }
    
    @Override
    public World getWorld() {
        return this.worldServer.getWorld();
    }
    
    public CraftWorld getCraftWorld() {
        return (CraftWorld)this.getWorld();
    }
    
    public net.minecraft.world.chunk.Chunk getHandle() {
        net.minecraft.world.chunk.Chunk c = this.weakChunk.get();
        if (c == null) {
            c = this.worldServer.getChunkFromChunkCoords(this.x, this.z);
            this.weakChunk = new WeakReference<net.minecraft.world.chunk.Chunk>(c);
        }
        return c;
    }
    
    void breakLink() {
        this.weakChunk.clear();
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public int getZ() {
        return this.z;
    }
    
    @Override
    public String toString() {
        return "CraftChunk{x=" + this.getX() + "z=" + this.getZ() + '}';
    }
    
    @Override
    public Block getBlock(final int x, final int y, final int z) {
        return new CraftBlock(this, this.getX() << 4 | (x & 0xF), y, this.getZ() << 4 | (z & 0xF));
    }
    
    @Override
    public Entity[] getEntities() {
        int count = 0;
        int index = 0;
        final net.minecraft.world.chunk.Chunk chunk = this.getHandle();
        for (int i = 0; i < 16; ++i) {
            count += chunk.entityLists[i].size();
        }
        final Entity[] entities = new Entity[count];
        for (int j = 0; j < 16; ++j) {
            Object[] array;
            for (int length = (array = chunk.entityLists[j].toArray()).length, k = 0; k < length; ++k) {
                final Object obj = array[k];
                if (obj instanceof net.minecraft.entity.Entity) {
                    entities[index++] = ((net.minecraft.entity.Entity)obj).getBukkitEntity();
                }
            }
        }
        return entities;
    }
    
    @Override
    public BlockState[] getTileEntities() {
        int index = 0;
        final net.minecraft.world.chunk.Chunk chunk = this.getHandle();
        final BlockState[] entities = new BlockState[chunk.chunkTileEntityMap.size()];
        Object[] array;
        for (int length = (array = chunk.chunkTileEntityMap.keySet().toArray()).length, i = 0; i < length; ++i) {
            final Object obj = array[i];
            if (obj instanceof BlockPos) {
                final BlockPos position = (BlockPos)obj;
                entities[index++] = this.worldServer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
            }
        }
        return entities;
    }
    
    @Override
    public boolean isLoaded() {
        return this.getWorld().isChunkLoaded(this);
    }
    
    @Override
    public boolean load() {
        return this.getWorld().loadChunk(this.getX(), this.getZ(), true);
    }
    
    @Override
    public boolean load(final boolean generate) {
        return this.getWorld().loadChunk(this.getX(), this.getZ(), generate);
    }
    
    @Override
    public boolean unload() {
        return this.getWorld().unloadChunk(this.getX(), this.getZ());
    }
    
    @Override
    public boolean unload(final boolean save) {
        return this.getWorld().unloadChunk(this.getX(), this.getZ(), save);
    }
    
    @Override
    public boolean unload(final boolean save, final boolean safe) {
        return this.getWorld().unloadChunk(this.getX(), this.getZ(), save, safe);
    }
    
    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return this.getChunkSnapshot(true, false, false);
    }
    
    @Override
    public ChunkSnapshot getChunkSnapshot(final boolean includeMaxBlockY, final boolean includeBiome, final boolean includeBiomeTempRain) {
        final net.minecraft.world.chunk.Chunk chunk = this.getHandle();
        final ExtendedBlockStorage[] cs = chunk.getBlockStorageArray();
        final short[][] sectionBlockIDs = new short[cs.length][];
        final byte[][] sectionBlockData = new byte[cs.length][];
        final byte[][] sectionSkyLights = new byte[cs.length][];
        final byte[][] sectionEmitLights = new byte[cs.length][];
        final boolean[] sectionEmpty = new boolean[cs.length];
        for (int i = 0; i < cs.length; ++i) {
            if (cs[i] == null) {
                sectionBlockIDs[i] = CraftChunk.emptyBlockIDs;
                sectionBlockData[i] = CraftChunk.emptyData;
                sectionSkyLights[i] = CraftChunk.emptySkyLight;
                sectionEmitLights[i] = CraftChunk.emptyData;
                sectionEmpty[i] = true;
            }
            else {
                final short[] blockids = new short[4096];
                final byte[] rawIds = new byte[4096];
                final NibbleArray data = new NibbleArray();
                cs[i].getData().getDataForNBT(rawIds, data);
                sectionBlockData[i] = data.getData();
                for (int j = 0; j < 4096; ++j) {
                    blockids[j] = (short)(rawIds[j] & 0xFF);
                }
                sectionBlockIDs[i] = blockids;
                if (cs[i].getSkylightArray() == null) {
                    sectionSkyLights[i] = CraftChunk.emptyData;
                }
                else {
                    sectionSkyLights[i] = new byte[2048];
                    System.arraycopy(cs[i].getSkylightArray().getData(), 0, sectionSkyLights[i], 0, 2048);
                }
                sectionEmitLights[i] = new byte[2048];
                System.arraycopy(cs[i].getBlocklightArray().getData(), 0, sectionEmitLights[i], 0, 2048);
            }
        }
        int[] hmap = null;
        if (includeMaxBlockY) {
            hmap = new int[256];
            System.arraycopy(chunk.heightMap, 0, hmap, 0, 256);
        }
        Biome[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;
        if (includeBiome || includeBiomeTempRain) {
            final BiomeProvider wcm = chunk.worldObj.getBiomeProvider();
            if (includeBiome) {
                biome = new Biome[256];
                for (int k = 0; k < 256; ++k) {
                    biome[k] = chunk.getBiome(new BlockPos(k & 0xF, 0, k >> 4), wcm);
                }
            }
            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                final float[] dat = getTemperatures(wcm, this.getX() << 4, this.getZ() << 4);
                for (int l = 0; l < 256; ++l) {
                    biomeTemp[l] = dat[l];
                }
            }
        }
        final World world = this.getWorld();
        return new CraftChunkSnapshot(this.getX(), this.getZ(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionBlockData, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, biome, biomeTemp, biomeRain);
    }
    
    public static ChunkSnapshot getEmptyChunkSnapshot(final int x, final int z, final CraftWorld world, final boolean includeBiome, final boolean includeBiomeTempRain) {
        Biome[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;
        if (includeBiome || includeBiomeTempRain) {
            final BiomeProvider wcm = world.getHandle().getBiomeProvider();
            if (includeBiome) {
                biome = new Biome[256];
                for (int i = 0; i < 256; ++i) {
                    biome[i] = world.getHandle().getBiome(new BlockPos((x << 4) + (i & 0xF), 0, (z << 4) + (i >> 4)));
                }
            }
            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                final float[] dat = getTemperatures(wcm, x << 4, z << 4);
                for (int j = 0; j < 256; ++j) {
                    biomeTemp[j] = dat[j];
                }
            }
        }
        final int hSection = world.getMaxHeight() >> 4;
        final short[][] blockIDs = new short[hSection][];
        final byte[][] skyLight = new byte[hSection][];
        final byte[][] emitLight = new byte[hSection][];
        final byte[][] blockData = new byte[hSection][];
        final boolean[] empty = new boolean[hSection];
        for (int k = 0; k < hSection; ++k) {
            blockIDs[k] = CraftChunk.emptyBlockIDs;
            skyLight[k] = CraftChunk.emptySkyLight;
            emitLight[k] = CraftChunk.emptyData;
            blockData[k] = CraftChunk.emptyData;
            empty[k] = true;
        }
        return new CraftChunkSnapshot(x, z, world.getName(), world.getFullTime(), blockIDs, blockData, skyLight, emitLight, empty, new int[256], biome, biomeTemp, biomeRain);
    }
    
    private static float[] getTemperatures(final BiomeProvider chunkmanager, final int chunkX, final int chunkZ) {
        final Biome[] biomes = chunkmanager.getBiomesForGeneration(null, chunkX, chunkZ, 16, 16);
        final float[] temps = new float[biomes.length];
        for (int i = 0; i < biomes.length; ++i) {
            float temp = biomes[i].getTemperature();
            if (temp > 1.0f) {
                temp = 1.0f;
            }
            temps[i] = temp;
        }
        return temps;
    }
}
