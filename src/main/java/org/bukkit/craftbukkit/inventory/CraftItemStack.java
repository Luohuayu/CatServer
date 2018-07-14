// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentHelper;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.lang.Validate;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import net.minecraft.item.Item;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.ItemStack;

@DelegateDeserialization(ItemStack.class)
public final class CraftItemStack extends ItemStack
{
    net.minecraft.item.ItemStack handle;
    
    public static net.minecraft.item.ItemStack asNMSCopy(final ItemStack original) {
        if (original instanceof CraftItemStack) {
            final CraftItemStack stack = (CraftItemStack)original;
            return (stack.handle == null) ? null : stack.handle.copy();
        }
        if (original == null || original.getTypeId() <= 0) {
            return null;
        }
        final Item item = CraftMagicNumbers.getItem(original.getType());
        if (item == null) {
            return null;
        }
        final net.minecraft.item.ItemStack stack2 = new net.minecraft.item.ItemStack(item, original.getAmount(), original.getDurability());
        if (original.hasItemMeta()) {
            setItemMeta(stack2, original.getItemMeta());
        }
        return stack2;
    }
    
    public static net.minecraft.item.ItemStack copyNMSStack(final net.minecraft.item.ItemStack original, final int amount) {
        final net.minecraft.item.ItemStack stack = original.copy();
        stack.stackSize = amount;
        return stack;
    }
    
    public static ItemStack asBukkitCopy(final net.minecraft.item.ItemStack original) {
        if (original == null) {
            return new ItemStack(Material.AIR);
        }
        final ItemStack stack = new ItemStack(CraftMagicNumbers.getMaterial(original.getItem()), original.stackSize, (short)original.getMetadata());
        if (hasItemMeta(original)) {
            stack.setItemMeta(getItemMeta(original));
        }
        return stack;
    }
    
    public static CraftItemStack asCraftMirror(final net.minecraft.item.ItemStack original) {
        return new CraftItemStack(original);
    }
    
    public static CraftItemStack asCraftCopy(final ItemStack original) {
        if (original instanceof CraftItemStack) {
            final CraftItemStack stack = (CraftItemStack)original;
            return new CraftItemStack((stack.handle == null) ? null : stack.handle.copy());
        }
        return new CraftItemStack(original);
    }
    
    public static CraftItemStack asNewCraftStack(final Item item) {
        return asNewCraftStack(item, 1);
    }
    
    public static CraftItemStack asNewCraftStack(final Item item, final int amount) {
        return new CraftItemStack(CraftMagicNumbers.getMaterial(item), amount, (short)0, (ItemMeta)null);
    }
    
    private CraftItemStack(final net.minecraft.item.ItemStack item) {
        this.handle = item;
    }
    
    private CraftItemStack(final ItemStack item) {
        this(item.getTypeId(), item.getAmount(), item.getDurability(), item.hasItemMeta() ? item.getItemMeta() : null);
    }
    
    private CraftItemStack(final Material type, final int amount, final short durability, final ItemMeta itemMeta) {
        this.setType(type);
        this.setAmount(amount);
        this.setDurability(durability);
        this.setItemMeta(itemMeta);
    }
    
    private CraftItemStack(final int typeId, final int amount, final short durability, final ItemMeta itemMeta) {
        this(Material.getMaterial(typeId), amount, durability, itemMeta);
    }
    
    @Override
    public int getTypeId() {
        return (this.handle != null) ? CraftMagicNumbers.getId(this.handle.getItem()) : 0;
    }
    
    @Override
    public void setTypeId(final int type) {
        if (this.getTypeId() == type) {
            return;
        }
        if (type == 0) {
            this.handle = null;
        }
        else if (CraftMagicNumbers.getItem(type) == null) {
            this.handle = null;
        }
        else if (this.handle == null) {
            this.handle = new net.minecraft.item.ItemStack(CraftMagicNumbers.getItem(type), 1, 0);
        }
        else {
            this.handle.setItem(CraftMagicNumbers.getItem(type));
            if (this.hasItemMeta()) {
                setItemMeta(this.handle, getItemMeta(this.handle));
            }
        }
        this.setData(null);
    }
    
    @Override
    public int getAmount() {
        return (this.handle != null) ? this.handle.stackSize : 0;
    }
    
