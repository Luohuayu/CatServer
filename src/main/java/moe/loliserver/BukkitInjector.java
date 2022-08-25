package moe.loliserver;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import catserver.server.entity.CraftCustomEntity;
import moe.loliserver.utils.EnumHelper;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import org.bukkit.Art;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_16_R3.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.permissions.DefaultPermissions;

public class BukkitInjector {

    public static BiMap<RegistryKey<DimensionType>, World.Environment> environment =
            HashBiMap.create(ImmutableMap.<RegistryKey<DimensionType>, World.Environment>builder()
                    .put(DimensionType.OVERWORLD_LOCATION, World.Environment.NORMAL)
                    .put(DimensionType.NETHER_LOCATION, World.Environment.NETHER)
                    .put(DimensionType.END_LOCATION, World.Environment.THE_END)
                    .build());

    public static Map<Villager.Profession, ResourceLocation> profession = new HashMap<>();
    public static Map<org.bukkit.attribute.Attribute, ResourceLocation> attributemap = new HashMap<>();
    public static Map<PaintingType, Art> artMap = new HashMap<>();

    public static Map<net.minecraft.entity.EntityType<?>, String> entityTypeMap = new HashMap<>();

    public static void init(){
        addEnumMaterialInItems();
        addEnumMaterialsInBlocks();
        addEnumBiome();
        addEnumEnchantment();
        addEnumPotion();
        addEnumPattern();
        addEnumEntity();
        addEnumVillagerProfession();
        addEnumAttribute();
        addEnumArt();
    }


