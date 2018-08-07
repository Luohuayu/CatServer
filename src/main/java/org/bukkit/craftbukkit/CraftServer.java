// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import net.minecraft.server.management.UserList;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.map.MapView;
import org.bukkit.util.CachedServerIcon;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.UnsafeValues;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import org.bukkit.craftbukkit.generator.CraftChunkData;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.base64.Base64;
import jline.console.ConsoleReader;
import luohuayu.CatServer.CatServerLogger;
import luohuayu.CatServer.command.CraftSimpleCommandMap;

import java.io.OutputStream;
import java.awt.image.RenderedImage;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Comparator;
import org.bukkit.util.StringUtil;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.command.CommandException;
import org.bukkit.ChatColor;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.help.HelpMap;
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.messaging.Messenger;
import java.io.FilenameFilter;
import org.bukkit.craftbukkit.util.DatFileFilter;
import net.minecraft.world.storage.SaveHandler;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.GameMode;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.server.management.UserListWhitelistEntry;
import java.util.LinkedHashSet;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.server.management.UserListBansEntry;
import java.util.Date;
import org.bukkit.BanList;
import java.util.HashSet;
import java.util.Arrays;
import com.mojang.authlib.GameProfile;
import java.util.Set;
import org.bukkit.permissions.Permissible;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapData;
import org.bukkit.craftbukkit.map.CraftMapView;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.bukkit.craftbukkit.inventory.RecipeIterator;
import org.bukkit.inventory.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import org.bukkit.craftbukkit.inventory.CraftFurnaceRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.craftbukkit.inventory.CraftShapelessRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.craftbukkit.inventory.CraftShapedRecipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.inventory.Recipe;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import org.bukkit.command.PluginCommand;
//import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import net.minecraft.world.MinecraftException;
import org.bukkit.event.world.WorldUnloadEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.ISaveFormat;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.Event;
import org.bukkit.event.world.WorldInitEvent;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.entity.EntityTracker;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.GameType;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.WorldCreator;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import org.bukkit.scheduler.BukkitWorker;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import org.bukkit.conversations.Conversable;
import net.minecraft.server.dedicated.PendingCommand;
import org.bukkit.command.CommandSender;
import java.util.Collection;
import java.util.Locale;
import org.apache.commons.lang.Validate;
import org.bukkit.permissions.Permission;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ServerCommandManager;
import org.bukkit.craftbukkit.util.permissions.CraftDefaultPermissions;
import org.bukkit.util.permissions.DefaultPermissions;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPluginLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginLoadOrder;
import java.util.ArrayList;
import org.bukkit.configuration.Configuration;
import java.io.Reader;
import java.io.InputStreamReader;
import org.bukkit.potion.PotionEffectType;
import net.minecraft.init.MobEffects;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.Potion;
import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.bukkit.enchantments.Enchantment;
import net.minecraft.init.Enchantments;
import org.bukkit.Bukkit;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Function;
import com.google.common.base.Charsets;
import com.google.common.collect.MapMaker;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import java.util.LinkedHashMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.craftbukkit.util.Versioning;
import net.minecraft.server.management.PlayerList;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.craftbukkit.util.CraftIconCache;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager;
import org.bukkit.Warning;
import java.io.File;
import org.bukkit.craftbukkit.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.metadata.EntityMetadataStore;
import org.bukkit.OfflinePlayer;
import java.util.UUID;
import org.yaml.snakeyaml.Yaml;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.World;
import java.util.Map;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.MinecraftServer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.craftbukkit.help.SimpleHelpMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.plugin.ServicesManager;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.Server;

public final class CraftServer implements Server
{
    private static final Player[] EMPTY_PLAYER_ARRAY;
    private final String serverName = "CraftBukkit";
    private final String serverVersion;
    private final String bukkitVersion;
    private final Logger logger;
    private final ServicesManager servicesManager;
    private final CraftScheduler scheduler;
    private final CraftSimpleCommandMap craftCommandMap = new CraftSimpleCommandMap(this); // Cauldron
    private final SimpleCommandMap commandMap;
    private final SimpleHelpMap helpMap;
    private final StandardMessenger messenger;
    private final PluginManager pluginManager;
    protected final MinecraftServer console;
    protected final DedicatedPlayerList playerList;
    private final Map<String, World> worlds;
    private YamlConfiguration configuration;
    private YamlConfiguration commandsConfiguration;
    private final Yaml yaml;
    private final Map<UUID, OfflinePlayer> offlinePlayers;
    private final EntityMetadataStore entityMetadata;
    private final PlayerMetadataStore playerMetadata;
    private final WorldMetadataStore worldMetadata;
    private int monsterSpawn;
    private int animalSpawn;
    private int waterAnimalSpawn;
    private int ambientSpawn;
    public int chunkGCPeriod;
    public int chunkGCLoadThresh;
    private File container;
    private Warning.WarningState warningState;
    private final BooleanWrapper online;
    public CraftScoreboardManager scoreboardManager;
    public boolean playerCommandState;
    private boolean printSaveWarning;
    private CraftIconCache icon;
    private boolean overrideAllCommandBlockCommands;
    private final Pattern validUserPattern;
    private final UUID invalidUserUUID;
    private final List<CraftPlayer> playerView;
    public int reloadCount;
    
    static {
        EMPTY_PLAYER_ARRAY = new Player[0];
        ConfigurationSerialization.registerClass(CraftOfflinePlayer.class);
        CraftItemFactory.instance();
    }
    
