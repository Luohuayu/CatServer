// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.metadata;

import org.bukkit.metadata.MetadataStore;
import org.bukkit.World;
import org.bukkit.metadata.MetadataStoreBase;

public class WorldMetadataStore extends MetadataStoreBase<World> implements MetadataStore<World>
{
    @Override
    protected String disambiguate(final World world, final String metadataKey) {
        return String.valueOf(world.getUID().toString()) + ":" + metadataKey;
    }
}
