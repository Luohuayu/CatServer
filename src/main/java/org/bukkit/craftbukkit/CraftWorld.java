package org.bukkit.craftbukkit;

import catserver.server.CatServer;
import com.google.common.base.Preconditions;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.block.BlockChorusFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.commons.lang3.Validate;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public class CraftWorld implements World {
    public static final int CUSTOM_DIMENSION_OFFSET = 10;

    private final WorldServer world;
    private WorldBorder worldBorder;
    private Environment environment;
    private final CraftServer server = (CraftServer) Bukkit.getServer();
    public ChunkGenerator generator;
    private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private int ambientSpawn = -1;
    private int chunkLoadCount = 0;
    private int chunkGCTickCount;

    private static final Random rand = new Random();

    public CraftWorld(WorldServer world, ChunkGenerator gen, Environment env) {
        this.world = world;
        this.generator = gen;

        environment = env;

        if (server.chunkGCPeriod > 0) {
            chunkGCTickCount = rand.nextInt(server.chunkGCPeriod);
        }
    }

    public Block getBlockAt(int x, int y, int z) {
        return getChunkAt(x >> 4, z >> 4).getBlock(x & 0xF, y, z & 0xF);
    }

    public int getBlockTypeIdAt(int x, int y, int z) {
        return CraftMagicNumbers.getId(world.getBlockState(new BlockPos(x, y, z)).getBlock());
    }

    public int getHighestBlockYAt(int x, int z) {
        if (!isChunkLoaded(x >> 4, z >> 4)) {
            loadChunk(x >> 4, z >> 4);
        }

        return world.getHeight(new BlockPos(x, 0, z)).getY();
    }

    public Location getSpawnLocation() {
        BlockPos spawn = world.getSpawnPoint();
        return new Location(this, spawn.getX(), spawn.getY(), spawn.getZ());
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        Preconditions.checkArgument(location != null, "location");

        return equals(location.getWorld()) ? setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ()) : false;
    }

    public boolean setSpawnLocation(int x, int y, int z) {
        try {
            Location previousLocation = getSpawnLocation();
            world.worldInfo.setSpawn(new BlockPos(x, y, z));

            // Notify anyone who's listening.
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            server.getPluginManager().callEvent(event);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Chunk getChunkAt(int x, int z) {
        return this.world.getChunkProvider().provideChunk(x, z).bukkitChunk;
    }

    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    public boolean isChunkLoaded(int x, int z) {
        return world.getChunkProvider().chunkExists(x, z);
    }

    public Chunk[] getLoadedChunks() {
        Object[] chunks = world.getChunkProvider().id2ChunkMap.values().toArray();
        Chunk[] craftChunks = new CraftChunk[chunks.length];

        for (int i = 0; i < chunks.length; i++) {
            net.minecraft.world.chunk.Chunk chunk = (net.minecraft.world.chunk.Chunk) chunks[i];
            craftChunks[i] = chunk.bukkitChunk;
        }

        return craftChunks;
    }

    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    public boolean unloadChunk(Chunk chunk) {
        return unloadChunk(chunk.getX(), chunk.getZ());
    }

    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    public boolean unloadChunk(int x, int z, boolean save) {
        return unloadChunk(x, z, save, false);
    }

    public boolean unloadChunkRequest(int x, int z) {
        return unloadChunkRequest(x, z, true);
    }

    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        org.spigotmc.AsyncCatcher.catchOp( "chunk unload"); // Spigot
        if (safe && isChunkInUse(x, z)) {
            return false;
        }

        net.minecraft.world.chunk.Chunk chunk = world.getChunkProvider().getLoadedChunk(x, z);
        if (chunk != null) {
            world.getChunkProvider().queueUnload(chunk);
        }

        return true;
    }

    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        org.spigotmc.AsyncCatcher.catchOp( "chunk unload"); // Spigot
        if (isChunkInUse(x, z)) {
            return false;
        }

        return unloadChunk0(x, z, save);
    }

    private boolean unloadChunk0(int x, int z, boolean save) {
        net.minecraft.world.chunk.Chunk chunk = world.getChunkProvider().getChunkIfLoaded(x, z);
        if (chunk == null) {
            return true;
        }

        // If chunk had previously been queued to save, must do save to avoid loss of that data
        world.getChunkProvider().queueUnload(chunk);
        return chunk.unloadQueued;
    }

    public boolean regenerateChunk(int x, int z) {
        if (!unloadChunk0(x, z, false)) {
            return false;
        }

        final long chunkKey = ChunkPos.asLong(x, z);
        world.getChunkProvider().droppedChunksSet.remove(chunkKey);

        net.minecraft.world.chunk.Chunk chunk = null;

        chunk = world.getChunkProvider().chunkGenerator.generateChunk(x, z);
        PlayerChunkMapEntry playerChunk = world.getPlayerChunkMap().getEntry(x, z);
        if (playerChunk != null) {
            playerChunk.chunk = chunk;
        }

        if (chunk != null) {
            world.getChunkProvider().id2ChunkMap.put(chunkKey, chunk);

            chunk.onLoad();
            chunk.populateCB(world.getChunkProvider(), world.getChunkProvider().chunkGenerator, true);

            refreshChunk(x, z);
        }

        return chunk != null;
    }

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
            world.notifyBlockUpdate(new BlockPos(px + (idx / height), ((idx % height) * 16), pz), Blocks.AIR.getDefaultState(), Blocks.STONE.getDefaultState(), 3);
        }
        world.notifyBlockUpdate(new BlockPos(px + 15, (height * 16) - 1, pz + 15), Blocks.AIR.getDefaultState(), Blocks.STONE.getDefaultState(), 3);

        return true;
    }

    public boolean isChunkInUse(int x, int z) {
        return world.getPlayerChunkMap().isChunkInUse(x, z);
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        org.spigotmc.AsyncCatcher.catchOp( "chunk load"); // Spigot
        chunkLoadCount++;
        if (generate) {
            // Use the default variant of loadChunk when generate == true.
            return world.getChunkProvider().provideChunk(x, z) != null;
        }

        return world.getChunkProvider().loadChunk(x, z) != null;
    }

    public boolean isChunkLoaded(Chunk chunk) {
        return isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    public void loadChunk(Chunk chunk) {
        loadChunk(chunk.getX(), chunk.getZ());
        ((CraftChunk) getChunkAt(chunk.getX(), chunk.getZ())).getHandle().bukkitChunk = chunk;
    }

    public WorldServer getHandle() {
        return world;
    }

    public Item dropItem(Location loc, ItemStack item) {
        Validate.notNull(item, "Cannot drop a Null item.");
        Validate.isTrue(item.getTypeId() != 0, "Cannot drop AIR.");
        EntityItem entity = new EntityItem(world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        entity.pickupDelay = 10;
        world.addEntity(entity, SpawnReason.CUSTOM);
        // TODO this is inconsistent with how Entity.getBukkitEntity() works.
        // However, this entity is not at the moment backed by a server entity class so it may be left.
        return new CraftItem(world.getServer(), entity);
    }

    private static void randomLocationWithinBlock(Location loc, double xs, double ys, double zs) {
        double prevX = loc.getX();
        double prevY = loc.getY();
        double prevZ = loc.getZ();
        loc.add(xs, ys, zs);
        if (loc.getX() < Math.floor(prevX)) {
            loc.setX(Math.floor(prevX));
        }
        if (loc.getX() >= Math.ceil(prevX)) {
            loc.setX(Math.ceil(prevX - 0.01));
        }
        if (loc.getY() < Math.floor(prevY)) {
            loc.setY(Math.floor(prevY));
        }
        if (loc.getY() >= Math.ceil(prevY)) {
            loc.setY(Math.ceil(prevY - 0.01));
        }
        if (loc.getZ() < Math.floor(prevZ)) {
            loc.setZ(Math.floor(prevZ));
        }
        if (loc.getZ() >= Math.ceil(prevZ)) {
            loc.setZ(Math.ceil(prevZ - 0.01));
        }
    }

    public Item dropItemNaturally(Location loc, ItemStack item) {
        double xs = world.rand.nextFloat() * 0.7F - 0.35D;
        double ys = world.rand.nextFloat() * 0.7F - 0.35D;
        double zs = world.rand.nextFloat() * 0.7F - 0.35D;
        loc = loc.clone();
        // Makes sure the new item is created within the block the location points to.
        // This prevents item spill in 1-block wide farms.
        randomLocationWithinBlock(loc, xs, ys, zs);
        return dropItem(loc, item);
    }

    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        return spawnArrow(loc, velocity, speed, spread, Arrow.class);
    }

    public <T extends Arrow> T spawnArrow(Location loc, Vector velocity, float speed, float spread, Class<T> clazz) {
        Validate.notNull(loc, "Can not spawn arrow with a null location");
        Validate.notNull(velocity, "Can not spawn arrow with a null velocity");
        Validate.notNull(clazz, "Can not spawn an arrow with no class");

        EntityArrow arrow;
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = new EntityTippedArrow(world);
            ((EntityTippedArrow) arrow).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
        } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
            arrow = new EntitySpectralArrow(world);
        } else {
            arrow = new EntityTippedArrow(world);
        }

        arrow.setLocationAndAngles(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.shoot(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        world.spawnEntity(arrow);
        return (T) arrow.getBukkitEntity();
    }

    public Entity spawnEntity(Location loc, EntityType entityType) {
        if (EntityRegistry.entityClassMap.get(entityType.getName()) != null) {
            net.minecraft.entity.Entity entity = null;
            entity = getEntity(EntityRegistry.entityClassMap.get(entityType.getName()), world);
            if (entity != null) {
                entity.setLocationAndAngles(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
                world.addEntity(entity, SpawnReason.CUSTOM);
                return entity.getBukkitEntity();
            }
        }
        return spawn(loc, entityType.getEntityClass());
    }

    private net.minecraft.entity.Entity getEntity(Class<? extends net.minecraft.entity.Entity> aClass, WorldServer world) {
        EntityLiving entity = null;
        try {
            entity = (net.minecraft.entity.EntityLiving) aClass.getConstructor(new Class[] { net.minecraft.world.World.class }).newInstance(new Object[] { world });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return entity;
    }

    public LightningStrike strikeLightning(Location loc) {
        EntityLightningBolt lightning = new EntityLightningBolt(world, loc.getX(), loc.getY(), loc.getZ(), false);
        world.addWeatherEffect(lightning);
        return new CraftLightningStrike(server, lightning);
    }

    public LightningStrike strikeLightningEffect(Location loc) {
        EntityLightningBolt lightning = new EntityLightningBolt(world, loc.getX(), loc.getY(), loc.getZ(), true);
        world.addWeatherEffect(lightning);
        return new CraftLightningStrike(server, lightning);
    }

    public boolean generateTree(Location loc, TreeType type) {
        BlockPos pos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        net.minecraft.world.gen.feature.WorldGenerator gen;
        switch (type) {
        case BIG_TREE:
            gen = new WorldGenBigTree(true);
            break;
        case BIRCH:
            gen = new WorldGenBirchTree(true, false);
            break;
        case REDWOOD:
            gen = new WorldGenTaiga2(true);
            break;
        case TALL_REDWOOD:
            gen = new WorldGenTaiga1();
            break;
        case JUNGLE:
            IBlockState iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
            IBlockState iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenMegaJungle(true, 10, 20, iblockdata1, iblockdata2); // Magic values as in BlockSapling
            break;
        case SMALL_JUNGLE:
            iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, false);
            break;
        case COCOA_TREE:
            iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, true);
            break;
        case JUNGLE_BUSH:
            iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenShrub(iblockdata1, iblockdata2);
            break;
        case RED_MUSHROOM:
            gen = new WorldGenBigMushroom(Blocks.RED_MUSHROOM_BLOCK);
            break;
        case BROWN_MUSHROOM:
            gen = new WorldGenBigMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
            break;
        case SWAMP:
            gen = new WorldGenSwamp();
            break;
        case ACACIA:
            gen = new WorldGenSavannaTree(true);
            break;
        case DARK_OAK:
            gen = new WorldGenCanopyTree(true);
            break;
        case MEGA_REDWOOD:
            gen = new WorldGenMegaPineTree(false, rand.nextBoolean());
            break;
        case TALL_BIRCH:
            gen = new WorldGenBirchTree(true, true);
            break;
        case CHORUS_PLANT:
            BlockChorusFlower.generatePlant(world, pos, rand, 8);
            return true;
        case TREE:
        default:
            gen = new WorldGenTrees(true);
            break;
        }

        return gen.generate(world, rand, pos);
    }

    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        world.captureTreeGeneration = true;
        world.captureBlockSnapshots = true;
        boolean grownTree = generateTree(loc, type);
        world.captureBlockSnapshots = false;
        world.captureTreeGeneration = false;
        if (grownTree) { // Copy block data to delegate
            for (BlockSnapshot blocksnapshot : this.world.capturedBlockSnapshots) {
                BlockPos position = blocksnapshot.getPos();
                int x = position.getX();
                int y = position.getY();
                int z = position.getZ();
                net.minecraft.block.state.IBlockState oldBlock = world.getBlockState(position);
                int typeId = net.minecraft.block.Block.getIdFromBlock(blocksnapshot.getReplacedBlock().getBlock());
                int data = blocksnapshot.getMeta();
                int flag = blocksnapshot.getFlag();;
                delegate.setTypeIdAndData(x, y, z, typeId, data);
                net.minecraft.block.state.IBlockState newBlock = world.getBlockState(position);
                world.markAndNotifyBlock(position, null, oldBlock, newBlock, flag);
            }
            world.capturedBlockSnapshots.clear();
            return true;
        } else {
            world.capturedBlockSnapshots.clear();
            return false;
        }
    }

    public TileEntity getTileEntityAt(final int x, final int y, final int z) {
        return world.getTileEntity(new BlockPos(x, y, z));
    }

    public String getName() {
        return world.worldInfo.getWorldName();
    }

    @Deprecated
    public long getId() {
        return world.worldInfo.getSeed();
    }

    public UUID getUID() {
        return world.getSaveHandler().getUUID();
    }

    @Override
    public String toString() {
        return "CraftWorld{name=" + getName() + '}';
    }

    public long getTime() {
        long time = getFullTime() % 24000;
        if (time < 0) time += 24000;
        return time;
    }

    public void setTime(long time) {
        long margin = (time - getFullTime()) % 24000;
        if (margin < 0) margin += 24000;
        setFullTime(getFullTime() + margin);
    }

    public long getFullTime() {
        return world.getWorldTime();
    }

    public void setFullTime(long time) {
        world.setWorldTime(time);

        // Forces the client to update to the new time immediately
        for (Player p : getPlayers()) {
            CraftPlayer cp = (CraftPlayer) p;
            if (cp.getHandle().connection == null) continue;

            cp.getHandle().connection.sendPacket(new SPacketTimeUpdate(cp.getHandle().world.getTotalWorldTime(), cp.getHandle().getPlayerTime(), cp.getHandle().world.getGameRules().getBoolean("doDaylightCycle")));
        }
    }

    public boolean createExplosion(double x, double y, double z, float power) {
        return createExplosion(x, y, z, power, false, true);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return createExplosion(x, y, z, power, setFire, true);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return !world.newExplosion(null, x, y, z, power, setFire, breakBlocks).wasCanceled;
    }

    public boolean createExplosion(Location loc, float power) {
        return createExplosion(loc, power, false);
    }

    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment env) {
        if (environment != env) {
            environment = env;
            world.provider = net.minecraft.world.WorldProvider.getProviderForDimension(environment.getId());
        }
    }

    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getBlockTypeIdAt(Location location) {
        return getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getHighestBlockYAt(Location location) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    public Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }

    public List<BlockPopulator> getPopulators() {
        return populators;
    }

    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    public Block getHighestBlockAt(Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    public Biome getBiome(int x, int z) {
        return CraftBlock.biomeBaseToBiome(this.world.getBiome(new BlockPos(x, 0, z)));
    }

    public void setBiome(int x, int z, Biome bio) {
        net.minecraft.world.biome.Biome bb = CraftBlock.biomeToBiomeBase(bio);
        if (this.world.isBlockLoaded(new BlockPos(x, 0, z))) {
            net.minecraft.world.chunk.Chunk chunk = this.world.getChunkFromBlockCoords(new BlockPos(x, 0, z));

            if (chunk != null) {
                byte[] biomevals = chunk.getBiomeArray();
                biomevals[((z & 0xF) << 4) | (x & 0xF)] = (byte) net.minecraft.world.biome.Biome.REGISTRY.getIDForObject(bb);

                chunk.markDirty(); // SPIGOT-2890
            }
        }
    }

    public double getTemperature(int x, int z) {
        return this.world.getBiome(new BlockPos(x, 0, z)).getDefaultTemperature();
    }

    public double getHumidity(int x, int z) {
        return this.world.getBiome(new BlockPos(x, 0, z)).getRainfall();
    }

    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();

        for (Object o : world.loadedEntityList) {
            if (o instanceof net.minecraft.entity.Entity) {
                net.minecraft.entity.Entity mcEnt = (net.minecraft.entity.Entity) o;
                Entity bukkitEntity = mcEnt.getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null) {
                    list.add(bukkitEntity);
                }
            }
        }

        return list;
    }

    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<LivingEntity>();

        for (Object o : world.loadedEntityList) {
            if (o instanceof net.minecraft.entity.Entity) {
                net.minecraft.entity.Entity mcEnt = (net.minecraft.entity.Entity) o;
                Entity bukkitEntity = mcEnt.getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null && bukkitEntity instanceof LivingEntity) {
                    list.add((LivingEntity) bukkitEntity);
                }
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return (Collection<T>)getEntitiesByClasses(classes);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
        Collection<T> list = new ArrayList<T>();

        for (Object entity: world.loadedEntityList) {
            if (entity instanceof net.minecraft.entity.Entity) {
                Entity bukkitEntity = ((net.minecraft.entity.Entity) entity).getBukkitEntity();

                if (bukkitEntity == null) {
                    continue;
                }

                Class<?> bukkitClass = bukkitEntity.getClass();

                if (clazz.isAssignableFrom(bukkitClass)) {
                    list.add((T) bukkitEntity);
                }
            }
        }

        return list;
    }

    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        Collection<Entity> list = new ArrayList<Entity>();

        for (Object entity: world.loadedEntityList) {
            if (entity instanceof net.minecraft.entity.Entity) {
                Entity bukkitEntity = ((net.minecraft.entity.Entity) entity).getBukkitEntity();

                if (bukkitEntity == null) {
                    continue;
                }

                Class<?> bukkitClass = bukkitEntity.getClass();

                for (Class<?> clazz : classes) {
                    if (clazz.isAssignableFrom(bukkitClass)) {
                        list.add(bukkitEntity);
                        break;
                    }
                }
            }
        }

        return list;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        if (location == null || !location.getWorld().equals(this)) {
            return Collections.emptyList();
        }

        AxisAlignedBB bb = new AxisAlignedBB(location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z);
        List<net.minecraft.entity.Entity> entityList = getHandle().getEntitiesInAABBexcluding(null, bb, null);
        List<Entity> bukkitEntityList = new ArrayList<Entity>(entityList.size());
        for (Object entity : entityList) {
            bukkitEntityList.add(((net.minecraft.entity.Entity) entity).getBukkitEntity());
        }
        return bukkitEntityList;
    }

    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>(world.playerEntities.size());

        for (EntityPlayer human : world.playerEntities) {
            HumanEntity bukkitEntity = human.getBukkitEntity();

            if ((bukkitEntity != null) && (bukkitEntity instanceof Player)) {
                list.add((Player) bukkitEntity);
            }
        }

        return list;
    }

    public void save() {
        this.server.checkSaveState();
        try {
            boolean oldSave = world.disableLevelSaving;

            world.disableLevelSaving = false;
            world.saveAllChunks(true, null);

            world.disableLevelSaving = oldSave;
        } catch (MinecraftException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isAutoSave() {
        return !world.disableLevelSaving;
    }

    public void setAutoSave(boolean value) {
        world.disableLevelSaving = !value;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.getHandle().worldInfo.setDifficulty(EnumDifficulty.getDifficultyEnum(difficulty.getValue()));
    }

    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    public BlockMetadataStore getBlockMetadata() {
        return blockMetadata;
    }

    public boolean hasStorm() {
        return world.worldInfo.isRaining();
    }

    public void setStorm(boolean hasStorm) {
        world.worldInfo.setRaining(hasStorm);
        setWeatherDuration(0); // Reset weather duration (legacy behaviour)
    }

    public int getWeatherDuration() {
        return world.worldInfo.getRainTime();
    }

    public void setWeatherDuration(int duration) {
        world.worldInfo.setRainTime(duration);
    }

    public boolean isThundering() {
        return world.worldInfo.isThundering();
    }

    public void setThundering(boolean thundering) {
        world.worldInfo.setThundering(thundering);
        setThunderDuration(0); // Reset weather duration (legacy behaviour)
    }

    public int getThunderDuration() {
        return world.worldInfo.getThunderTime();
    }

    public void setThunderDuration(int duration) {
        world.worldInfo.setThunderTime(duration);
    }

    public long getSeed() {
        return world.worldInfo.getSeed();
    }

    public boolean getPVP() {
        return world.pvpMode;
    }

    public void setPVP(boolean pvp) {
        world.pvpMode = pvp;
    }

    public void playEffect(Player player, Effect effect, int data) {
        playEffect(player.getLocation(), effect, data, 0);
    }

    public void playEffect(Location location, Effect effect, int data) {
        playEffect(location, effect, data, 64);
    }

    public <T> void playEffect(Location loc, Effect effect, T data) {
        playEffect(loc, effect, data, 64);
    }

    public <T> void playEffect(Location loc, Effect effect, T data, int radius) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        if (data != null && data.getClass().equals( org.bukkit.material.MaterialData.class )) {
            org.bukkit.material.MaterialData materialData = (org.bukkit.material.MaterialData) data;
            Validate.isTrue( materialData.getItemType().isBlock(), "Material must be block" );
            spigot().playEffect( loc, effect, materialData.getItemType().getId(), materialData.getData(), 0, 0, 0, 1, 1, radius );
        } else {
            int dataValue = data == null ? 0 : CraftEffect.getDataValue( effect, data );
            playEffect( loc, effect, dataValue, radius );
        }
    }

    public void playEffect(Location location, Effect effect, int data, int radius) {
        spigot().playEffect( location, effect, data, 0, 0, 0, 0, 1, 1, radius );
    }

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

    public FallingBlock spawnFallingBlock(Location location, org.bukkit.Material material, byte data) throws IllegalArgumentException {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(material.isBlock(), "Material must be a block");

        EntityFallingBlock entity = new EntityFallingBlock(world, location.getX(), location.getY(), location.getZ(), CraftMagicNumbers.getBlock(material).getStateFromMeta(data));
        entity.ticksExisted = 1;

        world.addEntity(entity, SpawnReason.CUSTOM);
        return (FallingBlock) entity.getBukkitEntity();
    }

    public FallingBlock spawnFallingBlock(Location location, int blockId, byte blockData) throws IllegalArgumentException {
        return spawnFallingBlock(location, org.bukkit.Material.getBlockMaterial(blockId), blockData);
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
            entity = new EntityBoat(world, x, y, z);
            entity.setLocationAndAngles(x, y, z, yaw, pitch);
        } else if (FallingBlock.class.isAssignableFrom(clazz)) {
            entity = new EntityFallingBlock(world, x, y, z, world.getBlockState(new BlockPos(x, y, z)));
        } else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = new EntitySnowball(world, x, y, z);
            } else if (Egg.class.isAssignableFrom(clazz)) {
                entity = new EntityEgg(world, x, y, z);
            } else if (Arrow.class.isAssignableFrom(clazz)) {
                if (TippedArrow.class.isAssignableFrom(clazz)) {
                    entity = new EntityTippedArrow(world);
                    ((EntityTippedArrow) entity).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
                } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
                    entity = new EntitySpectralArrow(world);
                } else {
                    entity = new EntityTippedArrow(world);
                }
                entity.setLocationAndAngles(x, y, z, 0, 0);
            } else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                entity = new EntityExpBottle(world);
                entity.setLocationAndAngles(x, y, z, 0, 0);
            } else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderPearl(world);
                entity.setLocationAndAngles(x, y, z, 0, 0);
            } else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                if (LingeringPotion.class.isAssignableFrom(clazz)) {
                    entity = new EntityPotion(world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
                } else {
                    entity = new EntityPotion(world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
                }
            } else if (Fireball.class.isAssignableFrom(clazz)) {
                if (SmallFireball.class.isAssignableFrom(clazz)) {
                    entity = new EntitySmallFireball(world);
                } else if (WitherSkull.class.isAssignableFrom(clazz)) {
                    entity = new EntityWitherSkull(world);
                } else if (DragonFireball.class.isAssignableFrom(clazz)) {
                    entity = new EntityDragonFireball(world);
                } else {
                    entity = new EntityLargeFireball(world);
                }
                entity.setLocationAndAngles(x, y, z, yaw, pitch);
                Vector direction = location.getDirection().multiply(10);
                ((EntityFireball) entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            } else if (ShulkerBullet.class.isAssignableFrom(clazz)) {
                entity = new EntityShulkerBullet(world);
                entity.setLocationAndAngles(x, y, z, yaw, pitch);
            } else if (LlamaSpit.class.isAssignableFrom(clazz)) {
                entity = new EntityLlamaSpit(world);
                entity.setLocationAndAngles(x, y, z, yaw, pitch);
            }
        } else if (Minecart.class.isAssignableFrom(clazz)) {
            if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartFurnace(world, x, y, z);
            } else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartChest(world, x, y, z);
            } else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartTNT(world, x, y, z);
            } else if (HopperMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartHopper(world, x, y, z);
            } else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartMobSpawner(world, x, y, z);
            } else if (CommandMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartCommandBlock(world, x, y, z);
            } else { // Default to rideable minecart for pre-rideable compatibility
                entity = new EntityMinecartEmpty(world, x, y, z);
            }
        } else if (EnderSignal.class.isAssignableFrom(clazz)) {
            entity = new EntityEnderEye(world, x, y, z);
        } else if (EnderCrystal.class.isAssignableFrom(clazz)) {
            entity = new EntityEnderCrystal(world);
            entity.setLocationAndAngles(x, y, z, 0, 0);
        } else if (LivingEntity.class.isAssignableFrom(clazz)) {
            if (Chicken.class.isAssignableFrom(clazz)) {
                entity = new EntityChicken(world);
            } else if (Cow.class.isAssignableFrom(clazz)) {
                if (MushroomCow.class.isAssignableFrom(clazz)) {
                    entity = new EntityMooshroom(world);
                } else {
                    entity = new EntityCow(world);
                }
            } else if (Golem.class.isAssignableFrom(clazz)) {
                if (Snowman.class.isAssignableFrom(clazz)) {
                    entity = new EntitySnowman(world);
                } else if (IronGolem.class.isAssignableFrom(clazz)) {
                    entity = new EntityIronGolem(world);
                } else if (Shulker.class.isAssignableFrom(clazz)) {
                    entity = new EntityShulker(world);
                }
            } else if (Creeper.class.isAssignableFrom(clazz)) {
                entity = new EntityCreeper(world);
            } else if (Ghast.class.isAssignableFrom(clazz)) {
                entity = new EntityGhast(world);
            } else if (Pig.class.isAssignableFrom(clazz)) {
                entity = new EntityPig(world);
            } else if (Player.class.isAssignableFrom(clazz)) {
                // need a net server handler for this one
            } else if (Sheep.class.isAssignableFrom(clazz)) {
                entity = new EntitySheep(world);
            } else if (AbstractHorse.class.isAssignableFrom(clazz)) {
                if (ChestedHorse.class.isAssignableFrom(clazz)) {
                    if (Donkey.class.isAssignableFrom(clazz)) {
                        entity = new EntityDonkey(world);
                    } else if (Mule.class.isAssignableFrom(clazz)) {
                        entity = new EntityMule(world);
                    } else if (Llama.class.isAssignableFrom(clazz)) {
                        entity = new EntityLlama(world);
                    }
                } else if (SkeletonHorse.class.isAssignableFrom(clazz)) {
                    entity = new EntitySkeletonHorse(world);
                } else if (ZombieHorse.class.isAssignableFrom(clazz)) {
                    entity = new EntityZombieHorse(world);
                } else {
                    entity = new EntityHorse(world);
                }
            } else if (Skeleton.class.isAssignableFrom(clazz)) {
                if (Stray.class.isAssignableFrom(clazz)){
                    entity = new EntityStray(world);
                } else if (WitherSkeleton.class.isAssignableFrom(clazz)) {
                    entity = new EntityWitherSkeleton(world);
                } else {
                    entity = new EntitySkeleton(world);
                }
            } else if (Slime.class.isAssignableFrom(clazz)) {
                if (MagmaCube.class.isAssignableFrom(clazz)) {
                    entity = new EntityMagmaCube(world);
                } else {
                    entity = new EntitySlime(world);
                }
            } else if (Spider.class.isAssignableFrom(clazz)) {
                if (CaveSpider.class.isAssignableFrom(clazz)) {
                    entity = new EntityCaveSpider(world);
                } else {
                    entity = new EntitySpider(world);
                }
            } else if (Squid.class.isAssignableFrom(clazz)) {
                entity = new EntitySquid(world);
            } else if (Tameable.class.isAssignableFrom(clazz)) {
                if (Wolf.class.isAssignableFrom(clazz)) {
                    entity = new EntityWolf(world);
                } else if (Ocelot.class.isAssignableFrom(clazz)) {
                    entity = new EntityOcelot(world);
                } else if (Parrot.class.isAssignableFrom(clazz)) {
                    entity = new EntityParrot(world);
                }
            } else if (PigZombie.class.isAssignableFrom(clazz)) {
                entity = new EntityPigZombie(world);
            } else if (Zombie.class.isAssignableFrom(clazz)) {
                if (Husk.class.isAssignableFrom(clazz)) {
                    entity = new EntityHusk(world);
                } else if (ZombieVillager.class.isAssignableFrom(clazz)) {
                    entity = new EntityZombieVillager(world);
                } else {
                    entity = new EntityZombie(world);
                }
            } else if (Giant.class.isAssignableFrom(clazz)) {
                entity = new EntityGiantZombie(world);
            } else if (Silverfish.class.isAssignableFrom(clazz)) {
                entity = new EntitySilverfish(world);
            } else if (Enderman.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderman(world);
            } else if (Blaze.class.isAssignableFrom(clazz)) {
                entity = new EntityBlaze(world);
            } else if (Villager.class.isAssignableFrom(clazz)) {
                entity = new EntityVillager(world);
            } else if (Witch.class.isAssignableFrom(clazz)) {
                entity = new EntityWitch(world);
            } else if (Wither.class.isAssignableFrom(clazz)) {
                entity = new EntityWither(world);
            } else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                if (EnderDragon.class.isAssignableFrom(clazz)) {
                    entity = new EntityDragon(world);
                }
            } else if (Ambient.class.isAssignableFrom(clazz)) {
                if (Bat.class.isAssignableFrom(clazz)) {
                    entity = new EntityBat(world);
                }
            } else if (Rabbit.class.isAssignableFrom(clazz)) {
                entity = new EntityRabbit(world);
            } else if (Endermite.class.isAssignableFrom(clazz)) {
                entity = new EntityEndermite(world);
            } else if (Guardian.class.isAssignableFrom(clazz)) {
                if (ElderGuardian.class.isAssignableFrom(clazz)){
                    entity = new EntityElderGuardian(world);
                } else {
                    entity = new EntityGuardian(world);
                }
            } else if (ArmorStand.class.isAssignableFrom(clazz)) {
                entity = new EntityArmorStand(world, x, y, z);
            } else if (PolarBear.class.isAssignableFrom(clazz)) {
                entity = new EntityPolarBear(world);
            } else if (Vex.class.isAssignableFrom(clazz)) {
                entity = new EntityVex(world);
            } else if (Illager.class.isAssignableFrom(clazz)) {
                if (Spellcaster.class.isAssignableFrom(clazz)) {
                    if (Evoker.class.isAssignableFrom(clazz)) {
                        entity = new EntityEvoker(world);
                    } else if (Illusioner.class.isAssignableFrom(clazz)) {
                        entity = new EntityIllusionIllager(world);
                    }
                } else if (Vindicator.class.isAssignableFrom(clazz)) {
                    entity = new EntityVindicator(world);
                }
            }

            if (entity != null) {
                entity.setPositionAndRotation(x, y, z, yaw, pitch);
                entity.setRotationYawHead(yaw); // SPIGOT-3587
            }
        } else if (Hanging.class.isAssignableFrom(clazz)) {
            Block block = getBlockAt(location);
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

            BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
            final BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
            for (BlockFace dir : faces) {
                net.minecraft.block.Block nmsBlock = CraftMagicNumbers.getBlock(block.getRelative(dir));
                if (nmsBlock.getDefaultState().getMaterial().isSolid() || BlockRedstoneDiode.isDiode(nmsBlock.getDefaultState())) {
                    boolean taken = false;
                    AxisAlignedBB bb = EntityHanging.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height);
                    List<net.minecraft.entity.Entity> list = (List<net.minecraft.entity.Entity>) world.getEntitiesWithinAABBExcludingEntity(null, bb);
                    for (Iterator<net.minecraft.entity.Entity> it = list.iterator(); !taken && it.hasNext();) {
                        net.minecraft.entity.Entity e = it.next();
                        if (e instanceof EntityHanging) {
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
                entity = new EntityLeashKnot(world, new BlockPos((int) x, (int) y, (int) z));
                entity.forceSpawn = true;
            } else {
                // No valid face found
                Preconditions.checkArgument(face != BlockFace.SELF, "Cannot spawn hanging entity for %s at %s (no free face)", clazz.getName(), location);

                EnumFacing dir = CraftBlock.blockFaceToNotch(face).getOpposite();
                if (Painting.class.isAssignableFrom(clazz)) {
                    entity = new EntityPainting(world, new BlockPos((int) x, (int) y, (int) z), dir);
                } else if (ItemFrame.class.isAssignableFrom(clazz)) {
                    entity = new EntityItemFrame(world, new BlockPos((int) x, (int) y, (int) z), dir);
                }
            }

            if (entity != null && !((EntityHanging) entity).onValidSurface()) {
                throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
            }
        } else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = new EntityTNTPrimed(world, x, y, z, null);
        } else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = new EntityXPOrb(world, x, y, z, 0);
        } else if (Weather.class.isAssignableFrom(clazz)) {
            // not sure what this can do
            if (LightningStrike.class.isAssignableFrom(clazz)) {
                entity = new EntityLightningBolt(world, x, y, z, false);
                // what is this, I don't even
            }
        } else if (Firework.class.isAssignableFrom(clazz)) {
            entity = new EntityFireworkRocket(world, x, y, z, net.minecraft.item.ItemStack.EMPTY);
        } else if (AreaEffectCloud.class.isAssignableFrom(clazz)) {
            entity = new EntityAreaEffectCloud(world, x, y, z);
        } else if (EvokerFangs.class.isAssignableFrom(clazz)) {
            entity = new EntityEvokerFangs(world, x, y, z, (float) Math.toRadians(yaw), 0, null);
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

        if (entity instanceof EntityLiving) {
            ((EntityLiving) entity).onInitialSpawn(getHandle().getDifficultyForLocation(new BlockPos(entity)), null);
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

    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }

    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        world.setAllowedSpawnTypes(allowMonsters, allowAnimals);
    }

    public boolean getAllowAnimals() {
        return world.spawnPeacefulMobs;
    }

    public boolean getAllowMonsters() {
        return world.spawnHostileMobs;
    }

    public int getMaxHeight() {
        return world.getHeight();
    }

    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    public boolean getKeepSpawnInMemory() {
        return (world.dimension == 0 || CatServer.keepSpawnInMemory) && world.keepSpawnInMemory; // CatServer
    }

    public void setKeepSpawnInMemory(boolean keepLoaded) {
        world.keepSpawnInMemory = keepLoaded;
        // Grab the worlds spawn chunk
        BlockPos chunkcoordinates = this.world.getSpawnPoint();
        int chunkCoordX = chunkcoordinates.getX() >> 4;
        int chunkCoordZ = chunkcoordinates.getZ() >> 4;
        // Cycle through the 25x25 Chunks around it to load/unload the chunks.
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                if (keepLoaded) {
                    loadChunk(chunkCoordX + x, chunkCoordZ + z);
                } else {
                    if (isChunkLoaded(chunkCoordX + x, chunkCoordZ + z)) {
                        unloadChunk(chunkCoordX + x, chunkCoordZ + z);
                    }
                }
            }
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

    public File getWorldFolder() {
        return world.getSaveHandler().getWorldDirectory();
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);

        for (Player player : getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    public org.bukkit.WorldType getWorldType() {
        return org.bukkit.WorldType.getByName(world.getWorldInfo().getTerrainType().getName());
    }

    public boolean canGenerateStructures() {
        return world.getWorldInfo().isMapFeaturesEnabled();
    }

    public long getTicksPerAnimalSpawns() {
        return world.ticksPerAnimalSpawns;
    }

    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        world.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
    }

    public long getTicksPerMonsterSpawns() {
        return world.ticksPerMonsterSpawns;
    }

    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        world.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getWorldMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return server.getWorldMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public int getMonsterSpawnLimit() {
        if (monsterSpawn < 0) {
            return server.getMonsterSpawnLimit();
        }

        return monsterSpawn;
    }

    public void setMonsterSpawnLimit(int limit) {
        monsterSpawn = limit;
    }

    public int getAnimalSpawnLimit() {
        if (animalSpawn < 0) {
            return server.getAnimalSpawnLimit();
        }

        return animalSpawn;
    }

    public void setAnimalSpawnLimit(int limit) {
        animalSpawn = limit;
    }

    public int getWaterAnimalSpawnLimit() {
        if (waterAnimalSpawn < 0) {
            return server.getWaterAnimalSpawnLimit();
        }

        return waterAnimalSpawn;
    }

    public void setWaterAnimalSpawnLimit(int limit) {
        waterAnimalSpawn = limit;
    }

    public int getAmbientSpawnLimit() {
        if (ambientSpawn < 0) {
            return server.getAmbientSpawnLimit();
        }

        return ambientSpawn;
    }

    public void setAmbientSpawnLimit(int limit) {
        ambientSpawn = limit;
    }

    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    public void playSound(Location loc, String sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        getHandle().playSound(null, x, y, z, CraftSound.getSoundEffect(CraftSound.getSound(sound)), SoundCategory.valueOf(category.name()), volume, pitch); // PAIL: rename
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        SPacketCustomSound packet = new SPacketCustomSound(sound, SoundCategory.valueOf(category.name()), x, y, z, volume, pitch);
        world.getMinecraftServer().getPlayerList().sendToAllNearExcept(null, x, y, z, volume > 1.0F ? 16.0F * volume : 16.0D, this.world.dimension, packet);
    }

    public String getGameRuleValue(String rule) {
        return getHandle().getGameRules().getString(rule);
    }

    public boolean setGameRuleValue(String rule, String value) {
        // No null values allowed
        if (rule == null || value == null) return false;

        if (!isGameRule(rule)) return false;

        getHandle().getGameRules().setOrCreateGameRule(rule, value);
        return true;
    }

    public String[] getGameRules() {
        return getHandle().getGameRules().getRules();
    }

    public boolean isGameRule(String rule) {
        return getHandle().getGameRules().hasRule(rule);
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
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        getHandle().sendParticles(
                null, // Sender
                CraftParticle.toNMS(particle), // Particle
                true, // Extended range
                x, y, z, // Position
                count,  // Count
                offsetX, offsetY, offsetZ, // Random offset
                extra, // Speed?
                CraftParticle.toData(particle, data)

        );

    }

    public void processChunkGC() {
        chunkGCTickCount++;

        if (chunkLoadCount >= server.chunkGCLoadThresh && server.chunkGCLoadThresh > 0) {
            chunkLoadCount = 0;
        } else if (chunkGCTickCount >= server.chunkGCPeriod && server.chunkGCPeriod > 0) {
            chunkGCTickCount = 0;
        } else {
            return;
        }

        ChunkProviderServer cps = world.getChunkProvider();
        for (net.minecraft.world.chunk.Chunk chunk : cps.id2ChunkMap.values()) {
            // If in use, skip it
            if (isChunkInUse(chunk.x, chunk.z)) {
                continue;
            }

            // Already unloading?
            if (cps.droppedChunksSet.contains(ChunkPos.asLong(chunk.x, chunk.z))) {
                continue;
            }

            // Add unload request
            cps.queueUnload(chunk);
        }
    }

    // Spigot start
    private final Spigot spigot = new Spigot()
    {
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
            for ( Player player : getPlayers() )
            {
                if ( ( (CraftPlayer) player ).getHandle().connection == null )
                {
                    continue;
                }
                if ( !location.getWorld().equals( player.getWorld() ) )
                {
                    continue;
                }
                distance = (int) player.getLocation().distanceSquared( location );
                if ( distance <= radius )
                {
                    ( (CraftPlayer) player ).getHandle().connection.sendPacket( packet );
                }
            }
        }

        @Override
        public void playEffect( Location location, Effect effect )
        {
            CraftWorld.this.playEffect( location, effect, 0 );
        }

        @Override
        public LightningStrike strikeLightning(Location loc, boolean isSilent)
        {
            EntityLightningBolt lightning = new EntityLightningBolt( world, loc.getX(), loc.getY(), loc.getZ(), false, isSilent);
            world.addWeatherEffect( lightning );
            return new CraftLightningStrike( server, lightning );
        }

        @Override
        public LightningStrike strikeLightningEffect(Location loc, boolean isSilent)
        {
            EntityLightningBolt lightning = new EntityLightningBolt( world, loc.getX(), loc.getY(), loc.getZ(), true, isSilent);
            world.addWeatherEffect( lightning );
            return new CraftLightningStrike( server, lightning );
        }
    };

    public Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
