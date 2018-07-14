// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityEndermite;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Endermite;

public class CraftEndermite extends CraftMonster implements Endermite
{
    public CraftEndermite(final CraftServer server, final EntityEndermite entity) {
        super(server, entity);
    }
    
    @Override
    public String toString() {
        return "CraftEndermite";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.ENDERMITE;
    }
}
