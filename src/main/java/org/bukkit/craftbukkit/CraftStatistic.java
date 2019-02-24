package org.bukkit.craftbukkit;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftStatistic {
    private static final BiMap<String, Statistic> statistics;

    static {
        ImmutableBiMap.Builder<String, Statistic> statisticBuilder = ImmutableBiMap.<String, Statistic>builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name()), statistic);
            }
        }

        statistics = statisticBuilder.build();
    }

    private CraftStatistic() {}

    public static Statistic getBukkitStatistic(StatBase statistic) {
        return getBukkitStatisticByName(statistic.statId);
    }

    public static Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity.")) {
            name = "stat.killEntity";
        }
        if (name.startsWith("stat.entityKilledBy.")) {
            name = "stat.entityKilledBy";
        }
        if (name.startsWith("stat.breakItem.")) {
            name = "stat.breakItem";
        }
        if (name.startsWith("stat.useItem.")) {
            name = "stat.useItem";
        }
        if (name.startsWith("stat.mineBlock.")) {
            name = "stat.mineBlock";
        }
        if (name.startsWith("stat.craftItem.")) {
            name = "stat.craftItem";
        }
        if (name.startsWith("stat.drop.")) {
            name = "stat.drop";
        }
        if (name.startsWith("stat.pickup.")) {
            name = "stat.pickup";
        }
        return statistics.get(name);
    }

    public static StatBase getNMSStatistic(Statistic statistic) {
        return StatList.getOneShotStat(statistics.inverse().get(statistic));
    }

    public static StatBase getMaterialStatistic(Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatList.getBlockStats(CraftMagicNumbers.getBlock(material)); // PAIL: getMineBlockStatistic
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatList.getCraftStats(CraftMagicNumbers.getItem(material)); // PAIL: getCraftItemStatistic
            }
            if (stat == Statistic.USE_ITEM) {
                return StatList.getObjectUseStats(CraftMagicNumbers.getItem(material)); // PAIL: getUseItemStatistic
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatList.getObjectBreakStats(CraftMagicNumbers.getItem(material)); // PAIL: getBreakItemStatistic
            }
            if (stat == Statistic.PICKUP) {
                return StatList.getObjectsPickedUpStats(CraftMagicNumbers.getItem(material)); // PAIL: getPickupStatistic
            }
            if (stat == Statistic.DROP) {
                return StatList.getDroppedObjectStats(CraftMagicNumbers.getItem(material)); // PAIL: getDropItemStatistic
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static StatBase getEntityStatistic(Statistic stat, EntityType entity) {
        EntityList.EntityEggInfo monsteregginfo = EntityList.ENTITY_EGGS.get(new ResourceLocation(entity.getName()));

        if (monsteregginfo != null) {
            if (stat == Statistic.KILL_ENTITY) {
                return monsteregginfo.killEntityStat;
            }
            if (stat == Statistic.ENTITY_KILLED_BY) {
                return monsteregginfo.entityKilledByStat;
            }
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(StatBase statistic) {
        String statisticString = statistic.statId;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(StatBase statistic) {
        String statisticString = statistic.statId;
        String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        Item item = Item.REGISTRY.getObject(new ResourceLocation(val));
        if (item != null) {
            return Material.getMaterial(Item.getIdFromItem(item));
        }
        Block block = Block.REGISTRY.getObject(new ResourceLocation(val));
        if (block != null) {
            return Material.getBlockMaterial(Block.getIdFromBlock(block));
        }
        try {
            return Material.getMaterial(Integer.parseInt(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
