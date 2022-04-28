package org.bukkit.craftbukkit.v1_16_R3.entity;

import catserver.server.entity.CraftCustomChestHorse;
import catserver.server.entity.CraftCustomEntity;
import catserver.server.entity.CraftCustomProjectile;
import catserver.server.entity.CraftFakePlayer;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.item.ExperienceBottleEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.LeashKnotEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.CodEntity;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.entity.passive.fish.SalmonEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.passive.horse.TraderLlamaEntity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.entity.projectile.EyeOfEnderEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_16_R3.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftVector;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.CreatureSpawnEvent;
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

public abstract class CraftEntity implements org.bukkit.entity.Entity {
    private static PermissibleBase perm;
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    protected final CraftServer server;
    protected Entity entity;
    private EntityDamageEvent lastDamageEvent;
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public static CraftEntity getEntity(CraftServer server, Entity entity) {
        if (entity instanceof LivingEntity) {
            // Players
            if (entity instanceof PlayerEntity) {
                // CatServer start - support fake player
                if (entity instanceof ServerPlayerEntity) {
                    if (entity instanceof FakePlayer) { return new CraftFakePlayer(server, (FakePlayer) entity); }
                    return new CraftPlayer(server, (ServerPlayerEntity) entity);
                } else {
                    return new CraftFakePlayer(server, FakePlayerFactory.get(server.getServer().getLevel(entity.level.dimension()), ((PlayerEntity) entity).getGameProfile()));
                }
                // CatServer end
            }
            // Water Animals
            else if (entity instanceof WaterMobEntity) {
                if (entity instanceof SquidEntity) { return new CraftSquid(server, (SquidEntity) entity); }
                else if (entity instanceof AbstractFishEntity) {
                    if (entity instanceof CodEntity) { return new CraftCod(server, (CodEntity) entity); }
                    else if (entity instanceof PufferfishEntity) { return new CraftPufferFish(server, (PufferfishEntity) entity); }
                    else if (entity instanceof SalmonEntity) { return new CraftSalmon(server, (SalmonEntity) entity); }
                    else if (entity instanceof TropicalFishEntity) { return new CraftTropicalFish(server, (TropicalFishEntity) entity); }
                    else { return new CraftFish(server, (AbstractFishEntity) entity); }
                }
                else if (entity instanceof DolphinEntity) { return new CraftDolphin(server, (DolphinEntity) entity); }
                else { return new CraftWaterMob(server, (WaterMobEntity) entity); }
            }
            else if (entity instanceof CreatureEntity) {
                // Animals
                if (entity instanceof AnimalEntity) {
                    if (entity instanceof ChickenEntity) { return new CraftChicken(server, (ChickenEntity) entity); }
                    else if (entity instanceof CowEntity) {
                        if (entity instanceof MooshroomEntity) { return new CraftMushroomCow(server, (MooshroomEntity) entity); }
                        else { return new CraftCow(server, (CowEntity) entity); }
                    }
                    else if (entity instanceof PigEntity) { return new CraftPig(server, (PigEntity) entity); }
                    else if (entity instanceof TameableEntity) {
                        if (entity instanceof WolfEntity) { return new CraftWolf(server, (WolfEntity) entity); }
                        else if (entity instanceof CatEntity) { return new CraftCat(server, (CatEntity) entity); }
                        else if (entity instanceof ParrotEntity) { return new CraftParrot(server, (ParrotEntity) entity); }
                        else { return new CraftTameableAnimal(server, (TameableEntity) entity); }
                    }
                    else if (entity instanceof SheepEntity) { return new CraftSheep(server, (SheepEntity) entity); }
                    else if (entity instanceof AbstractHorseEntity) {
                        if (entity instanceof AbstractChestedHorseEntity){
                            if (entity instanceof DonkeyEntity) { return new CraftDonkey(server, (DonkeyEntity) entity); }
                            else if (entity instanceof MuleEntity) { return new CraftMule(server, (MuleEntity) entity); }
                            else if (entity instanceof TraderLlamaEntity) { return new CraftTraderLlama(server, (TraderLlamaEntity) entity); }
                            else if (entity instanceof LlamaEntity) { return new CraftLlama(server, (LlamaEntity) entity); }
                        } else if (entity instanceof HorseEntity) { return new CraftHorse(server, (HorseEntity) entity); }
                        else if (entity instanceof SkeletonHorseEntity) { return new CraftSkeletonHorse(server, (SkeletonHorseEntity) entity); }
                        else if (entity instanceof ZombieHorseEntity) { return new CraftZombieHorse(server, (ZombieHorseEntity) entity); }
                        else { return new CraftCustomChestHorse(server, (AbstractHorseEntity) entity); }
                    }
                    else if (entity instanceof RabbitEntity) { return new CraftRabbit(server, (RabbitEntity) entity); }
                    else if (entity instanceof PolarBearEntity) { return new CraftPolarBear(server, (PolarBearEntity) entity); }
                    else if (entity instanceof TurtleEntity) { return new CraftTurtle(server, (TurtleEntity) entity); }
                    else if (entity instanceof OcelotEntity) { return new CraftOcelot(server, (OcelotEntity) entity); }
                    else if (entity instanceof PandaEntity) { return new CraftPanda(server, (PandaEntity) entity); }
                    else if (entity instanceof FoxEntity) { return new CraftFox(server, (FoxEntity) entity); }
                    else if (entity instanceof BeeEntity) { return new CraftBee(server, (BeeEntity) entity); }
                    else if (entity instanceof HoglinEntity) { return new CraftHoglin(server, (HoglinEntity) entity); }
                    else if (entity instanceof StriderEntity) { return new CraftStrider(server, (StriderEntity) entity); }
                    else  { return new CraftAnimals(server, (AnimalEntity) entity); }
                }
                // Monsters
                else if (entity instanceof MonsterEntity) {
                    if (entity instanceof ZombieEntity) {
                        if (entity instanceof ZombifiedPiglinEntity) { return new CraftPigZombie(server, (ZombifiedPiglinEntity) entity); }
                        else if (entity instanceof HuskEntity) { return new CraftHusk(server, (HuskEntity) entity); }
                        else if (entity instanceof ZombieVillagerEntity) { return new CraftVillagerZombie(server, (ZombieVillagerEntity) entity); }
                        else if (entity instanceof DrownedEntity) { return new CraftDrowned(server, (DrownedEntity) entity); }
                        else { return new CraftZombie(server, (ZombieEntity) entity); }
                    }
                    else if (entity instanceof CreeperEntity) { return new CraftCreeper(server, (CreeperEntity) entity); }
                    else if (entity instanceof EndermanEntity) { return new CraftEnderman(server, (EndermanEntity) entity); }
                    else if (entity instanceof SilverfishEntity) { return new CraftSilverfish(server, (SilverfishEntity) entity); }
                    else if (entity instanceof GiantEntity) { return new CraftGiant(server, (GiantEntity) entity); }
                    else if (entity instanceof AbstractSkeletonEntity) {
                        if (entity instanceof StrayEntity) { return new CraftStray(server, (StrayEntity) entity); }
                        else if (entity instanceof WitherSkeletonEntity) { return new CraftWitherSkeleton(server, (WitherSkeletonEntity) entity); }
                        else { return new CraftSkeleton(server, (AbstractSkeletonEntity) entity); }
                    }
                    else if (entity instanceof BlazeEntity) { return new CraftBlaze(server, (BlazeEntity) entity); }
                    else if (entity instanceof WitchEntity) { return new CraftWitch(server, (WitchEntity) entity); }
                    else if (entity instanceof WitherEntity) { return new CraftWither(server, (WitherEntity) entity); }
                    else if (entity instanceof SpiderEntity) {
                        if (entity instanceof CaveSpiderEntity) { return new CraftCaveSpider(server, (CaveSpiderEntity) entity); }
                        else { return new CraftSpider(server, (SpiderEntity) entity); }
                    }
                    else if (entity instanceof EndermiteEntity) { return new CraftEndermite(server, (EndermiteEntity) entity); }
                    else if (entity instanceof GuardianEntity) {
                        if (entity instanceof ElderGuardianEntity) { return new CraftElderGuardian(server, (ElderGuardianEntity) entity); }
                        else { return new CraftGuardian(server, (GuardianEntity) entity); }
                    }
                    else if (entity instanceof VexEntity) { return new CraftVex(server, (VexEntity) entity); }
                    else if (entity instanceof AbstractIllagerEntity) {
                        if (entity instanceof SpellcastingIllagerEntity) {
                            if (entity instanceof EvokerEntity) { return new CraftEvoker(server, (EvokerEntity) entity); }
                            else if (entity instanceof IllusionerEntity) { return new CraftIllusioner(server, (IllusionerEntity) entity); }
                            else {  return new CraftSpellcaster(server, (SpellcastingIllagerEntity) entity); }
                        }
                        else if (entity instanceof VindicatorEntity) { return new CraftVindicator(server, (VindicatorEntity) entity); }
                        else if (entity instanceof PillagerEntity) { return new CraftPillager(server, (PillagerEntity) entity); }
                        else { return new CraftIllager(server, (AbstractIllagerEntity) entity); }
                    }
                    else if (entity instanceof RavagerEntity) { return new CraftRavager(server, (RavagerEntity) entity); }
                    else if (entity instanceof AbstractPiglinEntity) {
                        if (entity instanceof PiglinEntity) return new CraftPiglin(server, (PiglinEntity) entity);
                        else if (entity instanceof PiglinBruteEntity) { return new CraftPiglinBrute(server, (PiglinBruteEntity) entity); }
                        else { return new CraftPiglinAbstract(server, (AbstractPiglinEntity) entity); }
                    }
                    else if (entity instanceof ZoglinEntity) { return new CraftZoglin(server, (ZoglinEntity) entity); }
                    else  { return new CraftMonster(server, (MonsterEntity) entity); }
                }
                else if (entity instanceof GolemEntity) {
                    if (entity instanceof SnowGolemEntity) { return new CraftSnowman(server, (SnowGolemEntity) entity); }
                    else if (entity instanceof IronGolemEntity) { return new CraftIronGolem(server, (IronGolemEntity) entity); }
                    else if (entity instanceof ShulkerEntity) { return new CraftShulker(server, (ShulkerEntity) entity); }
                    else { return new CraftGolem(server, (GolemEntity) entity); }
                }
                else if (entity instanceof AbstractVillagerEntity) {
                    if (entity instanceof VillagerEntity) { return new CraftVillager(server, (VillagerEntity) entity); }
                    else if (entity instanceof WanderingTraderEntity) { return new CraftWanderingTrader(server, (WanderingTraderEntity) entity); }
                    else { return new CraftAbstractVillager(server, (AbstractVillagerEntity) entity); }
                }
                else { return new CraftCreature(server, (CreatureEntity) entity); }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof SlimeEntity) {
                if (entity instanceof MagmaCubeEntity) { return new CraftMagmaCube(server, (MagmaCubeEntity) entity); }
                else { return new CraftSlime(server, (SlimeEntity) entity); }
            }
            // Flying
            else if (entity instanceof FlyingEntity) {
                if (entity instanceof GhastEntity) { return new CraftGhast(server, (GhastEntity) entity); }
                else if (entity instanceof PhantomEntity) { return new CraftPhantom(server, (PhantomEntity) entity); }
                else { return new CraftFlying(server, (FlyingEntity) entity); }
            }
            else if (entity instanceof EnderDragonEntity) {
                return new CraftEnderDragon(server, (EnderDragonEntity) entity);
            }
            // Ambient
            else if (entity instanceof AmbientEntity) {
                if (entity instanceof BatEntity) { return new CraftBat(server, (BatEntity) entity); }
                else { return new CraftAmbient(server, (AmbientEntity) entity); }
            }
            else if (entity instanceof ArmorStandEntity) { return new CraftArmorStand(server, (ArmorStandEntity) entity); }
            else  { return new CraftLivingEntity(server, (LivingEntity) entity); }
        }
        else if (entity instanceof EnderDragonPartEntity) {
            EnderDragonPartEntity part = (EnderDragonPartEntity) entity;
            if (part.parentMob instanceof EnderDragonEntity) { return new CraftEnderDragonPart(server, (EnderDragonPartEntity) entity); }
            else { return new CraftComplexPart(server, (EnderDragonPartEntity) entity); }
        }
        else if (entity instanceof ExperienceOrbEntity) { return new CraftExperienceOrb(server, (ExperienceOrbEntity) entity); }
        else if (entity instanceof ArrowEntity) { return new CraftTippedArrow(server, (ArrowEntity) entity); }
        else if (entity instanceof SpectralArrowEntity) { return new CraftSpectralArrow(server, (SpectralArrowEntity) entity); }
        else if (entity instanceof AbstractArrowEntity) {
            if (entity instanceof TridentEntity) { return new CraftTrident(server, (TridentEntity) entity); }
            else { return new CraftArrow(server, (AbstractArrowEntity) entity); }
        }
        else if (entity instanceof BoatEntity) { return new CraftBoat(server, (BoatEntity) entity); }
        else if (entity instanceof ThrowableEntity) {
            if (entity instanceof EggEntity) { return new CraftEgg(server, (EggEntity) entity); }
            else if (entity instanceof SnowballEntity) { return new CraftSnowball(server, (SnowballEntity) entity); }
            else if (entity instanceof PotionEntity) { return new CraftThrownPotion(server, (PotionEntity) entity); }
            else if (entity instanceof EnderPearlEntity) { return new CraftEnderPearl(server, (EnderPearlEntity) entity); }
            else if (entity instanceof ExperienceBottleEntity) { return new CraftThrownExpBottle(server, (ExperienceBottleEntity) entity); }
            else { return new CraftCustomProjectile(server, (ThrowableEntity) entity); }
        }
        else if (entity instanceof FallingBlockEntity) { return new CraftFallingBlock(server, (FallingBlockEntity) entity); }
        else if (entity instanceof DamagingProjectileEntity) {
            if (entity instanceof SmallFireballEntity) { return new CraftSmallFireball(server, (SmallFireballEntity) entity); }
            else if (entity instanceof FireballEntity) { return new CraftLargeFireball(server, (FireballEntity) entity); }
            else if (entity instanceof WitherSkullEntity) { return new CraftWitherSkull(server, (WitherSkullEntity) entity); }
            else if (entity instanceof DragonFireballEntity) { return new CraftDragonFireball(server, (DragonFireballEntity) entity); }
            else { return new CraftFireball(server, (DamagingProjectileEntity) entity); }
        }
        else if (entity instanceof EyeOfEnderEntity) { return new CraftEnderSignal(server, (EyeOfEnderEntity) entity); }
        else if (entity instanceof EnderCrystalEntity) { return new CraftEnderCrystal(server, (EnderCrystalEntity) entity); }
        else if (entity instanceof FishingBobberEntity) { return new CraftFishHook(server, (FishingBobberEntity) entity); }
        else if (entity instanceof ItemEntity) { return new CraftItem(server, (ItemEntity) entity); }
        else if (entity instanceof LightningBoltEntity) { return new CraftLightningStrike(server, (LightningBoltEntity) entity); }
        else if (entity instanceof AbstractMinecartEntity) {
            if (entity instanceof FurnaceMinecartEntity) { return new CraftMinecartFurnace(server, (FurnaceMinecartEntity) entity); }
            else if (entity instanceof ChestMinecartEntity) { return new CraftMinecartChest(server, (ChestMinecartEntity) entity); }
            else if (entity instanceof TNTMinecartEntity) { return new CraftMinecartTNT(server, (TNTMinecartEntity) entity); }
            else if (entity instanceof HopperMinecartEntity) { return new CraftMinecartHopper(server, (HopperMinecartEntity) entity); }
            else if (entity instanceof SpawnerMinecartEntity) { return new CraftMinecartMobSpawner(server, (SpawnerMinecartEntity) entity); }
            else if (entity instanceof MinecartEntity) { return new CraftMinecartRideable(server, (MinecartEntity) entity); }
            else if (entity instanceof CommandBlockMinecartEntity) { return new CraftMinecartCommand(server, (CommandBlockMinecartEntity) entity); }
            else { return new CraftMinecart(server, (AbstractMinecartEntity) entity); }
        } else if (entity instanceof HangingEntity) {
            if (entity instanceof PaintingEntity) { return new CraftPainting(server, (PaintingEntity) entity); }
            else if (entity instanceof ItemFrameEntity) { return new CraftItemFrame(server, (ItemFrameEntity) entity); }
            else if (entity instanceof LeashKnotEntity) { return new CraftLeash(server, (LeashKnotEntity) entity); }
            else { return new CraftHanging(server, (HangingEntity) entity); }
        }
        else if (entity instanceof TNTEntity) { return new CraftTNTPrimed(server, (TNTEntity) entity); }
        else if (entity instanceof FireworkRocketEntity) { return new CraftFirework(server, (FireworkRocketEntity) entity); }
        else if (entity instanceof ShulkerBulletEntity) { return new CraftShulkerBullet(server, (ShulkerBulletEntity) entity); }
        else if (entity instanceof AreaEffectCloudEntity) { return new CraftAreaEffectCloud(server, (AreaEffectCloudEntity) entity); }
        else if (entity instanceof EvokerFangsEntity) { return new CraftEvokerFangs(server, (EvokerFangsEntity) entity); }
        else if (entity instanceof LlamaSpitEntity) { return new CraftLlamaSpit(server, (LlamaSpitEntity) entity); }
        else if (entity instanceof ProjectileEntity) { return new CraftCustomProjectile(server, (ProjectileEntity) entity); }
        else if (entity != null) { return new CraftCustomEntity(server, entity);}
        throw new AssertionError("Unknown entity " + (entity == null ? " is null" : entity.getClass() + ": " + entity));
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), entity.getX(), entity.getY(), entity.getZ(), entity.getBukkitYaw(), entity.xRot);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(entity.getX());
            loc.setY(entity.getY());
            loc.setZ(entity.getZ());
            loc.setYaw(entity.getBukkitYaw());
            loc.setPitch(entity.xRot);
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
        AxisAlignedBB bb = getHandle().getBoundingBox();
        return new BoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    @Override
    public boolean isOnGround() {
        if (entity instanceof AbstractArrowEntity) {
            return ((AbstractArrowEntity) entity).inGround;
        }
        return entity.isOnGround();
    }

    @Override
    public boolean isInWater() {
        return entity.isInWater();
    }

    @Override
    public World getWorld() {
        return entity.level.getWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");

        yaw = Location.normalizeYaw(yaw);
        pitch = Location.normalizePitch(pitch);

        entity.yRot = yaw;
        entity.xRot = pitch;
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
        Preconditions.checkArgument(location != null, "location");
        location.checkFinite();

        if (entity.isVehicle() || entity.removed) {
            return false;
        }

        // If this entity is riding another entity, we must dismount before teleporting.
        entity.stopRiding();
        entity.level = ((CraftWorld) location.getWorld()).getHandle();

        // Let the server handle cross world teleports
        //if (!location.getWorld().equals(getWorld())) {
        //    entity.teleportTo(((CraftWorld) location.getWorld()).getHandle(), new BlockPos(location.getX(), location.getY(), location.getZ()));
        //    return true;
        //}

        // entity.setLocation() throws no event, and so cannot be cancelled
        entity.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); // Paper - use proper setPositionRotation for teleportation
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
        List<Entity> notchEntityList = entity.level.getEntities(entity, entity.getBoundingBox().inflate(x, y, z), null);
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
        return entity.remainingFireTicks;
    }

    @Override
    public int getMaxFireTicks() {
        return entity.getFireImmuneTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        entity.remainingFireTicks = ticks;
    }

    @Override
    public void remove() {
        entity.remove();
    }

    public void remove(boolean keepData) {
        entity.remove(keepData);
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
        return isEmpty() ? null : getHandle().passengers.get(0).getBukkitEntity();
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
        return Lists.newArrayList(Lists.transform(getHandle().passengers, new Function<Entity, org.bukkit.entity.Entity>() {
            @Override
            public org.bukkit.entity.Entity apply(Entity input) {
                return input.getBukkitEntity();
            }
        }));
    }

    @Override
    public boolean addPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "passenger == null");

        return ((CraftEntity) passenger).getHandle().startRiding(getHandle(), true);
    }

    @Override
    public boolean removePassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "passenger == null");

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
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        getHandle().tickCount = value;
    }

    public Entity getHandle() {
        return entity;
    }

    @Override
    public void playEffect(EntityEffect type) {
        Preconditions.checkArgument(type != null, "type");

        if (type.getApplicable().isInstance(this)) {
            this.getHandle().level.broadcastEntityEvent(getHandle(), type.getData());
        }
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
        return (this.getHandle() == other.getHandle()); // Paper - while logically the same, this is clearer
    }

    // Paper - Fix hashCode. entity ID's are not static.
    // A CraftEntity can change reference to a new entity with a new ID, and hash codes should never change
    @Override
    public int hashCode() {
        return getUniqueId().hashCode();
        // Paper end
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
        ITextComponent name = getHandle().getCustomName();

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
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] messages) {

    }

    @Override
    public void sendMessage(UUID sender, String message) {
        this.sendMessage(message); // Most entities don't know about senders
    }

    @Override
    public void sendMessage(UUID sender, String[] messages) {
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
        getHandle().glowing = flag;
        Entity e = getHandle();
        if (e.getSharedFlag(6) != flag) {
            e.setSharedFlag(6, flag);
        }
    }

    @Override
    public boolean isGlowing() {
        return getHandle().glowing;
    }

    @Override
    public void setInvulnerable(boolean flag) {
        getHandle().setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return getHandle().isInvulnerableTo(DamageSource.GENERIC);
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
    public CreatureSpawnEvent.SpawnReason getEntitySpawnReason(){
        return CreatureSpawnEvent.SpawnReason.DEFAULT;
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

    public void storeBukkitValues(CompoundNBT c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.put("BukkitValues", this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(CompoundNBT c) {
        INBT base = c.get("BukkitValues");
        if (base != null) {
            this.persistentDataContainer.putAll((CompoundNBT) base);
        }
    }

    protected CompoundNBT save() {
        CompoundNBT nbttagcompound = new CompoundNBT();

        nbttagcompound.putString("id", getHandle().getEncodeId());
        getHandle().saveWithoutId(nbttagcompound);

        return nbttagcompound;
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
    private final Spigot spigot = new Spigot()
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

    @Override
    public Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
