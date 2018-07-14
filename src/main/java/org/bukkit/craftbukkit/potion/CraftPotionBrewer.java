// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.potion;

import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.ImmutableList;
import org.bukkit.potion.PotionData;
import com.google.common.collect.Maps;
import org.bukkit.potion.PotionEffect;
import java.util.Collection;
import org.bukkit.potion.PotionType;
import java.util.Map;
import org.bukkit.potion.PotionBrewer;

public class CraftPotionBrewer implements PotionBrewer
{
    private static final Map<PotionType, Collection<PotionEffect>> cache;
    
    static {
        cache = Maps.newHashMap();
    }
    
    @Override
    public Collection<PotionEffect> getEffects(final PotionType damage, final boolean upgraded, final boolean extended) {
        if (CraftPotionBrewer.cache.containsKey(damage)) {
            return CraftPotionBrewer.cache.get(damage);
        }
        final List<net.minecraft.potion.PotionEffect> mcEffects = net.minecraft.potion.PotionType.getPotionTypeForName(CraftPotionUtil.fromBukkit(new PotionData(damage, extended, upgraded))).getEffects();
        final ImmutableList.Builder<PotionEffect> builder = (ImmutableList.Builder<PotionEffect>)new ImmutableList.Builder();
        for (final net.minecraft.potion.PotionEffect effect : mcEffects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        CraftPotionBrewer.cache.put(damage, (Collection<PotionEffect>)builder.build());
        return CraftPotionBrewer.cache.get(damage);
    }
    
    @Override
    public Collection<PotionEffect> getEffectsFromDamage(final int damage) {
        return new ArrayList<PotionEffect>();
    }
    
    @Override
    public PotionEffect createEffect(final PotionEffectType potion, final int duration, final int amplifier) {
        return new PotionEffect(potion, potion.isInstant() ? 1 : ((int)(duration * potion.getDurationModifier())), amplifier);
    }
}
