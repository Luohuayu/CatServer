// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import java.util.Collection;
import org.bukkit.Material;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftWorld;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import java.util.List;
import org.bukkit.DyeColor;
import net.minecraft.tileentity.TileEntityBanner;
import org.bukkit.block.Banner;

public class CraftBanner extends CraftBlockState implements Banner
{
    private final TileEntityBanner banner;
    private DyeColor base;
    private List<Pattern> patterns;
    
    public CraftBanner(final Block block) {
        super(block);
        this.patterns = new ArrayList<Pattern>();
        final CraftWorld world = (CraftWorld)block.getWorld();
        this.banner = (TileEntityBanner)world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
        this.base = DyeColor.getByDyeData((byte)this.banner.baseColor);
        if (this.banner.patterns != null) {
            for (int i = 0; i < this.banner.patterns.tagCount(); ++i) {
                final NBTTagCompound p = this.banner.patterns.getCompoundTagAt(i);
                this.patterns.add(new Pattern(DyeColor.getByDyeData((byte)p.getInteger("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }
    
    public CraftBanner(final Material material, final TileEntityBanner te) {
        super(material);
        this.patterns = new ArrayList<Pattern>();
        this.banner = te;
        this.base = DyeColor.getByDyeData((byte)this.banner.baseColor);
        if (this.banner.patterns != null) {
            for (int i = 0; i < this.banner.patterns.tagCount(); ++i) {
                final NBTTagCompound p = this.banner.patterns.getCompoundTagAt(i);
                this.patterns.add(new Pattern(DyeColor.getByDyeData((byte)p.getInteger("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }
    
    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }
    
    @Override
    public void setBaseColor(final DyeColor color) {
        this.base = color;
    }
    
    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(this.patterns);
    }
    
    @Override
    public void setPatterns(final List<Pattern> patterns) {
        this.patterns = new ArrayList<Pattern>(patterns);
    }
    
    @Override
    public void addPattern(final Pattern pattern) {
        this.patterns.add(pattern);
    }
    
    @Override
    public Pattern getPattern(final int i) {
        return this.patterns.get(i);
    }
    
    @Override
    public Pattern removePattern(final int i) {
        return this.patterns.remove(i);
    }
    
    @Override
    public void setPattern(final int i, final Pattern pattern) {
        this.patterns.set(i, pattern);
    }
    
    @Override
    public int numberOfPatterns() {
        return this.patterns.size();
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = !this.isPlaced() || super.update(force, applyPhysics);
        if (result) {
            this.banner.baseColor = this.base.getDyeData();
            final NBTTagList newPatterns = new NBTTagList();
            for (final Pattern p : this.patterns) {
                final NBTTagCompound compound = new NBTTagCompound();
                compound.setInteger("Color", p.getColor().getDyeData());
                compound.setString("Pattern", p.getPattern().getIdentifier());
                newPatterns.appendTag(compound);
            }
            this.banner.patterns = newPatterns;
            this.banner.markDirty();
        }
        return result;
    }
    
    @Override
    public TileEntityBanner getTileEntity() {
        return this.banner;
    }
}
