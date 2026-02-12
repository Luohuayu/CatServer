package org.bukkit.craftbukkit.v1_20_R1.entity;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.PositionImpl;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.phys.AABB;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftSound;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_20_R1.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftLocation;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftSpawnCategory;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftVector;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.chat.BaseComponent; // Spigot

public abstract class CraftEntity implements org.bukkit.entity.Entity {
    private static PermissibleBase perm;
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    protected final CraftServer server;
    protected Entity entity;
    private final EntityType entityType;
    private EntityDamageEvent lastDamageEvent;
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
        EntityType type = Registry.ENTITY_TYPE.get(CraftNamespacedKey.fromMinecraft(net.minecraft.world.entity.EntityType.getKey(entity.getType())));
        this.entityType = (type != null) ? type : EntityType.UNKNOWN;
    }

    public static CraftEntity getEntity(CraftServer server, Entity entity) {
        /*
         * Order is *EXTREMELY* important -- keep it right! =D
         */
        // CHECKSTYLE:OFF
        if (entity instanceof LivingEntity) {
            // Players
            if (entity instanceof net.minecraft.world.entity.player.Player) {
                if (entity instanceof ServerPlayer) {
                    // CatServer start - support fakePlayer
                    if (entity instanceof net.minecraftforge.common.util.FakePlayer) {
                        return new catserver.server.entity.CraftFakePlayer(server, (net.minecraftforge.common.util.FakePlayer) entity);
                    } else {
                        return new CraftPlayer(server, (ServerPlayer) entity);
                    }
                } else {
                    return new catserver.server.entity.CraftFakePlayer(server, net.minecraftforge.common.util.FakePlayerFactory.get((ServerLevel) entity.level(), ((ServerPlayer) entity).getGameProfile()));
                }
                // CatServer end
            }
            // Water Animals
            else if (entity instanceof WaterAnimal) {
                if (entity instanceof Squid) {
                    if (entity instanceof GlowSquid) { return new CraftGlowSquid(server, (GlowSquid) entity); }
                    else { return new CraftSquid(server, (Squid) entity); }
                }
                else if (entity instanceof AbstractFish) {
                    if (entity instanceof Cod) { return new CraftCod(server, (Cod) entity); }
                    else if (entity instanceof Pufferfish) { return new CraftPufferFish(server, (Pufferfish) entity); }
                    else if (entity instanceof Salmon) { return new CraftSalmon(server, (Salmon) entity); }
                    else if (entity instanceof TropicalFish) { return new CraftTropicalFish(server, (TropicalFish) entity); }
                    else if (entity instanceof Tadpole) { return new CraftTadpole(server, (Tadpole) entity); }
                    else { return new CraftFish(server, (AbstractFish) entity); }
                }
                else if (entity instanceof Dolphin) { return new CraftDolphin(server, (Dolphin) entity); }
                else { return new CraftWaterMob(server, (WaterAnimal) entity); }
            }
            else if (entity instanceof PathfinderMob) {
                // Animals
                if (entity instanceof Animal) {
                    if (entity instanceof Chicken) { return new CraftChicken(server, (Chicken) entity); }
                    else if (entity instanceof Cow) {
                        if (entity instanceof MushroomCow) { return new CraftMushroomCow(server, (MushroomCow) entity); }
                        else { return new CraftCow(server, (Cow) entity); }
                    }
                    else if (entity instanceof Pig) { return new CraftPig(server, (Pig) entity); }
                    else if (entity instanceof TamableAnimal) {
                        if (entity instanceof Wolf) { return new CraftWolf(server, (Wolf) entity); }
                        else if (entity instanceof Cat) { return new CraftCat(server, (Cat) entity); }
                        else if (entity instanceof Parrot) { return new CraftParrot(server, (Parrot) entity); }
                        else return new CraftTameableAnimal(server, (TamableAnimal) entity); // CatServer
                    }
                    else if (entity instanceof Sheep) { return new CraftSheep(server, (Sheep) entity); }
                    else if (entity instanceof AbstractHorse) {
                        if (entity instanceof AbstractChestedHorse){
                            if (entity instanceof Donkey) { return new CraftDonkey(server, (Donkey) entity); }
                            else if (entity instanceof Mule) { return new CraftMule(server, (Mule) entity); }
                            else if (entity instanceof TraderLlama) { return new CraftTraderLlama(server, (TraderLlama) entity); }
                            else if (entity instanceof Llama) { return new CraftLlama(server, (Llama) entity); }
                            else return new catserver.server.entity.CraftCustomChestHorse(server, (AbstractChestedHorse) entity); // CatServer
                        } else if (entity instanceof Horse) { return new CraftHorse(server, (Horse) entity); }
                        else if (entity instanceof SkeletonHorse) { return new CraftSkeletonHorse(server, (SkeletonHorse) entity); }
                        else if (entity instanceof ZombieHorse) { return new CraftZombieHorse(server, (ZombieHorse) entity); }
                        else if (entity instanceof Camel) { return new CraftCamel(server, (Camel) entity); }
                        else return new catserver.server.entity.CraftCustomHorse(server, (AbstractHorse) entity); // CatServer
                    }
                    else if (entity instanceof Rabbit) { return new CraftRabbit(server, (Rabbit) entity); }
                    else if (entity instanceof PolarBear) { return new CraftPolarBear(server, (PolarBear) entity); }
                    else if (entity instanceof Turtle) { return new CraftTurtle(server, (Turtle) entity); }
                    else if (entity instanceof Ocelot) { return new CraftOcelot(server, (Ocelot) entity); }
                    else if (entity instanceof Panda) { return new CraftPanda(server, (Panda) entity); }
                    else if (entity instanceof Fox) { return new CraftFox(server, (Fox) entity); }
                    else if (entity instanceof Bee) { return new CraftBee(server, (Bee) entity); }
                    else if (entity instanceof Hoglin) { return new CraftHoglin(server, (Hoglin) entity); }
                    else if (entity instanceof Strider) { return new CraftStrider(server, (Strider) entity); }
                    else if (entity instanceof Axolotl) { return new CraftAxolotl(server, (Axolotl) entity); }
                    else if (entity instanceof Goat) { return new CraftGoat(server, (Goat) entity); }
                    else if (entity instanceof Frog) { return new CraftFrog(server, (Frog) entity); }
                    else if (entity instanceof Sniffer) { return new CraftSniffer(server, (Sniffer) entity); }
                    else  { return new CraftAnimals(server, (Animal) entity); }
                }
                // Monsters
                else if (entity instanceof Monster) {
                    if (entity instanceof Zombie) {
                        if (entity instanceof ZombifiedPiglin) { return new CraftPigZombie(server, (ZombifiedPiglin) entity); }
                        else if (entity instanceof Husk) { return new CraftHusk(server, (Husk) entity); }
                        else if (entity instanceof ZombieVillager) { return new CraftVillagerZombie(server, (ZombieVillager) entity); }
                        else if (entity instanceof Drowned) { return new CraftDrowned(server, (Drowned) entity); }
                        else { return new CraftZombie(server, (Zombie) entity); }
                    }
                    else if (entity instanceof Creeper) { return new CraftCreeper(server, (Creeper) entity); }
                    else if (entity instanceof EnderMan) { return new CraftEnderman(server, (EnderMan) entity); }
                    else if (entity instanceof Silverfish) { return new CraftSilverfish(server, (Silverfish) entity); }
                    else if (entity instanceof Giant) { return new CraftGiant(server, (Giant) entity); }
                    else if (entity instanceof AbstractSkeleton) {
                        if (entity instanceof Stray) { return new CraftStray(server, (Stray) entity); }
                        else if (entity instanceof WitherSkeleton) { return new CraftWitherSkeleton(server, (WitherSkeleton) entity); }
                        else if (entity instanceof Skeleton){ return new CraftSkeleton(server, (Skeleton) entity); }
                        else return new catserver.server.entity.CraftCustomAbstractSkeleton(server, (AbstractSkeleton) entity); // CatServer
                    }
                    else if (entity instanceof Blaze) { return new CraftBlaze(server, (Blaze) entity); }
                    else if (entity instanceof Witch) { return new CraftWitch(server, (Witch) entity); }
                    else if (entity instanceof WitherBoss) { return new CraftWither(server, (WitherBoss) entity); }
                    else if (entity instanceof Spider) {
                        if (entity instanceof CaveSpider) { return new CraftCaveSpider(server, (CaveSpider) entity); }
                        else { return new CraftSpider(server, (Spider) entity); }
                    }
                    else if (entity instanceof Endermite) { return new CraftEndermite(server, (Endermite) entity); }
                    else if (entity instanceof Guardian) {
                        if (entity instanceof ElderGuardian) { return new CraftElderGuardian(server, (ElderGuardian) entity); }
                        else { return new CraftGuardian(server, (Guardian) entity); }
                    }
                    else if (entity instanceof Vex) { return new CraftVex(server, (Vex) entity); }
                    else if (entity instanceof AbstractIllager) {
                        if (entity instanceof SpellcasterIllager) {
                            if (entity instanceof Evoker) { return new CraftEvoker(server, (Evoker) entity); }
                            else if (entity instanceof Illusioner) { return new CraftIllusioner(server, (Illusioner) entity); }
                            else {  return new CraftSpellcaster(server, (SpellcasterIllager) entity); }
                        }
                        else if (entity instanceof Vindicator) { return new CraftVindicator(server, (Vindicator) entity); }
                        else if (entity instanceof Pillager) { return new CraftPillager(server, (Pillager) entity); }
                        else { return new CraftIllager(server, (AbstractIllager) entity); }
                    }
                    else if (entity instanceof Ravager) { return new CraftRavager(server, (Ravager) entity); }
                    else if (entity instanceof AbstractPiglin) {
                        if (entity instanceof Piglin) return new CraftPiglin(server, (Piglin) entity);
                        else if (entity instanceof PiglinBrute) { return new CraftPiglinBrute(server, (PiglinBrute) entity); }
                        else { return new CraftPiglinAbstract(server, (AbstractPiglin) entity); }
                    }
                    else if (entity instanceof Zoglin) { return new CraftZoglin(server, (Zoglin) entity); }
                    else if (entity instanceof Warden) { return new CraftWarden(server, (Warden) entity); }

                    else  { return new catserver.server.entity.CraftCustomMonster(server, (Monster) entity); } // CatServer
                }
                else if (entity instanceof AbstractGolem) {
                    if (entity instanceof SnowGolem) { return new CraftSnowman(server, (SnowGolem) entity); }
                    else if (entity instanceof IronGolem) { return new CraftIronGolem(server, (IronGolem) entity); }
                    else if (entity instanceof Shulker) { return new CraftShulker(server, (Shulker) entity); }
                    else return new catserver.server.entity.CraftCustomGolem(server, (AbstractGolem) entity); // CatServer
                }
                else if (entity instanceof AbstractVillager) {
                    if (entity instanceof Villager) { return new CraftVillager(server, (Villager) entity); }
                    else if (entity instanceof WanderingTrader) { return new CraftWanderingTrader(server, (WanderingTrader) entity); }
                    else { return new CraftAbstractVillager(server, (AbstractVillager) entity); }
                }
                else if (entity instanceof Allay) { return new CraftAllay(server, (Allay) entity); }
                else { return new CraftCreature(server, (PathfinderMob) entity); }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof Slime) {
                if (entity instanceof MagmaCube) { return new CraftMagmaCube(server, (MagmaCube) entity); }
                else { return new CraftSlime(server, (Slime) entity); }
            }
            // Flying
            else if (entity instanceof FlyingMob) {
                if (entity instanceof Ghast) { return new CraftGhast(server, (Ghast) entity); }
                else if (entity instanceof Phantom) { return new CraftPhantom(server, (Phantom) entity); }
                else { return new CraftFlying(server, (FlyingMob) entity); }
            }
            else if (entity instanceof EnderDragon) {
                return new CraftEnderDragon(server, (EnderDragon) entity);
            }
            // Ambient
            else if (entity instanceof AmbientCreature) {
                if (entity instanceof Bat) { return new CraftBat(server, (Bat) entity); }
                else { return new CraftAmbient(server, (AmbientCreature) entity); }
            }
            else if (entity instanceof ArmorStand) { return new CraftArmorStand(server, (ArmorStand) entity); }
            else  { return new CraftLivingEntity(server, (LivingEntity) entity); }
        }
        else if (entity instanceof EnderDragonPart) {
            EnderDragonPart part = (EnderDragonPart) entity;
            if (part.parentMob instanceof EnderDragon) { return new CraftEnderDragonPart(server, (EnderDragonPart) entity); }
            else { return new CraftComplexPart(server, (EnderDragonPart) entity); }
        }
        else if (entity instanceof ExperienceOrb) { return new CraftExperienceOrb(server, (ExperienceOrb) entity); }
        else if (entity instanceof Arrow) { return new CraftTippedArrow(server, (Arrow) entity); }
        else if (entity instanceof SpectralArrow) { return new CraftSpectralArrow(server, (SpectralArrow) entity); }
        else if (entity instanceof AbstractArrow) {
            if (entity instanceof ThrownTrident) { return new CraftTrident(server, (ThrownTrident) entity); }
            else { return new CraftArrow(server, (AbstractArrow) entity); }
        }
        else if (entity instanceof Boat) {
            if (entity instanceof ChestBoat) { return new CraftChestBoat(server, (ChestBoat) entity); }
            else { return new CraftBoat(server, (Boat) entity); }
        }
        else if (entity instanceof ThrowableProjectile) {
            if (entity instanceof ThrownEgg) { return new CraftEgg(server, (ThrownEgg) entity); }
            else if (entity instanceof Snowball) { return new CraftSnowball(server, (Snowball) entity); }
            else if (entity instanceof ThrownPotion) { return new CraftThrownPotion(server, (ThrownPotion) entity); }
            else if (entity instanceof ThrownEnderpearl) { return new CraftEnderPearl(server, (ThrownEnderpearl) entity); }
            else if (entity instanceof ThrownExperienceBottle) { return new CraftThrownExpBottle(server, (ThrownExperienceBottle) entity); }
            else return new catserver.server.entity.CraftCustomProjectile(server, entity); // CatServer
        }
        else if (entity instanceof FallingBlockEntity) { return new CraftFallingBlock(server, (FallingBlockEntity) entity); }
        else if (entity instanceof AbstractHurtingProjectile) {
            if (entity instanceof SmallFireball) { return new CraftSmallFireball(server, (SmallFireball) entity); }
            else if (entity instanceof LargeFireball) { return new CraftLargeFireball(server, (LargeFireball) entity); }
            else if (entity instanceof WitherSkull) { return new CraftWitherSkull(server, (WitherSkull) entity); }
            else if (entity instanceof DragonFireball) { return new CraftDragonFireball(server, (DragonFireball) entity); }
            else { return new CraftFireball(server, (AbstractHurtingProjectile) entity); }
        }
        else if (entity instanceof EyeOfEnder) { return new CraftEnderSignal(server, (EyeOfEnder) entity); }
        else if (entity instanceof EndCrystal) { return new CraftEnderCrystal(server, (EndCrystal) entity); }
        else if (entity instanceof FishingHook) { return new CraftFishHook(server, (FishingHook) entity); }
        else if (entity instanceof ItemEntity) { return new CraftItem(server, (ItemEntity) entity); }
        else if (entity instanceof LightningBolt) { return new CraftLightningStrike(server, (LightningBolt) entity); }
        else if (entity instanceof AbstractMinecart) {
            if (entity instanceof MinecartFurnace) { return new CraftMinecartFurnace(server, (MinecartFurnace) entity); }
            else if (entity instanceof MinecartChest) { return new CraftMinecartChest(server, (MinecartChest) entity); }
            else if (entity instanceof MinecartTNT) { return new CraftMinecartTNT(server, (MinecartTNT) entity); }
            else if (entity instanceof MinecartHopper) { return new CraftMinecartHopper(server, (MinecartHopper) entity); }
            else if (entity instanceof MinecartSpawner) { return new CraftMinecartMobSpawner(server, (MinecartSpawner) entity); }
            else if (entity instanceof Minecart) { return new CraftMinecartRideable(server, (Minecart) entity); }
            else if (entity instanceof MinecartCommandBlock) { return new CraftMinecartCommand(server, (MinecartCommandBlock) entity); }
            else return new catserver.server.entity.CraftCustomMinecart(server, (AbstractMinecart) entity); // CatServer
        } else if (entity instanceof HangingEntity) {
            if (entity instanceof Painting) { return new CraftPainting(server, (Painting) entity); }
            else if (entity instanceof ItemFrame) {
                if (entity instanceof GlowItemFrame) { return new CraftGlowItemFrame(server, (GlowItemFrame) entity); }
                else { return new CraftItemFrame(server, (ItemFrame) entity); }
            }
            else if (entity instanceof LeashFenceKnotEntity) { return new CraftLeash(server, (LeashFenceKnotEntity) entity); }
            else { return new CraftHanging(server, (HangingEntity) entity); }
        }
        else if (entity instanceof PrimedTnt) { return new CraftTNTPrimed(server, (PrimedTnt) entity); }
        else if (entity instanceof FireworkRocketEntity) { return new CraftFirework(server, (FireworkRocketEntity) entity); }
        else if (entity instanceof ShulkerBullet) { return new CraftShulkerBullet(server, (ShulkerBullet) entity); }
        else if (entity instanceof AreaEffectCloud) { return new CraftAreaEffectCloud(server, (AreaEffectCloud) entity); }
        else if (entity instanceof EvokerFangs) { return new CraftEvokerFangs(server, (EvokerFangs) entity); }
        else if (entity instanceof LlamaSpit) { return new CraftLlamaSpit(server, (LlamaSpit) entity); }
        else if (entity instanceof Marker) { return new CraftMarker(server, (Marker) entity); }
        else if (entity instanceof Interaction) { return new CraftInteraction(server, (Interaction) entity); }
        else if (entity instanceof Display) {
            if (entity instanceof Display.BlockDisplay) { return new CraftBlockDisplay(server, (Display.BlockDisplay) entity); }
            else if (entity instanceof Display.ItemDisplay) { return new CraftItemDisplay(server, (Display.ItemDisplay) entity); }
            else if (entity instanceof Display.TextDisplay) { return new CraftTextDisplay(server, (Display.TextDisplay) entity); }
            else { return new CraftDisplay(server, (Display) entity); }
        }
        // CatServer start
        else if (entity instanceof net.minecraft.world.entity.projectile.Projectile) {
            return new catserver.server.entity.CraftCustomProjectile(server, entity);
        } else return new catserver.server.entity.CraftCustomEntity(server, entity);
        // CatServer end
        // CHECKSTYLE:ON

        // throw new AssertionError("Unknown entity " + (entity == null ? null : entity.getClass())); // CatServer - remove
    }

    @Override
    public Location getLocation() {
        return CraftLocation.toBukkit(entity.position(), getWorld(), entity.getBukkitYaw(), entity.getXRot());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(entity.getX());
            loc.setY(entity.getY());
            loc.setZ(entity.getZ());
            loc.setYaw(entity.getBukkitYaw());
            loc.setPitch(entity.getXRot());
        }

        return loc;
    }

    @Override
    public Vector getVelocity() {
        return CraftVector.toBukkit(entity.getDeltaMovement());
    }

    @Override
    public void setVelocity(Vector velocity) {
        Preconditions.checkArgument(velocity != null, "velocity");
        velocity.checkFinite();
        entity.setDeltaMovement(CraftVector.toNMS(velocity));
        entity.hurtMarked = true;
    }

    @Override
    public double getHeight() {
        return getHandle().getBbHeight();
    }

    @Override
    public double getWidth() {
        return getHandle().getBbWidth();
    }

    @Override
    public BoundingBox getBoundingBox() {
        AABB bb = getHandle().getBoundingBox();
        return new BoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    @Override
    public boolean isOnGround() {
        if (entity instanceof AbstractArrow) {
            return ((AbstractArrow) entity).inGround;
        }
        return entity.onGround();
    }

    @Override
    public boolean isInWater() {
        return entity.isInWater();
    }

    @Override
    public World getWorld() {
        return entity.level().getWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");

        yaw = Location.normalizeYaw(yaw);
        pitch = Location.normalizePitch(pitch);

        entity.setYRot(yaw);
        entity.setXRot(pitch);
        entity.yRotO = yaw;
        entity.xRotO = pitch;
        entity.setYHeadRot(yaw);
    }

    @Override
    public boolean teleport(Location location) {
        return teleport(location, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location cannot be null");
        location.checkFinite();

        if (entity.isVehicle() || entity.isRemoved()) {
            return false;
        }

        // If this entity is riding another entity, we must dismount before teleporting.
        entity.stopRiding();

        // Let the server handle cross world teleports
        if (location.getWorld() != null && !location.getWorld().equals(getWorld())) {
            // Prevent teleportation to an other world during world generation
            Preconditions.checkState(!entity.generation, "Cannot teleport entity to an other world during world generation");
            entity.teleportTo(((CraftWorld) location.getWorld()).getHandle(), CraftLocation.toPosition(location));
            return true;
        }

        // entity.setLocation() throws no event, and so cannot be cancelled
        entity.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // SPIGOT-619: Force sync head rotation also
        entity.setYHeadRot(location.getYaw());

        return true;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination) {
        return teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    @Override
    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        Preconditions.checkState(!entity.generation, "Cannot get nearby entities during world generation");
        // org.spigotmc.AsyncCatcher.catchOp("getNearbyEntities"); // Spigot // CatServer - remove

        List<Entity> notchEntityList = entity.level().getEntities(entity, entity.getBoundingBox().inflate(x, y, z), Predicates.alwaysTrue());
        List<org.bukkit.entity.Entity> bukkitEntityList = new java.util.ArrayList<org.bukkit.entity.Entity>(notchEntityList.size());

        for (Entity e : notchEntityList) {
            bukkitEntityList.add(e.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    @Override
    public int getEntityId() {
        return entity.getId();
    }

    @Override
    public int getFireTicks() {
        return entity.getRemainingFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return entity.getFireImmuneTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        entity.setRemainingFireTicks(ticks);
    }

    @Override
    public void setVisualFire(boolean fire) {
        getHandle().hasVisualFire = fire;
    }

    @Override
    public boolean isVisualFire() {
        return getHandle().hasVisualFire;
    }

    @Override
    public int getFreezeTicks() {
        return getHandle().getTicksFrozen();
    }

    @Override
    public int getMaxFreezeTicks() {
        return getHandle().getTicksRequiredToFreeze();
    }

    @Override
    public void setFreezeTicks(int ticks) {
        Preconditions.checkArgument(0 <= ticks, "Ticks (%s) cannot be less than 0", ticks);

        getHandle().setTicksFrozen(ticks);
    }

    @Override
    public boolean isFrozen() {
        return getHandle().isFullyFrozen();
    }

    @Override
    public void remove() {
        entity.discard();
    }

    @Override
    public boolean isDead() {
        return !entity.isAlive();
    }

    @Override
    public boolean isValid() {
        return entity.isAlive() && entity.valid && entity.isChunkLoaded();
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public boolean isPersistent() {
        return entity.persist;
    }

    @Override
    public void setPersistent(boolean persistent) {
        entity.persist = persistent;
    }

    public Vector getMomentum() {
        return getVelocity();
    }

    public void setMomentum(Vector value) {
        setVelocity(value);
    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
        return isEmpty() ? null : getHandle().getPassengers().get(0).getBukkitEntity();
    }

    @Override
    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");
        if (passenger instanceof CraftEntity) {
            eject();
            return ((CraftEntity) passenger).getHandle().startRiding(getHandle());
        } else {
            return false;
        }
    }

    @Override
    public List<org.bukkit.entity.Entity> getPassengers() {
        return Lists.newArrayList(Lists.transform(getHandle().getPassengers(), (Function<Entity, org.bukkit.entity.Entity>) input -> input.getBukkitEntity())); // CatServer
    }

    @Override
    public boolean addPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "Entity passenger cannot be null");
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");

        return ((CraftEntity) passenger).getHandle().startRiding(getHandle(), true);
    }

    @Override
    public boolean removePassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "Entity passenger cannot be null");

        ((CraftEntity) passenger).getHandle().stopRiding();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return !getHandle().isVehicle();
    }

    @Override
    public boolean eject() {
        if (isEmpty()) {
            return false;
        }

        getHandle().ejectPassengers();
        return true;
    }

    @Override
    public float getFallDistance() {
        return getHandle().fallDistance;
    }

    @Override
    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        lastDamageEvent = event;
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return lastDamageEvent;
    }

    @Override
    public UUID getUniqueId() {
        return getHandle().getUUID();
    }

    @Override
    public int getTicksLived() {
        return getHandle().tickCount;
    }

    @Override
    public void setTicksLived(int value) {
        Preconditions.checkArgument(value > 0, "Age value (%s) must be greater than 0", value);
        getHandle().tickCount = value;
    }

    public Entity getHandle() {
        return entity;
    }

    @Override
    public EntityType getType() { // CatServer - remove final
        return entityType;
    }

    @Override
    public void playEffect(EntityEffect type) {
        Preconditions.checkArgument(type != null, "type");
        Preconditions.checkState(!entity.generation, "Cannot play effect during world generation");

        if (type.getApplicable().isInstance(this)) {
            this.getHandle().level().broadcastEntityEvent(getHandle(), type.getData());
        }
    }

    @Override
    public Sound getSwimSound() {
        return CraftSound.getBukkit(getHandle().getSwimSound0());
    }

    @Override
    public Sound getSwimSplashSound() {
        return CraftSound.getBukkit(getHandle().getSwimSplashSound0());
    }

    @Override
    public Sound getSwimHighSpeedSplashSound() {
        return CraftSound.getBukkit(getHandle().getSwimHighSpeedSplashSound0());
    }

    public void setHandle(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "CraftEntity{" + "id=" + getEntityId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftEntity other = (CraftEntity) obj;
        return (this.getEntityId() == other.getEntityId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.getEntityId();
        return hash;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean isInsideVehicle() {
        return getHandle().isPassenger();
    }

    @Override
    public boolean leaveVehicle() {
        if (!isInsideVehicle()) {
            return false;
        }

        getHandle().stopRiding();
        return true;
    }

    @Override
    public org.bukkit.entity.Entity getVehicle() {
        if (!isInsideVehicle()) {
            return null;
        }

        return getHandle().getVehicle().getBukkitEntity();
    }

    @Override
    public void setCustomName(String name) {
        // sane limit for name length
        if (name != null && name.length() > 256) {
            name = name.substring(0, 256);
        }

        getHandle().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public String getCustomName() {
        Component name = getHandle().getCustomName();

        if (name == null) {
            return null;
        }

        return CraftChatMessage.fromComponent(name);
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        getHandle().setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getHandle().isCustomNameVisible();
    }

    @Override
    public void setVisibleByDefault(boolean visible) {
        if (getHandle().visibleByDefault != visible) {
            if (visible) {
                // Making visible by default, reset and show to all players
                for (Player player : server.getOnlinePlayers()) {
                    ((CraftPlayer) player).resetAndShowEntity(this);
                }
            } else {
                // Hiding by default, reset and hide from all players
                for (Player player : server.getOnlinePlayers()) {
                    ((CraftPlayer) player).resetAndHideEntity(this);
                }
            }

            getHandle().visibleByDefault = visible;
        }
    }

    @Override
    public boolean isVisibleByDefault() {
        return getHandle().visibleByDefault;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String... messages) {

    }

    @Override
    public void sendMessage(UUID sender, String message) {
        this.sendMessage(message); // Most entities don't know about senders
    }

    @Override
    public void sendMessage(UUID sender, String... messages) {
        this.sendMessage(messages); // Most entities don't know about senders
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(getHandle().getName());
    }

    @Override
    public boolean isPermissionSet(String name) {
        return getPermissibleBase().isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return CraftEntity.getPermissibleBase().isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return getPermissibleBase().hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return getPermissibleBase().hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return getPermissibleBase().addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return getPermissibleBase().addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return getPermissibleBase().addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return getPermissibleBase().addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
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
    public void setOp(boolean value) {
        getPermissibleBase().setOp(value);
    }

    @Override
    public void setGlowing(boolean flag) {
        getHandle().setGlowingTag(flag);
    }

    @Override
    public boolean isGlowing() {
        return getHandle().isCurrentlyGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        getHandle().setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return getHandle().isInvulnerableTo(getHandle().damageSources().generic());
    }

    @Override
    public boolean isSilent() {
        return getHandle().isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        getHandle().setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return !getHandle().isNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return getHandle().portalCooldown;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        getHandle().portalCooldown = cooldown;
    }

    @Override
    public Set<String> getScoreboardTags() {
        return getHandle().getTags();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return getHandle().addTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return getHandle().removeTag(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getHandle().getPistonPushReaction().ordinal());
    }

    @Override
    public BlockFace getFacing() {
        // Use this method over getDirection because it handles boats and minecarts.
        return CraftBlock.notchToBlockFace(getHandle().getMotionDirection());
    }

    @Override
    public CraftPersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    @Override
    public Pose getPose() {
        return Pose.values()[getHandle().getPose().ordinal()];
    }

    @Override
    public SpawnCategory getSpawnCategory() {
        return CraftSpawnCategory.toBukkit(getHandle().getType().getCategory());
    }

    public void storeBukkitValues(CompoundTag c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.put("BukkitValues", this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(CompoundTag c) {
        Tag base = c.get("BukkitValues");
        if (base instanceof CompoundTag) {
            this.persistentDataContainer.putAll((CompoundTag) base);
        }
    }

    protected CompoundTag save() {
        CompoundTag nbttagcompound = new CompoundTag();

        nbttagcompound.putString("id", getHandle().getEncodeId());
        getHandle().saveWithoutId(nbttagcompound);

        return nbttagcompound;
    }

    // re-sends the spawn entity packet to updated values which cannot be updated otherwise
    protected void update() {
        if (!getHandle().isAlive()) {
            return;
        }

        ServerLevel world = ((CraftWorld) getWorld()).getHandle();
        ChunkMap.TrackedEntity entityTracker = world.getChunkSource().chunkMap.entityMap.get(getEntityId());

        if (entityTracker == null) {
            return;
        }

        entityTracker.broadcast(getHandle().getAddEntityPacket());
    }

    private static PermissibleBase getPermissibleBase() {
        if (perm == null) {
            perm = new PermissibleBase(new ServerOperator() {

                @Override
                public boolean isOp() {
                    return false;
                }

                @Override
                public void setOp(boolean value) {

                }
            });
        }
        return perm;
    }

    // Spigot start
    private final org.bukkit.entity.Entity.Spigot spigot = new org.bukkit.entity.Entity.Spigot()
    {

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component)
        {
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components)
        {
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent... components)
        {
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent component)
        {
        }
    };

    public org.bukkit.entity.Entity.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
