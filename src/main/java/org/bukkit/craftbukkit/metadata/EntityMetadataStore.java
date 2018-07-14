// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.metadata;

import org.bukkit.metadata.MetadataStore;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataStoreBase;

public class EntityMetadataStore extends MetadataStoreBase<Entity> implements MetadataStore<Entity>
{
    @Override
    protected String disambiguate(final Entity entity, final String metadataKey) {
        return String.valueOf(entity.getUniqueId().toString()) + ":" + metadataKey;
    }
}
