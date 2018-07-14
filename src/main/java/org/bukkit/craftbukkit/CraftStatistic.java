// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityList;
import org.bukkit.entity.EntityType;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.Material;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatBase;
import java.util.Map;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Achievement;
import org.bukkit.Statistic;
import com.google.common.collect.BiMap;

public class CraftStatistic
{
    private static final BiMap<String, Statistic> statistics;
    private static final BiMap<String, Achievement> achievements;
    
    static {
        final ImmutableMap<Object, Object> specialCases = /*(ImmutableMap<String, Achievement>)*/ImmutableMap.builder().put("achievement.buildWorkBench", Achievement.BUILD_WORKBENCH).put("achievement.diamonds", Achievement.GET_DIAMONDS).put("achievement.portal", Achievement.NETHER_PORTAL).put("achievement.ghast", Achievement.GHAST_RETURN).put("achievement.theEnd", Achievement.END_PORTAL).put("achievement.theEnd2", Achievement.THE_END).put("achievement.blazeRod", Achievement.GET_BLAZE_ROD).put("achievement.potion", Achievement.BREW_POTION).build();
        final ImmutableBiMap.Builder<String, Statistic> statisticBuilder = /*(ImmutableBiMap.Builder<String, Statistic>)*/ImmutableBiMap.builder();
        final ImmutableBiMap.Builder<String, Achievement> achievementBuilder = /*(ImmutableBiMap.Builder<String, Achievement>)*/ImmutableBiMap.builder();
        Statistic[] values;
        for (int length = (values = Statistic.values()).length, i = 0; i < length; ++i) {
            final Statistic statistic = values[i];
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            }
            else {
                statisticBuilder.put(("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name())), statistic);
            }
        }
        Achievement[] values2;
        for (int length2 = (values2 = Achievement.values()).length, j = 0; j < length2; ++j) {
            final Achievement achievement = values2[j];
            if (!specialCases.values().contains((Object)achievement)) {
                achievementBuilder.put(("achievement." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, achievement.name())), achievement);
            }
        }
        achievementBuilder.putAll((Map)specialCases);
        statistics = (BiMap)statisticBuilder.build();
        achievements = (BiMap)achievementBuilder.build();
    }
    
    public static Achievement getBukkitAchievement(final net.minecraft.stats.Achievement achievement) {
        return getBukkitAchievementByName(achievement.statId);
    }
    
    public static Achievement getBukkitAchievementByName(final String name) {
        return (Achievement)CraftStatistic.achievements.get((Object)name);
    }
    
    public static Statistic getBukkitStatistic(final StatBase statistic) {
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
        return (Statistic)CraftStatistic.statistics.get((Object)name);
    }
    
    public static StatBase getNMSStatistic(final Statistic statistic) {
        return StatList.getOneShotStat((String)CraftStatistic.statistics.inverse().get((Object)statistic));
    }
    
    public static net.minecraft.stats.Achievement getNMSAchievement(final Achievement achievement) {
        return (net.minecraft.stats.Achievement)StatList.getOneShotStat((String)CraftStatistic.achievements.inverse().get((Object)achievement));
    }
    
    public static StatBase getMaterialStatistic(final Statistic stat, final Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatList.getBlockStats(CraftMagicNumbers.getBlock(material));
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatList.getCraftStats(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.USE_ITEM) {
                return StatList.getObjectUseStats(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatList.getObjectBreakStats(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.DROP) {
                return StatList.getDroppedObjectStats(CraftMagicNumbers.getItem(material));
            }
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
        return null;
    }
    
    public static StatBase getEntityStatistic(final Statistic stat, final EntityType entity) {
        final EntityList.EntityEggInfo monsteregginfo = EntityList.ENTITY_EGGS.get(entity.getName());
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
    
    public static EntityType getEntityTypeFromStatistic(final StatBase statistic) {
        final String statisticString = statistic.statId;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }
    
    public static Material getMaterialFromStatistic(final StatBase statistic) {
        final String statisticString = statistic.statId;
        final String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        final Item item = Item.REGISTRY.getObject(new ResourceLocation(val));
        if (item != null) {
            return Material.getMaterial(Item.getIdFromItem(item));
        }
        final Block block = Block.REGISTRY.getObject(new ResourceLocation(val));
        if (block != null) {
            return Material.getMaterial(Block.getIdFromBlock(block));
        }
        try {
            return Material.getMaterial(Integer.parseInt(val));
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
}
