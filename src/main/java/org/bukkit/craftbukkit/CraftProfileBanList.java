// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListEntry;
import java.util.Iterator;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.apache.commons.lang.StringUtils;
import java.util.Date;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.Validate;
import org.bukkit.BanEntry;
import net.minecraft.server.management.UserListBans;
import org.bukkit.BanList;

public class CraftProfileBanList implements BanList
{
    private final UserListBans list;
    
    public CraftProfileBanList(final UserListBans list) {
        this.list = list;
    }
    
    @Override
    public BanEntry getBanEntry(final String target) {
        Validate.notNull((Object)target, "Target cannot be null");
        final GameProfile profile = MinecraftServer.getServerInst().getPlayerProfileCache().getGameProfileForUsername(target);
        if (profile == null) {
            return null;
        }
        final UserListBansEntry entry = this.list.getEntry(profile);
        if (entry == null) {
            return null;
        }
        return new CraftProfileBanEntry(profile, entry, this.list);
    }
    
    @Override
    public BanEntry addBan(final String target, final String reason, final Date expires, final String source) {
        Validate.notNull((Object)target, "Ban target cannot be null");
        final GameProfile profile = MinecraftServer.getServerInst().getPlayerProfileCache().getGameProfileForUsername(target);
        if (profile == null) {
            return null;
        }
        final UserListBansEntry entry = new UserListBansEntry(profile, new Date(), StringUtils.isBlank(source) ? null : source, expires, StringUtils.isBlank(reason) ? null : reason);
        (/*(UserList<K, UserListBansEntry>)*/this.list).addEntry(entry);
        try {
            this.list.writeChanges();
        }
        catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-players.json, {0}", ex.getMessage());
        }
        return new CraftProfileBanEntry(profile, entry, this.list);
    }
    
    @Override
    public Set<BanEntry> getBanEntries() {
        final ImmutableSet.Builder<BanEntry> builder = ImmutableSet.builder();
        for (final UserListEntry entry : (/*(UserList<K, UserListBansEntry>)*/this.list).getValuesCB()) {
            final GameProfile profile = (GameProfile) entry.getValue();
            builder.add(new CraftProfileBanEntry(profile, (UserListBansEntry)entry, this.list));
        }
        return (Set<BanEntry>)builder.build();
    }
    
    @Override
    public boolean isBanned(final String target) {
        Validate.notNull((Object)target, "Target cannot be null");
        final GameProfile profile = MinecraftServer.getServerInst().getPlayerProfileCache().getGameProfileForUsername(target);
        return profile != null && this.list.isBanned(profile);
    }
    
    @Override
    public void pardon(final String target) {
        Validate.notNull((Object)target, "Target cannot be null");
        final GameProfile profile = MinecraftServer.getServerInst().getPlayerProfileCache().getGameProfileForUsername(target);
        (/*(UserList<GameProfile, V>)*/this.list).removeEntry(profile);
    }
}
