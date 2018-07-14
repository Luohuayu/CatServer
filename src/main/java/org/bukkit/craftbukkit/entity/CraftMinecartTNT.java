// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.minecart.ExplosiveMinecart;

final class CraftMinecartTNT extends CraftMinecart implements ExplosiveMinecart
{
    CraftMinecartTNT(final CraftServer server, final EntityMinecartTNT entity) {
        super(server, entity);
    }
    
    @Override
    public String toString() {
        return "CraftMinecartTNT";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MINECART_TNT;
    }
}
