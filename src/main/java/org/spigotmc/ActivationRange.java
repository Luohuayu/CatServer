package org.spigotmc;

import java.util.Collection;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.bukkit.craftbukkit.v1_16_R3.SpigotTimings;

public class ActivationRange
{

    public enum ActivationType
    {
        MONSTER,
        ANIMAL,
        RAIDER,
        MISC;

        AxisAlignedBB boundingBox = new AxisAlignedBB( 0, 0, 0, 0, 0, 0 );
    }

    static AxisAlignedBB maxBB = new AxisAlignedBB( 0, 0, 0, 0, 0, 0 );

    /**
     * Initializes an entities type on construction to specify what group this
     * entity is in for activation ranges.
     *
     * @param entity
     * @return group id
     */
    public static ActivationType initializeEntityActivationType(Entity entity)
    {
        if ( entity instanceof AbstractRaiderEntity )
        {
            return ActivationType.RAIDER;
        } else if ( entity instanceof MobEntity || entity instanceof SlimeEntity )
        {
            return ActivationType.MONSTER;
        } else if ( entity instanceof CreatureEntity || entity instanceof AmbientEntity )
        {
            return ActivationType.ANIMAL;
        } else
        {
            return ActivationType.MISC;
        }
    }

    /**
     * These entities are excluded from Activation range checks.
     *
     * @param entity
     * @param config
     * @return boolean If it should always tick.
     */
    public static boolean initializeEntityActivationState(Entity entity, SpigotWorldConfig config)
    {
        if ( ( entity.activationType == ActivationType.MISC && config.miscActivationRange == 0 )
                || ( entity.activationType == ActivationType.RAIDER && config.raiderActivationRange == 0 )
                || ( entity.activationType == ActivationType.ANIMAL && config.animalActivationRange == 0 )
                || ( entity.activationType == ActivationType.MONSTER && config.monsterActivationRange == 0 )
                || entity instanceof PlayerEntity
            || entity instanceof ThrowableEntity
            || entity instanceof EnderDragonEntity
            || entity instanceof EnderDragonPartEntity
            || entity instanceof WitherEntity
            || entity instanceof FireballEntity
            || entity instanceof LightningBoltEntity
            || entity instanceof TNTEntity
            || entity instanceof EnderCrystalEntity
            || entity instanceof FireworkRocketEntity
            || entity instanceof TridentEntity )
        {
            return true;
        }

        return false;
    }

    /**
     * Find what entities are in range of the players in the world and set
     * active if in range.
     *
     * @param world
     */
    public static void activateEntities(World world)
    {
        SpigotTimings.entityActivationCheckTimer.startTiming();
        final int miscActivationRange = world.spigotConfig.miscActivationRange;
        final int raiderActivationRange = world.spigotConfig.raiderActivationRange;
        final int animalActivationRange = world.spigotConfig.animalActivationRange;
        final int monsterActivationRange = world.spigotConfig.monsterActivationRange;

        int maxRange = Math.max( monsterActivationRange, animalActivationRange );
        maxRange = Math.max( maxRange, raiderActivationRange );
        maxRange = Math.max( maxRange, miscActivationRange );
        maxRange = Math.min( ( world.spigotConfig.viewDistance << 4 ) - 8, maxRange );

        for ( PlayerEntity player : world.players() )
        {

            player.activatedTick = MinecraftServer.currentTick;
            maxBB = player.getBoundingBox().inflate( maxRange, 256, maxRange );
            ActivationType.MISC.boundingBox = player.getBoundingBox().inflate( miscActivationRange, 256, miscActivationRange );
            ActivationType.RAIDER.boundingBox = player.getBoundingBox().inflate( raiderActivationRange, 256, raiderActivationRange );
            ActivationType.ANIMAL.boundingBox = player.getBoundingBox().inflate( animalActivationRange, 256, animalActivationRange );
            ActivationType.MONSTER.boundingBox = player.getBoundingBox().inflate( monsterActivationRange, 256, monsterActivationRange );

            int i = MathHelper.floor( maxBB.minX / 16.0D );
            int j = MathHelper.floor( maxBB.maxX / 16.0D );
            int k = MathHelper.floor( maxBB.minZ / 16.0D );
            int l = MathHelper.floor( maxBB.maxZ / 16.0D );

            for ( int i1 = i; i1 <= j; ++i1 )
            {
                for ( int j1 = k; j1 <= l; ++j1 )
                {
                    if ( world.getWorld().isChunkLoaded( i1, j1 ) )
                    {
                        activateChunkEntities( world.getChunk( i1, j1 ) );
                    }
                }
            }
        }
        SpigotTimings.entityActivationCheckTimer.stopTiming();
    }

