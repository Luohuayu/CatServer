// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityShulker;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Shulker;

public class CraftShulker extends CraftGolem implements Shulker
{
    public CraftShulker(final CraftServer server, final EntityShulker entity) {
        super(server, entity);
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SHULKER;
    }
    
    @Override
    public EntityShulker getHandle() {
        return (EntityShulker)this.entity;
    }
}
