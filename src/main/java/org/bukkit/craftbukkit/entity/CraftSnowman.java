// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySnowman;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Snowman;

public class CraftSnowman extends CraftGolem implements Snowman
{
    public CraftSnowman(final CraftServer server, final EntitySnowman entity) {
        super(server, entity);
    }
    
    @Override
    public boolean isDerp() {
        return this.getHandle().isPumpkinEquipped();
    }
    
    @Override
    public void setDerp(final boolean derpMode) {
        this.getHandle().setPumpkinEquipped(derpMode);
    }
    
    @Override
    public EntitySnowman getHandle() {
        return (EntitySnowman)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSnowman";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SNOWMAN;
    }
}
