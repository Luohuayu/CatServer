// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Vehicle;

public abstract class CraftVehicle extends CraftEntity implements Vehicle
{
    public CraftVehicle(final CraftServer server, final net.minecraft.entity.Entity entity) {
        super(server, entity);
    }
    
    @Override
    public String toString() {
        return "CraftVehicle{passenger=" + this.getPassenger() + '}';
    }
}
