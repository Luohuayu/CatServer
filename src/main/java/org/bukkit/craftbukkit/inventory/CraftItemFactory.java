// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.google.common.collect.ImmutableSet;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import java.util.Collection;
import org.bukkit.Color;
import org.bukkit.inventory.ItemFactory;

public final class CraftItemFactory implements ItemFactory
{
    static final Color DEFAULT_LEATHER_COLOR;
    static final Collection<String> KNOWN_NBT_ATTRIBUTE_NAMES;
    private static final CraftItemFactory instance;
    
    static {
        DEFAULT_LEATHER_COLOR = Color.fromRGB(10511680);
        instance = new CraftItemFactory();
        ConfigurationSerialization.registerClass(CraftMetaItem.SerializableMeta.class);
        KNOWN_NBT_ATTRIBUTE_NAMES = (Collection)ImmutableSet.builder().add((Object)"generic.armor").add((Object)"generic.armorToughness").add((Object)"generic.attackDamage").add((Object)"generic.followRange").add((Object)"generic.knockbackResistance").add((Object)"generic.maxHealth").add((Object)"generic.movementSpeed").add((Object)"generic.attackSpeed").add((Object)"generic.luck").add((Object)"horse.jumpStrength").add((Object)"zombie.spawnReinforcements").build();
    }
    
    @Override
    public boolean isApplicable(final ItemMeta meta, final ItemStack itemstack) {
        return itemstack != null && this.isApplicable(meta, itemstack.getType());
    }
    
    @Override
    public boolean isApplicable(final ItemMeta meta, final Material type) {
        if (type == null || meta == null) {
            return false;
        }
        if (!(meta instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Meta of " + meta.getClass().toString() + " not created by " + CraftItemFactory.class.getName());
        }
        return ((CraftMetaItem)meta).applicableTo(type);
    }
    
    @Override
    public ItemMeta getItemMeta(final Material material) {
        Validate.notNull((Object)material, "Material cannot be null");
        return this.getItemMeta(material, null);
    }
    
    private ItemMeta getItemMeta(final Material material, final CraftMetaItem meta) {
        switch (material) {
            case AIR: {
                return null;
            }
            case WRITTEN_BOOK: {
                return (meta instanceof CraftMetaBookSigned) ? meta : new CraftMetaBookSigned(meta);
            }
            case BOOK_AND_QUILL: {
                return (meta != null && meta.getClass().equals(CraftMetaBook.class)) ? meta : new CraftMetaBook(meta);
            }
            case SKULL_ITEM: {
                return (meta instanceof CraftMetaSkull) ? meta : new CraftMetaSkull(meta);
            }
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS: {
                return (meta instanceof CraftMetaLeatherArmor) ? meta : new CraftMetaLeatherArmor(meta);
            }
            case POTION:
            case SPLASH_POTION:
            case TIPPED_ARROW:
            case LINGERING_POTION: {
                return (meta instanceof CraftMetaPotion) ? meta : new CraftMetaPotion(meta);
            }
            case MAP: {
                return (meta instanceof CraftMetaMap) ? meta : new CraftMetaMap(meta);
            }
            case FIREWORK: {
                return (meta instanceof CraftMetaFirework) ? meta : new CraftMetaFirework(meta);
            }
            case FIREWORK_CHARGE: {
                return (meta instanceof CraftMetaCharge) ? meta : new CraftMetaCharge(meta);
            }
            case ENCHANTED_BOOK: {
                return (meta instanceof CraftMetaEnchantedBook) ? meta : new CraftMetaEnchantedBook(meta);
            }
            case BANNER: {
                return (meta instanceof CraftMetaBanner) ? meta : new CraftMetaBanner(meta);
            }
            case DISPENSER:
            case NOTE_BLOCK:
            case PISTON_BASE:
            case MOB_SPAWNER:
            case CHEST:
            case FURNACE:
            case JUKEBOX:
            case ENCHANTMENT_TABLE:
            case COMMAND:
            case BEACON:
            case TRAPPED_CHEST:
            case DAYLIGHT_DETECTOR:
            case HOPPER:
            case DROPPER:
            case DAYLIGHT_DETECTOR_INVERTED:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN:
            case SIGN:
            case BREWING_STAND_ITEM:
            case FLOWER_POT_ITEM:
            case REDSTONE_COMPARATOR:
            case SHIELD: {
                return new CraftMetaBlockState(meta, material);
            }
            default: {
                return new CraftMetaItem(meta);
            }
        }
    }
    
    @Override
    public boolean equals(final ItemMeta meta1, final ItemMeta meta2) {
        if (meta1 == meta2) {
            return true;
        }
        if (meta1 != null && !(meta1 instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("First meta of " + meta1.getClass().getName() + " does not belong to " + CraftItemFactory.class.getName());
        }
        if (meta2 != null && !(meta2 instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Second meta " + meta2.getClass().getName() + " does not belong to " + CraftItemFactory.class.getName());
        }
        if (meta1 == null) {
            return ((CraftMetaItem)meta2).isEmpty();
        }
        if (meta2 == null) {
            return ((CraftMetaItem)meta1).isEmpty();
        }
        return this.equals((CraftMetaItem)meta1, (CraftMetaItem)meta2);
    }
    
    boolean equals(final CraftMetaItem meta1, final CraftMetaItem meta2) {
        return meta1.equalsCommon(meta2) && meta1.notUncommon(meta2) && meta2.notUncommon(meta1);
    }
    
    public static CraftItemFactory instance() {
        return CraftItemFactory.instance;
    }
    
    @Override
    public ItemMeta asMetaFor(final ItemMeta meta, final ItemStack stack) {
        Validate.notNull((Object)stack, "Stack cannot be null");
        return this.asMetaFor(meta, stack.getType());
    }
    
    @Override
    public ItemMeta asMetaFor(final ItemMeta meta, final Material material) {
        Validate.notNull((Object)material, "Material cannot be null");
        if (!(meta instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Meta of " + ((meta != null) ? meta.getClass().toString() : "null") + " not created by " + CraftItemFactory.class.getName());
        }
        return this.getItemMeta(material, (CraftMetaItem)meta);
    }
    
    @Override
    public Color getDefaultLeatherColor() {
        return CraftItemFactory.DEFAULT_LEATHER_COLOR;
    }
}
