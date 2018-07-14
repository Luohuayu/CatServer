// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityBlaze;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Blaze;

public class CraftBlaze extends CraftMonster implements Blaze
{
    public CraftBlaze(final CraftServer server, final EntityBlaze entity) {
        super(server, entity);
    }
    
    @Override
    public EntityBlaze getHandle() {
        return (EntityBlaze)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftBlaze";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.BLAZE;
    }
}
