// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import net.minecraft.util.DamageSource;

public final class CraftDamageSource extends DamageSource
{
    public static DamageSource copyOf(final DamageSource original) {
        final CraftDamageSource newSource = new CraftDamageSource(original.damageType);
        if (original.isUnblockable()) {
            newSource.setDamageBypassesArmor();
        }
        if (original.isMagicDamage()) {
            newSource.setMagicDamage();
        }
        if (original.isExplosion()) {
            newSource.setFireDamage();
        }
        return newSource;
    }
    
    private CraftDamageSource(final String identifier) {
        super(identifier);
    }
}
