// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.EnumHelper;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.permissions.ServerOperator;
import net.minecraft.util.DamageSource;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.EntityEffect;
import com.google.common.base.Preconditions;
import org.bukkit.Server;
import com.google.common.base.Predicate;

import luohuayu.CatServer.entity.CraftCustomEntity;
import luohuayu.CatServer.entity.CustomCraftProjectile;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.entity.Entity;
import net.minecraftforge.common.util.FakePlayerFactory;

public abstract class CraftEntity implements Entity
{
    private static PermissibleBase perm;
    protected final CraftServer server;
    protected net.minecraft.entity.Entity entity;
    private EntityDamageEvent lastDamageEvent;
    
    public CraftEntity(final CraftServer server, final net.minecraft.entity.Entity entity) {
        this.server = server;
        this.entity = entity;
    }
    
/*
    public static CraftEntity getEntity(final CraftServer server, final net.minecraft.entity.Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity instanceof EntityPlayer) {
                if (entity instanceof EntityPlayerMP) {
                    return new CraftPlayer(server, (EntityPlayerMP)entity);
                }
                // Svarka start
                //return new CraftHumanEntity(server, (EntityPlayer)entity);
                return new CraftPlayer(server,FakePlayerFactory.get(DimensionManager.getWorld(entity.worldObj.provider.getDimension()), ((EntityPlayer) entity).getGameProfile()));
                // Svarka end
            }
            else if (entity instanceof EntityWaterMob) {
                if (entity instanceof EntitySquid) {
                    return new CraftSquid(server, (EntitySquid)entity);
                }
                return new CraftWaterMob(server, (EntityWaterMob)entity);
            }
            else if (entity instanceof EntityCreature) {
                if (entity instanceof EntityAnimal) {
                    if (entity instanceof EntityChicken) {
                        return new CraftChicken(server, (EntityChicken)entity);
                    }
                    if (entity instanceof EntityCow) {
                        if (entity instanceof EntityMooshroom) {
                            return new CraftMushroomCow(server, (EntityMooshroom)entity);
                        }
                        return new CraftCow(server, (EntityCow)entity);
                    }
                    else {
                        if (entity instanceof EntityPig) {
                            return new CraftPig(server, (EntityPig)entity);
                        }
                        if (entity instanceof EntityTameable) {
                            if (entity instanceof EntityWolf) {
                                return new CraftWolf(server, (EntityWolf)entity);
                            }
                            if (entity instanceof EntityOcelot) {
                                return new CraftOcelot(server, (EntityOcelot)entity);
                            }
                        }
                        else {
                            if (entity instanceof EntitySheep) {
                                return new CraftSheep(server, (EntitySheep)entity);
                            }
                            if (entity instanceof EntityHorse) {
                                return new CraftHorse(server, (EntityHorse)entity);
                            }
                            if (entity instanceof EntityRabbit) {
                                return new CraftRabbit(server, (EntityRabbit)entity);
                            }
                            if (entity instanceof EntityPolarBear) {
                                return new CraftPolarBear(server, (EntityPolarBear)entity);
                            }
                            return new CraftAnimals(server, (EntityAnimal)entity);
                        }
                    }
                }
                else if (entity instanceof EntityMob) {
                    if (entity instanceof EntityZombie) {
                        if (entity instanceof EntityPigZombie) {
                            return new CraftPigZombie(server, (EntityPigZombie)entity);
                        }
                        return new CraftZombie(server, (EntityZombie)entity);
                    }
                    else {
                        if (entity instanceof EntityCreeper) {
                            return new CraftCreeper(server, (EntityCreeper)entity);
                        }
                        if (entity instanceof EntityEnderman) {
                            return new CraftEnderman(server, (EntityEnderman)entity);
                        }
                        if (entity instanceof EntitySilverfish) {
                            return new CraftSilverfish(server, (EntitySilverfish)entity);
                        }
                        if (entity instanceof EntityGiantZombie) {
                            return new CraftGiant(server, (EntityGiantZombie)entity);
                        }
                        if (entity instanceof EntitySkeleton) {
                            return new CraftSkeleton(server, (EntitySkeleton)entity);
                        }
                        if (entity instanceof EntityBlaze) {
                            return new CraftBlaze(server, (EntityBlaze)entity);
                        }
                        if (entity instanceof EntityWitch) {
                            return new CraftWitch(server, (EntityWitch)entity);
                        }
                        if (entity instanceof EntityWither) {
                            return new CraftWither(server, (EntityWither)entity);
                        }
                        if (entity instanceof EntitySpider) {
                            if (entity instanceof EntityCaveSpider) {
                                return new CraftCaveSpider(server, (EntityCaveSpider)entity);
                            }
                            return new CraftSpider(server, (EntitySpider)entity);
                        }
                        else {
                            if (entity instanceof EntityEndermite) {
                                return new CraftEndermite(server, (EntityEndermite)entity);
                            }
                            if (entity instanceof EntityGuardian) {
                                return new CraftGuardian(server, (EntityGuardian)entity);
                            }
                            return new CraftMonster(server, (EntityMob)entity);
                        }
                    }
                }
                else if (entity instanceof EntityGolem) {
                    if (entity instanceof EntitySnowman) {
                        return new CraftSnowman(server, (EntitySnowman)entity);
                    }
                    if (entity instanceof EntityIronGolem) {
                        return new CraftIronGolem(server, (EntityIronGolem)entity);
                    }
                    if (entity instanceof EntityShulker) {
                        return new CraftShulker(server, (EntityShulker)entity);
                    }
                }
                else {
                    if (entity instanceof EntityVillager) {
                        return new CraftVillager(server, (EntityVillager)entity);
                    }
                    return new CraftCreature(server, (EntityCreature)entity);
                }
            }
            else if (entity instanceof EntitySlime) {
                if (entity instanceof EntityMagmaCube) {
                    return new CraftMagmaCube(server, (EntityMagmaCube)entity);
                }
                return new CraftSlime(server, (EntitySlime)entity);
            }
            else if (entity instanceof EntityFlying) {
                if (entity instanceof EntityGhast) {
                    return new CraftGhast(server, (EntityGhast)entity);
                }
                return new CraftFlying(server, (EntityFlying)entity);
            }
            else {
                if (entity instanceof EntityDragon) {
                    return new CraftEnderDragon(server, (EntityDragon)entity);
                }
                if (entity instanceof EntityAmbientCreature) {
                    if (entity instanceof EntityBat) {
                        return new CraftBat(server, (EntityBat)entity);
                    }
                    return new CraftAmbient(server, (EntityAmbientCreature)entity);
                }
                else {
                    if (entity instanceof EntityArmorStand) {
                        return new CraftArmorStand(server, (EntityArmorStand)entity);
                    }
                    return new CraftLivingEntity(server, (EntityLivingBase)entity);
                }
            }
        }
        else if (entity instanceof EntityDragonPart) {
            final EntityDragonPart part = (EntityDragonPart)entity;
            if (part.entityDragonObj instanceof EntityDragon) {
                return new CraftEnderDragonPart(server, (EntityDragonPart)entity);
            }
            return new CraftComplexPart(server, (EntityDragonPart)entity);
        }
        else {
            if (entity instanceof EntityXPOrb) {
                return new CraftExperienceOrb(server, (EntityXPOrb)entity);
            }
            if (entity instanceof EntityTippedArrow) {
                if (((EntityTippedArrow)entity).isTipped()) {
                    return new CraftTippedArrow(server, (EntityTippedArrow)entity);
                }
                return new CraftArrow(server, (EntityArrow)entity);
            }
            else {
                if (entity instanceof EntitySpectralArrow) {
                    return new CraftSpectralArrow(server, (EntitySpectralArrow)entity);
                }
                if (entity instanceof EntityArrow) {
                    return new CraftArrow(server, (EntityArrow)entity);
                }
                if (entity instanceof EntityBoat) {
                    return new CraftBoat(server, (EntityBoat)entity);
                }
                if (entity instanceof EntityThrowable) {
                    if (entity instanceof EntityEgg) {
                        return new CraftEgg(server, (EntityEgg)entity);
                    }
                    if (entity instanceof EntitySnowball) {
                        return new CraftSnowball(server, (EntitySnowball)entity);
                    }
                    if (entity instanceof EntityPotion) {
                        if (!((EntityPotion)entity).isLingering()) {
                            return new CraftSplashPotion(server, (EntityPotion)entity);
                        }
                        return new CraftLingeringPotion(server, (EntityPotion)entity);
                    }
                    else {
                        if (entity instanceof EntityEnderPearl) {
                            return new CraftEnderPearl(server, (EntityEnderPearl)entity);
                        }
                        if (entity instanceof EntityExpBottle) {
                            return new CraftThrownExpBottle(server, (EntityExpBottle)entity);
                        }
                    }
                }
                else {
                    if (entity instanceof EntityFallingBlock) {
                        return new CraftFallingSand(server, (EntityFallingBlock)entity);
                    }
                    if (entity instanceof EntityFireball) {
                        if (entity instanceof EntitySmallFireball) {
                            return new CraftSmallFireball(server, (EntitySmallFireball)entity);
                        }
                        if (entity instanceof EntityLargeFireball) {
                            return new CraftLargeFireball(server, (EntityLargeFireball)entity);
                        }
                        if (entity instanceof EntityWitherSkull) {
                            return new CraftWitherSkull(server, (EntityWitherSkull)entity);
                        }
                        if (entity instanceof EntityDragonFireball) {
                            return new CraftDragonFireball(server, (EntityDragonFireball)entity);
                        }
                        return new CraftFireball(server, (EntityFireball)entity);
                    }
                    else {
                        if (entity instanceof EntityEnderEye) {
                            return new CraftEnderSignal(server, (EntityEnderEye)entity);
                        }
                        if (entity instanceof EntityEnderCrystal) {
                            return new CraftEnderCrystal(server, (EntityEnderCrystal)entity);
                        }
                        if (entity instanceof EntityFishHook) {
                            return new CraftFish(server, (EntityFishHook)entity);
                        }
                        if (entity instanceof EntityItem) {
                            return new CraftItem(server, (EntityItem)entity);
                        }
                        if (entity instanceof EntityWeatherEffect) {
                            if (entity instanceof EntityLightningBolt) {
                                return new CraftLightningStrike(server, (EntityLightningBolt)entity);
                            }
                            return new CraftWeather(server, (EntityWeatherEffect)entity);
                        }
                        else if (entity instanceof EntityMinecart) {
                            if (entity instanceof EntityMinecartFurnace) {
                                return new CraftMinecartFurnace(server, (EntityMinecartFurnace)entity);
                            }
                            if (entity instanceof EntityMinecartChest) {
                                return new CraftMinecartChest(server, (EntityMinecartChest)entity);
                            }
                            if (entity instanceof EntityMinecartTNT) {
                                return new CraftMinecartTNT(server, (EntityMinecartTNT)entity);
                            }
                            if (entity instanceof EntityMinecartHopper) {
                                return new CraftMinecartHopper(server, (EntityMinecartHopper)entity);
                            }
                            if (entity instanceof EntityMinecartMobSpawner) {
                                return new CraftMinecartMobSpawner(server, (EntityMinecartMobSpawner)entity);
                            }
                            if (entity instanceof EntityMinecartEmpty) {
                                return new CraftMinecartRideable(server, (EntityMinecart)entity);
                            }
                            if (entity instanceof EntityMinecartCommandBlock) {
                                return new CraftMinecartCommand(server, (EntityMinecartCommandBlock)entity);
                            }
                        }
                        else if (entity instanceof EntityHanging) {
                            if (entity instanceof EntityPainting) {
                                return new CraftPainting(server, (EntityPainting)entity);
                            }
                            if (entity instanceof EntityItemFrame) {
                                return new CraftItemFrame(server, (EntityItemFrame)entity);
                            }
                            if (entity instanceof EntityLeashKnot) {
                                return new CraftLeash(server, (EntityLeashKnot)entity);
                            }
                            return new CraftHanging(server, (EntityHanging)entity);
                        }
                        else {
                            if (entity instanceof EntityTNTPrimed) {
                                return new CraftTNTPrimed(server, (EntityTNTPrimed)entity);
                            }
                            if (entity instanceof EntityFireworkRocket) {
                                return new CraftFirework(server, (EntityFireworkRocket)entity);
                            }
                            if (entity instanceof EntityShulkerBullet) {
                                return new CraftShulkerBullet(server, (EntityShulkerBullet)entity);
                            }
                            if (entity instanceof EntityAreaEffectCloud) {
                                return new CraftAreaEffectCloud(server, (EntityAreaEffectCloud)entity);
                            }
                        }
                    }
                }
            }
        }
        if (entity instanceof net.minecraft.entity.Entity){
            if(entity instanceof net.minecraft.entity.IProjectile) return new CustomCraftProjectile(server, entity);
            return new CraftCustomEntity(server, entity);
        }
        throw new AssertionError((Object)("Unknown entity " + ((entity == null) ? null : entity.getClass())));
    }
*/
public static CraftEntity getEntity(CraftServer server, net.minecraft.entity.Entity entity) {
    /**
     * Order is *EXTREMELY* important -- keep it right! =D
     */
    if (entity instanceof net.minecraft.entity.EntityLivingBase) {
        // Players
        if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
            if (entity instanceof net.minecraft.entity.player.EntityPlayerMP) { return new CraftPlayer(server, (net.minecraft.entity.player.EntityPlayerMP) entity); }
            // Cauldron start - support fake player classes from mods
            // This case is never hit in vanilla
            //else { return new CraftHumanEntity(server, (net.minecraft.entity.player.EntityPlayer) entity); }
            else {
                return new CraftPlayer(server, FakePlayerFactory.get(DimensionManager.getWorld(entity.worldObj.provider.getDimension()), ((net.minecraft.entity.player.EntityPlayer) entity).getGameProfile()));
            }
            // Cauldron end
        }
        else if (entity instanceof net.minecraft.entity.EntityCreature) {
            // Animals
            if (entity instanceof net.minecraft.entity.passive.EntityAnimal) {
                if (entity instanceof net.minecraft.entity.passive.EntityChicken) { return new CraftChicken(server, (net.minecraft.entity.passive.EntityChicken) entity); }
                else if (entity instanceof net.minecraft.entity.passive.EntityCow) {
                    if (entity instanceof net.minecraft.entity.passive.EntityMooshroom) { return new CraftMushroomCow(server, (net.minecraft.entity.passive.EntityMooshroom) entity); }
                    else { return new CraftCow(server, (net.minecraft.entity.passive.EntityCow) entity); }
                }
                else if (entity instanceof net.minecraft.entity.passive.EntityPig) { return new CraftPig(server, (net.minecraft.entity.passive.EntityPig) entity); }
                else if (entity instanceof net.minecraft.entity.passive.EntityTameable) {
                    if (entity instanceof net.minecraft.entity.passive.EntityWolf) { return new CraftWolf(server, (net.minecraft.entity.passive.EntityWolf) entity); }
                    else if (entity instanceof net.minecraft.entity.passive.EntityOcelot) { return new CraftOcelot(server, (net.minecraft.entity.passive.EntityOcelot) entity); } // Cauldron
                    else { return new CraftTameableAnimal(server, (net.minecraft.entity.passive.EntityTameable) entity); } // Cauldron
                }
                else if (entity instanceof net.minecraft.entity.passive.EntitySheep) { return new CraftSheep(server, (net.minecraft.entity.passive.EntitySheep) entity); }
                else if (entity instanceof net.minecraft.entity.passive.EntityHorse) { return new CraftHorse(server, (net.minecraft.entity.passive.EntityHorse) entity); }
                else { return new CraftAnimals(server, (net.minecraft.entity.passive.EntityAnimal) entity); }
            }
            // Monsters
            else if (entity instanceof net.minecraft.entity.monster.EntityMob) {
                if (entity instanceof net.minecraft.entity.monster.EntityZombie) {
                    if (entity instanceof net.minecraft.entity.monster.EntityPigZombie) { return new CraftPigZombie(server, (net.minecraft.entity.monster.EntityPigZombie) entity); }
                    else { return new CraftZombie(server, (net.minecraft.entity.monster.EntityZombie) entity); }
                }
                else if (entity instanceof net.minecraft.entity.monster.EntityCreeper) { return new CraftCreeper(server, (net.minecraft.entity.monster.EntityCreeper) entity); }
                else if (entity instanceof net.minecraft.entity.monster.EntityEnderman) { return new CraftEnderman(server, (net.minecraft.entity.monster.EntityEnderman) entity); }
                else if (entity instanceof net.minecraft.entity.monster.EntitySilverfish) { return new CraftSilverfish(server, (net.minecraft.entity.monster.EntitySilverfish) entity); }
                else if (entity instanceof net.minecraft.entity.monster.EntityGiantZombie) { return new CraftGiant(server, (net.minecraft.entity.monster.EntityGiantZombie) entity); }
                else if (entity instanceof net.minecraft.entity.monster.EntitySkeleton) { return new CraftSkeleton(server, (net.minecraft.entity.monster.EntitySkeleton) entity); }
                else if (entity instanceof net.minecraft.entity.monster.EntityBlaze) { return new CraftBlaze(server, (net.minecraft.entity.monster.EntityBlaze) entity); }
                else if (entity instanceof net.minecraft.entity.monster.EntityWitch) { return new CraftWitch(server, (net.minecraft.entity.monster.EntityWitch) entity); }
                else if (entity instanceof net.minecraft.entity.boss.EntityWither) { return new CraftWither(server, (net.minecraft.entity.boss.EntityWither) entity); }
                else if (entity instanceof net.minecraft.entity.monster.EntitySpider) {
                    if (entity instanceof net.minecraft.entity.monster.EntityCaveSpider) { return new CraftCaveSpider(server, (net.minecraft.entity.monster.EntityCaveSpider) entity); }
                    else { return new CraftSpider(server, (net.minecraft.entity.monster.EntitySpider) entity); }
                }

                else  { return new CraftMonster(server, (net.minecraft.entity.monster.EntityMob) entity); }
            }
            // Water Animals
            else if (entity instanceof net.minecraft.entity.passive.EntityWaterMob) {
                if (entity instanceof net.minecraft.entity.passive.EntitySquid) { return new CraftSquid(server, (net.minecraft.entity.passive.EntitySquid) entity); }
                else { return new CraftWaterMob(server, (net.minecraft.entity.passive.EntityWaterMob) entity); }
            }
            else if (entity instanceof net.minecraft.entity.monster.EntityGolem) {
                if (entity instanceof net.minecraft.entity.monster.EntitySnowman) { return new CraftSnowman(server, (net.minecraft.entity.monster.EntitySnowman) entity); }
                else if (entity instanceof net.minecraft.entity.monster.EntityIronGolem) { return new CraftIronGolem(server, (net.minecraft.entity.monster.EntityIronGolem) entity); }
                else { return new CraftLivingEntity(server, (net.minecraft.entity.EntityLivingBase) entity); } // Cauldron
            }
            else if (entity instanceof net.minecraft.entity.passive.EntityVillager) { return new CraftVillager(server, (net.minecraft.entity.passive.EntityVillager) entity); }
            else { return new CraftCreature(server, (net.minecraft.entity.EntityCreature) entity); }
        }
        // Slimes are a special (and broken) case
        else if (entity instanceof net.minecraft.entity.monster.EntitySlime) {
            if (entity instanceof net.minecraft.entity.monster.EntityMagmaCube) { return new CraftMagmaCube(server, (net.minecraft.entity.monster.EntityMagmaCube) entity); }
            else { return new CraftSlime(server, (net.minecraft.entity.monster.EntitySlime) entity); }
        }
        // Flying
        else if (entity instanceof net.minecraft.entity.EntityFlying) {
            if (entity instanceof net.minecraft.entity.monster.EntityGhast) { return new CraftGhast(server, (net.minecraft.entity.monster.EntityGhast) entity); }
            else { return new CraftFlying(server, (net.minecraft.entity.EntityFlying) entity); }
        }
        else if (entity instanceof net.minecraft.entity.boss.EntityDragon) {
            return new CraftEnderDragon(server, (net.minecraft.entity.boss.EntityDragon) entity);
        }
        // Ambient
        else if (entity instanceof net.minecraft.entity.passive.EntityAmbientCreature) {
            if (entity instanceof net.minecraft.entity.passive.EntityBat) { return new CraftBat(server, (net.minecraft.entity.passive.EntityBat) entity); }
            else { return new CraftAmbient(server, (net.minecraft.entity.passive.EntityAmbientCreature) entity); }
        }
        else  { return new CraftLivingEntity(server, (net.minecraft.entity.EntityLivingBase) entity); }
    }
    else if (entity instanceof net.minecraft.entity.boss.EntityDragonPart) {
        net.minecraft.entity.boss.EntityDragonPart part = (net.minecraft.entity.boss.EntityDragonPart) entity;
        if (part.entityDragonObj instanceof net.minecraft.entity.boss.EntityDragon) { return new CraftEnderDragonPart(server, (net.minecraft.entity.boss.EntityDragonPart) entity); }
        else { return new CraftComplexPart(server, (net.minecraft.entity.boss.EntityDragonPart) entity); }
    }
    else if (entity instanceof net.minecraft.entity.item.EntityXPOrb) { return new CraftExperienceOrb(server, (net.minecraft.entity.item.EntityXPOrb) entity); }
    else if (entity instanceof net.minecraft.entity.projectile.EntityArrow) { return new CraftArrow(server, (net.minecraft.entity.projectile.EntityArrow) entity); }
    else if (entity instanceof net.minecraft.entity.item.EntityBoat) { return new CraftBoat(server, (net.minecraft.entity.item.EntityBoat) entity); }
    else if (entity instanceof net.minecraft.entity.projectile.EntityThrowable) {
        if (entity instanceof net.minecraft.entity.projectile.EntityEgg) { return new CraftEgg(server, (net.minecraft.entity.projectile.EntityEgg) entity); }
        else if (entity instanceof net.minecraft.entity.projectile.EntitySnowball) { return new CraftSnowball(server, (net.minecraft.entity.projectile.EntitySnowball) entity); }
        else if (entity instanceof net.minecraft.entity.projectile.EntityPotion) { return new CraftThrownPotion(server, (net.minecraft.entity.projectile.EntityPotion) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityEnderPearl) { return new CraftEnderPearl(server, (net.minecraft.entity.item.EntityEnderPearl) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityExpBottle) { return new CraftThrownExpBottle(server, (net.minecraft.entity.item.EntityExpBottle) entity); }
        else { return new CraftProjectile(server, (net.minecraft.entity.projectile.EntityThrowable) entity); } // Cauldron
    }
    else if (entity instanceof net.minecraft.entity.item.EntityFallingBlock) { return new CraftFallingSand(server, (net.minecraft.entity.item.EntityFallingBlock) entity); }
    else if (entity instanceof net.minecraft.entity.projectile.EntityFireball) {
        if (entity instanceof net.minecraft.entity.projectile.EntitySmallFireball) { return new CraftSmallFireball(server, (net.minecraft.entity.projectile.EntitySmallFireball) entity); }
        else if (entity instanceof net.minecraft.entity.projectile.EntityLargeFireball) { return new CraftLargeFireball(server, (net.minecraft.entity.projectile.EntityLargeFireball) entity); }
        else if (entity instanceof net.minecraft.entity.projectile.EntityWitherSkull) { return new CraftWitherSkull(server, (net.minecraft.entity.projectile.EntityWitherSkull) entity); }
        else { return new CraftFireball(server, (net.minecraft.entity.projectile.EntityFireball) entity); }
    }
    else if (entity instanceof net.minecraft.entity.item.EntityEnderEye) { return new CraftEnderSignal(server, (net.minecraft.entity.item.EntityEnderEye) entity); }
    else if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal) { return new CraftEnderCrystal(server, (net.minecraft.entity.item.EntityEnderCrystal) entity); }
    else if (entity instanceof net.minecraft.entity.projectile.EntityFishHook) { return new CraftFish(server, (net.minecraft.entity.projectile.EntityFishHook) entity); }
    else if (entity instanceof net.minecraft.entity.item.EntityItem) { return new CraftItem(server, (net.minecraft.entity.item.EntityItem) entity); }
    else if (entity instanceof net.minecraft.entity.effect.EntityWeatherEffect) {
        if (entity instanceof net.minecraft.entity.effect.EntityLightningBolt) { return new CraftLightningStrike(server, (net.minecraft.entity.effect.EntityLightningBolt) entity); }
        else { return new CraftWeather(server, (net.minecraft.entity.effect.EntityWeatherEffect) entity); }
    }
    else if (entity instanceof net.minecraft.entity.item.EntityMinecart) {
        if (entity instanceof net.minecraft.entity.item.EntityMinecartFurnace) { return new CraftMinecartFurnace(server, (net.minecraft.entity.item.EntityMinecartFurnace) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityMinecartChest) { return new CraftMinecartChest(server, (net.minecraft.entity.item.EntityMinecartChest) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityMinecartTNT) { return new CraftMinecartTNT(server, (net.minecraft.entity.item.EntityMinecartTNT) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityMinecartHopper) { return new CraftMinecartHopper(server, (net.minecraft.entity.item.EntityMinecartHopper) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityMinecartMobSpawner) { return new CraftMinecartMobSpawner(server, (net.minecraft.entity.item.EntityMinecartMobSpawner) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityMinecartEmpty) { return new CraftMinecartRideable(server, (net.minecraft.entity.item.EntityMinecartEmpty) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityMinecartCommandBlock) { return new CraftMinecartCommand(server, (net.minecraft.entity.item.EntityMinecartCommandBlock) entity); }
        else { return new CraftMinecart(server, (net.minecraft.entity.item.EntityMinecart) entity); } // Cauldron - other minecarts (Steve's Carts)
    } else if (entity instanceof net.minecraft.entity.EntityHanging) {
        if (entity instanceof net.minecraft.entity.item.EntityPainting) { return new CraftPainting(server, (net.minecraft.entity.item.EntityPainting) entity); }
        else if (entity instanceof net.minecraft.entity.item.EntityItemFrame) { return new CraftItemFrame(server, (net.minecraft.entity.item.EntityItemFrame) entity); }
        else if (entity instanceof net.minecraft.entity.EntityLeashKnot) { return new CraftLeash(server, (net.minecraft.entity.EntityLeashKnot) entity); }
        else { return new CraftHanging(server, (net.minecraft.entity.EntityHanging) entity); }
    }
    else if (entity instanceof net.minecraft.entity.item.EntityTNTPrimed) { return new CraftTNTPrimed(server, (net.minecraft.entity.item.EntityTNTPrimed) entity); }
    else if (entity instanceof net.minecraft.entity.item.EntityFireworkRocket) { return new CraftFirework(server, (net.minecraft.entity.item.EntityFireworkRocket) entity); }
    // Cauldron - used for custom entities that extend Entity directly
    else if (entity instanceof net.minecraft.entity.Entity) {
        if (entity instanceof net.minecraft.entity.IProjectile) return new luohuayu.CatServer.entity.CustomCraftProjectile(server, entity); // Thermos
        return new CraftCustomEntity(server, (net.minecraft.entity.Entity) entity); }
    else { return null; }
    //throw new AssertionError("Unknown entity " + entity == null ? null : entity.getClass() + ": " + entity); // Cauldron - show the entity that caused exception
}

    // Cauldron start - copy of getEntity() but operates on classes instead of instances, for EntityRegistry registerBukkitType
    public static Class<? extends org.bukkit.entity.Entity> getEntityClass(Class<? extends net.minecraft.entity.Entity> nmsClass) {
        /**
         * Order is *EXTREMELY* important -- keep it right! =D
         */
        // pbpaste|perl -pe's/entity instanceof ([\w.]+)/$1.class.isAssignableFrom(nmsClass)/g'|perl -pe's/return new (\w+)([^;\n]+)/return $1.class/g'
        if (net.minecraft.entity.EntityLivingBase.class.isAssignableFrom(nmsClass)) {
            // Players
            if (net.minecraft.entity.player.EntityPlayer.class.isAssignableFrom(nmsClass)) {
                if (net.minecraft.entity.player.EntityPlayerMP.class.isAssignableFrom(nmsClass)) { return CraftPlayer.class; }
                // support fake player classes from mods
                // This case is never hit in vanilla
                //else { return CraftHumanEntity.class; }
                else {
                    return CraftPlayer.class;
                }
            }
            else if (net.minecraft.entity.EntityCreature.class.isAssignableFrom(nmsClass)) {
                // Animals
                if (net.minecraft.entity.passive.EntityAnimal.class.isAssignableFrom(nmsClass)) {
                    if (net.minecraft.entity.passive.EntityChicken.class.isAssignableFrom(nmsClass)) { return CraftChicken.class; }
                    else if (net.minecraft.entity.passive.EntityCow.class.isAssignableFrom(nmsClass)) {
                        if (net.minecraft.entity.passive.EntityMooshroom.class.isAssignableFrom(nmsClass)) { return CraftMushroomCow.class; }
                        else { return CraftCow.class; }
                    }
                    else if (net.minecraft.entity.passive.EntityPig.class.isAssignableFrom(nmsClass)) { return CraftPig.class; }
                    else if (net.minecraft.entity.passive.EntityTameable.class.isAssignableFrom(nmsClass)) {
                        if (net.minecraft.entity.passive.EntityWolf.class.isAssignableFrom(nmsClass)) { return CraftWolf.class; }
                        else if (net.minecraft.entity.passive.EntityOcelot.class.isAssignableFrom(nmsClass)) { return CraftOcelot.class; } // Cauldron
                        else { return CraftTameableAnimal.class; }
                    }
                    else if (net.minecraft.entity.passive.EntitySheep.class.isAssignableFrom(nmsClass)) { return CraftSheep.class; }
                    else if (net.minecraft.entity.passive.EntityHorse.class.isAssignableFrom(nmsClass)) { return CraftHorse.class; }
                    else { return CraftAnimals.class; }
                }
                // Monsters
                else if (net.minecraft.entity.monster.EntityMob.class.isAssignableFrom(nmsClass)) {
                    if (net.minecraft.entity.monster.EntityZombie.class.isAssignableFrom(nmsClass)) {
                        if (net.minecraft.entity.monster.EntityPigZombie.class.isAssignableFrom(nmsClass)) { return CraftPigZombie.class; }
                        else { return CraftZombie.class; }
                    }
                    else if (net.minecraft.entity.monster.EntityCreeper.class.isAssignableFrom(nmsClass)) { return CraftCreeper.class; }
                    else if (net.minecraft.entity.monster.EntityEnderman.class.isAssignableFrom(nmsClass)) { return CraftEnderman.class; }
                    else if (net.minecraft.entity.monster.EntitySilverfish.class.isAssignableFrom(nmsClass)) { return CraftSilverfish.class; }
                    else if (net.minecraft.entity.monster.EntityGiantZombie.class.isAssignableFrom(nmsClass)) { return CraftGiant.class; }
                    else if (net.minecraft.entity.monster.EntitySkeleton.class.isAssignableFrom(nmsClass)) { return CraftSkeleton.class; }
                    else if (net.minecraft.entity.monster.EntityBlaze.class.isAssignableFrom(nmsClass)) { return CraftBlaze.class; }
                    else if (net.minecraft.entity.monster.EntityWitch.class.isAssignableFrom(nmsClass)) { return CraftWitch.class; }
                    else if (net.minecraft.entity.boss.EntityWither.class.isAssignableFrom(nmsClass)) { return CraftWither.class; }
                    else if (net.minecraft.entity.monster.EntitySpider.class.isAssignableFrom(nmsClass)) {
                        if (net.minecraft.entity.monster.EntityCaveSpider.class.isAssignableFrom(nmsClass)) { return CraftCaveSpider.class; }
                        else { return CraftSpider.class; }
                    }

                    else  { return CraftMonster.class; }
                }
                // Water Animals
                else if (net.minecraft.entity.passive.EntityWaterMob.class.isAssignableFrom(nmsClass)) {
                    if (net.minecraft.entity.passive.EntitySquid.class.isAssignableFrom(nmsClass)) { return CraftSquid.class; }
                    else { return CraftWaterMob.class; }
                }
                else if (net.minecraft.entity.monster.EntityGolem.class.isAssignableFrom(nmsClass)) {
                    if (net.minecraft.entity.monster.EntitySnowman.class.isAssignableFrom(nmsClass)) { return CraftSnowman.class; }
                    else if (net.minecraft.entity.monster.EntityIronGolem.class.isAssignableFrom(nmsClass)) { return CraftIronGolem.class; }
                    else { return CraftLivingEntity.class; }
                }
                else if (net.minecraft.entity.passive.EntityVillager.class.isAssignableFrom(nmsClass)) { return CraftVillager.class; }
                else { return CraftCreature.class; }
            }
            // Slimes are a special (and broken) case
            else if (net.minecraft.entity.monster.EntitySlime.class.isAssignableFrom(nmsClass)) {
                if (net.minecraft.entity.monster.EntityMagmaCube.class.isAssignableFrom(nmsClass)) { return CraftMagmaCube.class; }
                else { return CraftSlime.class; }
            }
            // Flying
            else if (net.minecraft.entity.EntityFlying.class.isAssignableFrom(nmsClass)) {
                if (net.minecraft.entity.monster.EntityGhast.class.isAssignableFrom(nmsClass)) { return CraftGhast.class; }
                else { return CraftFlying.class; }
            }
            else if (net.minecraft.entity.boss.EntityDragon.class.isAssignableFrom(nmsClass)) {
                return CraftEnderDragon.class;
            }
            // Ambient
            else if (net.minecraft.entity.passive.EntityAmbientCreature.class.isAssignableFrom(nmsClass)) {
                if (net.minecraft.entity.passive.EntityBat.class.isAssignableFrom(nmsClass)) { return CraftBat.class; }
                else { return CraftAmbient.class; }
            }
            else  { return CraftLivingEntity.class; }
        }
        else if (net.minecraft.entity.boss.EntityDragonPart.class.isAssignableFrom(nmsClass)) {
            /* Cauldron - no instance, best we can say is this is a CraftComplexPart
            net.minecraft.entity.boss.EntityDragonPart part = (net.minecraft.entity.boss.EntityDragonPart) entity;
            if (part.entityDragonObj instanceof net.minecraft.entity.boss.EntityDragon) { return CraftEnderDragonPart.class; }
            else { return CraftComplexPart.class; }
            */
            return CraftComplexPart.class;
        }
        else if (net.minecraft.entity.item.EntityXPOrb.class.isAssignableFrom(nmsClass)) { return CraftExperienceOrb.class; }
        else if (net.minecraft.entity.projectile.EntityArrow.class.isAssignableFrom(nmsClass)) { return CraftArrow.class; }
        else if (net.minecraft.entity.item.EntityBoat.class.isAssignableFrom(nmsClass)) { return CraftBoat.class; }
        else if (net.minecraft.entity.projectile.EntityThrowable.class.isAssignableFrom(nmsClass)) {
            if (net.minecraft.entity.projectile.EntityEgg.class.isAssignableFrom(nmsClass)) { return CraftEgg.class; }
            else if (net.minecraft.entity.projectile.EntitySnowball.class.isAssignableFrom(nmsClass)) { return CraftSnowball.class; }
            else if (net.minecraft.entity.projectile.EntityPotion.class.isAssignableFrom(nmsClass)) { return CraftThrownPotion.class; }
            else if (net.minecraft.entity.item.EntityEnderPearl.class.isAssignableFrom(nmsClass)) { return CraftEnderPearl.class; }
            else if (net.minecraft.entity.item.EntityExpBottle.class.isAssignableFrom(nmsClass)) { return CraftThrownExpBottle.class; }
            else { return CraftProjectile.class; } // Cauldron
        }
        else if (net.minecraft.entity.item.EntityFallingBlock.class.isAssignableFrom(nmsClass)) { return CraftFallingSand.class; }
        else if (net.minecraft.entity.projectile.EntityFireball.class.isAssignableFrom(nmsClass)) {
            if (net.minecraft.entity.projectile.EntitySmallFireball.class.isAssignableFrom(nmsClass)) { return CraftSmallFireball.class; }
            else if (net.minecraft.entity.projectile.EntityLargeFireball.class.isAssignableFrom(nmsClass)) { return CraftLargeFireball.class; }
            else if (net.minecraft.entity.projectile.EntityWitherSkull.class.isAssignableFrom(nmsClass)) { return CraftWitherSkull.class; }
            else { return CraftFireball.class; }
        }
        else if (net.minecraft.entity.item.EntityEnderEye.class.isAssignableFrom(nmsClass)) { return CraftEnderSignal.class; }
        else if (net.minecraft.entity.item.EntityEnderCrystal.class.isAssignableFrom(nmsClass)) { return CraftEnderCrystal.class; }
        else if (net.minecraft.entity.projectile.EntityFishHook.class.isAssignableFrom(nmsClass)) { return CraftFish.class; }
        else if (net.minecraft.entity.item.EntityItem.class.isAssignableFrom(nmsClass)) { return CraftItem.class; }
        else if (net.minecraft.entity.effect.EntityWeatherEffect.class.isAssignableFrom(nmsClass)) {
            if (net.minecraft.entity.effect.EntityLightningBolt.class.isAssignableFrom(nmsClass)) { return CraftLightningStrike.class; }
            else { return CraftWeather.class; }
        }
        else if (net.minecraft.entity.item.EntityMinecart.class.isAssignableFrom(nmsClass)) {
            if (net.minecraft.entity.item.EntityMinecartFurnace.class.isAssignableFrom(nmsClass)) { return CraftMinecartFurnace.class; }
            else if (net.minecraft.entity.item.EntityMinecartChest.class.isAssignableFrom(nmsClass)) { return CraftMinecartChest.class; }
            else if (net.minecraft.entity.item.EntityMinecartTNT.class.isAssignableFrom(nmsClass)) { return CraftMinecartTNT.class; }
            else if (net.minecraft.entity.item.EntityMinecartHopper.class.isAssignableFrom(nmsClass)) { return CraftMinecartHopper.class; }
            else if (net.minecraft.entity.item.EntityMinecartMobSpawner.class.isAssignableFrom(nmsClass)) { return CraftMinecartMobSpawner.class; }
            else if (net.minecraft.entity.item.EntityMinecartEmpty.class.isAssignableFrom(nmsClass)) { return CraftMinecartRideable.class; }
            else if (net.minecraft.entity.item.EntityMinecartCommandBlock.class.isAssignableFrom(nmsClass)) { return CraftMinecartCommand.class; }
            else { return CraftMinecart.class; } // Cauldron - other minecarts (Steve's Carts)
        } else if (net.minecraft.entity.EntityHanging.class.isAssignableFrom(nmsClass)) {
            if (net.minecraft.entity.item.EntityPainting.class.isAssignableFrom(nmsClass)) { return CraftPainting.class; }
            else if (net.minecraft.entity.item.EntityItemFrame.class.isAssignableFrom(nmsClass)) { return CraftItemFrame.class; }
            else if (net.minecraft.entity.EntityLeashKnot.class.isAssignableFrom(nmsClass)) { return CraftLeash.class; }
            else { return CraftHanging.class; }
        }
        else if (net.minecraft.entity.item.EntityTNTPrimed.class.isAssignableFrom(nmsClass)) { return CraftTNTPrimed.class; }
        else if (net.minecraft.entity.item.EntityFireworkRocket.class.isAssignableFrom(nmsClass)) { return CraftFirework.class; }
        // Cauldron - used for custom entities that extend Entity directly
        else if (net.minecraft.entity.Entity.class.isAssignableFrom(nmsClass)) { return CraftCustomEntity.class; }

        throw new AssertionError("Unknown entity class " + nmsClass == null ? null : nmsClass); // Cauldron - show the entity that caused exception
    }

    // add Bukkit wrappers
    public static void initMappings() {
        for (Map.Entry<Class<? extends net.minecraft.entity.Entity>, String> entry : net.minecraftforge.fml.common.registry.EntityRegistry.entityTypeMap.entrySet()) {
            Class<? extends net.minecraft.entity.Entity> entityClass = entry.getKey();
            String entityName = entry.getValue();
            int entityId = getEntityTypeIDfromClass(entityClass);

            Class<? extends org.bukkit.entity.Entity> bukkitEntityClass = CraftEntity.getEntityClass(entityClass);
            EnumHelper.addBukkitEntityType(entityName, bukkitEntityClass, entityId, false);
        }
    }

    // Lookup entity id from NMS entity class
    private static int getEntityTypeIDfromClass(Class entityClass) {
        // check both maps, since mods can add to either

        Map<Class, Integer> classToIDMapping = net.minecraftforge.fml.relauncher.ReflectionHelper.getPrivateValue(net.minecraft.entity.EntityList.class, null, "field_75624_e", "classToIDMapping");
        if (classToIDMapping.containsKey(entityClass)) {
            return classToIDMapping.get(entityClass);
        }

        Map<Integer, Class> IDtoClassMapping = net.minecraftforge.fml.relauncher.ReflectionHelper.getPrivateValue(net.minecraft.entity.EntityList.class, null, "field_75623_d", "IDtoClassMapping");
        for (Map.Entry<Integer, Class> entry : IDtoClassMapping.entrySet()) {
            int entityId = entry.getKey();
            Class thisEntityClass = entry.getValue();

            if (thisEntityClass.getName().equals(entityClass.getName())) {
                return entityId;
            }
        }

        // if there is no entity ID, choose a negative integer based on the class name
        return -Math.abs(entityClass.getName().hashCode()^(entityClass.getName().hashCode()>>>16));
    }

    @Override
    public Location getLocation() {
        return new Location(this.getWorld(), this.entity.posX, this.entity.posY, this.entity.posZ, this.entity.rotationYaw, this.entity.rotationPitch);
    }
    
    @Override
    public Location getLocation(final Location loc) {
        if (loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX(this.entity.posX);
            loc.setY(this.entity.posY);
            loc.setZ(this.entity.posZ);
            loc.setYaw(this.entity.rotationYaw);
            loc.setPitch(this.entity.rotationPitch);
        }
        return loc;
    }
    
    @Override
    public Vector getVelocity() {
        return new Vector(this.entity.motionX, this.entity.motionY, this.entity.motionZ);
    }
    
    @Override
    public void setVelocity(final Vector vel) {
        this.entity.motionX = vel.getX();
        this.entity.motionY = vel.getY();
        this.entity.motionZ = vel.getZ();
        this.entity.velocityChanged = true;
    }
    
    @Override
    public boolean isOnGround() {
        if (this.entity instanceof EntityArrow) {
            return ((EntityArrow)this.entity).isInGround();
        }
        return this.entity.onGround;
    }
    
    @Override
    public World getWorld() {
        return this.entity.worldObj.getWorld();
    }
    
    @Override
    public boolean teleport(final Location location) {
        return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
    
    @Override
    public boolean teleport(final Location location, final PlayerTeleportEvent.TeleportCause cause) {
        if (this.entity.isBeingRidden() || this.entity.isDead) {
            return false;
        }
        this.entity.dismountRidingEntity();
        this.entity.worldObj = ((CraftWorld)location.getWorld()).getHandle();
        this.entity.setPositionAndRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        return true;
    }
    
    @Override
    public boolean teleport(final Entity destination) {
        return this.teleport(destination.getLocation());
    }
    
    @Override
    public boolean teleport(final Entity destination, final PlayerTeleportEvent.TeleportCause cause) {
        return this.teleport(destination.getLocation(), cause);
    }
    
    @Override
    public List<Entity> getNearbyEntities(final double x, final double y, final double z) {
        final List<net.minecraft.entity.Entity> notchEntityList = this.entity.worldObj.getEntitiesInAABBexcluding(this.entity, this.entity.getEntityBoundingBox().expand(x, y, z), null);
        final List<Entity> bukkitEntityList = new ArrayList<Entity>(notchEntityList.size());
        for (final net.minecraft.entity.Entity e : notchEntityList) {
            bukkitEntityList.add(e.getBukkitEntity());
        }
        return bukkitEntityList;
    }
    
    @Override
    public int getEntityId() {
        return this.entity.getEntityId();
    }
    
    @Override
    public int getFireTicks() {
        return this.entity.fire;
    }
    
    @Override
    public int getMaxFireTicks() {
        return this.entity.fireResistance;
    }
    
    @Override
    public void setFireTicks(final int ticks) {
        this.entity.fire = ticks;
    }
    
    @Override
    public void remove() {
        this.entity.setDead();
    }
    
    @Override
    public boolean isDead() {
        return !this.entity.isEntityAlive();
    }
    
    @Override
    public boolean isValid() {
        return this.entity.isEntityAlive() && this.entity.valid;
    }
    
    @Override
    public Server getServer() {
        return this.server;
    }
    
    public Vector getMomentum() {
        return this.getVelocity();
    }
    
    public void setMomentum(final Vector value) {
        this.setVelocity(value);
    }
    
    @Override
    public Entity getPassenger() {
        return this.isEmpty() ? null : this.getHandle().riddenByEntities.get(0).getBukkitEntity();
    }
    
    @Override
    public boolean setPassenger(final Entity passenger) {
        Preconditions.checkArgument(!this.equals(passenger), (Object)"Entity cannot ride itself.");
        if (passenger instanceof CraftEntity) {
            this.eject();
            ((CraftEntity)passenger).getHandle().startRiding(this.getHandle());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return !this.getHandle().isBeingRidden();
    }
    
    @Override
    public boolean eject() {
        if (this.isEmpty()) {
            return false;
        }
        this.getPassenger().leaveVehicle();
        return true;
    }
    
    @Override
    public float getFallDistance() {
        return this.getHandle().fallDistance;
    }
    
    @Override
    public void setFallDistance(final float distance) {
        this.getHandle().fallDistance = distance;
    }
    
    @Override
    public void setLastDamageCause(final EntityDamageEvent event) {
        this.lastDamageEvent = event;
    }
    
    @Override
    public EntityDamageEvent getLastDamageCause() {
        return this.lastDamageEvent;
    }
    
    @Override
    public UUID getUniqueId() {
        return this.getHandle().getUniqueID();
    }
    
    @Override
    public int getTicksLived() {
        return this.getHandle().ticksExisted;
    }
    
    @Override
    public void setTicksLived(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        this.getHandle().ticksExisted = value;
    }
    
    public net.minecraft.entity.Entity getHandle() {
        return this.entity;
    }
    
    @Override
    public void playEffect(final EntityEffect type) {
        this.getHandle().worldObj.setEntityState(this.getHandle(), type.getData());
    }
    
    public void setHandle(final net.minecraft.entity.Entity entity) {
        this.entity = entity;
    }
    
    @Override
    public String toString() {
        return "CraftEntity{id=" + this.getEntityId() + '}';
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftEntity other = (CraftEntity)obj;
        return this.getEntityId() == other.getEntityId();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.getEntityId();
        return hash;
    }
    
    @Override
    public void setMetadata(final String metadataKey, final MetadataValue newMetadataValue) {
        (/*(MetadataStoreBase<CraftEntity>)*/this.server.getEntityMetadata()).setMetadata(this, metadataKey, newMetadataValue);
    }
    
    @Override
    public List<MetadataValue> getMetadata(final String metadataKey) {
        return (/*(MetadataStoreBase<CraftEntity>)*/this.server.getEntityMetadata()).getMetadata(this, metadataKey);
    }
    
    @Override
    public boolean hasMetadata(final String metadataKey) {
        return (/*(MetadataStoreBase<CraftEntity>)*/this.server.getEntityMetadata()).hasMetadata(this, metadataKey);
    }
    
    @Override
    public void removeMetadata(final String metadataKey, final Plugin owningPlugin) {
        (/*(MetadataStoreBase<CraftEntity>)*/this.server.getEntityMetadata()).removeMetadata(this, metadataKey, owningPlugin);
    }
    
    @Override
    public boolean isInsideVehicle() {
        return this.getHandle().isRiding();
    }
    
    @Override
    public boolean leaveVehicle() {
        if (!this.isInsideVehicle()) {
            return false;
        }
        this.getHandle().dismountRidingEntity();
        return true;
    }
    
    @Override
    public Entity getVehicle() {
        if (!this.isInsideVehicle()) {
            return null;
        }
        return this.getHandle().getLowestRidingEntity().getBukkitEntity();
    }
    
    @Override
    public void setCustomName(String name) {
        if (name == null) {
            name = "";
        }
        this.getHandle().setCustomNameTag(name);
    }
    
    @Override
    public String getCustomName() {
        final String name = this.getHandle().getCustomNameTag();
        if (name == null || name.length() == 0) {
            return null;
        }
        return name;
    }
    
    @Override
    public void setCustomNameVisible(final boolean flag) {
        this.getHandle().setAlwaysRenderNameTag(flag);
    }
    
    @Override
    public boolean isCustomNameVisible() {
        return this.getHandle().getAlwaysRenderNameTag();
    }
    
    @Override
    public void sendMessage(final String message) {
    }
    
    @Override
    public void sendMessage(final String[] messages) {
    }
    
    @Override
    public String getName() {
        return this.getHandle().getName();
    }
    
    @Override
    public boolean isPermissionSet(final String name) {
        return getPermissibleBase().isPermissionSet(name);
    }
    
    @Override
    public boolean isPermissionSet(final Permission perm) {
        return getPermissibleBase().isPermissionSet(perm);
    }
    
    @Override
    public boolean hasPermission(final String name) {
        return getPermissibleBase().hasPermission(name);
    }
    
    @Override
    public boolean hasPermission(final Permission perm) {
        return getPermissibleBase().hasPermission(perm);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
        return getPermissibleBase().addAttachment(plugin, name, value);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin) {
        return getPermissibleBase().addAttachment(plugin);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value, final int ticks) {
        return getPermissibleBase().addAttachment(plugin, name, value, ticks);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
        return getPermissibleBase().addAttachment(plugin, ticks);
    }
    
    @Override
    public void removeAttachment(final PermissionAttachment attachment) {
        getPermissibleBase().removeAttachment(attachment);
    }
    
    @Override
    public void recalculatePermissions() {
        getPermissibleBase().recalculatePermissions();
    }
    
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return getPermissibleBase().getEffectivePermissions();
    }
    
    @Override
    public boolean isOp() {
        return getPermissibleBase().isOp();
    }
    
    @Override
    public void setOp(final boolean value) {
        getPermissibleBase().setOp(value);
    }
    
    @Override
    public void setGlowing(final boolean flag) {
        this.getHandle().glowing = flag;
        final net.minecraft.entity.Entity e = this.getHandle();
        if (e.getFlag(6) != flag) {
            e.setFlag(6, flag);
        }
    }
    
    @Override
    public boolean isGlowing() {
        return this.getHandle().glowing;
    }
    
    @Override
    public void setInvulnerable(final boolean flag) {
        this.getHandle().setEntityInvulnerable(flag);
    }
    
    @Override
    public boolean isInvulnerable() {
        return this.getHandle().isEntityInvulnerable(DamageSource.generic);
    }
    
    @Override
    public boolean isSilent() {
        return this.getHandle().isSilent();
    }
    
    @Override
    public void setSilent(final boolean flag) {
        this.getHandle().setSilent(flag);
    }
    
    @Override
    public boolean hasGravity() {
        return !this.getHandle().isGlowing();
    }
    
    @Override
    public void setGravity(final boolean gravity) {
        this.getHandle().setNoGravity(!gravity);
    }
    
    private static PermissibleBase getPermissibleBase() {
        if (CraftEntity.perm == null) {
            CraftEntity.perm = new PermissibleBase(new ServerOperator() {
                @Override
                public boolean isOp() {
                    return false;
                }
                
                @Override
                public void setOp(final boolean value) {
                }
            });
        }
        return CraftEntity.perm;
    }
}
