// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import java.util.Set;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.block.banner.PatternType;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import java.util.ArrayList;
import org.bukkit.block.banner.Pattern;
import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.BannerMeta;

//@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBanner extends CraftMetaItem implements BannerMeta
{
    static final ItemMetaKey BASE;
    static final ItemMetaKey PATTERNS;
    static final ItemMetaKey COLOR;
    static final ItemMetaKey PATTERN;
    private DyeColor base;
    private List<Pattern> patterns;
    
    static {
        BASE = new ItemMetaKey("Base", "base-color");
        PATTERNS = new ItemMetaKey("Patterns", "patterns");
        COLOR = new ItemMetaKey("Color", "color");
        PATTERN = new ItemMetaKey("Pattern", "pattern");
    }
    
    CraftMetaBanner(final CraftMetaItem meta) {
        super(meta);
        this.patterns = new ArrayList<Pattern>();
        if (!(meta instanceof CraftMetaBanner)) {
            return;
        }
        final CraftMetaBanner banner = (CraftMetaBanner)meta;
        this.base = banner.base;
        this.patterns = new ArrayList<Pattern>(banner.patterns);
    }
    
    CraftMetaBanner(final NBTTagCompound tag) {
        super(tag);
        this.patterns = new ArrayList<Pattern>();
        if (!tag.hasKey("BlockEntityTag")) {
            return;
        }
        final NBTTagCompound entityTag = tag.getCompoundTag("BlockEntityTag");
        this.base = (entityTag.hasKey(CraftMetaBanner.BASE.NBT) ? DyeColor.getByDyeData((byte)entityTag.getInteger(CraftMetaBanner.BASE.NBT)) : null);
        if (entityTag.hasKey(CraftMetaBanner.PATTERNS.NBT)) {
            final NBTTagList patterns = entityTag.getTagList(CraftMetaBanner.PATTERNS.NBT, 10);
            for (int i = 0; i < Math.min(patterns.tagCount(), 20); ++i) {
                final NBTTagCompound p = patterns.getCompoundTagAt(i);
                this.patterns.add(new Pattern(DyeColor.getByDyeData((byte)p.getInteger(CraftMetaBanner.COLOR.NBT)), PatternType.getByIdentifier(p.getString(CraftMetaBanner.PATTERN.NBT))));
            }
        }
    }
    
    CraftMetaBanner(final Map<String, Object> map) {
        super(map);
        this.patterns = new ArrayList<Pattern>();
        final String baseStr = SerializableMeta.getString(map, CraftMetaBanner.BASE.BUKKIT, true);
        if (baseStr != null) {
            this.base = DyeColor.valueOf(baseStr);
        }
        final Iterable<?> rawPatternList = SerializableMeta.getObject(/*(Class<Iterable<?>>)*/Iterable.class, map, CraftMetaBanner.PATTERNS.BUKKIT, true);
        if (rawPatternList == null) {
            return;
        }
        for (final Object obj : rawPatternList) {
            if (!(obj instanceof Pattern)) {
                throw new IllegalArgumentException("Object in pattern list is not valid. " + obj.getClass());
            }
            this.addPattern((Pattern)obj);
        }
    }
    
    @Override
    void applyToItem(final NBTTagCompound tag) {
        super.applyToItem(tag);
        final NBTTagCompound entityTag = new NBTTagCompound();
        if (this.base != null) {
            entityTag.setInteger(CraftMetaBanner.BASE.NBT, this.base.getDyeData());
        }
        final NBTTagList newPatterns = new NBTTagList();
        for (final Pattern p : this.patterns) {
            final NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger(CraftMetaBanner.COLOR.NBT, p.getColor().getDyeData());
            compound.setString(CraftMetaBanner.PATTERN.NBT, p.getPattern().getIdentifier());
            newPatterns.appendTag(compound);
        }
        entityTag.setTag(CraftMetaBanner.PATTERNS.NBT, newPatterns);
        tag.setTag("BlockEntityTag", entityTag);
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
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.base != null) {
            builder.put(/*(Object)*/CraftMetaBanner.BASE.BUKKIT, /*(Object)*/this.base.toString());
        }
        if (!this.patterns.isEmpty()) {
            builder.put(/*(Object)*/CraftMetaBanner.PATTERNS.BUKKIT, /*(Object)*/ImmutableList.copyOf(/*(Collection)*/this.patterns));
        }
        return builder;
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.base != null) {
            hash = 31 * hash + this.base.hashCode();
        }
        if (!this.patterns.isEmpty()) {
            hash = 31 * hash + this.patterns.hashCode();
        }
        return (original != hash) ? (CraftMetaBanner.class.hashCode() ^ hash) : hash;
    }
    
    public boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBanner) {
            final CraftMetaBanner that = (CraftMetaBanner)meta;
            return this.base == that.base && this.patterns.equals(that.patterns);
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBanner || (this.patterns.isEmpty() && this.base == null));
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.patterns.isEmpty() && this.base == null;
    }
    
    @Override
    boolean applicableTo(final Material type) {
        return type == Material.BANNER;
    }
}
