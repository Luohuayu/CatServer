package org.spigotmc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;

public class TrackingRange
{

    /**
     * Gets the range an entity should be 'tracked' by players and visible in
     * the client.
     *
     * @param entity
     * @param defaultRange Default range defined by Mojang
     * @return
     */
    public static int getEntityTrackingRange(Entity entity, int defaultRange)
    {
        SpigotWorldConfig config = entity.level.spigotConfig;
        if ( entity instanceof PlayerEntity )
        {
            return config.playerTrackingRange;
        }  else if ( entity.activationType == ActivationRange.ActivationType.MONSTER || entity.activationType == ActivationRange.ActivationType.RAIDER )
        {
            return config.monsterTrackingRange;
        } else if ( entity instanceof GhastEntity )
        {
            if ( config.monsterTrackingRange > config.monsterActivationRange )
            {
                return config.monsterTrackingRange;
            } else
            {
                return config.monsterActivationRange;
            }
        } else if ( entity.activationType == ActivationRange.ActivationType.ANIMAL )
        {
            return config.animalTrackingRange;
        } else if ( entity instanceof ItemFrameEntity || entity instanceof PaintingEntity || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity )
        {
            return config.miscTrackingRange;
        } else
        {
            return config.otherTrackingRange;
        }
    }
}
