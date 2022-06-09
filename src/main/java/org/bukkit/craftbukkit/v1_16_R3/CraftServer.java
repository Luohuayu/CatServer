package org.bukkit.craftbukkit.v1_16_R3;

import catserver.server.CatServer;
import catserver.server.remapper.ReflectionTransformer;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.impl.ReloadCommand;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.loot.LootTableManager;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.potion.Effects;
import net.minecraft.server.CustomServerBossInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPropertiesProvider;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PendingCommand;
import net.minecraft.server.dedicated.ServerProperties;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.registry.WorldSettingsImport;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.Difficulty;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraft.world.spawner.*;
import net.minecraft.world.storage.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.World;
import org.bukkit.Warning.WarningState;
import org.bukkit.World.Environment;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.conversations.Conversable;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R3.boss.CraftBossBar;
import org.bukkit.craftbukkit.v1_16_R3.boss.CraftKeyedBossbar;
import org.bukkit.craftbukkit.v1_16_R3.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_16_R3.command.CraftCommandMap;
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.generator.CraftChunkData;
import org.bukkit.craftbukkit.v1_16_R3.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_16_R3.inventory.*;
import org.bukkit.craftbukkit.v1_16_R3.inventory.util.CraftInventoryCreator;
import org.bukkit.craftbukkit.v1_16_R3.map.CraftMapView;
import org.bukkit.craftbukkit.v1_16_R3.metadata.EntityMetadataStore;
import org.bukkit.craftbukkit.v1_16_R3.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.v1_16_R3.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionBrewer;
import org.bukkit.craftbukkit.v1_16_R3.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_16_R3.tag.CraftBlockTag;
import org.bukkit.craftbukkit.v1_16_R3.tag.CraftFluidTag;
import org.bukkit.craftbukkit.v1_16_R3.tag.CraftItemTag;
import org.bukkit.craftbukkit.v1_16_R3.util.*;
import org.bukkit.craftbukkit.v1_16_R3.util.permissions.CraftDefaultPermissions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.Potion;
import org.bukkit.scheduler.BukkitWorker;
import org.bukkit.util.StringUtil;
import org.bukkit.util.permissions.DefaultPermissions;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CraftServer implements Server {
    private final String serverName = "CatServer";
    public static String serverVersion;
    private final String bukkitVersion = Versioning.getBukkitVersion();
    private final Logger logger = Logger.getLogger("Minecraft");
    private final ServicesManager servicesManager = new SimpleServicesManager();
    private final CraftScheduler scheduler = new CraftScheduler();
    private final CraftCommandMap commandMap = new CraftCommandMap(this);
    private final SimpleHelpMap helpMap = new SimpleHelpMap(this);
    private final StandardMessenger messenger = new StandardMessenger();
    private final SimplePluginManager pluginManager = new SimplePluginManager(this, commandMap);
    protected final DedicatedServer console;
    protected final DedicatedPlayerList playerList;
    private final Map<String, World> worlds = new LinkedHashMap<String, World>();
    private YamlConfiguration configuration;
    private YamlConfiguration commandsConfiguration;
    private final Yaml yaml = new Yaml(new SafeConstructor());
    private final Map<UUID, OfflinePlayer> offlinePlayers = new MapMaker().weakValues().makeMap();
    private final EntityMetadataStore entityMetadata = new EntityMetadataStore();
    private final PlayerMetadataStore playerMetadata = new PlayerMetadataStore();
    private final WorldMetadataStore worldMetadata = new WorldMetadataStore();
    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private int waterAmbientSpawn = -1;
    private int ambientSpawn = -1;
    private File container;
    private WarningState warningState = WarningState.DEFAULT;
    public String minimumAPI;
    public CraftScoreboardManager scoreboardManager;
    public boolean playerCommandState;
    private boolean printSaveWarning;
    private CraftIconCache icon;
    private boolean overrideAllCommandBlockCommands = false;
    public boolean ignoreVanillaPermissions = false;
    private final List<CraftPlayer> playerView;
    public int reloadCount;
    // LoliServer start
    public boolean isVanillaCommandRegistered = false;
    public boolean isSyncingCommand = false;
    // LoliServer end

    static {
        ConfigurationSerialization.registerClass(CraftOfflinePlayer.class);
        CraftItemFactory.instance();
    }

    public CraftServer(DedicatedServer console, PlayerList playerList) {
        this.console = console;
        this.playerList = (DedicatedPlayerList) playerList;
        this.playerView = Collections.unmodifiableList(Lists.transform(playerList.players, new Function<ServerPlayerEntity, CraftPlayer>() {
            @Override
            public CraftPlayer apply(ServerPlayerEntity player) {
                return player.getBukkitEntity();
            }
        }));
        this.serverVersion = (CraftServer.class.getPackage().getImplementationVersion() != null) ? CraftServer.class.getPackage().getImplementationVersion() : "unknown";

        Bukkit.setServer(this);

        // Register all the Enchantments and PotionTypes now so we can stop new registration immediately after
        Enchantments.SHARPNESS.getClass();

        Potion.setPotionBrewer(new CraftPotionBrewer());
        Effects.BLINDNESS.getClass();
        // Ugly hack :(

        configuration = YamlConfiguration.loadConfiguration(getConfigFile());
        configuration.options().copyDefaults(true);
        configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"), Charsets.UTF_8)));
        ConfigurationSection legacyAlias = null;
        if (!configuration.isString("aliases")) {
            legacyAlias = configuration.getConfigurationSection("aliases");
            configuration.set("aliases", "now-in-commands.yml");
        }
        saveConfig();
        if (getCommandsConfigFile().isFile()) {
            legacyAlias = null;
        }
        commandsConfiguration = YamlConfiguration.loadConfiguration(getCommandsConfigFile());
        commandsConfiguration.options().copyDefaults(true);
        commandsConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/commands.yml"), Charsets.UTF_8)));
        saveCommandsConfig();

        // Migrate aliases from old file and add previously implicit $1- to pass all arguments
        if (legacyAlias != null) {
            ConfigurationSection aliases = commandsConfiguration.createSection("aliases");
            for (String key : legacyAlias.getKeys(false)) {
                ArrayList<String> commands = new ArrayList<String>();

                if (legacyAlias.isList(key)) {
                    for (String command : legacyAlias.getStringList(key)) {
                        commands.add(command + " $1-");
                    }
                } else {
                    commands.add(legacyAlias.getString(key) + " $1-");
                }

                aliases.set(key, commands);
            }
        }

        saveCommandsConfig();
        overrideAllCommandBlockCommands = commandsConfiguration.getStringList("command-block-overrides").contains("*");
        ignoreVanillaPermissions = commandsConfiguration.getBoolean("ignore-vanilla-permissions");
        pluginManager.useTimings(configuration.getBoolean("settings.plugin-profiling"));
        monsterSpawn = configuration.getInt("spawn-limits.monsters");
        animalSpawn = configuration.getInt("spawn-limits.animals");
        waterAnimalSpawn = configuration.getInt("spawn-limits.water-animals");
        waterAmbientSpawn = configuration.getInt("spawn-limits.water-ambient");
        ambientSpawn = configuration.getInt("spawn-limits.ambient");
        console.autosavePeriod = configuration.getInt("ticks-per.autosave");
        warningState = WarningState.value(configuration.getString("settings.deprecated-verbose"));
        TicketType.PLUGIN.timeout = configuration.getInt("chunk-gc.period-in-ticks");
        minimumAPI = configuration.getString("settings.minimum-api");
        loadIcon();
        CatServer.getConfig().loadConfig(); // CatServer
    }

    public boolean getCommandBlockOverride(String command) {
        return overrideAllCommandBlockCommands || commandsConfiguration.getStringList("command-block-overrides").contains(command);
    }

    private File getConfigFile() {
        return (File) console.options.valueOf("bukkit-settings");
    }

    private File getCommandsConfigFile() {
        return (File) console.options.valueOf("commands-settings");
    }

    private void saveConfig() {
        try {
            configuration.save(getConfigFile());
        } catch (IOException ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + getConfigFile(), ex);
        }
    }

    private void saveCommandsConfig() {
        try {
            commandsConfiguration.save(getCommandsConfigFile());
        } catch (IOException ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + getCommandsConfigFile(), ex);
        }
    }

    public void loadPlugins() {
        ReflectionTransformer.init();
        pluginManager.registerInterface(JavaPluginLoader.class);

        File pluginFolder = (File) console.options.valueOf("plugins");

        if (pluginFolder.exists()) {
            Plugin[] plugins = pluginManager.loadPlugins(pluginFolder);
            for (Plugin plugin : plugins) {
                try {
                    String message = String.format("Loading %s", plugin.getDescription().getFullName());
                    plugin.getLogger().info(message);
                    plugin.onLoad();
                } catch (Throwable ex) {
                    Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
                }
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    public void enablePlugins(PluginLoadOrder type) {
        if (type == PluginLoadOrder.STARTUP) {
            helpMap.clear();
            helpMap.initializeGeneralTopics();
        }

        Plugin[] plugins = pluginManager.getPlugins();

        for (Plugin plugin : plugins) {
            if ((!plugin.isEnabled()) && (plugin.getDescription().getLoad() == type)) {
                enablePlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            // Spigot start - Allow vanilla commands to be forced to be the main command
            setVanillaCommands(true);
            commandMap.setFallbackCommands();
            setVanillaCommands(false);
            isVanillaCommandRegistered = true; // LoliServer
            // Spigot end
            commandMap.registerServerAliases();
            DefaultPermissions.registerCorePermissions();
            CraftDefaultPermissions.registerCorePermissions();
            loadCustomPermissions();
            helpMap.initializeCommands();
            syncCommands();
        }
    }

    public void disablePlugins() {
        pluginManager.disablePlugins();
    }

    private void setVanillaCommands(boolean first) { // Spigot
        Commands dispatcher = console.vanillaCommandDispatcher;

        // Build a list of all Vanilla commands and create wrappers
        for (CommandNode<CommandSource> cmd : dispatcher.getDispatcher().getRoot().getChildren()) {
            // Spigot start
            VanillaCommandWrapper wrapper = new VanillaCommandWrapper(dispatcher, cmd);
            if (org.spigotmc.SpigotConfig.replaceCommands.contains( wrapper.getName() ) ) {
                if (first) {
                    commandMap.register("minecraft", wrapper);
                }
            } else if (!first) {
                commandMap.register("minecraft", wrapper);
            }
            // Spigot end
        }
    }

    public void syncCommands() {
        // Clear existing commands
        Commands dispatcher = console.resources.commands = new Commands();

        isSyncingCommand = true; // LoliServer
        // Register all commands, vanilla ones will be using the old dispatcher references
        for (Map.Entry<String, Command> entry : commandMap.getKnownCommands().entrySet()) {
            String label = entry.getKey();
            Command command = entry.getValue();

            if (command instanceof VanillaCommandWrapper) {
                LiteralCommandNode<CommandSource> node = (LiteralCommandNode<CommandSource>) ((VanillaCommandWrapper) command).vanillaCommand;
                if (!node.getLiteral().equals(label)) {
                    LiteralCommandNode<CommandSource> clone = new LiteralCommandNode(label, node.getCommand(), node.getRequirement(), node.getRedirect(), node.getRedirectModifier(), node.isFork());

                    for (CommandNode<CommandSource> child : node.getChildren()) {
                        clone.addChild(child);
                    }
                    node = clone;
                }

                dispatcher.getDispatcher().getRoot().addChild(node);
            } else {
                new BukkitCommandWrapper(this, entry.getValue()).register(dispatcher.getDispatcher(), label);
            }
        }
        isSyncingCommand = false; // LoliServer

        // Refresh commands
        for (ServerPlayerEntity player : getHandle().players) {
            dispatcher.sendCommands(player);
        }
    }

    private void enablePlugin(Plugin plugin) {
        try {
            List<Permission> perms = plugin.getDescription().getPermissions();

            for (Permission perm : perms) {
                try {
                    pluginManager.addPermission(perm, false);
                } catch (IllegalArgumentException ex) {
                    getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
                }
            }
            pluginManager.dirtyPermissibles();

            pluginManager.enablePlugin(plugin);
        } catch (Throwable ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
    }

    @Override
    public String getName() {
        return serverName;
    }

    @Override
    public String getVersion() {
        return serverVersion + " (MC: " + console.getServerVersion() + ")";
    }

    @Override
    public String getBukkitVersion() {
        return bukkitVersion;
    }

    @Override
    public List<CraftPlayer> getOnlinePlayers() {
        return this.playerView;
    }

    @Override
    @Deprecated
    public Player getPlayer(final String name) {
        Validate.notNull(name, "Name cannot be null");

        Player found = getPlayerExact(name);
        // Try for an exact match first.
        if (found != null) {
            return found;
        }

        String lowerName = name.toLowerCase(java.util.Locale.ENGLISH);
        int delta = Integer.MAX_VALUE;
        for (Player player : getOnlinePlayers()) {
            if (player.getName().toLowerCase(java.util.Locale.ENGLISH).startsWith(lowerName)) {
                int curDelta = Math.abs(player.getName().length() - lowerName.length());
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) break;
            }
        }
        return found;
    }

    @Override
    @Deprecated
    public Player getPlayerExact(String name) {
        Validate.notNull(name, "Name cannot be null");

        ServerPlayerEntity player = playerList.getPlayerByName(name);
        return (player != null) ? player.getBukkitEntity() : null;
    }

    @Override
    public Player getPlayer(UUID id) {
        ServerPlayerEntity player = playerList.getPlayer(id);

        if (player != null) {
            return player.getBukkitEntity();
        }

        return null;
    }

    @Override
    public int broadcastMessage(String message) {
        return broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public Player getPlayer(final ServerPlayerEntity entity) {
        return entity.getBukkitEntity();
    }

    @Override
    @Deprecated
    public List<Player> matchPlayer(String partialName) {
        Validate.notNull(partialName, "PartialName cannot be null");

        List<Player> matchedPlayers = new ArrayList<Player>();

        for (Player iterPlayer : this.getOnlinePlayers()) {
            String iterPlayerName = iterPlayer.getName();

            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                // Exact match
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }
            if (iterPlayerName.toLowerCase(java.util.Locale.ENGLISH).contains(partialName.toLowerCase(java.util.Locale.ENGLISH))) {
                // Partial match
                matchedPlayers.add(iterPlayer);
            }
        }

        return matchedPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return playerList.getMaxPlayers();
    }

    // NOTE: These are dependent on the corresponding call in MinecraftServer
    // so if that changes this will need to as well
    @Override
    public int getPort() {
        return this.getServer().getPort();
    }

    @Override
    public int getViewDistance() {
        return this.getProperties().viewDistance;
    }

    @Override
    public String getIp() {
        return this.getServer().getLocalIp();
    }

    @Override
    public String getWorldType() {
        return this.getProperties().properties.getProperty("level-type");
    }

    @Override
    public boolean getGenerateStructures() {
        return this.getProperties().worldGenSettings.generateFeatures();
    }

    @Override
    public int getMaxWorldSize() {
        return this.getProperties().maxWorldSize;
    }

    @Override
    public boolean getAllowEnd() {
        return this.configuration.getBoolean("settings.allow-end");
    }

    @Override
    public boolean getAllowNether() {
        return this.getServer().isNetherEnabled();
    }

    public boolean getWarnOnOverload() {
        return this.configuration.getBoolean("settings.warn-on-overload");
    }

    public boolean getQueryPlugins() {
        return this.configuration.getBoolean("settings.query-plugins");
    }

    @Override
    public boolean hasWhitelist() {
        return this.getProperties().whiteList.get();
    }

    // NOTE: Temporary calls through to server.properies until its replaced
    private ServerProperties getProperties() {
        return this.console.getProperties();
    }
    // End Temporary calls

    @Override
    public String getUpdateFolder() {
        return this.configuration.getString("settings.update-folder", "update");
    }

    @Override
    public File getUpdateFolderFile() {
        return new File((File) console.options.valueOf("plugins"), this.configuration.getString("settings.update-folder", "update"));
    }

    @Override
    public long getConnectionThrottle() {
        // Spigot Start - Automatically set connection throttle for bungee configurations
        if (org.spigotmc.SpigotConfig.bungee) {
            return -1;
        } else {
            return this.configuration.getInt("settings.connection-throttle");
        }
        // Spigot End
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
    public int getTicksPerWaterSpawns() {
        return this.configuration.getInt("ticks-per.water-spawns");
    }

    @Override
    public int getTicksPerWaterAmbientSpawns() {
        return this.configuration.getInt("ticks-per.water-ambient-spawns");
    }

    @Override
    public int getTicksPerAmbientSpawns() {
        return this.configuration.getInt("ticks-per.ambient-spawns");
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public CraftScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public ServicesManager getServicesManager() {
        return servicesManager;
    }

    @Override
    public List<World> getWorlds() {
        return new ArrayList<World>(worlds.values());
    }

    public DedicatedPlayerList getHandle() {
        return playerList;
    }

    // NOTE: Should only be called from DedicatedServer.ah()
    public boolean dispatchServerCommand(CommandSender sender, PendingCommand serverCommand) {
        if (sender instanceof Conversable) {
            Conversable conversable = (Conversable) sender;

            if (conversable.isConversing()) {
                conversable.acceptConversationInput(serverCommand.msg);
                return true;
            }
        }
        try {
            this.playerCommandState = true;
            return dispatchCommand(sender, serverCommand.msg);
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Unexpected exception while parsing console command \"" + serverCommand.msg + '"', ex);
            return false;
        } finally {
            this.playerCommandState = false;
        }
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(commandLine, "CommandLine cannot be null");

        if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        // Spigot start
        if (!org.spigotmc.SpigotConfig.unknownCommandMessage.isEmpty()) {
            sender.sendMessage(org.spigotmc.SpigotConfig.unknownCommandMessage);
        }
        // Spigot end

        return false;
    }

    @Override
    public void reload() {
        reloadCount++;
        configuration = YamlConfiguration.loadConfiguration(getConfigFile());
        commandsConfiguration = YamlConfiguration.loadConfiguration(getCommandsConfigFile());

        console.settings = new ServerPropertiesProvider(console.registryAccess(), Paths.get("server.properties"));
        ServerProperties config = console.settings.getProperties();

        console.setPvpAllowed(config.pvp);
        console.setFlightAllowed(config.allowFlight);
        console.setMotd(config.motd);
        monsterSpawn = configuration.getInt("spawn-limits.monsters");
        animalSpawn = configuration.getInt("spawn-limits.animals");
        waterAnimalSpawn = configuration.getInt("spawn-limits.water-animals");
        waterAmbientSpawn = configuration.getInt("spawn-limits.water-ambient");
        ambientSpawn = configuration.getInt("spawn-limits.ambient");
        warningState = WarningState.value(configuration.getString("settings.deprecated-verbose"));
        TicketType.PLUGIN.timeout = configuration.getInt("chunk-gc.period-in-ticks");
        minimumAPI = configuration.getString("settings.minimum-api");
        printSaveWarning = false;
        console.autosavePeriod = configuration.getInt("ticks-per.autosave");
        loadIcon();

        try {
            playerList.getIpBans().load();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to load banned-ips.json, " + ex.getMessage());
        }
        try {
            playerList.getBans().load();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to load banned-ips.json, " + ex.getMessage());
        }

        org.spigotmc.SpigotConfig.init((File) console.options.valueOf("spigot-settings")); // Spigot
        for (ServerWorld world : console.getAllLevels()) {
            world.getServer().getWorldData().setDifficulty(config.difficulty);
            world.setSpawnSettings(config.spawnMonsters, config.spawnAnimals);
            if (this.getTicksPerAnimalSpawns() < 0) {
                world.ticksPerAnimalSpawns = 400;
            } else {
                world.ticksPerAnimalSpawns = this.getTicksPerAnimalSpawns();
            }

            if (this.getTicksPerMonsterSpawns() < 0) {
                world.ticksPerMonsterSpawns = 1;
            } else {
                world.ticksPerMonsterSpawns = this.getTicksPerMonsterSpawns();
            }

            if (this.getTicksPerWaterSpawns() < 0) {
                world.ticksPerWaterSpawns = 1;
            } else {
                world.ticksPerWaterSpawns = this.getTicksPerWaterSpawns();
            }

            if (this.getTicksPerWaterAmbientSpawns() < 0) {
                world.ticksPerWaterAmbientSpawns = 1;
            } else {
                world.ticksPerWaterAmbientSpawns = this.getTicksPerWaterAmbientSpawns();
            }

            if (this.getTicksPerAmbientSpawns() < 0) {
                world.ticksPerAmbientSpawns = 1;
            } else {
                world.ticksPerAmbientSpawns = this.getTicksPerAmbientSpawns();
            }
            world.spigotConfig.init(); // Spigot
        }

        pluginManager.clearPlugins();
        commandMap.clearCommands();
        resetRecipes();
        reloadData();
        org.spigotmc.SpigotConfig.registerCommands(); // Spigot
        overrideAllCommandBlockCommands = commandsConfiguration.getStringList("command-block-overrides").contains("*");
        ignoreVanillaPermissions = commandsConfiguration.getBoolean("ignore-vanilla-permissions");

        int pollCount = 0;

        // Wait for at most 2.5 seconds for plugins to close their threads
        while (pollCount < 50 && getScheduler().getActiveWorkers().size() > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            pollCount++;
        }

        List<BukkitWorker> overdueWorkers = getScheduler().getActiveWorkers();
        for (BukkitWorker worker : overdueWorkers) {
            Plugin plugin = worker.getOwner();
            String author = "<NoAuthorGiven>";
            if (plugin.getDescription().getAuthors().size() > 0) {
                author = plugin.getDescription().getAuthors().get(0);
            }
            getLogger().log(Level.SEVERE, String.format(
                    "Nag author: '%s' of '%s' about the following: %s",
                    author,
                    plugin.getDescription().getName(),
                    "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin"
            ));
        }
        loadPlugins();
        enablePlugins(PluginLoadOrder.STARTUP);
        enablePlugins(PluginLoadOrder.POSTWORLD);
        getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.RELOAD));
    }

    @Override
    public void reloadData() {
        ReloadCommand.reload(console);
    }

    private void loadIcon() {
        icon = new CraftIconCache(null);
        try {
            final File file = new File(new File("."), "server-icon.png");
            if (file.isFile()) {
                icon = loadServerIcon0(file);
            }
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Couldn't load server icon", ex);
        }
    }

    @SuppressWarnings({"unchecked", "finally"})
    private void loadCustomPermissions() {
        File file = new File(configuration.getString("settings.permissions-file"));
        FileInputStream stream;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            try {
                file.createNewFile();
            } finally {
                return;
            }
        }

        Map<String, Map<String, Object>> perms;

        try {
            perms = (Map<String, Map<String, Object>>) yaml.load(stream);
        } catch (MarkedYAMLException ex) {
            getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML: " + ex.toString());
            return;
        } catch (Throwable ex) {
            getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML.", ex);
            return;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
            }
        }

        if (perms == null) {
            getLogger().log(Level.INFO, "Server permissions file " + file + " is empty, ignoring it");
            return;
        }

        List<Permission> permsList = Permission.loadPermissions(perms, "Permission node '%s' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);

        for (Permission perm : permsList) {
            try {
                pluginManager.addPermission(perm);
            } catch (IllegalArgumentException ex) {
                getLogger().log(Level.SEVERE, "Permission in " + file + " was already defined", ex);
            }
        }
    }

    @Override
    public String toString() {
        return "CraftServer{" + "serverName=" + serverName + ",serverVersion=" + serverVersion + ",minecraftVersion=" + console.getServerVersion() + '}';
    }

    public World createWorld(String name, World.Environment environment) {
        return WorldCreator.name(name).environment(environment).createWorld();
    }

    public World createWorld(String name, World.Environment environment, long seed) {
        return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
    }

    public World createWorld(String name, Environment environment, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
    }

    public World createWorld(String name, Environment environment, long seed, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
    }

    @Override
    public World createWorld(WorldCreator creator) {
        Preconditions.checkState(!console.levels.isEmpty(), "Cannot create additional worlds on STARTUP");
        Validate.notNull(creator, "Creator may not be null");

        String name = creator.name();
        ChunkGenerator generator = creator.generator();
        File folder = new File(getWorldContainer(), name);
        World world = getWorld(name);

        if (world != null) {
            return world;
        }

        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }

        if ("DIM1".equalsIgnoreCase(name) || "DIM-1".equalsIgnoreCase(name)) throw new IllegalArgumentException("Dynamic loading is not supported: " + name); // CatServer

        if (generator == null) {
            generator = getGenerator(name);
        }

        RegistryKey<Dimension> actualDimension;
        switch (creator.environment()) {
            case NORMAL:
                actualDimension = Dimension.OVERWORLD;
                break;
            case NETHER:
                actualDimension = Dimension.NETHER;
                break;
            case THE_END:
                actualDimension = Dimension.END;
                break;
            default:
                throw new IllegalArgumentException("Illegal dimension");
        }

        SaveFormat.LevelSave worldSession = console.storageSource;
        try {
            worldSession = SaveFormat.createDefault(getWorldContainer().toPath()).cBCreateAccess(name, actualDimension);
            // CatServer start - Fix import
            for (String s : new String[]{ "region", "data", "entities", "poi" }) {
                File saveDir = new File(worldSession.getWorldDir().toFile(), "dimensions/minecraft/" + name + "/" + s);
                if (!saveDir.exists()) {
                    File importDir = new File(worldSession.levelPath.toFile(), s);
                    if (importDir.exists()) {
                        logger.info(String.format("[Import Fixer] Copying %s to %s", importDir.getAbsolutePath(), saveDir.getAbsolutePath()));
                        FileUtils.copyDirectory(importDir, saveDir);
                    }
                }
            }
            // CatServer end
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        MinecraftServer.convertFromRegionFormatIfNeeded(worldSession); // Run conversion now

        boolean hardcore = creator.hardcore();

        WorldSettingsImport<INBT> registryreadops = WorldSettingsImport.create((DynamicOps) NBTDynamicOps.INSTANCE, console.getDataPackRegistries().getResourceManager(), console.registryHolder);
        ServerWorldInfo worlddata = (ServerWorldInfo) worldSession.getDataTag((DynamicOps) registryreadops, console.datapackconfiguration);

        WorldSettings worldSettings;
        // See MinecraftServer.a(String, String, long, WorldType, JsonElement)
        if (worlddata == null) {
            Properties properties = new Properties();
            properties.put("generator-settings", Objects.toString(creator.generatorSettings()));
            properties.put("level-seed", Objects.toString(creator.seed()));
            properties.put("generate-structures", Objects.toString(creator.generateStructures()));
            properties.put("level-type", Objects.toString(creator.type().getName()));

            DimensionGeneratorSettings generatorsettings = DimensionGeneratorSettings.create(console.registryAccess(), properties);
            worldSettings = new WorldSettings(name, GameType.byId(getDefaultGameMode().getValue()), hardcore, Difficulty.EASY, false, new GameRules(), console.datapackconfiguration);
            worlddata = new ServerWorldInfo(worldSettings, generatorsettings, Lifecycle.stable());
        }
        worlddata.checkName(name);
        worlddata.setModdedInfo(console.getServerModName(), console.getModdedStatus().isPresent());

        long j = BiomeManager.obfuscateSeed(creator.seed());
        List<ISpecialSpawner> list = ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new WanderingTraderSpawner(worlddata));
        SimpleRegistry<Dimension> registrymaterials = worlddata.worldGenSettings().dimensions();
        Dimension worlddimension = (Dimension) registrymaterials.get(actualDimension);
        DimensionType dimensionmanager;
        net.minecraft.world.gen.ChunkGenerator chunkgenerator;

        if (worlddimension == null) {
            dimensionmanager = (DimensionType) console.registryHolder.dimensionTypes().getOrThrow(DimensionType.OVERWORLD_LOCATION);
            chunkgenerator = DimensionGeneratorSettings.makeDefaultOverworld(console.registryHolder.registryOrThrow(Registry.BIOME_REGISTRY), console.registryHolder.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), (new Random()).nextLong());
        } else {
            dimensionmanager = worlddimension.type();
            chunkgenerator = worlddimension.generator();
        }

        RegistryKey<net.minecraft.world.World> worldKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(name.toLowerCase(java.util.Locale.ENGLISH)));

        ServerWorld internal;
        try {
            catserver.server.utils.BukkitWorldSetter.get().setWorld(generator, creator.environment());
            internal = new ServerWorld(console, console.executor, worldSession, worlddata, worldKey, dimensionmanager, getServer().progressListenerFactory.create(11),
                    chunkgenerator, worlddata.worldGenSettings().isDebug(), j, creator.environment() == Environment.NORMAL ? list : ImmutableList.of(), true);
            internal.isBukkitWorld = true;
        } finally {
            catserver.server.utils.BukkitWorldSetter.get().reset();
        }

        if (!(worlds.containsKey(name.toLowerCase(java.util.Locale.ENGLISH)))) {
            return null;
        }

        console.initWorld(internal, worlddata, worlddata, worlddata.worldGenSettings());

        internal.setSpawnSettings(true, true);
        console.levels.put(internal.dimension(), internal);

        getServer().loadSpawn(internal.getChunkSource().chunkMap.progressListener, internal);

        return internal.getWorld();
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        return unloadWorld(getWorld(name), save);
    }

    @Override
    public boolean unloadWorld(World world, boolean save) {
        if (world == null) {
            return false;
        }

        ServerWorld handle = ((CraftWorld) world).getHandle();

        if (!(console.levels.containsKey(handle.dimension()))) {
            return false;
        }

        if (handle.dimension() == net.minecraft.world.World.OVERWORLD) {
            return false;
        }

        if (handle.players().size() > 0) {
            return false;
        }

        WorldUnloadEvent e = new WorldUnloadEvent(handle.getWorld());
        pluginManager.callEvent(e);

        if (e.isCancelled()) {
            return false;
        }

        try {
            if (save) {
                handle.save(null, true, true);
            }

            handle.getChunkSource().close(save);
            handle.convertable.close();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, null, ex);
        }

        MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(handle));
        worlds.remove(world.getName().toLowerCase(java.util.Locale.ENGLISH));
        console.levels.remove(handle.dimension());
        return true;
    }

    public DedicatedServer getServer() {
        return console;
    }

    @Override
    public World getWorld(String name) {
        Validate.notNull(name, "Name cannot be null");

        return worlds.get(name.toLowerCase(java.util.Locale.ENGLISH));
    }

    @Override
    public World getWorld(UUID uid) {
        for (World world : worlds.values()) {
            if (world.getUID().equals(uid)) {
                return world;
            }
        }
        return null;
    }

    public void addWorld(World world) {
        // Check if a World already exists with the UID.
        if (getWorld(world.getUID()) != null) {
            //System.out.println("World " + world.getName() + " is a duplicate of another world and has been prevented from loading. Please delete the uid.dat file from " + world.getName() + "'s world directory if you want to be able to load the duplicate world.");
            return;
        }
        worlds.put(world.getName().toLowerCase(java.util.Locale.ENGLISH), world);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        Command command = commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand) command;
        } else {
            return null;
        }
    }

    @Override
    public void savePlayers() {
        checkSaveState();
        playerList.saveAll();
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        CraftRecipe toAdd;
        if (recipe instanceof CraftRecipe) {
            toAdd = (CraftRecipe) recipe;
        } else {
            if (recipe instanceof ShapedRecipe) {
                toAdd = CraftShapedRecipe.fromBukkitRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                toAdd = CraftShapelessRecipe.fromBukkitRecipe((ShapelessRecipe) recipe);
            } else if (recipe instanceof FurnaceRecipe) {
                toAdd = CraftFurnaceRecipe.fromBukkitRecipe((FurnaceRecipe) recipe);
            } else if (recipe instanceof BlastingRecipe) {
                toAdd = CraftBlastingRecipe.fromBukkitRecipe((BlastingRecipe) recipe);
            } else if (recipe instanceof CampfireRecipe) {
                toAdd = CraftCampfireRecipe.fromBukkitRecipe((CampfireRecipe) recipe);
            } else if (recipe instanceof SmokingRecipe) {
                toAdd = CraftSmokingRecipe.fromBukkitRecipe((SmokingRecipe) recipe);
            } else if (recipe instanceof StonecuttingRecipe) {
                toAdd = CraftStonecuttingRecipe.fromBukkitRecipe((StonecuttingRecipe) recipe);
            } else if (recipe instanceof SmithingRecipe) {
                toAdd = CraftSmithingRecipe.fromBukkitRecipe((SmithingRecipe) recipe);
            } else if (recipe instanceof ComplexRecipe) {
                throw new UnsupportedOperationException("Cannot add custom complex recipe");
            } else {
                return false;
            }
        }
        toAdd.addToCraftingManager();
        return true;
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        Validate.notNull(result, "Result cannot be null");

        List<Recipe> results = new ArrayList<Recipe>();
        Iterator<Recipe> iter = recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            ItemStack stack = recipe.getResult();
            if (stack.getType() != result.getType()) {
                continue;
            }
            if (result.getDurability() == -1 || result.getDurability() == stack.getDurability()) {
                results.add(recipe);
            }
        }
        return results;
    }

    @Override
    public Recipe getRecipe(NamespacedKey recipeKey) {
        Preconditions.checkArgument(recipeKey != null, "recipeKey == null");

        return getServer().getRecipeManager().byKey(CraftNamespacedKey.toMinecraft(recipeKey)).map(IRecipe::toBukkitRecipe).orElse(null);
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return new RecipeIterator();
    }

    @Override
    public void clearRecipes() {
        console.getRecipeManager().clearRecipes();
    }

    @Override
    public void resetRecipes() {
        reloadData(); // Not ideal but hard to reload a subset of a resource pack
    }

    @Override
    public boolean removeRecipe(NamespacedKey recipeKey) {
        Preconditions.checkArgument(recipeKey != null, "recipeKey == null");

        ResourceLocation mcKey = CraftNamespacedKey.toMinecraft(recipeKey);
        for (Map<ResourceLocation, IRecipe<?>> recipes : getServer().getRecipeManager().recipes.values()) {
            if (recipes.remove(mcKey) != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        ConfigurationSection section = commandsConfiguration.getConfigurationSection("aliases");
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();

        if (section != null) {
            for (String key : section.getKeys(false)) {
                List<String> commands;

                if (section.isList(key)) {
                    commands = section.getStringList(key);
                } else {
                    commands = ImmutableList.of(section.getString(key));
                }

                result.put(key, commands.toArray(new String[commands.size()]));
            }
        }

        return result;
    }

    public void removeBukkitSpawnRadius() {
        configuration.set("settings.spawn-radius", null);
        saveConfig();
    }

    public int getBukkitSpawnRadius() {
        return configuration.getInt("settings.spawn-radius", -1);
    }

    @Override
    public String getShutdownMessage() {
        return configuration.getString("settings.shutdown-message");
    }

    @Override
    public int getSpawnRadius() {
        return this.getServer().getSpawnProtectionRadius();
    }

    @Override
    public void setSpawnRadius(int value) {
        configuration.set("settings.spawn-radius", value);
        saveConfig();
    }

    @Override
    public boolean getOnlineMode() {
        return console.usesAuthentication();
    }

    @Override
    public boolean getAllowFlight() {
        return console.isFlightAllowed();
    }

    @Override
    public boolean isHardcore() {
        return console.isHardcore();
    }

    public ChunkGenerator getGenerator(String world) {
        ConfigurationSection section = configuration.getConfigurationSection("worlds");
        ChunkGenerator result = null;

        if (section != null) {
            section = section.getConfigurationSection(world);

            if (section != null) {
                String name = section.getString("generator");

                if ((name != null) && (!name.equals(""))) {
                    String[] split = name.split(":", 2);
                    String id = (split.length > 1) ? split[1] : null;
                    Plugin plugin = pluginManager.getPlugin(split[0]);

                    if (plugin == null) {
                        getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
                    } else if (!plugin.isEnabled()) {
                        getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
                    } else {
                        try {
                            result = plugin.getDefaultWorldGenerator(world, id);
                            if (result == null) {
                                getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world generator");
                            }
                        } catch (Throwable t) {
                            plugin.getLogger().log(Level.SEVERE, "Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t);
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    @Deprecated
    public CraftMapView getMap(int id) {
        MapData worldmap = console.getLevel(net.minecraft.world.World.OVERWORLD).getMapData("map_" + id);
        if (worldmap == null) {
            return null;
        }
        return worldmap.mapView;
    }

    @Override
    public CraftMapView createMap(World world) {
        Validate.notNull(world, "World cannot be null");

        net.minecraft.item.ItemStack stack = new net.minecraft.item.ItemStack(Items.MAP, 1);
        MapData worldmap = FilledMapItem.getCustomMapData_static(stack, ((CraftWorld) world).getHandle()); // LoliServer
        return worldmap.mapView;
    }

    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType) {
        return this.createExplorerMap(world, location, structureType, 100, true);
    }

    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType, int radius, boolean findUnexplored) {
        Validate.notNull(world, "World cannot be null");
        Validate.notNull(structureType, "StructureType cannot be null");
        Validate.notNull(structureType.getMapIcon(), "Cannot create explorer maps for StructureType " + structureType.getName());

        ServerWorld worldServer = ((CraftWorld) world).getHandle();
        Location structureLocation = world.locateNearestStructure(location, structureType, radius, findUnexplored);
        BlockPos structurePosition = new BlockPos(structureLocation.getBlockX(), structureLocation.getBlockY(), structureLocation.getBlockZ());

        // Create map with trackPlayer = true, unlimitedTracking = true
        net.minecraft.item.ItemStack stack = FilledMapItem.create(worldServer, structurePosition.getX(), structurePosition.getZ(), MapView.Scale.NORMAL.getValue(), true, true);
        FilledMapItem.renderBiomePreviewMap(worldServer, stack);
        // "+" map ID taken from EntityVillager
        FilledMapItem.getOrCreateSavedData(stack, worldServer).addTargetDecoration(stack, structurePosition, "+", MapDecoration.Type.byIcon(structureType.getMapIcon().getValue()));

        return CraftItemStack.asBukkitCopy(stack);
    }

    @Override
    public void shutdown() {
        console.halt(false);
    }

    @Override
    public int broadcast(String message, String permission) {
        Set<CommandSender> recipients = new HashSet<>();
        for (Permissible permissible : getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                recipients.add((CommandSender) permissible);
            }
        }

        BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(!Bukkit.isPrimaryThread(), message, recipients);
        getPluginManager().callEvent(broadcastMessageEvent);

        if (broadcastMessageEvent.isCancelled()) {
            return 0;
        }

        message = broadcastMessageEvent.getMessage();

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    @Override
    @Deprecated
    public OfflinePlayer getOfflinePlayer(String name) {
        Validate.notNull(name, "Name cannot be null");
        Validate.notEmpty(name, "Name cannot be empty");

        OfflinePlayer result = getPlayerExact(name);
        if (result == null) {
            // Spigot Start
            GameProfile profile = null;
            // Only fetch an online UUID in online mode
            if ( getOnlineMode() || org.spigotmc.SpigotConfig.bungee )
            {
                profile = console.getProfileCache().get(name);
            }
            // Spigot end
            if (profile == null) {
                // Make an OfflinePlayer using an offline mode UUID since the name has no profile
                result = getOfflinePlayer(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name));
            } else {
                // Use the GameProfile even when we get a UUID so we ensure we still have a name
                result = getOfflinePlayer(profile);
            }
        } else {
            offlinePlayers.remove(result.getUniqueId());
        }

        return result;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        Validate.notNull(id, "UUID cannot be null");

        OfflinePlayer result = getPlayer(id);
        if (result == null) {
            result = offlinePlayers.get(id);
            if (result == null) {
                result = new CraftOfflinePlayer(this, new GameProfile(id, null));
                offlinePlayers.put(id, result);
            }
        } else {
            offlinePlayers.remove(id);
        }

        return result;
    }

    public OfflinePlayer getOfflinePlayer(GameProfile profile) {
        OfflinePlayer player = new CraftOfflinePlayer(this, profile);
        offlinePlayers.put(profile.getId(), player);
        return player;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> getIPBans() {
        return new HashSet<String>(Arrays.asList(playerList.getIpBans().getUserList()));
    }

    @Override
    public void banIP(String address) {
        Validate.notNull(address, "Address cannot be null.");

        this.getBanList(org.bukkit.BanList.Type.IP).addBan(address, null, null, null);
    }

    @Override
    public void unbanIP(String address) {
        Validate.notNull(address, "Address cannot be null.");

        this.getBanList(org.bukkit.BanList.Type.IP).pardon(address);
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        Set<OfflinePlayer> result = new HashSet<OfflinePlayer>();

        for (UserListEntry entry : playerList.getBans().getEntries()) {
            result.add(getOfflinePlayer((GameProfile) entry.getUser()));
        }

        return result;
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        Validate.notNull(type, "Type cannot be null");

        switch (type) {
            case IP:
                return new CraftIpBanList(playerList.getIpBans());
            case NAME:
            default:
                return new CraftProfileBanList(playerList.getBans());
        }
    }

    @Override
    public void setWhitelist(boolean value) {
        playerList.setUsingWhiteList(value);
        console.storeUsingWhiteList(value);
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        Set<OfflinePlayer> result = new LinkedHashSet<OfflinePlayer>();

        for (UserListEntry entry : playerList.getWhiteList().getEntries()) {
            result.add(getOfflinePlayer((GameProfile) entry.getUser()));
        }

        return result;
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        Set<OfflinePlayer> result = new HashSet<OfflinePlayer>();

        for (UserListEntry entry : playerList.getOps().getEntries()) {
            result.add(getOfflinePlayer((GameProfile) entry.getUser()));
        }

        return result;
    }

    @Override
    public void reloadWhitelist() {
        playerList.reloadWhiteList();
    }

    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(console.getLevel(net.minecraft.world.World.OVERWORLD).getServerWorldInfo().getGameType().getId());
    }

    @Override
    public void setDefaultGameMode(GameMode mode) {
        Validate.notNull(mode, "Mode cannot be null");

        for (World world : getWorlds()) {
            ((CraftWorld) world).getHandle().getServerWorldInfo().setGameType(GameType.byId(mode.getValue()));
        }
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return console.console;
    }

    public EntityMetadataStore getEntityMetadata() {
        return entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return playerMetadata;
    }

    public WorldMetadataStore getWorldMetadata() {
        return worldMetadata;
    }

    @Override
    public File getWorldContainer() {
        return this.getServer().storageSource.levelPath.toFile().getAbsoluteFile();
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        PlayerData storage = console.playerDataStorage;
        String[] files = storage.getPlayerDataFolder().list(new DatFileFilter());
        Set<OfflinePlayer> players = new HashSet<OfflinePlayer>();

        for (String file : files) {
            try {
                players.add(getOfflinePlayer(UUID.fromString(file.substring(0, file.length() - 4))));
            } catch (IllegalArgumentException ex) {
                // Who knows what is in this directory, just ignore invalid files
            }
        }

        players.addAll(getOnlinePlayers());

        return players.toArray(new OfflinePlayer[players.size()]);
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(getMessenger(), source, channel, message);

        for (Player player : getOnlinePlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getOnlinePlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        Validate.isTrue(type.isCreatable(), "Cannot open an inventory of type ", type);
        return CraftInventoryCreator.INSTANCE.createInventory(owner, type);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        Validate.isTrue(type.isCreatable(), "Cannot open an inventory of type ", type);
        return CraftInventoryCreator.INSTANCE.createInventory(owner, type, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        Validate.isTrue(9 <= size && size <= 54 && size % 9 == 0, "Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got " + size + ")");
        return CraftInventoryCreator.INSTANCE.createInventory(owner, size);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        Validate.isTrue(9 <= size && size <= 54 && size % 9 == 0, "Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got " + size + ")");
        return CraftInventoryCreator.INSTANCE.createInventory(owner, size, title);
    }

    @Override
    public Merchant createMerchant(String title) {
        return new CraftMerchantCustom(title == null ? InventoryType.MERCHANT.getDefaultTitle() : title);
    }

    @Override
    public HelpMap getHelpMap() {
        return helpMap;
    }

    public SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    @Override
    public int getMonsterSpawnLimit() {
        return monsterSpawn;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return animalSpawn;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return waterAnimalSpawn;
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        return waterAmbientSpawn;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return ambientSpawn;
    }

    @Override
    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(console.serverThread); // Paper - Fix issues with detecting main thread properlyog)
    }

    @Override
    public String getMotd() {
        return console.getMotd();
    }

    @Override
    public WarningState getWarningState() {
        return warningState;
    }

    public List<String> tabComplete(CommandSender sender, String message, ServerWorld world, Vector3d pos, boolean forceCommand) {
        if (!(sender instanceof Player)) {
            return ImmutableList.of();
        }

        List<String> offers;
        Player player = (Player) sender;
        if (message.startsWith("/") || forceCommand) {
            offers = tabCompleteCommand(player, message, world, pos);
        } else {
            offers = tabCompleteChat(player, message);
        }

        TabCompleteEvent tabEvent = new TabCompleteEvent(player, message, offers);
        getPluginManager().callEvent(tabEvent);

        return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
    }

    public List<String> tabCompleteCommand(Player player, String message, ServerWorld world, Vector3d pos) {
        // Spigot Start
        if ( (org.spigotmc.SpigotConfig.tabComplete < 0 || message.length() <= org.spigotmc.SpigotConfig.tabComplete) && !message.contains( " " ) )
        {
            return ImmutableList.of();
        }
        // Spigot End
        List<String> completions = null;
        try {
            if (message.startsWith("/")) {
                // Trim leading '/' if present (won't always be present in command blocks)
                message = message.substring(1);
            }
            if (pos == null) {
                completions = getCommandMap().tabComplete(player, message);
            } else {
                completions = getCommandMap().tabComplete(player, message, new Location(world.getWorld(), pos.x, pos.y, pos.z));
            }
        } catch (CommandException ex) {
            player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to tab-complete this command");
            getLogger().log(Level.SEVERE, "Exception when " + player.getName() + " attempted to tab complete " + message, ex);
        }

        return completions == null ? ImmutableList.<String>of() : completions;
    }

    public List<String> tabCompleteChat(Player player, String message) {
        List<String> completions = new ArrayList<String>();
        PlayerChatTabCompleteEvent event = new PlayerChatTabCompleteEvent(player, message, completions);
        String token = event.getLastToken();
        for (Player p : getOnlinePlayers()) {
            if (player.canSee(p) && StringUtil.startsWithIgnoreCase(p.getName(), token)) {
                completions.add(p.getName());
            }
        }
        pluginManager.callEvent(event);

        Iterator<?> it = completions.iterator();
        while (it.hasNext()) {
            Object current = it.next();
            if (!(current instanceof String)) {
                // Sanity
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
        return scoreboardManager;
    }

    public void checkSaveState() {
        if (this.playerCommandState || this.printSaveWarning || this.console.autosavePeriod <= 0) {
            return;
        }
        this.printSaveWarning = true;
        getLogger().log(Level.WARNING, "A manual (plugin-induced) save has been detected while server is configured to auto-save. This may affect performance.", warningState == WarningState.ON ? new Throwable() : null);
    }

    @Override
    public CraftIconCache getServerIcon() {
        return icon;
    }

    @Override
    public CraftIconCache loadServerIcon(File file) throws Exception {
        Validate.notNull(file, "File cannot be null");
        if (!file.isFile()) {
            throw new IllegalArgumentException(file + " is not a file");
        }
        return loadServerIcon0(file);
    }

    static CraftIconCache loadServerIcon0(File file) throws Exception {
        return loadServerIcon0(ImageIO.read(file));
    }

    @Override
    public CraftIconCache loadServerIcon(BufferedImage image) throws Exception {
        Validate.notNull(image, "Image cannot be null");
        return loadServerIcon0(image);
    }

    static CraftIconCache loadServerIcon0(BufferedImage image) throws Exception {
        ByteBuf bytebuf = Unpooled.buffer();

        Validate.isTrue(image.getWidth() == 64, "Must be 64 pixels wide");
        Validate.isTrue(image.getHeight() == 64, "Must be 64 pixels high");
        ImageIO.write(image, "PNG", new ByteBufOutputStream(bytebuf));
        ByteBuffer bytebuffer = Base64.getEncoder().encode(bytebuf.nioBuffer());

        return new CraftIconCache("data:image/png;base64," + StandardCharsets.UTF_8.decode(bytebuffer));
    }

    @Override
    public void setIdleTimeout(int threshold) {
        console.setPlayerIdleTimeout(threshold);
    }

    @Override
    public int getIdleTimeout() {
        return console.getPlayerIdleTimeout();
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        Validate.notNull(world, "World cannot be null");
        return new CraftChunkData(world);
    }

    @Override
    public BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        return new CraftBossBar(title, color, style, flags);
    }

    @Override
    public KeyedBossBar createBossBar(NamespacedKey key, String title, BarColor barColor, BarStyle barStyle, BarFlag... barFlags) {
        Preconditions.checkArgument(key != null, "key");

        CustomServerBossInfo bossBattleCustom = getServer().getCustomBossEvents().create(CraftNamespacedKey.toMinecraft(key), CraftChatMessage.fromString(title, true)[0]);
        CraftKeyedBossbar craftKeyedBossbar = new CraftKeyedBossbar(bossBattleCustom);
        craftKeyedBossbar.setColor(barColor);
        craftKeyedBossbar.setStyle(barStyle);
        for (BarFlag flag : barFlags) {
            craftKeyedBossbar.addFlag(flag);
        }

        return craftKeyedBossbar;
    }

    @Override
    public Iterator<KeyedBossBar> getBossBars() {
        return Iterators.unmodifiableIterator(Iterators.transform(getServer().getCustomBossEvents().getEvents().iterator(), new Function<CustomServerBossInfo, org.bukkit.boss.KeyedBossBar>() {
            @Override
            public org.bukkit.boss.KeyedBossBar apply(CustomServerBossInfo bossBattleCustom) {
                return bossBattleCustom.getBukkitEntity();
            }
        }));
    }

    @Override
    public KeyedBossBar getBossBar(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "key");
        net.minecraft.server.CustomServerBossInfo bossBattleCustom = getServer().getCustomBossEvents().get(CraftNamespacedKey.toMinecraft(key));

        return (bossBattleCustom == null) ? null : bossBattleCustom.getBukkitEntity();
    }

    @Override
    public boolean removeBossBar(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "key");
        net.minecraft.server.CustomServerBossInfoManager bossBattleCustomData = getServer().getCustomBossEvents();
        net.minecraft.server.CustomServerBossInfo bossBattleCustom = bossBattleCustomData.get(CraftNamespacedKey.toMinecraft(key));

        if (bossBattleCustom != null) {
            bossBattleCustomData.remove(bossBattleCustom);
            return true;
        }

        return false;
    }

    @Override
    public Entity getEntity(UUID uuid) {
        Validate.notNull(uuid, "UUID cannot be null");

        for (ServerWorld world : getServer().getAllLevels()) {
            net.minecraft.entity.Entity entity = world.getEntity(uuid);
            if (entity != null) {
                return entity.getBukkitEntity();
            }
        }

        return null;
    }

    @NotNull
    @Override
    public double[] getTPS() {
        return MinecraftServer.getServer().recentTps;
    }

    @Override
    public org.bukkit.advancement.Advancement getAdvancement(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "key");

        Advancement advancement = console.getAdvancements().getAdvancement(CraftNamespacedKey.toMinecraft(key));
        return (advancement == null) ? null : advancement.bukkit;
    }

    @Override
    public Iterator<org.bukkit.advancement.Advancement> advancementIterator() {
        return Iterators.unmodifiableIterator(Iterators.transform(console.getAdvancements().getAllAdvancements().iterator(), new Function<Advancement, org.bukkit.advancement.Advancement>() {
            @Override
            public org.bukkit.advancement.Advancement apply(Advancement advancement) {
                return advancement.bukkit;
            }
        }));
    }

    @Override
    public BlockData createBlockData(org.bukkit.Material material) {
        Validate.isTrue(material != null, "Must provide material");

        return createBlockData(material, (String) null);
    }

    @Override
    public BlockData createBlockData(org.bukkit.Material material, Consumer<BlockData> consumer) {
        BlockData data = createBlockData(material);

        if (consumer != null) {
            consumer.accept(data);
        }

        return data;
    }

    @Override
    public BlockData createBlockData(String data) throws IllegalArgumentException {
        Validate.isTrue(data != null, "Must provide data");

        return createBlockData(null, data);
    }

    @Override
    public BlockData createBlockData(org.bukkit.Material material, String data) {
        Validate.isTrue(material != null || data != null, "Must provide one of material or data");

        return CraftBlockData.newData(material, data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Keyed> org.bukkit.Tag<T> getTag(String registry, NamespacedKey tag, Class<T> clazz) {
        ResourceLocation key = CraftNamespacedKey.toMinecraft(tag);

        switch (registry) {
            case org.bukkit.Tag.REGISTRY_BLOCKS:
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Block namespace must have material type");

                return (org.bukkit.Tag<T>) new CraftBlockTag(console.getTags().getBlocks(), key);
            case org.bukkit.Tag.REGISTRY_ITEMS:
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Item namespace must have material type");

                return (org.bukkit.Tag<T>) new CraftItemTag(console.getTags().getItems(), key);
            case org.bukkit.Tag.REGISTRY_FLUIDS:
                Preconditions.checkArgument(clazz == org.bukkit.Fluid.class, "Fluid namespace must have fluid type");

                return (org.bukkit.Tag<T>) new CraftFluidTag(console.getTags().getFluids(), key);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Keyed> Iterable<org.bukkit.Tag<T>> getTags(String registry, Class<T> clazz) {
        switch (registry) {
            case org.bukkit.Tag.REGISTRY_BLOCKS:
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Block namespace must have material type");

                ITagCollection<Block> blockTags = console.getTags().getBlocks();
                return blockTags.getAllTags().keySet().stream().map(key -> (org.bukkit.Tag<T>) new CraftBlockTag(blockTags, key)).collect(ImmutableList.toImmutableList());
            case org.bukkit.Tag.REGISTRY_ITEMS:
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Item namespace must have material type");

                ITagCollection<Item> itemTags = console.getTags().getItems();
                return itemTags.getAllTags().keySet().stream().map(key -> (org.bukkit.Tag<T>) new CraftItemTag(itemTags, key)).collect(ImmutableList.toImmutableList());
            case org.bukkit.Tag.REGISTRY_FLUIDS:
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Fluid namespace must have fluid type");

                ITagCollection<Fluid> fluidTags = console.getTags().getFluids();
                return fluidTags.getAllTags().keySet().stream().map(key -> (org.bukkit.Tag<T>) new CraftFluidTag(fluidTags, key)).collect(ImmutableList.toImmutableList());
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public LootTable getLootTable(NamespacedKey key) {
        Validate.notNull(key, "NamespacedKey cannot be null");

        LootTableManager registry = getServer().getLootTables();
        return new CraftLootTable(key, registry.get(CraftNamespacedKey.toMinecraft(key)));
    }

    @Override
    public List<Entity> selectEntities(CommandSender sender, String selector) {
        Preconditions.checkArgument(selector != null, "Selector cannot be null");
        Preconditions.checkArgument(sender != null, "Sender cannot be null");

        EntityArgument arg = EntityArgument.entities();
        List<? extends net.minecraft.entity.Entity> nms;

        try {
            StringReader reader = new StringReader(selector);
            nms = arg.parse(reader, true).findEntities(VanillaCommandWrapper.getListener(sender));
            Preconditions.checkArgument(!reader.canRead(), "Spurious trailing data in selector: " + selector);
        } catch (CommandSyntaxException ex) {
            throw new IllegalArgumentException("Could not parse selector: " + selector, ex);
        }

        return new ArrayList<Entity>(Lists.transform(nms, (entity) -> entity.getBukkitEntity()));
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

        @Override
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
        }
    };

    public Spigot spigot()
    {
        return spigot;
    }

    public void setPlayerList(PlayerList playerList) {
        playerList = (DedicatedPlayerList)playerList;
    }
}
