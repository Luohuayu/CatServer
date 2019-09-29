package org.bukkit.craftbukkit.entity;

import catserver.server.PlayerDataFixer;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import catserver.server.entity.CraftCustomEntity;
import catserver.server.entity.CraftCustomProjectile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.DamageSource;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class CraftEntity implements org.bukkit.entity.Entity {
    private static PermissibleBase perm;

    protected final CraftServer server;
    protected Entity entity;
    private EntityDamageEvent lastDamageEvent;

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public static CraftEntity getEntity(CraftServer server, Entity entity) {
        /**
         * Order is *EXTREMELY* important -- keep it right! =D
         */
        if (entity instanceof EntityLivingBase) {
            // Players
            if (entity instanceof EntityPlayer) {
                if (entity instanceof EntityPlayerMP) {
                    // CatServer start - support fake player
                    if (entity instanceof FakePlayer)
                        return new CraftFuckPlayer(server, (FakePlayer) entity);
                    return new CraftPlayer(server, (EntityPlayerMP) entity);
                }
                else { // CatServer -  support fake player classes from mods
                    return new CraftFuckPlayer(server, FakePlayerFactory.get(DimensionManager.getWorld(entity.world.provider.getDimension()), ((EntityPlayer) entity).getGameProfile()));
                }
                // CatServer end - support fake player
            }
            // Water Animals
            else if (entity instanceof EntityWaterMob) {
                if (entity instanceof EntitySquid) { return new CraftSquid(server, (EntitySquid) entity); }
                else { return new CraftWaterMob(server, (EntityWaterMob) entity); }
            }
            else if (entity instanceof EntityCreature) {
                // Animals
                if (entity instanceof EntityAnimal) {
                    if (entity instanceof EntityChicken) { return new CraftChicken(server, (EntityChicken) entity); }
                    else if (entity instanceof EntityCow) {
                        if (entity instanceof EntityMooshroom) { return new CraftMushroomCow(server, (EntityMooshroom) entity); }
                        else { return new CraftCow(server, (EntityCow) entity); }
                    }
                    else if (entity instanceof EntityPig) { return new CraftPig(server, (EntityPig) entity); }
                    else if (entity instanceof EntityTameable) {
                        if (entity instanceof EntityWolf) { return new CraftWolf(server, (EntityWolf) entity); }
                        else if (entity instanceof EntityOcelot) { return new CraftOcelot(server, (EntityOcelot) entity); }
                        else if (entity instanceof EntityParrot) { return new CraftParrot(server, (EntityParrot) entity); }
                        else return new CraftTameableAnimal(server, (EntityTameable) entity);
                    }
                    else if (entity instanceof EntitySheep) { return new CraftSheep(server, (EntitySheep) entity); }
                    else if (entity instanceof AbstractHorse) {
                        if (entity instanceof AbstractChestHorse) {
                            if (entity instanceof EntityDonkey) { return new CraftDonkey(server, (EntityDonkey) entity); }
                            else if (entity instanceof EntityMule) { return new CraftMule(server, (EntityMule) entity); }
                            else if (entity instanceof EntityLlama) { return new CraftLlama(server, (EntityLlama) entity); }
                            else { return new CraftAnimals(server, (EntityAnimal) entity); }
                        } else if (entity instanceof EntityHorse) { return new CraftHorse(server, (EntityHorse) entity); }
                        else if (entity instanceof EntitySkeletonHorse) { return new CraftSkeletonHorse(server, (EntitySkeletonHorse) entity); }
                        else if (entity instanceof EntityZombieHorse) { return new CraftZombieHorse(server, (EntityZombieHorse) entity); }
                        else { return new CraftAnimals(server, (EntityAnimal) entity); }
                    }
                    else if (entity instanceof EntityRabbit) { return new CraftRabbit(server, (EntityRabbit) entity); }
                    else if (entity instanceof EntityPolarBear) { return new CraftPolarBear(server, (EntityPolarBear) entity); }
                    else  { return new CraftAnimals(server, (EntityAnimal) entity); }
                }
                // Monsters
                else if (entity instanceof EntityMob) {
                    if (entity instanceof EntityZombie) {
                        if (entity instanceof EntityPigZombie) { return new CraftPigZombie(server, (EntityPigZombie) entity); }
                        else if (entity instanceof EntityHusk) { return new CraftHusk(server, (EntityHusk) entity); }
                        else if (entity instanceof EntityZombieVillager) { return new CraftVillagerZombie(server, (EntityZombieVillager) entity); }
                        else { return new CraftZombie(server, (EntityZombie) entity); }
                    }
                    else if (entity instanceof EntityCreeper) { return new CraftCreeper(server, (EntityCreeper) entity); }
                    else if (entity instanceof EntityEnderman) { return new CraftEnderman(server, (EntityEnderman) entity); }
                    else if (entity instanceof EntitySilverfish) { return new CraftSilverfish(server, (EntitySilverfish) entity); }
                    else if (entity instanceof EntityGiantZombie) { return new CraftGiant(server, (EntityGiantZombie) entity); }
                    else if (entity instanceof AbstractSkeleton) {
                        if (entity instanceof EntityStray) { return new CraftStray(server, (EntityStray) entity); }
                        else if (entity instanceof EntityWitherSkeleton) { return new CraftWitherSkeleton(server, (EntityWitherSkeleton) entity); }
                        else { return new CraftSkeleton(server, (AbstractSkeleton) entity); }
                    }
                    else if (entity instanceof EntityBlaze) { return new CraftBlaze(server, (EntityBlaze) entity); }
                    else if (entity instanceof EntityWitch) { return new CraftWitch(server, (EntityWitch) entity); }
                    else if (entity instanceof EntityWither) { return new CraftWither(server, (EntityWither) entity); }
                    else if (entity instanceof EntitySpider) {
                        if (entity instanceof EntityCaveSpider) { return new CraftCaveSpider(server, (EntityCaveSpider) entity); }
                        else { return new CraftSpider(server, (EntitySpider) entity); }
                    }
                    else if (entity instanceof EntityEndermite) { return new CraftEndermite(server, (EntityEndermite) entity); }
                    else if (entity instanceof EntityGuardian) {
                        if (entity instanceof EntityElderGuardian) { return new CraftElderGuardian(server, (EntityElderGuardian) entity); }
                        else { return new CraftGuardian(server, (EntityGuardian) entity); }
                    }
                    else if (entity instanceof EntityVex) { return new CraftVex(server, (EntityVex) entity); }
                    else if (entity instanceof AbstractIllager) {
                        if (entity instanceof EntitySpellcasterIllager) {
                            if (entity instanceof EntityEvoker) { return new CraftEvoker(server, (EntityEvoker) entity); }
                            else if (entity instanceof EntityIllusionIllager) { return new CraftIllusioner(server, (EntityIllusionIllager) entity); }
                            else {  return new CraftSpellcaster(server, (EntitySpellcasterIllager) entity); }
                        }
                        else if (entity instanceof EntityVindicator) { return new CraftVindicator(server, (EntityVindicator) entity); }
                        else { return new CraftIllager(server, (AbstractIllager) entity); }
                    }

                    else  { return new CraftMonster(server, (EntityMob) entity); }
                }
                else if (entity instanceof EntityGolem) {
                    if (entity instanceof EntitySnowman) { return new CraftSnowman(server, (EntitySnowman) entity); }
                    else if (entity instanceof EntityIronGolem) { return new CraftIronGolem(server, (EntityIronGolem) entity); }
                    else if (entity instanceof EntityShulker) { return new CraftShulker(server, (EntityShulker) entity); }
                    else { return new CraftLivingEntity(server, (net.minecraft.entity.EntityLivingBase) entity); }
                }
                else if (entity instanceof EntityVillager) { return new CraftVillager(server, (EntityVillager) entity); }
                else { return new CraftCreature(server, (EntityCreature) entity); }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof EntitySlime) {
                if (entity instanceof EntityMagmaCube) { return new CraftMagmaCube(server, (EntityMagmaCube) entity); }
                else { return new CraftSlime(server, (EntitySlime) entity); }
            }
            // Flying
            else if (entity instanceof EntityFlying) {
                if (entity instanceof EntityGhast) { return new CraftGhast(server, (EntityGhast) entity); }
                else { return new CraftFlying(server, (EntityFlying) entity); }
            }
            else if (entity instanceof EntityDragon) {
                return new CraftEnderDragon(server, (EntityDragon) entity);
            }
            // Ambient
            else if (entity instanceof EntityAmbientCreature) {
                if (entity instanceof EntityBat) { return new CraftBat(server, (EntityBat) entity); }
                else { return new CraftAmbient(server, (EntityAmbientCreature) entity); }
            }
            else if (entity instanceof EntityArmorStand) { return new CraftArmorStand(server, (EntityArmorStand) entity); }
            else  { return new CraftLivingEntity(server, (EntityLivingBase) entity); }
        }
        else if (entity instanceof MultiPartEntityPart) {
            MultiPartEntityPart part = (MultiPartEntityPart) entity;
            if (part.parent instanceof EntityDragon) { return new CraftEnderDragonPart(server, (MultiPartEntityPart) entity); }
            else { return new CraftComplexPart(server, (MultiPartEntityPart) entity); }
        }
        else if (entity instanceof EntityXPOrb) { return new CraftExperienceOrb(server, (EntityXPOrb) entity); }
        else if (entity instanceof EntityTippedArrow) {
        	if (((EntityTippedArrow) entity).isTipped()) { return new CraftTippedArrow(server, (EntityTippedArrow) entity); }
        	else { return new CraftArrow(server, (EntityArrow) entity); }
        }
        else if (entity instanceof EntitySpectralArrow) { return new CraftSpectralArrow(server, (EntitySpectralArrow) entity); }
        else if (entity instanceof EntityArrow) { return new CraftArrow(server, (EntityArrow) entity); }
        else if (entity instanceof EntityBoat) { return new CraftBoat(server, (EntityBoat) entity); }
        else if (entity instanceof EntityThrowable) {
            if (entity instanceof EntityEgg) { return new CraftEgg(server, (EntityEgg) entity); }
            else if (entity instanceof EntitySnowball) { return new CraftSnowball(server, (EntitySnowball) entity); }
            else if (entity instanceof EntityPotion) {
                if (!((EntityPotion) entity).isLingering()) { return new CraftSplashPotion(server, (EntityPotion) entity); }
            	else { return new CraftLingeringPotion(server, (EntityPotion) entity); }
            }
            else if (entity instanceof EntityEnderPearl) { return new CraftEnderPearl(server, (EntityEnderPearl) entity); }
            else if (entity instanceof EntityExpBottle) { return new CraftThrownExpBottle(server, (EntityExpBottle) entity); }
            else { return new CraftProjectile(server, entity); }
        }
        else if (entity instanceof EntityFallingBlock) { return new CraftFallingBlock(server, (EntityFallingBlock) entity); }
        else if (entity instanceof EntityFireball) {
            if (entity instanceof EntitySmallFireball) { return new CraftSmallFireball(server, (EntitySmallFireball) entity); }
            else if (entity instanceof EntityLargeFireball) { return new CraftLargeFireball(server, (EntityLargeFireball) entity); }
            else if (entity instanceof EntityWitherSkull) { return new CraftWitherSkull(server, (EntityWitherSkull) entity); }
            else if (entity instanceof EntityDragonFireball) { return new CraftDragonFireball(server, (EntityDragonFireball) entity); }
            else { return new CraftFireball(server, (EntityFireball) entity); }
        }
        else if (entity instanceof EntityEnderEye) { return new CraftEnderSignal(server, (EntityEnderEye) entity); }
        else if (entity instanceof EntityEnderCrystal) { return new CraftEnderCrystal(server, (EntityEnderCrystal) entity); }
        else if (entity instanceof EntityFishHook) { return new CraftFish(server, (EntityFishHook) entity); }
        else if (entity instanceof EntityItem) { return new CraftItem(server, (EntityItem) entity); }
        else if (entity instanceof EntityWeatherEffect) {
            if (entity instanceof EntityLightningBolt) { return new CraftLightningStrike(server, (EntityLightningBolt) entity); }
            else { return new CraftWeather(server, (EntityWeatherEffect) entity); }
        }
        else if (entity instanceof EntityMinecart) {
            if (entity instanceof EntityMinecartFurnace) { return new CraftMinecartFurnace(server, (EntityMinecartFurnace) entity); }
            else if (entity instanceof EntityMinecartChest) { return new CraftMinecartChest(server, (EntityMinecartChest) entity); }
            else if (entity instanceof EntityMinecartTNT) { return new CraftMinecartTNT(server, (EntityMinecartTNT) entity); }
            else if (entity instanceof EntityMinecartHopper) { return new CraftMinecartHopper(server, (EntityMinecartHopper) entity); }
            else if (entity instanceof EntityMinecartMobSpawner) { return new CraftMinecartMobSpawner(server, (EntityMinecartMobSpawner) entity); }
            else if (entity instanceof EntityMinecartEmpty) { return new CraftMinecartRideable(server, (EntityMinecartEmpty) entity); }
            else if (entity instanceof EntityMinecartCommandBlock) { return new CraftMinecartCommand(server, (EntityMinecartCommandBlock) entity); }
            else { return new CraftMinecart(server, (net.minecraft.entity.item.EntityMinecart) entity); }
        } else if (entity instanceof EntityHanging) {
            if (entity instanceof EntityPainting) { return new CraftPainting(server, (EntityPainting) entity); }
            else if (entity instanceof EntityItemFrame) { return new CraftItemFrame(server, (EntityItemFrame) entity); }
            else if (entity instanceof EntityLeashKnot) { return new CraftLeash(server, (EntityLeashKnot) entity); }
            else { return new CraftHanging(server, (EntityHanging) entity); }
        }
        else if (entity instanceof EntityTNTPrimed) { return new CraftTNTPrimed(server, (EntityTNTPrimed) entity); }
        else if (entity instanceof EntityFireworkRocket) { return new CraftFirework(server, (EntityFireworkRocket) entity); }
        else if (entity instanceof EntityShulkerBullet) { return new CraftShulkerBullet(server, (EntityShulkerBullet) entity); }
        else if (entity instanceof EntityAreaEffectCloud) { return new CraftAreaEffectCloud(server, (EntityAreaEffectCloud) entity); }
        else if (entity instanceof EntityEvokerFangs) { return new CraftEvokerFangs(server, (EntityEvokerFangs) entity); }
        else if (entity instanceof EntityLlamaSpit) { return new CraftLlamaSpit(server, (EntityLlamaSpit) entity); }
        else if (entity instanceof net.minecraft.entity.IProjectile) { return new CraftCustomProjectile(server, entity); }
        else if (entity != null) { return new CraftCustomEntity(server, entity); }
        throw new AssertionError("Unknown entity " + (entity == null ? " is null" : entity.getClass() + ": " + entity));
    }

    public Location getLocation() {
        return new Location(getWorld(), entity.posX, entity.posY, entity.posZ, entity.getBukkitYaw(), entity.rotationPitch);
    }

    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(entity.posX);
            loc.setY(entity.posY);
            loc.setZ(entity.posZ);
            loc.setYaw(entity.getBukkitYaw());
            loc.setPitch(entity.rotationPitch);
        }

        return loc;
    }

    public Vector getVelocity() {
        PlayerDataFixer.checkVector(entity); // CatServer - fix invalid vector
        return new Vector(entity.motionX, entity.motionY, entity.motionZ);
    }

    public void setVelocity(Vector velocity) {
        Preconditions.checkArgument(velocity != null, "velocity");
        velocity.checkFinite();
        entity.motionX = velocity.getX();
        entity.motionY = velocity.getY();
        entity.motionZ = velocity.getZ();
        entity.velocityChanged = true;
    }

    @Override
    public double getHeight() {
        return getHandle().height;
    }

    @Override
    public double getWidth() {
        return getHandle().width;
    }

    public boolean isOnGround() {
        if (entity instanceof EntityArrow) {
            return ((EntityArrow) entity).onGround;
        }
        return entity.onGround;
    }

    public World getWorld() {
        return entity.world.getWorld();
    }

    public boolean teleport(Location location) {
        return teleport(location, TeleportCause.PLUGIN);
    }

    public boolean teleport(Location location, TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location");
        location.checkFinite();

        if (entity.isBeingRidden() || entity.isDead) {
            return false;
        }

        // If this entity is riding another entity, we must dismount before teleporting.
        entity.dismountRidingEntity();

        entity.world = ((CraftWorld) location.getWorld()).getHandle();
        // entity.setLocation() throws no event, and so cannot be cancelled
        entity.setPositionAndRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // SPIGOT-619: Force sync head rotation also
        entity.setRotationYawHead(location.getYaw());
        entity.world.updateEntityWithOptionalForce(entity, false); // Spigot - register to new chunk

        return true;
    }

    public boolean teleport(org.bukkit.entity.Entity destination) {
        return teleport(destination.getLocation());
    }

    public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        List<Entity> notchEntityList = entity.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().grow(x, y, z), null);
        List<org.bukkit.entity.Entity> bukkitEntityList = new java.util.ArrayList<org.bukkit.entity.Entity>(notchEntityList.size());

        for (Entity e : notchEntityList) {
            bukkitEntityList.add(e.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    public int getEntityId() {
        return entity.getEntityId();
    }

    public int getFireTicks() {
        return entity.fire;
    }

    public int getMaxFireTicks() {
        return entity.getFireImmuneTicks();
    }

    public void setFireTicks(int ticks) {
        entity.fire = ticks;
    }

    public void remove() {
        entity.setDead();
    }

    public boolean isDead() {
        return !entity.isEntityAlive();
    }

    public boolean isValid() {
        return entity.isEntityAlive() && entity.valid;
    }

    public Server getServer() {
        return server;
    }

    public Vector getMomentum() {
        return getVelocity();
    }

    public void setMomentum(Vector value) {
        setVelocity(value);
    }

    public org.bukkit.entity.Entity getPassenger() {
        return isEmpty() ? null : getHandle().getPassengers().get(0).getBukkitEntity();
    }

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
        return Lists.newArrayList(Lists.transform(getHandle().getPassengers(), new Function<Entity, org.bukkit.entity.Entity>() {
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

        ((CraftEntity) passenger).getHandle().dismountRidingEntity();
        return true;
    }

    public boolean isEmpty() {
        return !getHandle().isBeingRidden();
    }

    public boolean eject() {
        if (isEmpty()) {
            return false;
        }

        getHandle().removePassengers();
        return true;
    }

    public float getFallDistance() {
        return getHandle().fallDistance;
    }

    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    public void setLastDamageCause(EntityDamageEvent event) {
        lastDamageEvent = event;
    }

    public EntityDamageEvent getLastDamageCause() {
        return lastDamageEvent;
    }

    public UUID getUniqueId() {
        return getHandle().getUniqueID();
    }

    public int getTicksLived() {
        return getHandle().ticksExisted;
    }

    public void setTicksLived(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        getHandle().ticksExisted = value;
    }

    public Entity getHandle() {
        return entity;
    }

    @Override
    public void playEffect(EntityEffect type) {
        Preconditions.checkArgument(type != null, "type");

        if (type.getApplicable().isInstance(this)) {
            this.getHandle().world.setEntityState(getHandle(), type.getData());
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
        return (this.getEntityId() == other.getEntityId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.getEntityId();
        return hash;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public boolean isInsideVehicle() {
        return getHandle().isRiding();
    }

    public boolean leaveVehicle() {
        if (!isInsideVehicle()) {
            return false;
        }

        getHandle().dismountRidingEntity();
        return true;
    }

    public org.bukkit.entity.Entity getVehicle() {
        if (!isInsideVehicle()) {
            return null;
        }

        return getHandle().getRidingEntity().getBukkitEntity(); // PAIL: rename getVehicle() -> getRootVehicle(), bJ() -> getVehicle()
    }

    @Override
    public void setCustomName(String name) {
        if (name == null) {
            name = "";
        }

        getHandle().setCustomNameTag(name);
    }

    @Override
    public String getCustomName() {
        String name = getHandle().getCustomNameTag();

        if (name == null || name.length() == 0) {
            if (getType().getEntityClass() == CraftCustomEntity.class && this instanceof CraftLivingEntity) return ((CraftLivingEntity) this).entity.getName(); // CatServer
            return null;
        }

        return name;
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        getHandle().setAlwaysRenderNameTag(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getHandle().getAlwaysRenderNameTag();
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] messages) {

    }

    @Override
    public String getName() {
        return getHandle().getName();
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
        if (e.getFlag(6) != flag) {
            e.setFlag(6, flag);
        }
    }

    @Override
    public boolean isGlowing() {
        return getHandle().glowing;
    }

    @Override
    public void setInvulnerable(boolean flag) {
        getHandle().setEntityInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return getHandle().isEntityInvulnerable(DamageSource.GENERIC);
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
        return !getHandle().hasNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return getHandle().timeUntilPortal;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        getHandle().timeUntilPortal = cooldown;
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
        return PistonMoveReaction.getById(getHandle().getPushReaction().ordinal());
    }

    protected NBTTagCompound save() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setString("id", getHandle().getEntityString());
        getHandle().writeToNBT(nbttagcompound);

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
        public boolean isInvulnerable()
        {
            return getHandle().isEntityInvulnerable(net.minecraft.util.DamageSource.GENERIC);
        }
    };

    public Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
