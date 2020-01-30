package org.bukkit.craftbukkit.inventory;

import static org.bukkit.craftbukkit.inventory.CraftMetaItem.ENCHANTMENTS;
import static org.bukkit.craftbukkit.inventory.CraftMetaItem.ENCHANTMENTS_ID;
import static org.bukkit.craftbukkit.inventory.CraftMetaItem.ENCHANTMENTS_LVL;

import java.util.Map;

import catserver.server.inventory.CatForgeItemCap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.ImmutableMap;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;

@DelegateDeserialization(ItemStack.class)
public final class CraftItemStack extends ItemStack {

    public static net.minecraft.item.ItemStack asNMSCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack) original;
            return stack.handle == null ? net.minecraft.item.ItemStack.EMPTY : stack.handle.copy();
        }
        if (original == null || original.getTypeId() <= 0) {
            return net.minecraft.item.ItemStack.EMPTY;
        }

        Item item = CraftMagicNumbers.getItem(original.getType());

        if (item == null) {
            return net.minecraft.item.ItemStack.EMPTY;
        }

        net.minecraft.item.ItemStack stack = new net.minecraft.item.ItemStack(item, original.getAmount(), original.getDurability(), original.hasForgeItemCap() ? original.getForgeItemCap().getItemCap() : null);
        if (original.hasItemMeta()) {
            setItemMeta(stack, original.getItemMeta());
        }
        return stack;
    }

    public static net.minecraft.item.ItemStack copyNMSStack(net.minecraft.item.ItemStack original, int amount) {
        net.minecraft.item.ItemStack stack = original.copy();
        stack.setCount(amount);
        return stack;
    }

    /**
     * Copies the NMS stack to return as a strictly-Bukkit stack
     */
    public static ItemStack asBukkitCopy(net.minecraft.item.ItemStack original) {
        if (original.isEmpty()) {
            return new ItemStack(Material.AIR);
        }
        ItemStack stack = new ItemStack(CraftMagicNumbers.getMaterial(original.getItem()), original.getCount(), (short) original.getMetadata());
        if (hasItemMeta(original)) {
            stack.setItemMeta(getItemMeta(original));
        }
        CatForgeItemCap.setItemCap(original, stack); // CatServer
        return stack;
    }

    public static CraftItemStack asCraftMirror(net.minecraft.item.ItemStack original) {
        return new CraftItemStack((original == null || original.isEmpty()) ? null : original);
    }

    public static CraftItemStack asCraftCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack) original;
            return new CraftItemStack(stack.handle == null ? null : stack.handle.copy());
        }
        return new CraftItemStack(original);
    }

    public static CraftItemStack asNewCraftStack(Item item) {
        return asNewCraftStack(item, 1);
    }

    public static CraftItemStack asNewCraftStack(Item item, int amount) {
        return new CraftItemStack(CraftMagicNumbers.getMaterial(item), amount, (short) 0, null);
    }

    net.minecraft.item.ItemStack handle;

    /**
     * Mirror
     */
    private CraftItemStack(net.minecraft.item.ItemStack item) {
        this.handle = item;
        CatForgeItemCap.setItemCap(item, this);
    }

    private CraftItemStack(ItemStack item) {
        this(item.getTypeId(), item.getAmount(), item.getDurability(), item.hasItemMeta() ? item.getItemMeta() : null);
    }

    private CraftItemStack(Material type, int amount, short durability, ItemMeta itemMeta) {
        setType(type);
        setAmount(amount);
        setDurability(durability);
        setItemMeta(itemMeta);
    }

    private CraftItemStack(int typeId, int amount, short durability, ItemMeta itemMeta) {
        this(Material.getMaterial(typeId), amount, durability, itemMeta);

    }

    @Override
    public int getTypeId() {
        return handle != null ? CraftMagicNumbers.getId(handle.getItem()) : 0;
    }

    @Override
    public void setTypeId(int type) {
        if (getTypeId() == type) {
            return;
        } else if (type == 0) {
            handle = null;
        } else if (CraftMagicNumbers.getItem(type) == null) { // :(
            handle = null;
        } else if (handle == null) {
            handle = new net.minecraft.item.ItemStack(CraftMagicNumbers.getItem(type), 1, 0);
        } else {
            handle.setItem(CraftMagicNumbers.getItem(type));
            if (hasItemMeta()) {
                // This will create the appropriate item meta, which will contain all the data we intend to keep
                setItemMeta(handle, getItemMeta(handle));
            }
        }
        setData(null);
    }

    @Override
    public int getAmount() {
        return handle != null ? handle.getCount() : 0;
    }

    @Override
    public void setAmount(int amount) {
        if (handle == null) {
            return;
        }

        handle.setCount(amount);
        if (amount == 0) {
            handle = null;
        }
    }

    @Override
    public void setDurability(final short durability) {
        // Ignore damage if item is null
        if (handle != null) {
            handle.setItemDamage(durability);
        }
    }

    @Override
    public short getDurability() {
        if (handle != null) {
            return (short) handle.getMetadata();
        } else {
            return -1;
        }
    }

    @Override
    public int getMaxStackSize() {
        return (handle == null) ? Material.AIR.getMaxStackSize() : handle.getItem().getItemStackLimit(this.handle);
    }

    @Override
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        Validate.notNull(ench, "Cannot add null enchantment");

        if (!makeTag(handle)) {
            return;
        }
        NBTTagList list = getEnchantmentList(handle);
        if (list == null) {
            list = new NBTTagList();
            handle.getTagCompound().setTag(ENCHANTMENTS.NBT, list);
        }
        int size = list.tagCount();

        for (int i = 0; i < size; i++) {
            NBTTagCompound tag = (NBTTagCompound) list.get(i);
            short id = tag.getShort(ENCHANTMENTS_ID.NBT);
            if (id == ench.getId()) {
                tag.setShort(ENCHANTMENTS_LVL.NBT, (short) level);
                return;
            }
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort(ENCHANTMENTS_ID.NBT, (short) ench.getId());
        tag.setShort(ENCHANTMENTS_LVL.NBT, (short) level);
        list.appendTag(tag);
    }

    static boolean makeTag(net.minecraft.item.ItemStack item) {
        if (item == null) {
            return false;
        }

        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        return true;
    }

    @Override
    public boolean containsEnchantment(Enchantment ench) {
        return getEnchantmentLevel(ench) > 0;
    }

    @Override
    public int getEnchantmentLevel(Enchantment ench) {
        Validate.notNull(ench, "Cannot find null enchantment");
        if (handle == null) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(CraftEnchantment.getRaw(ench), handle);
    }

    @Override
    public int removeEnchantment(Enchantment ench) {
        Validate.notNull(ench, "Cannot remove null enchantment");

        NBTTagList list = getEnchantmentList(handle), listCopy;
        if (list == null) {
            return 0;
        }
        int index = Integer.MIN_VALUE;
        int level = Integer.MIN_VALUE;
        int size = list.tagCount();

        for (int i = 0; i < size; i++) {
            NBTTagCompound enchantment = (NBTTagCompound) list.get(i);
            int id = 0xffff & enchantment.getShort(ENCHANTMENTS_ID.NBT);
            if (id == ench.getId()) {
                index = i;
                level = 0xffff & enchantment.getShort(ENCHANTMENTS_LVL.NBT);
                break;
            }
        }

        if (index == Integer.MIN_VALUE) {
            return 0;
        }
        if (size == 1) {
            handle.getTagCompound().removeTag(ENCHANTMENTS.NBT);
            if (handle.getTagCompound().hasNoTags()) {
                handle.setTagCompound(null);
            }
            return level;
        }

        // This is workaround for not having an index removal
        listCopy = new NBTTagList();
        for (int i = 0; i < size; i++) {
            if (i != index) {
                listCopy.appendTag(list.get(i));
            }
        }
        handle.getTagCompound().setTag(ENCHANTMENTS.NBT, listCopy);

        return level;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return getEnchantments(handle);
    }

    static Map<Enchantment, Integer> getEnchantments(net.minecraft.item.ItemStack item) {
        NBTTagList list = (item != null && item.isItemEnchanted()) ? item.getEnchantmentTagList() : null;

        if (list == null || list.tagCount() == 0) {
            return ImmutableMap.of();
        }

        ImmutableMap.Builder<Enchantment, Integer> result = ImmutableMap.builder();

        for (int i = 0; i < list.tagCount(); i++) {
            int id = 0xffff & ((NBTTagCompound) list.get(i)).getShort(ENCHANTMENTS_ID.NBT);
            int level = 0xffff & ((NBTTagCompound) list.get(i)).getShort(ENCHANTMENTS_LVL.NBT);

            result.put(Enchantment.getById(id), level);
        }

        return result.build();
    }

    static NBTTagList getEnchantmentList(net.minecraft.item.ItemStack item) {
        return (item != null && item.isItemEnchanted()) ? item.getEnchantmentTagList() : null;
    }

    @Override
    public CraftItemStack clone() {
        CraftItemStack itemStack = (CraftItemStack) super.clone();
        if (this.handle != null) {
            itemStack.handle = this.handle.copy();
        }
        return itemStack;
    }

    @Override
    public ItemMeta getItemMeta() {
        return getItemMeta(handle);
    }

    public static ItemMeta getItemMeta(net.minecraft.item.ItemStack item) {
        if (!hasItemMeta(item)) {
            return CraftItemFactory.instance().getItemMeta(getType(item));
        }
        switch (getType(item)) {
            case WRITTEN_BOOK:
                return new CraftMetaBookSigned(item.getTagCompound());
            case BOOK_AND_QUILL:
                return new CraftMetaBook(item.getTagCompound());
            case SKULL_ITEM:
                return new CraftMetaSkull(item.getTagCompound());
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return new CraftMetaLeatherArmor(item.getTagCompound());
            case POTION:
            case SPLASH_POTION:
            case LINGERING_POTION:
            case TIPPED_ARROW:
                return new CraftMetaPotion(item.getTagCompound());
            case MAP:
                return new CraftMetaMap(item.getTagCompound());
            case FIREWORK:
                return new CraftMetaFirework(item.getTagCompound());
            case FIREWORK_CHARGE:
                return new CraftMetaCharge(item.getTagCompound());
            case ENCHANTED_BOOK:
                return new CraftMetaEnchantedBook(item.getTagCompound());
            case BANNER:
                return new CraftMetaBanner(item.getTagCompound());
            case MONSTER_EGG:
                return new CraftMetaSpawnEgg(item.getTagCompound());
            case KNOWLEDGE_BOOK:
                return new CraftMetaKnowledgeBook(item.getTagCompound());
            case FURNACE:
            case CHEST:
            case TRAPPED_CHEST:
            case JUKEBOX:
            case DISPENSER:
            case DROPPER:
            case SIGN:
            case MOB_SPAWNER:
            case NOTE_BLOCK:
            case BREWING_STAND_ITEM:
            case ENCHANTMENT_TABLE:
            case COMMAND:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN:
            case BEACON:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case HOPPER:
            case REDSTONE_COMPARATOR:
            case FLOWER_POT_ITEM:
            case SHIELD:
            case STRUCTURE_BLOCK:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case SILVER_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case ENDER_CHEST:
                return new CraftMetaBlockState(item.getTagCompound(), CraftMagicNumbers.getMaterial(item.getItem()));
            default:
                return new CraftMetaItem(item.getTagCompound());
        }
    }

    static Material getType(net.minecraft.item.ItemStack item) {
        Material material = Material.getMaterial(item == null ? 0 : CraftMagicNumbers.getId(item.getItem()));
        return material == null ? Material.AIR : material;
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        return setItemMeta(handle, itemMeta);
    }

    public static boolean setItemMeta(net.minecraft.item.ItemStack item, ItemMeta itemMeta) {
        if (item == null) {
            return false;
        }
        if (CraftItemFactory.instance().equals(itemMeta, null)) {
            item.setTagCompound(null);
            return true;
        }
        if (!CraftItemFactory.instance().isApplicable(itemMeta, getType(item))) {
            return false;
        }

        itemMeta = CraftItemFactory.instance().asMetaFor(itemMeta, getType(item));
        if (itemMeta == null) return true;

        NBTTagCompound tag = new NBTTagCompound();
        item.setTagCompound(tag);

        ((CraftMetaItem) itemMeta).applyToItem(tag);

        return true;
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        if (!(stack instanceof CraftItemStack)) {
            return stack.getClass() == ItemStack.class && stack.isSimilar(this);
        }

        CraftItemStack that = (CraftItemStack) stack;
        if (handle == that.handle) {
            return true;
        }
        if (handle == null || that.handle == null) {
            return false;
        }
        if (!(that.getTypeId() == getTypeId() && getDurability() == that.getDurability())) {
            return false;
        }
        return (hasItemMeta() ? that.hasItemMeta() && handle.getTagCompound().equals(that.handle.getTagCompound()) : !that.hasItemMeta()) && handle.areCapsCompatible(that.handle);
    }

    @Override
    public boolean hasItemMeta() {
        return hasItemMeta(handle);
    }

    static boolean hasItemMeta(net.minecraft.item.ItemStack item) {
        return !(item == null || item.getTagCompound() == null || item.getTagCompound().hasNoTags());
    }
}
