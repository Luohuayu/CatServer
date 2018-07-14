// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import java.util.ArrayList;
import org.bukkit.potion.PotionType;
import org.bukkit.potion.PotionEffect;
import java.util.List;
import org.bukkit.potion.PotionData;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.PotionMeta;

//@DelegateDeserialization(SerializableMeta.class)
class CraftMetaPotion extends CraftMetaItem implements PotionMeta
{
    static final ItemMetaKey AMPLIFIER;
    static final ItemMetaKey AMBIENT;
    static final ItemMetaKey DURATION;
    static final ItemMetaKey SHOW_PARTICLES;
    static final ItemMetaKey POTION_EFFECTS;
    static final ItemMetaKey ID;
    static final ItemMetaKey DEFAULT_POTION;
    private PotionData type;
    private List<PotionEffect> customEffects;
    
    static {
        AMPLIFIER = new ItemMetaKey("Amplifier", "amplifier");
        AMBIENT = new ItemMetaKey("Ambient", "ambient");
        DURATION = new ItemMetaKey("Duration", "duration");
        SHOW_PARTICLES = new ItemMetaKey("ShowParticles", "has-particles");
        POTION_EFFECTS = new ItemMetaKey("CustomPotionEffects", "custom-effects");
        ID = new ItemMetaKey("Id", "potion-id");
        DEFAULT_POTION = new ItemMetaKey("Potion", "potion-type");
    }
    
    CraftMetaPotion(final CraftMetaItem meta) {
        super(meta);
        this.type = new PotionData(PotionType.UNCRAFTABLE, false, false);
        if (!(meta instanceof CraftMetaPotion)) {
            return;
        }
        final CraftMetaPotion potionMeta = (CraftMetaPotion)meta;
        this.type = potionMeta.type;
        if (potionMeta.hasCustomEffects()) {
            this.customEffects = new ArrayList<PotionEffect>(potionMeta.customEffects);
        }
    }
    
    CraftMetaPotion(final NBTTagCompound tag) {
        super(tag);
        this.type = new PotionData(PotionType.UNCRAFTABLE, false, false);
        if (tag.hasKey(CraftMetaPotion.DEFAULT_POTION.NBT)) {
            this.type = CraftPotionUtil.toBukkit(tag.getString(CraftMetaPotion.DEFAULT_POTION.NBT));
        }
        if (tag.hasKey(CraftMetaPotion.POTION_EFFECTS.NBT)) {
            final NBTTagList list = tag.getTagList(CraftMetaPotion.POTION_EFFECTS.NBT, 10);
            final int length = list.tagCount();
            this.customEffects = new ArrayList<PotionEffect>(length);
            for (int i = 0; i < length; ++i) {
                final NBTTagCompound effect = list.getCompoundTagAt(i);
                final PotionEffectType type = PotionEffectType.getById(effect.getByte(CraftMetaPotion.ID.NBT));
                final int amp = effect.getByte(CraftMetaPotion.AMPLIFIER.NBT);
                final int duration = effect.getInteger(CraftMetaPotion.DURATION.NBT);
                final boolean ambient = effect.getBoolean(CraftMetaPotion.AMBIENT.NBT);
                final boolean particles = effect.getBoolean(CraftMetaPotion.SHOW_PARTICLES.NBT);
                this.customEffects.add(new PotionEffect(type, duration, amp, ambient, particles));
            }
        }
    }
    
    CraftMetaPotion(final Map<String, Object> map) {
        super(map);
        this.type = new PotionData(PotionType.UNCRAFTABLE, false, false);
        this.type = CraftPotionUtil.toBukkit(SerializableMeta.getString(map, CraftMetaPotion.DEFAULT_POTION.BUKKIT, true));
        final Iterable<?> rawEffectList = SerializableMeta.getObject(Iterable.class, map, CraftMetaPotion.POTION_EFFECTS.BUKKIT, true);
        if (rawEffectList == null) {
            return;
        }
        for (final Object obj : rawEffectList) {
            if (!(obj instanceof PotionEffect)) {
                throw new IllegalArgumentException("Object in effect list is not valid. " + obj.getClass());
            }
            this.addCustomEffect((PotionEffect)obj, true);
        }
    }
    
    @Override
    void applyToItem(final NBTTagCompound tag) {
        super.applyToItem(tag);
        tag.setString(CraftMetaPotion.DEFAULT_POTION.NBT, CraftPotionUtil.fromBukkit(this.type));
        if (this.customEffects != null) {
            final NBTTagList effectList = new NBTTagList();
            tag.setTag(CraftMetaPotion.POTION_EFFECTS.NBT, effectList);
            for (final PotionEffect effect : this.customEffects) {
                final NBTTagCompound effectData = new NBTTagCompound();
                effectData.setByte(CraftMetaPotion.ID.NBT, (byte)effect.getType().getId());
                effectData.setByte(CraftMetaPotion.AMPLIFIER.NBT, (byte)effect.getAmplifier());
                effectData.setInteger(CraftMetaPotion.DURATION.NBT, effect.getDuration());
                effectData.setBoolean(CraftMetaPotion.AMBIENT.NBT, effect.isAmbient());
                effectData.setBoolean(CraftMetaPotion.SHOW_PARTICLES.NBT, effect.hasParticles());
                effectList.appendTag(effectData);
            }
        }
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isPotionEmpty();
    }
    
