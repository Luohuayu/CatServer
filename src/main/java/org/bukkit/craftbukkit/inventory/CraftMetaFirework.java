// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.apache.commons.lang.Validate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Color;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import java.util.ArrayList;
import org.bukkit.FireworkEffect;
import java.util.List;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.FireworkMeta;

//@DelegateDeserialization(SerializableMeta.class)
class CraftMetaFirework extends CraftMetaItem implements FireworkMeta
{
    static final ItemMetaKey FIREWORKS;
    static final ItemMetaKey FLIGHT;
    static final ItemMetaKey EXPLOSIONS;
    static final ItemMetaKey EXPLOSION_COLORS;
    static final ItemMetaKey EXPLOSION_TYPE;
    static final ItemMetaKey EXPLOSION_TRAIL;
    static final ItemMetaKey EXPLOSION_FLICKER;
    static final ItemMetaKey EXPLOSION_FADE;
    private List<FireworkEffect> effects;
    private int power;
    
    static {
        FIREWORKS = new ItemMetaKey("Fireworks");
        FLIGHT = new ItemMetaKey("Flight", "power");
        EXPLOSIONS = new ItemMetaKey("Explosions", "firework-effects");
        EXPLOSION_COLORS = new ItemMetaKey("Colors");
        EXPLOSION_TYPE = new ItemMetaKey("Type");
        EXPLOSION_TRAIL = new ItemMetaKey("Trail");
        EXPLOSION_FLICKER = new ItemMetaKey("Flicker");
        EXPLOSION_FADE = new ItemMetaKey("FadeColors");
    }
    
