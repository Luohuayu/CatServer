// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.LeatherArmorMeta;

//@DelegateDeserialization(SerializableMeta.class)
class CraftMetaLeatherArmor extends CraftMetaItem implements LeatherArmorMeta
{
    static final ItemMetaKey COLOR;
    private Color color;
    
    static {
        COLOR = new ItemMetaKey("color");
    }
    
    CraftMetaLeatherArmor(final CraftMetaItem meta) {
        super(meta);
        this.color = CraftItemFactory.DEFAULT_LEATHER_COLOR;
        if (!(meta instanceof CraftMetaLeatherArmor)) {
            return;
        }
        final CraftMetaLeatherArmor armorMeta = (CraftMetaLeatherArmor)meta;
        this.color = armorMeta.color;
    }
    
    CraftMetaLeatherArmor(final NBTTagCompound tag) {
        super(tag);
        this.color = CraftItemFactory.DEFAULT_LEATHER_COLOR;
        if (tag.hasKey(CraftMetaLeatherArmor.DISPLAY.NBT)) {
            final NBTTagCompound display = tag.getCompoundTag(CraftMetaLeatherArmor.DISPLAY.NBT);
            if (display.hasKey(CraftMetaLeatherArmor.COLOR.NBT)) {
                this.color = Color.fromRGB(display.getInteger(CraftMetaLeatherArmor.COLOR.NBT));
            }
        }
    }
    
    CraftMetaLeatherArmor(final Map<String, Object> map) {
        super(map);
        this.color = CraftItemFactory.DEFAULT_LEATHER_COLOR;
        this.setColor(SerializableMeta.getObject(Color.class, map, CraftMetaLeatherArmor.COLOR.BUKKIT, true));
    }
    
    @Override
    void applyToItem(final NBTTagCompound itemTag) {
        super.applyToItem(itemTag);
        if (this.hasColor()) {
            this.setDisplayTag(itemTag, CraftMetaLeatherArmor.COLOR.NBT, new NBTTagInt(this.color.asRGB()));
        }
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isLeatherArmorEmpty();
    }
    
    boolean isLeatherArmorEmpty() {
        return !this.hasColor();
    }
    
    @Override
    boolean applicableTo(final Material type) {
        switch (type) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public CraftMetaLeatherArmor clone() {
        return (CraftMetaLeatherArmor)super.clone();
    }
    
    @Override
    public Color getColor() {
        return this.color;
    }
    
    @Override
    public void setColor(final Color color) {
        this.color = ((color == null) ? CraftItemFactory.DEFAULT_LEATHER_COLOR : color);
    }
    
    boolean hasColor() {
        return !CraftItemFactory.DEFAULT_LEATHER_COLOR.equals(this.color);
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasColor()) {
            builder.put(CraftMetaLeatherArmor.COLOR.BUKKIT, this.color);
        }
        return builder;
    }
    
    @Override
    boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaLeatherArmor) {
            final CraftMetaLeatherArmor that = (CraftMetaLeatherArmor)meta;
            return this.color.equals(that.color);
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaLeatherArmor || this.isLeatherArmorEmpty());
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.hasColor()) {
            hash ^= this.color.hashCode();
        }
        return (original != hash) ? (CraftMetaSkull.class.hashCode() ^ hash) : hash;
    }
}
