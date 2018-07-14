// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityMagmaCube;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftSlime implements MagmaCube
{
    public CraftMagmaCube(final CraftServer server, final EntityMagmaCube entity) {
        super(server, entity);
    }
    
    @Override
    public EntityMagmaCube getHandle() {
        return (EntityMagmaCube)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftMagmaCube";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MAGMA_CUBE;
    }
}
