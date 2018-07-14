// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.potion.PotionType;
import java.util.Iterator;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import net.minecraft.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.TippedArrow;

public class CraftTippedArrow extends CraftArrow implements TippedArrow
{
    public CraftTippedArrow(final CraftServer server, final EntityTippedArrow entity) {
        super(server, entity);
    }
    
    @Override
    public EntityTippedArrow getHandle() {
        return (EntityTippedArrow)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftTippedArrow";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.TIPPED_ARROW;
    }
    
    @Override
    public boolean addCustomEffect(final PotionEffect effect, final boolean override) {
        final int effectId = effect.getType().getId();
        net.minecraft.potion.PotionEffect existing = null;
        for (final net.minecraft.potion.PotionEffect mobEffect : this.getHandle().customPotionEffects) {
            if (Potion.getIdFromPotion(mobEffect.getPotion()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            this.getHandle().customPotionEffects.remove(existing);
        }
        this.getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        this.getHandle().refreshEffects();
        return true;
    }
    
    @Override
    public void clearCustomEffects() {
        Validate.isTrue(this.getBasePotionData().getType() != PotionType.UNCRAFTABLE, "Tipped Arrows must have at least 1 effect");
        this.getHandle().customPotionEffects.clear();
        this.getHandle().refreshEffects();
    }
    
    @Override
    public List<PotionEffect> getCustomEffects() {
        final ImmutableList.Builder<PotionEffect> builder = /*(ImmutableList.Builder<PotionEffect>)*/ImmutableList.builder();
        for (final net.minecraft.potion.PotionEffect effect : this.getHandle().customPotionEffects) {
            builder.add(/*(Object)*/CraftPotionUtil.toBukkit(effect));
        }
        return (List<PotionEffect>)builder.build();
    }
    
    @Override
    public boolean hasCustomEffect(final PotionEffectType type) {
        for (final net.minecraft.potion.PotionEffect effect : this.getHandle().customPotionEffects) {
            if (CraftPotionUtil.equals(effect.getPotion(), type)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasCustomEffects() {
        return !this.getHandle().customPotionEffects.isEmpty();
    }
    
    @Override
    public boolean removeCustomEffect(final PotionEffectType effect) {
        final int effectId = effect.getId();
        net.minecraft.potion.PotionEffect existing = null;
        for (final net.minecraft.potion.PotionEffect mobEffect : this.getHandle().customPotionEffects) {
            if (Potion.getIdFromPotion(mobEffect.getPotion()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        Validate.isTrue(this.getBasePotionData().getType() != PotionType.UNCRAFTABLE || !this.getHandle().customPotionEffects.isEmpty(), "Tipped Arrows must have at least 1 effect");
        this.getHandle().customPotionEffects.remove(existing);
        this.getHandle().refreshEffects();
        return true;
    }
    
    @Override
    public void setBasePotionData(final PotionData data) {
        Validate.notNull((Object)data, "PotionData cannot be null");
        Validate.isTrue(data.getType() != PotionType.UNCRAFTABLE || !this.getHandle().customPotionEffects.isEmpty(), "Tipped Arrows must have at least 1 effect");
        this.getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }
    
    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(this.getHandle().getType());
    }
}