    public static void addEnumMaterialInItems(){
        for (Map.Entry<RegistryKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                // inject item materials into Bukkit for FML
                String materialName = normalizeName(entry.getKey().toString()).replace("RESOURCEKEYMINECRAFT_ITEM__", "");
                NamespacedKey keyForge = CraftNamespacedKey.fromMinecraft(resourceLocation);
                Item item = entry.getValue();
                int id = Item.getId(item);
                Material material = Material.addMaterial(materialName, id, false, resourceLocation.getNamespace(), keyForge);
                CraftMagicNumbers.ITEM_MATERIAL.put(item, material);
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
                if (material != null) {
                    LoliServer.LOGGER.debug("Save-ITEM: " + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumMaterialsInBlocks(){
        for (Map.Entry<RegistryKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation resourceLocation = entry.getValue().getRegistryName();
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                // inject block materials into Bukkit for FML
                String materialName = normalizeName(entry.getKey().toString()).replace("RESOURCEKEYMINECRAFT_BLOCK__", "");
                NamespacedKey keyForge = CraftNamespacedKey.fromMinecraft(resourceLocation);
                Block block = entry.getValue();
                int id = Item.getId(block.asItem());
                Material material = Material.addMaterial(materialName, id, true, resourceLocation.getNamespace(), keyForge);
                CraftMagicNumbers.BLOCK_MATERIAL.put(block, material);
                CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
                if (material != null) {
                    LoliServer.LOGGER.debug("Save-BLOCK:" + material.name() + " - " + materialName);
                }
            }
        }
    }


    public static void addEnumEnchantment() {
        // Enchantment
        for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(entry.getValue()));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void addEnumPotion() {
        // Points
        for (Map.Entry<RegistryKey<Effect>, Effect> entry : ForgeRegistries.POTIONS.getEntries()) {
            PotionEffectType pet = new CraftPotionEffectType(entry.getValue());
            PotionEffectType.registerPotionEffectType(pet);
        }
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void addEnumBiome() {
        List<String> map = new ArrayList<>();
        for (Map.Entry<RegistryKey<Biome>, Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getValue().getRegistryName().getNamespace();
            if (!biomeName.equals(NamespacedKey.MINECRAFT) && !map.contains(biomeName)) {
                map.add(biomeName);
                org.bukkit.block.Biome biome = EnumHelper.addEnum0(org.bukkit.block.Biome.class, biomeName, new Class[0]);
                LoliServer.LOGGER.debug("Save-BIOME:" + biome.name() + " - " + biomeName);
            }
        }
        map.clear();
    }

    public static void addEnumPattern(){
        Map<String, PatternType> PATTERN_MAP = ObfuscationReflectionHelper.getPrivateValue(PatternType.class, null, "byString");
        for (BannerPattern bannerpattern : BannerPattern.values()) {
            String p_i47246_3_ = bannerpattern.name();
            String hashname = bannerpattern.getHashname();
            if (PatternType.getByIdentifier(hashname) == null) {
                PatternType patternType = EnumHelper.addEnum0(PatternType.class, p_i47246_3_, new Class[]{String.class}, hashname);
                PATTERN_MAP.put(hashname, patternType);
            }
        }
    }

    public static void addEnumEnvironment() {
        int i = World.Environment.values().length;
        Registry<DimensionType> registry = MinecraftServer.getServer().registryHolder.dimensionTypes();
        for (Map.Entry<RegistryKey<DimensionType>, DimensionType> entry : registry.entrySet()) {
            RegistryKey<DimensionType> key = entry.getKey();
            World.Environment environment1 = environment.get(key);
            if (environment1 == null) {
                String name = normalizeName(key.location().toString());
                int id = i - 1;
                environment1 = EnumHelper.addEnum(World.Environment.class, name, new Class[]{Integer.TYPE}, new Object[]{id});
                environment.put(key, environment1);
                LoliServer.LOGGER.debug("Registered forge DimensionType as environment {}", environment1);
                i++;
            }
        }
    }

    public static WorldType addEnumWorldType(String name)
    {
        WorldType worldType = EnumHelper.addEnum0(WorldType.class, name, new Class [] { String.class }, name);
        Map<String, WorldType> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(WorldType.class, null, "BY_NAME");
        BY_NAME.put(name.toUpperCase(), worldType);
        return worldType;
    }

    public static void addEnumEntity() {
        Map<String, EntityType> NAME_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map<Short, EntityType> ID_MAP = ObfuscationReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");

        for (Map.Entry<RegistryKey<net.minecraft.entity.EntityType<?>>, net.minecraft.entity.EntityType<?>> entity : ForgeRegistries.ENTITIES.getEntries()) {
            ResourceLocation resourceLocation = entity.getValue().getRegistryName();
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String entityType = normalizeName(resourceLocation.toString());
                int typeId = entityType.hashCode();
                EntityType bukkitType = EnumHelper.addEnum0(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, entityType.toLowerCase(), CraftCustomEntity.class, typeId, false);
                NAME_MAP.put(entityType.toLowerCase(), bukkitType);
                ID_MAP.put((short) typeId, bukkitType);
                BukkitInjector.entityTypeMap.put(entity.getValue(), entityType);
            }
        }
    }

    public static void registerDefaultPermission(String name, DefaultPermissionLevel level, String desc) {
        PermissionDefault permissionDefault;
        switch (level) {
            case ALL:
                permissionDefault = PermissionDefault.TRUE;
                break;
            case OP:
                permissionDefault = PermissionDefault.OP;
                break;
            case NONE:
            default:
                permissionDefault = PermissionDefault.FALSE;
                break;
        }
        DefaultPermissions.registerPermission(name, desc, permissionDefault);
    }

    public static void addEnumVillagerProfession() {
        for (VillagerProfession villagerProfession : ForgeRegistries.PROFESSIONS) {
            ResourceLocation resourceLocation = villagerProfession.getRegistryName();
            String name = normalizeName(resourceLocation.toString());
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                Villager.Profession vp = EnumHelper.addEnum0(Villager.Profession.class, name, new Class[0]);
                profession.put(vp, resourceLocation);
                LoliServer.LOGGER.debug("Registered forge VillagerProfession as Profession {}", vp.name());
            }
        }
    }

    public static void addEnumAttribute() {
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES) {
            ResourceLocation resourceLocation = attribute.getRegistryName();
            String name = normalizeName(resourceLocation.getPath());
            if(!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                org.bukkit.attribute.Attribute ab = EnumHelper.addEnum0(org.bukkit.attribute.Attribute.class, name, new Class[]{String.class}, resourceLocation.getPath());
                attributemap.put(ab, resourceLocation);
                LoliServer.LOGGER.debug("Registered forge Attribute as Attribute(Bukkit) {}", ab.name());
            }
        }
    }

    public static void addEnumArt() {
        int i = Art.values().length;
        HashMap<String, Art> BY_NAME = ObfuscationReflectionHelper.getPrivateValue(Art.class, null, "BY_NAME");
        HashMap<Integer, Art> BY_ID = ObfuscationReflectionHelper.getPrivateValue(Art.class, null, "BY_ID");
        for (PaintingType entry : ForgeRegistries.PAINTING_TYPES) {
            int width = entry.getWidth();
            int height = entry.getHeight();
            ResourceLocation resourceLocation = entry.getRegistryName();
            if (!resourceLocation.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                String name = normalizeName(resourceLocation.toString());
                int id = i - 1;
                Art art = EnumHelper.addEnum(Art.class, name, new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}, new Object[]{id, width, height});
                artMap.put(entry, art);
                BY_NAME.put(name, art);
                BY_ID.put(id, art);
                LoliServer.LOGGER.debug("Registered forge PaintingType as Art {}", art);
                i++;
            }
        }
    }

    public static String normalizeName(String name) {
        return name.toUpperCase(java.util.Locale.ENGLISH).replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
    }
}