    public CraftServer(final MinecraftServer console, final PlayerList playerList) {
        this.bukkitVersion = Versioning.getBukkitVersion();
        this.logger = CatServerLogger.getLogger("Minecraft");
        this.servicesManager = new SimpleServicesManager();
        this.scheduler = new CraftScheduler();
        this.commandMap = new SimpleCommandMap(this);
        this.helpMap = new SimpleHelpMap(this);
        this.messenger = new StandardMessenger();
        this.pluginManager = new SimplePluginManager(this, this.commandMap);
        this.worlds = new LinkedHashMap<String, World>();
        this.yaml = new Yaml((BaseConstructor)new SafeConstructor());
        this.offlinePlayers = /*(Map<UUID, OfflinePlayer>)*/new MapMaker().weakValues().makeMap();
        this.entityMetadata = new EntityMetadataStore();
        this.playerMetadata = new PlayerMetadataStore();
        this.worldMetadata = new WorldMetadataStore();
        this.monsterSpawn = -1;
        this.animalSpawn = -1;
        this.waterAnimalSpawn = -1;
        this.ambientSpawn = -1;
        this.chunkGCPeriod = -1;
        this.chunkGCLoadThresh = 0;
        this.warningState = Warning.WarningState.DEFAULT;
        this.online = new BooleanWrapper();
        this.overrideAllCommandBlockCommands = false;
        this.validUserPattern = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
        this.invalidUserUUID = UUID.nameUUIDFromBytes("InvalidUsername".getBytes(Charsets.UTF_8));
        this.console = console;
        this.playerList = (DedicatedPlayerList)playerList;
        this.playerView = Collections.unmodifiableList((List<? extends CraftPlayer>)Lists.transform((List)playerList.playerEntityList, (Function)new Function<EntityPlayerMP, CraftPlayer>() {
            public CraftPlayer apply(final EntityPlayerMP player) {
                return (CraftPlayer) player.getBukkitEntity();
            }
        }));
        this.serverVersion = CraftServer.class.getPackage().getImplementationVersion();
        BooleanWrapper.access$1(this.online, console.getPropertyManager().getBooleanProperty("online-mode", true));
        Bukkit.setServer(this);
        Enchantments.SHARPNESS.getClass();
        Enchantment.stopAcceptingRegistrations();
        Potion.setPotionBrewer(new CraftPotionBrewer());
        MobEffects.BLINDNESS.getClass();
        PotionEffectType.stopAcceptingRegistrations();
        if (!Main.useConsole) {
            this.getLogger().info("Console input is disabled due to --noconsole command argument");
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.getConfigFile());
        this.configuration.options().copyDefaults(true);
        this.configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"), Charsets.UTF_8)));
        ConfigurationSection legacyAlias = null;
        if (!this.configuration.isString("aliases")) {
            legacyAlias = this.configuration.getConfigurationSection("aliases");
            this.configuration.set("aliases", "now-in-commands.yml");
        }
        this.saveConfig();
        if (this.getCommandsConfigFile().isFile()) {
            legacyAlias = null;
        }
        this.commandsConfiguration = YamlConfiguration.loadConfiguration(this.getCommandsConfigFile());
        this.commandsConfiguration.options().copyDefaults(true);
        this.commandsConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/commands.yml"), Charsets.UTF_8)));
        this.saveCommandsConfig();
        if (legacyAlias != null) {
            final ConfigurationSection aliases = this.commandsConfiguration.createSection("aliases");
            for (final String key : legacyAlias.getKeys(false)) {
                final ArrayList<String> commands = new ArrayList<String>();
                if (legacyAlias.isList(key)) {
                    for (final String command : legacyAlias.getStringList(key)) {
                        commands.add(String.valueOf(command) + " $1-");
                    }
                }
                else {
                    commands.add(String.valueOf(legacyAlias.getString(key)) + " $1-");
                }
                aliases.set(key, commands);
            }
        }
        this.saveCommandsConfig();
        this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");
        ((SimplePluginManager)this.pluginManager).useTimings(this.configuration.getBoolean("settings.plugin-profiling"));
        this.monsterSpawn = this.configuration.getInt("spawn-limits.monsters");
        this.animalSpawn = this.configuration.getInt("spawn-limits.animals");
        this.waterAnimalSpawn = this.configuration.getInt("spawn-limits.water-animals");
        this.ambientSpawn = this.configuration.getInt("spawn-limits.ambient");
        console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");
        this.warningState = Warning.WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
        this.chunkGCPeriod = this.configuration.getInt("chunk-gc.period-in-ticks");
        this.chunkGCLoadThresh = this.configuration.getInt("chunk-gc.load-threshold");
        this.loadIcon();
    }
    
    public boolean getCommandBlockOverride(final String command) {
        return this.overrideAllCommandBlockCommands || this.commandsConfiguration.getStringList("command-block-overrides").contains(command);
    }
    
    private File getConfigFile() {
        return (File)this.console.options.valueOf("bukkit-settings");
    }
    
    private File getCommandsConfigFile() {
        return (File)this.console.options.valueOf("commands-settings");
    }
    
