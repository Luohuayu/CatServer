// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.generator;

import java.util.Arrays;
import org.bukkit.material.MaterialData;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public final class CraftChunkData implements ChunkGenerator.ChunkData
{
    private final int maxHeight;
    private final char[][] sections;
    
    public CraftChunkData(final World world) {
        this(world.getMaxHeight());
    }
    
    CraftChunkData(final int maxHeight) {
        if (maxHeight > 256) {
            throw new IllegalArgumentException("World height exceeded max chunk height");
        }
        this.maxHeight = maxHeight;
        this.sections = new char[16][];
    }
    
    @Override
    public int getMaxHeight() {
        return this.maxHeight;
    }
    
    @Override
    public void setBlock(final int x, final int y, final int z, final Material material) {
        this.setBlock(x, y, z, material.getId());
    }
    
    @Override
    public void setBlock(final int x, final int y, final int z, final MaterialData material) {
        this.setBlock(x, y, z, material.getItemTypeId(), material.getData());
    }
    
    @Override
    public void setRegion(final int xMin, final int yMin, final int zMin, final int xMax, final int yMax, final int zMax, final Material material) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.getId());
    }
    
    @Override
    public void setRegion(final int xMin, final int yMin, final int zMin, final int xMax, final int yMax, final int zMax, final MaterialData material) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.getItemTypeId(), material.getData());
    }
    
    @Override
    public Material getType(final int x, final int y, final int z) {
        return Material.getMaterial(this.getTypeId(x, y, z));
    }
    
    @Override
    public MaterialData getTypeAndData(final int x, final int y, final int z) {
        return this.getType(x, y, z).getNewData(this.getData(x, y, z));
    }
    
    @Override
    public void setRegion(final int xMin, final int yMin, final int zMin, final int xMax, final int yMax, final int zMax, final int blockId) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, blockId, 0);
    }
    
    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, final int blockId, final int data) {
        if (xMin > 15 || yMin >= this.maxHeight || zMin > 15) {
            return;
        }
        if (xMin < 0) {
            xMin = 0;
        }
        if (yMin < 0) {
            yMin = 0;
        }
        if (zMin < 0) {
            zMin = 0;
        }
        if (xMax > 16) {
            xMax = 16;
        }
        if (yMax > this.maxHeight) {
            yMax = this.maxHeight;
        }
        if (zMax > 16) {
            zMax = 16;
        }
        if (xMin >= xMax || yMin >= yMax || zMin >= zMax) {
            return;
        }
        final char typeChar = (char)(blockId << 4 | data);
        if (xMin == 0 && xMax == 16) {
            if (zMin == 0 && zMax == 16) {
                for (int y = yMin & 0xF0; y < yMax; y += 16) {
                    final char[] section = this.getChunkSection(y, true);
                    if (y <= yMin) {
                        if (y + 16 > yMax) {
                            Arrays.fill(section, (yMin & 0xF) << 8, (yMax & 0xF) << 8, typeChar);
                        }
                        else {
                            Arrays.fill(section, (yMin & 0xF) << 8, 4096, typeChar);
                        }
                    }
                    else if (y + 16 > yMax) {
                        Arrays.fill(section, 0, (yMax & 0xF) << 8, typeChar);
                    }
                    else {
                        Arrays.fill(section, 0, 4096, typeChar);
                    }
                }
            }
            else {
                for (int y = yMin; y < yMax; ++y) {
                    final char[] section = this.getChunkSection(y, true);
                    final int offsetBase = (y & 0xF) << 8;
                    final int min = offsetBase | zMin << 4;
                    final int max = offsetBase + (zMax << 4);
                    Arrays.fill(section, min, max, typeChar);
                }
            }
        }
        else {
            for (int y = yMin; y < yMax; ++y) {
                final char[] section = this.getChunkSection(y, true);
                final int offsetBase = (y & 0xF) << 8;
                for (int z = zMin; z < zMax; ++z) {
                    final int offset = offsetBase | z << 4;
                    Arrays.fill(section, offset | xMin, offset + xMax, typeChar);
                }
            }
        }
    }
    
    @Override
    public void setBlock(final int x, final int y, final int z, final int blockId) {
        this.setBlock(x, y, z, blockId, (byte)0);
    }
    
    @Override
    public void setBlock(final int x, final int y, final int z, final int blockId, final byte data) {
        this.setBlock(x, y, z, (char)(blockId << 4 | data));
    }
    
    @Override
    public int getTypeId(final int x, final int y, final int z) {
        if (x != (x & 0xF) || y < 0 || y >= this.maxHeight || z != (z & 0xF)) {
            return 0;
        }
        final char[] section = this.getChunkSection(y, false);
        if (section == null) {
            return 0;
        }
        return section[(y & 0xF) << 8 | z << 4 | x] >> 4;
    }
    
    @Override
    public byte getData(final int x, final int y, final int z) {
        if (x != (x & 0xF) || y < 0 || y >= this.maxHeight || z != (z & 0xF)) {
            return 0;
        }
        final char[] section = this.getChunkSection(y, false);
        if (section == null) {
            return 0;
        }
        return (byte)(section[(y & 0xF) << 8 | z << 4 | x] & '\u000f');
    }
    
    private void setBlock(final int x, final int y, final int z, final char type) {
        if (x != (x & 0xF) || y < 0 || y >= this.maxHeight || z != (z & 0xF)) {
            return;
        }
        final char[] section = this.getChunkSection(y, true);
        section[(y & 0xF) << 8 | z << 4 | x] = type;
    }
    
    private char[] getChunkSection(final int y, final boolean create) {
        char[] section = this.sections[y >> 4];
        if (create && section == null) {
            section = (this.sections[y >> 4] = new char[4096]);
        }
        return section;
    }
    
    char[][] getRawChunkData() {
        return this.sections;
    }
}
