// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LeashHitch;

public class CraftLeash extends CraftHanging implements LeashHitch
{
    public CraftLeash(final CraftServer server, final EntityLeashKnot entity) {
        super(server, entity);
    }
    
    @Override
    public EntityLeashKnot getHandle() {
        return (EntityLeashKnot)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftLeash";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.LEASH_HITCH;
    }
}