    @Override
    public void setAmount(final int amount) {
        if (this.handle == null) {
            return;
        }
        if (amount == 0) {
            this.handle = null;
        }
        else {
            this.handle.stackSize = amount;
        }
    }
    
    @Override
    public void setDurability(final short durability) {
        if (this.handle != null) {
            this.handle.setItemDamage(durability);
        }
    }
    
    @Override
    public short getDurability() {
        if (this.handle != null) {
            return (short)this.handle.getMetadata();
        }
        return -1;
    }
    
    @Override
    public int getMaxStackSize() {
        return (this.handle == null) ? Material.AIR.getMaxStackSize() : this.handle.getItem().getItemStackLimit();
    }
    
    @Override
    public void addUnsafeEnchantment(final Enchantment ench, final int level) {
        Validate.notNull((Object)ench, "Cannot add null enchantment");
        if (!makeTag(this.handle)) {
            return;
        }
        NBTTagList list = getEnchantmentList(this.handle);
        if (list == null) {
            list = new NBTTagList();
            this.handle.getTagCompound().setTag(CraftMetaItem.ENCHANTMENTS.NBT, list);
        }
        for (int size = list.tagCount(), i = 0; i < size; ++i) {
            final NBTTagCompound tag = list.getCompoundTagAt(i);
            final short id = tag.getShort(CraftMetaItem.ENCHANTMENTS_ID.NBT);
            if (id == ench.getId()) {
                tag.setShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT, (short)level);
                return;
            }
        }
        final NBTTagCompound tag2 = new NBTTagCompound();
        tag2.setShort(CraftMetaItem.ENCHANTMENTS_ID.NBT, (short)ench.getId());
        tag2.setShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT, (short)level);
        list.appendTag(tag2);
    }
    
    static boolean makeTag(final net.minecraft.item.ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }
        return true;
    }
    
    @Override
    public boolean containsEnchantment(final Enchantment ench) {
        return this.getEnchantmentLevel(ench) > 0;
    }
    
    @Override
    public int getEnchantmentLevel(final Enchantment ench) {
        Validate.notNull((Object)ench, "Cannot find null enchantment");
        if (this.handle == null) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(CraftEnchantment.getRaw(ench), this.handle);
    }
    
    @Override
    public int removeEnchantment(final Enchantment ench) {
        Validate.notNull((Object)ench, "Cannot remove null enchantment");
        final NBTTagList list = getEnchantmentList(this.handle);
        if (list == null) {
            return 0;
        }
        int index = Integer.MIN_VALUE;
        int level = Integer.MIN_VALUE;
        final int size = list.tagCount();
        for (int i = 0; i < size; ++i) {
            final NBTTagCompound enchantment = list.getCompoundTagAt(i);
            final int id = 0xFFFF & enchantment.getShort(CraftMetaItem.ENCHANTMENTS_ID.NBT);
            if (id == ench.getId()) {
                index = i;
                level = (0xFFFF & enchantment.getShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT));
                break;
            }
        }
        if (index == Integer.MIN_VALUE) {
            return 0;
        }
        if (size == 1) {
            this.handle.getTagCompound().removeTag(CraftMetaItem.ENCHANTMENTS.NBT);
            if (this.handle.getTagCompound().hasNoTags()) {
                this.handle.setTagCompound(null);
            }
            return level;
        }
        final NBTTagList listCopy = new NBTTagList();
        for (int i = 0; i < size; ++i) {
            if (i != index) {
                listCopy.appendTag(list.getCompoundTagAt(i));
            }
        }
        this.handle.getTagCompound().setTag(CraftMetaItem.ENCHANTMENTS.NBT, listCopy);
        return level;
    }
    
    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return getEnchantments(this.handle);
    }
    
    static Map<Enchantment, Integer> getEnchantments(final net.minecraft.item.ItemStack item) {
        final NBTTagList list = (item != null && item.isItemEnchanted()) ? item.getEnchantmentTagList() : null;
        if (list == null || list.tagCount() == 0) {
            return /*(Map<Enchantment, Integer>)*/ImmutableMap.of();
        }
        final ImmutableMap.Builder<Enchantment, Integer> result = /*(ImmutableMap.Builder<Enchantment, Integer>)*/ImmutableMap.builder();
        for (int i = 0; i < list.tagCount(); ++i) {
            final int id = 0xFFFF & list.getCompoundTagAt(i).getShort(CraftMetaItem.ENCHANTMENTS_ID.NBT);
            final int level = 0xFFFF & list.getCompoundTagAt(i).getShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT);
            result.put(/*(Object)*/Enchantment.getById(id), /*(Object)*/level);
        }
        return (Map<Enchantment, Integer>)result.build();
    }
    
    static NBTTagList getEnchantmentList(final net.minecraft.item.ItemStack item) {
        return (item != null && item.isItemEnchanted()) ? item.getEnchantmentTagList() : null;
    }
    
    @Override
    public CraftItemStack clone() {
        final CraftItemStack itemStack = (CraftItemStack)super.clone();
        if (this.handle != null) {
            itemStack.handle = this.handle.copy();
        }
        return itemStack;
    }
    
    @Override
    public ItemMeta getItemMeta() {
        return getItemMeta(this.handle);
    }
    
    public static ItemMeta getItemMeta(final net.minecraft.item.ItemStack item) {
        if (!hasItemMeta(item)) {
            return CraftItemFactory.instance().getItemMeta(getType(item));
        }
        switch (getType(item)) {
            case WRITTEN_BOOK: {
                return new CraftMetaBookSigned(item.getTagCompound());
            }
            case BOOK_AND_QUILL: {
                return new CraftMetaBook(item.getTagCompound());
            }
            case SKULL_ITEM: {
                return new CraftMetaSkull(item.getTagCompound());
            }
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS: {
                return new CraftMetaLeatherArmor(item.getTagCompound());
            }
            case POTION:
            case SPLASH_POTION:
            case TIPPED_ARROW:
            case LINGERING_POTION: {
                return new CraftMetaPotion(item.getTagCompound());
            }
            case MAP: {
                return new CraftMetaMap(item.getTagCompound());
            }
            case FIREWORK: {
                return new CraftMetaFirework(item.getTagCompound());
            }
            case FIREWORK_CHARGE: {
                return new CraftMetaCharge(item.getTagCompound());
            }
            case ENCHANTED_BOOK: {
                return new CraftMetaEnchantedBook(item.getTagCompound());
            }
            case BANNER: {
                return new CraftMetaBanner(item.getTagCompound());
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
                return new CraftMetaBlockState(item.getTagCompound(), CraftMagicNumbers.getMaterial(item.getItem()));
            }
            default: {
                return new CraftMetaItem(item.getTagCompound());
            }
        }
    }
    
    static Material getType(final net.minecraft.item.ItemStack item) {
        final Material material = Material.getMaterial((item == null) ? 0 : CraftMagicNumbers.getId(item.getItem()));
        return (material == null) ? Material.AIR : material;
    }
    
    @Override
    public boolean setItemMeta(final ItemMeta itemMeta) {
        return setItemMeta(this.handle, itemMeta);
    }
    
    public static boolean setItemMeta(final net.minecraft.item.ItemStack item, ItemMeta itemMeta) {
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
        if (itemMeta == null) {
            return true;
        }
        final NBTTagCompound tag = new NBTTagCompound();
        item.setTagCompound(tag);
        ((CraftMetaItem)itemMeta).applyToItem(tag);
        return true;
    }
    
    @Override
    public boolean isSimilar(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        if (!(stack instanceof CraftItemStack)) {
            return stack.getClass() == ItemStack.class && stack.isSimilar(this);
        }
        final CraftItemStack that = (CraftItemStack)stack;
        return this.handle == that.handle || (this.handle != null && that.handle != null && that.getTypeId() == this.getTypeId() && this.getDurability() == that.getDurability() && (this.hasItemMeta() ? (that.hasItemMeta() && this.handle.getTagCompound().equals(that.handle.getTagCompound())) : (!that.hasItemMeta())));
    }
    
    @Override
    public boolean hasItemMeta() {
        return hasItemMeta(this.handle);
    }
    
    static boolean hasItemMeta(final net.minecraft.item.ItemStack item) {
        return item != null && item.getTagCompound() != null && !item.getTagCompound().hasNoTags();
    }
}
