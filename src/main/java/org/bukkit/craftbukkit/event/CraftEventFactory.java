package org.bukkit.craftbukkit.event;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import catserver.server.CatServer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Statistic.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.util.CraftDamageSource;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class CraftEventFactory {
    public static final DamageSource MELTING = CraftDamageSource.copyOf(DamageSource.ON_FIRE);
    public static final DamageSource POISON = CraftDamageSource.copyOf(DamageSource.MAGIC);
    public static Block blockDamage; // For use in EntityDamageByBlockEvent
    public static Entity entityDamage; // For use in EntityDamageByEntityEvent

    // helper methods
    private static boolean canBuild(CraftWorld world, Player player, int x, int z) {
        WorldServer worldServer = world.getHandle();
        int spawnSize = Bukkit.getServer().getSpawnRadius();

        if (world.getHandle().dimension != 0) return true;
        if (spawnSize <= 0) return true;
        if (((CraftServer) Bukkit.getServer()).getHandle().getOppedPlayers().isEmpty()) return true;
        if (player.isOp()) return true;

        BlockPos chunkcoordinates = worldServer.getSpawnPoint();

        int distanceFromSpawn = Math.max(Math.abs(x - chunkcoordinates.getX()), Math.abs(z - chunkcoordinates.getZ()));
        return distanceFromSpawn > spawnSize;
    }

    public static <T extends Event> T callEvent(T event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event;
    }

    /**
     * Block place methods
     */
    public static BlockMultiPlaceEvent callBlockMultiPlaceEvent(World world, EntityPlayer who, EnumHand hand, List<BlockState> blockStates, int clickedX, int clickedY, int clickedZ) {
        CraftWorld craftWorld = world.getWorld();
        CraftServer craftServer = world.getServer();
        Player player = (Player) who.getBukkitEntity();

        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);

        boolean canBuild = true;
        for (int i = 0; i < blockStates.size(); i++) {
            if (!canBuild(craftWorld, player, blockStates.get(i).getX(), blockStates.get(i).getZ())) {
                canBuild = false;
                break;
            }
        }

        org.bukkit.inventory.ItemStack item;
        if (hand == EnumHand.MAIN_HAND) {
            item = player.getInventory().getItemInMainHand();
        } else {
            item = player.getInventory().getItemInOffHand();
        }

        BlockMultiPlaceEvent event = new BlockMultiPlaceEvent(blockStates, blockClicked, item, player, canBuild);
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    public static BlockPlaceEvent callBlockPlaceEvent(World world, EntityPlayer who, EnumHand hand, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ) {
        CraftWorld craftWorld = world.getWorld();
        CraftServer craftServer = world.getServer();

        Player player = (Player) who.getBukkitEntity();

        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        Block placedBlock = replacedBlockState.getBlock();

        boolean canBuild = canBuild(craftWorld, player, placedBlock.getX(), placedBlock.getZ());

        org.bukkit.inventory.ItemStack item;
        EquipmentSlot equipmentSlot;
        if (hand == EnumHand.MAIN_HAND) {
            item = player.getInventory().getItemInMainHand();
            equipmentSlot = EquipmentSlot.HAND;
        } else {
            item = player.getInventory().getItemInOffHand();
            equipmentSlot = EquipmentSlot.OFF_HAND;
        }

        BlockPlaceEvent event = new BlockPlaceEvent(placedBlock, replacedBlockState, blockClicked, item, player, canBuild, equipmentSlot);
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * Bucket methods
     */
    public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(EntityPlayer who, int clickedX, int clickedY, int clickedZ, EnumFacing clickedFace, ItemStack itemInHand) {
        return (PlayerBucketEmptyEvent) getPlayerBucketEvent(false, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, Items.BUCKET);
    }

    public static PlayerBucketFillEvent callPlayerBucketFillEvent(EntityPlayer who, int clickedX, int clickedY, int clickedZ, EnumFacing clickedFace, ItemStack itemInHand, net.minecraft.item.Item bucket) {
        return (PlayerBucketFillEvent) getPlayerBucketEvent(true, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, bucket);
    }

    private static PlayerEvent getPlayerBucketEvent(boolean isFilling, EntityPlayer who, int clickedX, int clickedY, int clickedZ, EnumFacing clickedFace, ItemStack itemstack, net.minecraft.item.Item item) {
        Player player = (who == null) ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asNewCraftStack(item);
        Material bucket = CraftMagicNumbers.getMaterial(itemstack.getItem());

        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();

        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);

        PlayerEvent event = null;
        if (isFilling) {
            event = new PlayerBucketFillEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketFillEvent) event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
        } else {
            event = new PlayerBucketEmptyEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketEmptyEvent) event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
        }

        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * Player Interact event
     */
    public static PlayerInteractEvent callPlayerInteractEvent(EntityPlayer who, Action action, ItemStack itemstack, EnumHand hand) {
        if (action != Action.LEFT_CLICK_AIR && action != Action.RIGHT_CLICK_AIR) {
            throw new AssertionError(String.format("%s performing %s with %s", who, action, itemstack));
        }
        return callPlayerInteractEvent(who, action, null, EnumFacing.SOUTH, itemstack, hand);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(EntityPlayer who, Action action, BlockPos position, EnumFacing direction, ItemStack itemstack, EnumHand hand) {
        return callPlayerInteractEvent(who, action, position, direction, itemstack, false, hand);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(EntityPlayer who, Action action, BlockPos position, EnumFacing direction, ItemStack itemstack, boolean cancelledBlock, EnumHand hand) {
        Player player = (who == null) ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);

        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();

        Block blockClicked = null;
        if (position != null) {
            blockClicked = craftWorld.getBlockAt(position.getX(), position.getY(), position.getZ());
        } else {
            switch (action) {
                case LEFT_CLICK_BLOCK:
                    action = Action.LEFT_CLICK_AIR;
                    break;
                case RIGHT_CLICK_BLOCK:
                    action = Action.RIGHT_CLICK_AIR;
                    break;
            }
        }
        BlockFace blockFace = CraftBlock.notchToBlockFace(direction);

        if (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0) {
            itemInHand = null;
        }

        PlayerInteractEvent event = new PlayerInteractEvent(player, action, itemInHand, blockClicked, blockFace, (hand == null) ? null : ((hand == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
        if (cancelledBlock) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * EntityShootBowEvent
     */
    public static EntityShootBowEvent callEntityShootBowEvent(EntityLivingBase who, ItemStack itemstack, EntityArrow entityArrow, float force) {
        LivingEntity shooter = (LivingEntity) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        Arrow arrow = (Arrow) entityArrow.getBukkitEntity();

        if (itemInHand != null && (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0)) {
            itemInHand = null;
        }

        EntityShootBowEvent event = new EntityShootBowEvent(shooter, itemInHand, arrow, force);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * BlockDamageEvent
     */
    public static BlockDamageEvent callBlockDamageEvent(EntityPlayer who, int x, int y, int z, ItemStack itemstack, boolean instaBreak) {
        Player player = (who == null) ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);

        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();

        Block blockClicked = craftWorld.getBlockAt(x, y, z);

        BlockDamageEvent event = new BlockDamageEvent(player, blockClicked, itemInHand, instaBreak);
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * CreatureSpawnEvent
     */
    public static CreatureSpawnEvent callCreatureSpawnEvent(EntityLivingBase entityliving, SpawnReason spawnReason) {
        LivingEntity entity = (LivingEntity) entityliving.getBukkitEntity();
        CraftServer craftServer = (CraftServer) entity.getServer();

        CreatureSpawnEvent event = new CreatureSpawnEvent(entity, spawnReason);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * EntityTameEvent
     */
    public static EntityTameEvent callEntityTameEvent(EntityLiving entity, EntityPlayer tamer) {
        org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
        org.bukkit.entity.AnimalTamer bukkitTamer = (tamer != null ? tamer.getBukkitEntity() : null);
        CraftServer craftServer = (CraftServer) bukkitEntity.getServer();

        entity.persistenceRequired = true;

        EntityTameEvent event = new EntityTameEvent((LivingEntity) bukkitEntity, bukkitTamer);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * ItemSpawnEvent
     */
    public static ItemSpawnEvent callItemSpawnEvent(EntityItem entityitem) {
        org.bukkit.entity.Item entity = (org.bukkit.entity.Item) entityitem.getBukkitEntity();
        CraftServer craftServer = (CraftServer) entity.getServer();

        ItemSpawnEvent event = new ItemSpawnEvent(entity, entity.getLocation());

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * ItemDespawnEvent
     */
    public static ItemDespawnEvent callItemDespawnEvent(EntityItem entityitem) {
        org.bukkit.entity.Item entity = (org.bukkit.entity.Item) entityitem.getBukkitEntity();

        ItemDespawnEvent event = new ItemDespawnEvent(entity, entity.getLocation());

        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    /**
     * ItemMergeEvent
     */
    public static ItemMergeEvent callItemMergeEvent(EntityItem merging, EntityItem mergingWith) {
        org.bukkit.entity.Item entityMerging = (org.bukkit.entity.Item) merging.getBukkitEntity();
        org.bukkit.entity.Item entityMergingWith = (org.bukkit.entity.Item) mergingWith.getBukkitEntity();

        ItemMergeEvent event = new ItemMergeEvent(entityMerging, entityMergingWith);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * PotionSplashEvent
     */
    public static PotionSplashEvent callPotionSplashEvent(EntityPotion potion, Map<LivingEntity, Double> affectedEntities) {
        ThrownPotion thrownPotion = (ThrownPotion) potion.getBukkitEntity();

        PotionSplashEvent event = new PotionSplashEvent(thrownPotion, affectedEntities);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static LingeringPotionSplashEvent callLingeringPotionSplashEvent(EntityPotion potion, EntityAreaEffectCloud cloud) {
        ThrownPotion thrownPotion = (ThrownPotion) potion.getBukkitEntity();
        AreaEffectCloud effectCloud = (AreaEffectCloud) cloud.getBukkitEntity();

        LingeringPotionSplashEvent event = new LingeringPotionSplashEvent(thrownPotion, effectCloud);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * BlockFadeEvent
     */
    public static BlockFadeEvent callBlockFadeEvent(Block block, net.minecraft.block.Block type) {
        BlockState state = block.getState();
        state.setTypeId(net.minecraft.block.Block.getIdFromBlock(type));

        BlockFadeEvent event = new BlockFadeEvent(block, state);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleBlockSpreadEvent(Block block, Block source, net.minecraft.block.Block type, int data) {
        BlockState state = block.getState();
        state.setTypeId(net.minecraft.block.Block.getIdFromBlock(type));
        state.setRawData((byte) data);

        BlockSpreadEvent event = new BlockSpreadEvent(block, source, state);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            state.update(true);
        }
        return !event.isCancelled();
    }

    public static EntityDeathEvent callEntityDeathEvent(EntityLivingBase victim) {
        return callEntityDeathEvent(victim, new ArrayList<org.bukkit.inventory.ItemStack>(0));
    }

    public static EntityDeathEvent callEntityDeathEvent(EntityLivingBase victim, List<org.bukkit.inventory.ItemStack> drops) {
        CraftLivingEntity entity = (CraftLivingEntity) victim.getBukkitEntity();
        EntityDeathEvent event = new EntityDeathEvent(entity, drops, victim.getExpReward());
        //CraftWorld world = (CraftWorld) entity.getWorld();
        Bukkit.getServer().getPluginManager().callEvent(event);

        victim.expToDrop = event.getDroppedExp();
        // CatServer start - handle any drop changes from plugins
        victim.capturedDrops.clear();
        for (org.bukkit.inventory.ItemStack stack : event.getDrops())
        {
            net.minecraft.entity.item.EntityItem entityitem = new net.minecraft.entity.item.EntityItem(victim.world, entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), CraftItemStack.asNMSCopy(stack));
            if (entityitem != null)
            {
                victim.capturedDrops.add((EntityItem)entityitem);
            }
        }
        // CatServer end
        return event;
    }

    public static PlayerDeathEvent callPlayerDeathEvent(EntityPlayerMP victim, List<org.bukkit.inventory.ItemStack> drops, String deathMessage, boolean keepInventory) {
        CraftPlayer entity = victim.getBukkitEntity();
        PlayerDeathEvent event = new PlayerDeathEvent(entity, drops, victim.getExpReward(), 0, deathMessage);
        event.setKeepInventory(keepInventory);
        //org.bukkit.World world = entity.getWorld();
        Bukkit.getServer().getPluginManager().callEvent(event);

        victim.keepLevel = event.getKeepLevel();
        victim.newLevel = event.getNewLevel();
        victim.newTotalExp = event.getNewTotalExp();
        victim.expToDrop = event.getDroppedExp();
        victim.newExp = event.getNewExp();

        if (event.getKeepInventory()) {
            return event;
        }
        victim.capturedDrops.clear(); // CatServer - we must clear pre-capture to avoid duplicates
        for (final org.bukkit.inventory.ItemStack stack : event.getDrops()) {
            if (stack != null) {
                if (stack.getType() == Material.AIR) {
                    continue;
                }
                // CatServer start - add support for Forge's PlayerDropsEvent
                //world.dropItemNaturally(entity.getLocation(), stack); // handle world drop in EntityPlayerMP
                if (victim.captureDrops)
                {
                    net.minecraft.entity.item.EntityItem entityitem = new net.minecraft.entity.item.EntityItem(victim.world, entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), CraftItemStack.asNMSCopy(stack));
                    if (entityitem != null)
                    {
                        victim.capturedDrops.add((EntityItem)entityitem);
                    }
                }
                // CatServer end
            }
        }
        return event;
    }

    /**
     * Server methods
     */
    public static ServerListPingEvent callServerListPingEvent(Server craftServer, InetAddress address, String motd, int numPlayers, int maxPlayers) {
        ServerListPingEvent event = new ServerListPingEvent(address, motd, numPlayers, maxPlayers);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    private static EntityDamageEvent handleEntityDamageEvent(Entity entity, DamageSource source, Map<DamageModifier, Double> modifiers, Map<DamageModifier, Function<? super Double, Double>> modifierFunctions) {
        if (source.isExplosion()) {
            DamageCause damageCause;
            Entity damager = entityDamage;
            entityDamage = null;
            EntityDamageEvent event;
            if (damager == null) {
                event = new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), DamageCause.BLOCK_EXPLOSION, modifiers, modifierFunctions);
            } else if (entity instanceof EntityDragon && /*PAIL FIXME ((EntityEnderDragon) entity).target == damager*/ false) {
                event = new EntityDamageEvent(entity.getBukkitEntity(), DamageCause.ENTITY_EXPLOSION, modifiers, modifierFunctions);
            } else {
                if (damager instanceof org.bukkit.entity.TNTPrimed) {
                    damageCause = DamageCause.BLOCK_EXPLOSION;
                } else {
                    damageCause = DamageCause.ENTITY_EXPLOSION;
                }
                event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), entity.getBukkitEntity(), damageCause, modifiers, modifierFunctions);
            }

            callEvent(event);

            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (source instanceof EntityDamageSource) {
            Entity damager = source.getTrueSource();
            DamageCause cause = (source.isSweep()) ? DamageCause.ENTITY_SWEEP_ATTACK : DamageCause.ENTITY_ATTACK;

            if (source instanceof EntityDamageSourceIndirect) {
                damager = ((EntityDamageSourceIndirect) source).getProximateDamageSource();
                if (damager != null) { // CatServer - check null
                    if (damager.getBukkitEntity() instanceof ThrownPotion) {
                        cause = DamageCause.MAGIC;
                    } else if (damager.getBukkitEntity() instanceof Projectile) {
                        cause = DamageCause.PROJECTILE;
                    }
                }
            } else if ("thorns".equals(source.damageType)) {
                cause = DamageCause.THORNS;
            }

            return callEntityDamageEvent(damager, entity, cause, modifiers, modifierFunctions);
        } else if (source == DamageSource.OUT_OF_WORLD) {
            EntityDamageEvent event = callEvent(new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), DamageCause.VOID, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (source == DamageSource.LAVA) {
            EntityDamageEvent event = callEvent(new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), DamageCause.LAVA, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (blockDamage != null) {
            DamageCause cause = null;
            Block damager = blockDamage;
            blockDamage = null;
            if (source == DamageSource.CACTUS) {
                cause = DamageCause.CONTACT;
            } else if (source == DamageSource.HOT_FLOOR) {
                cause = DamageCause.HOT_FLOOR;
            } else if (source == DamageSource.FALL) {
                cause = DamageCause.FALL;
            } else if (source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) {
                cause = DamageCause.FALLING_BLOCK;
            } else if (source == DamageSource.IN_FIRE) {
                cause = DamageCause.FIRE;
            } else if (source == DamageSource.ON_FIRE) {
                cause = DamageCause.FIRE_TICK;
            } else if (source == DamageSource.LAVA) {
                cause = DamageCause.LAVA;
            } else if (damager instanceof LightningStrike) {
                cause = DamageCause.LIGHTNING;
            } else if (source == DamageSource.MAGIC) {
                cause = DamageCause.MAGIC;
            } else if (source == MELTING) {
                cause = DamageCause.MELTING;
            } else if (source == POISON) {
                cause = DamageCause.POISON;
            } else if (source == DamageSource.GENERIC) {
                cause = DamageCause.CUSTOM;
            } else {
                cause = DamageCause.CUSTOM;
            }
            EntityDamageEvent event = callEvent(new EntityDamageByBlockEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (entityDamage != null) {
            DamageCause cause = null;
            CraftEntity damager = entityDamage.getBukkitEntity();
            entityDamage = null;
            if (source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) {
                cause = DamageCause.FALLING_BLOCK;
            } else if (damager instanceof LightningStrike) {
                cause = DamageCause.LIGHTNING;
            } else if (source == DamageSource.FALL) {
                cause = DamageCause.FALL;
            } else if (source == DamageSource.DRAGON_BREATH) {
                cause = DamageCause.DRAGON_BREATH;
            } else if (source == DamageSource.MAGIC) {
                cause = DamageCause.MAGIC;
            } else if (source == DamageSource.CACTUS) {
                cause = DamageCause.CONTACT;
            } else if (source == DamageSource.IN_FIRE) {
                cause = DamageCause.FIRE;
            } else if (source == DamageSource.ON_FIRE) {
                cause = DamageCause.FIRE_TICK;
            } else if (source == DamageSource.LAVA) {
                cause = DamageCause.LAVA;
            } else if (source == MELTING) {
                cause = DamageCause.MELTING;
            } else if (source == POISON) {
                cause = DamageCause.POISON;
            } else {
                cause = DamageCause.CUSTOM;
            }
            EntityDamageEvent event = callEvent(new EntityDamageByEntityEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        }

        DamageCause cause = null;
        if (source == DamageSource.IN_FIRE) {
            cause = DamageCause.FIRE;
        } else if (source == DamageSource.STARVE) {
            cause = DamageCause.STARVATION;
        } else if (source == DamageSource.WITHER) {
            cause = DamageCause.WITHER;
        } else if (source == DamageSource.IN_WALL) {
            cause = DamageCause.SUFFOCATION;
        } else if (source == DamageSource.DROWN) {
            cause = DamageCause.DROWNING;
        } else if (source == DamageSource.ON_FIRE) {
            cause = DamageCause.FIRE_TICK;
        } else if (source == MELTING) {
            cause = DamageCause.MELTING;
        } else if (source == POISON) {
            cause = DamageCause.POISON;
        } else if (source == DamageSource.MAGIC) {
            cause = DamageCause.MAGIC;
        } else if (source == DamageSource.FALL) {
            cause = DamageCause.FALL;
        } else if (source == DamageSource.FLY_INTO_WALL) {
            cause = DamageCause.FLY_INTO_WALL;
        } else if (source == DamageSource.CRAMMING) {
            cause = DamageCause.CRAMMING;
        } else if (source == DamageSource.GENERIC) {
            cause = DamageCause.CUSTOM;
        }

        if (cause != null) {
            return callEntityDamageEvent(null, entity, cause, modifiers, modifierFunctions);
        } else {
            return new EntityDamageEvent(entity.getBukkitEntity(), DamageCause.CUSTOM, modifiers, modifierFunctions); // use custom
        }
    }

    private static EntityDamageEvent callEntityDamageEvent(Entity damager, Entity damagee, DamageCause cause, Map<DamageModifier, Double> modifiers, Map<DamageModifier, Function<? super Double, Double>> modifierFunctions) {
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

    private static final Function<? super Double, Double> ZERO = Functions.constant(-0.0);

    public static EntityDamageEvent handleLivingEntityDamageEvent(Entity damagee, DamageSource source, double rawDamage, double hardHatModifier, double blockingModifier, double armorModifier, double resistanceModifier, double magicModifier, double absorptionModifier, Function<Double, Double> hardHat, Function<Double, Double> blocking, Function<Double, Double> armor, Function<Double, Double> resistance, Function<Double, Double> magic, Function<Double, Double> absorption) {
        Map<DamageModifier, Double> modifiers = new EnumMap<DamageModifier, Double>(DamageModifier.class);
        Map<DamageModifier, Function<? super Double, Double>> modifierFunctions = new EnumMap<DamageModifier, Function<? super Double, Double>>(DamageModifier.class);
        modifiers.put(DamageModifier.BASE, rawDamage);
        modifierFunctions.put(DamageModifier.BASE, ZERO);
        if (source == DamageSource.FALLING_BLOCK || source == DamageSource.ANVIL) {
            modifiers.put(DamageModifier.HARD_HAT, hardHatModifier);
            modifierFunctions.put(DamageModifier.HARD_HAT, hardHat);
        }
        if (damagee instanceof EntityPlayer) {
            modifiers.put(DamageModifier.BLOCKING, blockingModifier);
            modifierFunctions.put(DamageModifier.BLOCKING, blocking);
        }
        modifiers.put(DamageModifier.ARMOR, armorModifier);
        modifierFunctions.put(DamageModifier.ARMOR, armor);
        modifiers.put(DamageModifier.RESISTANCE, resistanceModifier);
        modifierFunctions.put(DamageModifier.RESISTANCE, resistance);
        modifiers.put(DamageModifier.MAGIC, magicModifier);
        modifierFunctions.put(DamageModifier.MAGIC, magic);
        modifiers.put(DamageModifier.ABSORPTION, absorptionModifier);
        modifierFunctions.put(DamageModifier.ABSORPTION, absorption);
        return handleEntityDamageEvent(damagee, source, modifiers, modifierFunctions);
    }

    // Non-Living Entities such as EntityEnderCrystal and EntityFireball need to call this
    public static boolean handleNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage) {
        return handleNonLivingEntityDamageEvent(entity, source, damage, true);
    }

    public static boolean handleNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage, boolean cancelOnZeroDamage) {
        if (entity instanceof EntityEnderCrystal && !(source instanceof EntityDamageSource)) {
            return false;
        }

        final EnumMap<DamageModifier, Double> modifiers = new EnumMap<DamageModifier, Double>(DamageModifier.class);
        final EnumMap<DamageModifier, Function<? super Double, Double>> functions = new EnumMap(DamageModifier.class);

        modifiers.put(DamageModifier.BASE, damage);
        functions.put(DamageModifier.BASE, ZERO);

        final EntityDamageEvent event = handleEntityDamageEvent(entity, source, modifiers, functions);
        if (event == null) {
            return false;
        }
        return event.isCancelled() || (cancelOnZeroDamage && event.getDamage() == 0);
    }

    public static PlayerLevelChangeEvent callPlayerLevelChangeEvent(Player player, int oldLevel, int newLevel) {
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(player, oldLevel, newLevel);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerExpChangeEvent callPlayerExpChangeEvent(EntityPlayer entity, int expAmount) {
        Player player = (Player) entity.getBukkitEntity();
        PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, expAmount);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerItemMendEvent callPlayerItemMendEvent(EntityPlayer entity, EntityXPOrb orb, net.minecraft.item.ItemStack nmsMendedItem, int repairAmount) {
        Player player = (Player) entity.getBukkitEntity();
        org.bukkit.inventory.ItemStack bukkitStack = CraftItemStack.asCraftMirror(nmsMendedItem);
        PlayerItemMendEvent event = new PlayerItemMendEvent(player, bukkitStack, (ExperienceOrb) orb.getBukkitEntity(), repairAmount);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleBlockGrowEvent(World world, int x, int y, int z, net.minecraft.block.Block type, int data) {
        Block block = world.getWorld().getBlockAt(x, y, z);
        CraftBlockState state = (CraftBlockState) block.getState();
        state.setTypeId(net.minecraft.block.Block.getIdFromBlock(type));
        state.setRawData((byte) data);

        BlockGrowEvent event = new BlockGrowEvent(block, state);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            state.update(true);
        }

        return !event.isCancelled();
    }

    public static FoodLevelChangeEvent callFoodLevelChangeEvent(EntityPlayer entity, int level) {
        FoodLevelChangeEvent event = new FoodLevelChangeEvent(entity.getBukkitEntity(), level);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PigZapEvent callPigZapEvent(Entity pig, Entity lightning, Entity pigzombie) {
        PigZapEvent event = new PigZapEvent((Pig) pig.getBukkitEntity(), (LightningStrike) lightning.getBukkitEntity(), (PigZombie) pigzombie.getBukkitEntity());
        pig.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static HorseJumpEvent callHorseJumpEvent(Entity horse, float power) {
        HorseJumpEvent event = new HorseJumpEvent((AbstractHorse) horse.getBukkitEntity(), power);
        horse.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, Block block, Material material) {
        return callEntityChangeBlockEvent(entity, block, material, 0);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, Block block, Material material) {
        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, Block block, Material material, boolean cancelled) {
        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0, cancelled);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, BlockPos position, net.minecraft.block.Block type, int data) {
        Block block = entity.world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
        Material material = CraftMagicNumbers.getMaterial(type);

        return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, data);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, Block block, Material material, int data) {
        return callEntityChangeBlockEvent(entity, block, material, data, false);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, Block block, Material material, int data, boolean cancelled) {
        EntityChangeBlockEvent event = new EntityChangeBlockEvent(entity, block, material, (byte) data);
        event.setCancelled(cancelled);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static CreeperPowerEvent callCreeperPowerEvent(Entity creeper, Entity lightning, CreeperPowerEvent.PowerCause cause) {
        CreeperPowerEvent event = new CreeperPowerEvent((Creeper) creeper.getBukkitEntity(), (LightningStrike) lightning.getBukkitEntity(), cause);
        creeper.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetEvent callEntityTargetEvent(Entity entity, Entity target, EntityTargetEvent.TargetReason reason) {
        EntityTargetEvent event = new EntityTargetEvent(entity.getBukkitEntity(), target == null ? null : target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetLivingEntityEvent callEntityTargetLivingEvent(Entity entity, EntityLivingBase target, EntityTargetEvent.TargetReason reason) {
        EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(entity.getBukkitEntity(), (LivingEntity) target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreakDoorEvent callEntityBreakDoorEvent(Entity entity, int x, int y, int z) {
        org.bukkit.entity.Entity entity1 = entity.getBukkitEntity();
        Block block = entity1.getWorld().getBlockAt(x, y, z);

        EntityBreakDoorEvent event = new EntityBreakDoorEvent((LivingEntity) entity1, block);
        entity1.getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static Container callInventoryOpenEvent(EntityPlayerMP player, Container container) {
        return callInventoryOpenEvent(player, container, false);
    }

    public static Container callInventoryOpenEvent(EntityPlayerMP player, Container container, boolean cancelled) {
        if (CatServer.asyncCatch("call InventoryOpenEvent")) return container;
        if (player.openContainer != player.inventoryContainer) { // fire INVENTORY_CLOSE if one already open
            player.connection.processCloseWindow(new CPacketCloseWindow(player.openContainer.windowId));
        }

        CraftServer server = player.world.getServer();
        CraftPlayer craftPlayer = player.getBukkitEntity();
        try {
            player.openContainer.transferTo(container, craftPlayer);
        } catch (AbstractMethodError e) {
            // do nothing
        }
        InventoryOpenEvent event = new InventoryOpenEvent(container.getBukkitView());
        event.setCancelled(cancelled);
        if (container.getBukkitView() != null) server.getPluginManager().callEvent(event); // CatServer - mods bypass

        if (event.isCancelled()) {
            container.transferTo(player.openContainer, craftPlayer);
            return null;
        }

        return container;
    }

    public static ItemStack callPreCraftEvent(InventoryCrafting matrix, ItemStack result, InventoryView lastCraftView, boolean isRepair) {
        CraftInventoryCrafting inventory = new CraftInventoryCrafting(matrix, matrix.resultInventory);
        inventory.setResult(CraftItemStack.asCraftMirror(result));

        PrepareItemCraftEvent event = new PrepareItemCraftEvent(inventory, lastCraftView, isRepair);
        Bukkit.getPluginManager().callEvent(event);

        org.bukkit.inventory.ItemStack bitem = event.getInventory().getResult();

        return CraftItemStack.asNMSCopy(bitem);
    }

    public static ProjectileLaunchEvent callProjectileLaunchEvent(Entity entity) {
        Projectile bukkitEntity = (Projectile) entity.getBukkitEntity();
        ProjectileLaunchEvent event = new ProjectileLaunchEvent(bukkitEntity);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static ProjectileHitEvent callProjectileHitEvent(Entity entity, RayTraceResult position) {
        Block hitBlock = null;
        if (position.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockposition = position.getBlockPos();
            hitBlock = entity.getBukkitEntity().getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        }

        ProjectileHitEvent event = new ProjectileHitEvent((Projectile) entity.getBukkitEntity(), position.entityHit == null ? null : position.entityHit.getBukkitEntity(), hitBlock);
        entity.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static ExpBottleEvent callExpBottleEvent(Entity entity, int exp) {
        ThrownExpBottle bottle = (ThrownExpBottle) entity.getBukkitEntity();
        ExpBottleEvent event = new ExpBottleEvent(bottle, exp);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockRedstoneEvent callRedstoneChange(World world, int x, int y, int z, int oldCurrent, int newCurrent) {
        BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(x, y, z), oldCurrent, newCurrent);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static NotePlayEvent callNotePlayEvent(World world, int x, int y, int z, byte instrument, byte note) {
        NotePlayEvent event = new NotePlayEvent(world.getWorld().getBlockAt(x, y, z), org.bukkit.Instrument.getByType(instrument), new org.bukkit.Note(note));
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void callPlayerItemBreakEvent(EntityPlayer human, ItemStack brokenItem) {
        CraftItemStack item = CraftItemStack.asCraftMirror(brokenItem);
        PlayerItemBreakEvent event = new PlayerItemBreakEvent((Player) human.getBukkitEntity(), item);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, int igniterX, int igniterY, int igniterZ) {
        org.bukkit.World bukkitWorld = world.getWorld();
        Block igniter = bukkitWorld.getBlockAt(igniterX, igniterY, igniterZ);
        IgniteCause cause;
        switch (igniter.getType()) {
            case LAVA:
            case STATIONARY_LAVA:
                cause = IgniteCause.LAVA;
                break;
            case DISPENSER:
                cause = IgniteCause.FLINT_AND_STEEL;
                break;
            case FIRE: // Fire or any other unknown block counts as SPREAD.
            default:
                cause = IgniteCause.SPREAD;
        }

        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), cause, igniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, Entity igniter) {
        org.bukkit.World bukkitWorld = world.getWorld();
        org.bukkit.entity.Entity bukkitIgniter = igniter.getBukkitEntity();
        IgniteCause cause;
        switch (bukkitIgniter.getType()) {
            case ENDER_CRYSTAL:
                cause = IgniteCause.ENDER_CRYSTAL;
                break;
            case LIGHTNING:
                cause = IgniteCause.LIGHTNING;
                break;
            case SMALL_FIREBALL:
            case FIREBALL:
                cause = IgniteCause.FIREBALL;
                break;
            default:
                cause = IgniteCause.FLINT_AND_STEEL;
        }

        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), cause, bukkitIgniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, Explosion explosion) {
        org.bukkit.World bukkitWorld = world.getWorld();
        org.bukkit.entity.Entity igniter = explosion.exploder == null ? null : explosion.exploder.getBukkitEntity();

        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), IgniteCause.EXPLOSION, igniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, IgniteCause cause, Entity igniter) {
        BlockIgniteEvent event = new BlockIgniteEvent(world.getWorld().getBlockAt(x, y, z), cause, igniter.getBukkitEntity());
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void handleInventoryCloseEvent(EntityPlayer human) {
        if (CatServer.asyncCatch("call InventoryCloseEvent")) return;
        InventoryCloseEvent event = new InventoryCloseEvent(human.openContainer.getBukkitView());
        if(human.openContainer.getBukkitView() != null) human.world.getServer().getPluginManager().callEvent(event); // CatServer - mods bypass
        human.openContainer.transferTo(human.inventoryContainer, human.getBukkitEntity());
    }

    public static void handleEditBookEvent(EntityPlayerMP player, ItemStack newBookItem) {
        int itemInHandIndex = player.inventory.currentItem;

        PlayerEditBookEvent editBookEvent = new PlayerEditBookEvent(player.getBukkitEntity(), player.inventory.currentItem, (BookMeta) CraftItemStack.getItemMeta(player.inventory.getCurrentItem()), (BookMeta) CraftItemStack.getItemMeta(newBookItem), newBookItem.getItem() == Items.WRITTEN_BOOK);
        player.world.getServer().getPluginManager().callEvent(editBookEvent);
        ItemStack itemInHand = player.inventory.getStackInSlot(itemInHandIndex);

        // If they've got the same item in their hand, it'll need to be updated.
        if (itemInHand != null && itemInHand.getItem() == Items.WRITABLE_BOOK) {
            if (!editBookEvent.isCancelled()) {
                if (editBookEvent.isSigning()) {
                    itemInHand.setItem(Items.WRITTEN_BOOK);
                }
                CraftMetaBook meta = (CraftMetaBook) editBookEvent.getNewBookMeta();
                List<ITextComponent> pages = meta.pages;
                for (int i = 0; i < pages.size(); i++) {
                    pages.set(i, stripEvents(pages.get(i)));
                }
                CraftItemStack.setItemMeta(itemInHand, meta);
            }

            // Client will have updated its idea of the book item; we need to overwrite that
            Slot slot = player.openContainer.getSlotFromInventory(player.inventory, itemInHandIndex);
            player.connection.sendPacket(new SPacketSetSlot(player.openContainer.windowId, slot.slotNumber, itemInHand));
        }
    }

    private static ITextComponent stripEvents(ITextComponent c) {
        Style modi = c.getStyle();
        if (modi != null) {
            modi.setClickEvent(null);
            modi.setHoverEvent(null);
        }
        c.setStyle(modi);
        if (c instanceof TextComponentTranslation) {
            TextComponentTranslation cm = (TextComponentTranslation) c;
            Object[] oo = cm.getFormatArgs();
            for (int i = 0; i < oo.length; i++) {
                Object o = oo[i];
                if (o instanceof ITextComponent) {
                    oo[i] = stripEvents((ITextComponent) o);
                }
            }
        }
        List<ITextComponent> ls = c.getSiblings();
        if (ls != null) {
            for (int i = 0; i < ls.size(); i++) {
                ls.set(i, stripEvents(ls.get(i)));
            }
        }
        return c;
    }

    public static PlayerUnleashEntityEvent callPlayerUnleashEntityEvent(EntityLiving entity, EntityPlayer player) {
        PlayerUnleashEntityEvent event = new PlayerUnleashEntityEvent(entity.getBukkitEntity(), (Player) player.getBukkitEntity());
        entity.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerLeashEntityEvent callPlayerLeashEntityEvent(EntityLiving entity, Entity leashHolder, EntityPlayer player) {
        PlayerLeashEntityEvent event = new PlayerLeashEntityEvent(entity.getBukkitEntity(), leashHolder.getBukkitEntity(), (Player) player.getBukkitEntity());
        entity.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static Cancellable handleStatisticsIncrease(EntityPlayer entityHuman, net.minecraft.stats.StatBase statistic, int current, int incrementation) {
        Player player = ((EntityPlayerMP) entityHuman).getBukkitEntity();
        Event event;
        if (true) {
            org.bukkit.Statistic stat = CraftStatistic.getBukkitStatistic(statistic);
            if (stat == null) {
                return null;
            }
            switch (stat) {
                case FALL_ONE_CM:
                case BOAT_ONE_CM:
                case CLIMB_ONE_CM:
                case DIVE_ONE_CM:
                case FLY_ONE_CM:
                case HORSE_ONE_CM:
                case MINECART_ONE_CM:
                case PIG_ONE_CM:
                case PLAY_ONE_TICK:
                case SWIM_ONE_CM:
                case WALK_ONE_CM:
                case SPRINT_ONE_CM:
                case CROUCH_ONE_CM:
                case TIME_SINCE_DEATH:
                case SNEAK_TIME:
                    // Do not process event for these - too spammy
                    return null;
                default:
            }
            if (stat.getType() == Type.UNTYPED) {
                event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation);
            } else if (stat.getType() == Type.ENTITY) {
                EntityType entityType = CraftStatistic.getEntityTypeFromStatistic(statistic);
                event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation, entityType);
            } else {
                Material material = CraftStatistic.getMaterialFromStatistic(statistic);
                event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation, material);
            }
        }
        entityHuman.world.getServer().getPluginManager().callEvent(event);
        return (Cancellable) event;
    }

    public static FireworkExplodeEvent callFireworkExplodeEvent(EntityFireworkRocket firework) {
        FireworkExplodeEvent event = new FireworkExplodeEvent((Firework) firework.getBukkitEntity());
        firework.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PrepareAnvilEvent callPrepareAnvilEvent(InventoryView view, ItemStack item) {
        PrepareAnvilEvent event = new PrepareAnvilEvent(view, CraftItemStack.asCraftMirror(item).clone());
        event.getView().getPlayer().getServer().getPluginManager().callEvent(event);
        event.getInventory().setItem(2, event.getResult());
        return event;
    }

    /**
     * Mob spawner event.
     */
    public static SpawnerSpawnEvent callSpawnerSpawnEvent(Entity spawnee, BlockPos pos) {
        org.bukkit.craftbukkit.entity.CraftEntity entity = spawnee.getBukkitEntity();
        BlockState state = entity.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()).getState();
        if (!(state instanceof org.bukkit.block.CreatureSpawner)) {
            state = null;
        }

        SpawnerSpawnEvent event = new SpawnerSpawnEvent(entity, (org.bukkit.block.CreatureSpawner) state);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityToggleGlideEvent callToggleGlideEvent(EntityLivingBase entity, boolean gliding) {
        EntityToggleGlideEvent event = new EntityToggleGlideEvent((LivingEntity) entity.getBukkitEntity(), gliding);
        entity.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static AreaEffectCloudApplyEvent callAreaEffectCloudApplyEvent(EntityAreaEffectCloud cloud, List<LivingEntity> entities) {
        AreaEffectCloudApplyEvent event = new AreaEffectCloudApplyEvent((AreaEffectCloud) cloud.getBukkitEntity(), entities);
        cloud.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static VehicleCreateEvent callVehicleCreateEvent(Entity entity) {
        Vehicle bukkitEntity = (Vehicle) entity.getBukkitEntity();
        VehicleCreateEvent event = new VehicleCreateEvent(bukkitEntity);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreedEvent callEntityBreedEvent(EntityLivingBase child, EntityLivingBase mother, EntityLivingBase father, EntityLivingBase breeder, ItemStack bredWith, int experience) {
        LivingEntity breederEntity = (LivingEntity) (breeder == null ? null : breeder.getBukkitEntity());
        CraftItemStack bredWithStack = bredWith == null ? null : CraftItemStack.asCraftMirror(bredWith).clone();

        EntityBreedEvent event = new EntityBreedEvent((LivingEntity) child.getBukkitEntity(), (LivingEntity) mother.getBukkitEntity(), (LivingEntity) father.getBukkitEntity(), breederEntity, bredWithStack, experience);
        child.world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockPhysicsEvent callBlockPhysicsEvent(World world, BlockPos blockposition) {
        Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        BlockPhysicsEvent event = new BlockPhysicsEvent(block, block.getTypeId());
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleBlockFormEvent(World world, BlockPos pos, IBlockState block, @Nullable Entity entity) {
        BlockState blockState = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()).getState();
        blockState.setType(CraftMagicNumbers.getMaterial(block.getBlock()));
        blockState.setRawData((byte) block.getBlock().getMetaFromState(block));

        BlockFormEvent event = (entity == null) ? new BlockFormEvent(blockState.getBlock(), blockState) : new EntityBlockFormEvent(entity.getBukkitEntity(), blockState.getBlock(), blockState);
        world.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            blockState.update(true);
        }

        return !event.isCancelled();
    }

    // CatServer start
    public static BlockBreakEvent callBlockBreakEvent(net.minecraft.world.World world, BlockPos pos, IBlockState iBlockState, net.minecraft.entity.player.EntityPlayerMP player)
    {
        org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(pos.getX(),pos.getY(),pos.getZ());
        org.bukkit.event.block.BlockBreakEvent blockBreakEvent = new org.bukkit.event.block.BlockBreakEvent(bukkitBlock, ((EntityPlayerMP)player).getBukkitEntity());
        EntityPlayerMP playermp = (EntityPlayerMP)player;
        net.minecraft.block.Block block = iBlockState.getBlock();
        if (!(playermp instanceof FakePlayer))
        {
            boolean isSwordNoBreak = playermp.interactionManager.getGameType().isCreative() && !playermp.getHeldItemMainhand().isEmpty() && playermp.getHeldItemMainhand().getItem() instanceof ItemSword;
            if (!isSwordNoBreak)
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
    // CatServer end
}
