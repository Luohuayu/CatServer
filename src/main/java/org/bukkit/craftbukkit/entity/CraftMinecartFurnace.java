// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PoweredMinecart;

public class CraftMinecartFurnace extends CraftMinecart implements PoweredMinecart
{
    public CraftMinecartFurnace(final CraftServer server, final EntityMinecartFurnace entity) {
        super(server, entity);
    }
    
    @Override
    public String toString() {
        return "CraftMinecartFurnace";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MINECART_FURNACE;
    }
}
