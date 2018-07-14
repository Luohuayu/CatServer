// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import java.util.Set;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import com.google.common.collect.ImmutableMap;
import java.util.AbstractList;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import com.google.common.base.Strings;
import org.bukkit.Material;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.util.text.ITextComponent;
import java.util.List;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.BookMeta;

//@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBook extends CraftMetaItem implements BookMeta
{
    static final ItemMetaKey BOOK_TITLE;
    static final ItemMetaKey BOOK_AUTHOR;
    static final ItemMetaKey BOOK_PAGES;
    static final ItemMetaKey RESOLVED;
    static final ItemMetaKey GENERATION;
    static final int MAX_PAGE_LENGTH = 32767;
    static final int MAX_TITLE_LENGTH = 65535;
    protected String title;
    protected String author;
    public List<ITextComponent> pages;
    protected Integer generation;
    
    static {
        BOOK_TITLE = new ItemMetaKey("title");
        BOOK_AUTHOR = new ItemMetaKey("author");
        BOOK_PAGES = new ItemMetaKey("pages");
        RESOLVED = new ItemMetaKey("resolved");
        GENERATION = new ItemMetaKey("generation");
    }
    
    CraftMetaBook(final CraftMetaItem meta) {
        super(meta);
        this.pages = new ArrayList<ITextComponent>();
        if (meta instanceof CraftMetaBook) {
            final CraftMetaBook bookMeta = (CraftMetaBook)meta;
            this.title = bookMeta.title;
            this.author = bookMeta.author;
            this.pages.addAll(bookMeta.pages);
            this.generation = bookMeta.generation;
        }
    }
    
    CraftMetaBook(final NBTTagCompound tag) {
        this(tag, true);
    }
    
    CraftMetaBook(final NBTTagCompound tag, final boolean handlePages) {
        super(tag);
        this.pages = new ArrayList<ITextComponent>();
        if (tag.hasKey(CraftMetaBook.BOOK_TITLE.NBT)) {
            this.title = tag.getString(CraftMetaBook.BOOK_TITLE.NBT);
        }
        if (tag.hasKey(CraftMetaBook.BOOK_AUTHOR.NBT)) {
            this.author = tag.getString(CraftMetaBook.BOOK_AUTHOR.NBT);
        }
        boolean resolved = false;
        if (tag.hasKey(CraftMetaBook.RESOLVED.NBT)) {
            resolved = tag.getBoolean(CraftMetaBook.RESOLVED.NBT);
        }
        if (tag.hasKey(CraftMetaBook.GENERATION.NBT)) {
            this.generation = tag.getInteger(CraftMetaBook.GENERATION.NBT);
        }
        if (tag.hasKey(CraftMetaBook.BOOK_PAGES.NBT) && handlePages) {
            final NBTTagList pages = tag.getTagList(CraftMetaBook.BOOK_PAGES.NBT, 8);
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
    
    CraftMetaBook(final Map<String, Object> map) {
        super(map);
        this.pages = new ArrayList<ITextComponent>();
        this.setAuthor(SerializableMeta.getString(map, CraftMetaBook.BOOK_AUTHOR.BUKKIT, true));
        this.setTitle(SerializableMeta.getString(map, CraftMetaBook.BOOK_TITLE.BUKKIT, true));
        final Iterable<?> pages = SerializableMeta.getObject(/*(Class<Iterable<?>>)*/Iterable.class, map, CraftMetaBook.BOOK_PAGES.BUKKIT, true);
        if (pages != null) {
            for (final Object page : pages) {
                if (page instanceof String) {
                    this.addPage((String)page);
                }
            }
        }
        this.generation = SerializableMeta.getObject(Integer.class, map, CraftMetaBook.GENERATION.BUKKIT, true);
    }
    
    @Override
    void applyToItem(final NBTTagCompound itemData) {
        this.applyToItem(itemData, true);
    }
    
    void applyToItem(final NBTTagCompound itemData, final boolean handlePages) {
        super.applyToItem(itemData);
        if (this.hasTitle()) {
            itemData.setString(CraftMetaBook.BOOK_TITLE.NBT, this.title);
        }
        if (this.hasAuthor()) {
            itemData.setString(CraftMetaBook.BOOK_AUTHOR.NBT, this.author);
        }
        if (handlePages) {
            if (this.hasPages()) {
                final NBTTagList list = new NBTTagList();
                for (final ITextComponent page : this.pages) {
                    list.appendTag(new NBTTagString(CraftChatMessage.fromComponent(page)));
                }
                itemData.setTag(CraftMetaBook.BOOK_PAGES.NBT, list);
            }
            itemData.removeTag(CraftMetaBook.RESOLVED.NBT);
        }
        if (this.generation != null) {
            itemData.setInteger(CraftMetaBook.GENERATION.NBT, this.generation);
        }
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBookEmpty();
    }
    
    boolean isBookEmpty() {
        return !this.hasPages() && !this.hasAuthor() && !this.hasTitle();
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
    public boolean hasAuthor() {
        return !Strings.isNullOrEmpty(this.author);
    }
    
    @Override
    public boolean hasTitle() {
        return !Strings.isNullOrEmpty(this.title);
    }
    
    @Override
    public boolean hasPages() {
        return !this.pages.isEmpty();
    }
    
    @Override
    public boolean hasGeneration() {
        return this.generation != null;
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public boolean setTitle(final String title) {
        if (title == null) {
            this.title = null;
            return true;
        }
        if (title.length() > 65535) {
            return false;
        }
        this.title = title;
        return true;
    }
    
    @Override
    public String getAuthor() {
        return this.author;
    }
    
    @Override
    public void setAuthor(final String author) {
        this.author = author;
    }
    
    @Override
    public Generation getGeneration() {
        return (this.generation == null) ? null : Generation.values()[this.generation];
    }
    
    @Override
    public void setGeneration(final Generation generation) {
        this.generation = ((generation == null) ? null : generation.ordinal());
    }
    
    @Override
    public String getPage(final int page) {
        Validate.isTrue(this.isValidPage(page), "Invalid page number");
        return CraftChatMessage.fromComponent(this.pages.get(page - 1));
    }
    
    @Override
    public void setPage(final int page, final String text) {
        if (!this.isValidPage(page)) {
            throw new IllegalArgumentException("Invalid page number " + page + "/" + this.pages.size());
        }
        final String newText = (text == null) ? "" : ((text.length() > 32767) ? text.substring(0, 32767) : text);
        this.pages.set(page - 1, CraftChatMessage.fromString(newText, true)[0]);
    }
    
    @Override
    public void setPages(final String... pages) {
        this.pages.clear();
        this.addPage(pages);
    }
    
    @Override
    public void addPage(final String... pages) {
        for (String page : pages) {
            if (page == null) {
                page = "";
            }
            else if (page.length() > 32767) {
                page = page.substring(0, 32767);
            }
            this.pages.add(CraftChatMessage.fromString(page, true)[0]);
        }
    }
    
    @Override
    public int getPageCount() {
        return this.pages.size();
    }
    
    @Override
    public List<String> getPages() {
        final List<ITextComponent> copy = (List<ITextComponent>)ImmutableList.copyOf((Collection)this.pages);
        return new AbstractList<String>() {
            @Override
            public String get(final int index) {
                return CraftChatMessage.fromComponent(copy.get(index));
            }
            
            @Override
            public int size() {
                return copy.size();
            }
        };
    }
    
    @Override
    public void setPages(final List<String> pages) {
        this.pages.clear();
        for (final String page : pages) {
            this.addPage(page);
        }
    }
    
    private boolean isValidPage(final int page) {
        return page > 0 && page <= this.pages.size();
    }
    
    @Override
    public CraftMetaBook clone() {
        final CraftMetaBook meta = (CraftMetaBook)super.clone();
        meta.pages = new ArrayList<ITextComponent>(this.pages);
        return meta;
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.hasTitle()) {
            hash = 61 * hash + this.title.hashCode();
        }
        if (this.hasAuthor()) {
            hash = 61 * hash + 13 * this.author.hashCode();
        }
        if (this.hasPages()) {
            hash = 61 * hash + 17 * this.pages.hashCode();
        }
        if (this.hasGeneration()) {
            hash = 61 * hash + 19 * this.generation.hashCode();
        }
        return (original != hash) ? (CraftMetaBook.class.hashCode() ^ hash) : hash;
    }
    
    @Override
    boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBook) {
            final CraftMetaBook that = (CraftMetaBook)meta;
            if (this.hasTitle()) {
                if (!that.hasTitle() || !this.title.equals(that.title)) {
                    return false;
                }
            }
            else if (that.hasTitle()) {
                return false;
            }
            if (this.hasAuthor()) {
                if (!that.hasAuthor() || !this.author.equals(that.author)) {
                    return false;
                }
            }
            else if (that.hasAuthor()) {
                return false;
            }
            if (this.hasPages()) {
                if (!that.hasPages() || !this.pages.equals(that.pages)) {
                    return false;
                }
            }
            else if (that.hasPages()) {
                return false;
            }
            if (this.hasGeneration()) {
                if (!that.hasGeneration() || !this.generation.equals(that.generation)) {
                    return false;
                }
            }
            else if (that.hasGeneration()) {
                return false;
            }
            return true;
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBook || this.isBookEmpty());
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasTitle()) {
            builder.put(CraftMetaBook.BOOK_TITLE.BUKKIT, (Object)this.title);
        }
        if (this.hasAuthor()) {
            builder.put(CraftMetaBook.BOOK_AUTHOR.BUKKIT, (Object)this.author);
        }
        if (this.hasPages()) {
            final List<String> pagesString = new ArrayList<String>();
            for (final ITextComponent comp : this.pages) {
                pagesString.add(CraftChatMessage.fromComponent(comp));
            }
            builder.put(CraftMetaBook.BOOK_PAGES.BUKKIT, (Object)pagesString);
        }
        if (this.generation != null) {
            builder.put(CraftMetaBook.GENERATION.BUKKIT, (Object)this.generation);
        }
        return builder;
    }
}
