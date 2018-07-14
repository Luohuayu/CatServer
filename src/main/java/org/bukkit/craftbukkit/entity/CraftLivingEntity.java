// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.Attribute;
import org.bukkit.util.NumberConversions;
import net.minecraft.entity.boss.EntityWither;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityDragonFireball;
import org.bukkit.entity.DragonFireball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import org.bukkit.entity.WitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Fireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.entity.Fish;
import net.minecraft.entity.item.EntityExpBottle;
import org.bukkit.entity.ThrownExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.ThrownPotion;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import net.minecraft.entity.projectile.EntityTippedArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Arrow;
import net.minecraft.entity.item.EntityEnderPearl;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;

import net.minecraft.entity.projectile.EntityEgg;
import org.bukkit.entity.Egg;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntitySnowball;
import org.bukkit.entity.Snowball;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.util.Vector;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffectType;
import java.util.Collection;
import net.minecraft.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.Material;
import java.util.Set;
import java.util.Iterator;
import org.bukkit.util.BlockIterator;
import java.util.ArrayList;
import org.bukkit.block.Block;
import java.util.List;
import java.util.HashSet;
import net.minecraft.entity.SharedMonsterAttributes;
import org.apache.commons.lang.Validate;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftEntityEquipment;
import org.bukkit.entity.LivingEntity;

public class CraftLivingEntity extends CraftEntity implements LivingEntity
{
    private CraftEntityEquipment equipment;
    
    public CraftLivingEntity(final CraftServer server, final EntityLivingBase entity) {
        super(server, entity);
        if (entity instanceof EntityLiving || entity instanceof EntityArmorStand) {
            this.equipment = new CraftEntityEquipment(this);
        }
    }
    
    @Override
    public double getHealth() {
        return Math.min(Math.max(0.0f, this.getHandle().getHealth()), this.getMaxHealth());
    }
    
    @Override
    public void setHealth(final double health) {
        if (health < 0.0 || health > this.getMaxHealth()) {
            throw new IllegalArgumentException("Health must be between 0 and " + this.getMaxHealth());
        }
        if (health == 0.0) {
            this.getHandle().onDeath(DamageSource.generic);
        }
        this.getHandle().setHealth((float)health);
    }
    
    @Override
    public double getMaxHealth() {
        return this.getHandle().getMaxHealth();
    }
    
    @Override
    public void setMaxHealth(final double amount) {
        Validate.isTrue(amount > 0.0, "Max health must be greater than 0");
        this.getHandle().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(amount);
        if (this.getHealth() > amount) {
            this.setHealth(amount);
        }
    }
    
    @Override
    public void resetMaxHealth() {
        this.setMaxHealth(this.getHandle().getMaxHealth());
    }
    
    @Override
    public double getEyeHeight() {
        return this.getHandle().getEyeHeight();
    }
    
    @Override
    public double getEyeHeight(final boolean ignoreSneaking) {
        return this.getEyeHeight();
    }
    
