// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.potion;

import org.bukkit.potion.PotionEffectType;
import net.minecraft.potion.Potion;
import org.bukkit.potion.PotionEffect;
import com.google.common.base.Preconditions;
import org.bukkit.potion.PotionData;
import com.google.common.collect.ImmutableBiMap;
import org.bukkit.potion.PotionType;
import com.google.common.collect.BiMap;

public class CraftPotionUtil
{
    private static final BiMap<PotionType, String> regular;
    private static final BiMap<PotionType, String> upgradeable;
    private static final BiMap<PotionType, String> extendable;
    
    static {
        regular = (BiMap)ImmutableBiMap.builder().put((Object)PotionType.UNCRAFTABLE, (Object)"empty").put((Object)PotionType.WATER, (Object)"water").put((Object)PotionType.MUNDANE, (Object)"mundane").put((Object)PotionType.THICK, (Object)"thick").put((Object)PotionType.AWKWARD, (Object)"awkward").put((Object)PotionType.NIGHT_VISION, (Object)"night_vision").put((Object)PotionType.INVISIBILITY, (Object)"invisibility").put((Object)PotionType.JUMP, (Object)"leaping").put((Object)PotionType.FIRE_RESISTANCE, (Object)"fire_resistance").put((Object)PotionType.SPEED, (Object)"swiftness").put((Object)PotionType.SLOWNESS, (Object)"slowness").put((Object)PotionType.WATER_BREATHING, (Object)"water_breathing").put((Object)PotionType.INSTANT_HEAL, (Object)"healing").put((Object)PotionType.INSTANT_DAMAGE, (Object)"harming").put((Object)PotionType.POISON, (Object)"poison").put((Object)PotionType.REGEN, (Object)"regeneration").put((Object)PotionType.STRENGTH, (Object)"strength").put((Object)PotionType.WEAKNESS, (Object)"weakness").put((Object)PotionType.LUCK, (Object)"luck").build();
        upgradeable = (BiMap)ImmutableBiMap.builder().put((Object)PotionType.JUMP, (Object)"strong_leaping").put((Object)PotionType.SPEED, (Object)"strong_swiftness").put((Object)PotionType.INSTANT_HEAL, (Object)"strong_healing").put((Object)PotionType.INSTANT_DAMAGE, (Object)"strong_harming").put((Object)PotionType.POISON, (Object)"strong_poison").put((Object)PotionType.REGEN, (Object)"strong_regeneration").put((Object)PotionType.STRENGTH, (Object)"strong_strength").build();
        extendable = (BiMap)ImmutableBiMap.builder().put((Object)PotionType.NIGHT_VISION, (Object)"long_night_vision").put((Object)PotionType.INVISIBILITY, (Object)"long_invisibility").put((Object)PotionType.JUMP, (Object)"long_leaping").put((Object)PotionType.FIRE_RESISTANCE, (Object)"long_fire_resistance").put((Object)PotionType.SPEED, (Object)"long_swiftness").put((Object)PotionType.SLOWNESS, (Object)"long_slowness").put((Object)PotionType.WATER_BREATHING, (Object)"long_water_breathing").put((Object)PotionType.POISON, (Object)"long_poison").put((Object)PotionType.REGEN, (Object)"long_regeneration").put((Object)PotionType.STRENGTH, (Object)"long_strength").put((Object)PotionType.WEAKNESS, (Object)"long_weakness").build();
    }
    
    public static String fromBukkit(final PotionData data) {
        String type;
        if (data.isUpgraded()) {
            type = (String)CraftPotionUtil.upgradeable.get((Object)data.getType());
        }
        else if (data.isExtended()) {
            type = (String)CraftPotionUtil.extendable.get((Object)data.getType());
        }
        else {
            type = (String)CraftPotionUtil.regular.get((Object)data.getType());
        }
        Preconditions.checkNotNull((Object)type, (Object)("Unknown potion type from data " + data));
        return "minecraft:" + type;
    }
    
    public static PotionData toBukkit(String type) {
        if (type == null) {
            return new PotionData(PotionType.UNCRAFTABLE, false, false);
        }
        if (type.startsWith("minecraft:")) {
            type = type.substring(10);
        }
        PotionType potionType = null;
        potionType = (PotionType)CraftPotionUtil.extendable.inverse().get((Object)type);
        if (potionType != null) {
            return new PotionData(potionType, true, false);
        }
        potionType = (PotionType)CraftPotionUtil.upgradeable.inverse().get((Object)type);
        if (potionType != null) {
            return new PotionData(potionType, false, true);
        }
        potionType = (PotionType)CraftPotionUtil.regular.inverse().get((Object)type);
        if (potionType != null) {
            return new PotionData(potionType, false, false);
        }
        return new PotionData(PotionType.UNCRAFTABLE, false, false);
    }
    
    public static net.minecraft.potion.PotionEffect fromBukkit(final PotionEffect effect) {
        final Potion type = Potion.getPotionById(effect.getType().getId());
        return new net.minecraft.potion.PotionEffect(type, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
    }
    
    public static PotionEffect toBukkit(final net.minecraft.potion.PotionEffect effect) {
        final PotionEffectType type = PotionEffectType.getById(Potion.getIdFromPotion(effect.getPotion()));
        final int amp = effect.getAmplifier();
        final int duration = effect.getDuration();
        final boolean ambient = effect.getIsAmbient();
        final boolean particles = effect.doesShowParticles();
        return new PotionEffect(type, duration, amp, ambient, particles);
    }
    
    public static boolean equals(final Potion mobEffect, final PotionEffectType type) {
        final PotionEffectType typeV = PotionEffectType.getById(Potion.getIdFromPotion(mobEffect));
        return typeV.equals(type);
    }
}
