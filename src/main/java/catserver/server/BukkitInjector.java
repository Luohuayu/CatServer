package catserver.server;

import catserver.server.entity.CraftCustomEntity;
import catserver.server.utils.EnumHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_18_R2.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_18_R2.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class BukkitInjector {

    public static boolean initializedBukkit = false;
    public static Map<net.minecraft.world.entity.EntityType<?>, String> entityTypeMap = new HashMap<>();

    public static void injectEnchantments() {
        for (Map.Entry<ResourceKey<Enchantment>, Enchantment> entry : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(entry.getValue()));
            CatServer.LOGGER.info(String.format("Registered forge Enchantment: %s", entry.getValue().getRegistryName().toString()));
        }
    }

    public static void injectBlockMaterials() {
        for (Map.Entry<ResourceKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String materialName = normalizeName(entry.getKey().toString()).replace("RESOURCEKEYMINECRAFT_BLOCK__", "");
                NamespacedKey forgeKey = CraftNamespacedKey.fromMinecraft(resourceLocation);
                Block block = entry.getValue();
                int itemId = Item.getId(block.asItem());
                Material material = Material.addMaterial(materialName, itemId, true, resourceLocation.getNamespace(), forgeKey);
                CraftMagicNumbers.BLOCK_MATERIAL.put(block, material);
                CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
                if (material != null) {
                    CatServer.LOGGER.info(String.format("Registered forge Block Material: %s", materialName));
                }
            }
        }
    }

    public static void injectItemMaterials() {
        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String materialName = normalizeName(entry.getKey().toString()).replace("RESOURCEKEYMINECRAFT_ITEM__", "");
                NamespacedKey forgeKey = CraftNamespacedKey.fromMinecraft(resourceLocation);
                Item item = entry.getValue();
                int itemId = Item.getId(item);
                Material material = Material.addMaterial(materialName, itemId, false, resourceLocation.getNamespace(), forgeKey);
                CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
                if (material != null) {
                    CatServer.LOGGER.info(String.format("Registered forge Item Material: %s", materialName));
                }
            }
        }
    }

    public static void injectMobEffects() {
        for (Map.Entry<ResourceKey<MobEffect>, MobEffect> entry : ForgeRegistries.MOB_EFFECTS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            if (PotionEffectType.getByName(pet.getName()) == null) {
                PotionEffectType.registerPotionEffectType(pet);
            }
            CatServer.LOGGER.info(String.format("Registered forge MobEffect: %s, Effect ID: %s", entry.getValue().getRegistryName().toString(), pet.getId()));
        }
    }

    public static void injectBiomes() {
        for (Map.Entry<ResourceKey<Biome>, Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            if (!entry.getValue().getRegistryName().getNamespace().equals(NamespacedKey.MINECRAFT)) {
                org.bukkit.block.Biome biome = EnumHelper.addEnum(org.bukkit.block.Biome.class, entry.getValue().getRegistryName().getNamespace(), new Class[0]);
                CatServer.LOGGER.info(String.format("Registered forge Biome: %s", biome.name()));
            }
        }
    }

    public static void injectBannerPatterns() {
        Map<String, PatternType> PATTERN_MAP = ObfuscationReflectionHelper.getPrivateValue(PatternType.class, null, "byString");
        for (BannerPattern bannerPattern : BannerPattern.values()) {
            if (PatternType.getByIdentifier(bannerPattern.getHashname()) == null) {
                PatternType patternType = EnumHelper.addEnum(PatternType.class, bannerPattern.name(), new Class[]{String.class}, bannerPattern.getHashname());
                if (patternType != null) {
                    PATTERN_MAP.put(bannerPattern.getHashname(), patternType);
                    CatServer.LOGGER.info(String.format("Registered forge BannerPattern: %s", bannerPattern.name()));
                }
            }
        }
    }

    public static void injectEntityTypes() {
        Map<String, EntityType> NAME_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Map.Entry<ResourceKey<net.minecraft.world.entity.EntityType<?>>, net.minecraft.world.entity.EntityType<?>> entry : ForgeRegistries.ENTITIES.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String entityType = normalizeName(resourceLocation.toString());
                int typeId = entityType.hashCode();
                EntityType bukkitType = EnumHelper.addEnum(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, entityType.toLowerCase(), CraftCustomEntity.class, typeId, false);
                NAME_MAP.put(entityType.toLowerCase(), bukkitType);
                ID_MAP.put((short) typeId, bukkitType);
                BukkitInjector.entityTypeMap.put(entry.getValue(), entityType);
                CatServer.LOGGER.info(String.format("Registered forge EntityType: %s", entry.getValue().getRegistryName().toString()));
            }
        }
    }

    public static String normalizeName(String name) {
        return name.toUpperCase(java.util.Locale.ENGLISH).replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
    }
}
