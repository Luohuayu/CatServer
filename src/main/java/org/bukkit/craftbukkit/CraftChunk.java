package org.bukkit.craftbukkit;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.ChunkSnapshot;

public class CraftChunk implements Chunk {
    private WeakReference<net.minecraft.world.chunk.Chunk> weakChunk;
    private final WorldServer worldServer;
    private final int x;
    private final int z;
    private static final byte[] emptyData = new byte[2048];
    private static final short[] emptyBlockIDs = new short[4096];
    private static final byte[] emptySkyLight = new byte[2048];
    private final net.minecraft.world.World nmsWorld;

    public CraftChunk(net.minecraft.world.chunk.Chunk chunk) {
        this.weakChunk = new WeakReference<net.minecraft.world.chunk.Chunk>(chunk);
        nmsWorld = getHandle().getWorld();
        worldServer = nmsWorld instanceof WorldServer ? (WorldServer)nmsWorld : null;
        x = getHandle().x;
        z = getHandle().z;
    }

    public World getWorld() {
        return nmsWorld.getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) getWorld();
    }

    public net.minecraft.world.chunk.Chunk getHandle() {
        net.minecraft.world.chunk.Chunk c = weakChunk.get();

        if (c == null) {
            c = nmsWorld.getChunkFromChunkCoords(x, z);

            weakChunk = new WeakReference<>(c);
        }

        return c;
    }

    void breakLink() {
        weakChunk.clear();
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }

    public Block getBlock(int x, int y, int z) {
        return new CraftBlock(this, (getX() << 4) | (x & 0xF), y, (getZ() << 4) | (z & 0xF));
    }

    public Entity[] getEntities() {
        int count = 0, index = 0;
        net.minecraft.world.chunk.Chunk chunk = getHandle();

        for (int i = 0; i < 16; i++) {
            count += chunk.getEntityLists()[i].size();
        }

        Entity[] entities = new Entity[count];

        for (int i = 0; i < 16; i++) {

            for (Object obj : chunk.getEntityLists()[i].toArray()) {
                if (!(obj instanceof net.minecraft.entity.Entity)) {
                    continue;
                }

                entities[index++] = ((net.minecraft.entity.Entity) obj).getBukkitEntity();
            }
        }

        return entities;
    }

    public BlockState[] getTileEntities() {
        int index = 0;
        net.minecraft.world.chunk.Chunk chunk = getHandle();

        BlockState[] entities = new BlockState[chunk.getTileEntityMap().size()];

        for (Object obj : chunk.getTileEntityMap().keySet().toArray()) {
            if (!(obj instanceof BlockPos)) {
                continue;
            }

            BlockPos position = (BlockPos) obj;
            entities[index++] = nmsWorld.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
        }

        return entities;
    }

    public boolean isLoaded() {
        return getWorld().isChunkLoaded(this);
    }

    public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
    }

    public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
    }

    public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
    }

    @Override
    public boolean isSlimeChunk() {
        // 987234911L is deterimined in EntitySlime when seeing if a slime can spawn in a chunk
        return getHandle().getRandomWithSeed(987234911L).nextInt(10) == 0;
    }

    public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
    }

    public boolean unload(boolean save, boolean safe) {
        return getWorld().unloadChunk(getX(), getZ(), save, safe);
    }

    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        net.minecraft.world.chunk.Chunk chunk = getHandle();

        ExtendedBlockStorage[] cs = chunk.getBlockStorageArray();
        short[][] sectionBlockIDs = new short[cs.length][];
        byte[][] sectionBlockData = new byte[cs.length][];
        byte[][] sectionSkyLights = new byte[cs.length][];
        byte[][] sectionEmitLights = new byte[cs.length][];
        boolean[] sectionEmpty = new boolean[cs.length];

        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == null) { // Section is empty?
                sectionBlockIDs[i] = emptyBlockIDs;
                sectionBlockData[i] = emptyData;
                sectionSkyLights[i] = emptySkyLight;
                sectionEmitLights[i] = emptyData;
                sectionEmpty[i] = true;
            } else { // Not empty
                short[] blockids = new short[4096];

                byte[] rawIds = new byte[4096];
                NibbleArray data = new NibbleArray();
                NibbleArray blockIdExtensionData = cs[i].getData().getDataForNBT(rawIds, data); // CatServer

                byte[] dataValues = sectionBlockData[i] = data.getData();

                // Copy base IDs
                for (int j = 0; j < 4096; j++) {
                    // CatServer start
                    int extData = blockIdExtensionData == null ? 0 : blockIdExtensionData.get(j & 15, j >> 8 & 15, j >> 4 & 15);
                    int blockId = (extData << 12 | (rawIds[j] & 0xFF) << 4 | extData) >> 4;
                    blockids[j] = blockId > Short.MAX_VALUE ? 0 : (short)blockId; // Not support id > 32767
                    // CatServer end
                }

                sectionBlockIDs[i] = blockids;

                if (cs[i].getSkyLight() == null) {
                    sectionSkyLights[i] = emptyData;
                } else {
                    sectionSkyLights[i] = new byte[2048];
                    System.arraycopy(cs[i].getSkyLight().getData(), 0, sectionSkyLights[i], 0, 2048);
                }
                sectionEmitLights[i] = new byte[2048];
                System.arraycopy(cs[i].getBlockLight().getData(), 0, sectionEmitLights[i], 0, 2048);
            }
        }

        int[] hmap = null;

        if (includeMaxBlockY) {
            hmap = new int[256]; // Get copy of height map
            System.arraycopy(chunk.getHeightMap(), 0, hmap, 0, 256);
        }

        Biome[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;

        if (includeBiome || includeBiomeTempRain) {
            BiomeProvider wcm = chunk.getWorld().getBiomeProvider();

            if (includeBiome) {
                biome = new Biome[256];
                for (int i = 0; i < 256; i++) {
                    biome[i] = chunk.getBiome(new BlockPos(i & 0xF, 0, i >> 4), wcm);
                }
            }

            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                float[] dat = getTemperatures(wcm, getX() << 4, getZ() << 4);

                for (int i = 0; i < 256; i++) {
                    biomeTemp[i] = dat[i];
                }

                /* Removed 15w46a
                dat = wcm.getWetness(null, getX() << 4, getZ() << 4, 16, 16);

                for (int i = 0; i < 256; i++) {
                    biomeRain[i] = dat[i];
                }
                */
            }
        }

        World world = getWorld();
        return new CraftChunkSnapshot(getX(), getZ(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionBlockData, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, biome, biomeTemp, biomeRain);
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        Biome[] biome = null;
        double[] biomeTemp = null;
        double[] biomeRain = null;

        if (includeBiome || includeBiomeTempRain) {
            BiomeProvider wcm = world.getHandle().getBiomeProvider();

            if (includeBiome) {
                biome = new Biome[256];
                for (int i = 0; i < 256; i++) {
                    biome[i] = world.getHandle().getBiome(new BlockPos((x << 4) + (i & 0xF), 0, (z << 4) + (i >> 4)));
                }
            }

            if (includeBiomeTempRain) {
                biomeTemp = new double[256];
                biomeRain = new double[256];
                float[] dat = getTemperatures(wcm, x << 4, z << 4);

                for (int i = 0; i < 256; i++) {
                    biomeTemp[i] = dat[i];
                }

                /* Removed 15w46a
                dat = wcm.getWetness(null, x << 4, z << 4, 16, 16);

                for (int i = 0; i < 256; i++) {
                    biomeRain[i] = dat[i];
                }
                */
            }
        }

        /* Fill with empty data */
        int hSection = world.getMaxHeight() >> 4;
        short[][] blockIDs = new short[hSection][];
        byte[][] skyLight = new byte[hSection][];
        byte[][] emitLight = new byte[hSection][];
        byte[][] blockData = new byte[hSection][];
        boolean[] empty = new boolean[hSection];

        for (int i = 0; i < hSection; i++) {
            blockIDs[i] = emptyBlockIDs;
            skyLight[i] = emptySkyLight;
            emitLight[i] = emptyData;
            blockData[i] = emptyData;
            empty[i] = true;
        }

        return new CraftChunkSnapshot(x, z, world.getName(), world.getFullTime(), blockIDs, blockData, skyLight, emitLight, empty, new int[256], biome, biomeTemp, biomeRain);
    }

    private static float[] getTemperatures(BiomeProvider chunkmanager, int chunkX, int chunkZ) {
        Biome[] biomes = chunkmanager.getBiomes(null, chunkX, chunkZ, 16, 16);
        float[] temps = new float[biomes.length];

        for (int i = 0; i < biomes.length; i++) {
            float temp = biomes[i].getDefaultTemperature(); // Vanilla of olde: ((int) biomes[i].temperature * 65536.0F) / 65536.0F

            if (temp > 1F) {
                temp = 1F;
            }

            temps[i] = temp;
        }

        return temps;
    }

    static {
        Arrays.fill(emptySkyLight, (byte) 0xFF);
    }
}
