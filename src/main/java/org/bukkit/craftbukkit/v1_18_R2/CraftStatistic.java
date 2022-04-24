package org.bukkit.craftbukkit.v1_18_R2;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;

public enum CraftStatistic {
    DAMAGE_DEALT(Stats.DAMAGE_DEALT),
    DAMAGE_TAKEN(Stats.DAMAGE_TAKEN),
    DEATHS(Stats.DEATHS),
    MOB_KILLS(Stats.MOB_KILLS),
    PLAYER_KILLS(Stats.PLAYER_KILLS),
    FISH_CAUGHT(Stats.FISH_CAUGHT),
    ANIMALS_BRED(Stats.ANIMALS_BRED),
    LEAVE_GAME(Stats.LEAVE_GAME),
    JUMP(Stats.JUMP),
    DROP_COUNT(Stats.DROP),
    DROP(new ResourceLocation("dropped")),
    PICKUP(new ResourceLocation("picked_up")),
    PLAY_ONE_MINUTE(Stats.PLAY_TIME),
    TOTAL_WORLD_TIME(Stats.TOTAL_WORLD_TIME),
    WALK_ONE_CM(Stats.WALK_ONE_CM),
    WALK_ON_WATER_ONE_CM(Stats.WALK_ON_WATER_ONE_CM),
    FALL_ONE_CM(Stats.FALL_ONE_CM),
    SNEAK_TIME(Stats.CROUCH_TIME),
    CLIMB_ONE_CM(Stats.CLIMB_ONE_CM),
    FLY_ONE_CM(Stats.FLY_ONE_CM),
    WALK_UNDER_WATER_ONE_CM(Stats.WALK_UNDER_WATER_ONE_CM),
    MINECART_ONE_CM(Stats.MINECART_ONE_CM),
    BOAT_ONE_CM(Stats.BOAT_ONE_CM),
    PIG_ONE_CM(Stats.PIG_ONE_CM),
    HORSE_ONE_CM(Stats.HORSE_ONE_CM),
    SPRINT_ONE_CM(Stats.SPRINT_ONE_CM),
    CROUCH_ONE_CM(Stats.CROUCH_ONE_CM),
    AVIATE_ONE_CM(Stats.AVIATE_ONE_CM),
    MINE_BLOCK(new ResourceLocation("mined")),
    USE_ITEM(new ResourceLocation("used")),
    BREAK_ITEM(new ResourceLocation("broken")),
    CRAFT_ITEM(new ResourceLocation("crafted")),
    KILL_ENTITY(new ResourceLocation("killed")),
    ENTITY_KILLED_BY(new ResourceLocation("killed_by")),
    TIME_SINCE_DEATH(Stats.TIME_SINCE_DEATH),
    TALKED_TO_VILLAGER(Stats.TALKED_TO_VILLAGER),
    TRADED_WITH_VILLAGER(Stats.TRADED_WITH_VILLAGER),
    CAKE_SLICES_EATEN(Stats.EAT_CAKE_SLICE),
    CAULDRON_FILLED(Stats.FILL_CAULDRON),
    CAULDRON_USED(Stats.USE_CAULDRON),
    ARMOR_CLEANED(Stats.CLEAN_ARMOR),
    BANNER_CLEANED(Stats.CLEAN_BANNER),
    BREWINGSTAND_INTERACTION(Stats.INTERACT_WITH_BREWINGSTAND),
    BEACON_INTERACTION(Stats.INTERACT_WITH_BEACON),
    DROPPER_INSPECTED(Stats.INSPECT_DROPPER),
    HOPPER_INSPECTED(Stats.INSPECT_HOPPER),
    DISPENSER_INSPECTED(Stats.INSPECT_DISPENSER),
    NOTEBLOCK_PLAYED(Stats.PLAY_NOTEBLOCK),
    NOTEBLOCK_TUNED(Stats.TUNE_NOTEBLOCK),
    FLOWER_POTTED(Stats.POT_FLOWER),
    TRAPPED_CHEST_TRIGGERED(Stats.TRIGGER_TRAPPED_CHEST),
    ENDERCHEST_OPENED(Stats.OPEN_ENDERCHEST),
    ITEM_ENCHANTED(Stats.ENCHANT_ITEM),
    RECORD_PLAYED(Stats.PLAY_RECORD),
    FURNACE_INTERACTION(Stats.INTERACT_WITH_FURNACE),
    CRAFTING_TABLE_INTERACTION(Stats.INTERACT_WITH_CRAFTING_TABLE),
    CHEST_OPENED(Stats.OPEN_CHEST),
    SLEEP_IN_BED(Stats.SLEEP_IN_BED),
    SHULKER_BOX_OPENED(Stats.OPEN_SHULKER_BOX),
    TIME_SINCE_REST(Stats.TIME_SINCE_REST),
    SWIM_ONE_CM(Stats.SWIM_ONE_CM),
    DAMAGE_DEALT_ABSORBED(Stats.DAMAGE_DEALT_ABSORBED),
    DAMAGE_DEALT_RESISTED(Stats.DAMAGE_DEALT_RESISTED),
    DAMAGE_BLOCKED_BY_SHIELD(Stats.DAMAGE_BLOCKED_BY_SHIELD),
    DAMAGE_ABSORBED(Stats.DAMAGE_ABSORBED),
    DAMAGE_RESISTED(Stats.DAMAGE_RESISTED),
    CLEAN_SHULKER_BOX(Stats.CLEAN_SHULKER_BOX),
    OPEN_BARREL(Stats.OPEN_BARREL),
    INTERACT_WITH_BLAST_FURNACE(Stats.INTERACT_WITH_BLAST_FURNACE),
    INTERACT_WITH_SMOKER(Stats.INTERACT_WITH_SMOKER),
    INTERACT_WITH_LECTERN(Stats.INTERACT_WITH_LECTERN),
    INTERACT_WITH_CAMPFIRE(Stats.INTERACT_WITH_CAMPFIRE),
    INTERACT_WITH_CARTOGRAPHY_TABLE(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE),
    INTERACT_WITH_LOOM(Stats.INTERACT_WITH_LOOM),
    INTERACT_WITH_STONECUTTER(Stats.INTERACT_WITH_STONECUTTER),
    BELL_RING(Stats.BELL_RING),
    RAID_TRIGGER(Stats.RAID_TRIGGER),
    RAID_WIN(Stats.RAID_WIN),
    INTERACT_WITH_ANVIL(Stats.INTERACT_WITH_ANVIL),
    INTERACT_WITH_GRINDSTONE(Stats.INTERACT_WITH_GRINDSTONE),
    TARGET_HIT(Stats.TARGET_HIT),
    INTERACT_WITH_SMITHING_TABLE(Stats.INTERACT_WITH_SMITHING_TABLE),
    STRIDER_ONE_CM(Stats.STRIDER_ONE_CM);
    private final ResourceLocation minecraftKey;
    private final org.bukkit.Statistic bukkit;
    private static final BiMap<ResourceLocation, org.bukkit.Statistic> statistics;

