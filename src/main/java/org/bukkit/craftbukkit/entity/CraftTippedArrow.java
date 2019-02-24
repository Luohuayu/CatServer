package org.bukkit.craftbukkit.entity;

import java.util.List;

import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.potion.Potion;
import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TippedArrow;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.google.common.collect.ImmutableList;

public class CraftTippedArrow extends CraftArrow implements TippedArrow {

    public CraftTippedArrow(CraftServer server, EntityTippedArrow entity) {
        super(server, entity);
    }

    @Override
    public EntityTippedArrow getHandle() {
        return (EntityTippedArrow) entity;
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
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        int effectId = effect.getType().getId();
        net.minecraft.potion.PotionEffect existing = null;
        for (net.minecraft.potion.PotionEffect mobEffect : getHandle().customPotionEffects) {
            if (Potion.getIdFromPotion(mobEffect.getPotion()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            getHandle().customPotionEffects.remove(existing);
        }
        getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        Validate.isTrue(getBasePotionData().getType() != PotionType.UNCRAFTABLE, "Tipped Arrows must have at least 1 effect");
        getHandle().customPotionEffects.clear();
        getHandle().refreshEffects();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (net.minecraft.potion.PotionEffect effect : getHandle().customPotionEffects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (net.minecraft.potion.PotionEffect effect : getHandle().customPotionEffects) {
            if (CraftPotionUtil.equals(effect.getPotion(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !getHandle().customPotionEffects.isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        int effectId = effect.getId();
        net.minecraft.potion.PotionEffect existing = null;
        for (net.minecraft.potion.PotionEffect mobEffect : getHandle().customPotionEffects) {
            if (Potion.getIdFromPotion(mobEffect.getPotion()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        Validate.isTrue(getBasePotionData().getType() != PotionType.UNCRAFTABLE || !getHandle().customPotionEffects.isEmpty(), "Tipped Arrows must have at least 1 effect");
        getHandle().customPotionEffects.remove(existing);
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull(data, "PotionData cannot be null");
        Validate.isTrue(data.getType() != PotionType.UNCRAFTABLE || !getHandle().customPotionEffects.isEmpty(), "Tipped Arrows must have at least 1 effect");
        getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(getHandle().getType());
    }

    @Override
    public void setColor(Color color) {
        getHandle().setFixedColor(color.asRGB());
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(getHandle().getColor());
    }
}
