package org.bukkit.craftbukkit.v1_20_R1.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.potion.CraftPotionUtil;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class CraftAreaEffectCloud extends CraftEntity implements AreaEffectCloud {

    public CraftAreaEffectCloud(CraftServer server, net.minecraft.world.entity.AreaEffectCloud entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.AreaEffectCloud getHandle() {
        return (net.minecraft.world.entity.AreaEffectCloud) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftAreaEffectCloud";
    }

    @Override
    public int getDuration() {
        return getHandle().getDuration();
    }

    @Override
    public void setDuration(int duration) {
        getHandle().setDuration(duration);
    }

    @Override
    public int getWaitTime() {
        return getHandle().getWaitTime(); // CatServer - waitTime -> getWaitTime()
    }

    @Override
    public void setWaitTime(int waitTime) {
        getHandle().setWaitTime(waitTime);
    }

    @Override
    public int getReapplicationDelay() {
        return getHandle().reapplicationDelay;
    }

    @Override
    public void setReapplicationDelay(int delay) {
        getHandle().reapplicationDelay = delay;
    }

    @Override
    public int getDurationOnUse() {
        return getHandle().getDurationOnUse(); // CatServer - durationOnUse -> getDurationOnUse()
    }

    @Override
    public void setDurationOnUse(int duration) {
        getHandle().setDurationOnUse(duration); // CatServer - durationOnUse -> setDurationOnUse()
    }

    @Override
    public float getRadius() {
        return getHandle().getRadius();
    }

    @Override
    public void setRadius(float radius) {
        getHandle().setRadius(radius);
    }

    @Override
    public float getRadiusOnUse() {
        return getHandle().getRadiusOnUse(); // CatServer - radiusOnUse -> getRadiusOnUse()
    }

    @Override
    public void setRadiusOnUse(float radius) {
        getHandle().setRadiusOnUse(radius);
    }

    @Override
    public float getRadiusPerTick() {
        return getHandle().getRadiusPerTick(); // CatServer - radiusPerTick -> getRadiusPerTick()
    }

    @Override
    public void setRadiusPerTick(float radius) {
        getHandle().setRadiusPerTick(radius);
    }

    @Override
    public Particle getParticle() {
        return CraftParticle.toBukkit(getHandle().getParticle());
    }

    @Override
    public void setParticle(Particle particle) {
        setParticle(particle, null);
    }

    @Override
    public <T> void setParticle(Particle particle, T data) {
        getHandle().setParticle(CraftParticle.toNMS(particle, data));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(getHandle().getColor());
    }

    @Override
    public void setColor(Color color) {
        getHandle().setFixedColor(color.asRGB());
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        int effectId = effect.getType().getId();
        MobEffectInstance existing = null;
        for (MobEffectInstance mobEffect : getHandle().effects) {
            if (MobEffect.getId(mobEffect.getEffect()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            getHandle().effects.remove(existing);
        }
        getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        getHandle().updateColor();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        getHandle().effects.clear();
        getHandle().updateColor();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (MobEffectInstance effect : getHandle().effects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (MobEffectInstance effect : getHandle().effects) {
            if (CraftPotionUtil.equals(effect.getEffect(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !getHandle().effects.isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        int effectId = effect.getId();
        MobEffectInstance existing = null;
        for (MobEffectInstance mobEffect : getHandle().effects) {
            if (MobEffect.getId(mobEffect.getEffect()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        getHandle().effects.remove(existing);
        getHandle().updateColor();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Preconditions.checkArgument(data != null, "PotionData cannot be null");
        getHandle().setPotion(BuiltInRegistries.POTION.get(new ResourceLocation(CraftPotionUtil.fromBukkit(data))));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit((BuiltInRegistries.POTION.getKey(getHandle().potion)).toString());
    }

    @Override
    public ProjectileSource getSource() {
        net.minecraft.world.entity.LivingEntity source = getHandle().getOwner();
        return (source == null) ? null : (LivingEntity) source.getBukkitEntity();
    }

    @Override
    public void setSource(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity craftLivingEntity) {
            getHandle().setOwner(craftLivingEntity.getHandle());
        } else {
            getHandle().setOwner(null);
        }
    }
}
