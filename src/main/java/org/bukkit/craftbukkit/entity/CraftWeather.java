package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.effect.EntityWeatherEffect;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Weather;

public class CraftWeather extends CraftEntity implements Weather {
    public CraftWeather(final CraftServer server, final EntityWeatherEffect entity) {
        super(server, entity);
    }

    @Override
    public EntityWeatherEffect getHandle() {
        return (EntityWeatherEffect) entity;
    }

    @Override
    public String toString() {
        return "CraftWeather";
    }

    public EntityType getType() {
        return EntityType.WEATHER;
    }
}
