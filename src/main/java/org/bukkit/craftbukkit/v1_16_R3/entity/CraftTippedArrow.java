package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftTippedArrow extends CraftArrow implements Arrow {

    public CraftTippedArrow(CraftServer server, ArrowEntity entity) {
        super(server, entity);
    }

    @Override
    public ArrowEntity getHandle() {
        return (ArrowEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftTippedArrow";
    }

    @Override
    public EntityType getType() {
        return EntityType.ARROW;
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        int effectId = effect.getType().getId();
        EffectInstance existing = null;
        for (EffectInstance mobEffect : getHandle().effects) {
            if (Effect.getId(mobEffect.getEffect()) == effectId) {
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
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        getHandle().effects.clear();
        getHandle().refreshEffects();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (EffectInstance effect : getHandle().effects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (EffectInstance effect : getHandle().effects) {
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
        EffectInstance existing = null;
        for (EffectInstance mobEffect : getHandle().effects) {
            if (Effect.getId(mobEffect.getEffect()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        getHandle().effects.remove(existing);
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull(data, "PotionData cannot be null");
        getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(getHandle().getTypeCB());
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
