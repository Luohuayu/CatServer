// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import net.minecraft.nbt.NBTTagCompound;
import java.util.HashMap;
import org.bukkit.enchantments.Enchantment;
import java.util.Map;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

//@DelegateDeserialization(SerializableMeta.class)
class CraftMetaEnchantedBook extends CraftMetaItem implements EnchantmentStorageMeta
{
    static final ItemMetaKey STORED_ENCHANTMENTS;
    private Map<Enchantment, Integer> enchantments;
    
    static {
        STORED_ENCHANTMENTS = new ItemMetaKey("StoredEnchantments", "stored-enchants");
    }
    
    CraftMetaEnchantedBook(final CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaEnchantedBook)) {
            return;
        }
        final CraftMetaEnchantedBook that = (CraftMetaEnchantedBook)meta;
        if (that.hasEnchants()) {
            this.enchantments = new HashMap<Enchantment, Integer>(that.enchantments);
        }
    }
    
    CraftMetaEnchantedBook(final NBTTagCompound tag) {
        super(tag);
        if (!tag.hasKey(CraftMetaEnchantedBook.STORED_ENCHANTMENTS.NBT)) {
            return;
        }
        this.enchantments = CraftMetaItem.buildEnchantments(tag, CraftMetaEnchantedBook.STORED_ENCHANTMENTS);
    }
    
    CraftMetaEnchantedBook(final Map<String, Object> map) {
        super(map);
        this.enchantments = CraftMetaItem.buildEnchantments(map, CraftMetaEnchantedBook.STORED_ENCHANTMENTS);
    }
    
    @Override
    void applyToItem(final NBTTagCompound itemTag) {
        super.applyToItem(itemTag);
        CraftMetaItem.applyEnchantments(this.enchantments, itemTag, CraftMetaEnchantedBook.STORED_ENCHANTMENTS);
    }
    
    @Override
    boolean applicableTo(final Material type) {
        switch (type) {
            case ENCHANTED_BOOK: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isEnchantedEmpty();
    }
    
    @Override
    boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaEnchantedBook) {
            final CraftMetaEnchantedBook that = (CraftMetaEnchantedBook)meta;
            return this.hasStoredEnchants() ? (that.hasStoredEnchants() && this.enchantments.equals(that.enchantments)) : (!that.hasStoredEnchants());
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaEnchantedBook || this.isEnchantedEmpty());
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.hasStoredEnchants()) {
            hash = 61 * hash + this.enchantments.hashCode();
        }
        return (original != hash) ? (CraftMetaEnchantedBook.class.hashCode() ^ hash) : hash;
    }
    
    @Override
    public CraftMetaEnchantedBook clone() {
        final CraftMetaEnchantedBook meta = (CraftMetaEnchantedBook)super.clone();
        if (this.enchantments != null) {
            meta.enchantments = new HashMap<Enchantment, Integer>(this.enchantments);
        }
        return meta;
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        CraftMetaItem.serializeEnchantments(this.enchantments, builder, CraftMetaEnchantedBook.STORED_ENCHANTMENTS);
        return builder;
    }
    
    boolean isEnchantedEmpty() {
        return !this.hasStoredEnchants();
    }
    
    @Override
    public boolean hasStoredEnchant(final Enchantment ench) {
        return this.hasStoredEnchants() && this.enchantments.containsKey(ench);
    }
    
    @Override
    public int getStoredEnchantLevel(final Enchantment ench) {
        final Integer level = this.hasStoredEnchants() ? this.enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }
    
    @Override
    public Map<Enchantment, Integer> getStoredEnchants() {
        return (Map<Enchantment, Integer>)(this.hasStoredEnchants() ? ImmutableMap.copyOf((Map)this.enchantments) : ImmutableMap.of());
    }
    
    @Override
    public boolean addStoredEnchant(final Enchantment ench, final int level, final boolean ignoreRestrictions) {
        if (this.enchantments == null) {
            this.enchantments = new HashMap<Enchantment, Integer>(4);
        }
        if (ignoreRestrictions || (level >= ench.getStartLevel() && level <= ench.getMaxLevel())) {
            final Integer old = this.enchantments.put(ench, level);
            return old == null || old != level;
        }
        return false;
    }
    
    @Override
    public boolean removeStoredEnchant(final Enchantment ench) {
        return this.hasStoredEnchants() && this.enchantments.remove(ench) != null;
    }
    
    @Override
    public boolean hasStoredEnchants() {
        return this.enchantments != null && !this.enchantments.isEmpty();
    }
    
    @Override
    public boolean hasConflictingStoredEnchant(final Enchantment ench) {
        return CraftMetaItem.checkConflictingEnchants(this.enchantments, ench);
    }
}
