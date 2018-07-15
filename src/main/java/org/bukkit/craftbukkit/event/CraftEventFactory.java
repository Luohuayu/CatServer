// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItemFrame;
import org.apache.logging.log4j.Level;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.FakePlayer;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.entity.Firework;
import org.bukkit.event.entity.FireworkExplodeEvent;
import net.minecraft.entity.item.EntityFireworkRocket;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.Statistic;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.craftbukkit.CraftStatistic;
import net.minecraft.stats.Achievement;
import org.bukkit.event.Cancellable;
import net.minecraft.stats.StatBase;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.craftbukkit.inventory.CraftMetaBook;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.event.inventory.InventoryCloseEvent;
import net.minecraft.world.Explosion;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.Note;
import org.bukkit.Instrument;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.inventory.InventoryView;
import net.minecraft.inventory.InventoryCrafting;
import org.bukkit.event.inventory.InventoryOpenEvent;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.inventory.Container;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.entity.Horse;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Pig;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.EnumMap;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.LightningStrike;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntityDamageSource;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.TNTPrimed;
import net.minecraft.entity.boss.EntityDragon;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.server.ServerListPingEvent;
import java.net.InetAddress;
import org.bukkit.Server;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.entity.PlayerDeathEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import java.util.ArrayList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import net.minecraft.entity.EntityAreaEffectCloud;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.PotionSplashEvent;
import java.util.Map;
import net.minecraft.entity.projectile.EntityPotion;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import net.minecraft.entity.item.EntityItem;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.event.entity.EntityTameEvent;
import net.minecraft.entity.EntityLiving;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.EntityLivingBase;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import net.minecraft.util.EnumFacing;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.BlockState;
import java.util.List;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.bukkit.event.Event;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.CraftWorld;
import com.google.common.base.Functions;
import org.bukkit.craftbukkit.util.CraftDamageSource;
import com.google.common.base.Function;
import net.minecraft.entity.Entity;
import org.bukkit.block.Block;
import net.minecraft.util.DamageSource;

@SuppressWarnings("RedundantCast")
public class CraftEventFactory
{
    public static final DamageSource MELTING;
    public static final DamageSource POISON;
    public static Block blockDamage;
    public static Entity entityDamage;
    private static final Function<? super Double, Double> ZERO;
    
    static {
        MELTING = CraftDamageSource.copyOf(DamageSource.onFire);
        POISON = CraftDamageSource.copyOf(DamageSource.magic);
        ZERO = Functions.constant(-0.0);
    }
    
    private static boolean canBuild(final CraftWorld world, final Player player, final int x, final int z) {
        final WorldServer worldServer = world.getHandle();
        final int spawnSize = Bukkit.getServer().getSpawnRadius();
        if (world.getHandle().dimension != 0) {
            return true;
        }
        if (spawnSize <= 0) {
            return true;
        }
        if (((CraftServer)Bukkit.getServer()).getHandle().getOppedPlayers().isEmpty()) {
            return true;
        }
        if (player.isOp()) {
            return true;
        }
        final BlockPos chunkcoordinates = worldServer.getSpawnPoint();
        final int distanceFromSpawn = Math.max(Math.abs(x - chunkcoordinates.getX()), Math.abs(z - chunkcoordinates.getY()));
        return distanceFromSpawn > spawnSize;
    }
    
