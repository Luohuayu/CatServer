package org.bukkit.craftbukkit.v1_16_R3.inventory;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.SkullTileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.SkullMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey SKULL_PROFILE = new ItemMetaKey("SkullProfile");

    static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");
    static final int MAX_OWNER_LENGTH = 16;

    private GameProfile profile;
    private CompoundNBT serializedProfile;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        CraftMetaSkull skullMeta = (CraftMetaSkull) meta;
        this.setProfile(skullMeta.profile);
    }

    CraftMetaSkull(CompoundNBT tag) {
        super(tag);

        if (tag.contains(SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            this.setProfile(NBTUtil.readGameProfile(tag.getCompound(SKULL_OWNER.NBT)));
        } else if (tag.contains(SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_STRING) && !tag.getString(SKULL_OWNER.NBT).isEmpty()) {
            this.setProfile(new GameProfile(null, tag.getString(SKULL_OWNER.NBT)));
        }
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (profile == null) {
            setOwner(SerializableMeta.getString(map, SKULL_OWNER.BUKKIT, true));
        }
    }

    @Override
    void deserializeInternal(CompoundNBT tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(SKULL_PROFILE.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            CompoundNBT skullTag = tag.getCompound(SKULL_PROFILE.NBT);
            // convert type of stored Id from String to UUID for backwards compatibility
            if (skullTag.contains("Id", CraftMagicNumbers.NBT.TAG_STRING)) {
                UUID uuid = UUID.fromString(skullTag.getString("Id"));
                skullTag.putUUID("Id", uuid);
            }

            this.setProfile(NBTUtil.readGameProfile(skullTag));
        }
    }

    @Override
    void serializeInternal(final Map<String, INBT> internalTags) {
        if (profile != null) {
            internalTags.put(SKULL_PROFILE.NBT, serializedProfile);
        }
    }

    private void setProfile(GameProfile profile) {
        this.profile = profile;
        this.serializedProfile = (profile == null) ? null : NBTUtil.writeGameProfile(new CompoundNBT(), profile);
    }

    @Override
    void applyToItem(CompoundNBT tag) {
        super.applyToItem(tag);

        if (profile != null) {
            // Fill in textures
            // Must be done sync due to way client handles textures
            setProfile(Futures.getUnchecked(SkullTileEntity.updateGameProfile(profile, Predicates.alwaysTrue(), true))); // Spigot

            tag.put(SKULL_OWNER.NBT, serializedProfile);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isSkullEmpty();
    }

    boolean isSkullEmpty() {
        return profile == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
                return true;
            default:
                return false;
        }
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
    }

    @Override
    public boolean hasOwner() {
        return profile != null && profile.getName() != null;
    }

    @Override
    public String getOwner() {
        return hasOwner() ? profile.getName() : null;
    }

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (hasOwner()) {
            if (profile.getId() != null) {
                return Bukkit.getOfflinePlayer(profile.getId());
            }

            if (profile.getName() != null) {
                return Bukkit.getOfflinePlayer(profile.getName());
            }
        }

        return null;
    }

    @Override
    public boolean setOwner(String name) {
        if (name != null && name.length() > MAX_OWNER_LENGTH) {
            return false;
        }

        if (name == null) {
            setProfile(null);
        } else {
            setProfile(new GameProfile(null, name));
        }

        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        if (owner == null) {
            setProfile(null);
        } else if (owner instanceof CraftPlayer) {
            setProfile(((CraftPlayer) owner).getProfile());
        } else {
            setProfile(new GameProfile(owner.getUniqueId(), owner.getName()));
        }

        return true;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasOwner()) {
            hash = 61 * hash + profile.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSkull) {
            CraftMetaSkull that = (CraftMetaSkull) meta;

            // SPIGOT-5403: equals does not check properties
            return (this.profile != null ? that.profile != null && this.serializedProfile.equals(that.serializedProfile) : that.profile == null);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || isSkullEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        if (hasOwner()) {
            return builder.put(SKULL_OWNER.BUKKIT, this.profile.getName());
        }
        return builder;
    }
}
