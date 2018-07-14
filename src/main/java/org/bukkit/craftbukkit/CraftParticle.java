// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import org.bukkit.material.MaterialData;
import org.bukkit.inventory.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import org.bukkit.Particle;

public class CraftParticle
{
    public static EnumParticleTypes toNMS(final Particle bukkit) {
        return EnumParticleTypes.valueOf(bukkit.name());
    }
    
    public static Particle toBukkit(final EnumParticleTypes nms) {
        return Particle.valueOf(nms.name());
    }
    
    public static int[] toData(final Particle particle, final Object obj) {
        if (particle.getDataType().equals(Void.class)) {
            return new int[0];
        }
        if (particle.getDataType().equals(ItemStack.class)) {
            if (obj == null) {
                return new int[2];
            }
            final ItemStack itemStack = (ItemStack)obj;
            return new int[] { itemStack.getType().getId(), itemStack.getDurability() };
        }
        else {
            if (!particle.getDataType().equals(MaterialData.class)) {
                throw new IllegalArgumentException(particle.getDataType().toString());
            }
            if (obj == null) {
                return new int[1];
            }
            final MaterialData data = (MaterialData)obj;
            return new int[] { data.getItemTypeId() + (data.getData() << 12) };
        }
    }
}
