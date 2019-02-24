package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BookMeta;

import com.google.common.collect.ImmutableMap.Builder;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaBookSigned extends CraftMetaBook implements BookMeta {

    CraftMetaBookSigned(CraftMetaItem meta) {
        super(meta);
    }

    CraftMetaBookSigned(NBTTagCompound tag) {
        super(tag, false);

        boolean resolved = true;
        if (tag.hasKey(RESOLVED.NBT)) {
            resolved = tag.getBoolean(RESOLVED.NBT);
        }

        if (tag.hasKey(BOOK_PAGES.NBT)) {
            NBTTagList pages = tag.getTagList(BOOK_PAGES.NBT, CraftMagicNumbers.NBT.TAG_STRING);

            for (int i = 0; i < Math.min(pages.tagCount(), MAX_PAGES); i++) {
                String page = pages.getStringTagAt(i);
                if (resolved) {
                    try {
                        this.pages.add(ITextComponent.Serializer.jsonToComponent(page));
                        continue;
                    } catch (Exception e) {
                        // Ignore and treat as an old book
                    }
                }
                addPage(page);
            }
        }
    }

    CraftMetaBookSigned(Map<String, Object> map) {
        super(map);
    }

    @Override
    void applyToItem(NBTTagCompound itemData) {
        super.applyToItem(itemData, false);

        if (hasTitle()) {
            itemData.setString(BOOK_TITLE.NBT, this.title);
        } else {
            itemData.setString(BOOK_TITLE.NBT, " ");
        }

        if (hasAuthor()) {
            itemData.setString(BOOK_AUTHOR.NBT, this.author);
        } else {
            itemData.setString(BOOK_AUTHOR.NBT, " ");
        }

        if (hasPages()) {
            NBTTagList list = new NBTTagList();
            for (ITextComponent page : pages) {
                list.appendTag(new NBTTagString(
                    ITextComponent.Serializer.componentToJson(page)
                ));
            }
            itemData.setTag(BOOK_PAGES.NBT, list);
        }
        itemData.setBoolean(RESOLVED.NBT, true);

        if (generation != null) {
            itemData.setInteger(GENERATION.NBT, generation);
        } else {
            itemData.setInteger(GENERATION.NBT, 0);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
        case WRITTEN_BOOK:
        case BOOK_AND_QUILL:
            return true;
        default:
            return false;
        }
    }

    @Override
    public CraftMetaBookSigned clone() {
        CraftMetaBookSigned meta = (CraftMetaBookSigned) super.clone();
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        return original != hash ? CraftMetaBookSigned.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        return super.equalsCommon(meta);
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBookSigned || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }
}
