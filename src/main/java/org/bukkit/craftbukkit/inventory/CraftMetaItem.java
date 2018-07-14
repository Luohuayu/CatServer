// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import java.util.NoSuchElementException;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang.Validate;
import java.lang.reflect.Constructor;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Strings;
import org.bukkit.Material;
import org.bukkit.craftbukkit.Overridden;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.ByteArrayInputStream;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemFlag;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTBase;
import java.util.Set;
import org.bukkit.enchantments.Enchantment;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.ItemMeta;

//@DelegateDeserialization(SerializableMeta.class)
class CraftMetaItem implements ItemMeta, Repairable
{
    static final ItemMetaKey NAME;
    static final ItemMetaKey DISPLAY;
    static final ItemMetaKey LORE;
    static final ItemMetaKey ENCHANTMENTS;
    static final ItemMetaKey ENCHANTMENTS_ID;
    static final ItemMetaKey ENCHANTMENTS_LVL;
    static final ItemMetaKey REPAIR;
    static final ItemMetaKey ATTRIBUTES;
    static final ItemMetaKey ATTRIBUTES_IDENTIFIER;
    static final ItemMetaKey ATTRIBUTES_NAME;
    static final ItemMetaKey ATTRIBUTES_VALUE;
    static final ItemMetaKey ATTRIBUTES_TYPE;
    static final ItemMetaKey ATTRIBUTES_UUID_HIGH;
    static final ItemMetaKey ATTRIBUTES_UUID_LOW;
    static final ItemMetaKey HIDEFLAGS;
    private String displayName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private int repairCost;
    private int hideFlag;
    private static final Set<String> HANDLED_TAGS;
    private final Map<String, NBTBase> unhandledTags;
    
    static {
        NAME = new ItemMetaKey("Name", "display-name");
        DISPLAY = new ItemMetaKey("display");
        LORE = new ItemMetaKey("Lore", "lore");
        ENCHANTMENTS = new ItemMetaKey("ench", "enchants");
        ENCHANTMENTS_ID = new ItemMetaKey("id");
        ENCHANTMENTS_LVL = new ItemMetaKey("lvl");
        REPAIR = new ItemMetaKey("RepairCost", "repair-cost");
        ATTRIBUTES = new ItemMetaKey("AttributeModifiers");
        ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
        ATTRIBUTES_NAME = new ItemMetaKey("Name");
        ATTRIBUTES_VALUE = new ItemMetaKey("Amount");
        ATTRIBUTES_TYPE = new ItemMetaKey("Operation");
        ATTRIBUTES_UUID_HIGH = new ItemMetaKey("UUIDMost");
        ATTRIBUTES_UUID_LOW = new ItemMetaKey("UUIDLeast");
        HIDEFLAGS = new ItemMetaKey("HideFlags", "ItemFlags");
        HANDLED_TAGS = Sets.newHashSet();
    }
    
    CraftMetaItem(final CraftMetaItem meta) {
        this.unhandledTags = new HashMap<String, NBTBase>();
        if (meta == null) {
            return;
        }
        this.displayName = meta.displayName;
        if (meta.hasLore()) {
            this.lore = new ArrayList<String>(meta.lore);
        }
        if (meta.hasEnchants()) {
            this.enchantments = new HashMap<Enchantment, Integer>(meta.enchantments);
        }
        this.repairCost = meta.repairCost;
        this.hideFlag = meta.hideFlag;
        this.unhandledTags.putAll(meta.unhandledTags);
    }
    
