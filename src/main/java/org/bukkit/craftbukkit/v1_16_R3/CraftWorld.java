package org.bukkit.craftbukkit.v1_16_R3;

import catserver.server.CatServer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.LeashKnotEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.entity.item.minecart.HopperMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.item.minecart.SpawnerMinecartEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.entity.projectile.EyeOfEnderEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SortedArraySet;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Unit;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.RuleKey;
import net.minecraft.world.GameRules.RuleType;
import net.minecraft.world.GameRules.RuleValue;
import net.minecraft.world.chunk.ChunkPrimerWrapper;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.raid.RaidManager;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.Ticket;
import net.minecraft.world.server.TicketManager;
import net.minecraft.world.server.TicketType;
import org.apache.commons.lang.Validate;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Raid;
import org.bukkit.Sound;
import org.bukkit.StructureType;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R3.boss.CraftDragonBattle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftRayTraceResult;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Cat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Egg;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illager;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Strider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.TraderLlama;
import org.bukkit.entity.Trident;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Turtle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class CraftWorld implements World {
    public static final int CUSTOM_DIMENSION_OFFSET = 10;

    private final ServerWorld world;
    private WorldBorder worldBorder;
    private Environment environment;
    private final CraftServer server = (CraftServer) Bukkit.getServer();
    private final ChunkGenerator generator;
    private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private int waterAmbientSpawn = -1;
    private int ambientSpawn = -1;

    private static final Random rand = new Random();

    public CraftWorld(ServerWorld world, ChunkGenerator gen, Environment env) {
        this.world = world;
        this.generator = gen;

        environment = env;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return CraftBlock.at(world, new BlockPos(x, y, z));
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return getHighestBlockYAt(x, z, org.bukkit.HeightMap.MOTION_BLOCKING);
    }

    @Override
    public Location getSpawnLocation() {
        BlockPos spawn = world.getSharedSpawnPos();
        return new Location(this, spawn.getX(), spawn.getY(), spawn.getZ());
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        Preconditions.checkArgument(location != null, "location");

        return equals(location.getWorld()) ? setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw()) : false;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z, float angle) {
        try {
            Location previousLocation = getSpawnLocation();
            world.levelData.setSpawn(new BlockPos(x, y, z), angle);

            // Notify anyone who's listening.
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            server.getPluginManager().callEvent(event);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        return setSpawnLocation(x, y, z, 0.0F);
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return this.world.getChunkSource().getChunk(x, z, true).bukkitChunk;
    }

    @Override
    public Chunk getChunkAt(Block block) {
        Preconditions.checkArgument(block != null, "null block");

        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        net.minecraft.world.chunk.Chunk chunk = world.getChunkSource().getChunk(x,z, false);
        return chunk != null;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        try {
            return isChunkLoaded(x, z) || world.getChunkSource().chunkMap.read(new ChunkPos(x, z)) != null;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Chunk[] getLoadedChunks() {
        Long2ObjectLinkedOpenHashMap<ChunkHolder> chunks = world.getChunkSource().chunkMap.visibleChunkMap;
        return chunks.values().stream().map(ChunkHolder::getFullChunk).filter(Objects::nonNull).map(net.minecraft.world.chunk.Chunk::getBukkitChunk).toArray(Chunk[]::new);
    }

    @Override
    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return unloadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return unloadChunk0(x, z, save);
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        if (isChunkLoaded(x, z)) {
            world.getChunkSource().removeRegionTicket(TicketType.PLUGIN, new ChunkPos(x, z), 2, Unit.INSTANCE); //LoliServer - KomiMoe - set p_217222_3_ to 2. because i want it.
        }

        return true;
    }

    private boolean unloadChunk0(int x, int z, boolean save) {
        if (!isChunkLoaded(x, z)) {
            return true;
        }
        net.minecraft.world.chunk.Chunk chunk = world.getChunk(x, z);

        chunk.mustNotSave = !save;
        unloadChunkRequest(x, z);

        world.getChunkSource().purgeUnload();
        return !isChunkLoaded(x, z);
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version! Unless you can fix it, this is not a bug :)");
        /*
        if (!unloadChunk0(x, z, false)) {
            return false;
        }

        final long chunkKey = ChunkCoordIntPair.pair(x, z);
        world.getChunkSource().unloadQueue.remove(chunkKey);

        net.minecraft.server.Chunk chunk = world.getChunkSource().generateChunk(x, z);
        PlayerChunk playerChunk = world.getPlayerChunkMap().getChunk(x, z);
        if (playerChunk != null) {
            playerChunk.chunk = chunk;
        }

        if (chunk != null) {
            refreshChunk(x, z);
        }

        return chunk != null;
        */
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        if (!isChunkLoaded(x, z)) {
            return false;
        }

        int px = x << 4;
        int pz = z << 4;

        // If there are more than 64 updates to a chunk at once, it will update all 'touched' sections within the chunk
        // And will include biome data if all sections have been 'touched'
        // This flags 65 blocks distributed across all the sections of the chunk, so that everything is sent, including biomes
        int height = getMaxHeight() / 16;
        for (int idx = 0; idx < 64; idx++) {
            world.sendBlockUpdated(new BlockPos(px + (idx / height), ((idx % height) * 16), pz), Blocks.AIR.defaultBlockState(), Blocks.STONE.defaultBlockState(), 3);
        }
        world.sendBlockUpdated(new BlockPos(px + 15, (height * 16) - 1, pz + 15), Blocks.AIR.defaultBlockState(), Blocks.STONE.defaultBlockState(), 3);

        return true;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return isChunkLoaded(x, z);
    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        IChunk chunk = world.getChunkSource().getChunk(x, z, generate ? ChunkStatus.FULL : ChunkStatus.EMPTY, true);

        // If generate = false, but the chunk already exists, we will get this back.
        if (chunk instanceof ChunkPrimerWrapper) {
            // We then cycle through again to get the full chunk immediately, rather than after the ticket addition
            chunk = world.getChunkSource().getChunk(x, z, ChunkStatus.FULL, true);
        }

        if (chunk instanceof net.minecraft.world.chunk.Chunk) {
            world.getChunkSource().addRegionTicket(TicketType.PLUGIN, new ChunkPos(x, z), 2, Unit.INSTANCE); //LoliServer - KomiMoe - set p_217228_3_ to 2. because i want it.
            return true;
        }

        return false;
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "null chunk");

        return isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    @Override
    public void loadChunk(Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "null chunk");

        loadChunk(chunk.getX(), chunk.getZ());
        ((CraftChunk) getChunkAt(chunk.getX(), chunk.getZ())).getHandle().bukkitChunk = chunk;
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "null plugin");
        Preconditions.checkArgument(plugin.isEnabled(), "plugin is not enabled");

        TicketManager chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;

        if (chunkDistanceManager.addTicketAtLevel(TicketType.PLUGIN_TICKET, new ChunkPos(x, z), 31, plugin)) { // keep in-line with force loading, add at level 31
            this.getChunkAt(x, z); // ensure loaded
            return true;
        }

        return false;
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        Preconditions.checkNotNull(plugin, "null plugin");

        TicketManager chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        return chunkDistanceManager.removeTicketAtLevel(TicketType.PLUGIN_TICKET, new ChunkPos(x, z), 31, plugin); // keep in-line with force loading, remove at level 31
    }

    @Override
    public void removePluginChunkTickets(Plugin plugin) {
        Preconditions.checkNotNull(plugin, "null plugin");

        TicketManager chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        chunkDistanceManager.removeAllTicketsFor(TicketType.PLUGIN_TICKET, 31, plugin); // keep in-line with force loading, remove at level 31
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        TicketManager chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        SortedArraySet<Ticket<?>> tickets = chunkDistanceManager.tickets.get(ChunkPos.asLong(x, z));

        if (tickets == null) {
            return Collections.emptyList();
        }

        ImmutableList.Builder<Plugin> ret = ImmutableList.builder();
        for (Ticket<?> ticket : tickets) {
            if (ticket.getType() == TicketType.PLUGIN_TICKET) {
                ret.add((Plugin) ticket.key);
            }
        }

        return ret.build();
    }

    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        Map<Plugin, ImmutableList.Builder<Chunk>> ret = new HashMap<>();
        TicketManager chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;

        for (Long2ObjectMap.Entry<SortedArraySet<Ticket<?>>> chunkTickets : chunkDistanceManager.tickets.long2ObjectEntrySet()) {
            long chunkKey = chunkTickets.getLongKey();
            SortedArraySet<Ticket<?>> tickets = chunkTickets.getValue();

            Chunk chunk = null;
            for (Ticket<?> ticket : tickets) {
                if (ticket.getType() != TicketType.PLUGIN_TICKET) {
                    continue;
                }

                if (chunk == null) {
                    chunk = this.getChunkAt(ChunkPos.getX(chunkKey), ChunkPos.getZ(chunkKey));
                }

                ret.computeIfAbsent((Plugin) ticket.key, (key) -> ImmutableList.builder()).add(chunk);
            }
        }

        return ret.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (entry) -> entry.getValue().build()));
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        return getHandle().getForcedChunks().contains(ChunkPos.asLong(x, z));
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        getHandle().setChunkForced(x, z, forced);
    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        Set<Chunk> chunks = new HashSet<>();

        for (long coord : getHandle().getForcedChunks()) {
            chunks.add(getChunkAt(ChunkPos.getX(coord), ChunkPos.getZ(coord)));
        }

        return Collections.unmodifiableCollection(chunks);
    }

    public ServerWorld getHandle() {
        return world;
    }

    @Override
    public org.bukkit.entity.Item dropItem(Location loc, ItemStack item) {
        return dropItem(loc, item, null);
    }

    @Override
    public org.bukkit.entity.Item dropItem(Location loc, ItemStack item, Consumer<org.bukkit.entity.Item> function) {
        Validate.notNull(item, "Cannot drop a Null item.");
        ItemEntity entity = new ItemEntity(world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        entity.pickupDelay = 10;
        if (function != null) {
            function.accept((org.bukkit.entity.Item) entity.getBukkitEntity());
        }
        world.addEntity(entity, SpawnReason.CUSTOM);
        return (org.bukkit.entity.Item) entity.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item) {
        return dropItemNaturally(loc, item, null);
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item, Consumer<org.bukkit.entity.Item> function) {
        double xs = (world.random.nextFloat() * 0.5F) + 0.25D;
        double ys = (world.random.nextFloat() * 0.5F) + 0.25D;
        double zs = (world.random.nextFloat() * 0.5F) + 0.25D;
        loc = loc.clone();
        loc.setX(loc.getX() + xs);
        loc.setY(loc.getY() + ys);
        loc.setZ(loc.getZ() + zs);
        return dropItem(loc, item, function);
    }

    @Override
    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        return spawnArrow(loc, velocity, speed, spread, Arrow.class);
    }

    @Override
    public <T extends AbstractArrow> T spawnArrow(Location loc, Vector velocity, float speed, float spread, Class<T> clazz) {
        Validate.notNull(loc, "Can not spawn arrow with a null location");
        Validate.notNull(velocity, "Can not spawn arrow with a null velocity");
        Validate.notNull(clazz, "Can not spawn an arrow with no class");

        AbstractArrowEntity arrow;
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = net.minecraft.entity.EntityType.ARROW.create(world);
            ((ArrowEntity) arrow).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
        } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
            arrow = net.minecraft.entity.EntityType.SPECTRAL_ARROW.create(world);
        } else if (Trident.class.isAssignableFrom(clazz)) {
            arrow = net.minecraft.entity.EntityType.TRIDENT.create(world);
        } else {
            arrow = net.minecraft.entity.EntityType.ARROW.create(world);
        }

        arrow.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.shoot(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        world.addFreshEntity(arrow);
        return (T) arrow.getBukkitEntity();
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType entityType) {
        return spawn(loc, entityType.getEntityClass());
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        LightningBoltEntity lightning = net.minecraft.entity.EntityType.LIGHTNING_BOLT.create(world);
        lightning.teleportToWithTicket(loc.getX(), loc.getY(), loc.getZ());
        world.strikeLightning(lightning);
        return (LightningStrike) lightning.getBukkitEntity();
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        LightningBoltEntity lightning = net.minecraft.entity.EntityType.LIGHTNING_BOLT.create(world);
        lightning.teleportToWithTicket(loc.getX(), loc.getY(), loc.getZ());
        lightning.setVisualOnly(true);
        world.strikeLightning(lightning);
        return (LightningStrike) lightning.getBukkitEntity();
    }

    @Override
    public boolean generateTree(Location loc, TreeType type) {
        BlockPos pos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        ConfiguredFeature gen;
        switch (type) {
        case BIG_TREE:
            gen = Features.FANCY_OAK;
            break;
        case BIRCH:
            gen = Features.BIRCH;
            break;
        case REDWOOD:
            gen = Features.SPRUCE;
            break;
        case TALL_REDWOOD:
            gen = Features.PINE;
            break;
        case JUNGLE:
            gen = Features.MEGA_JUNGLE_TREE;
            break;
        case SMALL_JUNGLE:
            gen = Features.JUNGLE_TREE_NO_VINE;
            break;
        case COCOA_TREE:
            gen = Features.JUNGLE_TREE;
            break;
        case JUNGLE_BUSH:
            gen = Features.JUNGLE_BUSH;
            break;
        case RED_MUSHROOM:
            gen = Features.HUGE_RED_MUSHROOM;
            break;
        case BROWN_MUSHROOM:
            gen = Features.HUGE_BROWN_MUSHROOM;
            break;
        case SWAMP:
            gen = Features.SWAMP_TREE;
            break;
        case ACACIA:
            gen = Features.ACACIA;
            break;
        case DARK_OAK:
            gen = Features.DARK_OAK;
            break;
        case MEGA_REDWOOD:
            gen = Features.MEGA_PINE;
            break;
        case TALL_BIRCH:
            gen = Features.OAK_BEES_0002;
            break;
        case CHORUS_PLANT:
            ((ChorusFlowerBlock) Blocks.CHORUS_FLOWER).generatePlant(world, pos, rand, 8);
            return true;
        case CRIMSON_FUNGUS:
            gen = Features.CRIMSON_FUNGI_PLANTED;
            break;
        case WARPED_FUNGUS:
            gen = Features.WARPED_FUNGI_PLANTED;
            break;
        case TREE:
        default:
            gen = Features.OAK;
            break;
        }

        return gen.feature.place(world, world.getChunkSource().getGenerator(), rand, pos, gen.config);
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        world.captureTreeGeneration = true;
        world.captureBlockStates = true;
        boolean grownTree = generateTree(loc, type);
        world.captureBlockStates = false;
        world.captureTreeGeneration = false;
        if (grownTree) { // Copy block data to delegate
            for (org.bukkit.block.BlockState blockstate : world.capturedBlockStates.values()) {
                BlockPos position = ((CraftBlockState) blockstate).getPosition();
                net.minecraft.block.BlockState oldBlock = world.getBlockState(position);
                int flag = ((CraftBlockState) blockstate).getFlag();
                delegate.setBlockData(blockstate.getX(), blockstate.getY(), blockstate.getZ(), blockstate.getBlockData());
                net.minecraft.block.BlockState newBlock = world.getBlockState(position);
                world.markAndNotifyBlock(position, null, oldBlock, newBlock, flag, 512);
            }
            world.capturedBlockStates.clear();
            return true;
        } else {
            world.capturedBlockStates.clear();
            return false;
        }
    }

    @Override
    public String getName() {
        return world.serverLevelData.getLevelName();
    }

    @Override
    public UUID getUID() {
        return world.uuid;
    }

    @Override
    public String toString() {
        return "CraftWorld{name=" + getName() + '}';
    }

    @Override
    public long getTime() {
        long time = getFullTime() % 24000;
        if (time < 0) time += 24000;
        return time;
    }

    @Override
    public void setTime(long time) {
        long margin = (time - getFullTime()) % 24000;
        if (margin < 0) margin += 24000;
        setFullTime(getFullTime() + margin);
    }

    @Override
    public long getFullTime() {
        return world.getDayTime();
    }

    @Override
    public void setFullTime(long time) {
        // Notify anyone who's listening
        TimeSkipEvent event = new TimeSkipEvent(this, TimeSkipEvent.SkipReason.CUSTOM, time - world.getDayTime());
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        world.setDayTime(world.getDayTime() + event.getSkipAmount());

        // Forces the client to update to the new time immediately
        for (Player p : getPlayers()) {
            CraftPlayer cp = (CraftPlayer) p;
            if (cp.getHandle().connection == null) continue;

            cp.getHandle().connection.send(new SUpdateTimePacket(cp.getHandle().level.getGameTime(), cp.getHandle().getPlayerTime(), cp.getHandle().level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
        }
    }

    @Override
    public long getGameTime() {
        return world.worldDataServer.getGameTime();
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return createExplosion(x, y, z, power, false, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return createExplosion(x, y, z, power, setFire, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(x, y, z, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, Entity source) {
        return !world.explode(source == null ? null : ((CraftEntity) source).getHandle(), x, y, z, power, setFire, breakBlocks ? Explosion.Mode.BREAK : Explosion.Mode.NONE).wasCanceled;
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        return createExplosion(loc, power, false);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return createExplosion(loc, power, setFire, true);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(loc, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
        Preconditions.checkArgument(loc != null, "Location is null");
        Preconditions.checkArgument(this.equals(loc.getWorld()), "Location not in world");

        return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire, breakBlocks, source);
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    @Override
    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return populators;
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z, org.bukkit.HeightMap heightMap) {
        // Transient load for this tick
        return world.getChunk(x >> 4, z >> 4).getHeight(CraftHeightMap.toNMS(heightMap), x, z);
    }

    @Override
    public int getHighestBlockYAt(Location location, org.bukkit.HeightMap heightMap) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @Override
    public Block getHighestBlockAt(int x, int z, org.bukkit.HeightMap heightMap) {
        return getBlockAt(x, getHighestBlockYAt(x, z, heightMap), z);
    }

    @Override
    public Block getHighestBlockAt(Location location, org.bukkit.HeightMap heightMap) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @Override
    public Biome getBiome(int x, int z) {
        return getBiome(x, 0, z);
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return CraftBlock.biomeBaseToBiome(getHandle().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2));
    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        for (int y = 0; y < getMaxHeight(); y++) {
            setBiome(x, y, z, bio);
        }
    }

    @Override
    public void setBiome(int x, int y, int z, Biome bio) {
        Preconditions.checkArgument(bio != Biome.CUSTOM, "Cannot set the biome to %s", bio);
        net.minecraft.world.biome.Biome bb = CraftBlock.biomeToBiomeBase(getHandle().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), bio);
        BlockPos pos = new BlockPos(x, 0, z);
        if (this.world.hasChunkAt(pos)) {
            net.minecraft.world.chunk.Chunk chunk = this.world.getChunkAt(pos);

            if (chunk != null) {
                chunk.getBiomes().setBiome(x >> 2, y >> 2, z >> 2, bb);

                chunk.markUnsaved(); // SPIGOT-2890
            }
        }
    }

    @Override
    public double getTemperature(int x, int z) {
        return getTemperature(x, 0, z);
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2).getTemperature(pos);
    }

    @Override
    public double getHumidity(int x, int z) {
        return getHumidity(x, 0, z);
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        return this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2).getDownfall();
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();

        for (Object o : world.entitiesById.values()) {
            if (o instanceof net.minecraft.entity.Entity) {
                net.minecraft.entity.Entity mcEnt = (net.minecraft.entity.Entity) o;
                Entity bukkitEntity = mcEnt.getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null && bukkitEntity.isValid()) {
                    list.add(bukkitEntity);
                }
            }
        }

        return list;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<LivingEntity>();

        for (Object o : world.entitiesById.values()) {
            if (o instanceof net.minecraft.entity.Entity) {
                net.minecraft.entity.Entity mcEnt = (net.minecraft.entity.Entity) o;
                Entity bukkitEntity = mcEnt.getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null && bukkitEntity instanceof LivingEntity && bukkitEntity.isValid()) {
                    list.add((LivingEntity) bukkitEntity);
                }
            }
        }

        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return (Collection<T>) getEntitiesByClasses(classes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
        Collection<T> list = new ArrayList<T>();

        for (Object entity: world.entitiesById.values()) {
            if (entity instanceof net.minecraft.entity.Entity) {
                Entity bukkitEntity = ((net.minecraft.entity.Entity) entity).getBukkitEntity();

                if (bukkitEntity == null) {
                    continue;
                }

                Class<?> bukkitClass = bukkitEntity.getClass();

                if (clazz.isAssignableFrom(bukkitClass) && bukkitEntity.isValid()) {
                    list.add((T) bukkitEntity);
                }
            }
        }

        return list;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        Collection<Entity> list = new ArrayList<Entity>();

        for (Object entity: world.entitiesById.values()) {
            if (entity instanceof net.minecraft.entity.Entity) {
                Entity bukkitEntity = ((net.minecraft.entity.Entity) entity).getBukkitEntity();

                if (bukkitEntity == null) {
                    continue;
                }

                Class<?> bukkitClass = bukkitEntity.getClass();

                for (Class<?> clazz : classes) {
                    if (clazz.isAssignableFrom(bukkitClass)) {
                        if (bukkitEntity.isValid()) {
                            list.add(bukkitEntity);
                        }
                        break;
                    }
                }
            }
        }

        return list;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        return this.getNearbyEntities(location, x, y, z, null);
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z, Predicate<Entity> filter) {
        Validate.notNull(location, "Location is null!");
        Validate.isTrue(this.equals(location.getWorld()), "Location is from different world!");

        BoundingBox aabb = BoundingBox.of(location, x, y, z);
        return this.getNearbyEntities(aabb, filter);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        return this.getNearbyEntities(boundingBox, null);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter) {
        Validate.notNull(boundingBox, "Bounding box is null!");

        AxisAlignedBB bb = new AxisAlignedBB(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
        List<net.minecraft.entity.Entity> entityList = getHandle().getEntities((net.minecraft.entity.Entity) null, bb, null);
        List<Entity> bukkitEntityList = new ArrayList<org.bukkit.entity.Entity>(entityList.size());

        for (net.minecraft.entity.Entity entity : entityList) {
            Entity bukkitEntity = entity.getBukkitEntity();
            if (filter == null || filter.test(bukkitEntity)) {
                bukkitEntityList.add(bukkitEntity);
            }
        }

        return bukkitEntityList;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        return this.rayTraceEntities(start, direction, maxDistance, null);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize) {
        return this.rayTraceEntities(start, direction, maxDistance, raySize, null);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, Predicate<Entity> filter) {
        return this.rayTraceEntities(start, direction, maxDistance, 0.0D, filter);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize, Predicate<Entity> filter) {
        Validate.notNull(start, "Start location is null!");
        Validate.isTrue(this.equals(start.getWorld()), "Start location is from different world!");
        start.checkFinite();

        Validate.notNull(direction, "Direction is null!");
        direction.checkFinite();

        Validate.isTrue(direction.lengthSquared() > 0, "Direction's magnitude is 0!");

        if (maxDistance < 0.0D) {
            return null;
        }

        Vector startPos = start.toVector();
        Vector dir = direction.clone().normalize().multiply(maxDistance);
        BoundingBox aabb = BoundingBox.of(startPos, startPos).expandDirectional(dir).expand(raySize);
        Collection<Entity> entities = this.getNearbyEntities(aabb, filter);

        Entity nearestHitEntity = null;
        RayTraceResult nearestHitResult = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        for (Entity entity : entities) {
            BoundingBox boundingBox = entity.getBoundingBox().expand(raySize);
            RayTraceResult hitResult = boundingBox.rayTrace(startPos, direction, maxDistance);

            if (hitResult != null) {
                double distanceSq = startPos.distanceSquared(hitResult.getHitPosition());

                if (distanceSq < nearestDistanceSq) {
                    nearestHitEntity = entity;
                    nearestHitResult = hitResult;
                    nearestDistanceSq = distanceSq;
                }
            }
        }

        return (nearestHitEntity == null) ? null : new RayTraceResult(nearestHitResult.getHitPosition(), nearestHitEntity, nearestHitResult.getHitBlockFace());
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        return this.rayTraceBlocks(start, direction, maxDistance, FluidCollisionMode.NEVER, false);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
        return this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, false);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        Validate.notNull(start, "Start location is null!");
        Validate.isTrue(this.equals(start.getWorld()), "Start location is from different world!");
        start.checkFinite();

        Validate.notNull(direction, "Direction is null!");
        direction.checkFinite();

        Validate.isTrue(direction.lengthSquared() > 0, "Direction's magnitude is 0!");
        Validate.notNull(fluidCollisionMode, "Fluid collision mode is null!");

        if (maxDistance < 0.0D) {
            return null;
        }

        Vector dir = direction.clone().normalize().multiply(maxDistance);
        Vector3d startPos = new Vector3d(start.getX(), start.getY(), start.getZ());
        Vector3d endPos = new Vector3d(start.getX() + dir.getX(), start.getY() + dir.getY(), start.getZ() + dir.getZ());
        net.minecraft.util.math.RayTraceResult nmsHitResult = this.getHandle().clip(new RayTraceContext(startPos, endPos, ignorePassableBlocks ? RayTraceContext.BlockMode.COLLIDER : RayTraceContext.BlockMode.OUTLINE, CraftFluidCollisionMode.toNMS(fluidCollisionMode), null));

        return CraftRayTraceResult.fromNMS(this, nmsHitResult);
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, Predicate<Entity> filter) {
        RayTraceResult blockHit = this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks);
        Vector startVec = null;
        double blockHitDistance = maxDistance;

        // limiting the entity search range if we found a block hit:
        if (blockHit != null) {
            startVec = start.toVector();
            blockHitDistance = startVec.distance(blockHit.getHitPosition());
        }

        RayTraceResult entityHit = this.rayTraceEntities(start, direction, blockHitDistance, raySize, filter);
        if (blockHit == null) {
            return entityHit;
        }

        if (entityHit == null) {
            return blockHit;
        }

        // Cannot be null as blockHit == null returns above
        double entityHitDistanceSquared = startVec.distanceSquared(entityHit.getHitPosition());
        if (entityHitDistanceSquared < (blockHitDistance * blockHitDistance)) {
            return entityHit;
        }

        return blockHit;
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>(world.players().size());

        for (PlayerEntity human : world.players()) {
            HumanEntity bukkitEntity = human.getBukkitEntity();

            if ((bukkitEntity != null) && (bukkitEntity instanceof Player)) {
                list.add((Player) bukkitEntity);
            }
        }

        return list;
    }

    @Override
    public void save() {
        this.server.checkSaveState();
        boolean oldSave = world.noSave;

        world.noSave = false;
        world.save(null, false, false);

        world.noSave = oldSave;
    }

    @Override
    public boolean isAutoSave() {
        return !world.noSave;
    }

    @Override
    public void setAutoSave(boolean value) {
        world.noSave = !value;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        if (world.convertable != world.getServer().storageSource) {
            world.worldDataServer.setDifficulty(net.minecraft.world.Difficulty.byId(difficulty.getValue()));
            return;
        }
        world.getServer().getWorldData().setDifficulty(net.minecraft.world.Difficulty.byId(difficulty.getValue()));
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    public BlockMetadataStore getBlockMetadata() {
        return blockMetadata;
    }

    @Override
    public boolean hasStorm() {
        return world.getLevelData().isRaining();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        world.getLevelData().setRaining(hasStorm);
        setWeatherDuration(0); // Reset weather duration (legacy behaviour)
        setClearWeatherDuration(0); // Reset clear weather duration (reset "/weather clear" commands)
    }

    @Override
    public int getWeatherDuration() {
        return world.getServerWorldInfo().getRainTime();
    }

    @Override
    public void setWeatherDuration(int duration) {
        world.getServerWorldInfo().setRainTime(duration);
    }

    @Override
    public boolean isThundering() {
        return world.getLevelData().isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        world.getServerWorldInfo().setThundering(thundering);
        setThunderDuration(0); // Reset weather duration (legacy behaviour)
        setClearWeatherDuration(0); // Reset clear weather duration (reset "/weather clear" commands)
    }

    @Override
    public int getThunderDuration() {
        return world.getServerWorldInfo().getThunderTime();
    }

    @Override
    public void setThunderDuration(int duration) {
        world.getServerWorldInfo().setThunderTime(duration);
    }

    @Override
    public boolean isClearWeather() {
        return !this.hasStorm() && !this.isThundering();
    }

    @Override
    public void setClearWeatherDuration(int duration) {
        world.getServerWorldInfo().setClearWeatherTime(duration);
    }

    @Override
    public int getClearWeatherDuration() {
        return world.getServerWorldInfo().getClearWeatherTime();
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public boolean getPVP() {
        return world.pvpMode;
    }

    @Override
    public void setPVP(boolean pvp) {
        world.pvpMode = pvp;
    }

    public void playEffect(Player player, Effect effect, int data) {
        playEffect(player.getLocation(), effect, data, 0);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        playEffect(location, effect, data, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        playEffect(loc, effect, data, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data, int radius) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue, radius);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(effect, "Effect cannot be null");
        Validate.notNull(location.getWorld(), "World cannot be null");
        int packetData = effect.getId();
        SPlaySoundEventPacket packet = new SPlaySoundEventPacket(packetData, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), data, false);
        int distance;
        radius *= radius;

        for (Player player : getPlayers()) {
            if (((CraftPlayer) player).getHandle().connection == null) continue;
            if (!location.getWorld().equals(player.getWorld())) continue;

            distance = (int) player.getLocation().distanceSquared(location);
            if (distance <= radius) {
                ((CraftPlayer) player).getHandle().connection.send(packet);
            }
        }
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return spawn(location, clazz, null, SpawnReason.CUSTOM);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
        return spawn(location, clazz, function, SpawnReason.CUSTOM);
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        Validate.notNull(data, "MaterialData cannot be null");
        return spawnFallingBlock(location, data.getItemType(), data.getData());
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, org.bukkit.Material material, byte data) throws IllegalArgumentException {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(material.isBlock(), "Material must be a block");

        FallingBlockEntity entity = new FallingBlockEntity(world, location.getX(), location.getY(), location.getZ(), CraftMagicNumbers.getBlock(material).defaultBlockState());
        entity.time = 1;

        world.addEntity(entity, SpawnReason.CUSTOM);
        return (FallingBlock) entity.getBukkitEntity();
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(data, "Material cannot be null");

        FallingBlockEntity entity = new FallingBlockEntity(world, location.getX(), location.getY(), location.getZ(), ((CraftBlockData) data).getState());
        entity.time = 1;

        world.addEntity(entity, SpawnReason.CUSTOM);
        return (FallingBlock) entity.getBukkitEntity();
    }

    @SuppressWarnings("unchecked")
    public net.minecraft.entity.Entity createEntity(Location location, Class<? extends Entity> clazz) throws IllegalArgumentException {
        if (location == null || clazz == null) {
            throw new IllegalArgumentException("Location or entity class cannot be null");
        }

        net.minecraft.entity.Entity entity = null;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        // order is important for some of these
        if (Boat.class.isAssignableFrom(clazz)) {
            entity = new BoatEntity(world, x, y, z);
            entity.moveTo(x, y, z, yaw, pitch);
        } else if (FallingBlock.class.isAssignableFrom(clazz)) {
            entity = new FallingBlockEntity(world, x, y, z, world.getBlockState(new BlockPos(x, y, z)));
        } else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = new SnowballEntity(world, x, y, z);
            } else if (Egg.class.isAssignableFrom(clazz)) {
                entity = new EggEntity(world, x, y, z);
            } else if (AbstractArrow.class.isAssignableFrom(clazz)) {
                if (TippedArrow.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.ARROW.create(world);
                    ((ArrowEntity) entity).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
                } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.SPECTRAL_ARROW.create(world);
                } else if (Trident.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.TRIDENT.create(world);
                } else {
                    entity = net.minecraft.entity.EntityType.ARROW.create(world);
                }
                entity.moveTo(x, y, z, 0, 0);
            } else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.EXPERIENCE_BOTTLE.create(world);
                entity.moveTo(x, y, z, 0, 0);
            } else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.ENDER_PEARL.create(world);
                entity.moveTo(x, y, z, 0, 0);
            } else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                if (LingeringPotion.class.isAssignableFrom(clazz)) {
                    entity = new PotionEntity(world, x, y, z);
                    ((PotionEntity) entity).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
                } else {
                    entity = new PotionEntity(world, x, y, z);
                    ((PotionEntity) entity).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
                }
            } else if (Fireball.class.isAssignableFrom(clazz)) {
                if (SmallFireball.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.SMALL_FIREBALL.create(world);
                } else if (WitherSkull.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.WITHER_SKULL.create(world);
                } else if (DragonFireball.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.DRAGON_FIREBALL.create(world);
                } else {
                    entity = net.minecraft.entity.EntityType.FIREBALL.create(world);
                }
                entity.moveTo(x, y, z, yaw, pitch);
                Vector direction = location.getDirection().multiply(10);
                ((DamagingProjectileEntity) entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            } else if (ShulkerBullet.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.SHULKER_BULLET.create(world);
                entity.moveTo(x, y, z, yaw, pitch);
            } else if (LlamaSpit.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.LLAMA_SPIT.create(world);
                entity.moveTo(x, y, z, yaw, pitch);
            } else if (Firework.class.isAssignableFrom(clazz)) {
                entity = new FireworkRocketEntity(world, x, y, z, net.minecraft.item.ItemStack.EMPTY);
            }
        } else if (Minecart.class.isAssignableFrom(clazz)) {
            if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                entity = new FurnaceMinecartEntity(world, x, y, z);
            } else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                entity = new ChestMinecartEntity(world, x, y, z);
            } else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
                entity = new TNTMinecartEntity(world, x, y, z);
            } else if (HopperMinecart.class.isAssignableFrom(clazz)) {
                entity = new HopperMinecartEntity(world, x, y, z);
            } else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
                entity = new SpawnerMinecartEntity(world, x, y, z);
            } else if (CommandMinecart.class.isAssignableFrom(clazz)) {
                entity = new MinecartEntity(world, x, y, z);
            } else { // Default to rideable minecart for pre-rideable compatibility
                entity = new MinecartEntity(world, x, y, z);
            }
        } else if (EnderSignal.class.isAssignableFrom(clazz)) {
            entity = new EyeOfEnderEntity(world, x, y, z);
        } else if (EnderCrystal.class.isAssignableFrom(clazz)) {
            entity = net.minecraft.entity.EntityType.END_CRYSTAL.create(world);
            entity.moveTo(x, y, z, 0, 0);
        } else if (LivingEntity.class.isAssignableFrom(clazz)) {
            if (Chicken.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.CHICKEN.create(world);
            } else if (Cow.class.isAssignableFrom(clazz)) {
                if (MushroomCow.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.MOOSHROOM.create(world);
                } else {
                    entity = net.minecraft.entity.EntityType.COW.create(world);
                }
            } else if (Golem.class.isAssignableFrom(clazz)) {
                if (Snowman.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.SNOW_GOLEM.create(world);
                } else if (IronGolem.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.IRON_GOLEM.create(world);
                } else if (Shulker.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.SHULKER.create(world);
                }
            } else if (Creeper.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.CREEPER.create(world);
            } else if (Ghast.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.GHAST.create(world);
            } else if (Pig.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.PIG.create(world);
            } else if (Player.class.isAssignableFrom(clazz)) {
                // need a net server handler for this one
            } else if (Sheep.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.SHEEP.create(world);
            } else if (AbstractHorse.class.isAssignableFrom(clazz)) {
                if (ChestedHorse.class.isAssignableFrom(clazz)) {
                    if (Donkey.class.isAssignableFrom(clazz)) {
                        entity = net.minecraft.entity.EntityType.DONKEY.create(world);
                    } else if (Mule.class.isAssignableFrom(clazz)) {
                        entity = net.minecraft.entity.EntityType.MULE.create(world);
                    } else if (Llama.class.isAssignableFrom(clazz)) {
                        if (TraderLlama.class.isAssignableFrom(clazz)) {
                            entity = net.minecraft.entity.EntityType.TRADER_LLAMA.create(world);
                        } else {
                            entity = net.minecraft.entity.EntityType.LLAMA.create(world);
                        }
                    }
                } else if (SkeletonHorse.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.SKELETON_HORSE.create(world);
                } else if (ZombieHorse.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.ZOMBIE_HORSE.create(world);
                } else {
                    entity = net.minecraft.entity.EntityType.HORSE.create(world);
                }
            } else if (Skeleton.class.isAssignableFrom(clazz)) {
                if (Stray.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.STRAY.create(world);
                } else if (WitherSkeleton.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.WITHER_SKELETON.create(world);
                } else {
                    entity = net.minecraft.entity.EntityType.SKELETON.create(world);
                }
            } else if (Slime.class.isAssignableFrom(clazz)) {
                if (MagmaCube.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.MAGMA_CUBE.create(world);
                } else {
                    entity = net.minecraft.entity.EntityType.SLIME.create(world);
                }
            } else if (Spider.class.isAssignableFrom(clazz)) {
                if (CaveSpider.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.CAVE_SPIDER.create(world);
                } else {
                    entity = net.minecraft.entity.EntityType.SPIDER.create(world);
                }
            } else if (Squid.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.SQUID.create(world);
            } else if (Tameable.class.isAssignableFrom(clazz)) {
                if (Wolf.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.WOLF.create(world);
                } else if (Parrot.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.PARROT.create(world);
                } else if (Cat.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.CAT.create(world);
                }
            } else if (PigZombie.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.ZOMBIFIED_PIGLIN.create(world);
            } else if (Zombie.class.isAssignableFrom(clazz)) {
                if (Husk.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.HUSK.create(world);
                } else if (ZombieVillager.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.ZOMBIE_VILLAGER.create(world);
                } else if (Drowned.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.DROWNED.create(world);
                } else {
                    entity = new ZombieEntity(world);
                }
            } else if (Giant.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.GIANT.create(world);
            } else if (Silverfish.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.SILVERFISH.create(world);
            } else if (Enderman.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.ENDERMAN.create(world);
            } else if (Blaze.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.BLAZE.create(world);
            } else if (AbstractVillager.class.isAssignableFrom(clazz)) {
                if (Villager.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.VILLAGER.create(world);
                } else if (WanderingTrader.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.WANDERING_TRADER.create(world);
                }
            } else if (Witch.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.WITCH.create(world);
            } else if (Wither.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.WITHER.create(world);
            } else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                if (EnderDragon.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.ENDER_DRAGON.create(world);
                }
            } else if (Ambient.class.isAssignableFrom(clazz)) {
                if (Bat.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.BAT.create(world);
                }
            } else if (Rabbit.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.RABBIT.create(world);
            } else if (Endermite.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.ENDERMITE.create(world);
            } else if (Guardian.class.isAssignableFrom(clazz)) {
                if (ElderGuardian.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.ELDER_GUARDIAN.create(world);
                } else {
                    entity = net.minecraft.entity.EntityType.GUARDIAN.create(world);
                }
            } else if (ArmorStand.class.isAssignableFrom(clazz)) {
                entity = new ArmorStandEntity(world, x, y, z);
            } else if (PolarBear.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.POLAR_BEAR.create(world);
            } else if (Vex.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.VEX.create(world);
            } else if (Illager.class.isAssignableFrom(clazz)) {
                if (Spellcaster.class.isAssignableFrom(clazz)) {
                    if (Evoker.class.isAssignableFrom(clazz)) {
                        entity = net.minecraft.entity.EntityType.EVOKER.create(world);
                    } else if (Illusioner.class.isAssignableFrom(clazz)) {
                        entity = net.minecraft.entity.EntityType.ILLUSIONER.create(world);
                    }
                } else if (Vindicator.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.VINDICATOR.create(world);
                } else if (Pillager.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.PILLAGER.create(world);
                }
            } else if (Turtle.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.TURTLE.create(world);
            } else if (Phantom.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.PHANTOM.create(world);
            } else if (Fish.class.isAssignableFrom(clazz)) {
                if (Cod.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.COD.create(world);
                } else if (PufferFish.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.PUFFERFISH.create(world);
                } else if (Salmon.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.SALMON.create(world);
                } else if (TropicalFish.class.isAssignableFrom(clazz)) {
                    entity = net.minecraft.entity.EntityType.TROPICAL_FISH.create(world);
                }
            } else if (Dolphin.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.DOLPHIN.create(world);
            } else if (Ocelot.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.OCELOT.create(world);
            } else if (Ravager.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.RAVAGER.create(world);
            } else if (Panda.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.PANDA.create(world);
            } else if (Fox.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.FOX.create(world);
            } else if (Bee.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.BEE.create(world);
            } else if (Hoglin.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.HOGLIN.create(world);
            } else if (Piglin.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.PIGLIN.create(world);
            } else if (PiglinBrute.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.PIGLIN_BRUTE.create(world);
            } else if (Strider.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.STRIDER.create(world);
            } else if (Zoglin.class.isAssignableFrom(clazz)) {
                entity = net.minecraft.entity.EntityType.ZOGLIN.create(world);
            }

            if (entity != null) {
                entity.absMoveTo(x, y, z, yaw, pitch);
                entity.setYHeadRot(yaw); // SPIGOT-3587
            }
        } else if (Hanging.class.isAssignableFrom(clazz)) {
            BlockFace face = BlockFace.SELF;

            int width = 16; // 1 full block, also painting smallest size.
            int height = 16; // 1 full block, also painting smallest size.

            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
            } else if (LeashHitch.class.isAssignableFrom(clazz)) {
                width = 9;
                height = 9;
            }

            // Paper start - In addition to d65a2576e40e58c8e446b330febe6799d13a604f do not check UP/DOWN for non item frames
            // BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};
            BlockFace[] faces = (ItemFrame.class.isAssignableFrom(clazz))
                    ? new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN}
                    : new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
            // Paper end
            final BlockPos pos = new BlockPos(x, y, z);
            for (BlockFace dir : faces) {
                net.minecraft.block.BlockState nmsBlock = world.getBlockState(pos.relative(CraftBlock.blockFaceToNotch(dir)));
                if (nmsBlock.getMaterial().isSolid() || RedstoneDiodeBlock.isDiode(nmsBlock)) {
                    boolean taken = false;
                    AxisAlignedBB bb = (ItemFrame.class.isAssignableFrom(clazz))
                            ? ItemFrameEntity.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height)
                            : HangingEntity.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height);
                    List<net.minecraft.entity.Entity> list = (List<net.minecraft.entity.Entity>) world.getEntities(null, bb);
                    for (Iterator<net.minecraft.entity.Entity> it = list.iterator(); !taken && it.hasNext();) {
                        net.minecraft.entity.Entity e = it.next();
                        if (e instanceof HangingEntity) {
                            taken = true; // Hanging entities do not like hanging entities which intersect them.
                        }
                    }

                    if (!taken) {
                        face = dir;
                        break;
                    }
                }
            }

            if (LeashHitch.class.isAssignableFrom(clazz)) {
                entity = new LeashKnotEntity(world, new BlockPos(x, y, z));
                entity.forcedLoading = true;
            } else {
                // No valid face found
                Preconditions.checkArgument(face != BlockFace.SELF, "Cannot spawn hanging entity for %s at %s (no free face)", clazz.getName(), location);

                Direction dir = CraftBlock.blockFaceToNotch(face).getOpposite();
                if (Painting.class.isAssignableFrom(clazz)) {
                    entity = new PaintingEntity(world, new BlockPos(x, y, z), dir);
                } else if (ItemFrame.class.isAssignableFrom(clazz)) {
                    entity = new ItemFrameEntity(world, new BlockPos(x, y, z), dir);
                }
            }

            if (entity != null && !((HangingEntity) entity).survives()) {
                throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
            }
        } else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = new TNTEntity(world, x, y, z, null);
        } else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = new ExperienceOrbEntity(world, x, y, z, 0);
        } else if (LightningStrike.class.isAssignableFrom(clazz)) {
            entity = net.minecraft.entity.EntityType.LIGHTNING_BOLT.create(world);
        } else if (AreaEffectCloud.class.isAssignableFrom(clazz)) {
            entity = new AreaEffectCloudEntity(world, x, y, z);
        } else if (EvokerFangs.class.isAssignableFrom(clazz)) {
            entity = new EvokerFangsEntity(world, x, y, z, (float) Math.toRadians(yaw), 0, null);
        }

        if (entity != null) {
            return entity;
        }

        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.entity.Entity entity, SpawnReason reason) throws IllegalArgumentException {
        return addEntity(entity, reason, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.entity.Entity entity, SpawnReason reason, Consumer<T> function) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");

        if (entity instanceof MobEntity) {
            ((MobEntity) entity).finalizeSpawn(getHandle(), getHandle().getCurrentDifficultyAt(entity.blockPosition()), net.minecraft.entity.SpawnReason.COMMAND, (ILivingEntityData) null, null);
        }

        if (function != null) {
            function.accept((T) entity.getBukkitEntity());
        }

        world.addEntity(entity, reason);
        return (T) entity.getBukkitEntity();
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function, SpawnReason reason) throws IllegalArgumentException {
        net.minecraft.entity.Entity entity = createEntity(location, clazz);

        return addEntity(entity, reason, function);
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        world.setSpawnSettings(allowMonsters, allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {
        return world.getChunkSource().spawnFriendlies;
    }

    @Override
    public boolean getAllowMonsters() {
        return world.getChunkSource().spawnEnemies;
    }

    @Override
    public int getMinHeight(){
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return world.getHeight();
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return (world.dimension() == server.console.overworld().dimension() || CatServer.getConfig().keepSpawnInMemory) && world.keepSpawnInMemory; // CatServer
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        world.keepSpawnInMemory = keepLoaded;
        // Grab the worlds spawn chunk
        BlockPos chunkcoordinates = this.world.getSharedSpawnPos();
        if (keepLoaded) {
            world.getChunkSource().addRegionTicket(TicketType.START, new ChunkPos(chunkcoordinates), 11, Unit.INSTANCE);
        } else {
            // TODO: doesn't work well if spawn changed....
            world.getChunkSource().removeRegionTicket(TicketType.START, new ChunkPos(chunkcoordinates), 11, Unit.INSTANCE);
        }
    }

    @Override
    public int hashCode() {
        return getUID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final CraftWorld other = (CraftWorld) obj;

        return this.getUID() == other.getUID();
    }

    @Override
    public File getWorldFolder() {
        return world.convertable.getDimensionPath(this.world.dimension());
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);

        for (Player player : getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    @Override
    public org.bukkit.WorldType getWorldType() {
        return world.isFlat() ? org.bukkit.WorldType.FLAT : org.bukkit.WorldType.NORMAL;
    }

    @Override
    public boolean canGenerateStructures() {
        if (world.convertable != world.getServer().storageSource) {
            return world.worldDataServer.worldGenSettings().generateFeatures();
        }
        return world.getServer().getWorldData().worldGenSettings().generateFeatures();
    }

    @Override
    public boolean isHardcore() {
        return world.getLevelData().isHardcore();
    }

    @Override
    public void setHardcore(boolean hardcore) {
        if (world.convertable != world.getServer().storageSource) {
            world.worldDataServer.getLevelSettings().hardcore = hardcore;
            return;
        }
        world.getServer().getWorldData().getLevelSettings().hardcore = hardcore;
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return world.ticksPerAnimalSpawns;
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        world.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return world.ticksPerMonsterSpawns;
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        world.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
    }

    @Override
    public long getTicksPerWaterSpawns() {
        return world.ticksPerWaterSpawns;
    }

    @Override
    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
        world.ticksPerWaterSpawns = ticksPerWaterSpawns;
    }

    @Override
    public long getTicksPerWaterAmbientSpawns() {
        return world.ticksPerWaterAmbientSpawns;
    }

    @Override
    public void setTicksPerWaterAmbientSpawns(int ticksPerWaterAmbientSpawns) {
        world.ticksPerWaterAmbientSpawns = ticksPerWaterAmbientSpawns;
    }

    @Override
    public long getTicksPerAmbientSpawns() {
        return world.ticksPerAmbientSpawns;
    }

    @Override
    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
        world.ticksPerAmbientSpawns = ticksPerAmbientSpawns;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getWorldMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getWorldMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public int getMonsterSpawnLimit() {
        if (monsterSpawn < 0) {
            return server.getMonsterSpawnLimit();
        }

        return monsterSpawn;
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {
        monsterSpawn = limit;
    }

    @Override
    public int getAnimalSpawnLimit() {
        if (animalSpawn < 0) {
            return server.getAnimalSpawnLimit();
        }

        return animalSpawn;
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {
        animalSpawn = limit;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        if (waterAnimalSpawn < 0) {
            return server.getWaterAnimalSpawnLimit();
        }

        return waterAnimalSpawn;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {
        waterAnimalSpawn = limit;
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        if (waterAmbientSpawn < 0) {
            return server.getWaterAmbientSpawnLimit();
        }

        return waterAmbientSpawn;
    }

    @Override
    public void setWaterAmbientSpawnLimit(int limit) {
        waterAmbientSpawn = limit;
    }

    @Override
    public int getAmbientSpawnLimit() {
        if (ambientSpawn < 0) {
            return server.getAmbientSpawnLimit();
        }

        return ambientSpawn;
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {
        ambientSpawn = limit;
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        getHandle().playSound(null, x, y, z, CraftSound.getSoundEffect(sound), SoundCategory.valueOf(category.name()), volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        SPlaySoundPacket packet = new SPlaySoundPacket(new ResourceLocation(sound), SoundCategory.valueOf(category.name()), new Vector3d(x, y, z), volume, pitch);
        world.getServer().getPlayerList().broadcast(null, x, y, z, volume > 1.0F ? 16.0F * volume : 16.0D, this.world.dimension(), packet);
    }

    private static Map<String, RuleKey<?>> gamerules;
    public static synchronized Map<String, RuleKey<?>> getGameRulesNMS() {
        if (gamerules != null) {
            return gamerules;
        }

        Map<String, RuleKey<?>> gamerules = new HashMap<>();
        GameRules.visitGameRuleTypes(new GameRules.IRuleEntryVisitor() {
            @Override
            public <T extends RuleValue<T>> void visit(RuleKey<T> gamerules_gamerulekey, RuleType<T> gamerules_gameruledefinition) {
                gamerules.put(gamerules_gamerulekey.getId(), gamerules_gamerulekey);
            }
        });

        return CraftWorld.gamerules = gamerules;
    }

    private static Map<String, RuleType<?>> gameruleDefinitions;
    public static synchronized Map<String, RuleType<?>> getGameRuleDefinitions() {
        if (gameruleDefinitions != null) {
            return gameruleDefinitions;
        }

        Map<String, RuleType<?>> gameruleDefinitions = new HashMap<>();
        GameRules.visitGameRuleTypes(new GameRules.IRuleEntryVisitor() {
            @Override
            public <T extends RuleValue<T>> void visit(RuleKey<T> gamerules_gamerulekey, RuleType<T> gamerules_gameruledefinition) {
                gameruleDefinitions.put(gamerules_gamerulekey.getId(), gamerules_gameruledefinition);
            }
        });

        return CraftWorld.gameruleDefinitions = gameruleDefinitions;
    }

    @Override
    public String getGameRuleValue(String rule) {
        // In method contract for some reason
        if (rule == null) {
            return null;
        }

        GameRules.RuleValue<?> value = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule));
        return value != null ? value.toString() : "";
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        // No null values allowed
        if (rule == null || value == null) return false;

        if (!isGameRule(rule)) return false;

        GameRules.RuleValue<?> handle = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule));
        handle.deserialize(value);
        handle.onChanged(getHandle().getServer());
        return true;
    }

    @Override
    public String[] getGameRules() {
        return getGameRulesNMS().keySet().toArray(new String[getGameRulesNMS().size()]);
    }

    @Override
    public boolean isGameRule(String rule) {
        Validate.isTrue(rule != null && !rule.isEmpty(), "Rule cannot be null nor empty");
        return getGameRulesNMS().containsKey(rule);
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        Validate.notNull(rule, "GameRule cannot be null");
        return convert(rule, getHandle().getGameRules().getRule(getGameRulesNMS().get(rule.getName())));
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        Validate.notNull(rule, "GameRule cannot be null");
        return convert(rule, getGameRuleDefinitions().get(rule.getName()).createRule());
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        Validate.notNull(rule, "GameRule cannot be null");
        Validate.notNull(newValue, "GameRule value cannot be null");

        if (!isGameRule(rule.getName())) return false;

        GameRules.RuleValue<?> handle = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule.getName()));
        handle.deserialize(newValue.toString());
        handle.onChanged(getHandle().getServer());
        return true;
    }

    private <T> T convert(GameRule<T> rule, GameRules.RuleValue<?> value) {
        if (value == null) {
            return null;
        }

        if (value instanceof GameRules.BooleanValue) {
            return rule.getType().cast(((GameRules.BooleanValue) value).get());
        } else if (value instanceof GameRules.IntegerValue) {
            return rule.getType().cast(value.getCommandResult());
        } else {
            throw new IllegalArgumentException("Invalid GameRule type (" + value + ") for GameRule " + rule.getName());
        }
    }

    @Override
    public WorldBorder getWorldBorder() {
        if (this.worldBorder == null) {
            this.worldBorder = new CraftWorldBorder(this);
        }

        return this.worldBorder;
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
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, false);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        getHandle().sendParticles(
                null, // Sender
                CraftParticle.toNMS(particle, data), // Particle
                x, y, z, // Position
                count,  // Count
                offsetX, offsetY, offsetZ, // Random offset
                extra, // Speed?
                force
        );

    }

    @Override
    public Location locateNearestStructure(Location origin, StructureType structureType, int radius, boolean findUnexplored) {
        BlockPos originPos = new BlockPos(origin.getX(), origin.getY(), origin.getZ());
        BlockPos nearest = getHandle().getChunkSource().getGenerator().findNearestMapFeature(getHandle(), Structure.STRUCTURES_REGISTRY.get(structureType.getName()), originPos, radius, findUnexplored);
        return (nearest == null) ? null : new Location(this, nearest.getX(), nearest.getY(), nearest.getZ());
    }

    @Override
    public Raid locateNearestRaid(Location location, int radius) {
        Validate.notNull(location, "Location cannot be null");
        Validate.isTrue(radius >= 0, "Radius cannot be negative");

        RaidManager persistentRaid = world.getRaids();
        net.minecraft.world.raid.Raid raid = persistentRaid.getNearbyRaid(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), radius * radius);
        return (raid == null) ? null : new CraftRaid(raid);
    }

    @Override
    public List<Raid> getRaids() {
        RaidManager persistentRaid = world.getRaids();
        return persistentRaid.raidMap.values().stream().map(CraftRaid::new).collect(Collectors.toList());
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        return (getHandle().dragonFight() == null) ? null : new CraftDragonBattle(getHandle().dragonFight());
    }

    public TileEntity getTileEntityAt(int x, int y, int z) {
        return world.getBlockEntity(new BlockPos(x, y, z));
    }

	// Spigot start
    @Override
    public int getViewDistance() {
        return world.spigotConfig.viewDistance;
    }
    // Spigot end

    // Spigot start
    private final Spigot spigot = new Spigot()
    {

        @Override
        public LightningStrike strikeLightning(Location loc, boolean isSilent)
        {
            LightningBoltEntity lightning = net.minecraft.entity.EntityType.LIGHTNING_BOLT.create( world );
            lightning.moveTo( loc.getX(), loc.getY(), loc.getZ() );
            lightning.isSilent = isSilent;
            world.strikeLightning( lightning );
            return (LightningStrike) lightning.getBukkitEntity();
        }

        @Override
        public LightningStrike strikeLightningEffect(Location loc, boolean isSilent)
        {
            LightningBoltEntity lightning = net.minecraft.entity.EntityType.LIGHTNING_BOLT.create( world );
            lightning.moveTo( loc.getX(), loc.getY(), loc.getZ() );
            lightning.isEffect = true;
            lightning.isSilent = isSilent;
            return (LightningStrike) lightning.getBukkitEntity();
        }

    };

    public Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
