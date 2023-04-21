package catserver.server;

import catserver.server.bukkit.CraftCustomEnchantment;
import catserver.server.bukkit.CraftCustomPotionEffect;
import catserver.server.entity.CraftCustomEntity;
import catserver.server.utils.EnumHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import io.izzel.arclight.api.Unsafe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R2.CraftStatistic;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R2.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftSpawnCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Modifier;
import java.util.*;

public class BukkitInjector {

    public static BiMap<ResourceKey<LevelStem>, World.Environment> environments = HashBiMap.create(ImmutableMap.<ResourceKey<LevelStem>, World.Environment>builder().put(LevelStem.OVERWORLD, World.Environment.NORMAL).put(LevelStem.NETHER, World.Environment.NETHER).put(LevelStem.END, World.Environment.THE_END).build());

    public static void registerAll() {
        registerMaterials();
        registerEnchantments();
        registerPotionEffects();
        registerBiomes();
        registerEntities();
        registerVillagerProfessions();
        registerStatistics();
        registerSpawnCategory();
        try {
            for (var field : org.bukkit.Registry.class.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && field.get(null) instanceof org.bukkit.Registry.SimpleRegistry<?> registry) {
                    registry.reload();
                }
            }
        } catch (Throwable ignored) {
        }
    }

    private static void registerSpawnCategory() {
        int i = SpawnCategory.values().length;
        List<SpawnCategory> categories = Lists.newArrayList();
        for (var category : MobCategory.values()) {
            try {
                CraftSpawnCategory.toBukkit(category);
            } catch (Exception e) {
                String name = category.name();
                SpawnCategory spawnCategory = EnumHelper.makeEnum(SpawnCategory.class, name, i++, ImmutableList.of(), ImmutableList.of());
                categories.add(spawnCategory);
                CatServer.LOGGER.debug("Save-SpawnCategory: {}", name);
            }
        }
        EnumHelper.addEnums(SpawnCategory.class, categories);
        CatServer.LOGGER.info("Registered {} spawn categories into Bukkit", categories.size());
    }

    public static void registerEnvironments(Registry<LevelStem> registry) {
        int i = World.Environment.values().length;
        List<World.Environment> worldTypes = Lists.newArrayList();
        for (var entry : registry.entrySet()) {
            ResourceKey<LevelStem> resourceKey = entry.getKey();
            // Skip minecraft
            if (Objects.equals(resourceKey.location().getNamespace(), NamespacedKey.MINECRAFT)) {
                continue;
            }
            World.Environment environment = environments.get(resourceKey);
            if (environment == null) {
                String name = standardize(resourceKey.location());
                environment = EnumHelper.makeEnum(World.Environment.class, name, i++, ImmutableList.of(Integer.TYPE), List.of(i - 1));
                worldTypes.add(environment);
                environments.put(resourceKey, environment);
                CatServer.LOGGER.debug("Registered new Forge DimensionType {}", environment);
            }
        }
        EnumHelper.addEnums(World.Environment.class, worldTypes);
    }

    private static void registerStatistics() {
        BiMap<ResourceLocation, Statistic> STATS = HashBiMap.create(CraftStatistic.statistics);

        int i = Statistic.values().length;
        List<Statistic> statistics = Lists.newArrayList();
        for (var stat : ForgeRegistries.STAT_TYPES) {
            // Skip minecraft
            if (stat.getRegistryName() == null || Objects.equals(NamespacedKey.MINECRAFT, stat.getRegistryName().getNamespace())) {
                continue;
            }
            if (stat == Stats.CUSTOM) continue;
            Statistic statistic = STATS.get(stat.getRegistryName());
            if (statistic != null) {
                String statName = standardize(stat.getRegistryName());
                Statistic.Type type;
                if (stat.getRegistry() == Registry.ENTITY_TYPE) {
                    type = Statistic.Type.ENTITY;
                } else if (stat.getRegistry() == Registry.BLOCK) {
                    type = Statistic.Type.BLOCK;
                } else if (stat.getRegistry() == Registry.ITEM) {
                    type = Statistic.Type.ITEM;
                } else {
                    type = Statistic.Type.UNTYPED;
                }
                statistic = EnumHelper.makeEnum(Statistic.class, statName, i++, ImmutableList.of(Statistic.Type.class), ImmutableList.of(type));
                statistics.add(statistic);
                STATS.put(stat.getRegistryName(), statistic);
                CatServer.LOGGER.debug("Save-Stats: {}", statistic.name());
            }
        }
        // Custom Stats
        for (var location : Registry.CUSTOM_STAT) {
            // Skip minecraft
            if (Objects.equals(NamespacedKey.MINECRAFT, location.getNamespace())) {
                continue;
            }
            Statistic statistic = STATS.get(location);
            if (statistic == null) {
                String statName = standardize(location);
                statistic = EnumHelper.makeEnum(Statistic.class, statName, i++, ImmutableList.of(), ImmutableList.of());
                statistics.add(statistic);
                STATS.put(location, statistic);
                CatServer.LOGGER.debug("Save-CustomStats: {}", statistic.name());
            }
        }
        EnumHelper.addEnums(Statistic.class, statistics);
        CraftStatistic.statistics = STATS;
        CatServer.LOGGER.info("Registered {} statistic and custom statistic into Bukkit", statistics.size());
    }

    private static void registerVillagerProfessions() {
        int i = Villager.Profession.values().length;
        List<Villager.Profession> professions = Lists.newArrayList();
        for (var forgeProfessions: ForgeRegistries.PROFESSIONS.getEntries()) {
            ResourceLocation location = forgeProfessions.getKey().location();
            // Skip minecraft
            if (Objects.equals(location.getNamespace(), NamespacedKey.MINECRAFT)) {
                continue;
            }
            var newPfName = standardize(location);
            var bukkitProfessions = EnumHelper.makeEnum(Villager.Profession.class, newPfName, i++, ImmutableList.of(), ImmutableList.of());
            professions.add(bukkitProfessions);
            CatServer.LOGGER.debug("Save-VillagerProfessions: {}", bukkitProfessions.name());
        }
        EnumHelper.addEnums(Villager.Profession.class, professions);
        CatServer.LOGGER.info("Registered {} villager professions into Bukkit", professions.size());
    }

    private static void registerEntities() {
        Map<String, EntityType> NAME_MAP = Unsafe.getStatic(EntityType.class, "NAME_MAP");
        Map<Short, EntityType> ID_MAP = Unsafe.getStatic(EntityType.class, "ID_MAP");

        int i = EntityType.values().length;
        List<EntityType> entityTypes = Lists.newArrayList();
        for (var entry : ForgeRegistries.ENTITIES.getEntries()) {
            ResourceLocation location = entry.getValue().getRegistryName();
            // Skip minecraft
            if (location == null || Objects.equals(location.getNamespace(), NamespacedKey.MINECRAFT)) {
                continue;
            }
            String entityName = standardize(location);
            int typeId = entityName.hashCode();
            EntityType entityType = EnumHelper.makeEnum(EntityType.class, entityName, i++, List.of(String.class, Class.class, Integer.TYPE, Boolean.TYPE), List.of(entityName.toLowerCase(), CraftCustomEntity.class, typeId, false));
            NAME_MAP.put(entityName.toLowerCase(), entityType);
            ID_MAP.put((short) typeId, entityType);
            entityTypes.add(entityType);
            CatServer.LOGGER.debug("Save-EntityType: {} - {}", location, entityType);
        }
        EnumHelper.addEnums(EntityType.class, entityTypes);
        CatServer.LOGGER.info("Registered {} entityTypes into Bukkit", entityTypes.size());
    }

    private static void registerBiomes() {
        int i = Biome.values().length;
        List<Biome> enumBiomes = Lists.newArrayList();
        for (var biome : ForgeRegistries.BIOMES.getEntries()) {
            ResourceLocation location = biome.getKey().location();
            // Skip minecraft
            if (Objects.equals(location.getNamespace(), NamespacedKey.MINECRAFT)) {
                continue;
            }
            String biomeName = standardize(location);
            Biome bukkitBiome = EnumHelper.makeEnum(Biome.class, biomeName, i++, ImmutableList.of(), ImmutableList.of());
            enumBiomes.add(bukkitBiome);
            CatServer.LOGGER.debug("Save-Biome: {}", bukkitBiome.name());
        }
        EnumHelper.addEnums(Biome.class, enumBiomes);
        CatServer.LOGGER.info("Registered {} biomes into Bukkit", enumBiomes.size());
    }

    private static void registerPotionEffects() {
        int i = 0;
        for (var potion : ForgeRegistries.MOB_EFFECTS.getEntries()) {
            var name = standardize(potion.getValue().getRegistryName());
            CraftCustomPotionEffect potionEffect = new CraftCustomPotionEffect(potion.getValue(), name);
            PotionEffectType.registerPotionEffectType(potionEffect);
            i++;
            CatServer.LOGGER.debug("Save-MobEffect: {}", potionEffect.getName());
        }
        org.bukkit.potion.PotionEffectType.stopAcceptingRegistrations();
        CatServer.LOGGER.info("Registered {} mob effect into Bukkit", i);
        // Register potion type
        int ordinal = EntityType.values().length;
        List<PotionType> potionTypes = Lists.newArrayList();
        BiMap<PotionType, String> regularMap = HashBiMap.create(CraftPotionUtil.regular);
        for (var potionType : ForgeRegistries.POTIONS.getEntries()) {
            if (CraftPotionUtil.toBukkit(potionType.getValue().getRegistryName().toString()).getType() == PotionType.UNCRAFTABLE && potionType.getValue() != Potions.EMPTY) {
                var name = standardize(potionType.getValue().getRegistryName());
                MobEffectInstance effectInstance = potionType.getValue().getEffects().isEmpty() ? null : potionType.getValue().getEffects().get(0);
                PotionType type = EnumHelper.makeEnum(PotionType.class, name, ordinal++, Arrays.asList(PotionEffectType.class, boolean.class, boolean.class), Arrays.asList(effectInstance == null ? null : PotionEffectType.getById(MobEffect.getId(effectInstance.getEffect())), false, false));
                potionTypes.add(type);
                regularMap.put(type, potionType.getValue().getRegistryName().toString());
                CatServer.LOGGER.debug("Save-PotionType: {}", type);
            }
        }
        EnumHelper.addEnums(PotionType.class, potionTypes);
        CatServer.LOGGER.info("Registered {} new potion type into Bukkit", regularMap.size());
    }

    private static void registerEnchantments() {
        int i = 0;
        for (var enchantment : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            var name = standardize(enchantment.getValue().getRegistryName());
            CraftCustomEnchantment enchantmentCb = new CraftCustomEnchantment(enchantment.getValue(), name);
            Enchantment.registerEnchantment(enchantmentCb);
            i++;
            CatServer.LOGGER.debug("Save-Enchantment: {}", enchantmentCb.getName());
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
        CatServer.LOGGER.info("Registered {} enchantments into Bukkit", i);
    }

    private static void registerMaterials() {
        Map<Block, Material> BLOCK_MATERIAL = Unsafe.getStatic(CraftMagicNumbers.class, "BLOCK_MATERIAL");
        Map<Item, Material> ITEM_MATERIAL = Unsafe.getStatic(CraftMagicNumbers.class, "ITEM_MATERIAL");
        Map<Material, Item> MATERIAL_ITEM = Unsafe.getStatic(CraftMagicNumbers.class, "MATERIAL_ITEM");
        Map<Material, Block> MATERIAL_BLOCK = Unsafe.getStatic(CraftMagicNumbers.class, "MATERIAL_BLOCK");

        int length = Material.values().length;
        List<Material> values = Lists.newArrayList();
        int blocks = 0, items = 0;
        for (var entry : ForgeRegistries.BLOCKS.getEntries()) {
            var location = entry.getKey().location();
            // Skip minecraft
            if (Objects.equals(location.getNamespace(), NamespacedKey.MINECRAFT)) {
                continue;
            }
            var blockName = standardize(location);
            var block = entry.getValue();
            var item = ForgeRegistries.ITEMS.getValue(location);
            try {
                Class<?> match = CraftBlockData.getClosestBlockDataClass(block.getClass());
                Class<?> blockClass = match == null ? null : match.getInterfaces()[0];
                Material material;
                if (blockClass == null) {
                    material = Material.addMaterial(blockName, length, CraftNamespacedKey.fromMinecraft(location), true, item != null && item != Items.AIR);
                } else {
                    material = Material.addMaterial(blockName, length, blockClass, CraftNamespacedKey.fromMinecraft(location), true, item != null && item != Items.AIR);
                }
                if (material == null) {
                    CatServer.LOGGER.error("Cannot register new forgeBlock : " + blockName);
                    continue;
                }
                length++;
                values.add(material);
                blocks++;
                BLOCK_MATERIAL.put(block, material);
                MATERIAL_BLOCK.put(material, block);
                CatServer.LOGGER.debug("Save-Block: " + material.name() + " - " + blockName);
            } catch (Throwable e) {
                CatServer.LOGGER.error("Cannot register new forgeBlock : " + blockName);
                e.printStackTrace();
            }
        }

        for (var entry : ForgeRegistries.ITEMS.getEntries()) {
            var location = entry.getKey().location();
            // Skip minecraft
            if (Objects.equals(location.getNamespace(), NamespacedKey.MINECRAFT)) {
                continue;
            }
            var itemName = standardize(location);
            var item = entry.getValue();
            var material = Material.getMaterial(itemName);
            if (material == null) {
                try {
                    material = Material.addMaterial(itemName, length, CraftNamespacedKey.fromMinecraft(location), false, true);
                    if (material == null) {
                        CatServer.LOGGER.error("Cannot register new forgeItem: " + itemName);
                        continue;
                    }
                    values.add(material);
                    length++;
                    items++;
                    CatServer.LOGGER.debug("Save-Item: " + material.name() + " - " + itemName);
                } catch (Throwable e) {
                    CatServer.LOGGER.error("Cannot register new forgeItem : " + itemName);
                }
            }
            ITEM_MATERIAL.put(item, material);
            MATERIAL_ITEM.put(material, item);
        }
        EnumHelper.addEnums(Material.class, values);
        CatServer.LOGGER.info("Registered {} blocks into Bukkit", blocks);
        CatServer.LOGGER.info("Registered {} items into Bukkit", items);
    }

    @Contract("null -> fail")
    public static String standardize(ResourceLocation location) {
        Preconditions.checkNotNull(location, "location");
        return (location.getNamespace().equals(NamespacedKey.MINECRAFT) ? location.getPath() : location.toString())
                .replace(':', '_')
                .replaceAll("\\s+", "_")
                .replaceAll("\\W", "")
                .toUpperCase(Locale.ENGLISH);
    }
}