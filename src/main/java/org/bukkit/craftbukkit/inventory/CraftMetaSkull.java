// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import net.minecraft.nbt.NBTBase;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.authlib.GameProfile;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.SkullMeta;

//@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta
{
    static final ItemMetaKey SKULL_PROFILE;
    static final ItemMetaKey SKULL_OWNER;
    static final int MAX_OWNER_LENGTH = 16;
    private GameProfile profile;
    
    static {
        SKULL_PROFILE = new ItemMetaKey("SkullProfile");
        SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");
    }
    
    CraftMetaSkull(final CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        final CraftMetaSkull skullMeta = (CraftMetaSkull)meta;
        this.profile = skullMeta.profile;
    }
    
    CraftMetaSkull(final NBTTagCompound tag) {
        super(tag);
        if (tag.hasKey(CraftMetaSkull.SKULL_OWNER.NBT, 10)) {
            this.profile = NBTUtil.readGameProfileFromNBT(tag.getCompoundTag(CraftMetaSkull.SKULL_OWNER.NBT));
        }
        else if (tag.hasKey(CraftMetaSkull.SKULL_OWNER.NBT, 8) && !tag.getString(CraftMetaSkull.SKULL_OWNER.NBT).isEmpty()) {
            this.profile = new GameProfile((UUID)null, tag.getString(CraftMetaSkull.SKULL_OWNER.NBT));
        }
    }
    
    CraftMetaSkull(final Map<String, Object> map) {
        super(map);
        if (this.profile == null) {
            this.setOwner(SerializableMeta.getString(map, CraftMetaSkull.SKULL_OWNER.BUKKIT, true));
        }
    }
    
    @Override
    void deserializeInternal(final NBTTagCompound tag) {
        if (tag.hasKey(CraftMetaSkull.SKULL_PROFILE.NBT, 10)) {
            this.profile = NBTUtil.readGameProfileFromNBT(tag.getCompoundTag(CraftMetaSkull.SKULL_PROFILE.NBT));
        }
    }
    
    @Override
    void serializeInternal(final Map<String, NBTBase> internalTags) {
        if (this.profile != null) {
            final NBTTagCompound nbtData = new NBTTagCompound();
            NBTUtil.writeGameProfile(nbtData, this.profile);
            internalTags.put(CraftMetaSkull.SKULL_PROFILE.NBT, nbtData);
        }
    }
    
    @Override
    void applyToItem(final NBTTagCompound tag) {
        super.applyToItem(tag);
        if (this.profile != null) {
            final NBTTagCompound owner = new NBTTagCompound();
            NBTUtil.writeGameProfile(owner, this.profile);
            tag.setTag(CraftMetaSkull.SKULL_OWNER.NBT, owner);
        }
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isSkullEmpty();
    }
    
    boolean isSkullEmpty() {
        return this.profile == null;
    }
    
    @Override
    boolean applicableTo(final Material type) {
        switch (type) {
            case SKULL_ITEM: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull)super.clone();
    }
    
    @Override
    public boolean hasOwner() {
        return this.profile != null && this.profile.getName() != null;
    }
    
    @Override
    public String getOwner() {
        return this.hasOwner() ? this.profile.getName() : null;
    }
    
    @Override
    public boolean setOwner(final String name) {
        if (name != null && name.length() > 16) {
            return false;
        }
        if (name == null) {
            this.profile = null;
        }
        else {
            this.profile = new GameProfile((UUID)null, name);
        }
        return true;
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.hasOwner()) {
            hash = 61 * hash + this.profile.hashCode();
        }
        return (original != hash) ? (CraftMetaSkull.class.hashCode() ^ hash) : hash;
    }
    
    @Override
    boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSkull) {
            final CraftMetaSkull that = (CraftMetaSkull)meta;
            return this.hasOwner() ? (that.hasOwner() && this.profile.equals((Object)that.profile)) : (!that.hasOwner());
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || this.isSkullEmpty());
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasOwner()) {
            return (ImmutableMap.Builder<String, Object>)builder.put(CraftMetaSkull.SKULL_OWNER.BUKKIT, this.profile.getName());
        }
        return builder;
    }
}