    private void saveConfig() {
        try {
            this.configuration.save(this.getConfigFile());
        }
        catch (IOException ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + this.getConfigFile(), ex);
        }
    }
    
    private void saveCommandsConfig() {
        try {
            this.commandsConfiguration.save(this.getCommandsConfigFile());
        }
        catch (IOException ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + this.getCommandsConfigFile(), ex);
        }
    }
    
    public void loadPlugins() {
        this.pluginManager.registerInterface(JavaPluginLoader.class);
        final File pluginFolder = (File)this.console.options.valueOf("plugins");
        if (pluginFolder.exists()) {
            final Plugin[] plugins = this.pluginManager.loadPlugins(pluginFolder);
            Plugin[] array;
            for (int length = (array = plugins).length, i = 0; i < length; ++i) {
                final Plugin plugin = array[i];
                try {
                    final String message = String.format("Loading %s", plugin.getDescription().getFullName());
                    plugin.getLogger().info(message);
                    plugin.onLoad();
                }
                catch (Throwable ex) {
                    Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, String.valueOf(ex.getMessage()) + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
                }
            }
        }
        else {
            pluginFolder.mkdir();
        }
    }
    
    public void enablePlugins(final PluginLoadOrder type) {
        if (type == PluginLoadOrder.STARTUP) {
            this.helpMap.clear();
            this.helpMap.initializeGeneralTopics();
        }
        final Plugin[] plugins = this.pluginManager.getPlugins();
        Plugin[] array;
        for (int length = (array = plugins).length, i = 0; i < length; ++i) {
            final Plugin plugin = array[i];
            if (!plugin.isEnabled() && plugin.getDescription().getLoad() == type) {
                this.loadPlugin(plugin);
            }
        }
        if (type == PluginLoadOrder.POSTWORLD) {
            this.commandMap.setFallbackCommands();
            this.setVanillaCommands();
            this.commandMap.registerServerAliases();
            this.loadCustomPermissions();
            DefaultPermissions.registerCorePermissions();
            CraftDefaultPermissions.registerCorePermissions();
            this.helpMap.initializeCommands();
        }
    }
    
    public void disablePlugins() {
        this.pluginManager.disablePlugins();
    }
    
    private void setVanillaCommands() {
        final Map<String, ICommand> commands = new ServerCommandManager(this.console).getCommands();
        for (final ICommand cmd : commands.values()) {
            //TODO SPIGOT
            this.commandMap.register("minecraft", new VanillaCommandWrapper((CommandBase)cmd, I18n.translateToLocal(cmd.getCommandUsage(null))));
        }
    }
    
    private void loadPlugin(final Plugin plugin) {
        try {
            this.pluginManager.enablePlugin(plugin);
            final List<Permission> perms = plugin.getDescription().getPermissions();
            for (final Permission perm : perms) {
                try {
                    this.pluginManager.addPermission(perm);
                }
                catch (IllegalArgumentException ex) {
                    this.getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
                }
            }
        }
        catch (Throwable ex2) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, String.valueOf(ex2.getMessage()) + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex2);
        }
    }
    
    @Override
    public String getName() {
        return "CraftBukkit";
    }
    
    @Override
    public String getVersion() {
        return String.valueOf(this.serverVersion) + " (MC: " + this.console.getMinecraftVersion() + ")";
    }
    
    @Override
    public String getBukkitVersion() {
        return this.bukkitVersion;
    }
    
    @Deprecated
    @Override
    public Player[] _INVALID_getOnlinePlayers() {
        return this.getOnlinePlayers().toArray(CraftServer.EMPTY_PLAYER_ARRAY);
    }
    
    @Override
    public List<CraftPlayer> getOnlinePlayers() {
        return this.playerView;
    }
    
    @Deprecated
    @Override
    public Player getPlayer(final String name) {
        Validate.notNull((Object)name, "Name cannot be null");
        Player found = this.getPlayerExact(name);
        if (found != null) {
            return found;
        }
        final String lowerName = name.toLowerCase(Locale.ENGLISH);
        int delta = Integer.MAX_VALUE;
        for (final Player player : this.getOnlinePlayers()) {
            if (player.getName().toLowerCase(Locale.ENGLISH).startsWith(lowerName)) {
                final int curDelta = Math.abs(player.getName().length() - lowerName.length());
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) {
                    break;
                }
                continue;
            }
        }
        return found;
    }
    
    @Deprecated
    @Override
    public Player getPlayerExact(final String name) {
        Validate.notNull((Object)name, "Name cannot be null");
        final EntityPlayerMP player = this.playerList.getPlayerByUsername(name);
        return (Player) ((player != null) ? player.getBukkitEntity() : null);
    }
    
    @Override
    public Player getPlayer(final UUID id) {
        final EntityPlayerMP player = this.playerList.getPlayerByUUID(id);
        if (player != null) {
            return (Player) player.getBukkitEntity();
        }
        return null;
    }
    
    @Override
    public int broadcastMessage(final String message) {
        return this.broadcast(message, "bukkit.broadcast.user");
    }
    
    public Player getPlayer(final EntityPlayerMP entity) {
        return (Player) entity.getBukkitEntity();
    }
    
    @Deprecated
    @Override
    public List<Player> matchPlayer(final String partialName) {
        Validate.notNull((Object)partialName, "PartialName cannot be null");
        final List<Player> matchedPlayers = new ArrayList<Player>();
        for (final Player iterPlayer : this.getOnlinePlayers()) {
            final String iterPlayerName = iterPlayer.getName();
            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }
            if (!iterPlayerName.toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH))) {
                continue;
            }
            matchedPlayers.add(iterPlayer);
        }
        return matchedPlayers;
    }
    
    @Override
    public int getMaxPlayers() {
        return this.playerList.getMaxPlayers();
    }
    
    @Override
    public int getPort() {
        return this.getConfigInt("server-port", 25565);
    }
    
    @Override
    public int getViewDistance() {
        return this.getConfigInt("view-distance", 10);
    }
    
    @Override
    public String getIp() {
        return this.getConfigString("server-ip", "");
    }
    
    @Override
    public String getServerName() {
        return this.getConfigString("server-name", "Unknown Server");
    }
    
    @Override
    public String getServerId() {
        return this.getConfigString("server-id", "unnamed");
    }
    
    @Override
    public String getWorldType() {
        return this.getConfigString("level-type", "DEFAULT");
    }
    
    @Override
    public boolean getGenerateStructures() {
        return this.getConfigBoolean("generate-structures", true);
    }
    
    @Override
    public boolean getAllowEnd() {
        return this.configuration.getBoolean("settings.allow-end");
    }
    
    @Override
    public boolean getAllowNether() {
        return this.getConfigBoolean("allow-nether", true);
    }
    
    public boolean getWarnOnOverload() {
        return this.configuration.getBoolean("settings.warn-on-overload");
    }
    
    public boolean getQueryPlugins() {
        return this.configuration.getBoolean("settings.query-plugins");
    }
    
    @Override
    public boolean hasWhitelist() {
        return this.getConfigBoolean("white-list", false);
    }
    
    private String getConfigString(final String variable, final String defaultValue) {
        return this.console.getPropertyManager().getStringProperty(variable, defaultValue);
    }
    
    private int getConfigInt(final String variable, final int defaultValue) {
        return this.console.getPropertyManager().getIntProperty(variable, defaultValue);
    }
    
    private boolean getConfigBoolean(final String variable, final boolean defaultValue) {
        return this.console.getPropertyManager().getBooleanProperty(variable, defaultValue);
    }
    
    @Override
    public String getUpdateFolder() {
        return this.configuration.getString("settings.update-folder", "update");
    }
    
    @Override
    public File getUpdateFolderFile() {
        return new File((File)this.console.options.valueOf("plugins"), this.configuration.getString("settings.update-folder", "update"));
    }
    
    @Override
    public long getConnectionThrottle() {
        if (org.spigotmc.SpigotConfig.bungee) {
            return -1;
        } else {
            return this.configuration.getInt("settings.connection-throttle");
        }
    }
    
    @Override
    public int getTicksPerAnimalSpawns() {
        return this.configuration.getInt("ticks-per.animal-spawns");
    }
    
    @Override
    public int getTicksPerMonsterSpawns() {
        return this.configuration.getInt("ticks-per.monster-spawns");
    }
    
    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }
    
    @Override
    public CraftScheduler getScheduler() {
        return this.scheduler;
    }
    
    @Override
    public ServicesManager getServicesManager() {
        return this.servicesManager;
    }
    
    @Override
    public List<World> getWorlds() {
        return new ArrayList<World>(this.worlds.values());
    }
    
    public DedicatedPlayerList getHandle() {
        return this.playerList;
    }
    
    public boolean dispatchServerCommand(final CommandSender sender, final PendingCommand serverCommand) {
        if (sender instanceof Conversable) {
            final Conversable conversable = (Conversable)sender;
            if (conversable.isConversing()) {
                conversable.acceptConversationInput(serverCommand.command);
                return true;
            }
        }
        try {
            this.playerCommandState = true;
            // Cauldron start - handle bukkit/vanilla console commands
            int space = serverCommand.command.indexOf(" ");
            // if bukkit command exists then execute it over vanilla
            if (this.getCommandMap().getCommand(serverCommand.command.substring(0, space != -1 ? space : serverCommand.command.length())) != null)
            {
                return this.dispatchCommand(sender, serverCommand.command);
            }
            else { // process vanilla console command
                craftCommandMap.setVanillaConsoleSender(serverCommand.sender);
                return this.dispatchVanillaCommand(sender, serverCommand.command);
            }
            // Cauldron end
        }
        catch (Exception ex) {
            this.getLogger().log(Level.WARNING, "Unexpected exception while parsing console command \"" + serverCommand.command + '\"', ex);
            return false;
        }
        finally {
            this.playerCommandState = false;
        }
    }
    
    @Override
    public boolean dispatchCommand(final CommandSender sender, final String commandLine) {
        Validate.notNull((Object)sender, "Sender cannot be null");
        Validate.notNull((Object)commandLine, "CommandLine cannot be null");
        if (this.commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        // Cauldron start - handle vanilla commands called from plugins
        if(sender instanceof ConsoleCommandSender) {
            craftCommandMap.setVanillaConsoleSender(this.console);
        }
            
        return this.dispatchVanillaCommand(sender, commandLine);
        // Cauldron end
    }

    // Cauldron start - used to process vanilla commands
    public boolean dispatchVanillaCommand(CommandSender sender, String commandLine) {
        if (craftCommandMap.dispatch(sender, commandLine)) {
            return true;
        }

        sender.sendMessage(org.spigotmc.SpigotConfig.unknownCommandMessage); // Spigot

        return false;
    }
    // Cauldron end   

    @Override
    public void reload() {
        ++this.reloadCount;
        this.configuration = YamlConfiguration.loadConfiguration(this.getConfigFile());
        this.commandsConfiguration = YamlConfiguration.loadConfiguration(this.getCommandsConfigFile());
        final PropertyManager config = new PropertyManager(this.console.options);
        ((DedicatedServer)this.console).settings = config;
        final boolean animals = config.getBooleanProperty("spawn-animals", this.console.getCanSpawnAnimals());
        final boolean monsters = config.getBooleanProperty("spawn-monsters", this.console./*worlds.get(0)*/worldServers[0].getDifficulty() != EnumDifficulty.PEACEFUL);
        final EnumDifficulty difficulty = EnumDifficulty.getDifficultyEnum(config.getIntProperty("difficulty", this.console./*worlds.get(0)*/worldServers[0].getDifficulty().ordinal()));
        BooleanWrapper.access$1(this.online, config.getBooleanProperty("online-mode", this.console.isServerInOnlineMode()));
        this.console.setCanSpawnAnimals(config.getBooleanProperty("spawn-animals", this.console.getCanSpawnAnimals()));
        this.console.setAllowPvp(config.getBooleanProperty("pvp", this.console.isPVPEnabled()));
        this.console.setAllowFlight(config.getBooleanProperty("allow-flight", this.console.isFlightAllowed()));
        this.console.setMOTD(config.getStringProperty("motd", this.console.getMOTD()));
        this.monsterSpawn = this.configuration.getInt("spawn-limits.monsters");
        this.animalSpawn = this.configuration.getInt("spawn-limits.animals");
        this.waterAnimalSpawn = this.configuration.getInt("spawn-limits.water-animals");
        this.ambientSpawn = this.configuration.getInt("spawn-limits.ambient");
        this.warningState = Warning.WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
        this.printSaveWarning = false;
        this.console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");
        this.chunkGCPeriod = this.configuration.getInt("chunk-gc.period-in-ticks");
        this.chunkGCLoadThresh = this.configuration.getInt("chunk-gc.load-threshold");
        this.loadIcon();
        try {
            this.playerList.getBannedIPs().readSavedFile();
        }
        catch (IOException ex) {
            this.logger.log(Level.WARNING, "Failed to load banned-ips.json, " + ex.getMessage());
        }
        try {
            this.playerList.getBannedPlayers().readSavedFile();
        }
        catch (IOException ex) {
            this.logger.log(Level.WARNING, "Failed to load banned-players.json, " + ex.getMessage());
        }
        org.spigotmc.SpigotConfig.init((File) console.options.valueOf("spigot-settings")); // Spigot
        for (final WorldServer world : /*this.console.worlds*/this.console.worldServers) {
            world.worldInfo.setDifficulty(difficulty);
            world.setAllowedSpawnTypes(monsters, animals);
            if (this.getTicksPerAnimalSpawns() < 0) {
                world.ticksPerAnimalSpawns = 400L;
            }
            else {
                world.ticksPerAnimalSpawns = this.getTicksPerAnimalSpawns();
            }
            if (this.getTicksPerMonsterSpawns() < 0) {
                world.ticksPerMonsterSpawns = 1L;
            }
            else {
                world.ticksPerMonsterSpawns = this.getTicksPerMonsterSpawns();
            }
            world.spigotConfig.init();
        }
        this.pluginManager.clearPlugins();
        this.commandMap.clearCommands();
        this.resetRecipes();
        org.spigotmc.SpigotConfig.registerCommands(); // Spigot
        this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");
        for (int pollCount = 0; pollCount < 50 && this.getScheduler().getActiveWorkers().size() > 0; ++pollCount) {
            try {
                Thread.sleep(50L);
            }
            catch (InterruptedException ex2) {}
        }
        final List<BukkitWorker> overdueWorkers = this.getScheduler().getActiveWorkers();
        for (final BukkitWorker worker : overdueWorkers) {
            final Plugin plugin = worker.getOwner();
            String author = "<NoAuthorGiven>";
            if (plugin.getDescription().getAuthors().size() > 0) {
                author = plugin.getDescription().getAuthors().get(0);
            }
            this.getLogger().log(Level.SEVERE, String.format("Nag author: '%s' of '%s' about the following: %s", author, plugin.getDescription().getName(), "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin"));
        }
    }
    
    private void loadIcon() {
        this.icon = new CraftIconCache(null);
        try {
            final File file = new File(new File("."), "server-icon.png");
            if (file.isFile()) {
                this.icon = loadServerIcon0(file);
            }
        }
        catch (Exception ex) {
            this.getLogger().log(Level.WARNING, "Couldn't load server icon", ex);
        }
    }
    
    private void loadCustomPermissions() {
        final File file = new File(this.configuration.getString("settings.permissions-file"));
        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
        }
        catch (FileNotFoundException ex4) {
            try {
                file.createNewFile();
            } catch (IOException e) {
				e.printStackTrace();
			}
            finally {}
            return;
        }
        Map<String, Map<String, Object>> perms;
        try {
            perms = (Map<String, Map<String, Object>>)this.yaml.load((InputStream)stream);
        }
        catch (MarkedYAMLException ex) {
            this.getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML: " + ex.toString());
            return;
        }
        catch (Throwable ex2) {
            this.getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML.", ex2);
            return;
        }
        finally {
            try {
                stream.close();
            }
            catch (IOException ex5) {}
        }
        try {
            stream.close();
        }
        catch (IOException ex6) {}
        if (perms == null) {
            this.getLogger().log(Level.INFO, "Server permissions file " + file + " is empty, ignoring it");
            return;
        }
        final List<Permission> permsList = Permission.loadPermissions(perms, "Permission node '%s' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);
        for (final Permission perm : permsList) {
            try {
                this.pluginManager.addPermission(perm);
            }
            catch (IllegalArgumentException ex3) {
                this.getLogger().log(Level.SEVERE, "Permission in " + file + " was already defined", ex3);
            }
        }
    }
    
    @Override
    public String toString() {
        return "CraftServer{serverName=CraftBukkit,serverVersion=" + this.serverVersion + ",minecraftVersion=" + this.console.getMinecraftVersion() + '}';
    }
    
    public World createWorld(final String name, final World.Environment environment) {
        return WorldCreator.name(name).environment(environment).createWorld();
    }
    
    public World createWorld(final String name, final World.Environment environment, final long seed) {
        return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
    }
    
    public World createWorld(final String name, final World.Environment environment, final ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
    }
    
    public World createWorld(final String name, final World.Environment environment, final long seed, final ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
    }
    
    @Override
    public World createWorld(final WorldCreator creator) {
        Validate.notNull(creator, "Creator may not be null");

        String name = creator.name();
        ChunkGenerator generator = creator.generator();
        File folder = new File(getWorldContainer(), name);
        World world = getWorld(name);
        net.minecraft.world.WorldType type = net.minecraft.world.WorldType.parseWorldType(creator.type().getName());
        boolean generateStructures = creator.generateStructures();

        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }

        if (world != null) {
            return world;
        }

        boolean hardcore = false;
        WorldSettings worldSettings = new WorldSettings(creator.seed(), WorldSettings.getGameTypeById(getDefaultGameMode().getValue()), generateStructures, hardcore, type);
        WorldServer worldserver = DimensionManager.initDimension(creator, worldSettings);
        
        pluginManager.callEvent(new WorldInitEvent(worldserver.getWorld()));
        logger.info("Preparing start region for level " + (console.worlds.size() - 1) + " (Dimension: " + worldserver.provider.getDimension() + ", Seed: " + worldserver.getSeed() + ")"); // Cauldron - log dimension

        if (worldserver.getWorld().getKeepSpawnInMemory()) {
            short short1 = 196;
            long i = System.currentTimeMillis();
            for (int j = -short1; j <= short1; j += 16) {
                for (int k = -short1; k <= short1; k += 16) {
                    long l = System.currentTimeMillis();

                    if (l < i) {
                        i = l;
                    }

                    if (l > i + 1000L) {
                        int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                        int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;

                        logger.info("Preparing spawn area for " + worldserver.getWorld().getName() + ", " + (j1 * 100 / i1) + "%");
                        i = l;
                    }

                    BlockPos pos = worldserver.getSpawnPoint();
                    worldserver.getChunkProvider().loadChunk(pos.getX() + j >> 4, pos.getZ() + k >> 4);
                }
            }
        }

        pluginManager.callEvent(new WorldLoadEvent(worldserver.getWorld()));
        return worldserver.getWorld();
    }
    
    @Override
    public boolean unloadWorld(final String name, final boolean save) {
        return this.unloadWorld(this.getWorld(name), save);
    }
    
    @Override
    public boolean unloadWorld(final World world, final boolean save) {
        if (world == null) {
            return false;
        }

        net.minecraft.world.WorldServer handle = ((CraftWorld) world).getHandle();

        if (!(console.worlds.contains(handle))) {
            return false;
        }

        if (handle.playerEntities.size() > 0) {
            return false;
        }

        WorldUnloadEvent e = new WorldUnloadEvent(handle.getWorld());
        pluginManager.callEvent(e);

        if (e.isCancelled()) {
            return false;
        }

        if (save) {
            try {
                handle.saveAllChunks(true, null);
                handle.flush();
                WorldSaveEvent event = new WorldSaveEvent(handle.getWorld());
                getPluginManager().callEvent(event);
            } catch (net.minecraft.world.MinecraftException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.WorldEvent.Unload(handle)); // Cauldron - fire unload event before removing world
        worlds.remove(world.getName().toLowerCase());
        DimensionManager.setWorld(handle.provider.getDimension(), null, FMLCommonHandler.instance().getMinecraftServerInstance()); // Cauldron - remove world from DimensionManager
        return true;
    }
    
    public MinecraftServer getServer() {
        return this.console;
    }
    
    @Override
    public World getWorld(final String name) {
        Validate.notNull((Object)name, "Name cannot be null");
        return this.worlds.get(name.toLowerCase(Locale.ENGLISH));
    }
    
    @Override
    public World getWorld(final UUID uid) {
        for (final World world : this.worlds.values()) {
            if (world.getUID().equals(uid)) {
                return world;
            }
        }
        return null;
    }
    
    public void addWorld(final World world) {
        if (this.getWorld(world.getUID()) != null) {
            System.out.println("World " + world.getName() + " is a duplicate of another world and has been prevented from loading. Please delete the uid.dat file from " + world.getName() + "'s world directory if you want to be able to load the duplicate world.");
            return;
        }
        this.worlds.put(world.getName().toLowerCase(Locale.ENGLISH), world);
    }
    
    @Override
    public Logger getLogger() {
        return this.logger;
    }
    
    public ConsoleReader getReader() {
        return this.console.reader;
    }
    
    @Override
    public PluginCommand getPluginCommand(final String name) {
        final Command command = this.commandMap.getCommand(name);
        if (command instanceof PluginCommand) {
            return (PluginCommand)command;
        }
        return null;
    }
    
    @Override
    public void savePlayers() {
        this.checkSaveState();
        this.playerList.saveAllPlayerData();
    }
    
    @Override
    public void configureDbConfig(final ServerConfig config) {
        Validate.notNull((Object)config, "Config cannot be null");
        final DataSourceConfig ds = new DataSourceConfig();
        ds.setDriver(this.configuration.getString("database.driver"));
        ds.setUrl(this.configuration.getString("database.url"));
        ds.setUsername(this.configuration.getString("database.username"));
        ds.setPassword(this.configuration.getString("database.password"));
        ds.setIsolationLevel(TransactionIsolation.getLevel(this.configuration.getString("database.isolation")));
        if (ds.getDriver().contains("sqlite")) {
            config.setDatabasePlatform((DatabasePlatform)new SQLitePlatform());
            config.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
        }
        config.setDataSourceConfig(ds);
    }
    
    @Override
    public boolean addRecipe(final Recipe recipe) {
        CraftRecipe toAdd;
        if (recipe instanceof CraftRecipe) {
            toAdd = (CraftRecipe)recipe;
        }
        else if (recipe instanceof ShapedRecipe) {
            toAdd = CraftShapedRecipe.fromBukkitRecipe((ShapedRecipe)recipe);
        }
        else if (recipe instanceof ShapelessRecipe) {
            toAdd = CraftShapelessRecipe.fromBukkitRecipe((ShapelessRecipe)recipe);
        }
        else {
            if (!(recipe instanceof FurnaceRecipe)) {
                return false;
            }
            toAdd = CraftFurnaceRecipe.fromBukkitRecipe((FurnaceRecipe)recipe);
        }
        toAdd.addToCraftingManager();
        CraftingManager.getInstance();
        return true;
    }
    
    @Override
    public List<Recipe> getRecipesFor(final ItemStack result) {
        Validate.notNull((Object)result, "Result cannot be null");
        final List<Recipe> results = new ArrayList<Recipe>();
        final Iterator<Recipe> iter = this.recipeIterator();
        while (iter.hasNext()) {
            final Recipe recipe = iter.next();
            final ItemStack stack = recipe.getResult();
            if (stack.getType() != result.getType()) {
                continue;
            }
            if (result.getDurability() != -1 && result.getDurability() != stack.getDurability()) {
                continue;
            }
            results.add(recipe);
        }
        return results;
    }
    
    @Override
    public Iterator<Recipe> recipeIterator() {
        return new RecipeIterator();
    }
    
    @Override
    public void clearRecipes() {
        CraftingManager.getInstance().recipes.clear();
        FurnaceRecipes.instance().smeltingList.clear();
        FurnaceRecipes.instance().customRecipes.clear();
        FurnaceRecipes.instance().customExperience.clear();
    }
    
    @Override
    public void resetRecipes() {
        CraftingManager.getInstance().recipes = new CraftingManager().recipes;
        FurnaceRecipes.instance().smeltingList = new FurnaceRecipes().smeltingList;
        FurnaceRecipes.instance().customRecipes.clear();
        FurnaceRecipes.instance().customExperience.clear();
    }
    
    @Override
    public Map<String, String[]> getCommandAliases() {
        final ConfigurationSection section = this.commandsConfiguration.getConfigurationSection("aliases");
        final Map<String, String[]> result = new LinkedHashMap<String, String[]>();
        if (section != null) {
            for (final String key : section.getKeys(false)) {
                List<String> commands;
                if (section.isList(key)) {
                    commands = section.getStringList(key);
                }
                else {
                    commands = /*(List<String>)*/ImmutableList.of(/*(Object)*/section.getString(key));
                }
                result.put(key, commands.toArray(new String[commands.size()]));
            }
        }
        return result;
    }
    
    public void removeBukkitSpawnRadius() {
        this.configuration.set("settings.spawn-radius", null);
        this.saveConfig();
    }
    
    public int getBukkitSpawnRadius() {
        return this.configuration.getInt("settings.spawn-radius", -1);
    }
    
    @Override
    public String getShutdownMessage() {
        return this.configuration.getString("settings.shutdown-message");
    }
    
    @Override
    public int getSpawnRadius() {
        return ((DedicatedServer)this.console).settings.getIntProperty("spawn-protection", 16);
    }
    
    @Override
    public void setSpawnRadius(final int value) {
        this.configuration.set("settings.spawn-radius", value);
        this.saveConfig();
    }
    
    @Override
    public boolean getOnlineMode() {
        return this.online.value;
    }
    
    @Override
    public boolean getAllowFlight() {
        return this.console.isFlightAllowed();
    }
    
    @Override
    public boolean isHardcore() {
        return this.console.isHardcore();
    }
    
    @Override
    public boolean useExactLoginLocation() {
        return this.configuration.getBoolean("settings.use-exact-login-location");
    }
    
    public ChunkGenerator getGenerator(final String world) {
        ConfigurationSection section = this.configuration.getConfigurationSection("worlds");
        ChunkGenerator result = null;
        if (section != null) {
            section = section.getConfigurationSection(world);
            if (section != null) {
                final String name = section.getString("generator");
                if (name != null && !name.equals("")) {
                    final String[] split = name.split(":", 2);
                    final String id = (split.length > 1) ? split[1] : null;
                    final Plugin plugin = this.pluginManager.getPlugin(split[0]);
                    if (plugin == null) {
                        this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
                    }
                    else if (!plugin.isEnabled()) {
                        this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
                    }
                    else {
                        try {
                            result = plugin.getDefaultWorldGenerator(world, id);
                            if (result == null) {
                                this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world generator");
                            }
                        }
                        catch (Throwable t) {
                            plugin.getLogger().log(Level.SEVERE, "Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t);
                        }
                    }
                }
            }
        }
        return result;
    }
    
    @Deprecated
    @Override
    public CraftMapView getMap(final short id) {
        final MapStorage collection = this.console./*worlds.get(0)*/worldServers[0].mapStorage;
        final MapData worldmap = (MapData)collection.getOrLoadData(MapData.class, "map_" + id);
        if (worldmap == null) {
            return null;
        }
        return worldmap.mapView;
    }
    
    @Override
    public CraftMapView createMap(final World world) {
        Validate.notNull((Object)world, "World cannot be null");
        final net.minecraft.item.ItemStack stack = new net.minecraft.item.ItemStack(Items.MAP, 1, -1);
        final MapData worldmap = Items.FILLED_MAP.getMapData(stack, ((CraftWorld)world).getHandle());
        return worldmap.mapView;
    }
    
    @Override
    public void shutdown() {
        this.console.initiateShutdown();
    }
    
    @Override
    public int broadcast(final String message, final String permission) {
        int count = 0;
        final Set<Permissible> permissibles = this.getPluginManager().getPermissionSubscriptions(permission);
        for (final Permissible permissible : permissibles) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                final CommandSender user = (CommandSender)permissible;
                user.sendMessage(message);
                ++count;
            }
        }
        return count;
    }
    
    @Deprecated
    @Override
    public OfflinePlayer getOfflinePlayer(final String name) {
        Validate.notNull((Object)name, "Name cannot be null");
        if (!this.validUserPattern.matcher(name).matches()) {
            return new CraftOfflinePlayer(this, new GameProfile(this.invalidUserUUID, name));
        }
        OfflinePlayer result = this.getPlayerExact(name);
        if (result == null) {
            GameProfile profile = null;
            // Only fetch an online UUID in online mode
            if ( FMLCommonHandler.instance().getMinecraftServerInstance().getServer().server.getOnlineMode() || org.spigotmc.SpigotConfig.bungee )
            {
                profile = console.getPlayerProfileCache().getGameProfileForUsername( name );
            }
            if (profile == null) {
                result = this.getOfflinePlayer(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name));
            }
            else {
                result = this.getOfflinePlayer(profile);
            }
        }
        else {
            this.offlinePlayers.remove(result.getUniqueId());
        }
        return result;
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer(final UUID id) {
        Validate.notNull((Object)id, "UUID cannot be null");
        OfflinePlayer result = this.getPlayer(id);
        if (result == null) {
            result = this.offlinePlayers.get(id);
            if (result == null) {
                result = new CraftOfflinePlayer(this, new GameProfile(id, (String)null));
                this.offlinePlayers.put(id, result);
            }
        }
        else {
            this.offlinePlayers.remove(id);
        }
        return result;
    }
    
    public OfflinePlayer getOfflinePlayer(final GameProfile profile) {
        final OfflinePlayer player = new CraftOfflinePlayer(this, profile);
        this.offlinePlayers.put(profile.getId(), player);
        return player;
    }
    
    @Override
    public Set<String> getIPBans() {
        return new HashSet<String>(Arrays.asList(this.playerList.getBannedIPs().getKeys()));
    }
    
    @Override
    public void banIP(final String address) {
        Validate.notNull((Object)address, "Address cannot be null.");
        this.getBanList(BanList.Type.IP).addBan(address, null, null, null);
    }
    
    @Override
    public void unbanIP(final String address) {
        Validate.notNull((Object)address, "Address cannot be null.");
        this.getBanList(BanList.Type.IP).pardon(address);
    }
    
    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        final Set<OfflinePlayer> result = new HashSet<OfflinePlayer>();
        for (final UserListEntry<GameProfile> entry : (/*(UserList<K, UserListBansEntry>)*/this.playerList.getBannedPlayers()).getValuesCB()) {
            result.add(this.getOfflinePlayer(entry.getValue()));
        }
        return result;
    }
    
    @Override
    public BanList getBanList(final BanList.Type type) {
        Validate.notNull((Object)type, "Type cannot be null");
        switch (type) {
            case IP: {
                return new CraftIpBanList(this.playerList.getBannedIPs());
            }
            default: {
                return new CraftProfileBanList(this.playerList.getBannedPlayers());
            }
        }
    }
    
    @Override
    public void setWhitelist(final boolean value) {
        this.playerList.setWhiteListEnabled(value);
        this.console.getPropertyManager().setProperty("white-list", value);
    }
    
    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        final Set<OfflinePlayer> result = new LinkedHashSet<OfflinePlayer>();
        for (final UserListEntry<GameProfile> entry : (/*(UserList<K, UserListWhitelistEntry>)*/this.playerList.getWhitelistedPlayers()).getValuesCB()) {
            result.add(this.getOfflinePlayer(entry.getValue()));
        }
        return result;
    }
    
    @Override
    public Set<OfflinePlayer> getOperators() {
        final Set<OfflinePlayer> result = new HashSet<OfflinePlayer>();
        for (final UserListEntry<GameProfile> entry : (/*(UserList<K, UserListOpsEntry>)*/this.playerList.getOppedPlayers()).getValuesCB()) {
            result.add(this.getOfflinePlayer(entry.getValue()));
        }
        return result;
    }
    
    @Override
    public void reloadWhitelist() {
        this.playerList.reloadWhitelist();
    }
    
    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(this.console./*worlds.get(0)*/worldServers[0].getWorldInfo().getGameType().getID());
    }
    
    @Override
    public void setDefaultGameMode(final GameMode mode) {
        Validate.notNull((Object)mode, "Mode cannot be null");
        for (final World world : this.getWorlds()) {
            ((CraftWorld)world).getHandle().worldInfo.setGameType(GameType.getByID(mode.getValue()));
        }
    }
    
    @Override
    public ConsoleCommandSender getConsoleSender() {
        return this.console.console;
    }
    
    public EntityMetadataStore getEntityMetadata() {
        return this.entityMetadata;
    }
    
    public PlayerMetadataStore getPlayerMetadata() {
        return this.playerMetadata;
    }
    
    public WorldMetadataStore getWorldMetadata() {
        return this.worldMetadata;
    }
    
    @Override
    public File getWorldContainer() {
        if (this.getServer().anvilFile != null) {
            return this.getServer().anvilFile;
        }
        if (this.container == null) {
            this.container = new File(this.configuration.getString("settings.world-container", "."));
        }
        return this.container;
    }
    
    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        final SaveHandler storage = (SaveHandler)this.console./*worlds.get(0)*/worldServers[0].getSaveHandler();
        final String[] files = storage.getPlayerDir().list(new DatFileFilter());
        final Set<OfflinePlayer> players = new HashSet<OfflinePlayer>();
        String[] array;
        for (int length = (array = files).length, i = 0; i < length; ++i) {
            final String file = array[i];
            try {
                players.add(this.getOfflinePlayer(UUID.fromString(file.substring(0, file.length() - 4))));
            }
            catch (IllegalArgumentException ex) {}
        }
        players.addAll(this.getOnlinePlayers());
        return players.toArray(new OfflinePlayer[players.size()]);
    }
    
    @Override
    public Messenger getMessenger() {
        return this.messenger;
    }
    
    @Override
    public void sendPluginMessage(final Plugin source, final String channel, final byte[] message) {
        StandardMessenger.validatePluginMessage(this.getMessenger(), source, channel, message);
        for (final Player player : this.getOnlinePlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }
    
    @Override
    public Set<String> getListeningPluginChannels() {
        final Set<String> result = new HashSet<String>();
        for (final Player player : this.getOnlinePlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }
        return result;
    }
    
    @Override
    public Inventory createInventory(final InventoryHolder owner, final InventoryType type) {
        return new CraftInventoryCustom(owner, type);
    }
    
    @Override
    public Inventory createInventory(final InventoryHolder owner, final InventoryType type, final String title) {
        return new CraftInventoryCustom(owner, type, title);
    }
    
    @Override
    public Inventory createInventory(final InventoryHolder owner, final int size) throws IllegalArgumentException {
        Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
        return new CraftInventoryCustom(owner, size);
    }
    
    @Override
    public Inventory createInventory(final InventoryHolder owner, final int size, final String title) throws IllegalArgumentException {
        Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
        return new CraftInventoryCustom(owner, size, title);
    }
    
    @Override
    public HelpMap getHelpMap() {
        return this.helpMap;
    }
    
    public SimpleCommandMap getCommandMap() {
        return this.commandMap;
    }
    
    // Cauldron start
    public CraftSimpleCommandMap getCraftCommandMap() {
        return craftCommandMap;
    }
    // Cauldron end

    
    @Override
    public int getMonsterSpawnLimit() {
        return this.monsterSpawn;
    }
    
    @Override
    public int getAnimalSpawnLimit() {
        return this.animalSpawn;
    }
    
    @Override
    public int getWaterAnimalSpawnLimit() {
        return this.waterAnimalSpawn;
    }
    
    @Override
    public int getAmbientSpawnLimit() {
        return this.ambientSpawn;
    }
    
    @Override
    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(this.console.primaryThread);
    }
    
    @Override
    public String getMotd() {
        return this.console.getMOTD();
    }
    
    @Override
    public Warning.WarningState getWarningState() {
        return this.warningState;
    }
    
    public List<String> tabComplete(final ICommandSender sender, final String message) {
        if (!(sender instanceof EntityPlayerMP)) {
            return /*(List<String>)*/ImmutableList.of();
        }
        final Player player = ((EntityPlayerMP)sender).getBukkitEntity();
        List<String> offers;
        if (message.startsWith("/")) {
            offers = this.tabCompleteCommand(player, message);
        }
        else {
            offers = this.tabCompleteChat(player, message);
        }
        final TabCompleteEvent tabEvent = new TabCompleteEvent(player, message, offers);
        this.getPluginManager().callEvent(tabEvent);
        return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
    }
    
    public List<String> tabCompleteCommand(final Player player, final String message) {
        List<String> completions = null;
        try {
            completions = this.getCommandMap().tabComplete(player, message.substring(1));
            // CatServer start
            List<String> vanillaCompletions = this.getCraftCommandMap().tabComplete(player, message.substring(1));
            if(completions != null && vanillaCompletions != null) completions.addAll(vanillaCompletions);
            // CatServer end
        }
        catch (CommandException ex) {
            player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to tab-complete this command");
            this.getLogger().log(Level.SEVERE, "Exception when " + player.getName() + " attempted to tab complete " + message, ex);
        }
        return (List<String>)((completions == null) ? ImmutableList.of() : completions);
    }
    
    public List<String> tabCompleteChat(final Player player, final String message) {
        final List<String> completions = new ArrayList<String>();
        final PlayerChatTabCompleteEvent event = new PlayerChatTabCompleteEvent(player, message, completions);
        final String token = event.getLastToken();
        for (final Player p : this.getOnlinePlayers()) {
            if (player.canSee(p) && StringUtil.startsWithIgnoreCase(p.getName(), token)) {
                completions.add(p.getName());
            }
        }
        this.pluginManager.callEvent(event);
        final Iterator<?> it = completions.iterator();
        while (it.hasNext()) {
            final Object current = it.next();
            if (!(current instanceof String)) {
                it.remove();
            }
        }
        Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
        return completions;
    }
    
    @Override
    public CraftItemFactory getItemFactory() {
        return CraftItemFactory.instance();
    }
    
    @Override
    public CraftScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }
    
    public void checkSaveState() {
        if (this.playerCommandState || this.printSaveWarning || this.console.autosavePeriod <= 0) {
            return;
        }
        this.printSaveWarning = true;
        this.getLogger().log(Level.WARNING, "A manual (plugin-induced) save has been detected while server is configured to auto-save. This may affect performance.", (this.warningState == Warning.WarningState.ON) ? new Throwable() : null);
    }
    
    @Override
    public CraftIconCache getServerIcon() {
        return this.icon;
    }
    
    @Override
    public CraftIconCache loadServerIcon(final File file) throws Exception {
        Validate.notNull((Object)file, "File cannot be null");
        if (!file.isFile()) {
            throw new IllegalArgumentException(file + " is not a file");
        }
        return loadServerIcon0(file);
    }
    
    static CraftIconCache loadServerIcon0(final File file) throws Exception {
        return loadServerIcon0(ImageIO.read(file));
    }
    
    @Override
    public CraftIconCache loadServerIcon(final BufferedImage image) throws Exception {
        Validate.notNull((Object)image, "Image cannot be null");
        return loadServerIcon0(image);
    }
    
    static CraftIconCache loadServerIcon0(final BufferedImage image) throws Exception {
        final ByteBuf bytebuf = Unpooled.buffer();
        Validate.isTrue(image.getWidth() == 64, "Must be 64 pixels wide");
        Validate.isTrue(image.getHeight() == 64, "Must be 64 pixels high");
        ImageIO.write(image, "PNG", (OutputStream)new ByteBufOutputStream(bytebuf));
        final ByteBuf bytebuf2 = Base64.encode(bytebuf);
        return new CraftIconCache("data:image/png;base64," + bytebuf2.toString(Charsets.UTF_8));
    }
    
    @Override
    public void setIdleTimeout(final int threshold) {
        this.console.setPlayerIdleTimeout(threshold);
    }
    
    @Override
    public int getIdleTimeout() {
        return this.console.getMaxPlayerIdleMinutes();
    }
    
    @Override
    public ChunkGenerator.ChunkData createChunkData(final World world) {
        return new CraftChunkData(world);
    }
    
    @Override
    public BossBar createBossBar(final String title, final BarColor color, final BarStyle style, final BarFlag... flags) {
        return new CraftBossBar(title, color, style, flags);
    }
    
    @Deprecated
    @Override
    public UnsafeValues getUnsafe() {
        return CraftMagicNumbers.INSTANCE;
    }

    private final Spigot spigot = new Spigot()
    {

        @Override
        public YamlConfiguration getConfig()
        {
            return org.spigotmc.SpigotConfig.config;
        }

        @Override
        public void restart() {
            org.spigotmc.RestartCommand.restart();
        }

        //TODO BUNDGE.API
        /*@Override
        public void broadcast(BaseComponent component) {
            for (Player player : getOnlinePlayers()) {
                player.spigot().sendMessage(component);
            }
        }

        @Override
        public void broadcast(BaseComponent... components) {
            for (Player player : getOnlinePlayers()) {
                player.spigot().sendMessage(components);
            }
        }*/
    };

    public Spigot spigot()
    {
        return this.spigot;
    }

    private static final class BooleanWrapper
    {
        private boolean value;
        
        private BooleanWrapper() {
            this.value = true;
        }
        
        static /* synthetic */ void access$1(final BooleanWrapper booleanWrapper, final boolean value) {
            booleanWrapper.value = value;
        }
    }
}
