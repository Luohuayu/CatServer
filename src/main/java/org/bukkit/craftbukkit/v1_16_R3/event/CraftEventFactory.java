package org.bukkit.craftbukkit.v1_16_R3.event;

import catserver.server.inventory.CatInventoryUtils;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.MerchantContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_16_R3.CraftLootTable;
import org.bukkit.craftbukkit.v1_16_R3.CraftRaid;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftRaider;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSpellcaster;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftDamageSource;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerBucketFishEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.event.raid.RaidSpawnWaveEvent;
import org.bukkit.event.raid.RaidStopEvent;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;

public class CraftEventFactory {
    public static final DamageSource MELTING = CraftDamageSource.copyOf(DamageSource.ON_FIRE);
    public static final DamageSource POISON = CraftDamageSource.copyOf(DamageSource.MAGIC);
    public static Block blockDamage; // For use in EntityDamageByBlockEvent
    public static Entity entityDamage; // For use in EntityDamageByEntityEvent

    // helper methods
    private static boolean canBuild(World world, Player player, int x, int z) {
        int spawnSize = Bukkit.getServer().getSpawnRadius();

        if (world.dimension() != World.OVERWORLD) return true;
        if (spawnSize <= 0) return true;
        if (((CraftServer) Bukkit.getServer()).getHandle().getOps().isEmpty()) return true;
        if (player.isOp()) return true;

        BlockPos chunkcoordinates = getSharedSpawnPos(world);

        int distanceFromSpawn = Math.max(Math.abs(x - chunkcoordinates.getX()), Math.abs(z - chunkcoordinates.getZ()));
        return distanceFromSpawn > spawnSize;
    }

    public static BlockPos getSharedSpawnPos(World world) {
        BlockPos blockpos = new BlockPos(world.levelData.getXSpawn(), world.levelData.getYSpawn(), world.levelData.getZSpawn());
        if (!world.getWorldBorder().isWithinBounds(blockpos)) {
            blockpos = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, new BlockPos(world.getWorldBorder().getCenterX(), 0.0D, world.getWorldBorder().getCenterZ()));
        }