    /**
     * Checks for the activation state of all entities in this chunk.
     *
     * @param chunk
     */
    private static void activateChunkEntities(Chunk chunk)
    {
        for ( ClassInheritanceMultiMap<Entity> slice : chunk.entitySections )
        {
            for ( Entity entity : (Collection<Entity>) slice )
            {
                if ( MinecraftServer.currentTick > entity.activatedTick )
                {
                    if ( entity.defaultActivationState )
                    {
                        entity.activatedTick = MinecraftServer.currentTick;
                        continue;
                    }
                    if ( entity.activationType.boundingBox.intersects( entity.getBoundingBox() ) )
                    {
                        entity.activatedTick = MinecraftServer.currentTick;
                    }
                }
            }
        }
    }

    /**
     * If an entity is not in range, do some more checks to see if we should
     * give it a shot.
     *
     * @param entity
     * @return
     */
    public static boolean checkEntityImmunities(Entity entity)
    {
        // quick checks.
        if ( entity.wasTouchingWater || entity.remainingFireTicks > 0 )
        {
            return true;
        }
        if ( !( entity instanceof ArrowEntity ) )
        {
            if ( !entity.isOnGround() || !entity.passengers.isEmpty() || entity.isPassenger() )
            {
                return true;
            }
        } else if ( !( (ArrowEntity) entity ).inGround )
        {
            return true;
        }
        // special cases.
        if ( entity instanceof LivingEntity )
        {
            LivingEntity living = (LivingEntity) entity;
            if ( /*TODO: Missed mapping? living.attackTicks > 0 || */ living.hurtTime > 0 || living.activeEffects.size() > 0 )
            {
                return true;
            }
            if ( entity instanceof CreatureEntity && ( (CreatureEntity) entity ).getTarget() != null )
            {
                return true;
            }
            if ( entity instanceof VillagerEntity && ( (VillagerEntity) entity ).canBreed() )
            {
                return true;
            }
            if ( entity instanceof AnimalEntity )
            {
                AnimalEntity animal = (AnimalEntity) entity;
                if ( animal.isBaby() || animal.isInLove() )
                {
                    return true;
                }
                if ( entity instanceof SheepEntity && ( (SheepEntity) entity ).isSheared() )
                {
                    return true;
                }
            }
            if (entity instanceof CreeperEntity && ((CreeperEntity) entity).isIgnited()) { // isExplosive
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the entity is active for this tick.
     *
     * @param entity
     * @return
     */
    public static boolean checkIfActive(Entity entity)
    {
        SpigotTimings.checkIfActiveTimer.startTiming();
        // Never safe to skip fireworks or entities not yet added to chunk
        if ( !entity.inChunk || entity instanceof FireworkRocketEntity ) {
            SpigotTimings.checkIfActiveTimer.stopTiming();
            return true;
        }

        boolean isActive = entity.activatedTick >= MinecraftServer.currentTick || entity.defaultActivationState;

        // Should this entity tick?
        if ( !isActive )
        {
            if ( ( MinecraftServer.currentTick - entity.activatedTick - 1 ) % 20 == 0 )
            {
                // Check immunities every 20 ticks.
                if ( checkEntityImmunities( entity ) )
                {
                    // Triggered some sort of immunity, give 20 full ticks before we check again.
                    entity.activatedTick = MinecraftServer.currentTick + 20;
                }
                isActive = true;
            }
            // Add a little performance juice to active entities. Skip 1/4 if not immune.
        } else if ( !entity.defaultActivationState && entity.tickCount % 4 == 0 && !checkEntityImmunities( entity ) )
        {
            isActive = false;
        }
        SpigotTimings.checkIfActiveTimer.stopTiming();
        return isActive;
    }
}
