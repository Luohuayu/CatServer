// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Weather;

public class CraftWeather extends CraftEntity implements Weather
{
    public CraftWeather(final CraftServer server, final EntityWeatherEffect entity) {
        super(server, entity);
    }
    
    @Override
    public EntityWeatherEffect getHandle() {
        return (EntityWeatherEffect)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftWeather";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.WEATHER;
    }
}