    public static <T extends Event> T callEvent(final T event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static BlockMultiPlaceEvent callBlockMultiPlaceEvent(final World world, final EntityPlayer who, final EnumHand hand, final List<BlockState> blockStates, final int clickedX, final int clickedY, final int clickedZ) {
        final CraftWorld craftWorld = world.getWorld();
        final CraftServer craftServer = world.getServer();
        final Player player = (Player)who.getBukkitEntity();
        final Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        boolean canBuild = true;
        for (int i = 0; i < blockStates.size(); ++i) {
            if (!canBuild(craftWorld, player, blockStates.get(i).getX(), blockStates.get(i).getZ())) {
                canBuild = false;
                break;
            }
        }
        ItemStack item;
        if (hand == EnumHand.MAIN_HAND) {
            item = player.getInventory().getItemInMainHand();
        }
        else {
            item = player.getInventory().getItemInOffHand();
        }
        final BlockMultiPlaceEvent event = new BlockMultiPlaceEvent(blockStates, blockClicked, item, player, canBuild);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    public static BlockPlaceEvent callBlockPlaceEvent(final World world, final EntityPlayer who, final EnumHand hand, final BlockState replacedBlockState, final int clickedX, final int clickedY, final int clickedZ) {
        final CraftWorld craftWorld = world.getWorld();
        final CraftServer craftServer = world.getServer();
        final Player player = (Player)who.getBukkitEntity();
        final Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        final Block placedBlock = replacedBlockState.getBlock();
        final boolean canBuild = canBuild(craftWorld, player, placedBlock.getX(), placedBlock.getZ());
        ItemStack item;
        EquipmentSlot equipmentSlot;
        if (hand == EnumHand.MAIN_HAND) {
            item = player.getInventory().getItemInMainHand();
            equipmentSlot = EquipmentSlot.HAND;
        }
        else {
            item = player.getInventory().getItemInOffHand();
            equipmentSlot = EquipmentSlot.OFF_HAND;
        }
        final BlockPlaceEvent event = new BlockPlaceEvent(placedBlock, replacedBlockState, blockClicked, item, player, canBuild, equipmentSlot);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(final EntityPlayer who, final int clickedX, final int clickedY, final int clickedZ, final EnumFacing clickedFace, final net.minecraft.item.ItemStack itemInHand) {
        return (PlayerBucketEmptyEvent)getPlayerBucketEvent(false, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, Items.BUCKET);
    }
    
    public static PlayerBucketFillEvent callPlayerBucketFillEvent(final EntityPlayer who, final int clickedX, final int clickedY, final int clickedZ, final EnumFacing clickedFace, final net.minecraft.item.ItemStack itemInHand, final Item bucket) {
        return (PlayerBucketFillEvent)getPlayerBucketEvent(true, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, bucket);
    }
    
    private static PlayerEvent getPlayerBucketEvent(final boolean isFilling, final EntityPlayer who, final int clickedX, final int clickedY, final int clickedZ, final EnumFacing clickedFace, final net.minecraft.item.ItemStack itemstack, final Item item) {
        final Player player = (who == null) ? null : ((Player)who.getBukkitEntity());
        final CraftItemStack itemInHand = CraftItemStack.asNewCraftStack(item);
        final Material bucket = CraftMagicNumbers.getMaterial(itemstack.getItem());
        final CraftWorld craftWorld = (CraftWorld)player.getWorld();
        final CraftServer craftServer = (CraftServer)player.getServer();
        final Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        final BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);
        PlayerEvent event = null;
        if (isFilling) {
            event = new PlayerBucketFillEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketFillEvent)event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
        }
        else {
            event = new PlayerBucketEmptyEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketEmptyEvent)event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
        }
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    public static PlayerInteractEvent callPlayerInteractEvent(final EntityPlayer who, final Action action, final net.minecraft.item.ItemStack itemstack, final EnumHand hand) {
        if (action != Action.LEFT_CLICK_AIR && action != Action.RIGHT_CLICK_AIR) {
            throw new AssertionError((Object)String.format("%s performing %s with %s", who, action, itemstack));
        }
        return callPlayerInteractEvent(who, action, null, EnumFacing.SOUTH, itemstack, hand);
    }
    
    public static PlayerInteractEvent callPlayerInteractEvent(final EntityPlayer who, final Action action, final BlockPos position, final EnumFacing direction, final net.minecraft.item.ItemStack itemstack, final EnumHand hand) {
        return callPlayerInteractEvent(who, action, position, direction, itemstack, false, hand);
    }
    
    public static PlayerInteractEvent callPlayerInteractEvent(final EntityPlayer who, Action action, final BlockPos position, final EnumFacing direction, final net.minecraft.item.ItemStack itemstack, final boolean cancelledBlock, final EnumHand hand) {
        final Player player = (who == null) ? null : ((Player)who.getBukkitEntity());
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        final CraftWorld craftWorld = (CraftWorld)player.getWorld();
        final CraftServer craftServer = (CraftServer)player.getServer();
        Block blockClicked = null;
        if (position != null) {
            blockClicked = craftWorld.getBlockAt(position.getX(), position.getY(), position.getZ());
        }
        else {
            switch (action) {
                case LEFT_CLICK_BLOCK: {
                    action = Action.LEFT_CLICK_AIR;
                    break;
                }
                case RIGHT_CLICK_BLOCK: {
                    action = Action.RIGHT_CLICK_AIR;
                    break;
                }
            }
        }
        final BlockFace blockFace = CraftBlock.notchToBlockFace(direction);
        if (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0) {
            itemInHand = null;
        }
        final PlayerInteractEvent event = new PlayerInteractEvent(player, action, itemInHand, blockClicked, blockFace, (hand == null) ? null : ((hand == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
        if (cancelledBlock) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    public static EntityShootBowEvent callEntityShootBowEvent(final EntityLivingBase who, final net.minecraft.item.ItemStack itemstack, final EntityArrow entityArrow, final float force) {
        final LivingEntity shooter = (LivingEntity)who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        final Arrow arrow = (Arrow)entityArrow.getBukkitEntity();
        if (itemInHand != null && (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0)) {
            itemInHand = null;
        }
        final EntityShootBowEvent event = new EntityShootBowEvent(shooter, itemInHand, arrow, force);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static BlockDamageEvent callBlockDamageEvent(final EntityPlayer who, final int x, final int y, final int z, final net.minecraft.item.ItemStack itemstack, final boolean instaBreak) {
        final Player player = (who == null) ? null : ((Player)who.getBukkitEntity());
        final CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        final CraftWorld craftWorld = (CraftWorld)player.getWorld();
        final CraftServer craftServer = (CraftServer)player.getServer();
        final Block blockClicked = craftWorld.getBlockAt(x, y, z);
        final BlockDamageEvent event = new BlockDamageEvent(player, blockClicked, itemInHand, instaBreak);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    public static CreatureSpawnEvent callCreatureSpawnEvent(final EntityLivingBase entityliving, final CreatureSpawnEvent.SpawnReason spawnReason) {
        final LivingEntity entity = (LivingEntity) entityliving.getBukkitEntity();
        final CraftServer craftServer = (CraftServer)entity.getServer();
        final CreatureSpawnEvent event = new CreatureSpawnEvent(entity, spawnReason);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    public static EntityTameEvent callEntityTameEvent(final EntityLiving entity, final EntityPlayer tamer) {
        final org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
        final AnimalTamer bukkitTamer = (tamer != null) ? tamer.getBukkitEntity() : null;
        final CraftServer craftServer = (CraftServer)bukkitEntity.getServer();
        entity.persistenceRequired = true;
        final EntityTameEvent event = new EntityTameEvent((LivingEntity)bukkitEntity, bukkitTamer);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    public static ItemSpawnEvent callItemSpawnEvent(final EntityItem entityitem) {
        final org.bukkit.entity.Item entity = (org.bukkit.entity.Item)entityitem.getBukkitEntity();
        final CraftServer craftServer = (CraftServer)entity.getServer();
        final ItemSpawnEvent event = new ItemSpawnEvent(entity, entity.getLocation());
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    public static ItemDespawnEvent callItemDespawnEvent(final EntityItem entityitem) {
        final org.bukkit.entity.Item entity = (org.bukkit.entity.Item)entityitem.getBukkitEntity();
        final ItemDespawnEvent event = new ItemDespawnEvent(entity, entity.getLocation());
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static ItemMergeEvent callItemMergeEvent(final EntityItem merging, final EntityItem mergingWith) {
        final org.bukkit.entity.Item entityMerging = (org.bukkit.entity.Item)merging.getBukkitEntity();
        final org.bukkit.entity.Item entityMergingWith = (org.bukkit.entity.Item)mergingWith.getBukkitEntity();
        final ItemMergeEvent event = new ItemMergeEvent(entityMerging, entityMergingWith);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static PotionSplashEvent callPotionSplashEvent(final EntityPotion potion, final Map<LivingEntity, Double> affectedEntities) {
        final ThrownPotion thrownPotion = (ThrownPotion)potion.getBukkitEntity();
        final PotionSplashEvent event = new PotionSplashEvent(thrownPotion, affectedEntities);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static LingeringPotionSplashEvent callLingeringPotionSplashEvent(final EntityPotion potion, final EntityAreaEffectCloud cloud) {
        final ThrownPotion thrownPotion = (ThrownPotion)potion.getBukkitEntity();
        final AreaEffectCloud effectCloud = (AreaEffectCloud)cloud.getBukkitEntity();
        final LingeringPotionSplashEvent event = new LingeringPotionSplashEvent(thrownPotion, effectCloud);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static BlockFadeEvent callBlockFadeEvent(final Block block, final net.minecraft.block.Block type) {
        final BlockState state = block.getState();
        state.setTypeId(net.minecraft.block.Block.getIdFromBlock(type));
        final BlockFadeEvent event = new BlockFadeEvent(block, state);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static boolean handleBlockSpreadEvent(final Block block, final Block source, final net.minecraft.block.Block type, final int data) {
        final BlockState state = block.getState();
        state.setTypeId(net.minecraft.block.Block.getIdFromBlock(type));
        state.setRawData((byte)data);
        final BlockSpreadEvent event = new BlockSpreadEvent(block, source, state);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            state.update(true);
        }
        return !event.isCancelled();
    }
    
    public static EntityDeathEvent callEntityDeathEvent(final EntityLivingBase victim) {
        return callEntityDeathEvent(victim, new ArrayList<ItemStack>(0));
    }
    
    public static EntityDeathEvent callEntityDeathEvent(final EntityLivingBase victim, final List<ItemStack> drops) {
        final CraftLivingEntity entity = (CraftLivingEntity)victim.getBukkitEntity();
        final EntityDeathEvent event = new EntityDeathEvent(entity, drops, victim.getExpReward());
        final CraftWorld world = (CraftWorld)entity.getWorld();
        Bukkit.getServer().getPluginManager().callEvent(event);
        victim.expToDrop = event.getDroppedExp();
        for (final ItemStack stack : event.getDrops()) {
            if (stack != null && stack.getType() != Material.AIR) {
                if (stack.getAmount() == 0) {
                    continue;
                }
                world.dropItemNaturally(entity.getLocation(), stack);
            }
        }
        return event;
    }
    
    public static PlayerDeathEvent callPlayerDeathEvent(final EntityPlayerMP victim, final List<ItemStack> drops, final String deathMessage, final boolean keepInventory) {
        final CraftPlayer entity = victim.getBukkitEntity();
        final PlayerDeathEvent event = new PlayerDeathEvent(entity, drops, victim.getExpReward(), 0, deathMessage);
        event.setKeepInventory(keepInventory);
        final org.bukkit.World world = entity.getWorld();
        Bukkit.getServer().getPluginManager().callEvent(event);
        victim.keepLevel = event.getKeepLevel();
        victim.newLevel = event.getNewLevel();
        victim.newTotalExp = event.getNewTotalExp();
        victim.expToDrop = event.getDroppedExp();
        victim.newExp = event.getNewExp();
        if (event.getKeepInventory()) {
            return event;
        }
        for (final ItemStack stack : event.getDrops()) {
            if (stack != null) {
                if (stack.getType() == Material.AIR) {
                    continue;
                }
                world.dropItemNaturally(entity.getLocation(), stack);
            }
        }
        return event;
    }
    
    public static ServerListPingEvent callServerListPingEvent(final Server craftServer, final InetAddress address, final String motd, final int numPlayers, final int maxPlayers) {
        final ServerListPingEvent event = new ServerListPingEvent(address, motd, numPlayers, maxPlayers);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }
    
    private static EntityDamageEvent handleEntityDamageEvent(final Entity entity, final DamageSource source, final Map<EntityDamageEvent.DamageModifier, Double> modifiers, final Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions) {
        //System.out.println("Func handleEntityDamageEvent: " + entity.toString() + " DAMAGER: " + source.getSourceOfDamage().toString());
        if (source.isExplosion()) {
            final Entity damager = CraftEventFactory.entityDamage;
            CraftEventFactory.entityDamage = null;
            EntityDamageEvent event;
            if (damager == null) {
                event = new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, modifiers, modifierFunctions);
            }
            else {
                final boolean b = entity instanceof EntityDragon;
                EntityDamageEvent.DamageCause damageCause;
                if (damager instanceof TNTPrimed) {
                    damageCause = EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
                }
                else {
                    damageCause = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
                }
                event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), entity.getBukkitEntity(), damageCause, modifiers, modifierFunctions);
            }
            callEvent(event);
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (source instanceof EntityDamageSource) {
            Entity damager2 = source.getEntity();
            EntityDamageEvent.DamageCause cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
            if (source instanceof EntityDamageSourceIndirect) {
                if(source.getSourceOfDamage() instanceof EntityPlayer){
                    damager2 = ((EntityDamageSourceIndirect)source).getSourceOfDamage();
                } else damager2 = ((EntityDamageSourceIndirect)source).getProximateDamageSource();
                assert damager2 != null;
                if (damager2.getBukkitEntity() instanceof ThrownPotion) {
                    cause = EntityDamageEvent.DamageCause.MAGIC;
                }
                else if (damager2.getBukkitEntity() instanceof Projectile) {
                    cause = EntityDamageEvent.DamageCause.PROJECTILE;
                }
            }
            else if ("thorns".equals(source.damageType)) {
                cause = EntityDamageEvent.DamageCause.THORNS;
            }
            return callEntityDamageEvent(damager2, entity, cause, modifiers, modifierFunctions);
        } else if (source == DamageSource.outOfWorld) {
            final EntityDamageEvent event2 = callEvent(new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, modifiers, modifierFunctions));
            if (!event2.isCancelled()) {
                event2.getEntity().setLastDamageCause(event2);
            }
            return event2;
        } else if (source == DamageSource.lava) {
            final EntityDamageEvent event2 = callEvent(new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.LAVA, modifiers, modifierFunctions));
            if (!event2.isCancelled()) {
                event2.getEntity().setLastDamageCause(event2);
            }
            return event2;
        } else if (CraftEventFactory.blockDamage != null) {
            EntityDamageEvent.DamageCause cause2 = null;
            final Block damager3 = CraftEventFactory.blockDamage;
            CraftEventFactory.blockDamage = null;
            if (source == DamageSource.cactus) {
                cause2 = EntityDamageEvent.DamageCause.CONTACT;
            } else if (source == DamageSource.fall) {
                cause2 = EntityDamageEvent.DamageCause.FALL;
            } else if (source == DamageSource.anvil || source == DamageSource.fallingBlock) {
                cause2 = EntityDamageEvent.DamageCause.FALLING_BLOCK;
            } else if (source == DamageSource.inFire) {
                cause2 = EntityDamageEvent.DamageCause.FIRE;
            } else if (source == DamageSource.onFire) {
                cause2 = EntityDamageEvent.DamageCause.FIRE_TICK;
            } else if (source == DamageSource.lava) {
                cause2 = EntityDamageEvent.DamageCause.LAVA;
            } else if (damager3 instanceof LightningStrike) {
                cause2 = EntityDamageEvent.DamageCause.LIGHTNING;
            } else if (source == DamageSource.magic) {
                cause2 = EntityDamageEvent.DamageCause.MAGIC;
            } else if (source == MELTING) {
                cause2 = EntityDamageEvent.DamageCause.MELTING;
            } else if (source == POISON) {
                cause2 = EntityDamageEvent.DamageCause.POISON;
            } else if (source == DamageSource.generic) {
                cause2 = EntityDamageEvent.DamageCause.CUSTOM;
            } else if(source == DamageSource.hotFloor){
                cause2 = EntityDamageEvent.DamageCause.HOT_FLOOR;
            } else {
                cause2 = EntityDamageEvent.DamageCause.CUSTOM;
            }
            EntityDamageEvent event = callEvent(new EntityDamageByBlockEvent(damager3, entity.getBukkitEntity(), cause2, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (CraftEventFactory.entityDamage != null) {
            EntityDamageEvent.DamageCause cause2 = null;
            CraftEntity damager4 = CraftEventFactory.entityDamage.getBukkitEntity();
            CraftEventFactory.entityDamage = null;
            if (source == DamageSource.anvil || source == DamageSource.fallingBlock) {
                cause2 = EntityDamageEvent.DamageCause.FALLING_BLOCK;
            } else if (damager4 instanceof LightningStrike) {
                cause2 = EntityDamageEvent.DamageCause.LIGHTNING;
            } else if (source == DamageSource.fall) {
                cause2 = EntityDamageEvent.DamageCause.FALL;
            } else if (source == DamageSource.cactus) {
                cause2 = EntityDamageEvent.DamageCause.CONTACT;
            } else if (source == DamageSource.inFire) {
                cause2 = EntityDamageEvent.DamageCause.FIRE;
            } else if (source == DamageSource.onFire) {
                cause2 = EntityDamageEvent.DamageCause.FIRE_TICK;
            } else if (source == DamageSource.lava) {
                cause2 = EntityDamageEvent.DamageCause.LAVA;
            } else if (source == DamageSource.magic) {
                cause2 = EntityDamageEvent.DamageCause.MAGIC;
            } else if (source == MELTING) {
                cause2 = EntityDamageEvent.DamageCause.MELTING;
            } else if (source == POISON) {
                cause2 = EntityDamageEvent.DamageCause.POISON;
            } else if (source == DamageSource.dragonBreath) {
                cause2 = EntityDamageEvent.DamageCause.DRAGON_BREATH;
            } else {
                cause2 = EntityDamageEvent.DamageCause.CUSTOM;
            }

            EntityDamageEvent event = callEvent(new EntityDamageByEntityEvent(damager4, entity.getBukkitEntity(), cause2, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        }
        EntityDamageEvent.DamageCause cause2 = null;
        if (source == DamageSource.inFire) {
            cause2 = EntityDamageEvent.DamageCause.FIRE;
        }
        else if (source == DamageSource.starve) {
            cause2 = EntityDamageEvent.DamageCause.STARVATION;
        }
        else if (source == DamageSource.wither) {
            cause2 = EntityDamageEvent.DamageCause.WITHER;
        }
        else if (source == DamageSource.inWall) {
            cause2 = EntityDamageEvent.DamageCause.SUFFOCATION;
        }
        else if (source == DamageSource.drown) {
            cause2 = EntityDamageEvent.DamageCause.DROWNING;
        }
        else if (source == DamageSource.onFire) {
            cause2 = EntityDamageEvent.DamageCause.FIRE_TICK;
        }
        else if (source == CraftEventFactory.MELTING) {
            cause2 = EntityDamageEvent.DamageCause.MELTING;
        }
        else if (source == CraftEventFactory.POISON) {
            cause2 = EntityDamageEvent.DamageCause.POISON;
        }
        else if (source == DamageSource.magic) {
            cause2 = EntityDamageEvent.DamageCause.MAGIC;
        }
        else if (source == DamageSource.fall) {
            cause2 = EntityDamageEvent.DamageCause.FALL;
        }
        else if (source == DamageSource.flyIntoWall) {
            cause2 = EntityDamageEvent.DamageCause.FLY_INTO_WALL;
        }
        else if (source != DamageSource.generic){
            return new EntityDamageEvent(entity.getBukkitEntity(), EntityDamageEvent.DamageCause.CUSTOM, modifiers, modifierFunctions); // use custom
        }
        return callEntityDamageEvent(null, entity, cause2, modifiers, modifierFunctions);
    }
    
    private static EntityDamageEvent callEntityDamageEvent(final Entity damager, final Entity damagee, final EntityDamageEvent.DamageCause cause, final Map<EntityDamageEvent.DamageModifier, Double> modifiers, final Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions) {
        EntityDamageEvent event;

        if (damager != null) {
            event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
        } else {
            event = new EntityDamageEvent(damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
        }
        callEvent(event);
        if (!event.isCancelled()) {
            event.getEntity().setLastDamageCause(event);
        }
        return event;
    }
    
    public static EntityDamageEvent handleLivingEntityDamageEvent(final Entity damagee, final DamageSource source, final double rawDamage, final double hardHatModifier, final double blockingModifier, final double armorModifier, final double resistanceModifier, final double magicModifier, final double absorptionModifier, final Function<Double, Double> hardHat, final Function<Double, Double> blocking, final Function<Double, Double> armor, final Function<Double, Double> resistance, final Function<Double, Double> magic, final Function<Double, Double> absorption) {
        final Map<EntityDamageEvent.DamageModifier, Double> modifiers = new EnumMap<EntityDamageEvent.DamageModifier, Double>(EntityDamageEvent.DamageModifier.class);
        final Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions = new EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>>(EntityDamageEvent.DamageModifier.class);
        modifiers.put(EntityDamageEvent.DamageModifier.BASE, rawDamage);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.BASE, CraftEventFactory.ZERO);
        if (source == DamageSource.fallingBlock || source == DamageSource.anvil) {
            modifiers.put(EntityDamageEvent.DamageModifier.HARD_HAT, hardHatModifier);
            modifierFunctions.put(EntityDamageEvent.DamageModifier.HARD_HAT, hardHat);
        }
        if (damagee instanceof EntityPlayer) {
            modifiers.put(EntityDamageEvent.DamageModifier.BLOCKING, blockingModifier);
            modifierFunctions.put(EntityDamageEvent.DamageModifier.BLOCKING, blocking);
        }
        modifiers.put(EntityDamageEvent.DamageModifier.ARMOR, armorModifier);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.ARMOR, armor);
        modifiers.put(EntityDamageEvent.DamageModifier.RESISTANCE, resistanceModifier);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.RESISTANCE, resistance);
        modifiers.put(EntityDamageEvent.DamageModifier.MAGIC, magicModifier);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.MAGIC, magic);
        modifiers.put(EntityDamageEvent.DamageModifier.ABSORPTION, absorptionModifier);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.ABSORPTION, absorption);
        return handleEntityDamageEvent(damagee, source, modifiers, modifierFunctions);
    }
    
    public static boolean handleNonLivingEntityDamageEvent(final Entity entity, final DamageSource source, final double damage) {
        return handleNonLivingEntityDamageEvent(entity, source, damage, true);
    }
    
    public static boolean handleNonLivingEntityDamageEvent(final Entity entity, final DamageSource source, final double damage, final boolean cancelOnZeroDamage) {
        if (entity instanceof EntityEnderCrystal && !(source instanceof EntityDamageSource)) {
            return false;
        }
        final EnumMap<EntityDamageEvent.DamageModifier, Double> modifiers = new EnumMap<EntityDamageEvent.DamageModifier, Double>(EntityDamageEvent.DamageModifier.class);
        final EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> functions = new EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>>(EntityDamageEvent.DamageModifier.class);
        modifiers.put(EntityDamageEvent.DamageModifier.BASE, Double.valueOf(damage));
        functions.put(EntityDamageEvent.DamageModifier.BASE, CraftEventFactory.ZERO);
        final EntityDamageEvent event = handleEntityDamageEvent(entity, source, modifiers, functions);
        return event != null && (event.isCancelled() || (cancelOnZeroDamage && event.getDamage() == 0.0 && !(entity instanceof EntityItemFrame)));
    }
    
    public static PlayerLevelChangeEvent callPlayerLevelChangeEvent(final Player player, final int oldLevel, final int newLevel) {
        final PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(player, oldLevel, newLevel);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static PlayerExpChangeEvent callPlayerExpChangeEvent(final EntityPlayer entity, final int expAmount) {
        final Player player = (Player)entity.getBukkitEntity();
        final PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, expAmount);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static boolean handleBlockGrowEvent(final World world, final int x, final int y, final int z, final net.minecraft.block.Block type, final int data) {
        final Block block = world.getWorld().getBlockAt(x, y, z);
        final CraftBlockState state = (CraftBlockState)block.getState();
        state.setTypeId(net.minecraft.block.Block.getIdFromBlock(type));
        state.setRawData((byte)data);
        final BlockGrowEvent event = new BlockGrowEvent(block, state);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            state.update(true);
        }
        return !event.isCancelled();
    }
    
    public static FoodLevelChangeEvent callFoodLevelChangeEvent(final EntityPlayer entity, final int level) {
        final FoodLevelChangeEvent event = new FoodLevelChangeEvent(entity.getBukkitEntity(), level);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static PigZapEvent callPigZapEvent(final Entity pig, final Entity lightning, final Entity pigzombie) {
        final PigZapEvent event = new PigZapEvent((Pig)pig.getBukkitEntity(), (LightningStrike)lightning.getBukkitEntity(), (PigZombie)pigzombie.getBukkitEntity());
        pig.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static HorseJumpEvent callHorseJumpEvent(final Entity horse, final float power) {
        final HorseJumpEvent event = new HorseJumpEvent((Horse)horse.getBukkitEntity(), power);
        horse.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static EntityChangeBlockEvent callEntityChangeBlockEvent(final org.bukkit.entity.Entity entity, final Block block, final Material material) {
        return callEntityChangeBlockEvent(entity, block, material, 0);
    }
    
    public static EntityChangeBlockEvent callEntityChangeBlockEvent(final Entity entity, final Block block, final Material material) {
        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0);
    }
    
    public static EntityChangeBlockEvent callEntityChangeBlockEvent(final Entity entity, final Block block, final Material material, final boolean cancelled) {
        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0, cancelled);
    }
    
    public static EntityChangeBlockEvent callEntityChangeBlockEvent(final Entity entity, final BlockPos position, final net.minecraft.block.Block type, final int data) {
        final Block block = entity.worldObj.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
        final Material material = CraftMagicNumbers.getMaterial(type);
        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, data);
    }
    
    public static EntityChangeBlockEvent callEntityChangeBlockEvent(final org.bukkit.entity.Entity entity, final Block block, final Material material, final int data) {
        return callEntityChangeBlockEvent(entity, block, material, data, false);
    }
    
    public static EntityChangeBlockEvent callEntityChangeBlockEvent(final org.bukkit.entity.Entity entity, final Block block, final Material material, final int data, final boolean cancelled) {
        final EntityChangeBlockEvent event = new EntityChangeBlockEvent(entity, block, material, (byte)data);
        event.setCancelled(cancelled);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static CreeperPowerEvent callCreeperPowerEvent(final Entity creeper, final Entity lightning, final CreeperPowerEvent.PowerCause cause) {
        final CreeperPowerEvent event = new CreeperPowerEvent((Creeper)creeper.getBukkitEntity(), (LightningStrike)lightning.getBukkitEntity(), cause);
        creeper.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static EntityTargetEvent callEntityTargetEvent(final Entity entity, final Entity target, final EntityTargetEvent.TargetReason reason) {
        final EntityTargetEvent event = new EntityTargetEvent(entity.getBukkitEntity(), (target == null) ? null : target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static EntityTargetLivingEntityEvent callEntityTargetLivingEvent(final Entity entity, final EntityLivingBase target, final EntityTargetEvent.TargetReason reason) {
        final EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(entity.getBukkitEntity(), (LivingEntity)target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static EntityBreakDoorEvent callEntityBreakDoorEvent(final Entity entity, final int x, final int y, final int z) {
        final org.bukkit.entity.Entity entity2 = entity.getBukkitEntity();
        final Block block = entity2.getWorld().getBlockAt(x, y, z);
        final EntityBreakDoorEvent event = new EntityBreakDoorEvent((LivingEntity)entity2, block);
        entity2.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static Container callInventoryOpenEvent(final EntityPlayerMP player, final Container container) {
        return callInventoryOpenEvent(player, container, false);
    }
    
    public static Container callInventoryOpenEvent(final EntityPlayerMP player, final Container container, final boolean cancelled) {
        if (player.openContainer != player.inventoryContainer) {
            player.connection.processCloseWindow(new CPacketCloseWindow(player.openContainer.windowId));
        }
        final CraftServer server = player.worldObj.getServer();
        final CraftPlayer craftPlayer = player.getBukkitEntity();
        player.openContainer.transferTo(container, craftPlayer);
        final InventoryOpenEvent event = new InventoryOpenEvent(container.getBukkitView());
        event.setCancelled(cancelled);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            ((Container) container).transferTo(player.openContainer, craftPlayer);
            return null;
        }
        return container;
    }
    
    public static net.minecraft.item.ItemStack callPreCraftEvent(final InventoryCrafting matrix, final net.minecraft.item.ItemStack result, final InventoryView lastCraftView, final boolean isRepair) {
        final CraftInventoryCrafting inventory = new CraftInventoryCrafting(matrix, (IInventory) matrix.resultInventory);
        inventory.setResult(CraftItemStack.asCraftMirror(result));
        final PrepareItemCraftEvent event = new PrepareItemCraftEvent(inventory, lastCraftView, isRepair);
        Bukkit.getPluginManager().callEvent(event);
        final ItemStack bitem = event.getInventory().getResult();
        return CraftItemStack.asNMSCopy(bitem);
    }
    
    public static ProjectileLaunchEvent callProjectileLaunchEvent(final Entity entity) {
        final Projectile bukkitEntity = (Projectile)entity.getBukkitEntity();
        final ProjectileLaunchEvent event = new ProjectileLaunchEvent(bukkitEntity);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static ProjectileHitEvent callProjectileHitEvent(final Entity entity) {
        final ProjectileHitEvent event = new ProjectileHitEvent((Projectile)entity.getBukkitEntity());
        entity.worldObj.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static ExpBottleEvent callExpBottleEvent(final Entity entity, final int exp) {
        final ThrownExpBottle bottle = (ThrownExpBottle)entity.getBukkitEntity();
        final ExpBottleEvent event = new ExpBottleEvent(bottle, exp);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static BlockRedstoneEvent callRedstoneChange(final World world, final int x, final int y, final int z, final int oldCurrent, final int newCurrent) {
        final BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(x, y, z), oldCurrent, newCurrent);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static NotePlayEvent callNotePlayEvent(final World world, final int x, final int y, final int z, final byte instrument, final byte note) {
        final NotePlayEvent event = new NotePlayEvent(world.getWorld().getBlockAt(x, y, z), Instrument.getByType(instrument), new Note(note));
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static void callPlayerItemBreakEvent(final EntityPlayer human, final net.minecraft.item.ItemStack brokenItem) {
        final CraftItemStack item = CraftItemStack.asCraftMirror(brokenItem);
        final PlayerItemBreakEvent event = new PlayerItemBreakEvent((Player)human.getBukkitEntity(), item);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    public static BlockIgniteEvent callBlockIgniteEvent(final World world, final int x, final int y, final int z, final int igniterX, final int igniterY, final int igniterZ) {
        final org.bukkit.World bukkitWorld = world.getWorld();
        final Block igniter = bukkitWorld.getBlockAt(igniterX, igniterY, igniterZ);
        BlockIgniteEvent.IgniteCause cause = null;
        switch (igniter.getType()) {
            case LAVA:
            case STATIONARY_LAVA: {
                cause = BlockIgniteEvent.IgniteCause.LAVA;
                break;
            }
            case DISPENSER: {
                cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
                break;
            }
            default: {
                cause = BlockIgniteEvent.IgniteCause.SPREAD;
                break;
            }
        }
        final BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), cause, igniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static BlockIgniteEvent callBlockIgniteEvent(final World world, final int x, final int y, final int z, final Entity igniter) {
        final org.bukkit.World bukkitWorld = world.getWorld();
        final org.bukkit.entity.Entity bukkitIgniter = igniter.getBukkitEntity();
        BlockIgniteEvent.IgniteCause cause = null;
        switch (bukkitIgniter.getType()) {
            case ENDER_CRYSTAL: {
                cause = BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL;
                break;
            }
            case LIGHTNING: {
                cause = BlockIgniteEvent.IgniteCause.LIGHTNING;
                break;
            }
            case FIREBALL:
            case SMALL_FIREBALL: {
                cause = BlockIgniteEvent.IgniteCause.FIREBALL;
                break;
            }
            default: {
                cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
                break;
            }
        }
        final BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), cause, bukkitIgniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static BlockIgniteEvent callBlockIgniteEvent(final World world, final int x, final int y, final int z, final Explosion explosion) {
        final org.bukkit.World bukkitWorld = world.getWorld();
        final org.bukkit.entity.Entity igniter = (explosion.exploder == null) ? null : explosion.exploder.getBukkitEntity();
        final BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), BlockIgniteEvent.IgniteCause.EXPLOSION, igniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static BlockIgniteEvent callBlockIgniteEvent(final World world, final int x, final int y, final int z, final BlockIgniteEvent.IgniteCause cause, final Entity igniter) {
        final BlockIgniteEvent event = new BlockIgniteEvent(world.getWorld().getBlockAt(x, y, z), cause, igniter.getBukkitEntity());
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static void handleInventoryCloseEvent(final EntityPlayer human) {
    	if(!(human.openContainer instanceof Container)) {
    		return;
    	}
        final InventoryCloseEvent event = new InventoryCloseEvent(((Container) human.openContainer).getBukkitView()); //TODO NULL
        if(human.openContainer.getBukkitView() != null) human.worldObj.getServer().getPluginManager().callEvent(event);
        human.openContainer.transferTo(human.inventoryContainer, human.getBukkitEntity());
    }
    
    public static void handleEditBookEvent(final EntityPlayerMP player, final net.minecraft.item.ItemStack newBookItem) {
        final int itemInHandIndex = player.inventory.currentItem;
        final PlayerEditBookEvent editBookEvent = new PlayerEditBookEvent(player.getBukkitEntity(), player.inventory.currentItem, (BookMeta)CraftItemStack.getItemMeta(player.inventory.getCurrentItem()), (BookMeta)CraftItemStack.getItemMeta(newBookItem), newBookItem.getItem() == Items.WRITTEN_BOOK);
        player.worldObj.getServer().getPluginManager().callEvent(editBookEvent);
        final net.minecraft.item.ItemStack itemInHand = player.inventory.getStackInSlot(itemInHandIndex);
        if (itemInHand != null && itemInHand.getItem() == Items.WRITABLE_BOOK) {
            if (!editBookEvent.isCancelled()) {
                if (editBookEvent.isSigning()) {
                    itemInHand.setItem(Items.WRITTEN_BOOK);
                }
                final CraftMetaBook meta = (CraftMetaBook)editBookEvent.getNewBookMeta();
                final List<ITextComponent> pages = meta.pages;
                for (int i = 0; i < pages.size(); ++i) {
                    pages.set(i, stripEvents(pages.get(i)));
                }
                CraftItemStack.setItemMeta(itemInHand, meta);
            }
            final Slot slot = player.openContainer.getSlotFromInventory(player.inventory, itemInHandIndex);
            player.connection.sendPacket(new SPacketSetSlot(player.openContainer.windowId, slot.slotNumber, itemInHand));
        }
    }
    
    private static ITextComponent stripEvents(final ITextComponent c) {
        final Style modi = c.getStyle();
        if (modi != null) {
            modi.setClickEvent(null);
            modi.setHoverEvent(null);
        }
        c.setStyle(modi);
        if (c instanceof TextComponentTranslation) {
            final TextComponentTranslation cm = (TextComponentTranslation)c;
            final Object[] oo = cm.getFormatArgs();
            for (int i = 0; i < oo.length; ++i) {
                final Object o = oo[i];
                if (o instanceof ITextComponent) {
                    oo[i] = stripEvents((ITextComponent)o);
                }
            }
        }
        final List<ITextComponent> ls = c.getSiblings();
        if (ls != null) {
            for (int j = 0; j < ls.size(); ++j) {
                ls.set(j, stripEvents(ls.get(j)));
            }
        }
        return c;
    }
    
    public static PlayerUnleashEntityEvent callPlayerUnleashEntityEvent(final EntityLiving entity, final EntityPlayer player) {
        final PlayerUnleashEntityEvent event = new PlayerUnleashEntityEvent(entity.getBukkitEntity(), (Player)player.getBukkitEntity());
        entity.worldObj.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static PlayerLeashEntityEvent callPlayerLeashEntityEvent(final EntityLiving entity, final Entity leashHolder, final EntityPlayer player) {
        final PlayerLeashEntityEvent event = new PlayerLeashEntityEvent(entity.getBukkitEntity(), leashHolder.getBukkitEntity(), (Player)player.getBukkitEntity());
        entity.worldObj.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static Cancellable handleStatisticsIncrease(final EntityPlayer entityHuman, final StatBase statistic, final int current, final int incrementation) {
        final Player player = ((EntityPlayerMP)entityHuman).getBukkitEntity();
        Event event = null;
        if (statistic instanceof Achievement) {
            if (current != 0) {
                return null;
            }
            event = new PlayerAchievementAwardedEvent(player, CraftStatistic.getBukkitAchievement((Achievement)statistic));
        }
        else {
            final Statistic stat = CraftStatistic.getBukkitStatistic(statistic);
            if (stat == null) {
                return null;
            }
            switch (stat) {
                case PLAY_ONE_TICK:
                case WALK_ONE_CM:
                case SWIM_ONE_CM:
                case FALL_ONE_CM:
                case SNEAK_TIME:
                case CLIMB_ONE_CM:
                case FLY_ONE_CM:
                case DIVE_ONE_CM:
                case MINECART_ONE_CM:
                case BOAT_ONE_CM:
                case PIG_ONE_CM:
                case HORSE_ONE_CM:
                case SPRINT_ONE_CM:
                case CROUCH_ONE_CM:
                case TIME_SINCE_DEATH: {
                    return null;
                }
                default: {
                    if (stat.getType() == Statistic.Type.UNTYPED) {
                        event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation);
                        break;
                    }
                    if (stat.getType() == Statistic.Type.ENTITY) {
                        final EntityType entityType = CraftStatistic.getEntityTypeFromStatistic(statistic);
                        event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation, entityType);
                        break;
                    }
                    final Material material = CraftStatistic.getMaterialFromStatistic(statistic);
                    event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation, material);
                    break;
                }
            }
        }
        entityHuman.worldObj.getServer().getPluginManager().callEvent(event);
        return (Cancellable)event;
    }
    
    public static FireworkExplodeEvent callFireworkExplodeEvent(final EntityFireworkRocket firework) {
        final FireworkExplodeEvent event = new FireworkExplodeEvent((Firework)firework.getBukkitEntity());
        firework.worldObj.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static PrepareAnvilEvent callPrepareAnvilEvent(final InventoryView view, final net.minecraft.item.ItemStack item) {
        final PrepareAnvilEvent event = new PrepareAnvilEvent(view, CraftItemStack.asCraftMirror(item).clone());
        event.getView().getPlayer().getServer().getPluginManager().callEvent(event);
        event.getInventory().setItem(2, event.getResult());
        return event;
    }
    
    public static EntityToggleGlideEvent callToggleGlideEvent(final EntityLivingBase entity, final boolean gliding) {
        final EntityToggleGlideEvent event = new EntityToggleGlideEvent((LivingEntity)entity.getBukkitEntity(), gliding);
        entity.worldObj.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static AreaEffectCloudApplyEvent callAreaEffectCloudApplyEvent(final EntityAreaEffectCloud cloud, final List<LivingEntity> entities) {
        final AreaEffectCloudApplyEvent event = new AreaEffectCloudApplyEvent((AreaEffectCloud)cloud.getBukkitEntity(), entities);
        cloud.worldObj.getServer().getPluginManager().callEvent(event);
        return event;
    }
    
    public static EntityBreedEvent callEntityBreedEvent(final EntityLivingBase child, final EntityLivingBase mother, final EntityLivingBase father, final EntityLivingBase breeder, final net.minecraft.item.ItemStack bredWith, final int experience) {
        final LivingEntity breederEntity = (LivingEntity)((breeder == null) ? null : breeder.getBukkitEntity());
        final CraftItemStack bredWithStack = (bredWith == null) ? null : CraftItemStack.asCraftMirror(bredWith).clone();
        final EntityBreedEvent event = new EntityBreedEvent((LivingEntity)child.getBukkitEntity(), (LivingEntity)mother.getBukkitEntity(), (LivingEntity)father.getBukkitEntity(), breederEntity, bredWithStack, experience);
        child.worldObj.getServer().getPluginManager().callEvent(event);
        return event;
    }

    // Svarka start
    public static BlockBreakEvent callBlockBreakEvent(net.minecraft.world.World world, BlockPos pos, IBlockState iBlockState, net.minecraft.entity.player.EntityPlayerMP player)
    {
        org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(pos.getX(),pos.getY(),pos.getZ());
        org.bukkit.event.block.BlockBreakEvent blockBreakEvent = new org.bukkit.event.block.BlockBreakEvent(bukkitBlock, ((EntityPlayerMP)player).getBukkitEntity());
        EntityPlayerMP playermp = (EntityPlayerMP)player;
        net.minecraft.block.Block block = iBlockState.getBlock();
        if (!(playermp instanceof FakePlayer))
        {
            if (!(playermp.interactionManager.getGameType().isAdventure() && !playermp.interactionManager.tryHarvestBlock(pos)) && !(playermp.interactionManager.getGameType().isCreative() &&
                    playermp.getHeldItem(EnumHand.OFF_HAND) != null &&
                    playermp.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof  ItemSword &&
                    playermp.getHeldItem(EnumHand.MAIN_HAND) != null &&
                    playermp.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword))
            {
                int exp = 0;
                if (!(block == null || !player.canHarvestBlock(block.getDefaultState()) || // Handle empty block or player unable to break block scenario
                         block.canSilkHarvest(world, pos, block.getBlockState().getBaseState(), player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH,player.getHeldItemMainhand()) > 0)) // If the block is being silk harvested, the exp dropped is 0
                {
                    int meta = block.getMetaFromState(block.getBlockState().getBaseState());
                    int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
                    exp = block.getExpDrop(iBlockState,world, pos, bonusLevel);
                }
                blockBreakEvent.setExpToDrop(exp);
            }
            else blockBreakEvent.setCancelled(true);
        }

        world.getServer().getPluginManager().callEvent(blockBreakEvent);
        return blockBreakEvent;
    }
    // Svarka end
}
