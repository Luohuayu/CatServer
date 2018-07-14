// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Silverfish;

public class CraftSilverfish extends CraftMonster implements Silverfish
{
    public CraftSilverfish(final CraftServer server, final EntitySilverfish entity) {
        super(server, entity);
    }
    
    @Override
    public EntitySilverfish getHandle() {
        return (EntitySilverfish)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSilverfish";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SILVERFISH;
    }
}
