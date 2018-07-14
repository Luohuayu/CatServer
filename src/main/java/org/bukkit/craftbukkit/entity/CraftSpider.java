// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Spider;

public class CraftSpider extends CraftMonster implements Spider
{
    public CraftSpider(final CraftServer server, final EntitySpider entity) {
        super(server, entity);
    }
    
    @Override
    public EntitySpider getHandle() {
        return (EntitySpider)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSpider";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SPIDER;
    }
}
