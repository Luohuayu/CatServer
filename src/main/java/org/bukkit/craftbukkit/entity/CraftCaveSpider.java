// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityCaveSpider;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.CaveSpider;

public class CraftCaveSpider extends CraftSpider implements CaveSpider
{
    public CraftCaveSpider(final CraftServer server, final EntityCaveSpider entity) {
        super(server, entity);
    }
    
    @Override
    public EntityCaveSpider getHandle() {
        return (EntityCaveSpider)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftCaveSpider";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.CAVE_SPIDER;
    }
}
