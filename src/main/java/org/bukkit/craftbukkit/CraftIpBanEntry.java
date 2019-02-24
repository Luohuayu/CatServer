package org.bukkit.craftbukkit;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

import net.minecraft.server.management.UserListIPBans;
import net.minecraft.server.management.UserListIPBansEntry;
import org.bukkit.Bukkit;

public final class CraftIpBanEntry implements org.bukkit.BanEntry {
    private final UserListIPBans list;
    private final String target;
    private Date created;
    private String source;
    private Date expiration;
    private String reason;

    public CraftIpBanEntry(String target, UserListIPBansEntry entry, UserListIPBans list) {
        this.list = list;
        this.target = target;
        this.created = entry.getCreated() != null ? new Date(entry.getCreated().getTime()) : null;
        this.source = entry.getSource();
        this.expiration = entry.getBanEndDate() != null ? new Date(entry.getBanEndDate().getTime()) : null;
        this.reason = entry.getBanReason();
    }

    @Override
    public String getTarget() {
        return this.target;
    }

    @Override
    public Date getCreated() {
        return this.created == null ? null : (Date) this.created.clone();
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public Date getExpiration() {
        return this.expiration == null ? null : (Date) this.expiration.clone();
    }

    @Override
    public void setExpiration(Date expiration) {
        if (expiration != null && expiration.getTime() == new Date(0, 0, 0, 0, 0, 0).getTime()) {
            expiration = null; // Forces "forever"
        }

        this.expiration = expiration;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void save() {
        UserListIPBansEntry entry = new UserListIPBansEntry(target, this.created, this.source, this.expiration, this.reason);
        this.list.addEntry(entry);
        try {
            this.list.writeChanges();
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-ips.json, {0}", ex.getMessage());
        }
    }
}
