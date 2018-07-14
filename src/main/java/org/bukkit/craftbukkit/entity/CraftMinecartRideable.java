// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.item.EntityMinecart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.minecart.RideableMinecart;

public class CraftMinecartRideable extends CraftMinecart implements RideableMinecart
{
    public CraftMinecartRideable(final CraftServer server, final EntityMinecart entity) {
        super(server, entity);
    }
    
    @Override
    public String toString() {
        return "CraftMinecartRideable";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MINECART;
    }
}