    CraftMetaFirework(final CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaFirework)) {
            return;
        }
        final CraftMetaFirework that = (CraftMetaFirework)meta;
        this.power = that.power;
        if (that.hasEffects()) {
            this.effects = new ArrayList<FireworkEffect>(that.effects);
        }
    }
    
    CraftMetaFirework(final NBTTagCompound tag) {
        super(tag);
        if (!tag.hasKey(CraftMetaFirework.FIREWORKS.NBT)) {
            return;
        }
        final NBTTagCompound fireworks = tag.getCompoundTag(CraftMetaFirework.FIREWORKS.NBT);
        this.power = (0xFF & fireworks.getByte(CraftMetaFirework.FLIGHT.NBT));
        if (!fireworks.hasKey(CraftMetaFirework.EXPLOSIONS.NBT)) {
            return;
        }
        final NBTTagList fireworkEffects = fireworks.getTagList(CraftMetaFirework.EXPLOSIONS.NBT, 10);
        final ArrayList<FireworkEffect> effects2 = new ArrayList<FireworkEffect>(fireworkEffects.tagCount());
        this.effects = effects2;
        final List<FireworkEffect> effects = effects2;
        for (int i = 0; i < fireworkEffects.tagCount(); ++i) {
            effects.add(getEffect(fireworkEffects.getCompoundTagAt(i)));
        }
    }
    
    static FireworkEffect getEffect(final NBTTagCompound explosion) {
        final FireworkEffect.Builder effect = FireworkEffect.builder().flicker(explosion.getBoolean(CraftMetaFirework.EXPLOSION_FLICKER.NBT)).trail(explosion.getBoolean(CraftMetaFirework.EXPLOSION_TRAIL.NBT)).with(getEffectType(0xFF & explosion.getByte(CraftMetaFirework.EXPLOSION_TYPE.NBT)));
        int[] intArray;
        for (int length = (intArray = explosion.getIntArray(CraftMetaFirework.EXPLOSION_COLORS.NBT)).length, i = 0; i < length; ++i) {
            final int color = intArray[i];
            effect.withColor(Color.fromRGB(color));
        }
        int[] intArray2;
        for (int length2 = (intArray2 = explosion.getIntArray(CraftMetaFirework.EXPLOSION_FADE.NBT)).length, j = 0; j < length2; ++j) {
            final int color = intArray2[j];
            effect.withFade(Color.fromRGB(color));
        }
        return effect.build();
    }
    
    static NBTTagCompound getExplosion(final FireworkEffect effect) {
        final NBTTagCompound explosion = new NBTTagCompound();
        if (effect.hasFlicker()) {
            explosion.setBoolean(CraftMetaFirework.EXPLOSION_FLICKER.NBT, true);
        }
        if (effect.hasTrail()) {
            explosion.setBoolean(CraftMetaFirework.EXPLOSION_TRAIL.NBT, true);
        }
        addColors(explosion, CraftMetaFirework.EXPLOSION_COLORS, effect.getColors());
        addColors(explosion, CraftMetaFirework.EXPLOSION_FADE, effect.getFadeColors());
        explosion.setByte(CraftMetaFirework.EXPLOSION_TYPE.NBT, (byte)getNBT(effect.getType()));
        return explosion;
    }
    
    static int getNBT(final FireworkEffect.Type type) {
        switch (type) {
            case BALL: {
                return 0;
            }
            case BALL_LARGE: {
                return 1;
            }
            case STAR: {
                return 2;
            }
            case CREEPER: {
                return 3;
            }
            case BURST: {
                return 4;
            }
            default: {
                throw new AssertionError(type);
            }
        }
    }
    
    static FireworkEffect.Type getEffectType(final int nbt) {
        switch (nbt) {
            case 0: {
                return FireworkEffect.Type.BALL;
            }
            case 1: {
                return FireworkEffect.Type.BALL_LARGE;
            }
            case 2: {
                return FireworkEffect.Type.STAR;
            }
            case 3: {
                return FireworkEffect.Type.CREEPER;
            }
            case 4: {
                return FireworkEffect.Type.BURST;
            }
            default: {
                throw new AssertionError(nbt);
            }
        }
    }
    
    CraftMetaFirework(final Map<String, Object> map) {
        super(map);
        final Integer power = SerializableMeta.getObject(Integer.class, map, CraftMetaFirework.FLIGHT.BUKKIT, true);
        if (power != null) {
            this.setPower(power);
        }
        final Iterable<?> effects = SerializableMeta.getObject(/*(Class<Iterable<?>>)*/Iterable.class, map, CraftMetaFirework.EXPLOSIONS.BUKKIT, true);
        this.safelyAddEffects(effects);
    }
    
    @Override
    public boolean hasEffects() {
        return this.effects != null && !this.effects.isEmpty();
    }
    
    void safelyAddEffects(final Iterable<?> collection) {
        if (collection == null || (collection instanceof Collection && ((Collection)collection).isEmpty())) {
            return;
        }
        List<FireworkEffect> effects = this.effects;
        if (effects == null) {
            final ArrayList<FireworkEffect> effects2 = new ArrayList<FireworkEffect>();
            this.effects = effects2;
            effects = effects2;
        }
        for (final Object obj : collection) {
            if (!(obj instanceof FireworkEffect)) {
                throw new IllegalArgumentException(obj + " in " + collection + " is not a FireworkEffect");
            }
            effects.add((FireworkEffect)obj);
        }
    }
    
    @Override
    void applyToItem(final NBTTagCompound itemTag) {
        super.applyToItem(itemTag);
        if (this.isFireworkEmpty()) {
            return;
        }
        final NBTTagCompound fireworks = itemTag.getCompoundTag(CraftMetaFirework.FIREWORKS.NBT);
        itemTag.setTag(CraftMetaFirework.FIREWORKS.NBT, fireworks);
        if (this.hasEffects()) {
            final NBTTagList effects = new NBTTagList();
            for (final FireworkEffect effect : this.effects) {
                effects.appendTag(getExplosion(effect));
            }
            if (effects.tagCount() > 0) {
                fireworks.setTag(CraftMetaFirework.EXPLOSIONS.NBT, effects);
            }
        }
        if (this.hasPower()) {
            fireworks.setByte(CraftMetaFirework.FLIGHT.NBT, (byte)this.power);
        }
    }
    
    static void addColors(final NBTTagCompound compound, final ItemMetaKey key, final List<Color> colors) {
        if (colors.isEmpty()) {
            return;
        }
        final int[] colorArray = new int[colors.size()];
        int i = 0;
        for (final Color color : colors) {
            colorArray[i++] = color.asRGB();
        }
        compound.setIntArray(key.NBT, colorArray);
    }
    
    @Override
    boolean applicableTo(final Material type) {
        switch (type) {
            case FIREWORK: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isFireworkEmpty();
    }
    
    boolean isFireworkEmpty() {
        return !this.hasEffects() && !this.hasPower();
    }
    
    boolean hasPower() {
        return this.power != 0;
    }
    
    @Override
    boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaFirework) {
            final CraftMetaFirework that = (CraftMetaFirework)meta;
            if (this.hasPower()) {
                if (!that.hasPower() || this.power != that.power) {
                    return false;
                }
            }
            else if (that.hasPower()) {
                return false;
            }
            if (this.hasEffects()) {
                if (!that.hasEffects() || !this.effects.equals(that.effects)) {
                    return false;
                }
            }
            else if (that.hasEffects()) {
                return false;
            }
            return true;
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaFirework || this.isFireworkEmpty());
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.hasPower()) {
            hash = 61 * hash + this.power;
        }
        if (this.hasEffects()) {
            hash = 61 * hash + 13 * this.effects.hashCode();
        }
        return (hash != original) ? (CraftMetaFirework.class.hashCode() ^ hash) : hash;
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasEffects()) {
            builder.put(CraftMetaFirework.EXPLOSIONS.BUKKIT, ImmutableList.copyOf(this.effects));
        }
        if (this.hasPower()) {
            builder.put(CraftMetaFirework.FLIGHT.BUKKIT, this.power);
        }
        return builder;
    }
    
    @Override
    public CraftMetaFirework clone() {
        final CraftMetaFirework meta = (CraftMetaFirework)super.clone();
        if (this.effects != null) {
            meta.effects = new ArrayList<FireworkEffect>(this.effects);
        }
        return meta;
    }
    
    @Override
    public void addEffect(final FireworkEffect effect) {
        Validate.notNull((Object)effect, "Effect cannot be null");
        if (this.effects == null) {
            this.effects = new ArrayList<FireworkEffect>();
        }
        this.effects.add(effect);
    }
    
    @Override
    public void addEffects(final FireworkEffect... effects) {
        Validate.notNull((Object)effects, "Effects cannot be null");
        if (effects.length == 0) {
            return;
        }
        List<FireworkEffect> list = this.effects;
        if (list == null) {
            final ArrayList<FireworkEffect> effects2 = new ArrayList<FireworkEffect>();
            this.effects = effects2;
            list = effects2;
        }
        for (final FireworkEffect effect : effects) {
            Validate.notNull((Object)effect, "Effect cannot be null");
            list.add(effect);
        }
    }
    
    @Override
    public void addEffects(final Iterable<FireworkEffect> effects) {
        Validate.notNull((Object)effects, "Effects cannot be null");
        this.safelyAddEffects(effects);
    }
    
    @Override
    public List<FireworkEffect> getEffects() {
        return (List<FireworkEffect>)((this.effects == null) ? ImmutableList.of() : ImmutableList.copyOf((Collection)this.effects));
    }
    
    @Override
    public int getEffectsSize() {
        return (this.effects == null) ? 0 : this.effects.size();
    }
    
    @Override
    public void removeEffect(final int index) {
        if (this.effects == null) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
        this.effects.remove(index);
    }
    
    @Override
    public void clearEffects() {
        this.effects = null;
    }
    
    @Override
    public int getPower() {
        return this.power;
    }
    
    @Override
    public void setPower(final int power) {
        Validate.isTrue(power >= 0, "Power cannot be less than zero: ", (long)power);
        Validate.isTrue(power < 128, "Power cannot be more than 127: ", (long)power);
        this.power = power;
    }
}
