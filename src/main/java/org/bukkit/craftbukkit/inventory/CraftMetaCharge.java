// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Map;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.FireworkEffectMeta;

//@DelegateDeserialization(SerializableMeta.class)
class CraftMetaCharge extends CraftMetaItem implements FireworkEffectMeta
{
    static final ItemMetaKey EXPLOSION;
    private FireworkEffect effect;
    
    static {
        EXPLOSION = new ItemMetaKey("Explosion", "firework-effect");
    }
    
    CraftMetaCharge(final CraftMetaItem meta) {
        super(meta);
        if (meta instanceof CraftMetaCharge) {
            this.effect = ((CraftMetaCharge)meta).effect;
        }
    }
    
    CraftMetaCharge(final Map<String, Object> map) {
        super(map);
        this.setEffect(SerializableMeta.getObject(FireworkEffect.class, map, CraftMetaCharge.EXPLOSION.BUKKIT, true));
    }
    
    CraftMetaCharge(final NBTTagCompound tag) {
        super(tag);
        if (tag.hasKey(CraftMetaCharge.EXPLOSION.NBT)) {
            this.effect = CraftMetaFirework.getEffect(tag.getCompoundTag(CraftMetaCharge.EXPLOSION.NBT));
        }
    }
    
    @Override
    public void setEffect(final FireworkEffect effect) {
        this.effect = effect;
    }
    
    @Override
    public boolean hasEffect() {
        return this.effect != null;
    }
    
    @Override
    public FireworkEffect getEffect() {
        return this.effect;
    }
    
    @Override
    void applyToItem(final NBTTagCompound itemTag) {
        super.applyToItem(itemTag);
        if (this.hasEffect()) {
            itemTag.setTag(CraftMetaCharge.EXPLOSION.NBT, CraftMetaFirework.getExplosion(this.effect));
        }
    }
    
    @Override
    boolean applicableTo(final Material type) {
        switch (type) {
            case FIREWORK_CHARGE: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && !this.hasChargeMeta();
    }
    
    boolean hasChargeMeta() {
        return this.hasEffect();
    }
    
    @Override
    boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCharge) {
            final CraftMetaCharge that = (CraftMetaCharge)meta;
            return this.hasEffect() ? (that.hasEffect() && this.effect.equals(that.effect)) : (!that.hasEffect());
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCharge || !this.hasChargeMeta());
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.hasEffect()) {
            hash = 61 * hash + this.effect.hashCode();
        }
        return (hash != original) ? (CraftMetaCharge.class.hashCode() ^ hash) : hash;
    }
    
    @Override
    public CraftMetaCharge clone() {
        return (CraftMetaCharge)super.clone();
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasEffect()) {
            builder.put(CraftMetaCharge.EXPLOSION.BUKKIT, this.effect);
        }
        return builder;
    }
}