    private List<Block> getLineOfSight(final HashSet<Byte> transparent, int maxDistance, final int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        final ArrayList<Block> blocks = new ArrayList<Block>();
        final Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            final Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            final int id = block.getTypeId();
            if (transparent == null) {
                if (id != 0) {
                    break;
                }
                continue;
            }
            else {
                if (!transparent.contains((byte)id)) {
                    break;
                }
                continue;
            }
        }
        return blocks;
    }
    
    private List<Block> getLineOfSight(final Set<Material> transparent, int maxDistance, final int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        final ArrayList<Block> blocks = new ArrayList<Block>();
        final Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            final Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            final Material material = block.getType();
            if (transparent == null) {
                if (!material.equals(Material.AIR)) {
                    break;
                }
                continue;
            }
            else {
                if (!transparent.contains(material)) {
                    break;
                }
                continue;
            }
        }
        return blocks;
    }
    
    @Override
    public List<Block> getLineOfSight(final HashSet<Byte> transparent, final int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 0);
    }
    
    @Override
    public List<Block> getLineOfSight(final Set<Material> transparent, final int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 0);
    }
    
    @Override
    public Block getTargetBlock(final HashSet<Byte> transparent, final int maxDistance) {
        final List<Block> blocks = this.getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }
    
    @Override
    public Block getTargetBlock(final Set<Material> transparent, final int maxDistance) {
        final List<Block> blocks = this.getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }
    
    @Override
    public List<Block> getLastTwoTargetBlocks(final HashSet<Byte> transparent, final int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 2);
    }
    
    @Override
    public List<Block> getLastTwoTargetBlocks(final Set<Material> transparent, final int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 2);
    }
    
    @Override
    public int getRemainingAir() {
        return this.getHandle().getAir();
    }
    
    @Override
    public void setRemainingAir(final int ticks) {
        this.getHandle().setAir(ticks);
    }
    
    @Override
    public int getMaximumAir() {
        return this.getHandle().maxAirTicks;
    }
    
    @Override
    public void setMaximumAir(final int ticks) {
        this.getHandle().maxAirTicks = ticks;
    }
    
    @Override
    public void damage(final double amount) {
        this.damage(amount, null);
    }
    
    @Override
    public void damage(final double amount, final Entity source) {
        DamageSource reason = DamageSource.generic;
        if (source instanceof HumanEntity) {
            reason = DamageSource.causePlayerDamage(((CraftHumanEntity)source).getHandle());
        }
        else if (source instanceof LivingEntity) {
            reason = DamageSource.causeMobDamage(((CraftLivingEntity)source).getHandle());
        }
        this.entity.attackEntityFrom(reason, (float)amount);
    }
    
    @Override
    public Location getEyeLocation() {
        final Location loc = this.getLocation();
        loc.setY(loc.getY() + this.getEyeHeight());
        return loc;
    }
    
    @Override
    public int getMaximumNoDamageTicks() {
        return this.getHandle().maxHurtResistantTime;
    }
    
    @Override
    public void setMaximumNoDamageTicks(final int ticks) {
        this.getHandle().maxHurtResistantTime = ticks;
    }
    
    @Override
    public double getLastDamage() {
        return this.getHandle().lastDamage;
    }
    
    @Override
    public void setLastDamage(final double damage) {
        this.getHandle().lastDamage = (float)damage;
    }
    
    @Override
    public int getNoDamageTicks() {
        return this.getHandle().hurtResistantTime;
    }
    
    @Override
    public void setNoDamageTicks(final int ticks) {
        this.getHandle().hurtResistantTime = ticks;
    }
    
    @Override
    public EntityLivingBase getHandle() {
        return (EntityLivingBase)this.entity;
    }
    
    public void setHandle(final EntityLivingBase entity) {
        super.setHandle(entity);
    }
    
    @Override
    public String toString() {
        return "CraftLivingEntity{id=" + this.getEntityId() + '}';
    }
    
    @Override
    public Player getKiller() {
        return (this.getHandle().attackingPlayer == null) ? null : ((Player)this.getHandle().attackingPlayer.getBukkitEntity());
    }
    
    @Override
    public boolean addPotionEffect(final PotionEffect effect) {
        return this.addPotionEffect(effect, false);
    }
    
    @Override
    public boolean addPotionEffect(final PotionEffect effect, final boolean force) {
        if (this.hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }
            this.removePotionEffect(effect.getType());
        }
        this.getHandle().addPotionEffect(new net.minecraft.potion.PotionEffect(Potion.getPotionById(effect.getType().getId()), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
        return true;
    }
    
    @Override
    public boolean addPotionEffects(final Collection<PotionEffect> effects) {
        boolean success = true;
        for (final PotionEffect effect : effects) {
            success &= this.addPotionEffect(effect);
        }
        return success;
    }
    
    @Override
    public boolean hasPotionEffect(final PotionEffectType type) {
        return this.getHandle().isPotionActive(Potion.getPotionById(type.getId()));
    }
    
    @Override
    public PotionEffect getPotionEffect(final PotionEffectType type) {
        final net.minecraft.potion.PotionEffect handle = this.getHandle().getActivePotionEffect(Potion.getPotionById(type.getId()));
        return (handle == null) ? null : new PotionEffect(PotionEffectType.getById(Potion.getIdFromPotion(handle.getPotion())), handle.getDuration(), handle.getAmplifier(), handle.getIsAmbient(), handle.doesShowParticles());
    }
    
    @Override
    public void removePotionEffect(final PotionEffectType type) {
        this.getHandle().removePotionEffect(Potion.getPotionById(type.getId()));
    }
    
    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        final List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (final net.minecraft.potion.PotionEffect handle : this.getHandle().activePotionsMap.values()) {
            effects.add(new PotionEffect(PotionEffectType.getById(Potion.getIdFromPotion(handle.getPotion())), handle.getDuration(), handle.getAmplifier(), handle.getIsAmbient(), handle.doesShowParticles()));
        }
        return effects;
    }
    
    @Override
    public <T extends Projectile> T launchProjectile(final Class<? extends T> projectile) {
        return this.launchProjectile(projectile, (Vector)null);
    }
    
    @Override
    public <T extends Projectile> T launchProjectile(final Class<? extends T> projectile, final Vector velocity) {
        final World world = ((CraftWorld)this.getWorld()).getHandle();
        net.minecraft.entity.Entity launch = null;
        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, this.getHandle());
            ((EntityThrowable)launch).setHeadingFromThrower(this.getHandle(), this.getHandle().rotationPitch, this.getHandle().rotationYaw, 0.0f, 1.5f, 1.0f);
        }
        else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, this.getHandle());
            ((EntityThrowable)launch).setHeadingFromThrower(this.getHandle(), this.getHandle().rotationPitch, this.getHandle().rotationYaw, 0.0f, 1.5f, 1.0f);
        }
        else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, this.getHandle());
            ((EntityThrowable)launch).setHeadingFromThrower(this.getHandle(), this.getHandle().rotationPitch, this.getHandle().rotationYaw, 0.0f, 1.5f, 1.0f);
        }
        else if (Arrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new EntityTippedArrow(world, this.getHandle());
                ((EntityTippedArrow)launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            }
            else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new EntitySpectralArrow(world, this.getHandle());
            }
            else {
                launch = new EntityTippedArrow(world, this.getHandle());
            }
            ((EntityArrow)launch).setAim(this.getHandle(), this.getHandle().rotationPitch, this.getHandle().rotationYaw, 0.0f, 3.0f, 1.0f);
        }
        else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new EntityPotion(world, this.getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1)));
            }
            else {
                launch = new EntityPotion(world, this.getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            }
            ((EntityThrowable)launch).setHeadingFromThrower(this.getHandle(), this.getHandle().rotationPitch, this.getHandle().rotationYaw, -20.0f, 0.5f, 1.0f);
        }
        else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityExpBottle(world, this.getHandle());
            ((EntityThrowable)launch).setHeadingFromThrower(this.getHandle(), this.getHandle().rotationPitch, this.getHandle().rotationYaw, -20.0f, 0.7f, 1.0f);
        }
        else if (Fish.class.isAssignableFrom(projectile) && this.getHandle() instanceof EntityPlayer) {
            launch = new EntityFishHook(world, (EntityPlayer)this.getHandle());
        }
        else if (Fireball.class.isAssignableFrom(projectile)) {
            final Location location = this.getEyeLocation();
            final Vector direction = location.getDirection().multiply(10);
            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }
            else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }
            else if (DragonFireball.class.isAssignableFrom(projectile)) {
                launch = new EntityDragonFireball(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }
            else {
                launch = new EntityLargeFireball(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }
            ((EntityFireball)launch).projectileSource = this;
            launch.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }
        Validate.notNull((Object)launch, "Projectile not supported");
        if (velocity != null) {
            ((Projectile)launch.getBukkitEntity()).setVelocity(velocity);
        }
        world.spawnEntityInWorld(launch);
        return (T)launch.getBukkitEntity();
    }
    
    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
    
    @Override
    public boolean hasLineOfSight(final Entity other) {
        return this.getHandle().canEntityBeSeen(((CraftEntity)other).getHandle());
    }
    
    @Override
    public boolean getRemoveWhenFarAway() {
        return this.getHandle() instanceof EntityLiving && !((EntityLiving)this.getHandle()).persistenceRequired;
    }
    
    @Override
    public void setRemoveWhenFarAway(final boolean remove) {
        if (this.getHandle() instanceof EntityLiving) {
            ((EntityLiving)this.getHandle()).persistenceRequired = !remove;
        }
    }
    
    @Override
    public EntityEquipment getEquipment() {
        return this.equipment;
    }
    
    @Override
    public void setCanPickupItems(final boolean pickup) {
        if (this.getHandle() instanceof EntityLiving) {
            ((EntityLiving)this.getHandle()).canPickUpLoot = pickup;
        }
    }
    
    @Override
    public boolean getCanPickupItems() {
        return this.getHandle() instanceof EntityLiving && ((EntityLiving)this.getHandle()).canPickUpLoot;
    }
    
    @Override
    public boolean teleport(final Location location, final PlayerTeleportEvent.TeleportCause cause) {
        return this.getHealth() != 0.0 && super.teleport(location, cause);
    }
    
    @Override
    public boolean isLeashed() {
        return this.getHandle() instanceof EntityLiving && ((EntityLiving)this.getHandle()).getLeashedToEntity() != null;
    }
    
    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        if (!this.isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        }
        return ((EntityLiving)this.getHandle()).getLeashedToEntity().getBukkitEntity();
    }
    
    private boolean unleash() {
        if (!this.isLeashed()) {
            return false;
        }
        ((EntityLiving)this.getHandle()).clearLeashed(true, false);
        return true;
    }
    
    @Override
    public boolean setLeashHolder(final Entity holder) {
        if (this.getHandle() instanceof EntityWither || !(this.getHandle() instanceof EntityLiving)) {
            return false;
        }
        if (holder == null) {
            return this.unleash();
        }
        if (holder.isDead()) {
            return false;
        }
        this.unleash();
        ((EntityLiving)this.getHandle()).setLeashedToEntity(((CraftEntity)holder).getHandle(), true);
        return true;
    }
    
    @Override
    public boolean isGliding() {
        return this.getHandle().getFlag(7);
    }
    
    @Override
    public void setGliding(final boolean gliding) {
        this.getHandle().setFlag(7, gliding);
    }
    
    @Deprecated
    @Override
    public int _INVALID_getLastDamage() {
        return NumberConversions.ceil(this.getLastDamage());
    }
    
    @Deprecated
    @Override
    public void _INVALID_setLastDamage(final int damage) {
        this.setLastDamage(damage);
    }
    
    @Deprecated
    @Override
    public void _INVALID_damage(final int amount) {
        this.damage(amount);
    }
    
    @Deprecated
    @Override
    public void _INVALID_damage(final int amount, final Entity source) {
        this.damage(amount, source);
    }
    
    @Deprecated
    @Override
    public int _INVALID_getHealth() {
        return NumberConversions.ceil(this.getHealth());
    }
    
    @Deprecated
    @Override
    public void _INVALID_setHealth(final int health) {
        this.setHealth(health);
    }
    
    @Deprecated
    @Override
    public int _INVALID_getMaxHealth() {
        return NumberConversions.ceil(this.getMaxHealth());
    }
    
    @Deprecated
    @Override
    public void _INVALID_setMaxHealth(final int health) {
        this.setMaxHealth(health);
    }
    
    @Override
    public AttributeInstance getAttribute(final Attribute attribute) {
        return this.getHandle().craftAttributes.getAttribute(attribute);
    }
    
    @Override
    public void setAI(final boolean ai) {
        if (this.getHandle() instanceof EntityLiving) {
            ((EntityLiving)this.getHandle()).setNoAI(!ai);
        }
    }
    
    @Override
    public boolean hasAI() {
        return this.getHandle() instanceof EntityLiving && !((EntityLiving)this.getHandle()).isAIDisabled();
    }
    
    @Override
    public void setCollidable(final boolean collidable) {
        this.getHandle().collides = collidable;
    }
    
    @Override
    public boolean isCollidable() {
        return this.getHandle().collides;
    }
}
