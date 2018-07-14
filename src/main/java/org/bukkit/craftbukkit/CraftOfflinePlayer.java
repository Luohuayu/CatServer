// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.plugin.Plugin;
import java.util.List;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.World;
import org.bukkit.Location;
import java.io.File;
import net.minecraft.nbt.NBTBase;
import org.bukkit.Bukkit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Date;
import org.bukkit.BanList;
import org.bukkit.Server;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.entity.Player;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.SaveHandler;
import com.mojang.authlib.GameProfile;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.OfflinePlayer;

@SerializableAs("Player")
public class CraftOfflinePlayer implements OfflinePlayer, ConfigurationSerializable
{
    private final GameProfile profile;
    private final CraftServer server;
    private final SaveHandler storage;
    
    protected CraftOfflinePlayer(final CraftServer server, final GameProfile profile) {
        this.server = server;
        this.profile = profile;
        this.storage = (SaveHandler)server.console/*worlds.get(0)*/.worldServers[0].getSaveHandler();
    }
    
    public GameProfile getProfile() {
        return this.profile;
    }
    
    @Override
    public boolean isOnline() {
        return this.getPlayer() != null;
    }
    
    @Override
    public String getName() {
        final Player player = this.getPlayer();
        if (player != null) {
            return player.getName();
        }
        if (this.profile.getName() != null) {
            return this.profile.getName();
        }
        final NBTTagCompound data = this.getBukkitData();
        if (data != null && data.hasKey("lastKnownName")) {
            return data.getString("lastKnownName");
        }
        return null;
    }
    
    @Override
    public UUID getUniqueId() {
        return this.profile.getId();
    }
    
    public Server getServer() {
        return this.server;
    }
    
    @Override
    public boolean isOp() {
        return this.server.getHandle().canSendCommands(this.profile);
    }
    
    @Override
    public void setOp(final boolean value) {
        if (value == this.isOp()) {
            return;
        }
        if (value) {
            this.server.getHandle().addOp(this.profile);
        }
        else {
            this.server.getHandle().removeOp(this.profile);
        }
    }
    
    @Override
    public boolean isBanned() {
        return this.getName() != null && this.server.getBanList(BanList.Type.NAME).isBanned(this.getName());
    }
    
    @Override
    public void setBanned(final boolean value) {
        if (this.getName() == null) {
            return;
        }
        if (value) {
            this.server.getBanList(BanList.Type.NAME).addBan(this.getName(), null, null, null);
        }
        else {
            this.server.getBanList(BanList.Type.NAME).pardon(this.getName());
        }
    }
    
    @Override
    public boolean isWhitelisted() {
        return this.server.getHandle().getWhitelistedPlayers().isWhitelisted(this.profile);
    }
    
    @Override
    public void setWhitelisted(final boolean value) {
        if (value) {
            this.server.getHandle().addWhitelistedPlayer(this.profile);
        }
        else {
            this.server.getHandle().removePlayerFromWhitelist(this.profile);
        }
    }
    
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("UUID", this.profile.getId().toString());
        return result;
    }
    
    public static OfflinePlayer deserialize(final Map<String, Object> args) {
        if (args.get("name") != null) {
            return Bukkit.getServer().getOfflinePlayer((String) args.get("name"));
        }
        return Bukkit.getServer().getOfflinePlayer(UUID.fromString((String) args.get("UUID")));
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getClass().getSimpleName()) + "[UUID=" + this.profile.getId() + "]";
    }
    
    @Override
    public Player getPlayer() {
        return this.server.getPlayer(this.getUniqueId());
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof OfflinePlayer)) {
            return false;
        }
        final OfflinePlayer other = (OfflinePlayer)obj;
        return this.getUniqueId() != null && other.getUniqueId() != null && this.getUniqueId().equals(other.getUniqueId());
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + ((this.getUniqueId() != null) ? this.getUniqueId().hashCode() : 0);
        return hash;
    }
    
    private NBTTagCompound getData() {
        return this.storage.getPlayerData(this.getUniqueId().toString());
    }
    
    private NBTTagCompound getBukkitData() {
        NBTTagCompound result = this.getData();
        if (result != null) {
            if (!result.hasKey("bukkit")) {
                result.setTag("bukkit", new NBTTagCompound());
            }
            result = result.getCompoundTag("bukkit");
        }
        return result;
    }
    
    private File getDataFile() {
        return new File(this.storage.getPlayerDir(), this.getUniqueId() + ".dat");
    }
    
    @Override
    public long getFirstPlayed() {
        final Player player = this.getPlayer();
        if (player != null) {
            return player.getFirstPlayed();
        }
        final NBTTagCompound data = this.getBukkitData();
        if (data == null) {
            return 0L;
        }
        if (data.hasKey("firstPlayed")) {
            return data.getLong("firstPlayed");
        }
        final File file = this.getDataFile();
        return file.lastModified();
    }
    
    @Override
    public long getLastPlayed() {
        final Player player = this.getPlayer();
        if (player != null) {
            return player.getLastPlayed();
        }
        final NBTTagCompound data = this.getBukkitData();
        if (data == null) {
            return 0L;
        }
        if (data.hasKey("lastPlayed")) {
            return data.getLong("lastPlayed");
        }
        final File file = this.getDataFile();
        return file.lastModified();
    }
    
    @Override
    public boolean hasPlayedBefore() {
        return this.getData() != null;
    }
    
    @Override
    public Location getBedSpawnLocation() {
        final NBTTagCompound data = this.getData();
        if (data == null) {
            return null;
        }
        if (data.hasKey("SpawnX") && data.hasKey("SpawnY") && data.hasKey("SpawnZ")) {
            String spawnWorld = data.getString("SpawnWorld");
            if (spawnWorld.equals("")) {
                spawnWorld = this.server.getWorlds().get(0).getName();
            }
            return new Location(this.server.getWorld(spawnWorld), data.getInteger("SpawnX"), data.getInteger("SpawnY"), data.getInteger("SpawnZ"));
        }
        return null;
    }
    
    public void setMetadata(final String metadataKey, final MetadataValue metadataValue) {
        (/*(MetadataStoreBase<CraftOfflinePlayer>)*/this.server.getPlayerMetadata()).setMetadata(this, metadataKey, metadataValue);
    }
    
    public List<MetadataValue> getMetadata(final String metadataKey) {
        return (/*(MetadataStoreBase<CraftOfflinePlayer>)*/this.server.getPlayerMetadata()).getMetadata(this, metadataKey);
    }
    
    public boolean hasMetadata(final String metadataKey) {
        return (/*(MetadataStoreBase<CraftOfflinePlayer>)*/this.server.getPlayerMetadata()).hasMetadata(this, metadataKey);
    }
    
    public void removeMetadata(final String metadataKey, final Plugin plugin) {
        (/*(MetadataStoreBase<CraftOfflinePlayer>)*/this.server.getPlayerMetadata()).removeMetadata(this, metadataKey, plugin);
    }
}