    static {
        ImmutableBiMap.Builder<ResourceLocation, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.builder();
        for (CraftStatistic statistic : CraftStatistic.values()) {
            statisticBuilder.put(statistic.minecraftKey, statistic.bukkit);
        }

        statistics = statisticBuilder.build();
    }

    private CraftStatistic(ResourceLocation minecraftKey) {
        this.minecraftKey = minecraftKey;

        this.bukkit = org.bukkit.Statistic.valueOf(this.name());
        Preconditions.checkState(bukkit != null, "Bukkit statistic %s does not exist", this.name());
    }

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.stats.Stat<?> statistic) {
        net.minecraft.core.Registry statRegistry = statistic.getType().getRegistry();
        ResourceLocation nmsKey = net.minecraft.core.Registry.STAT_TYPE.getKey(statistic.getType());

        if (statRegistry == net.minecraft.core.Registry.CUSTOM_STAT) {
            nmsKey = (ResourceLocation) statistic.getValue();
        }

        return statistics.get(nmsKey);
    }

    public static net.minecraft.stats.Stat getNMSStatistic(org.bukkit.Statistic bukkit) {
        Preconditions.checkArgument(bukkit.getType() == Statistic.Type.UNTYPED, "This method only accepts untyped statistics");

        net.minecraft.stats.Stat<ResourceLocation> nms = Stats.CUSTOM.get(statistics.inverse().get(bukkit));
        Preconditions.checkArgument(nms != null, "NMS Statistic %s does not exist", bukkit);

        return nms;
    }

    public static net.minecraft.stats.Stat getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return Stats.BLOCK_MINED.get(CraftMagicNumbers.getBlock(material));
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return Stats.ITEM_CRAFTED.get(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.USE_ITEM) {
                return Stats.ITEM_USED.get(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.BREAK_ITEM) {
                return Stats.ITEM_BROKEN.get(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.PICKUP) {
                return Stats.ITEM_PICKED_UP.get(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.DROP) {
                return Stats.ITEM_DROPPED.get(CraftMagicNumbers.getItem(material));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.stats.Stat getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        if (entity.getName() != null) {
            net.minecraft.world.entity.EntityType<?> nmsEntity = net.minecraft.core.Registry.ENTITY_TYPE.get(new ResourceLocation(entity.getName()));

            if (stat == org.bukkit.Statistic.KILL_ENTITY) {
                return net.minecraft.stats.Stats.ENTITY_KILLED.get(nmsEntity);
            }
            if (stat == org.bukkit.Statistic.ENTITY_KILLED_BY) {
                return net.minecraft.stats.Stats.ENTITY_KILLED_BY.get(nmsEntity);
            }
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.stats.Stat<net.minecraft.world.entity.EntityType<?>> statistic) {
        ResourceLocation name = net.minecraft.world.entity.EntityType.getKey(statistic.getValue());
        return EntityType.fromName(name.getPath());
    }

    public static Material getMaterialFromStatistic(net.minecraft.stats.Stat<?> statistic) {
        if (statistic.getValue() instanceof Item) {
            return CraftMagicNumbers.getMaterial((Item) statistic.getValue());
        }
        if (statistic.getValue() instanceof Block) {
            return CraftMagicNumbers.getMaterial((Block) statistic.getValue());
        }
        return null;
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic) {
        incrementStatistic(manager, statistic, 1);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic) {
        decrementStatistic(manager, statistic, 1);
    }

    public static int getStatistic(ServerStatsCounter manager, Statistic statistic) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Type.UNTYPED, "Must supply additional paramater for this statistic");
        return manager.getValue(CraftStatistic.getNMSStatistic(statistic));
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, getStatistic(manager, statistic) + amount);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, getStatistic(manager, statistic) - amount);
    }

    public static void setStatistic(ServerStatsCounter manager, Statistic statistic, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Type.UNTYPED, "Must supply additional paramater for this statistic");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getNMSStatistic(statistic);
        manager.setValue(null, nmsStatistic, newValue);;
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, Material material) {
        incrementStatistic(manager, statistic, material, 1);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, Material material) {
        decrementStatistic(manager, statistic, material, 1);
    }

    public static int getStatistic(ServerStatsCounter manager, Statistic statistic, Material material) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        return manager.getValue(nmsStatistic);
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, material, getStatistic(manager, statistic, material) + amount);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, material, getStatistic(manager, statistic, material) - amount);
    }

    public static void setStatistic(ServerStatsCounter manager, Statistic statistic, Material material, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        manager.setValue(null, nmsStatistic, newValue);
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType) {
        incrementStatistic(manager, statistic, entityType, 1);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType) {
        decrementStatistic(manager, statistic, entityType, 1);
    }

    public static int getStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        return manager.getValue(nmsStatistic);
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, entityType, getStatistic(manager, statistic, entityType) + amount);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, entityType, getStatistic(manager, statistic, entityType) - amount);
    }

    public static void setStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        manager.setValue(null, nmsStatistic, newValue);
    }
}
