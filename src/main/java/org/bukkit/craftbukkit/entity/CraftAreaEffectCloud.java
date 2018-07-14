// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLivingBase;
import org.bukkit.projectiles.ProjectileSource;
import org.apache.commons.lang.Validate;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Iterator;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import net.minecraft.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.Color;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AreaEffectCloud;

public class CraftAreaEffectCloud extends CraftEntity implements AreaEffectCloud
{
    public CraftAreaEffectCloud(final CraftServer server, final EntityAreaEffectCloud entity) {
        super(server, entity);
    }
    
    @Override
    public EntityAreaEffectCloud getHandle() {
        return (EntityAreaEffectCloud)super.getHandle();
    }
    
    @Override
    public EntityType getType() {
        return EntityType.AREA_EFFECT_CLOUD;
    }
    
    @Override
    public int getDuration() {
        return this.getHandle().getDuration();
    }
    
    @Override
    public void setDuration(final int duration) {
        this.getHandle().setDuration(duration);
    }
    
    @Override
    public int getWaitTime() {
        return this.getHandle().waitTime;
    }
    
    @Override
    public void setWaitTime(final int waitTime) {
        this.getHandle().setWaitTime(waitTime);
    }
    
    @Override
    public int getReapplicationDelay() {
        return this.getHandle().reapplicationDelay;
    }
    
    @Override
    public void setReapplicationDelay(final int delay) {
        this.getHandle().reapplicationDelay = delay;
    }
    
    @Override
    public int getDurationOnUse() {
        return this.getHandle().durationOnUse;
    }
    
    @Override
    public void setDurationOnUse(final int duration) {
        this.getHandle().durationOnUse = duration;
    }
    
    @Override
    public float getRadius() {
        return this.getHandle().getRadius();
    }
    
    @Override
    public void setRadius(final float radius) {
        this.getHandle().setRadius(radius);
    }
    
    @Override
    public float getRadiusOnUse() {
        return this.getHandle().radiusOnUse;
    }
    
    @Override
    public void setRadiusOnUse(final float radius) {
        this.getHandle().setRadiusOnUse(radius);
    }
    
    @Override
    public float getRadiusPerTick() {
        return this.getHandle().radiusPerTick;
    }
    
    @Override
    public void setRadiusPerTick(final float radius) {
        this.getHandle().setRadiusPerTick(radius);
    }
    
    @Override
    public Particle getParticle() {
        return CraftParticle.toBukkit(this.getHandle().getParticle());
    }
    
    @Override
    public void setParticle(final Particle particle) {
        this.getHandle().setParticle(CraftParticle.toNMS(particle));
    }
    
    @Override
    public Color getColor() {
        return Color.fromRGB(this.getHandle().getColor());
    }
    
    @Override
    public void setColor(final Color color) {
        this.getHandle().setColor(color.asRGB());
    }
    
    @Override
    public boolean addCustomEffect(final PotionEffect effect, final boolean override) {
        final int effectId = effect.getType().getId();
        net.minecraft.potion.PotionEffect existing = null;
        for (final net.minecraft.potion.PotionEffect mobEffect : this.getHandle().effects) {
            if (Potion.getIdFromPotion(mobEffect.getPotion()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            this.getHandle().effects.remove(existing);
        }
        this.getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        this.getHandle().refreshEffects();
        return true;
    }
    
    @Override
    public void clearCustomEffects() {
        this.getHandle().effects.clear();
        this.getHandle().refreshEffects();
    }
    
    @Override
    public List<PotionEffect> getCustomEffects() {
        final ImmutableList.Builder<PotionEffect> builder = /*(ImmutableList.Builder<PotionEffect>)*/ImmutableList.builder();
        for (final net.minecraft.potion.PotionEffect effect : this.getHandle().effects) {
            builder.add(/*(Object)*/CraftPotionUtil.toBukkit(effect));
        }
        return (List<PotionEffect>)builder.build();
    }
    
    @Override
    public boolean hasCustomEffect(final PotionEffectType type) {
        for (final net.minecraft.potion.PotionEffect effect : this.getHandle().effects) {
            if (CraftPotionUtil.equals(effect.getPotion(), type)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasCustomEffects() {
        return !this.getHandle().effects.isEmpty();
    }
    
    @Override
    public boolean removeCustomEffect(final PotionEffectType effect) {
        final int effectId = effect.getId();
        net.minecraft.potion.PotionEffect existing = null;
        for (final net.minecraft.potion.PotionEffect mobEffect : this.getHandle().effects) {
            if (Potion.getIdFromPotion(mobEffect.getPotion()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        this.getHandle().effects.remove(existing);
        this.getHandle().refreshEffects();
        return true;
    }
    
    @Override
    public void setBasePotionData(final PotionData data) {
        Validate.notNull((Object)data, "PotionData cannot be null");
        this.getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }
    
    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(this.getHandle().getType());
    }
    
    @Override
    public ProjectileSource getSource() {
        return this.getHandle().projectileSource;
    }
    
    @Override
    public void setSource(final ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().setOwner(((CraftLivingEntity)shooter).getHandle());
        }
        else {
            this.getHandle().setOwner(null);
        }
        this.getHandle().projectileSource = shooter;
    }
}
