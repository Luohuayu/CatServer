// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import net.minecraft.server.management.UserList;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.apache.commons.lang.StringUtils;
import java.util.Date;
import net.minecraft.server.management.UserListIPBansEntry;
import org.apache.commons.lang.Validate;
import org.bukkit.BanEntry;
import net.minecraft.server.management.UserListIPBans;
import org.bukkit.BanList;

public class CraftIpBanList implements BanList
{
    private final UserListIPBans list;
    
    public CraftIpBanList(final UserListIPBans list) {
        this.list = list;
    }
    
    @Override
    public BanEntry getBanEntry(final String target) {
        Validate.notNull((Object)target, "Target cannot be null");
        final UserListIPBansEntry entry = this.list.getEntry(target);
        if (entry == null) {
            return null;
        }
        return new CraftIpBanEntry(target, entry, this.list);
    }
    
    @Override
    public BanEntry addBan(final String target, final String reason, final Date expires, final String source) {
        Validate.notNull((Object)target, "Ban target cannot be null");
        final UserListIPBansEntry entry = new UserListIPBansEntry(target, new Date(), StringUtils.isBlank(source) ? null : source, expires, StringUtils.isBlank(reason) ? null : reason);
        (/*(UserList<K, UserListIPBansEntry>)*/this.list).addEntry(entry);
        try {
            this.list.writeChanges();
        }
        catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-ips.json, {0}", ex.getMessage());
        }
        return new CraftIpBanEntry(target, entry, this.list);
    }
    
    @Override
    public Set<BanEntry> getBanEntries() {
        final ImmutableSet.Builder<BanEntry> builder = ImmutableSet.builder();
        String[] keys;
        for (int length = (keys = this.list.getKeys()).length, i = 0; i < length; ++i) {
            final String target = keys[i];
            builder.add((BanEntry)new CraftIpBanEntry(target, this.list.getEntry(target), this.list));
        }
        return (Set<BanEntry>)builder.build();
    }
    
    @Override
    public boolean isBanned(final String target) {
        Validate.notNull((Object)target, "Target cannot be null");
        return this.list.isBanned(InetSocketAddress.createUnresolved(target, 0));
    }
    
    @Override
    public void pardon(final String target) {
        Validate.notNull((Object)target, "Target cannot be null");
        (/*(UserList<String, V>)*/this.list).removeEntry(target);
    }
}