    boolean isPotionEmpty() {
        return this.type.getType() == PotionType.UNCRAFTABLE && !this.hasCustomEffects();
    }
    
    @Override
    boolean applicableTo(final Material type) {
        switch (type) {
            case POTION:
            case SPLASH_POTION:
            case TIPPED_ARROW:
            case LINGERING_POTION: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public CraftMetaPotion clone() {
        final CraftMetaPotion clone = (CraftMetaPotion)super.clone();
        clone.type = this.type;
        if (this.customEffects != null) {
            clone.customEffects = new ArrayList<PotionEffect>(this.customEffects);
        }
        return clone;
    }
    
    @Override
    public void setBasePotionData(final PotionData data) {
        Validate.notNull((Object)data, "PotionData cannot be null");
        this.type = data;
    }
    
    @Override
    public PotionData getBasePotionData() {
        return this.type;
    }
    
    @Override
    public boolean hasCustomEffects() {
        return this.customEffects != null;
    }
    
    @Override
    public List<PotionEffect> getCustomEffects() {
        if (this.hasCustomEffects()) {
            return (List<PotionEffect>)ImmutableList.copyOf((Collection)this.customEffects);
        }
        return ImmutableList.of();
    }
    
    @Override
    public boolean addCustomEffect(final PotionEffect effect, final boolean overwrite) {
        Validate.notNull((Object)effect, "Potion effect must not be null");
        final int index = this.indexOfEffect(effect.getType());
        if (index == -1) {
            if (this.customEffects == null) {
                this.customEffects = new ArrayList<PotionEffect>();
            }
            this.customEffects.add(effect);
            return true;
        }
        if (!overwrite) {
            return false;
        }
        final PotionEffect old = this.customEffects.get(index);
        if (old.getAmplifier() == effect.getAmplifier() && old.getDuration() == effect.getDuration() && old.isAmbient() == effect.isAmbient()) {
            return false;
        }
        this.customEffects.set(index, effect);
        return true;
    }
    
    @Override
    public boolean removeCustomEffect(final PotionEffectType type) {
        Validate.notNull((Object)type, "Potion effect type must not be null");
        if (!this.hasCustomEffects()) {
            return false;
        }
        boolean changed = false;
        final Iterator<PotionEffect> iterator = this.customEffects.iterator();
        while (iterator.hasNext()) {
            final PotionEffect effect = iterator.next();
            if (type.equals(effect.getType())) {
                iterator.remove();
                changed = true;
            }
        }
        if (this.customEffects.isEmpty()) {
            this.customEffects = null;
        }
        return changed;
    }
    
    @Override
    public boolean hasCustomEffect(final PotionEffectType type) {
        Validate.notNull((Object)type, "Potion effect type must not be null");
        return this.indexOfEffect(type) != -1;
    }
    
    @Override
    public boolean setMainEffect(final PotionEffectType type) {
        Validate.notNull((Object)type, "Potion effect type must not be null");
        final int index = this.indexOfEffect(type);
        if (index == -1 || index == 0) {
            return false;
        }
        final PotionEffect old = this.customEffects.get(0);
        this.customEffects.set(0, this.customEffects.get(index));
        this.customEffects.set(index, old);
        return true;
    }
    
    private int indexOfEffect(final PotionEffectType type) {
        if (!this.hasCustomEffects()) {
            return -1;
        }
        for (int i = 0; i < this.customEffects.size(); ++i) {
            if (this.customEffects.get(i).getType().equals(type)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean clearCustomEffects() {
        final boolean changed = this.hasCustomEffects();
        this.customEffects = null;
        return changed;
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.type.getType() != PotionType.UNCRAFTABLE) {
            hash = 73 * hash + this.type.hashCode();
        }
        if (this.hasCustomEffects()) {
            hash = 73 * hash + this.customEffects.hashCode();
        }
        return (original != hash) ? (CraftMetaPotion.class.hashCode() ^ hash) : hash;
    }
    
    public boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaPotion) {
            final CraftMetaPotion that = (CraftMetaPotion)meta;
            if (this.type.equals(that.type)) {
                if (this.hasCustomEffects()) {
                    if (!that.hasCustomEffects() || !this.customEffects.equals(that.customEffects)) {
                        return false;
                    }
                }
                else if (that.hasCustomEffects()) {
                    return false;
                }
                return true;
            }
            return false;
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaPotion || this.isPotionEmpty());
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.type.getType() != PotionType.UNCRAFTABLE) {
            builder.put(CraftMetaPotion.DEFAULT_POTION.BUKKIT, CraftPotionUtil.fromBukkit(this.type));
        }
        if (this.hasCustomEffects()) {
            builder.put(CraftMetaPotion.POTION_EFFECTS.BUKKIT, ImmutableList.copyOf(this.customEffects));
        }
        return builder;
    }
}
