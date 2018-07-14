// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import org.bukkit.Achievement;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.Statistic;
import net.minecraft.nbt.NBTException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.nbt.JsonToNBT;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import java.util.Iterator;
import java.util.ArrayList;
import org.bukkit.util.StringUtil;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.UnsafeValues;

public final class CraftMagicNumbers implements UnsafeValues
{
    public static final UnsafeValues INSTANCE;
    
    static {
        INSTANCE = new CraftMagicNumbers();
    }
    
    public static net.minecraft.block.Block getBlock(final Block block) {
        return getBlock(block.getType());
    }
    
    @Deprecated
    public static net.minecraft.block.Block getBlock(final int id) {
        return getBlock(Material.getMaterial(id));
    }
    
    @Deprecated
    public static int getId(final net.minecraft.block.Block block) {
        return net.minecraft.block.Block.getIdFromBlock(block);
    }
    
    public static Material getMaterial(final net.minecraft.block.Block block) {
        return Material.getMaterial(net.minecraft.block.Block.getIdFromBlock(block));
    }
    
    public static Item getItem(final Material material) {
        final Item item = Item.getItemById(material.getId());
        return item;
    }
    
    @Deprecated
    public static Item getItem(final int id) {
        return Item.getItemById(id);
    }
    
    @Deprecated
    public static int getId(final Item item) {
        return Item.getIdFromItem(item);
    }
    
    public static Material getMaterial(final Item item) {
        final Material material = Material.getMaterial(Item.getIdFromItem(item));
        if (material == null) {
            return Material.AIR;
        }
        return material;
    }
    
    public static net.minecraft.block.Block getBlock(final Material material) {
        if (material == null) {
            return null;
        }
        final net.minecraft.block.Block block = net.minecraft.block.Block.getBlockById(material.getId());
        if (block == null) {
            return Blocks.AIR;
        }
        return block;
    }
    
    @Override
    public Material getMaterialFromInternalName(final String name) {
        return getMaterial(Item.REGISTRY.getObject(new ResourceLocation(name)));
    }
    
    @Override
    public List<String> tabCompleteInternalMaterialName(final String token, final List<String> completions) {
        final ArrayList<String> results = /*(ArrayList<String>)*/Lists.newArrayList();
        for (final ResourceLocation key : Item.REGISTRY.getKeys()) {
            results.add(key.toString());
        }
        return StringUtil.copyPartialMatches(token, results, completions);
    }
    
    @Override
    public ItemStack modifyItemStack(final ItemStack stack, final String arguments) {
        final net.minecraft.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        try {
            nmsStack.setTagCompound(JsonToNBT.getTagFromJson(arguments));
        }
        catch (NBTException ex) {
            Logger.getLogger(CraftMagicNumbers.class.getName()).log(Level.SEVERE, null, ex);
        }
        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
        return stack;
    }
    
    @Override
    public Statistic getStatisticFromInternalName(final String name) {
        return CraftStatistic.getBukkitStatisticByName(name);
    }
    
    @Override
    public Achievement getAchievementFromInternalName(final String name) {
        return CraftStatistic.getBukkitAchievementByName(name);
    }
    
    @Override
    public List<String> tabCompleteInternalStatisticOrAchievementName(final String token, final List<String> completions) {
        final List<String> matches = new ArrayList<String>();
        final Iterator<StatBase> iterator = StatList.ALL_STATS.iterator();
        while (iterator.hasNext()) {
            final String statistic = iterator.next().statId;
            if (statistic.startsWith(token)) {
                matches.add(statistic);
            }
        }
        return matches;
    }
}
