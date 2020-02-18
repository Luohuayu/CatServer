package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ConcurrentSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapDecoration;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.*;
import org.bukkit.Achievement;
import org.bukkit.BanList;
import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.Statistic.Type;
import org.bukkit.World;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.craftbukkit.CraftEffect;
import org.bukkit.craftbukkit.CraftOfflinePlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.advancement.CraftAdvancement;
import org.bukkit.craftbukkit.advancement.CraftAdvancementProgress;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.map.RenderData;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nullable;

import catserver.server.entity.CraftFakePlayer;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new ConcurrentSet<String>(); // HashSet - > ConcurrentSet
    private final Map<UUID, Set<WeakReference<Plugin>>> hiddenPlayers = new HashMap<>();
    private static final WeakHashMap<Plugin, WeakReference<Plugin>> pluginWeakReferences = new WeakHashMap<>();
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;

    public CraftPlayer(CraftServer server, EntityPlayerMP entity) {
        super(server, entity);

        firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return getHandle().getGameProfile();
    }

    @Override
    public boolean isOp() {
        return server.getHandle().canSendCommands(getProfile());
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().addOp(getProfile());
        } else {
            server.getHandle().removeOp(getProfile());
        }

        perm.recalculatePermissions();
    }

    public boolean isOnline() {
        return server.getPlayer(getUniqueId()) != null;
    }

    public InetSocketAddress getAddress() {
        if (getHandle().connection == null) return null;

        SocketAddress addr = getHandle().connection.netManager.getRemoteAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        if (ignorePose) {
            return 1.62D;
        } else {
            return getEyeHeight();
        }
    }

    @Override
    public void sendRawMessage(String message) {
        if (getHandle().connection == null) return;

        for (ITextComponent component : CraftChatMessage.fromString(message)) {
            getHandle().connection.sendPacket(new SPacketChat(component));
        }
    }

    @Override
    public void sendMessage(String message) {
        if (!conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getDisplayName() {
        return getHandle().displayName;
    }

    @Override
    public void setDisplayName(final String name) {
        getHandle().displayName = name == null ? getName() : name;
    }

    @Override
    public String getPlayerListName() {
        return getHandle().listName == null ? getName() : CraftChatMessage.fromComponent(getHandle().listName, TextFormatting.WHITE);
    }

    @Override
    public void setPlayerListName(String name) {
        if (name == null) {
            name = getName();
        }
        getHandle().listName = name.equals(getName()) ? null : CraftChatMessage.fromString(name)[0];
        for (EntityPlayerMP player : server.getHandle().getPlayers()) {
            if (player.getBukkitEntity().canSee(this)) {
                player.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_DISPLAY_NAME, getHandle()));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        OfflinePlayer other = (OfflinePlayer) obj;
        if ((this.getUniqueId() == null) || (other.getUniqueId() == null)) {
            return false;
        }

        boolean uuidEquals = this.getUniqueId().equals(other.getUniqueId());
        boolean idEquals = true;

        if (other instanceof CraftPlayer) {
            idEquals = this.getEntityId() == ((CraftPlayer) other).getEntityId();
        }

        return uuidEquals && idEquals;
    }

    @Override
    public void kickPlayer(String message) {
        org.spigotmc.AsyncCatcher.catchOp( "player kick"); // Spigot
        if (getHandle().connection == null) return;

        getHandle().connection.disconnect(message == null ? "" : message);
    }

    @Override
    public void setCompassTarget(Location loc) {
        if (getHandle().connection == null) return;

        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().connection.sendPacket(new SPacketSpawnPosition(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
    }

    @Override
    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    @Override
    public void chat(String msg) {
        if (getHandle().connection == null) return;

        getHandle().connection.chat(msg, false);
    }

    @Override
    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        if (getHandle().connection == null) return;

        String instrumentName = null;
        switch (instrument) {
        case 0:
            instrumentName = "harp";
            break;
        case 1:
            instrumentName = "basedrum";
            break;
        case 2:
            instrumentName = "snare";
            break;
        case 3:
            instrumentName = "hat";
            break;
        case 4:
            instrumentName = "bass";
            break;
        case 5:
            instrumentName = "flute";
            break;
        case 6:
            instrumentName = "bell";
            break;
        case 7:
            instrumentName = "guitar";
            break;
        case 8:
            instrumentName = "chime";
            break;
        case 9:
            instrumentName = "xylophone";
            break;
        }

        float f = (float) Math.pow(2.0D, (note - 12.0D) / 12.0D);
        getHandle().connection.sendPacket(new SPacketSoundEffect(CraftSound.getSoundEffect("block.note." + instrumentName),net.minecraft.util.SoundCategory.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        if (getHandle().connection == null) return;

        String instrumentName = null;
        switch (instrument.ordinal()) {
            case 0:
                instrumentName = "harp";
                break;
            case 1:
                instrumentName = "basedrum";
                break;
            case 2:
                instrumentName = "snare";
                break;
            case 3:
                instrumentName = "hat";
                break;
            case 4:
                instrumentName = "bass";
                break;
            case 5:
                instrumentName = "flute";
                break;
            case 6:
                instrumentName = "bell";
                break;
            case 7:
                instrumentName = "guitar";
                break;
            case 8:
                instrumentName = "chime";
                break;
            case 9:
                instrumentName = "xylophone";
                break;
        }
        float f = (float) Math.pow(2.0D, (note.getId() - 12.0D) / 12.0D);
        getHandle().connection.sendPacket(new SPacketSoundEffect(CraftSound.getSoundEffect("block.note." + instrumentName), net.minecraft.util.SoundCategory.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null || getHandle().connection == null) return;

        SPacketSoundEffect packet = new SPacketSoundEffect(CraftSound.getSoundEffect(CraftSound.getSound(sound)), net.minecraft.util.SoundCategory.valueOf(category.name()), loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        getHandle().connection.sendPacket(packet);
    }

    @Override
    public void playSound(Location loc, String sound, SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null || getHandle().connection == null) return;

        SPacketCustomSound packet = new SPacketCustomSound(sound, net.minecraft.util.SoundCategory.valueOf(category.name()), loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        getHandle().connection.sendPacket(packet);
    }

    @Override
    public void stopSound(Sound sound) {
        stopSound(sound, null);
    }

    @Override
    public void stopSound(String sound) {
        stopSound(sound, null);
    }

    @Override
    public void stopSound(Sound sound, SoundCategory category) {
        stopSound(CraftSound.getSound(sound), category);
    }

    @Override
    public void stopSound(String sound, SoundCategory category) {
        if (getHandle().connection == null) return;
        PacketBuffer packetdataserializer = new PacketBuffer(Unpooled.buffer());

        packetdataserializer.writeString(category == null ? "" : net.minecraft.util.SoundCategory.valueOf(category.name()).getName());
        packetdataserializer.writeString(sound);
        getHandle().connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetdataserializer));
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        if (getHandle().connection == null) return;
        spigot().playEffect(loc, effect, data, 0, 0, 0, 0, 1, 1, 64); // Spigot
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue);
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        sendBlockChange(loc, material.getId(), data);
    }

    @Override
    public void sendBlockChange(Location loc, int material, byte data) {
        if (getHandle().connection == null) return;

        SPacketBlockChange packet = new SPacketBlockChange(((CraftWorld) loc.getWorld()).getHandle(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

        packet.blockState = CraftMagicNumbers.getBlock(material).getStateFromMeta(data);
        getHandle().connection.sendPacket(packet);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) {
        if (getHandle().connection == null) {
            return;
        }

        if (lines == null) {
            lines = new String[4];
        }

        Validate.notNull(loc, "Location can not be null");
        if (lines.length < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }

        ITextComponent[] components = CraftSign.sanitizeLines(lines);
        TileEntitySign sign = new TileEntitySign();
        sign.setPos(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        System.arraycopy(components, 0, sign.signText, 0, sign.signText.length);

        getHandle().connection.sendPacket(sign.getUpdatePacket());
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (getHandle().connection == null) return false;

        /*
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        int cx = x >> 4;
        int cz = z >> 4;

        if (sx <= 0 || sy <= 0 || sz <= 0) {
            return false;
        }

        if ((x + sx - 1) >> 4 != cx || (z + sz - 1) >> 4 != cz || y < 0 || y + sy > 128) {
            return false;
        }

        if (data.length != (sx * sy * sz * 5) / 2) {
            return false;
        }

        Packet51MapChunk packet = new Packet51MapChunk(x, y, z, sx, sy, sz, data);

        getHandle().connection.sendPacket(packet);

        return true;
        */

        throw new NotImplementedException("Chunk changes do not yet work"); // TODO: Chunk changes.
    }

    @Override
    public void sendMap(MapView map) {
        if (getHandle().connection == null) return;

        RenderData data = ((CraftMapView) map).render(this);
        Collection<MapDecoration> icons = new ArrayList<>();
        for (MapCursor cursor : data.cursors) {
            if (cursor.isVisible()) {
                icons.add(new MapDecoration(MapDecoration.Type.byIcon(cursor.getRawType()), cursor.getX(), cursor.getY(), cursor.getDirection()));
            }
        }

        SPacketMaps packet = new SPacketMaps(map.getId(), map.getScale().getValue(), true, icons, data.buffer, 0, 0, 128, 128);
        getHandle().connection.sendPacket(packet);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location");
        Preconditions.checkArgument(location.getWorld() != null, "location.world");
        location.checkFinite();

        EntityPlayerMP entity = getHandle();

        if (getHealth() == 0 || entity.isDead) {
            return false;
        }

        if (entity.connection == null) {
           return false;
        }

        if (entity.isBeingRidden()) {
            return false;
        }

        // From = Players current Location
        Location from = this.getLocation();
        // To = Players new Location if Teleport is Successful
        Location to = location;
        // Create & Call the Teleport Event.
        PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to, cause);
        server.getPluginManager().callEvent(event);

        // Return False to inform the Plugin that the Teleport was unsuccessful/cancelled.
        if (event.isCancelled()) {
            return false;
        }

        // If this player is riding another entity, we must dismount before teleporting.
        entity.dismountRidingEntity();

        // Update the From Location
        from = event.getFrom();
        // Grab the new To Location dependent on whether the event was cancelled.
        to = event.getTo();
        // Grab the To and From World Handles.
        WorldServer fromWorld = ((CraftWorld) from.getWorld()).getHandle();
        WorldServer toWorld = ((CraftWorld) to.getWorld()).getHandle();

        // Close any foreign inventory
        if (getHandle().openContainer != getHandle().inventoryContainer) {
            getHandle().closeScreen();
        }

        // Check if the fromWorld and toWorld are the same.
        if (fromWorld == toWorld) {
            entity.connection.teleport(to);
        } else {
            server.getHandle().moveToWorld(entity, toWorld.dimension, true, to, true);
        }
        return true;
    }

    @Override
    public void setSneaking(boolean sneak) {
        getHandle().setSneaking(sneak);
    }

    @Override
    public boolean isSneaking() {
        return getHandle().isSneaking();
    }

    @Override
    public boolean isSprinting() {
        return getHandle().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getHandle().setSprinting(sprinting);
    }

    @Override
    public void loadData() {
        server.getHandle().playerDataManager.readPlayerData(getHandle());
    }

    @Override
    public void saveData() {
        server.getHandle().playerDataManager.writePlayerData(getHandle());
    }

    @Deprecated
    @Override
    public void updateInventory() {
        if (getHandle().connection == null) return;
        getHandle().sendContainerToPlayer(getHandle().openContainer);
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        getHandle().fauxSleeping = isSleeping;
        ((CraftWorld) getWorld()).getHandle().checkSleepStatus();
    }

    @Override
    public boolean isSleepingIgnored() {
        return getHandle().fauxSleeping;
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version.");
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version.");
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version.");
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        incrementStatistic(statistic, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        decrementStatistic(statistic, 1);
    }

    @Override
    public int getStatistic(Statistic statistic) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Type.UNTYPED, "Must supply additional paramater for this statistic");
        return getHandle().getStatFile().readStat(CraftStatistic.getNMSStatistic(statistic));
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, getStatistic(statistic) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, getStatistic(statistic) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Type.UNTYPED, "Must supply additional paramater for this statistic");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getNMSStatistic(statistic);
        getHandle().getStatFile().unlockAchievement(getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        incrementStatistic(statistic, material, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        decrementStatistic(statistic, material, 1);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        return getHandle().getStatFile().readStat(nmsStatistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, material, getStatistic(statistic, material) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, material, getStatistic(statistic, material) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        getHandle().getStatFile().unlockAchievement(getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        incrementStatistic(statistic, entityType, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        decrementStatistic(statistic, entityType, 1);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        return getHandle().getStatFile().readStat(nmsStatistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, entityType, getStatistic(statistic, entityType) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, entityType, getStatistic(statistic, entityType) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        getHandle().getStatFile().unlockAchievement(getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        getHandle().timeOffset = time;
        getHandle().relativeTime = relative;
    }

    @Override
    public long getPlayerTimeOffset() {
        return getHandle().timeOffset;
    }

    @Override
    public long getPlayerTime() {
        return getHandle().getPlayerTime();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return getHandle().relativeTime;
    }

    @Override
    public void resetPlayerTime() {
        setPlayerTime(0, true);
    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        getHandle().setPlayerWeather(type, true);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return getHandle().getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather() {
        getHandle().resetPlayerWeather();
    }

    @Override
    public boolean isBanned() {
        return server.getBanList(BanList.Type.NAME).isBanned(getName());
    }

    @Override
    public boolean isWhitelisted() {
        return server.getHandle().getWhitelistedPlayers().isWhitelisted(getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().addWhitelistedPlayer(getProfile());
        } else {
            server.getHandle().removePlayerFromWhitelist(getProfile());
        }
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (getHandle().connection == null) return;

        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        getHandle().setGameType(GameType.getByID(mode.getValue()));
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(getHandle().interactionManager.getGameType().getID());
    }

    @Override
    public void giveExp(int exp) {
        getHandle().addExperience(exp);
    }

    @Override
    public void giveExpLevels(int levels) {
        getHandle().addExperienceLevel(levels);
    }

    @Override
    public float getExp() {
        return getHandle().experience;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        getHandle().experience = exp;
        getHandle().lastExperience = -1;
    }

    @Override
    public int getLevel() {
        return getHandle().experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        getHandle().experienceLevel = level;
        getHandle().lastExperience = -1;
    }

    @Override
    public int getTotalExperience() {
        return getHandle().experienceTotal;
    }

    @Override
    public void setTotalExperience(int exp) {
        getHandle().experienceTotal = exp;
    }

    @Override
    public float getExhaustion() {
        return getHandle().getFoodStats().foodExhaustionLevel;
    }

    @Override
    public void setExhaustion(float value) {
        getHandle().getFoodStats().foodExhaustionLevel = value;
    }

    @Override
    public float getSaturation() {
        return getHandle().getFoodStats().foodSaturationLevel;
    }

    @Override
    public void setSaturation(float value) {
        getHandle().getFoodStats().foodSaturationLevel = value;
    }

    @Override
    public int getFoodLevel() {
        return getHandle().getFoodStats().foodLevel;
    }

    @Override
    public void setFoodLevel(int value) {
        getHandle().getFoodStats().foodLevel = value;
    }

    @Override
    public Location getBedSpawnLocation() {
        World world = getServer().getWorld(getHandle().spawnWorld);
        BlockPos bed = getHandle().getBedLocation();

        if (world != null && bed != null) {
            bed = EntityPlayer.getBedSpawnLocation(((CraftWorld) world).getHandle(), bed, getHandle().isSpawnForced());
            if (bed != null) {
                return new Location(world, bed.getX(), bed.getY(), bed.getZ());
            }
        }
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        setBedSpawnLocation(location, false);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean override) {
        if (location == null) {
            getHandle().setSpawnPoint(null, override);
        } else {
            getHandle().setSpawnPoint(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), override);
            getHandle().spawnWorld = location.getWorld().getName();
        }
    }

    @Nullable
    private static WeakReference<Plugin> getPluginWeakReference(@Nullable Plugin plugin) {
        return (plugin == null) ? null : pluginWeakReferences.computeIfAbsent(plugin, WeakReference::new);
    }

    @Override
    @Deprecated
    public void hidePlayer(Player player) {
        hidePlayer0(null, player);
    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        Validate.notNull(plugin, "Plugin cannot be null");
        Validate.isTrue(plugin.isEnabled(), "Plugin attempted to hide player while disabled");

        hidePlayer0(plugin, player);
    }

    private void hidePlayer0(@Nullable Plugin plugin, Player player) {
        Validate.notNull(player, "hidden player cannot be null");
        if (getHandle().connection == null) return;
        if (equals(player)) return;

        Set<WeakReference<Plugin>> hidingPlugins = hiddenPlayers.get(player.getUniqueId());
        if (hidingPlugins != null) {
            // Some plugins are already hiding the player. Just mark that this
            // plugin wants the player hidden too and end.
            hidingPlugins.add(getPluginWeakReference(plugin));
            return;
        }
        hidingPlugins = new HashSet<>();
        hidingPlugins.add(getPluginWeakReference(plugin));
        hiddenPlayers.put(player.getUniqueId(), hidingPlugins);

        // Remove this player from the hidden player's EntityTrackerEntry
        EntityTracker tracker = ((WorldServer) entity.world).entityTracker;
        EntityPlayerMP other = ((CraftPlayer) player).getHandle();
        EntityTrackerEntry entry = tracker.trackedEntityHashTable.lookup(other.getEntityId());
        if (entry != null) {
            entry.removeTrackedPlayerSymmetric(getHandle());
        }

        // Remove the hidden player from this player user list, if they're on it
        if (other.sentListPacket) {
            getHandle().connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, other));
        }
    }

    @Override
    @Deprecated
    public void showPlayer(Player player) {
        showPlayer0(null, player);
    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        Validate.notNull(plugin, "Plugin cannot be null");
        // Don't require that plugin be enabled. A plugin must be allowed to call
        // showPlayer during its onDisable() method.
        showPlayer0(plugin, player);
    }

    private void showPlayer0(@Nullable Plugin plugin, Player player) {
        Validate.notNull(player, "shown player cannot be null");
        if (getHandle().connection == null) return;
        if (equals(player)) return;

        Set<WeakReference<Plugin>> hidingPlugins = hiddenPlayers.get(player.getUniqueId());
        if (hidingPlugins == null) {
            return; // Player isn't hidden
        }
        hidingPlugins.remove(getPluginWeakReference(plugin));
        if (!hidingPlugins.isEmpty()) {
            return; // Some other plugins still want the player hidden
        }
        hiddenPlayers.remove(player.getUniqueId());

        EntityTracker tracker = ((WorldServer) entity.world).entityTracker;
        EntityPlayerMP other = ((CraftPlayer) player).getHandle();

        getHandle().connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, other));

        EntityTrackerEntry entry = tracker.trackedEntityHashTable.lookup(other.getEntityId());
        if (entry != null && !entry.trackingPlayers.contains(getHandle())) {
            entry.updatePlayerEntity(getHandle());
        }
    }

    public void removeDisconnectingPlayer(Player player) {
        hiddenPlayers.remove(player.getUniqueId());
    }

    @Override
    public boolean canSee(Player player) {
        return !hiddenPlayers.containsKey(player.getUniqueId());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("name", getName());

        return result;
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public EntityPlayerMP getHandle() {
        return (EntityPlayerMP) entity;
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public int hashCode() {
        if (hash == 0 || hash == 485) {
            hash = 97 * 5 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }
        return hash;
    }

    @Override
    public long getFirstPlayed() {
        return firstPlayed;
    }

    @Override
    public long getLastPlayed() {
        return lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public void readExtraData(NBTTagCompound nbttagcompound) {
        hasPlayedBefore = true;
        if (nbttagcompound.hasKey("bukkit")) {
            NBTTagCompound data = nbttagcompound.getCompoundTag("bukkit");

            if (data.hasKey("firstPlayed")) {
                firstPlayed = data.getLong("firstPlayed");
                lastPlayed = data.getLong("lastPlayed");
            }

            if (data.hasKey("newExp")) {
                EntityPlayerMP handle = getHandle();
                handle.newExp = data.getInteger("newExp");
                handle.newTotalExp = data.getInteger("newTotalExp");
                handle.newLevel = data.getInteger("newLevel");
                handle.expToDrop = data.getInteger("expToDrop");
                handle.keepLevel = data.getBoolean("keepLevel");
            }
        }
    }

    public void setExtraData(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("bukkit")) {
            nbttagcompound.setTag("bukkit", new NBTTagCompound());
        }

        NBTTagCompound data = nbttagcompound.getCompoundTag("bukkit");
        EntityPlayerMP handle = getHandle();
        data.setInteger("newExp", handle.newExp);
        data.setInteger("newTotalExp", handle.newTotalExp);
        data.setInteger("newLevel", handle.newLevel);
        data.setInteger("expToDrop", handle.expToDrop);
        data.setBoolean("keepLevel", handle.keepLevel);
        data.setLong("firstPlayed", getFirstPlayed());
        data.setLong("lastPlayed", System.currentTimeMillis());
        data.setString("lastKnownName", handle.getName());
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return conversationTracker.isConversing();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);
        if (getHandle().connection == null) return;

        if (channels.contains(channel) || org.spigotmc.SpigotConfig.bungee) { // CatServer - force send pluginmessage when bungeecord mode enable
            SPacketCustomPayload packet = new SPacketCustomPayload(channel, new PacketBuffer(Unpooled.wrappedBuffer(message)));
            getHandle().connection.sendPacket(packet);
        }
    }

    @Override
    public void setTexturePack(String url) {
        setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        Validate.notNull(url, "Resource pack URL cannot be null");

        getHandle().loadResourcePack(url, "null");
    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        Validate.notNull(url, "Resource pack URL cannot be null");
        Validate.notNull(hash, "Resource pack hash cannot be null");
        Validate.isTrue(hash.length == 20, "Resource pack hash should be 20 bytes long but was " + hash.length);

        getHandle().loadResourcePack(url, BaseEncoding.base16().lowerCase().encode(hash));
    }

    public void addChannel(String channel) {
        if (channels.add(channel)) {
            server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        if (channels.remove(channel)) {
            server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    public void sendSupportedChannels() {
        if (getHandle().connection == null) return;
        Set<String> listening = server.getMessenger().getIncomingChannels();

        if (!listening.isEmpty()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            for (String channel : listening) {
                try {
                    stream.write(channel.getBytes("UTF8"));
                    stream.write((byte) 0);
                } catch (IOException ex) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + getName(), ex);
                }
            }

            getHandle().connection.sendPacket(new SPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.wrappedBuffer(stream.toByteArray()))));
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        Container container = getHandle().openContainer;
        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        }
        getHandle().sendWindowProperty(container, prop.getId(), value);
        return true;
    }

    public void disconnect(String reason) {
        conversationTracker.abandonAllConversations();
        perm.clearPermissions();
    }

    @Override
    public boolean isFlying() {
        return getHandle().capabilities.isFlying;
    }

    @Override
    public void setFlying(boolean value) {
        if (!getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        }

        getHandle().capabilities.isFlying = value;
        getHandle().sendPlayerAbilities();
    }

    @Override
    public boolean getAllowFlight() {
        return getHandle().capabilities.allowFlying;
    }

    @Override
    public void setAllowFlight(boolean value) {
        if (isFlying() && !value) {
            getHandle().capabilities.isFlying = false;
        }

        getHandle().capabilities.allowFlying = value;
        getHandle().sendPlayerAbilities();
    }

    @Override
    public int getNoDamageTicks() {
        if (getHandle().respawnInvulnerabilityTicks > 0) {
            return Math.max(getHandle().respawnInvulnerabilityTicks, getHandle().hurtResistantTime);
        } else {
            return getHandle().hurtResistantTime;
        }
    }

    @Override
    public void setFlySpeed(float value) {
        validateSpeed(value);
        EntityPlayer player = getHandle();
        player.capabilities.flySpeed = value / 2f;
        player.sendPlayerAbilities();

    }

    @Override
    public void setWalkSpeed(float value) {
        validateSpeed(value);
        EntityPlayer player = getHandle();
        player.capabilities.walkSpeed = value / 2f;
        player.sendPlayerAbilities();
    }

    @Override
    public float getFlySpeed() {
        return getHandle().capabilities.flySpeed * 2f;
    }

    @Override
    public float getWalkSpeed() {
        return getHandle().capabilities.walkSpeed * 2f;
    }

    private void validateSpeed(float value) {
        if (value < 0) {
            if (value < -1f) {
                throw new IllegalArgumentException(value + " is too low");
            }
        } else {
            if (value > 1f) {
                throw new IllegalArgumentException(value + " is too high");
            }
        }
    }

    @Override
    public void setMaxHealth(double amount) {
        super.setMaxHealth(amount);
        this.health = Math.min(this.health, health);
        getHandle().setPlayerHealthUpdated();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        getHandle().setPlayerHealthUpdated();
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        NetHandlerPlayServer connection = getHandle().connection;
        if (connection == null) {
            return;
        }
        if (connection.isDisconnected()) {
            // throw new IllegalStateException("Cannot set scoreboard for invalid CraftPlayer"); // Spigot - remove this as Mojang's semi asynchronous Netty implementation can lead to races
        }

        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }

    @Override
    public void setHealthScale(double value) {
        Validate.isTrue((float) value > 0F, "Must be greater than 0");
        healthScale = value;
        scaledHealth = true;
        updateScaledHealth();
    }

    @Override
    public double getHealthScale() {
        return healthScale;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        if (scaledHealth != (scaledHealth = scale)) {
            updateScaledHealth();
        }
    }

    @Override
    public boolean isHealthScaled() {
        return scaledHealth;
    }

    public float getScaledHealth() {
        return (float) (isHealthScaled() ? getHealth() * getHealthScale() / getMaxHealth() : getHealth());
    }

    @Override
    public double getHealth() {
        return health;
    }

    public void setRealHealth(double health) {
        this.health = health;
    }

    public void updateScaledHealth() {
        AttributeMap attributemapserver = (AttributeMap) getHandle().getAttributeMap();
        Collection<IAttributeInstance> set = attributemapserver.getWatchedAttributes(); // PAIL: Rename

        injectScaledMaxHealth(set, true);

        // SPIGOT-3813: Attributes before health
        if (getHandle().connection != null) {
            getHandle().connection.sendPacket(new SPacketEntityProperties(getHandle().getEntityId(), set));
            sendHealthUpdate();
        }
        getHandle().getDataManager().set(EntityLiving.HEALTH, (float) getScaledHealth());

        getHandle().maxHealthCache = getMaxHealth();
    }

    public void sendHealthUpdate() {
        getHandle().connection.sendPacket(new SPacketUpdateHealth(getScaledHealth(), getHandle().getFoodStats().getFoodLevel(), getHandle().getFoodStats().getSaturationLevel()));
    }

    public void injectScaledMaxHealth(Collection<IAttributeInstance> collection, boolean force) {
        if (!scaledHealth && !force) {
            return;
        }
        for (IAttributeInstance genericInstance : collection) {
            if (genericInstance.getAttribute().getName().equals("generic.maxHealth")) {
                collection.remove(genericInstance);
                break;
            }
        }
        // Spigot start
        double healthMod = scaledHealth ? healthScale : getMaxHealth();
        if ( healthMod >= Float.MAX_VALUE || healthMod <= 0 )
        {
            healthMod = 20; // Reset health
            getServer().getLogger().warning( getName() + " tried to crash the server with a large health attribute" );
        }
        collection.add(new ModifiableAttributeInstance(getHandle().getAttributeMap(), (new RangedAttribute(null, "generic.maxHealth", healthMod, 0.0D, Float.MAX_VALUE)).setDescription("Max Health").setShouldWatch(true)));
        // Spigot end
    }

    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
        Entity followed = getHandle().getSpectatingEntity();
        return followed == getHandle() ? null : followed.getBukkitEntity();
    }

    @Override
    public void setSpectatorTarget(org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        getHandle().setSpectatingEntity((entity == null) ? null : ((CraftEntity) entity).getHandle());
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (getHandle().connection == null) return;

        SPacketTitle times = new SPacketTitle(fadeIn, stay, fadeOut);
        getHandle().connection.sendPacket(times);

        if (title != null) {
            SPacketTitle packetTitle = new SPacketTitle(SPacketTitle.Type.TITLE, CraftChatMessage.fromString(title)[0]);
            getHandle().connection.sendPacket(packetTitle);
        }

        if (subtitle != null) {
            SPacketTitle packetSubtitle = new SPacketTitle(SPacketTitle.Type.SUBTITLE, CraftChatMessage.fromString(subtitle)[0]);
            getHandle().connection.sendPacket(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        if (getHandle().connection == null) return;

        SPacketTitle packetReset = new SPacketTitle(SPacketTitle.Type.RESET, null);
        getHandle().connection.sendPacket(packetReset);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        if (getHandle().connection == null) return;

        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        SPacketParticles packetplayoutworldparticles = new SPacketParticles(CraftParticle.toNMS(particle), true, (float) x, (float) y, (float) z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count, CraftParticle.toData(particle, data));
        getHandle().connection.sendPacket(packetplayoutworldparticles);

    }

    @Override
    public org.bukkit.advancement.AdvancementProgress getAdvancementProgress(org.bukkit.advancement.Advancement advancement) {
        Preconditions.checkArgument(advancement != null, "advancement");

        CraftAdvancement craft = (CraftAdvancement) advancement;
        PlayerAdvancements data = getHandle().getAdvancements();
        AdvancementProgress progress = data.getProgress(craft.getHandle());

        return new CraftAdvancementProgress(craft, data, progress);
    }

    @Override
    public String getLocale() {
        return getHandle().language;
    }

    // Spigot start
    private final Player.Spigot spigot = new Player.Spigot()
    {

        @Override
        public InetSocketAddress getRawAddress()
        {
            return (InetSocketAddress) getHandle().connection.netManager.getRawAddress();
        }

        @Override
        public boolean getCollidesWithEntities() {
            return CraftPlayer.this.isCollidable();
        }

        @Override
        public void setCollidesWithEntities(boolean collides) {
            CraftPlayer.this.setCollidable(collides);
        }

        @Override
        public void respawn()
        {
            if ( getHealth() <= 0 && isOnline() )
            {
                server.getServer().getPlayerList().recreatePlayerEntity( getHandle(), 0, false );
            }
        }

        @Override
        public void playEffect( Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius )
        {
            Validate.notNull( location, "Location cannot be null" );
            Validate.notNull( effect, "Effect cannot be null" );
            Validate.notNull( location.getWorld(), "World cannot be null" );
            Packet packet;
            if ( effect.getType() != Effect.Type.PARTICLE )
            {
                int packetData = effect.getId();
                packet = new SPacketEffect( packetData, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ() ), id, false );
            } else
            {
                net.minecraft.util.EnumParticleTypes particle = null;
                int[] extra = null;
                for ( net.minecraft.util.EnumParticleTypes p : net.minecraft.util.EnumParticleTypes.values() )
                {
                    if ( effect.getName().startsWith( p.getParticleName().replace("_", "") ) )
                    {
                        particle = p;
                        if ( effect.getData() != null ) 
                        {
                            if ( effect.getData().equals( org.bukkit.Material.class ) )
                            {
                                extra = new int[]{ id };
                            } else 
                            {
                                extra = new int[]{ (data << 12) | (id & 0xFFF) };
                            }
                        }
                        break;
                    }
                }
                if ( extra == null )
                {
                    extra = new int[0];
                }
                packet = new SPacketParticles( particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, extra );
            }
            int distance;
            radius *= radius;
            if ( getHandle().connection == null )
            {
                return;
            }
            if ( !location.getWorld().equals( getWorld() ) )
            {
                return;
            }

            distance = (int) getLocation().distanceSquared( location );
            if ( distance <= radius )
            {
                getHandle().connection.sendPacket( packet );
            }
        }

        @Override
        public String getLocale()
        {
            return getHandle().language;
        }

        @Override
        public Set<Player> getHiddenPlayers()
        {
            Set<Player> ret = new HashSet<Player>();
            for ( UUID u : hiddenPlayers.keySet() )
            {
                ret.add( getServer().getPlayer( u ) );
            }

            return java.util.Collections.unmodifiableSet( ret );
        }

        @Override
        public void sendMessage(BaseComponent component) {
            sendMessage( new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(BaseComponent... components) {
            if ( getHandle().connection == null ) return;

            SPacketChat packet = new SPacketChat(null, ChatType.CHAT);
            packet.components = components;
            getHandle().connection.sendPacket(packet);
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent component) {
            sendMessage( position, new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent... components) {
            if ( getHandle().connection == null ) return;

            SPacketChat packet = new SPacketChat(null, ChatType.byId((byte) position.ordinal()));
            // Action bar doesn't render colours, replace colours with legacy section symbols
            if (position == net.md_5.bungee.api.ChatMessageType.ACTION_BAR) {
                components = new BaseComponent[]{new net.md_5.bungee.api.chat.TextComponent(BaseComponent.toLegacyText(components))};
            }
            packet.components = components;
            getHandle().connection.sendPacket(packet);
        }
    };

    public Player.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end

    // CatServer start
    public boolean isFakePlayer() {
        return (this instanceof CraftFakePlayer);
    }
    // CatServer end
}
