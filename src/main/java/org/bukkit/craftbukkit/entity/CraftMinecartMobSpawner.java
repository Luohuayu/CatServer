// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.minecart.SpawnerMinecart;

final class CraftMinecartMobSpawner extends CraftMinecart implements SpawnerMinecart
{
    CraftMinecartMobSpawner(final CraftServer server, final EntityMinecartMobSpawner entity) {
        super(server, entity);
    }
    
    @Override
    public String toString() {
        return "CraftMinecartMobSpawner";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MINECART_MOB_SPAWNER;
    }
}
