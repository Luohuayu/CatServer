// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.metadata;

import java.util.Locale;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.OfflinePlayer;
import org.bukkit.metadata.MetadataStoreBase;

public class PlayerMetadataStore extends MetadataStoreBase<OfflinePlayer> implements MetadataStore<OfflinePlayer>
{
    @Override
    protected String disambiguate(final OfflinePlayer player, final String metadataKey) {
        return String.valueOf(player.getName().toLowerCase(Locale.ENGLISH)) + ":" + metadataKey;
    }
}
