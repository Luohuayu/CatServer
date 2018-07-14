// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import java.util.Map;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.BookMeta;

//@DelegateDeserialization(SerializableMeta.class)
class CraftMetaBookSigned extends CraftMetaBook implements BookMeta
{
    CraftMetaBookSigned(final CraftMetaItem meta) {
        super(meta);
    }
    
    CraftMetaBookSigned(final NBTTagCompound tag) {
        super(tag, false);
        boolean resolved = true;
        if (tag.hasKey(CraftMetaBookSigned.RESOLVED.NBT)) {
            resolved = tag.getBoolean(CraftMetaBookSigned.RESOLVED.NBT);
        }
        if (tag.hasKey(CraftMetaBookSigned.BOOK_PAGES.NBT)) {
            final NBTTagList pages = tag.getTagList(CraftMetaBookSigned.BOOK_PAGES.NBT, 8);
            for (int i = 0; i < pages.tagCount(); ++i) {
                final String page = pages.getStringTagAt(i);
                if (resolved) {
                    try {
                        this.pages.add(ITextComponent.Serializer.jsonToComponent(page));
                        continue;
                    }
                    catch (Exception ex) {}
                }
                this.addPage(page);
            }
        }
    }
    
    CraftMetaBookSigned(final Map<String, Object> map) {
        super(map);
    }
    
    @Override
    void applyToItem(final NBTTagCompound itemData) {
        super.applyToItem(itemData, false);
        if (this.hasTitle()) {
            itemData.setString(CraftMetaBookSigned.BOOK_TITLE.NBT, this.title);
        }
        else {
            itemData.setString(CraftMetaBookSigned.BOOK_TITLE.NBT, " ");
        }
        if (this.hasAuthor()) {
            itemData.setString(CraftMetaBookSigned.BOOK_AUTHOR.NBT, this.author);
        }
        else {
            itemData.setString(CraftMetaBookSigned.BOOK_AUTHOR.NBT, " ");
        }
        if (this.hasPages()) {
            final NBTTagList list = new NBTTagList();
            for (final ITextComponent page : this.pages) {
                list.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(page)));
            }
            itemData.setTag(CraftMetaBookSigned.BOOK_PAGES.NBT, list);
        }
        itemData.setBoolean(CraftMetaBookSigned.RESOLVED.NBT, true);
        if (this.generation != null) {
            itemData.setInteger(CraftMetaBookSigned.GENERATION.NBT, this.generation);
        }
        else {
            itemData.setInteger(CraftMetaBookSigned.GENERATION.NBT, 0);
        }
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty();
    }
    
    @Override
    boolean applicableTo(final Material type) {
        switch (type) {
            case BOOK_AND_QUILL:
            case WRITTEN_BOOK: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public CraftMetaBookSigned clone() {
        final CraftMetaBookSigned meta = (CraftMetaBookSigned)super.clone();
        return meta;
    }
    
    @Override
    int applyHash() {
        final int hash;
        final int original = hash = super.applyHash();
        return (original != hash) ? (CraftMetaBookSigned.class.hashCode() ^ hash) : hash;
    }
    
    @Override
    boolean equalsCommon(final CraftMetaItem meta) {
        return super.equalsCommon(meta);
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBookSigned || this.isBookEmpty());
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }
}
