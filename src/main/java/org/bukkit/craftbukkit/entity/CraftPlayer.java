// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.*;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec4b;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.*;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
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
import org.bukkit.inventory.InventoryView;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player
{
    private long firstPlayed;
    private long lastPlayed;
    private boolean hasPlayedBefore;
    private final ConversationTracker conversationTracker;
    private final Set<String> channels;
    private final Set<UUID> hiddenPlayers;
    private int hash;
    private double health;
    private boolean scaledHealth;
    private double healthScale;
    
    public CraftPlayer(final CraftServer server, final EntityPlayerMP entity) {
        super(server, entity);
        this.firstPlayed = 0L;
        this.lastPlayed = 0L;
        this.hasPlayedBefore = false;
        this.conversationTracker = new ConversationTracker();
        this.channels = new HashSet<String>();
        this.hiddenPlayers = new HashSet<UUID>();
        this.hash = 0;
        this.health = 20.0;
        this.scaledHealth = false;
        this.healthScale = 20.0;
        this.firstPlayed = System.currentTimeMillis();
    }
    
    public GameProfile getProfile() {
        return this.getHandle().getGameProfile();
    }
    
    @Override
    public boolean isOp() {
        return this.server.getHandle().canSendCommands(this.getProfile());
    }
    
    @Override
    public void setOp(final boolean value) {
        if (value == this.isOp()) {
            return;
        }
        if (value) {
            this.server.getHandle().addOp(this.getProfile());
        }
        else {
            this.server.getHandle().removeOp(this.getProfile());
        }
        this.perm.recalculatePermissions();
    }
    
    @Override
    public boolean isOnline() {
        return this.server.getPlayer(this.getUniqueId()) != null;
    }
    
    @Override
    public InetSocketAddress getAddress() {
        if (this.getHandle().connection == null) {
            return null;
        }
        final SocketAddress addr = this.getHandle().connection.netManager.getRemoteAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress)addr;
        }
        return null;
    }
    
    @Override
    public double getEyeHeight() {
        return this.getEyeHeight(false);
    }
    
    @Override
    public double getEyeHeight(final boolean ignoreSneaking) {
        if (ignoreSneaking) {
            return 1.62;
        }
        if (this.isSneaking()) {
            return 1.54;
        }
        return 1.62;
    }
    
    @Override
    public void sendRawMessage(final String message) {
        if (this.getHandle().connection == null) {
            return;
        }
        ITextComponent[] fromString;
        for (int length = (fromString = CraftChatMessage.fromString(message)).length, i = 0; i < length; ++i) {
            final ITextComponent component = fromString[i];
            this.getHandle().connection.sendPacket(new SPacketChat(component));
        }
    }
    
    @Override
    public void sendMessage(final String message) {
        if (!this.conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }
    }
    
    @Override
    public void sendMessage(final String[] messages) {
        for (final String message : messages) {
            this.sendMessage(message);
        }
    }
    
    @Override
    public String getDisplayName() {
        return this.getHandle().displayName;
    }
    
    @Override
    public void setDisplayName(final String name) {
        this.getHandle().displayName = ((name == null) ? this.getName() : name);
    }
    
    @Override
    public String getPlayerListName() {
        return (this.getHandle().listName == null) ? this.getName() : CraftChatMessage.fromComponent(this.getHandle().listName);
    }
    
    @Override
    public void setPlayerListName(String name) {
        if (name == null) {
            name = this.getName();
        }
        this.getHandle().listName = (name.equals(this.getName()) ? null : CraftChatMessage.fromString(name)[0]);
        for (final EntityPlayerMP player : this.server.getHandle().playerEntityList) {
            if (player.getBukkitEntity().canSee(this)) {
                player.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_DISPLAY_NAME, new EntityPlayerMP[] { this.getHandle() }));
            }
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        final OfflinePlayer other = (OfflinePlayer)obj;
        if (this.getUniqueId() == null || other.getUniqueId() == null) {
            return false;
        }
        final boolean uuidEquals = this.getUniqueId().equals(other.getUniqueId());
        boolean idEquals = true;
        if (other instanceof CraftPlayer) {
            idEquals = (this.getEntityId() == ((CraftPlayer)other).getEntityId());
        }
        return uuidEquals && idEquals;
    }
    
    @Override
    public void kickPlayer(final String message) {
        if (this.getHandle().connection == null) {
            return;
        }
        this.getHandle().connection.kickPlayerFromServer((message == null) ? "" : message);
    }
    
    @Override
    public void setCompassTarget(final Location loc) {
        if (this.getHandle().connection == null) {
            return;
        }
        this.getHandle().connection.sendPacket(new SPacketSpawnPosition(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
    }
    
    @Override
    public Location getCompassTarget() {
        return this.getHandle().compassTarget;
    }
    
    @Override
    public void chat(final String msg) {
        if (this.getHandle().connection == null) {
            return;
        }
        this.getHandle().connection.chat(msg, false);
    }
    
    @Override
    public boolean performCommand(final String command) {
        return this.server.dispatchCommand(this, command);
    }
    
    @Override
    public void playNote(final Location loc, final byte instrument, final byte note) {
        if (this.getHandle().connection == null) {
            return;
        }
        String instrumentName = null;
        switch (instrument) {
            case 0: {
                instrumentName = "harp";
                break;
            }
            case 1: {
                instrumentName = "basedrum";
                break;
            }
            case 2: {
                instrumentName = "snare";
                break;
            }
            case 3: {
                instrumentName = "hat";
                break;
            }
            case 4: {
                instrumentName = "bass";
                break;
            }
        }
        final float f = (float)Math.pow(2.0, (note - 12.0) / 12.0);
        this.getHandle().connection.sendPacket(new SPacketSoundEffect(CraftSound.getSoundEffect("block.note." + instrumentName), SoundCategory.MUSIC, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }
    
    @Override
    public void playNote(final Location loc, final Instrument instrument, final Note note) {
        if (this.getHandle().connection == null) {
            return;
        }
        String instrumentName = null;
        switch (instrument.ordinal()) {
            case 0: {
                instrumentName = "harp";
                break;
            }
            case 1: {
                instrumentName = "basedrum";
                break;
            }
            case 2: {
                instrumentName = "snare";
                break;
            }
            case 3: {
                instrumentName = "hat";
                break;
            }
            case 4: {
                instrumentName = "bass";
                break;
            }
        }
        final float f = (float)Math.pow(2.0, (note.getId() - 12.0) / 12.0);
        this.getHandle().connection.sendPacket(new SPacketSoundEffect(CraftSound.getSoundEffect("block.note." + instrumentName), SoundCategory.MUSIC, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }
    
    @Override
    public void playSound(final Location loc, final Sound sound, final float volume, final float pitch) {
        if (loc == null || sound == null || this.getHandle().connection == null) {
            return;
        }
        final SPacketSoundEffect packet = new SPacketSoundEffect(CraftSound.getSoundEffect(CraftSound.getSound(sound)), SoundCategory.MASTER, loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        this.getHandle().connection.sendPacket(packet);
    }
    
    @Override
    public void playSound(final Location loc, final String sound, final float volume, final float pitch) {
        if (loc == null || sound == null || this.getHandle().connection == null) {
            return;
        }
        final SPacketCustomSound packet = new SPacketCustomSound(sound, SoundCategory.MASTER, loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        this.getHandle().connection.sendPacket(packet);
    }
    
    @Override
    public void stopSound(final Sound sound) {
        this.stopSound(CraftSound.getSound(sound));
    }
    
    @Override
    public void stopSound(final String sound) {
        if (this.getHandle().connection == null) {
            return;
        }
        final PacketBuffer packetdataserializer = new PacketBuffer(Unpooled.buffer());
        packetdataserializer.writeString(sound);
        packetdataserializer.writeString("");
        this.getHandle().connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetdataserializer));
    }
    
    @Override
    public void playEffect(final Location loc, final Effect effect, final int data) {
        if (this.getHandle().connection == null) {
            return;
        }
        final int packetData = effect.getId();
        final SPacketEffect packet = new SPacketEffect(packetData, new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), data, false);
        this.getHandle().connection.sendPacket(packet);
    }
    
    @Override
    public <T> void playEffect(final Location loc, final Effect effect, final T data) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        }
        else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }
        final int datavalue = (data == null) ? 0 : CraftEffect.getDataValue(effect, data);
        this.playEffect(loc, effect, datavalue);
    }
    
    @Override
    public void sendBlockChange(final Location loc, final Material material, final byte data) {
        this.sendBlockChange(loc, material.getId(), data);
    }
    
    @Override
    public void sendBlockChange(final Location loc, final int material, final byte data) {
        if (this.getHandle().connection == null) {
            return;
        }
        final SPacketBlockChange packet = new SPacketBlockChange(((CraftWorld)loc.getWorld()).getHandle(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        packet.blockState = CraftMagicNumbers.getBlock(material).getStateFromMeta(data);
        this.getHandle().connection.sendPacket(packet);
    }
    
    @Override
    public void sendSignChange(final Location loc, String[] lines) {
        if (this.getHandle().connection == null) {
            return;
        }
        if (lines == null) {
            lines = new String[4];
        }
        Validate.notNull((Object)loc, "Location can not be null");
        if (lines.length < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }
        final ITextComponent[] components = CraftSign.sanitizeLines(lines);
        final TileEntitySign sign = new TileEntitySign();
        sign.setPos(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        System.arraycopy(components, 0, sign.signText, 0, sign.signText.length);
        this.getHandle().connection.sendPacket(sign.getUpdatePacket());
    }
    
    @Override
    public boolean sendChunkChange(final Location loc, final int sx, final int sy, final int sz, final byte[] data) {
        if (this.getHandle().connection == null) {
            return false;
        }
        throw new NotImplementedException("Chunk changes do not yet work");
    }
    
    @Override
    public void sendMap(final MapView map) {
        if (this.getHandle().connection == null) {
            return;
        }
        final RenderData data = ((CraftMapView)map).render(this);
        final Collection<Vec4b> icons = new ArrayList<Vec4b>();
        for (final MapCursor cursor : data.cursors) {
            if (cursor.isVisible()) {
                icons.add(new Vec4b(cursor.getRawType(), cursor.getX(), cursor.getY(), cursor.getDirection()));
            }
        }
        final SPacketMaps packet = new SPacketMaps(map.getId(), map.getScale().getValue(), true, icons, data.buffer, 0, 0, 128, 128);
        this.getHandle().connection.sendPacket(packet);
    }
    
    @Override
    public boolean teleport(final Location location, final PlayerTeleportEvent.TeleportCause cause) {
        final EntityPlayerMP entity = this.getHandle();
        if (this.getHealth() == 0.0 || entity.isDead) {
            return false;
        }
        if (entity.connection == null) {
            return false;
        }
        if (entity.isBeingRidden()) {
            return false;
        }
        Location from = this.getLocation();
        final PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, location, cause);
        this.server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        entity.dismountRidingEntity();
        from = event.getFrom();
        final Location to = event.getTo();
        final WorldServer fromWorld = ((CraftWorld)from.getWorld()).getHandle();
        final WorldServer toWorld = ((CraftWorld)to.getWorld()).getHandle();
        if (this.getHandle().openContainer != this.getHandle().inventoryContainer) {
            this.getHandle().closeScreen();
        }
        if (fromWorld == toWorld) {
            entity.connection.teleport(to);
        }
        else {
            this.server.getHandle().moveToWorld(entity, toWorld.dimension, true, to, true);
        }
        return true;
    }
    
    @Override
    public void setSneaking(final boolean sneak) {
        this.getHandle().setSneaking(sneak);
    }
    
    @Override
    public boolean isSneaking() {
        return this.getHandle().isSneaking();
    }
    
    @Override
    public boolean isSprinting() {
        return this.getHandle().isSprinting();
    }
    
    @Override
    public void setSprinting(final boolean sprinting) {
        this.getHandle().setSprinting(sprinting);
    }
    
    @Override
    public void loadData() {
        this.server.getHandle().playerNBTManagerObj.readPlayerData(this.getHandle());
    }
    
    @Override
    public void saveData() {
        this.server.getHandle().playerNBTManagerObj.writePlayerData(this.getHandle());
    }
    
    @Deprecated
    @Override
    public void updateInventory() {
        this.getHandle().sendContainerToPlayer(this.getHandle().openContainer);
    }
    
    @Override
    public void setSleepingIgnored(final boolean isSleeping) {
        this.getHandle().fauxSleeping = isSleeping;
        ((CraftWorld)this.getWorld()).getHandle().checkSleepStatus();
    }
    
    @Override
    public boolean isSleepingIgnored() {
        return this.getHandle().fauxSleeping;
    }
    
    @Override
    public void awardAchievement(final Achievement achievement) {
        Validate.notNull((Object)achievement, "Achievement cannot be null");
        if (achievement.hasParent() && !this.hasAchievement(achievement.getParent())) {
            this.awardAchievement(achievement.getParent());
        }
        this.getHandle().getStatFile().unlockAchievement(this.getHandle(), CraftStatistic.getNMSAchievement(achievement), 1);
        this.getHandle().getStatFile().sendAchievements(this.getHandle());
    }
    
    @Override
    public void removeAchievement(final Achievement achievement) {
        Validate.notNull((Object)achievement, "Achievement cannot be null");
        Achievement[] values;
        for (int length = (values = Achievement.values()).length, i = 0; i < length; ++i) {
            final Achievement achieve = values[i];
            if (achieve.getParent() == achievement && this.hasAchievement(achieve)) {
                this.removeAchievement(achieve);
            }
        }
        this.getHandle().getStatFile().unlockAchievement(this.getHandle(), CraftStatistic.getNMSAchievement(achievement), 0);
    }
    
    @Override
    public boolean hasAchievement(final Achievement achievement) {
        Validate.notNull((Object)achievement, "Achievement cannot be null");
        return this.getHandle().getStatFile().hasAchievementUnlocked(CraftStatistic.getNMSAchievement(achievement));
    }
    
    @Override
    public void incrementStatistic(final Statistic statistic) {
        this.incrementStatistic(statistic, 1);
    }
    
    @Override
    public void decrementStatistic(final Statistic statistic) {
        this.decrementStatistic(statistic, 1);
    }
    
    @Override
    public int getStatistic(final Statistic statistic) {
        Validate.notNull((Object)statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Statistic.Type.UNTYPED, "Must supply additional paramater for this statistic");
        return this.getHandle().getStatFile().readStat(CraftStatistic.getNMSStatistic(statistic));
    }
    
    @Override
    public void incrementStatistic(final Statistic statistic, final int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, this.getStatistic(statistic) + amount);
    }
    
    @Override
    public void decrementStatistic(final Statistic statistic, final int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, this.getStatistic(statistic) - amount);
    }
    
    @Override
    public void setStatistic(final Statistic statistic, final int newValue) {
        Validate.notNull((Object)statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Statistic.Type.UNTYPED, "Must supply additional paramater for this statistic");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        final StatBase nmsStatistic = CraftStatistic.getNMSStatistic(statistic);
        this.getHandle().getStatFile().unlockAchievement(this.getHandle(), nmsStatistic, newValue);
    }
    
    @Override
    public void incrementStatistic(final Statistic statistic, final Material material) {
        this.incrementStatistic(statistic, material, 1);
    }
    
    @Override
    public void decrementStatistic(final Statistic statistic, final Material material) {
        this.decrementStatistic(statistic, material, 1);
    }
    
    @Override
    public int getStatistic(final Statistic statistic, final Material material) {
        Validate.notNull((Object)statistic, "Statistic cannot be null");
        Validate.notNull((Object)material, "Material cannot be null");
        Validate.isTrue(statistic.getType() == Statistic.Type.BLOCK || statistic.getType() == Statistic.Type.ITEM, "This statistic does not take a Material parameter");
        final StatBase nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull((Object)nmsStatistic, "The supplied Material does not have a corresponding statistic");
        return this.getHandle().getStatFile().readStat(nmsStatistic);
    }
    
    @Override
    public void incrementStatistic(final Statistic statistic, final Material material, final int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, material, this.getStatistic(statistic, material) + amount);
    }
    
    @Override
    public void decrementStatistic(final Statistic statistic, final Material material, final int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, material, this.getStatistic(statistic, material) - amount);
    }
    
    @Override
    public void setStatistic(final Statistic statistic, final Material material, final int newValue) {
        Validate.notNull((Object)statistic, "Statistic cannot be null");
        Validate.notNull((Object)material, "Material cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Statistic.Type.BLOCK || statistic.getType() == Statistic.Type.ITEM, "This statistic does not take a Material parameter");
        final StatBase nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull((Object)nmsStatistic, "The supplied Material does not have a corresponding statistic");
        this.getHandle().getStatFile().unlockAchievement(this.getHandle(), nmsStatistic, newValue);
    }
    
    @Override
    public void incrementStatistic(final Statistic statistic, final EntityType entityType) {
        this.incrementStatistic(statistic, entityType, 1);
    }
    
    @Override
    public void decrementStatistic(final Statistic statistic, final EntityType entityType) {
        this.decrementStatistic(statistic, entityType, 1);
    }
    
    @Override
    public int getStatistic(final Statistic statistic, final EntityType entityType) {
        Validate.notNull((Object)statistic, "Statistic cannot be null");
        Validate.notNull((Object)entityType, "EntityType cannot be null");
        Validate.isTrue(statistic.getType() == Statistic.Type.ENTITY, "This statistic does not take an EntityType parameter");
        final StatBase nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull((Object)nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        return this.getHandle().getStatFile().readStat(nmsStatistic);
    }
    
    @Override
    public void incrementStatistic(final Statistic statistic, final EntityType entityType, final int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, entityType, this.getStatistic(statistic, entityType) + amount);
    }
    
    @Override
    public void decrementStatistic(final Statistic statistic, final EntityType entityType, final int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        this.setStatistic(statistic, entityType, this.getStatistic(statistic, entityType) - amount);
    }
    
    @Override
    public void setStatistic(final Statistic statistic, final EntityType entityType, final int newValue) {
        Validate.notNull((Object)statistic, "Statistic cannot be null");
        Validate.notNull((Object)entityType, "EntityType cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Statistic.Type.ENTITY, "This statistic does not take an EntityType parameter");
        final StatBase nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull((Object)nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        this.getHandle().getStatFile().unlockAchievement(this.getHandle(), nmsStatistic, newValue);
    }
    
    @Override
    public void setPlayerTime(final long time, final boolean relative) {
        this.getHandle().timeOffset = time;
        this.getHandle().relativeTime = relative;
    }
    
    @Override
    public long getPlayerTimeOffset() {
        return this.getHandle().timeOffset;
    }
    
    @Override
    public long getPlayerTime() {
        return this.getHandle().getPlayerTime();
    }
    
    @Override
    public boolean isPlayerTimeRelative() {
        return this.getHandle().relativeTime;
    }
    
    @Override
    public void resetPlayerTime() {
        this.setPlayerTime(0L, true);
    }
    
    @Override
    public void setPlayerWeather(final WeatherType type) {
        this.getHandle().setPlayerWeather(type, true);
    }
    
    @Override
    public WeatherType getPlayerWeather() {
        return this.getHandle().getPlayerWeather();
    }
    
    @Override
    public void resetPlayerWeather() {
        this.getHandle().resetPlayerWeather();
    }
    
    @Override
    public boolean isBanned() {
        return this.server.getBanList(BanList.Type.NAME).isBanned(this.getName());
    }
    
    @Override
    public void setBanned(final boolean value) {
        if (value) {
            this.server.getBanList(BanList.Type.NAME).addBan(this.getName(), null, null, null);
        }
        else {
            this.server.getBanList(BanList.Type.NAME).pardon(this.getName());
        }
    }
    
    @Override
    public boolean isWhitelisted() {
        return this.server.getHandle().getWhitelistedPlayers().isWhitelisted(this.getProfile());
    }
    
    @Override
    public void setWhitelisted(final boolean value) {
        if (value) {
            this.server.getHandle().addWhitelistedPlayer(this.getProfile());
        }
        else {
            this.server.getHandle().removePlayerFromWhitelist(this.getProfile());
        }
    }
    
    @Override
    public void setGameMode(final GameMode mode) {
        if (this.getHandle().connection == null) {
            return;
        }
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }
        if (mode != this.getGameMode()) {
            final PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(this, mode);
            this.server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            this.getHandle().setSpectatingEntity(this.getHandle());
            this.getHandle().interactionManager.setGameType(GameType.getByID(mode.getValue()));
            this.getHandle().fallDistance = 0.0f;
            this.getHandle().connection.sendPacket(new SPacketChangeGameState(3, mode.getValue()));
        }
    }
    
    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(this.getHandle().interactionManager.getGameType().getID());
    }
    
    @Override
    public void giveExp(final int exp) {
        this.getHandle().addExperience(exp);
    }
    
    @Override
    public void giveExpLevels(final int levels) {
        this.getHandle().addExperienceLevel(levels);
    }
    
    @Override
    public float getExp() {
        return this.getHandle().experience;
    }
    
    @Override
    public void setExp(final float exp) {
        this.getHandle().experience = exp;
        this.getHandle().lastExperience = -1;
    }
    
    @Override
    public int getLevel() {
        return this.getHandle().experienceLevel;
    }
    
    @Override
    public void setLevel(final int level) {
        this.getHandle().experienceLevel = level;
        this.getHandle().lastExperience = -1;
    }
    
    @Override
    public int getTotalExperience() {
        return this.getHandle().experienceTotal;
    }
    
    @Override
    public void setTotalExperience(final int exp) {
        this.getHandle().experienceTotal = exp;
    }
    
    @Override
    public float getExhaustion() {
        return this.getHandle().getFoodStats().foodExhaustionLevel;
    }
    
    @Override
    public void setExhaustion(final float value) {
        this.getHandle().getFoodStats().foodExhaustionLevel = value;
    }
    
    @Override
    public float getSaturation() {
        return this.getHandle().getFoodStats().foodSaturationLevel;
    }
    
    @Override
    public void setSaturation(final float value) {
        this.getHandle().getFoodStats().foodSaturationLevel = value;
    }
    
    @Override
    public int getFoodLevel() {
        return this.getHandle().getFoodStats().foodLevel;
    }
    
    @Override
    public void setFoodLevel(final int value) {
        this.getHandle().getFoodStats().foodLevel = value;
    }
    
    @Override
    public Location getBedSpawnLocation() {
        final org.bukkit.World world = this.getServer().getWorld(this.getHandle().spawnWorld);
        BlockPos bed = this.getHandle().getBedLocation();
        if (world != null && bed != null) {
            bed = EntityPlayer.getBedSpawnLocation(((CraftWorld)world).getHandle(), bed, this.getHandle().isSpawnForced());
            if (bed != null) {
                return new Location(world, bed.getX(), bed.getY(), bed.getZ());
            }
        }
        return null;
    }
    
    @Override
    public void setBedSpawnLocation(final Location location) {
        this.setBedSpawnLocation(location, false);
    }
    
    @Override
    public void setBedSpawnLocation(final Location location, final boolean override) {
        if (location == null) {
            this.getHandle().setSpawnPoint(null, override);
        }
        else {
            this.getHandle().setSpawnPoint(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), override);
            this.getHandle().spawnWorld = location.getWorld().getName();
        }
    }
    
    @Override
    public void hidePlayer(final Player player) {
        Validate.notNull((Object)player, "hidden player cannot be null");
        if (this.getHandle().connection == null) {
            return;
        }
        if (this.equals(player)) {
            return;
        }
        if (this.hiddenPlayers.contains(player.getUniqueId())) {
            return;
        }
        this.hiddenPlayers.add(player.getUniqueId());
        final EntityTracker tracker = ((WorldServer)this.entity.worldObj).theEntityTracker;
        final EntityPlayerMP other = ((CraftPlayer)player).getHandle();
        final EntityTrackerEntry entry = tracker.trackedEntityHashTable.lookup(other.getEntityId());
        if (entry != null) {
            entry.removeTrackedPlayerSymmetric(this.getHandle());
        }
        this.getHandle().connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[] { other }));
    }
    
    @Override
    public void showPlayer(final Player player) {
        Validate.notNull((Object)player, "shown player cannot be null");
        if (this.getHandle().connection == null) {
            return;
        }
        if (this.equals(player)) {
            return;
        }
        if (!this.hiddenPlayers.contains(player.getUniqueId())) {
            return;
        }
        this.hiddenPlayers.remove(player.getUniqueId());
        final EntityTracker tracker = ((WorldServer)this.entity.worldObj).theEntityTracker;
        final EntityPlayerMP other = ((CraftPlayer)player).getHandle();
        this.getHandle().connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { other }));
        final EntityTrackerEntry entry = tracker.trackedEntityHashTable.lookup(other.getEntityId());
        if (entry != null && !entry.trackingPlayers.contains(this.getHandle())) {
            entry.updatePlayerEntity(this.getHandle());
        }
    }
    
    public void removeDisconnectingPlayer(final Player player) {
        this.hiddenPlayers.remove(player.getUniqueId());
    }
    
    @Override
    public boolean canSee(final Player player) {
        return !this.hiddenPlayers.contains(player.getUniqueId());
    }
    
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("name", this.getName());
        return result;
    }
    
    @Override
    public Player getPlayer() {
        return this;
    }
    
    @Override
    public EntityPlayerMP getHandle() {
        return (EntityPlayerMP)this.entity;
    }
    
    public void setHandle(final EntityPlayerMP entity) {
        super.setHandle(entity);
    }
    
    @Override
    public String toString() {
        return "CraftPlayer{name=" + this.getName() + '}';
    }
    
    @Override
    public int hashCode() {
        if (this.hash == 0 || this.hash == 485) {
            this.hash = 485 + ((this.getUniqueId() != null) ? this.getUniqueId().hashCode() : 0);
        }
        return this.hash;
    }
    
    @Override
    public long getFirstPlayed() {
        return this.firstPlayed;
    }
    
    @Override
    public long getLastPlayed() {
        return this.lastPlayed;
    }
    
    @Override
    public boolean hasPlayedBefore() {
        return this.hasPlayedBefore;
    }
    
    public void setFirstPlayed(final long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }
    
    public void readExtraData(final NBTTagCompound nbttagcompound) {
        this.hasPlayedBefore = true;
        if (nbttagcompound.hasKey("bukkit")) {
            final NBTTagCompound data = nbttagcompound.getCompoundTag("bukkit");
            if (data.hasKey("firstPlayed")) {
                this.firstPlayed = data.getLong("firstPlayed");
                this.lastPlayed = data.getLong("lastPlayed");
            }
            if (data.hasKey("newExp")) {
                final EntityPlayerMP handle = this.getHandle();
                handle.newExp = data.getInteger("newExp");
                handle.newTotalExp = data.getInteger("newTotalExp");
                handle.newLevel = data.getInteger("newLevel");
                handle.expToDrop = data.getInteger("expToDrop");
                handle.keepLevel = data.getBoolean("keepLevel");
            }
        }
    }
    
    public void setExtraData(final NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("bukkit")) {
            nbttagcompound.setTag("bukkit", new NBTTagCompound());
        }
        final NBTTagCompound data = nbttagcompound.getCompoundTag("bukkit");
        final EntityPlayerMP handle = this.getHandle();
        data.setInteger("newExp", handle.newExp);
        data.setInteger("newTotalExp", handle.newTotalExp);
        data.setInteger("newLevel", handle.newLevel);
        data.setInteger("expToDrop", handle.expToDrop);
        data.setBoolean("keepLevel", handle.keepLevel);
        data.setLong("firstPlayed", this.getFirstPlayed());
        data.setLong("lastPlayed", System.currentTimeMillis());
        data.setString("lastKnownName", handle.getName());
    }
    
    @Override
    public boolean beginConversation(final Conversation conversation) {
        return this.conversationTracker.beginConversation(conversation);
    }
    
    @Override
    public void abandonConversation(final Conversation conversation) {
        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }
    
    @Override
    public void abandonConversation(final Conversation conversation, final ConversationAbandonedEvent details) {
        this.conversationTracker.abandonConversation(conversation, details);
    }
    
    @Override
    public void acceptConversationInput(final String input) {
        this.conversationTracker.acceptConversationInput(input);
    }
    
    @Override
    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }
    
    @Override
    public void sendPluginMessage(final Plugin source, final String channel, final byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
        if (this.getHandle().connection == null) {
            return;
        }
        if (this.channels.contains(channel)) {
            final SPacketCustomPayload packet = new SPacketCustomPayload(channel, new PacketBuffer(Unpooled.wrappedBuffer(message)));
            this.getHandle().connection.sendPacket(packet);
        }
    }
    
    @Override
    public void setTexturePack(final String url) {
        this.setResourcePack(url);
    }
    
    @Override
    public void setResourcePack(final String url) {
        Validate.notNull((Object)url, "Resource pack URL cannot be null");
        this.getHandle().loadResourcePack(url, "null");
    }
    
    public void addChannel(final String channel) {
        if (this.channels.add(channel)) {
            this.server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }
    
    public void removeChannel(final String channel) {
        if (this.channels.remove(channel)) {
            this.server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }
    
    @Override
    public Set<String> getListeningPluginChannels() {
        return (Set<String>)ImmutableSet.copyOf((Collection)this.channels);
    }
    
    public void sendSupportedChannels() {
        if (this.getHandle().connection == null) {
            return;
        }
        final Set<String> listening = this.server.getMessenger().getIncomingChannels();
        if (!listening.isEmpty()) {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            for (final String channel : listening) {
                try {
                    stream.write(channel.getBytes("UTF8"));
                    stream.write(0);
                }
                catch (IOException ex) {
                	Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + this.getName(), ex);
                }
            }
            this.getHandle().connection.sendPacket(new SPacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.wrappedBuffer(stream.toByteArray()))));
        }
    }
    
    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }
    
    @Override
    public void setMetadata(final String metadataKey, final MetadataValue newMetadataValue) {
        (/*(MetadataStoreBase<CraftPlayer>)*/this.server.getPlayerMetadata()).setMetadata(this, metadataKey, newMetadataValue);
    }
    
    @Override
    public List<MetadataValue> getMetadata(final String metadataKey) {
        return (/*(MetadataStoreBase<CraftPlayer>)*/this.server.getPlayerMetadata()).getMetadata(this, metadataKey);
    }
    
    @Override
    public boolean hasMetadata(final String metadataKey) {
        return (/*(MetadataStoreBase<CraftPlayer>)*/this.server.getPlayerMetadata()).hasMetadata(this, metadataKey);
    }
    
    @Override
    public void removeMetadata(final String metadataKey, final Plugin owningPlugin) {
        (/*(MetadataStoreBase<CraftPlayer>)*/this.server.getPlayerMetadata()).removeMetadata(this, metadataKey, owningPlugin);
    }
    
    @Override
    public boolean setWindowProperty(final InventoryView.Property prop, final int value) {
        final Container container = this.getHandle().openContainer;
        if (((Container)container).getBukkitView().getType() != prop.getType()) {
            return false;
        }
        this.getHandle().sendProgressBarUpdate(container, prop.getId(), value);
        return true;
    }
    
    public void disconnect(final String reason) {
        this.conversationTracker.abandonAllConversations();
        this.perm.clearPermissions();
    }
    
    @Override
    public boolean isFlying() {
        return this.getHandle().capabilities.isFlying;
    }
    
    @Override
    public void setFlying(final boolean value) {
        if (!this.getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        }
        this.getHandle().capabilities.isFlying = value;
        this.getHandle().sendPlayerAbilities();
    }
    
    @Override
    public boolean getAllowFlight() {
        return this.getHandle().capabilities.allowFlying;
    }
    
    @Override
    public void setAllowFlight(final boolean value) {
        if (this.isFlying() && !value) {
            this.getHandle().capabilities.isFlying = false;
        }
        this.getHandle().capabilities.allowFlying = value;
        this.getHandle().sendPlayerAbilities();
    }
    
    @Override
    public int getNoDamageTicks() {
        if (this.getHandle().respawnInvulnerabilityTicks > 0) {
            return Math.max(this.getHandle().respawnInvulnerabilityTicks, this.getHandle().hurtResistantTime);
        }
        return this.getHandle().hurtResistantTime;
    }
    
    @Override
    public void setFlySpeed(final float value) {
        this.validateSpeed(value);
        final EntityPlayerMP player = this.getHandle();
        player.capabilities.flySpeed = value / 2.0f;
        player.sendPlayerAbilities();
    }
    
    @Override
    public void setWalkSpeed(final float value) {
        this.validateSpeed(value);
        final EntityPlayerMP player = this.getHandle();
        player.capabilities.walkSpeed = value / 2.0f;
        player.sendPlayerAbilities();
    }
    
    @Override
    public float getFlySpeed() {
        return this.getHandle().capabilities.flySpeed * 2.0f;
    }
    
    @Override
    public float getWalkSpeed() {
        return this.getHandle().capabilities.walkSpeed * 2.0f;
    }
    
    private void validateSpeed(final float value) {
        if (value < 0.0f) {
            if (value < -1.0f) {
                throw new IllegalArgumentException(String.valueOf(value) + " is too low");
            }
        }
        else if (value > 1.0f) {
            throw new IllegalArgumentException(String.valueOf(value) + " is too high");
        }
    }
    
    @Override
    public void setMaxHealth(final double amount) {
        super.setMaxHealth(amount);
        this.health = Math.min(this.health, this.health);
        this.getHandle().setPlayerHealthUpdated();
    }
    
    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        this.getHandle().setPlayerHealthUpdated();
    }
    
    @Override
    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }
    
    @Override
    public void setScoreboard(final Scoreboard scoreboard) {
        Validate.notNull((Object)scoreboard, "Scoreboard cannot be null");
        final NetHandlerPlayServer playerConnection = this.getHandle().connection;
        if (playerConnection == null) {
            throw new IllegalStateException("Cannot set scoreboard yet");
        }
        if (playerConnection.isDisconnected()) {
            throw new IllegalStateException("Cannot set scoreboard for invalid CraftPlayer");
        }
        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }
    
    @Override
    public void setHealthScale(final double value) {
        Validate.isTrue((float)value > 0.0f, "Must be greater than 0");
        this.healthScale = value;
        this.scaledHealth = true;
        this.updateScaledHealth();
    }
    
    @Override
    public double getHealthScale() {
        return this.healthScale;
    }
    
    @Override
    public void setHealthScaled(final boolean scale) {
        final boolean scaledHealth = this.scaledHealth;
        this.scaledHealth = scale;
        if (scaledHealth != scale) {
            this.updateScaledHealth();
        }
    }
    
    @Override
    public boolean isHealthScaled() {
        return this.scaledHealth;
    }
    
    public float getScaledHealth() {
        return (float)(this.isHealthScaled() ? (this.getHealth() * this.getHealthScale() / this.getMaxHealth()) : this.getHealth());
    }
    
    @Override
    public double getHealth() {
        return this.health;
    }
    
    public void setRealHealth(final double health) {
        this.health = health;
    }
    
    public void updateScaledHealth() {
        final AttributeMap attributemapserver = (AttributeMap)this.getHandle().getAttributeMap();
        final Collection set = attributemapserver.getWatchedAttributes();
        this.injectScaledMaxHealth(set, true);
        this.getHandle().getDataManager().set(EntityLivingBase.HEALTH, this.getScaledHealth());
        this.sendHealthUpdate();
        this.getHandle().connection.sendPacket(new SPacketEntityProperties(this.getHandle().getEntityId(), set));
        this.getHandle().maxHealthCache = this.getMaxHealth();
    }
    
    public void sendHealthUpdate() {
        this.getHandle().connection.sendPacket(new SPacketUpdateHealth(this.getScaledHealth(), this.getHandle().getFoodStats().getFoodLevel(), this.getHandle().getFoodStats().getSaturationLevel()));
    }
    
    public void injectScaledMaxHealth(final Collection collection, final boolean force) {
        if (!this.scaledHealth && !force) {
            return;
        }
        for (final Object genericInstance : collection) {
            final IAttribute attribute = ((IAttributeInstance)genericInstance).getAttribute();
            if (attribute.getAttributeUnlocalizedName().equals("generic.maxHealth")) {
                collection.remove(genericInstance);
                break;
            }
        }
        collection.add(new ModifiableAttributeInstance(this.getHandle().getAttributeMap(), new RangedAttribute(null, "generic.maxHealth", this.scaledHealth ? this.healthScale : this.getMaxHealth(), 0.0, 3.4028234663852886E38).setDescription("Max Health").setShouldWatch(true)));
    }
    
    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
        final net.minecraft.entity.Entity followed = this.getHandle().getSpectatingEntity();
        return (followed == this.getHandle()) ? null : followed.getBukkitEntity();
    }
    
    @Override
    public void setSpectatorTarget(final org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(this.getGameMode() == GameMode.SPECTATOR, (Object)"Player must be in spectator mode");
        this.getHandle().setSpectatingEntity((entity == null) ? null : ((CraftEntity)entity).getHandle());
    }
    
    @Override
    public void sendTitle(final String title, final String subtitle) {
        if (title != null) {
            final SPacketTitle packetTitle = new SPacketTitle(SPacketTitle.Type.TITLE, CraftChatMessage.fromString(title)[0]);
            this.getHandle().connection.sendPacket(packetTitle);
        }
        if (subtitle != null) {
            final SPacketTitle packetSubtitle = new SPacketTitle(SPacketTitle.Type.SUBTITLE, CraftChatMessage.fromString(subtitle)[0]);
            this.getHandle().connection.sendPacket(packetSubtitle);
        }
    }
    
    @Override
    public void resetTitle() {
        final SPacketTitle packetReset = new SPacketTitle(SPacketTitle.Type.RESET, null);
        this.getHandle().connection.sendPacket(packetReset);
    }
    
    @Override
    public void spawnParticle(final Particle particle, final Location location, final int count) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }
    
    @Override
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count) {
        this.spawnParticle(particle, x, y, z, count, (Object)null);
    }
    
    @Override
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }
    
    @Override
    public <T> void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final T data) {
        this.spawnParticle(particle, x, y, z, count, 0.0, 0.0, 0.0, data);
    }
    
    @Override
    public void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }
    
    @Override
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, (Object)null);
    }
    
    @Override
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }
    
    @Override
    public <T> void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1.0, data);
    }
    
    @Override
    public void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }
    
    @Override
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, (Object)null);
    }
    
    @Override
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra, final T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }
    
    @Override
    public <T> void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra, final T data) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        final SPacketParticles packetplayoutworldparticles = new SPacketParticles(CraftParticle.toNMS(particle), true, (float)x, (float)y, (float)z, (float)offsetX, (float)offsetY, (float)offsetZ, (float)extra, count, CraftParticle.toData(particle, data));
        this.getHandle().connection.sendPacket(packetplayoutworldparticles);
    }
}