        return blockpos;
    }

    public static <T extends Event> T callEvent(T event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event;
    }

    /**
     * PlayerBedEnterEvent
     */
    public static Either<PlayerEntity.SleepResult, Unit> callPlayerBedEnterEvent(PlayerEntity player, BlockPos bed, Either<PlayerEntity.SleepResult, Unit> nmsBedResult) {
        PlayerBedEnterEvent.BedEnterResult bedEnterResult = nmsBedResult.mapBoth(new Function<PlayerEntity.SleepResult, PlayerBedEnterEvent.BedEnterResult>() {
            @Override
            public PlayerBedEnterEvent.BedEnterResult apply(PlayerEntity.SleepResult t) {
                switch (t) {
                    case NOT_POSSIBLE_HERE:
                        return PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_HERE;
                    case NOT_POSSIBLE_NOW:
                        return PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_NOW;
                    case TOO_FAR_AWAY:
                        return PlayerBedEnterEvent.BedEnterResult.TOO_FAR_AWAY;
                    case NOT_SAFE:
                        return PlayerBedEnterEvent.BedEnterResult.NOT_SAFE;
                    default:
                        return PlayerBedEnterEvent.BedEnterResult.OTHER_PROBLEM;
                }
            }
        }, t -> PlayerBedEnterEvent.BedEnterResult.OK).map(java.util.function.Function.identity(), java.util.function.Function.identity());

        PlayerBedEnterEvent event = new PlayerBedEnterEvent((Player) player.getBukkitEntity(), CraftBlock.at(player.level, bed), bedEnterResult);
        Bukkit.getServer().getPluginManager().callEvent(event);

        Event.Result result = event.useBed();
        if (result == Event.Result.ALLOW) {
            return Either.right(Unit.INSTANCE);
        } else if (result == Event.Result.DENY) {
            return Either.left(PlayerEntity.SleepResult.OTHER_PROBLEM);
        }

        return nmsBedResult;
    }

    /**
     * Entity Enter Love Mode Event
     */
    public static EntityEnterLoveModeEvent callEntityEnterLoveModeEvent(PlayerEntity playerEntity, AnimalEntity entityAnimal, int loveTicks) {
        EntityEnterLoveModeEvent entityEnterLoveModeEvent = new EntityEnterLoveModeEvent((Animals) entityAnimal.getBukkitEntity(), playerEntity != null ? (HumanEntity) playerEntity.getBukkitEntity() : null, loveTicks);
        Bukkit.getPluginManager().callEvent(entityEnterLoveModeEvent);
        return entityEnterLoveModeEvent;
    }

    /**
     * Player Harvest Block Event
     */
    public static PlayerHarvestBlockEvent callPlayerHarvestBlockEvent(World world, BlockPos blockPos, PlayerEntity who, List<ItemStack> itemsToHarvest) {
        List<org.bukkit.inventory.ItemStack> bukkitItemsToHarvest = new ArrayList<>(itemsToHarvest.stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList()));
        Player player = (Player) who.getBukkitEntity();
        PlayerHarvestBlockEvent playerHarvestBlockEvent = new PlayerHarvestBlockEvent(player, CraftBlock.at(world, blockPos), bukkitItemsToHarvest);
        Bukkit.getPluginManager().callEvent(playerHarvestBlockEvent);
        return playerHarvestBlockEvent;
    }


    /**
     * Player Fish Bucket Event
     */
    public static PlayerBucketFishEvent callPlayerFishBucketEvent(AbstractFishEntity fish, PlayerEntity entityHuman, ItemStack waterBucket, ItemStack fishBucket) {
        Fish bukkitFish = (Fish) fish.getBukkitEntity();
        Player player = (Player) entityHuman.getBukkitEntity();
        PlayerBucketFishEvent playerBucketFishEvent = new PlayerBucketFishEvent(player, bukkitFish, CraftItemStack.asBukkitCopy(waterBucket), CraftItemStack.asBukkitCopy(fishBucket));
        Bukkit.getPluginManager().callEvent(playerBucketFishEvent);
        return playerBucketFishEvent;
    }

    /**
     * Trade Index Change Event
     */
    public static TradeSelectEvent callTradeSelectEvent(ServerPlayerEntity player, int newIndex, MerchantContainer merchant) {
        TradeSelectEvent tradeSelectEvent = new TradeSelectEvent(merchant.getBukkitView(), newIndex);
        Bukkit.getPluginManager().callEvent(tradeSelectEvent);
        return tradeSelectEvent;
    }

    /**
     * Block place methods
     */
    public static BlockMultiPlaceEvent callBlockMultiPlaceEvent(ServerWorld world, PlayerEntity who, Hand hand, List<BlockState> blockStates, int clickedX, int clickedY, int clickedZ) {
        CraftWorld craftWorld = world.getWorld();
        CraftServer craftServer = world.getCBServer();
        Player player = (Player) who.getBukkitEntity();

        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);

        boolean canBuild = true;
        for (int i = 0; i < blockStates.size(); i++) {
            if (!canBuild(world, player, blockStates.get(i).getX(), blockStates.get(i).getZ())) {
                canBuild = false;
                break;
            }
        }

        org.bukkit.inventory.ItemStack item;
        if (hand == Hand.MAIN_HAND) {
            item = player.getInventory().getItemInMainHand();
        } else {
            item = player.getInventory().getItemInOffHand();
        }

        BlockMultiPlaceEvent event = new BlockMultiPlaceEvent(blockStates, blockClicked, item, player, canBuild);
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    public static BlockPlaceEvent callBlockPlaceEvent(ServerWorld world, PlayerEntity who, Hand hand, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ) {
        CraftWorld craftWorld = world.getWorld();
        CraftServer craftServer = world.getCBServer();

        Player player = (Player) who.getBukkitEntity();

        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        Block placedBlock = replacedBlockState.getBlock();

        boolean canBuild = canBuild(world, player, placedBlock.getX(), placedBlock.getZ());

        org.bukkit.inventory.ItemStack item;
        EquipmentSlot equipmentSlot;
        if (hand == Hand.MAIN_HAND) {
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

    public static void handleBlockDropItemEvent(Block block, BlockState state, ServerPlayerEntity player, List<ItemEntity> items) {
        BlockDropItemEvent event = new BlockDropItemEvent(block, state, player.getBukkitEntity(), Lists.transform(items, (item) -> (Item) item.getBukkitEntity()));
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            for (ItemEntity item : items) {
                item.level.addFreshEntity(item);
            }
        }
    }

    public static EntityPlaceEvent callEntityPlaceEvent(ItemUseContext itemactioncontext, Entity entity) {
        return callEntityPlaceEvent(itemactioncontext.getLevel(), itemactioncontext.getClickedPos(), itemactioncontext.getClickedFace(), itemactioncontext.getPlayer(), entity);
    }

    public static EntityPlaceEvent callEntityPlaceEvent(World world, BlockPos clickPosition, Direction clickedFace, PlayerEntity human, Entity entity) {
        Player who = (human == null) ? null : (Player) human.getBukkitEntity();
        Block blockClicked = CraftBlock.at(world, clickPosition);
        BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);

        EntityPlaceEvent event = new EntityPlaceEvent(entity.getBukkitEntity(), who, blockClicked, blockFace);
        entity.level.getCBServer().getPluginManager().callEvent(event);

        return event;
    }

    /**
     * Bucket methods
     */
    public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(World world, PlayerEntity who, BlockPos changed, BlockPos clicked, Direction clickedFace, ItemStack itemInHand) {
        return (PlayerBucketEmptyEvent) getPlayerBucketEvent(false, world, who, changed, clicked, clickedFace, itemInHand, Items.BUCKET);
    }

    public static PlayerBucketFillEvent callPlayerBucketFillEvent(World world, PlayerEntity who, BlockPos changed, BlockPos clicked, Direction clickedFace, ItemStack itemInHand, net.minecraft.item.Item bucket) {
        return (PlayerBucketFillEvent) getPlayerBucketEvent(true, world, who, clicked, changed, clickedFace, itemInHand, bucket);
    }

    private static PlayerEvent getPlayerBucketEvent(boolean isFilling, World world, PlayerEntity who, BlockPos changed, BlockPos clicked, Direction clickedFace, ItemStack itemstack, net.minecraft.item.Item item) {
        Player player = (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asNewCraftStack(item);
        Material bucket = CraftMagicNumbers.getMaterial(itemstack.getItem());

        CraftServer craftServer = (CraftServer) player.getServer();

        Block block = CraftBlock.at(world, changed);
        Block blockClicked = CraftBlock.at(world, clicked);
        BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);

        PlayerEvent event;
        if (isFilling) {
            event = new PlayerBucketFillEvent(player, block, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketFillEvent) event).setCancelled(!canBuild(world, player, changed.getX(), changed.getZ()));
        } else {
            event = new PlayerBucketEmptyEvent(player, block, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketEmptyEvent) event).setCancelled(!canBuild(world, player, changed.getX(), changed.getZ()));
        }

        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * Player Interact event
     */
    public static PlayerInteractEvent callPlayerInteractEvent(PlayerEntity who, Action action, ItemStack itemstack, Hand hand) {
        if (action != Action.LEFT_CLICK_AIR && action != Action.RIGHT_CLICK_AIR) {
            throw new AssertionError(String.format("%s performing %s with %s", who, action, itemstack));
        }
        return callPlayerInteractEvent(who, action, null, Direction.SOUTH, itemstack, hand);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(PlayerEntity who, Action action, BlockPos position, Direction direction, ItemStack itemstack, Hand hand) {
        return callPlayerInteractEvent(who, action, position, direction, itemstack, false, hand);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(PlayerEntity who, Action action, BlockPos position, Direction direction, ItemStack itemstack, boolean cancelledBlock, Hand hand) {
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

        PlayerInteractEvent event = new PlayerInteractEvent(player, action, itemInHand, blockClicked, blockFace, (hand == null) ? null : ((hand == Hand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
        if (cancelledBlock) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * EntityTransformEvent
     */
    public static EntityTransformEvent callEntityTransformEvent(LivingEntity original, LivingEntity coverted, EntityTransformEvent.TransformReason transformReason) {
        return callEntityTransformEvent(original, Collections.singletonList(coverted), transformReason);
    }

    /**
     * EntityTransformEvent
     */
    public static EntityTransformEvent callEntityTransformEvent(LivingEntity original, List<LivingEntity> convertedList, EntityTransformEvent.TransformReason convertType) {
        List<org.bukkit.entity.Entity> list = new ArrayList<>();
        for (LivingEntity livingEntity : convertedList) {
            list.add(livingEntity.getBukkitEntity());
        }

        EntityTransformEvent event = new EntityTransformEvent(original.getBukkitEntity(), list, convertType);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * EntityShootBowEvent
     */
    public static EntityShootBowEvent callEntityShootBowEvent(LivingEntity who, ItemStack bow, ItemStack consumableItem, Entity entityArrow, Hand hand, float force, boolean consumeItem) {
        org.bukkit.entity.LivingEntity shooter = (org.bukkit.entity.LivingEntity) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(bow);
        CraftItemStack itemConsumable = CraftItemStack.asCraftMirror(consumableItem);
        org.bukkit.entity.Entity arrow = entityArrow.getBukkitEntity();
        EquipmentSlot handSlot = (hand == Hand.MAIN_HAND) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;

        if (itemInHand != null && (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0)) {
            itemInHand = null;
        }

        EntityShootBowEvent event = new EntityShootBowEvent(shooter, itemInHand, itemConsumable, arrow, handSlot, force, consumeItem);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * VillagerCareerChangeEvent
     */
    public static VillagerCareerChangeEvent callVillagerCareerChangeEvent(VillagerEntity vilager, Villager.Profession future, VillagerCareerChangeEvent.ChangeReason reason) {
        VillagerCareerChangeEvent event = new VillagerCareerChangeEvent((Villager) vilager.getBukkitEntity(), future, reason);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * BlockDamageEvent
     */
    public static BlockDamageEvent callBlockDamageEvent(PlayerEntity who, int x, int y, int z, ItemStack itemstack, boolean instaBreak) {
        Player player = (who == null) ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);

        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();

        Block blockClicked = craftWorld.getBlockAt(x, y, z);

        BlockDamageEvent event = new BlockDamageEvent(player, blockClicked, itemInHand, instaBreak);
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    public static boolean doEntityAddEventCalling(World world, Entity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (entity == null) return false;

        Cancellable event = null;
        if (entity instanceof LivingEntity && !(entity instanceof ServerPlayerEntity)) {
            boolean isAnimal = entity instanceof AnimalEntity || entity instanceof WaterMobEntity || entity instanceof GolemEntity;
            boolean isMonster = entity instanceof MonsterEntity || entity instanceof GhastEntity || entity instanceof SlimeEntity;
            boolean isNpc = entity instanceof NPC;

            if (spawnReason != CreatureSpawnEvent.SpawnReason.CUSTOM) {
                if (isAnimal && !world.getWorld().getAllowAnimals() || isMonster && !world.getWorld().getAllowMonsters() || isNpc && !world.getServer().getServer().areNpcsEnabled()) {
                    entity.removed = true;
                    return false;
                }
            }

            event = CraftEventFactory.callCreatureSpawnEvent((LivingEntity) entity, spawnReason);
        } else if (entity instanceof ItemEntity) {
            event = CraftEventFactory.callItemSpawnEvent((ItemEntity) entity);
        } else if (entity.getBukkitEntity() instanceof Projectile) {
            // Not all projectiles extend EntityProjectile, so check for Bukkit interface instead
            event = CraftEventFactory.callProjectileLaunchEvent(entity);
        } else if (entity.getBukkitEntity() instanceof Vehicle) {
            event = CraftEventFactory.callVehicleCreateEvent(entity);
        } else if (entity.getBukkitEntity() instanceof LightningStrike) {
            LightningStrikeEvent.Cause cause = (
                    spawnReason == CreatureSpawnEvent.SpawnReason.COMMAND ?
                            LightningStrikeEvent.Cause.COMMAND : LightningStrikeEvent.Cause.UNKNOWN);
            event = CraftEventFactory.callLightningStrikeEvent((LightningStrike) entity.getBukkitEntity(), cause);
        }
        // Spigot start
        else if (entity instanceof ExperienceOrbEntity) {
            ExperienceOrbEntity xp = (ExperienceOrbEntity) entity;
            double radius = world.spigotConfig.expMerge;
            if (radius > 0) {
                List<Entity> entities = world.getEntities(entity, entity.getBoundingBox().inflate(radius, radius, radius));
                for (Entity e : entities) {
                    if (e instanceof ExperienceOrbEntity) {
                        ExperienceOrbEntity loopItem = (ExperienceOrbEntity) e;
                        if (!loopItem.removed) {
                            xp.value += loopItem.value;
                            loopItem.remove();
                        }
                    }
                }
            }
            // Spigot end
        } else if (!(entity instanceof ServerPlayerEntity)) {
            event = CraftEventFactory.callEntitySpawnEvent(entity);
        }

        if (event != null && (event.isCancelled() || entity.removed)) {
            Entity vehicle = entity.getVehicle();
            if (vehicle != null) {
                vehicle.removed = true;
            }
            for (Entity passenger : entity.getIndirectPassengers()) {
                passenger.removed = true;
            }
            entity.removed = true;
            return false;
        }

        return true;
    }

    /**
     * EntitySpawnEvent
     */
    public static EntitySpawnEvent callEntitySpawnEvent(Entity entity) {
        org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();

        EntitySpawnEvent event = new EntitySpawnEvent(bukkitEntity);
        bukkitEntity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    /**
     * CreatureSpawnEvent
     */
    public static CreatureSpawnEvent callCreatureSpawnEvent(LivingEntity livingEntity, CreatureSpawnEvent.SpawnReason spawnReason) {
        org.bukkit.entity.LivingEntity entity = (org.bukkit.entity.LivingEntity) livingEntity.getBukkitEntity();
        CraftServer craftServer = (CraftServer) entity.getServer();

        CreatureSpawnEvent event = new CreatureSpawnEvent(entity, spawnReason);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * EntityTameEvent
     */
    public static EntityTameEvent callEntityTameEvent(MobEntity entity, PlayerEntity tamer) {
        org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
        org.bukkit.entity.AnimalTamer bukkitTamer = (tamer != null ? tamer.getBukkitEntity() : null);
        CraftServer craftServer = (CraftServer) bukkitEntity.getServer();

        entity.persistenceRequired = true;

        EntityTameEvent event = new EntityTameEvent((org.bukkit.entity.LivingEntity) bukkitEntity, bukkitTamer);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * ItemSpawnEvent
     */
    public static ItemSpawnEvent callItemSpawnEvent(ItemEntity entityitem) {
        Item entity = (Item) entityitem.getBukkitEntity();
        CraftServer craftServer = (CraftServer) entity.getServer();

        ItemSpawnEvent event = new ItemSpawnEvent(entity);

        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * ItemDespawnEvent
     */
    public static ItemDespawnEvent callItemDespawnEvent(ItemEntity entityitem) {
        Item entity = (Item) entityitem.getBukkitEntity();

        ItemDespawnEvent event = new ItemDespawnEvent(entity, entity.getLocation());

        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    /**
     * ItemMergeEvent
     */
    public static ItemMergeEvent callItemMergeEvent(ItemEntity merging, ItemEntity mergingWith) {
        Item entityMerging = (Item) merging.getBukkitEntity();
        Item entityMergingWith = (Item) mergingWith.getBukkitEntity();

        ItemMergeEvent event = new ItemMergeEvent(entityMerging, entityMergingWith);

        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * PotionSplashEvent
     */
    public static PotionSplashEvent callPotionSplashEvent(PotionEntity potion, Map<org.bukkit.entity.LivingEntity, Double> affectedEntities) {
        ThrownPotion thrownPotion = (ThrownPotion) potion.getBukkitEntity();

        PotionSplashEvent event = new PotionSplashEvent(thrownPotion, affectedEntities);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static LingeringPotionSplashEvent callLingeringPotionSplashEvent(PotionEntity potion, AreaEffectCloudEntity cloud) {
        ThrownPotion thrownPotion = (ThrownPotion) potion.getBukkitEntity();
        AreaEffectCloud effectCloud = (AreaEffectCloud) cloud.getBukkitEntity();

        LingeringPotionSplashEvent event = new LingeringPotionSplashEvent(thrownPotion, effectCloud);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * BlockFadeEvent
     */
    public static BlockFadeEvent callBlockFadeEvent(IWorld world, BlockPos pos, net.minecraft.block.BlockState newBlock) {
        CraftBlockState state = CraftBlockState.getBlockState(world, pos);
        state.setData(newBlock);

        BlockFadeEvent event = new BlockFadeEvent(state.getBlock(), state);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleMoistureChangeEvent(World world, BlockPos pos, net.minecraft.block.BlockState newBlock, int flag) {
        CraftBlockState state = CraftBlockState.getBlockState(world, pos, flag);
        state.setData(newBlock);

        MoistureChangeEvent event = new MoistureChangeEvent(state.getBlock(), state);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            state.update(true);
        }
        return !event.isCancelled();
    }

    public static boolean handleBlockSpreadEvent(World world, BlockPos source, BlockPos target, net.minecraft.block.BlockState block) {
        return handleBlockSpreadEvent(world, source, target, block, 2);
    }

    public static boolean handleBlockSpreadEvent(World world, BlockPos source, BlockPos target, net.minecraft.block.BlockState block, int flag) {
        CraftBlockState state = CraftBlockState.getBlockState(world, target, flag);
        state.setData(block);

        BlockSpreadEvent event = new BlockSpreadEvent(world.getWorld().getBlockAt(target.getX(), target.getY(), target.getZ()), world.getWorld().getBlockAt(source.getX(), source.getY(), source.getZ()), state);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            state.update(true);
        }
        return !event.isCancelled();
    }

    public static EntityDeathEvent callEntityDeathEvent(LivingEntity victim) {
        return callEntityDeathEvent(victim, new ArrayList<org.bukkit.inventory.ItemStack>(0));
    }

    public static EntityDeathEvent callEntityDeathEvent(LivingEntity victim, List<org.bukkit.inventory.ItemStack> drops) {
        CraftLivingEntity entity = (CraftLivingEntity) victim.getBukkitEntity();
        EntityDeathEvent event = new EntityDeathEvent(entity, drops, victim.expToDrop);
        CraftWorld world = (CraftWorld) entity.getWorld();
        Bukkit.getServer().getPluginManager().callEvent(event);

        victim.expToDrop = event.getDroppedExp();

        for (org.bukkit.inventory.ItemStack stack : event.getDrops()) {
            if (stack == null || stack.getType() == Material.AIR || stack.getAmount() == 0) continue;

            world.dropItem(entity.getLocation(), stack); // Paper - note: dropItem already clones due to this being bukkit -> NMS
            if (stack instanceof CraftItemStack)
                stack.setAmount(0); // Paper - destroy this item - if this ever leaks due to game bugs, ensure it doesn't dupe, but don't nuke bukkit stacks of manually added items
        }

        victim.dropExperience();

        return event;
    }

    public static PlayerDeathEvent callPlayerDeathEvent(ServerPlayerEntity victim, List<org.bukkit.inventory.ItemStack> drops, String deathMessage, boolean keepInventory) {
        CraftPlayer entity = victim.getBukkitEntity();
        PlayerDeathEvent event = new PlayerDeathEvent(entity, drops, victim.getExpReward(), 0, deathMessage);
        event.setKeepInventory(keepInventory);
        org.bukkit.World world = entity.getWorld();
        Bukkit.getServer().getPluginManager().callEvent(event);

        victim.keepLevel = event.getKeepLevel();
        victim.newLevel = event.getNewLevel();
        victim.newTotalExp = event.getNewTotalExp();
        victim.expToDrop = event.getDroppedExp();
        victim.newExp = event.getNewExp();

        for (org.bukkit.inventory.ItemStack stack : event.getDrops()) {
            if (stack == null || stack.getType() == Material.AIR) continue;

            world.dropItem(entity.getLocation(), stack);
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

    private static EntityDamageEvent handleEntityDamageEvent(Entity entity, DamageSource source, Map<EntityDamageEvent.DamageModifier, Double> modifiers, Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions) {
        return handleEntityDamageEvent(entity, source, modifiers, modifierFunctions, false);
    }

    private static EntityDamageEvent handleEntityDamageEvent(Entity entity, DamageSource source, Map<EntityDamageEvent.DamageModifier, Double> modifiers, Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions, boolean cancelled) {
        if (source.isExplosion()) {
            EntityDamageEvent.DamageCause damageCause;
            Entity damager = entityDamage;
            entityDamage = null;
            EntityDamageEvent event;
            if (damager == null) {
                event = new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), DamageCause.BLOCK_EXPLOSION, modifiers, modifierFunctions);
            } else if (entity instanceof EnderDragonEntity && /*PAIL FIXME ((EnderDragonEntity) entity).target == damager*/ false) {
                event = new EntityDamageEvent(entity.getBukkitEntity(), DamageCause.ENTITY_EXPLOSION, modifiers, modifierFunctions);
            } else {
                if (damager instanceof org.bukkit.entity.TNTPrimed) {
                    damageCause = DamageCause.BLOCK_EXPLOSION;
                } else {
                    damageCause = DamageCause.ENTITY_EXPLOSION;
                }
                event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), entity.getBukkitEntity(), damageCause, modifiers, modifierFunctions);
            }
            event.setCancelled(cancelled);

            callEvent(event);

            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (source instanceof EntityDamageSource) {
            Entity damager = source.getEntity();
            DamageCause cause = (source.isSweep()) ? DamageCause.ENTITY_SWEEP_ATTACK : DamageCause.ENTITY_ATTACK;

            if (source instanceof IndirectEntityDamageSource) {
                damager = ((IndirectEntityDamageSource) source).getProximateDamageSource();
                if (damager != null) {
                    if (damager.getBukkitEntity() instanceof ThrownPotion) {
                        cause = DamageCause.MAGIC;
                    } else if (damager.getBukkitEntity() instanceof Projectile) {
                        cause = DamageCause.PROJECTILE;
                    }
                }
            } else if ("thorns".equals(source.msgId)) {
                cause = DamageCause.THORNS;
            }

            return callEntityDamageEvent(damager, entity, cause, modifiers, modifierFunctions, cancelled);
        } else if (source == DamageSource.OUT_OF_WORLD) {
            EntityDamageEvent event = new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), DamageCause.VOID, modifiers, modifierFunctions);
            event.setCancelled(cancelled);
            callEvent(event);
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (source == DamageSource.LAVA) {
            EntityDamageEvent event = (new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), DamageCause.LAVA, modifiers, modifierFunctions));
            event.setCancelled(cancelled);
            callEvent(event);
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        } else if (blockDamage != null) {
            DamageCause cause = null;
            Block damager = blockDamage;
            blockDamage = null;
            if (source == DamageSource.CACTUS || source == DamageSource.SWEET_BERRY_BUSH) {
                cause = DamageCause.CONTACT;
            } else if (source == DamageSource.HOT_FLOOR) {
                cause = DamageCause.HOT_FLOOR;
            } else if (source == DamageSource.MAGIC) {
                cause = DamageCause.MAGIC;
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
            } else if (source == MELTING) {
                cause = DamageCause.MELTING;
            } else if (source == POISON) {
                cause = DamageCause.POISON;
            } else if (source == DamageSource.LIGHTNING_BOLT) {
                cause = DamageCause.LIGHTNING;
            } else if (source == DamageSource.GENERIC) {
                cause = DamageCause.CUSTOM;
            } else {
                cause = DamageCause.CUSTOM;
            }
            EntityDamageEvent event = new EntityDamageByBlockEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions);
            event.setCancelled(cancelled);
            callEvent(event);
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
            EntityDamageEvent event = new EntityDamageByEntityEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions);
            event.setCancelled(cancelled);
            callEvent(event);
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
        } else if (source == DamageSource.DRY_OUT) {
            cause = DamageCause.DRYOUT;
        } else if (source == DamageSource.GENERIC) {
            cause = DamageCause.CUSTOM;
        }

        if (cause != null) {
            return callEntityDamageEvent(null, entity, cause, modifiers, modifierFunctions, cancelled);
        } else {
            return new EntityDamageEvent(entity.getBukkitEntity(), DamageCause.CUSTOM, modifiers, modifierFunctions);
        }
    }

    private static EntityDamageEvent callEntityDamageEvent(Entity damager, Entity damagee, DamageCause cause, Map<DamageModifier, Double> modifiers, Map<DamageModifier, Function<? super Double, Double>> modifierFunctions) {
        return callEntityDamageEvent(damager, damagee, cause, modifiers, modifierFunctions, false);
    }

    private static EntityDamageEvent callEntityDamageEvent(Entity damager, Entity damagee, DamageCause cause, Map<DamageModifier, Double> modifiers, Map<DamageModifier, Function<? super Double, Double>> modifierFunctions, boolean cancelled) {
        EntityDamageEvent event;
        if (damager != null) {
            event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
        } else {
            event = new EntityDamageEvent(damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
        }
        event.setCancelled(cancelled);
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
        if (damagee instanceof PlayerEntity) {
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
        return handleNonLivingEntityDamageEvent(entity, source, damage, cancelOnZeroDamage, false);
    }

    public static boolean handleNonLivingEntityDamageEvent(Entity entity, DamageSource source, double damage, boolean cancelOnZeroDamage, boolean cancelled) {
        final EnumMap<DamageModifier, Double> modifiers = new EnumMap<DamageModifier, Double>(DamageModifier.class);
        final EnumMap<DamageModifier, Function<? super Double, Double>> functions = new EnumMap(DamageModifier.class);

        modifiers.put(DamageModifier.BASE, damage);
        functions.put(DamageModifier.BASE, ZERO);

        final EntityDamageEvent event = handleEntityDamageEvent(entity, source, modifiers, functions, cancelled);

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

    public static PlayerExpChangeEvent callPlayerExpChangeEvent(PlayerEntity entity, int expAmount) {
        Player player = (Player) entity.getBukkitEntity();
        PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, expAmount);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerItemMendEvent callPlayerItemMendEvent(PlayerEntity entity, ExperienceOrbEntity orb, net.minecraft.item.ItemStack nmsMendedItem, int repairAmount) {
        Player player = (Player) entity.getBukkitEntity();
        org.bukkit.inventory.ItemStack bukkitStack = CraftItemStack.asCraftMirror(nmsMendedItem);
        PlayerItemMendEvent event = new PlayerItemMendEvent(player, bukkitStack, (ExperienceOrb) orb.getBukkitEntity(), repairAmount);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleBlockGrowEvent(World world, BlockPos pos, net.minecraft.block.BlockState block) {
        return handleBlockGrowEvent(world, pos, block, 3);
    }

    public static boolean handleBlockGrowEvent(World world, BlockPos pos, net.minecraft.block.BlockState newData, int flag) {
        Block block = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        CraftBlockState state = (CraftBlockState) block.getState();
        state.setData(newData);

        BlockGrowEvent event = new BlockGrowEvent(block, state);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            state.update(true);
        }

        return !event.isCancelled();
    }

    public static FluidLevelChangeEvent callFluidLevelChangeEvent(World world, BlockPos block, net.minecraft.block.BlockState newData) {
        FluidLevelChangeEvent event = new FluidLevelChangeEvent(CraftBlock.at(world, block), CraftBlockData.fromData(newData));
        world.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static FoodLevelChangeEvent callFoodLevelChangeEvent(PlayerEntity entity, int level) {
        return callFoodLevelChangeEvent(entity, level, null);
    }

    public static FoodLevelChangeEvent callFoodLevelChangeEvent(PlayerEntity entity, int level, ItemStack item) {
        FoodLevelChangeEvent event = new FoodLevelChangeEvent(entity.getBukkitEntity(), level, (item == null) ? null : CraftItemStack.asBukkitCopy(item));
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

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, BlockPos position, net.minecraft.block.BlockState newBlock) {
        return callEntityChangeBlockEvent(entity, position, newBlock, false);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, BlockPos position, net.minecraft.block.BlockState newBlock, boolean cancelled) {
        Block block = entity.level.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());

        EntityChangeBlockEvent event = new EntityChangeBlockEvent(entity.getBukkitEntity(), block, CraftBlockData.fromData(newBlock));
        event.setCancelled(cancelled);
        event.getEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static CreeperPowerEvent callCreeperPowerEvent(Entity creeper, Entity lightning, CreeperPowerEvent.PowerCause cause) {
        CreeperPowerEvent event = new CreeperPowerEvent((Creeper) creeper.getBukkitEntity(), (LightningStrike) lightning.getBukkitEntity(), cause);
        creeper.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetEvent callEntityTargetEvent(Entity entity, Entity target, EntityTargetEvent.TargetReason reason) {
        EntityTargetEvent event = new EntityTargetEvent(entity.getBukkitEntity(), (target == null) ? null : target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetLivingEntityEvent callEntityTargetLivingEvent(Entity entity, LivingEntity target, EntityTargetEvent.TargetReason reason) {
        EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(entity.getBukkitEntity(), (target == null) ? null : (org.bukkit.entity.LivingEntity) target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreakDoorEvent callEntityBreakDoorEvent(Entity entity, BlockPos pos) {
        org.bukkit.entity.Entity entity1 = entity.getBukkitEntity();
        Block block = CraftBlock.at(entity.level, pos);

        EntityBreakDoorEvent event = new EntityBreakDoorEvent((org.bukkit.entity.LivingEntity) entity1, block);
        entity1.getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static Container callInventoryOpenEvent(ServerPlayerEntity player, Container container) {
        return callInventoryOpenEvent(player, container, false);
    }

    public static Container callInventoryOpenEvent(ServerPlayerEntity player, Container container, boolean cancelled) {
        if (player.containerMenu != player.inventoryMenu) { // fire INVENTORY_CLOSE if one already open
            player.connection.handleContainerClose(new CCloseWindowPacket(player.containerMenu.containerId));
        }

        CraftServer server = player.level.getCBServer();
        CraftPlayer craftPlayer = player.getBukkitEntity();
        try {
            player.containerMenu.transferTo(container, craftPlayer);
        } catch (AbstractMethodError e) {
        }

        InventoryOpenEvent event = new InventoryOpenEvent(container.getBukkitView());
        event.setCancelled(cancelled);
        if (container.getBukkitView() != null) {
            server.getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            container.transferTo(player.containerMenu, craftPlayer);
            if (!cancelled) { // fire INVENTORY_CLOSE if one already open
                player.containerMenu = container; // make sure the right container is processed
                player.closeContainer();
                player.containerMenu = player.inventoryMenu;
            }
            return null;
        }

        return container;
    }

    public static ItemStack callPreCraftEvent(IInventory matrix, IInventory resultInventory, ItemStack result, InventoryView lastCraftView, boolean isRepair) {
        CraftInventoryCrafting inventory = new CraftInventoryCrafting(matrix, resultInventory);
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
        if (position.getType() == RayTraceResult.Type.MISS) {
            return null;
        }

        Block hitBlock = null;
        BlockFace hitFace = null;
        if (position.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult positionBlock = (BlockRayTraceResult) position;
            hitBlock = CraftBlock.at(entity.level, positionBlock.getBlockPos());
            hitFace = CraftBlock.notchToBlockFace(positionBlock.getDirection());
        }

        org.bukkit.entity.Entity hitEntity = null;
        if (position.getType() == RayTraceResult.Type.ENTITY) {
            hitEntity = ((EntityRayTraceResult) position).getEntity().getBukkitEntity();
        }

        ProjectileHitEvent event = new ProjectileHitEvent((Projectile) entity.getBukkitEntity(), hitEntity, hitBlock, hitFace);
        entity.level.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static ExpBottleEvent callExpBottleEvent(Entity entity, int exp) {
        ThrownExpBottle bottle = (ThrownExpBottle) entity.getBukkitEntity();
        ExpBottleEvent event = new ExpBottleEvent(bottle, exp);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static LightningStrikeEvent callLightningStrikeEvent(LightningStrike entity, LightningStrikeEvent.Cause cause) {
        LightningStrikeEvent event = new LightningStrikeEvent(entity.getWorld(), entity, cause);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockRedstoneEvent callRedstoneChange(World world, BlockPos pos, int oldCurrent, int newCurrent) {
        BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), oldCurrent, newCurrent);
        world.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static NotePlayEvent callNotePlayEvent(World world, BlockPos pos, NoteBlockInstrument instrument, int note) {
        NotePlayEvent event = new NotePlayEvent(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), org.bukkit.Instrument.getByType((byte) instrument.ordinal()), new org.bukkit.Note(note));
        world.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void callPlayerItemBreakEvent(PlayerEntity human, ItemStack brokenItem) {
        CraftItemStack item = CraftItemStack.asCraftMirror(brokenItem);
        PlayerItemBreakEvent event = new PlayerItemBreakEvent((Player) human.getBukkitEntity(), item);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, BlockPos block, BlockPos source) {
        org.bukkit.World bukkitWorld = world.getWorld();
        Block igniter = bukkitWorld.getBlockAt(source.getX(), source.getY(), source.getZ());
        BlockIgniteEvent.IgniteCause cause;
        switch (igniter.getType()) {
            case LAVA:
                cause = BlockIgniteEvent.IgniteCause.LAVA;
                break;
            case DISPENSER:
                cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
                break;
            case FIRE: // Fire or any other unknown block counts as SPREAD.
            default:
                cause = BlockIgniteEvent.IgniteCause.SPREAD;
        }

        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(block.getX(), block.getY(), block.getZ()), cause, igniter);
        world.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, BlockPos pos, Entity igniter) {
        org.bukkit.World bukkitWorld = world.getWorld();
        org.bukkit.entity.Entity bukkitIgniter = igniter.getBukkitEntity();
        BlockIgniteEvent.IgniteCause cause;
        switch (bukkitIgniter.getType()) {
            case ENDER_CRYSTAL:
                cause = BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL;
                break;
            case LIGHTNING:
                cause = BlockIgniteEvent.IgniteCause.LIGHTNING;
                break;
            case SMALL_FIREBALL:
            case FIREBALL:
                cause = BlockIgniteEvent.IgniteCause.FIREBALL;
                break;
            case ARROW:
                cause = BlockIgniteEvent.IgniteCause.ARROW;
                break;
            default:
                cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
        }

        if (igniter instanceof ProjectileEntity) {
            Entity shooter = ((ProjectileEntity) igniter).getOwner();
            if (shooter != null) {
                bukkitIgniter = shooter.getBukkitEntity();
            }
        }

        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(pos.getX(), pos.getY(), pos.getZ()), cause, bukkitIgniter);
        world.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, int x, int y, int z, net.minecraft.world.Explosion explosion) {
        org.bukkit.World bukkitWorld = world.getWorld();
        org.bukkit.entity.Entity igniter = explosion.source == null ? null : explosion.source.getBukkitEntity();

        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), BlockIgniteEvent.IgniteCause.EXPLOSION, igniter);
        world.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(World world, BlockPos pos, BlockIgniteEvent.IgniteCause cause, Entity igniter) {
        BlockIgniteEvent event = new BlockIgniteEvent(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), cause, igniter.getBukkitEntity());
        world.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void handleInventoryCloseEvent(PlayerEntity human) {
        InventoryCloseEvent event = new InventoryCloseEvent(human.containerMenu.getBukkitView());
        if (human.containerMenu.getBukkitView() != null) {
            human.level.getCBServer().getPluginManager().callEvent(event);
        }
        human.containerMenu.transferTo(human.inventoryMenu, human.getBukkitEntity());
    }

    public static ItemStack handleEditBookEvent(ServerPlayerEntity player, int itemInHandIndex, ItemStack itemInHand, ItemStack newBookItem) {
        PlayerEditBookEvent editBookEvent = new PlayerEditBookEvent(player.getBukkitEntity(), (itemInHandIndex >= 0 && itemInHandIndex <= 8) ? itemInHandIndex : -1, (BookMeta) CraftItemStack.getItemMeta(itemInHand), (BookMeta) CraftItemStack.getItemMeta(newBookItem), newBookItem.getItem() == Items.WRITTEN_BOOK);
        player.level.getCBServer().getPluginManager().callEvent(editBookEvent);

        // If they've got the same item in their hand, it'll need to be updated.
        if (itemInHand != null && itemInHand.getItem() == Items.WRITABLE_BOOK) {
            if (!editBookEvent.isCancelled()) {
                if (editBookEvent.isSigning()) {
                    itemInHand.setItem(Items.WRITTEN_BOOK);
                }
                CraftItemStack.setItemMeta(itemInHand, editBookEvent.getNewBookMeta());
                player.inventory.setItem(itemInHandIndex, itemInHand);
                player.refreshContainer(player.inventoryMenu);
            }
        }

        return itemInHand;
    }

    public static PlayerUnleashEntityEvent callPlayerUnleashEntityEvent(MobEntity entity, PlayerEntity player) {
        PlayerUnleashEntityEvent event = new PlayerUnleashEntityEvent(entity.getBukkitEntity(), (Player) player.getBukkitEntity());
        entity.level.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerLeashEntityEvent callPlayerLeashEntityEvent(MobEntity entity, Entity leashHolder, PlayerEntity player) {
        PlayerLeashEntityEvent event = new PlayerLeashEntityEvent(entity.getBukkitEntity(), leashHolder.getBukkitEntity(), (Player) player.getBukkitEntity());
        entity.level.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockShearEntityEvent callBlockShearEntityEvent(Entity animal, Block dispenser, CraftItemStack is) {
        BlockShearEntityEvent bse = new BlockShearEntityEvent(dispenser, animal.getBukkitEntity(), is);
        Bukkit.getPluginManager().callEvent(bse);
        return bse;
    }

    public static boolean handlePlayerShearEntityEvent(PlayerEntity player, Entity sheared, ItemStack shears, Hand hand) {
        if (!(player instanceof ServerPlayerEntity)) {
            return true;
        }

        PlayerShearEntityEvent event = new PlayerShearEntityEvent((Player) player.getBukkitEntity(), sheared.getBukkitEntity(), CraftItemStack.asCraftMirror(shears), (hand == Hand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static Cancellable handleStatisticsIncrease(PlayerEntity playerEntity, net.minecraft.stats.Stat<?> statistic, int current, int newValue) {
        Player player = ((ServerPlayerEntity) playerEntity).getBukkitEntity();
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
                case WALK_ON_WATER_ONE_CM:
                case WALK_UNDER_WATER_ONE_CM:
                case FLY_ONE_CM:
                case HORSE_ONE_CM:
                case MINECART_ONE_CM:
                case PIG_ONE_CM:
                case PLAY_ONE_MINUTE:
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
            if (stat.getType() == Statistic.Type.UNTYPED) {
                event = new PlayerStatisticIncrementEvent(player, stat, current, newValue);
            } else if (stat.getType() == Statistic.Type.ENTITY) {
                org.bukkit.entity.EntityType entityType = CraftStatistic.getEntityTypeFromStatistic((net.minecraft.stats.Stat<net.minecraft.entity.EntityType<?>>) statistic);
                event = new PlayerStatisticIncrementEvent(player, stat, current, newValue, entityType);
            } else {
                Material material = CraftStatistic.getMaterialFromStatistic(statistic);
                event = new PlayerStatisticIncrementEvent(player, stat, current, newValue, material);
            }
        }
        playerEntity.level.getCBServer().getPluginManager().callEvent(event);
        return (Cancellable) event;
    }

    public static FireworkExplodeEvent callFireworkExplodeEvent(FireworkRocketEntity firework) {
        FireworkExplodeEvent event = new FireworkExplodeEvent((Firework) firework.getBukkitEntity());
        firework.level.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PrepareAnvilEvent callPrepareAnvilEvent(InventoryView view, ItemStack item) {
        PrepareAnvilEvent event = new PrepareAnvilEvent(view, CraftItemStack.asCraftMirror(item).clone());
        event.getView().getPlayer().getServer().getPluginManager().callEvent(event);
        event.getInventory().setItem(2, event.getResult());
        return event;
    }

    public static PrepareSmithingEvent callPrepareSmithingEvent(InventoryView view, ItemStack item) {
        PrepareSmithingEvent event = new PrepareSmithingEvent(view, CraftItemStack.asCraftMirror(item).clone());
        event.getView().getPlayer().getServer().getPluginManager().callEvent(event);
        event.getInventory().setItem(2, event.getResult());
        return event;
    }

    /**
     * Mob spawner event.
     */
    public static SpawnerSpawnEvent callSpawnerSpawnEvent(Entity spawnee, BlockPos pos) {
        org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity entity = spawnee.getBukkitEntity();
        BlockState state = entity.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()).getState();
        if (!(state instanceof org.bukkit.block.CreatureSpawner)) {
            state = null;
        }

        SpawnerSpawnEvent event = new SpawnerSpawnEvent(entity, (org.bukkit.block.CreatureSpawner) state);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityToggleGlideEvent callToggleGlideEvent(LivingEntity entity, boolean gliding) {
        EntityToggleGlideEvent event = new EntityToggleGlideEvent((org.bukkit.entity.LivingEntity) entity.getBukkitEntity(), gliding);
        entity.level.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityToggleSwimEvent callToggleSwimEvent(LivingEntity entity, boolean swimming) {
        EntityToggleSwimEvent event = new EntityToggleSwimEvent((org.bukkit.entity.LivingEntity) entity.getBukkitEntity(), swimming);
        entity.level.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static AreaEffectCloudApplyEvent callAreaEffectCloudApplyEvent(AreaEffectCloudEntity cloud, List<org.bukkit.entity.LivingEntity> entities) {
        AreaEffectCloudApplyEvent event = new AreaEffectCloudApplyEvent((AreaEffectCloud) cloud.getBukkitEntity(), entities);
        cloud.level.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static VehicleCreateEvent callVehicleCreateEvent(Entity entity) {
        Vehicle bukkitEntity = (Vehicle) entity.getBukkitEntity();
        VehicleCreateEvent event = new VehicleCreateEvent(bukkitEntity);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreedEvent callEntityBreedEvent(LivingEntity child, LivingEntity mother, LivingEntity father, LivingEntity breeder, ItemStack bredWith, int experience) {
        org.bukkit.entity.LivingEntity breederEntity = (org.bukkit.entity.LivingEntity) (breeder == null ? null : breeder.getBukkitEntity());
        CraftItemStack bredWithStack = bredWith == null ? null : CraftItemStack.asCraftMirror(bredWith).clone();

        EntityBreedEvent event = new EntityBreedEvent((org.bukkit.entity.LivingEntity) child.getBukkitEntity(), (org.bukkit.entity.LivingEntity) mother.getBukkitEntity(), (org.bukkit.entity.LivingEntity) father.getBukkitEntity(), breederEntity, bredWithStack, experience);
        child.level.getCBServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockPhysicsEvent callBlockPhysicsEvent(IWorld world, BlockPos blockPos) {
        Block block = CraftBlock.at(world, blockPos);
        BlockPhysicsEvent event = new BlockPhysicsEvent(block, block.getBlockData());
        // Suppress during worldgen
        if (world instanceof World) {
            world.getMinecraftWorld().getServer().server.getPluginManager().callEvent(event);
        }
        return event;
    }

    public static boolean handleBlockFormEvent(World world, BlockPos pos, net.minecraft.block.BlockState block) {
        return handleBlockFormEvent(world, pos, block, 3);
    }

    public static EntityPotionEffectEvent callEntityPotionEffectChangeEvent(LivingEntity entity, @Nullable EffectInstance oldEffect, @Nullable EffectInstance newEffect, EntityPotionEffectEvent.Cause cause) {
        return callEntityPotionEffectChangeEvent(entity, oldEffect, newEffect, cause, true);
    }

    public static EntityPotionEffectEvent callEntityPotionEffectChangeEvent(LivingEntity entity, @Nullable EffectInstance oldEffect, @Nullable EffectInstance newEffect, EntityPotionEffectEvent.Cause cause, EntityPotionEffectEvent.Action action) {
        return callEntityPotionEffectChangeEvent(entity, oldEffect, newEffect, cause, action, true);
    }

    public static EntityPotionEffectEvent callEntityPotionEffectChangeEvent(LivingEntity entity, @Nullable EffectInstance oldEffect, @Nullable EffectInstance newEffect, EntityPotionEffectEvent.Cause cause, boolean willOverride) {
        EntityPotionEffectEvent.Action action = EntityPotionEffectEvent.Action.CHANGED;
        if (oldEffect == null) {
            action = EntityPotionEffectEvent.Action.ADDED;
        } else if (newEffect == null) {
            action = EntityPotionEffectEvent.Action.REMOVED;
        }

        return callEntityPotionEffectChangeEvent(entity, oldEffect, newEffect, cause, action, willOverride);
    }

    public static EntityPotionEffectEvent callEntityPotionEffectChangeEvent(LivingEntity entity, @Nullable EffectInstance oldEffect, @Nullable EffectInstance newEffect, EntityPotionEffectEvent.Cause cause, EntityPotionEffectEvent.Action action, boolean willOverride) {
        PotionEffect bukkitOldEffect = (oldEffect == null) ? null : CraftPotionUtil.toBukkit(oldEffect);
        PotionEffect bukkitNewEffect = (newEffect == null) ? null : CraftPotionUtil.toBukkit(newEffect);

        if (bukkitOldEffect == null && bukkitNewEffect == null) {
            throw new IllegalStateException("Old and new potion effect are both null");
        }

        EntityPotionEffectEvent event = new EntityPotionEffectEvent((org.bukkit.entity.LivingEntity) entity.getBukkitEntity(), bukkitOldEffect, bukkitNewEffect, cause, action, willOverride);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static boolean handleBlockFormEvent(World world, BlockPos pos, net.minecraft.block.BlockState block, @Nullable Entity entity) {
        return handleBlockFormEvent(world, pos, block, 3, entity);
    }

    public static boolean handleBlockFormEvent(World world, BlockPos pos, net.minecraft.block.BlockState block, int flag) {
        return handleBlockFormEvent(world, pos, block, flag, null);
    }

    public static boolean handleBlockFormEvent(World world, BlockPos pos, net.minecraft.block.BlockState block, int flag, @Nullable Entity entity) {
        CraftBlockState blockState = CraftBlockState.getBlockState(world, pos, flag);
        blockState.setData(block);

        BlockFormEvent event = (entity == null) ? new BlockFormEvent(blockState.getBlock(), blockState) : new EntityBlockFormEvent(entity.getBukkitEntity(), blockState.getBlock(), blockState);
        world.getCBServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            blockState.update(true);
        }

        return !event.isCancelled();
    }

    public static boolean handleBatToggleSleepEvent(Entity bat, boolean awake) {
        BatToggleSleepEvent event = new BatToggleSleepEvent((Bat) bat.getBukkitEntity(), awake);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static boolean handlePlayerRecipeListUpdateEvent(PlayerEntity who, ResourceLocation recipe) {
        PlayerRecipeDiscoverEvent event = new PlayerRecipeDiscoverEvent((Player) who.getBukkitEntity(), CraftNamespacedKey.fromMinecraft(recipe));
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static EntityPickupItemEvent callEntityPickupItemEvent(Entity who, ItemEntity item, int remaining, boolean cancelled) {
        EntityPickupItemEvent event = new EntityPickupItemEvent((org.bukkit.entity.LivingEntity) who.getBukkitEntity(), (Item) item.getBukkitEntity(), remaining);
        event.setCancelled(cancelled);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * Raid events
     */
    public static boolean callRaidTriggerEvent(Raid raid, ServerPlayerEntity player) {
        RaidTriggerEvent event = new RaidTriggerEvent(new CraftRaid(raid), raid.getLevel().getWorld(), player.getBukkitEntity());
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static void callRaidFinishEvent(Raid raid, List<Player> players) {
        RaidFinishEvent event = new RaidFinishEvent(new CraftRaid(raid), raid.getLevel().getWorld(), players);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void callRaidStopEvent(Raid raid, RaidStopEvent.Reason reason) {
        RaidStopEvent event = new RaidStopEvent(new CraftRaid(raid), raid.getLevel().getWorld(), reason);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void callRaidSpawnWaveEvent(Raid raid, AbstractRaiderEntity leader, List<AbstractRaiderEntity> raiders) {
        Raider craftLeader = (CraftRaider) leader.getBukkitEntity();
        List<Raider> craftRaiders = new ArrayList<>();
        for (AbstractRaiderEntity entityRaider : raiders) {
            craftRaiders.add((Raider) entityRaider.getBukkitEntity());
        }
        RaidSpawnWaveEvent event = new RaidSpawnWaveEvent(new CraftRaid(raid), raid.getLevel().getWorld(), craftLeader, craftRaiders);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static LootGenerateEvent callLootGenerateEvent(IInventory inventory, LootTable lootTable, LootContext lootInfo, List<ItemStack> loot, boolean plugin) {
        CraftWorld world = lootInfo.getLevel().getWorld();
        Entity entity = lootInfo.getParamOrNull(LootParameters.THIS_ENTITY);
        NamespacedKey key = CraftNamespacedKey.fromMinecraft(world.getHandle().getServer().getLootTables().getKey(lootTable)); // LoliServer
        CraftLootTable craftLootTable = new CraftLootTable(key, lootTable);
        List<org.bukkit.inventory.ItemStack> bukkitLoot = loot.stream().map(CraftItemStack::asCraftMirror).collect(Collectors.toCollection(ArrayList::new));

        LootGenerateEvent event = new LootGenerateEvent(world, (entity != null ? entity.getBukkitEntity() : null), CatInventoryUtils.getOwner(inventory), craftLootTable, CraftLootTable.convertContext(lootInfo), bukkitLoot, plugin);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static void callStriderTemperatureChangeEvent(StriderEntity strider, boolean shivering) {
        StriderTemperatureChangeEvent event = new StriderTemperatureChangeEvent((Strider) strider.getBukkitEntity(), shivering);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static boolean handleEntitySpellCastEvent(SpellcastingIllagerEntity caster, SpellcastingIllagerEntity.SpellType spell) {
        EntitySpellCastEvent event = new EntitySpellCastEvent((Spellcaster) caster.getBukkitEntity(), CraftSpellcaster.toBukkitSpell(spell));
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    /**
     * ArrowBodyCountChangeEvent
     */
    public static ArrowBodyCountChangeEvent callArrowBodyCountChangeEvent(LivingEntity entity, int oldAmount, int newAmount, boolean isReset) {
        org.bukkit.entity.LivingEntity bukkitEntity = (org.bukkit.entity.LivingEntity) entity.getBukkitEntity();

        ArrowBodyCountChangeEvent event = new ArrowBodyCountChangeEvent(bukkitEntity, oldAmount, newAmount, isReset);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static EntityExhaustionEvent callPlayerExhaustionEvent(PlayerEntity humanEntity, EntityExhaustionEvent.ExhaustionReason exhaustionReason, float exhaustion) {
        EntityExhaustionEvent event = new EntityExhaustionEvent(humanEntity.getBukkitEntity(), exhaustionReason, exhaustion);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }

    public static PiglinBarterEvent callPiglinBarterEvent(PiglinEntity piglin, List<ItemStack> outcome, ItemStack input) {
        PiglinBarterEvent event = new PiglinBarterEvent((Piglin) piglin.getBukkitEntity(), CraftItemStack.asBukkitCopy(input), outcome.stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList()));
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