    CraftMetaItem(final NBTTagCompound tag) {
        this.unhandledTags = new HashMap<String, NBTBase>();
        if (tag.hasKey(CraftMetaItem.DISPLAY.NBT)) {
            final NBTTagCompound display = tag.getCompoundTag(CraftMetaItem.DISPLAY.NBT);
            if (display.hasKey(CraftMetaItem.NAME.NBT)) {
                this.displayName = display.getString(CraftMetaItem.NAME.NBT);
            }
            if (display.hasKey(CraftMetaItem.LORE.NBT)) {
                final NBTTagList list = display.getTagList(CraftMetaItem.LORE.NBT, 8);
                this.lore = new ArrayList<String>(list.tagCount());
                for (int index = 0; index < list.tagCount(); ++index) {
                    final String line = list.getStringTagAt(index);
                    this.lore.add(line);
                }
            }
        }
        this.enchantments = buildEnchantments(tag, CraftMetaItem.ENCHANTMENTS);
        if (tag.hasKey(CraftMetaItem.REPAIR.NBT)) {
            this.repairCost = tag.getInteger(CraftMetaItem.REPAIR.NBT);
        }
        if (tag.hasKey(CraftMetaItem.HIDEFLAGS.NBT)) {
            this.hideFlag = tag.getInteger(CraftMetaItem.HIDEFLAGS.NBT);
        }
        if (tag.getTag(CraftMetaItem.ATTRIBUTES.NBT) instanceof NBTTagList) {
            NBTTagList save = null;
            final NBTTagList nbttaglist = tag.getTagList(CraftMetaItem.ATTRIBUTES.NBT, 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                if (nbttaglist.getCompoundTagAt(i) instanceof NBTTagCompound) {
                    final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                    if (nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT, 99)) {
                        if (nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT, 99)) {
                            if (nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT) instanceof NBTTagString) {
                                if (CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT))) {
                                    if (nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_NAME.NBT) instanceof NBTTagString) {
                                        if (!nbttagcompound.getString(CraftMetaItem.ATTRIBUTES_NAME.NBT).isEmpty()) {
                                            if (nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_VALUE.NBT, 99)) {
                                                if (nbttagcompound.hasKey(CraftMetaItem.ATTRIBUTES_TYPE.NBT, 99) && nbttagcompound.getInteger(CraftMetaItem.ATTRIBUTES_TYPE.NBT) >= 0) {
                                                    if (nbttagcompound.getInteger(CraftMetaItem.ATTRIBUTES_TYPE.NBT) <= 2) {
                                                        if (save == null) {
                                                            save = new NBTTagList();
                                                        }
                                                        final NBTTagCompound entry = new NBTTagCompound();
                                                        entry.setTag(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT));
                                                        entry.setTag(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT));
                                                        entry.setTag(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT));
                                                        entry.setTag(CraftMetaItem.ATTRIBUTES_NAME.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_NAME.NBT));
                                                        entry.setTag(CraftMetaItem.ATTRIBUTES_VALUE.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_VALUE.NBT));
                                                        entry.setTag(CraftMetaItem.ATTRIBUTES_TYPE.NBT, nbttagcompound.getTag(CraftMetaItem.ATTRIBUTES_TYPE.NBT));
                                                        save.appendTag(entry);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.unhandledTags.put(CraftMetaItem.ATTRIBUTES.NBT, save);
        }
        final Set<String> keys = tag.getKeySet();
        for (final String key : keys) {
            if (!getHandledTags().contains(key)) {
                this.unhandledTags.put(key, tag.getTag(key));
            }
        }
    }
    
    static Map<Enchantment, Integer> buildEnchantments(final NBTTagCompound tag, final ItemMetaKey key) {
        if (!tag.hasKey(key.NBT)) {
            return null;
        }
        final NBTTagList ench = tag.getTagList(key.NBT, 10);
        final Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(ench.tagCount());
        for (int i = 0; i < ench.tagCount(); ++i) {
            final int id = 0xFFFF & ench.getCompoundTagAt(i).getShort(CraftMetaItem.ENCHANTMENTS_ID.NBT);
            final int level = 0xFFFF & ench.getCompoundTagAt(i).getShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT);
            enchantments.put(Enchantment.getById(id), level);
        }
        return enchantments;
    }
    
    CraftMetaItem(final Map<String, Object> map) {
        this.unhandledTags = new HashMap<String, NBTBase>();
        this.setDisplayName(SerializableMeta.getString(map, CraftMetaItem.NAME.BUKKIT, true));
        final Iterable<?> lore = SerializableMeta.getObject(/*(Class<Iterable<?>>)*/Iterable.class, map, CraftMetaItem.LORE.BUKKIT, true);
        if (lore != null) {
            safelyAdd(lore, this.lore = new ArrayList<String>(), Integer.MAX_VALUE);
        }
        this.enchantments = buildEnchantments(map, CraftMetaItem.ENCHANTMENTS);
        final Integer repairCost = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.REPAIR.BUKKIT, true);
        if (repairCost != null) {
            this.setRepairCost(repairCost);
        }
        final Set hideFlags = SerializableMeta.getObject(Set.class, map, CraftMetaItem.HIDEFLAGS.BUKKIT, true);
        if (hideFlags != null) {
            for (final Object hideFlagObject : hideFlags) {
                final String hideFlagString = (String)hideFlagObject;
                try {
                    final ItemFlag hideFlatEnum = ItemFlag.valueOf(hideFlagString);
                    this.addItemFlags(hideFlatEnum);
                }
                catch (IllegalArgumentException ex2) {}
            }
        }
        final String internal = SerializableMeta.getString(map, "internal", true);
        if (internal != null) {
            final ByteArrayInputStream buf = new ByteArrayInputStream(Base64.decodeBase64(internal));
            try {
                final NBTTagCompound tag = CompressedStreamTools.readCompressed(buf);
                this.deserializeInternal(tag);
                final Set<String> keys = tag.getKeySet();
                for (final String key : keys) {
                    if (!getHandledTags().contains(key)) {
                        this.unhandledTags.put(key, tag.getTag(key));
                    }
                }
            }
            catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void deserializeInternal(final NBTTagCompound tag) {
    }
    
    static Map<Enchantment, Integer> buildEnchantments(final Map<String, Object> map, final ItemMetaKey key) {
        final Map<?, ?> ench = SerializableMeta.getObject(/*(Class<Map<?, ?>>)*/Map.class, map, key.BUKKIT, true);
        if (ench == null) {
            return null;
        }
        final Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(ench.size());
        for (final Map.Entry<?, ?> entry : ench.entrySet()) {
            final Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());
            if (enchantment != null && entry.getValue() instanceof Integer) {
                enchantments.put(enchantment, (Integer)entry.getValue());
            }
        }
        return enchantments;
    }
    
    @Overridden
    void applyToItem(final NBTTagCompound itemTag) {
        if (this.hasDisplayName()) {
            this.setDisplayTag(itemTag, CraftMetaItem.NAME.NBT, new NBTTagString(this.displayName));
        }
        if (this.hasLore()) {
            this.setDisplayTag(itemTag, CraftMetaItem.LORE.NBT, createStringList(this.lore));
        }
        if (this.hideFlag != 0) {
            itemTag.setInteger(CraftMetaItem.HIDEFLAGS.NBT, this.hideFlag);
        }
        applyEnchantments(this.enchantments, itemTag, CraftMetaItem.ENCHANTMENTS);
        if (this.hasRepairCost()) {
            itemTag.setInteger(CraftMetaItem.REPAIR.NBT, this.repairCost);
        }
        for (final Map.Entry<String, NBTBase> e : this.unhandledTags.entrySet()) {
            itemTag.setTag(e.getKey(), e.getValue());
        }
    }
    
    static NBTTagList createStringList(final List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        final NBTTagList tagList = new NBTTagList();
        for (final String value : list) {
            tagList.appendTag(new NBTTagString(value));
        }
        return tagList;
    }
    
    static void applyEnchantments(final Map<Enchantment, Integer> enchantments, final NBTTagCompound tag, final ItemMetaKey key) {
        if (enchantments == null || enchantments.size() == 0) {
            return;
        }
        final NBTTagList list = new NBTTagList();
        for (final Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            final NBTTagCompound subtag = new NBTTagCompound();
            subtag.setShort(CraftMetaItem.ENCHANTMENTS_ID.NBT, (short)entry.getKey().getId());
            subtag.setShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT, entry.getValue().shortValue());
            list.appendTag(subtag);
        }
        tag.setTag(key.NBT, list);
    }
    
    void setDisplayTag(final NBTTagCompound tag, final String key, final NBTBase value) {
        final NBTTagCompound display = tag.getCompoundTag(CraftMetaItem.DISPLAY.NBT);
        if (!tag.hasKey(CraftMetaItem.DISPLAY.NBT)) {
            tag.setTag(CraftMetaItem.DISPLAY.NBT, display);
        }
        display.setTag(key, value);
    }
    
    @Overridden
    boolean applicableTo(final Material type) {
        return type != Material.AIR;
    }
    
    @Overridden
    boolean isEmpty() {
        return !this.hasDisplayName() && !this.hasEnchants() && !this.hasLore() && !this.hasRepairCost() && this.unhandledTags.isEmpty() && this.hideFlag == 0;
    }
    
    @Override
    public String getDisplayName() {
        return this.displayName;
    }
    
    @Override
    public final void setDisplayName(final String name) {
        this.displayName = name;
    }
    
    @Override
    public boolean hasDisplayName() {
        return !Strings.isNullOrEmpty(this.displayName);
    }
    
    @Override
    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }
    
    @Override
    public boolean hasRepairCost() {
        return this.repairCost > 0;
    }
    
    @Override
    public boolean hasEnchant(final Enchantment ench) {
        return this.hasEnchants() && this.enchantments.containsKey(ench);
    }
    
    @Override
    public int getEnchantLevel(final Enchantment ench) {
        final Integer level = this.hasEnchants() ? this.enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }
    
    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return (Map<Enchantment, Integer>)(this.hasEnchants() ? ImmutableMap.copyOf((Map)this.enchantments) : ImmutableMap.of());
    }
    
    @Override
    public boolean addEnchant(final Enchantment ench, final int level, final boolean ignoreRestrictions) {
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
    public boolean removeEnchant(final Enchantment ench) {
        return this.hasEnchants() && this.enchantments.remove(ench) != null;
    }
    
    @Override
    public boolean hasEnchants() {
        return this.enchantments != null && !this.enchantments.isEmpty();
    }
    
    @Override
    public boolean hasConflictingEnchant(final Enchantment ench) {
        return checkConflictingEnchants(this.enchantments, ench);
    }
    
    @Override
    public void addItemFlags(final ItemFlag... hideFlags) {
        for (final ItemFlag f : hideFlags) {
            this.hideFlag |= this.getBitModifier(f);
        }
    }
    
    @Override
    public void removeItemFlags(final ItemFlag... hideFlags) {
        for (final ItemFlag f : hideFlags) {
            this.hideFlag &= ~this.getBitModifier(f);
        }
    }
    
    @Override
    public Set<ItemFlag> getItemFlags() {
        final Set<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);
        ItemFlag[] values;
        for (int length = (values = ItemFlag.values()).length, i = 0; i < length; ++i) {
            final ItemFlag f = values[i];
            if (this.hasItemFlag(f)) {
                currentFlags.add(f);
            }
        }
        return currentFlags;
    }
    
    @Override
    public boolean hasItemFlag(final ItemFlag flag) {
        final int bitModifier = this.getBitModifier(flag);
        return (this.hideFlag & bitModifier) == bitModifier;
    }
    
    private byte getBitModifier(final ItemFlag hideFlag) {
        return (byte)(1 << hideFlag.ordinal());
    }
    
    @Override
    public List<String> getLore() {
        return (this.lore == null) ? null : new ArrayList<String>(this.lore);
    }
    
    @Override
    public void setLore(final List<String> lore) {
        if (lore == null) {
            this.lore = null;
        }
        else if (this.lore == null) {
            safelyAdd(lore, this.lore = new ArrayList<String>(lore.size()), Integer.MAX_VALUE);
        }
        else {
            this.lore.clear();
            safelyAdd(lore, this.lore, Integer.MAX_VALUE);
        }
    }
    
    @Override
    public int getRepairCost() {
        return this.repairCost;
    }
    
    @Override
    public void setRepairCost(final int cost) {
        this.repairCost = cost;
    }
    
    @Override
    public final boolean equals(final Object object) {
        return object != null && (object == this || (object instanceof CraftMetaItem && CraftItemFactory.instance().equals(this, (ItemMeta)object)));
    }
    
    @Overridden
    boolean equalsCommon(final CraftMetaItem that) {
        if (this.hasDisplayName()) {
            if (!that.hasDisplayName() || !this.displayName.equals(that.displayName)) {
                return false;
            }
        }
        else if (that.hasDisplayName()) {
            return false;
        }
        if (this.hasEnchants()) {
            if (!that.hasEnchants() || !this.enchantments.equals(that.enchantments)) {
                return false;
            }
        }
        else if (that.hasEnchants()) {
            return false;
        }
        if (this.hasLore()) {
            if (!that.hasLore() || !this.lore.equals(that.lore)) {
                return false;
            }
        }
        else if (that.hasLore()) {
            return false;
        }
        if (this.hasRepairCost()) {
            if (!that.hasRepairCost() || this.repairCost != that.repairCost) {
                return false;
            }
        }
        else if (that.hasRepairCost()) {
            return false;
        }
        if (this.unhandledTags.equals(that.unhandledTags) && this.hideFlag == that.hideFlag) {
            return true;
        }
        return false;
    }
    
    @Overridden
    boolean notUncommon(final CraftMetaItem meta) {
        return true;
    }
    
    @Override
    public final int hashCode() {
        return this.applyHash();
    }
    
    @Overridden
    int applyHash() {
        int hash = 3;
        hash = 61 * hash + (this.hasDisplayName() ? this.displayName.hashCode() : 0);
        hash = 61 * hash + (this.hasLore() ? this.lore.hashCode() : 0);
        hash = 61 * hash + (this.hasEnchants() ? this.enchantments.hashCode() : 0);
        hash = 61 * hash + (this.hasRepairCost() ? this.repairCost : 0);
        hash = 61 * hash + this.unhandledTags.hashCode();
        hash = 61 * hash + this.hideFlag;
        return hash;
    }
    
    @Overridden
    @Override
    public CraftMetaItem clone() {
        try {
            final CraftMetaItem clone = (CraftMetaItem)super.clone();
            if (this.lore != null) {
                clone.lore = new ArrayList<String>(this.lore);
            }
            if (this.enchantments != null) {
                clone.enchantments = new HashMap<Enchantment, Integer>(this.enchantments);
            }
            clone.hideFlag = this.hideFlag;
            return clone;
        }
        catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
    
    @Override
    public final Map<String, Object> serialize() {
        final ImmutableMap.Builder<String, Object> map = /*(ImmutableMap.Builder<String, Object>)*/ImmutableMap.builder();
        map.put("meta-type", SerializableMeta.classMap.get(this.getClass()));
        this.serialize(map);
        return (Map<String, Object>)map.build();
    }
    
    @Overridden
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        if (this.hasDisplayName()) {
            builder.put(CraftMetaItem.NAME.BUKKIT, this.displayName);
        }
        if (this.hasLore()) {
            builder.put(CraftMetaItem.LORE.BUKKIT, ImmutableList.copyOf(this.lore));
        }
        serializeEnchantments(this.enchantments, builder, CraftMetaItem.ENCHANTMENTS);
        if (this.hasRepairCost()) {
            builder.put(CraftMetaItem.REPAIR.BUKKIT, this.repairCost);
        }
        final Set<String> hideFlags = new HashSet<String>();
        for (final ItemFlag hideFlagEnum : this.getItemFlags()) {
            hideFlags.add(hideFlagEnum.name());
        }
        if (!hideFlags.isEmpty()) {
            builder.put(CraftMetaItem.HIDEFLAGS.BUKKIT, hideFlags);
        }
        final Map<String, NBTBase> internalTags = new HashMap<String, NBTBase>(this.unhandledTags);
        this.serializeInternal(internalTags);
        if (!internalTags.isEmpty()) {
            final NBTTagCompound internal = new NBTTagCompound();
            for (final Map.Entry<String, NBTBase> e : internalTags.entrySet()) {
                internal.setTag(e.getKey(), e.getValue());
            }
            try {
                final ByteArrayOutputStream buf = new ByteArrayOutputStream();
                CompressedStreamTools.writeCompressed(internal, buf);
                builder.put("internal", Base64.encodeBase64String(buf.toByteArray()));
            }
            catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return builder;
    }
    
    void serializeInternal(final Map<String, NBTBase> unhandledTags) {
    }
    
    static void serializeEnchantments(final Map<Enchantment, Integer> enchantments, final ImmutableMap.Builder<String, Object> builder, final ItemMetaKey key) {
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }
        final ImmutableMap.Builder<String, Integer> enchants = /*(ImmutableMap.Builder<String, Integer>)*/ImmutableMap.builder();
        for (final Map.Entry<? extends Enchantment, Integer> enchant : enchantments.entrySet()) {
            enchants.put(((Enchantment)enchant.getKey()).getName(), enchant.getValue());
        }
        builder.put(key.BUKKIT, enchants.build());
    }
    
    static void safelyAdd(final Iterable<?> addFrom, final Collection<String> addTo, final int maxItemLength) {
        if (addFrom == null) {
            return;
        }
        for (final Object object : addFrom) {
            if (!(object instanceof String)) {
                if (object != null) {
                    throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
                }
                addTo.add("");
            }
            else {
                String page = object.toString();
                if (page.length() > maxItemLength) {
                    page = page.substring(0, maxItemLength);
                }
                addTo.add(page);
            }
        }
    }
    
    static boolean checkConflictingEnchants(final Map<Enchantment, Integer> enchantments, final Enchantment ench) {
        if (enchantments == null || enchantments.isEmpty()) {
            return false;
        }
        for (final Enchantment enchant : enchantments.keySet()) {
            if (enchant.conflictsWith(ench)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final String toString() {
        return String.valueOf(SerializableMeta.classMap.get((Object)this.getClass())) + "_META:" + this.serialize();
    }
    
    public static Set<String> getHandledTags() {
        synchronized (CraftMetaItem.HANDLED_TAGS) {
            if (CraftMetaItem.HANDLED_TAGS.isEmpty()) {
                CraftMetaItem.HANDLED_TAGS.addAll(Arrays.asList(CraftMetaItem.DISPLAY.NBT, CraftMetaItem.REPAIR.NBT, CraftMetaItem.ENCHANTMENTS.NBT, CraftMetaItem.HIDEFLAGS.NBT, CraftMetaMap.MAP_SCALING.NBT, CraftMetaPotion.POTION_EFFECTS.NBT, CraftMetaPotion.DEFAULT_POTION.NBT, CraftMetaSkull.SKULL_OWNER.NBT, CraftMetaSkull.SKULL_PROFILE.NBT, CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, CraftMetaBook.BOOK_TITLE.NBT, CraftMetaBook.BOOK_AUTHOR.NBT, CraftMetaBook.BOOK_PAGES.NBT, CraftMetaBook.RESOLVED.NBT, CraftMetaBook.GENERATION.NBT, CraftMetaFirework.FIREWORKS.NBT, CraftMetaEnchantedBook.STORED_ENCHANTMENTS.NBT, CraftMetaCharge.EXPLOSION.NBT, CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT));
            }
            // monitorexit(CraftMetaItem.HANDLED_TAGS)
            return CraftMetaItem.HANDLED_TAGS;
        }
    }
    
    static class ItemMetaKey
    {
        final String BUKKIT;
        final String NBT;
        
        ItemMetaKey(final String both) {
            this(both, both);
        }
        
        ItemMetaKey(final String nbt, final String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }
        
        @Retention(RetentionPolicy.SOURCE)
        @Target({ ElementType.FIELD })
        @interface Specific {
            To value();
            
            public enum To
            {
                BUKKIT("BUKKIT", 0), 
                NBT("NBT", 1);
                
                private To(final String s, final int n) {
                }
            }
        }
    }
    
    @SerializableAs("ItemMeta")
    public static class SerializableMeta implements ConfigurationSerializable
    {
        static final String TYPE_FIELD = "meta-type";
        static final ImmutableMap<Object, Object> classMap;
        static final ImmutableMap<String, Constructor<? extends CraftMetaItem>> constructorMap;
        
        static {
            classMap = ImmutableMap.builder().put(CraftMetaBanner.class, "BANNER").put(CraftMetaBlockState.class, "TILE_ENTITY").put(CraftMetaBook.class, "BOOK").put(CraftMetaBookSigned.class, "BOOK_SIGNED").put(CraftMetaSkull.class, "SKULL").put(CraftMetaLeatherArmor.class, "LEATHER_ARMOR").put(CraftMetaMap.class, "MAP").put(CraftMetaPotion.class, "POTION").put(CraftMetaEnchantedBook.class, "ENCHANTED").put(CraftMetaFirework.class, "FIREWORK").put(CraftMetaCharge.class, "FIREWORK_EFFECT").put(CraftMetaItem.class, "UNSPECIFIC").build();
            final ImmutableMap.Builder<String, Constructor<? extends CraftMetaItem>> classConstructorBuilder = ImmutableMap.builder();
            for (final Entry<Object, Object> mapping : SerializableMeta.classMap.entrySet()) {
                try {
                    classConstructorBuilder.put((String) mapping.getValue(), ((Class<? extends CraftMetaItem>) mapping.getKey()).getDeclaredConstructor(Map.class));
                }
                catch (NoSuchMethodException e) {
                    throw new AssertionError((Object)e);
                }
            }
            constructorMap = classConstructorBuilder.build();
        }
        
        public static ItemMeta deserialize(final Map<String, Object> map) throws Throwable {
            Validate.notNull((Object)map, "Cannot deserialize null map");
            final String type = getString(map, "meta-type", false);
            final Constructor<? extends CraftMetaItem> constructor = (Constructor<? extends CraftMetaItem>)SerializableMeta.constructorMap.get((Object)type);
            if (constructor == null) {
                throw new IllegalArgumentException(String.valueOf(type) + " is not a valid " + "meta-type");
            }
            try {
                return (ItemMeta)constructor.newInstance(map);
            }
            catch (InstantiationException e) {
                throw new AssertionError((Object)e);
            }
            catch (IllegalAccessException e2) {
                throw new AssertionError((Object)e2);
            }
            catch (InvocationTargetException e3) {
                throw e3.getCause();
            }
        }
        
        @Override
        public Map<String, Object> serialize() {
            throw new AssertionError();
        }
        
        static String getString(final Map<?, ?> map, final Object field, final boolean nullable) {
            return getObject(String.class, map, field, nullable);
        }
        
        static boolean getBoolean(final Map<?, ?> map, final Object field) {
            final Boolean value = getObject(Boolean.class, map, field, true);
            return value != null && value;
        }
        
        static <T> T getObject(final Class<T> clazz, final Map<?, ?> map, final Object field, final boolean nullable) {
            final Object object = map.get(field);
            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            }
            if (object != null) {
                throw new IllegalArgumentException(field + "(" + object + ") is not a valid " + clazz);
            }
            if (!nullable) {
                throw new NoSuchElementException(map + " does not contain " + field);
            }
            return null;
        }
    }
}
