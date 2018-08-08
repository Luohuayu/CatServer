// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.bukkit.*;
import org.bukkit.metadata.MetadataStoreBase;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import org.bukkit.metadata.MetadataValue;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.plugin.Plugin;
import net.minecraft.world.storage.SaveHandler;
import java.io.File;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.EntityLiving;
import com.google.common.base.Preconditions;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.EntityAreaEffectCloud;
import org.bukkit.entity.AreaEffectCloud;
import net.minecraft.entity.item.EntityFireworkRocket;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Weather;
import net.minecraft.entity.item.EntityXPOrb;
import org.bukkit.entity.ExperienceOrb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.bukkit.entity.TNTPrimed;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import org.bukkit.entity.Painting;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityHanging;
import net.minecraft.block.BlockRedstoneDiode;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.ItemFrame;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import net.minecraft.entity.monster.EntityPolarBear;
import org.bukkit.entity.PolarBear;
import net.minecraft.entity.item.EntityArmorStand;
import org.bukkit.entity.ArmorStand;
import net.minecraft.entity.monster.EntityGuardian;
import org.bukkit.entity.Guardian;
import net.minecraft.entity.monster.EntityEndermite;
import org.bukkit.entity.Endermite;
import net.minecraft.entity.passive.EntityRabbit;
import org.bukkit.entity.Rabbit;
import net.minecraft.entity.passive.EntityBat;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Ambient;
import net.minecraft.entity.boss.EntityDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.ComplexLivingEntity;
import net.minecraft.entity.boss.EntityWither;
import org.bukkit.entity.Wither;
import net.minecraft.entity.monster.EntityWitch;
import org.bukkit.entity.Witch;
import net.minecraft.entity.passive.EntityVillager;
import org.bukkit.entity.Villager;
import net.minecraft.entity.monster.EntityBlaze;
import org.bukkit.entity.Blaze;
import net.minecraft.entity.monster.EntityEnderman;
import org.bukkit.entity.Enderman;
import net.minecraft.entity.monster.EntitySilverfish;
import org.bukkit.entity.Silverfish;
import net.minecraft.entity.monster.EntityGiantZombie;
import org.bukkit.entity.Giant;
import net.minecraft.entity.monster.EntityZombie;
import org.bukkit.entity.Zombie;
import net.minecraft.entity.monster.EntityPigZombie;
import org.bukkit.entity.PigZombie;
import net.minecraft.entity.passive.EntityOcelot;
import org.bukkit.entity.Ocelot;
import net.minecraft.entity.passive.EntityWolf;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Tameable;
import net.minecraft.entity.passive.EntitySquid;
import org.bukkit.entity.Squid;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityCaveSpider;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Spider;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityMagmaCube;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Slime;
import net.minecraft.entity.monster.EntitySkeleton;
import org.bukkit.entity.Skeleton;
import net.minecraft.entity.passive.EntityHorse;
import org.bukkit.entity.Horse;
import net.minecraft.entity.passive.EntitySheep;
import org.bukkit.entity.Sheep;
import net.minecraft.entity.passive.EntityPig;
import org.bukkit.entity.Pig;
import net.minecraft.entity.monster.EntityGhast;
import org.bukkit.entity.Ghast;
import net.minecraft.entity.monster.EntityCreeper;
import org.bukkit.entity.Creeper;
import net.minecraft.entity.monster.EntityShulker;
import org.bukkit.entity.Shulker;
import net.minecraft.entity.monster.EntityIronGolem;
import org.bukkit.entity.IronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Golem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Cow;
import net.minecraft.entity.passive.EntityChicken;
import org.bukkit.entity.Chicken;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.bukkit.entity.EnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import org.bukkit.entity.EnderSignal;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import org.bukkit.entity.minecart.CommandMinecart;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import org.bukkit.entity.minecart.SpawnerMinecart;
import net.minecraft.entity.item.EntityMinecartHopper;
import org.bukkit.entity.minecart.HopperMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import org.bukkit.entity.minecart.StorageMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.Minecart;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import org.bukkit.entity.ShulkerBullet;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityDragonFireball;
import org.bukkit.entity.DragonFireball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import org.bukkit.entity.WitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Fireball;
import net.minecraft.entity.projectile.EntityPotion;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.ThrownPotion;
import net.minecraft.entity.item.EntityEnderPearl;
import org.bukkit.entity.EnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import org.bukkit.entity.ThrownExpBottle;
import net.minecraft.entity.projectile.EntityEgg;
import org.bukkit.entity.Egg;
import net.minecraft.entity.projectile.EntitySnowball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Projectile;
import net.minecraft.entity.item.EntityBoat;
import org.bukkit.entity.Boat;
import net.minecraft.entity.item.EntityFallingBlock;
import org.bukkit.entity.FallingBlock;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.util.IProgressUpdate;
import org.bukkit.entity.HumanEntity;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.base.Predicate;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Collections;
import java.util.Collection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.block.Biome;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.UUID;
import net.minecraft.tileentity.TileEntity;
import java.util.Iterator;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.block.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.block.BlockChorusFlower;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import org.bukkit.craftbukkit.entity.CraftLightningStrike;
import net.minecraft.entity.effect.EntityLightningBolt;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import net.minecraft.entity.projectile.EntityTippedArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.util.Vector;
import org.bukkit.craftbukkit.entity.CraftItem;
import net.minecraft.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import net.minecraft.entity.item.EntityItem;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.util.math.ChunkPos;
import org.bukkit.event.Event;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import net.minecraft.util.math.BlockPos;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.craftbukkit.metadata.BlockMetadataStore;
import org.bukkit.generator.BlockPopulator;
import java.util.List;
import org.bukkit.generator.ChunkGenerator;
import net.minecraft.world.WorldServer;

public class CraftWorld implements World
{
    public static final int CUSTOM_DIMENSION_OFFSET = 10;
    private final WorldServer world;
    private WorldBorder worldBorder;
    private Environment environment;
    private final CraftServer server;
    public ChunkGenerator generator;
    private final List<BlockPopulator> populators;
    private final BlockMetadataStore blockMetadata;
    private int monsterSpawn;
    private int animalSpawn;
    private int waterAnimalSpawn;
    private int ambientSpawn;
    private int chunkLoadCount;
    private int chunkGCTickCount;
    private static final Random rand;
    
    static {
        rand = new Random();
    }
    
    public CraftWorld(final WorldServer world, final ChunkGenerator gen, final Environment env) {
        this.server = (CraftServer)Bukkit.getServer();
        this.populators = new ArrayList<BlockPopulator>();
        this.blockMetadata = new BlockMetadataStore(this);
        this.monsterSpawn = -1;
        this.animalSpawn = -1;
        this.waterAnimalSpawn = -1;
        this.ambientSpawn = -1;
        this.chunkLoadCount = 0;
        this.world = world;
        this.generator = gen;
        this.environment = env;
        if (this.server.chunkGCPeriod > 0) {
            this.chunkGCTickCount = CraftWorld.rand.nextInt(this.server.chunkGCPeriod);
        }
    }
    
    @Override
    public Block getBlockAt(final int x, final int y, final int z) {
        return this.getChunkAt(x >> 4, z >> 4).getBlock(x & 0xF, y, z & 0xF);
    }
    
    @Override
    public int getBlockTypeIdAt(final int x, final int y, final int z) {
        return CraftMagicNumbers.getId(this.world.getBlockState(new BlockPos(x, y, z)).getBlock());
    }
    
    @Override
    public int getHighestBlockYAt(final int x, final int z) {
        if (!this.isChunkLoaded(x >> 4, z >> 4)) {
            this.loadChunk(x >> 4, z >> 4);
        }
        return this.world.getHeight(new BlockPos(x, 0, z)).getY();
    }
    
    @Override
    public Location getSpawnLocation() {
        final BlockPos spawn = this.world.getSpawnPoint();
        return new Location(this, spawn.getX(), spawn.getY(), spawn.getZ());
    }
    
    @Override
    public boolean setSpawnLocation(final int x, final int y, final int z) {
        try {
            final Location previousLocation = this.getSpawnLocation();
            this.world.worldInfo.setSpawn(new BlockPos(x, y, z));
            final SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            this.server.getPluginManager().callEvent(event);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public Chunk getChunkAt(final int x, final int z) {
        return this.world.getChunkProvider().provideChunk(x, z).bukkitChunk;
    }
    
    @Override
    public Chunk getChunkAt(final Block block) {
        return this.getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }
    
    @Override
    public boolean isChunkLoaded(final int x, final int z) {
        return this.world.getChunkProvider().chunkExists(x, z);
    }
    
    @Override
    public Chunk[] getLoadedChunks() {
        final Object[] chunks = this.world.getChunkProvider().id2ChunkMap.values().toArray();
        final Chunk[] craftChunks = new CraftChunk[chunks.length];
        for (int i = 0; i < chunks.length; ++i) {
            final net.minecraft.world.chunk.Chunk chunk = (net.minecraft.world.chunk.Chunk)chunks[i];
            craftChunks[i] = chunk.bukkitChunk;
        }
        return craftChunks;
    }
    
    @Override
    public void loadChunk(final int x, final int z) {
        this.loadChunk(x, z, true);
    }
    
    @Override
    public boolean unloadChunk(final Chunk chunk) {
        return this.unloadChunk(chunk.getX(), chunk.getZ());
    }
    
    @Override
    public boolean unloadChunk(final int x, final int z) {
        return this.unloadChunk(x, z, true);
    }
    
    @Override
    public boolean unloadChunk(final int x, final int z, final boolean save) {
        return this.unloadChunk(x, z, save, false);
    }
    
    @Override
    public boolean unloadChunkRequest(final int x, final int z) {
        return this.unloadChunkRequest(x, z, true);
    }
    
    @Override
    public boolean unloadChunkRequest(final int x, final int z, final boolean safe) {
        if (safe && this.isChunkInUse(x, z)) {
            return false;
        }
        final net.minecraft.world.chunk.Chunk chunk = this.world.getChunkProvider().getLoadedChunk(x, z);
        if (chunk != null) {
            this.world.getChunkProvider().unload(chunk);
        }
        return true;
    }
    
    @Override
    public boolean unloadChunk(final int x, final int z, final boolean save, final boolean safe) {
        return !this.isChunkInUse(x, z) && this.unloadChunk0(x, z, save);
    }
    
    private boolean unloadChunk0(final int x, final int z, final boolean save) {
        final net.minecraft.world.chunk.Chunk chunk = this.world.getChunkProvider().getLoadedChunk(x, z);
        if(chunk == null) {
        	return true;
        }
        this.world.getChunkProvider().unload(chunk);
        if(chunk.unloaded) {
        	return true;
        }
        return false;
    }
    
    @Override
    public boolean regenerateChunk(final int x, final int z) {
        if (!this.unloadChunk0(x, z, false)) {
            return false;
        }
        final long chunkKey = ChunkPos.asLong(x, z);
        this.world.getChunkProvider().droppedChunksSet.remove(chunkKey);
        net.minecraft.world.chunk.Chunk chunk = null;
        chunk = this.world.getChunkProvider().chunkGenerator.provideChunk(x, z);
        final PlayerChunkMapEntry playerChunk = this.world.getPlayerChunkMap().getEntry(x, z);
        if (playerChunk != null) {
            playerChunk.chunk = chunk;
        }
        if (chunk != null) {
            this.world.getChunkProvider().id2ChunkMap.put(chunkKey, /*(Object)*/chunk);
            chunk.onChunkLoad();
            chunk.loadNearby(this.world.getChunkProvider(), this.world.getChunkProvider().chunkGenerator, true);
            this.refreshChunk(x, z);
        }
        return chunk != null;
    }
    
    @Override
    public boolean refreshChunk(final int x, final int z) {
        if (!this.isChunkLoaded(x, z)) {
            return false;
        }
        final int px = x << 4;
        final int pz = z << 4;
        final int height = this.getMaxHeight() / 16;
        for (int idx = 0; idx < 64; ++idx) {
            this.world.notifyBlockUpdate(new BlockPos(px + idx / height, idx % height * 16, pz), Blocks.AIR.getDefaultState(), Blocks.STONE.getDefaultState(), 3);
        }
        this.world.notifyBlockUpdate(new BlockPos(px + 15, height * 16 - 1, pz + 15), Blocks.AIR.getDefaultState(), Blocks.STONE.getDefaultState(), 3);
        return true;
    }
    
    @Override
    public boolean isChunkInUse(final int x, final int z) {
        return this.world.getPlayerChunkMap().isChunkInUse(x, z);
    }
    
    @Override
    public boolean loadChunk(final int x, final int z, final boolean generate) {
        ++this.chunkLoadCount;
        if (generate) {
            return this.world.getChunkProvider().provideChunk(x, z) != null;
        }
        return this.world.getChunkProvider().loadChunk(x, z) != null;
    }
    
    @Override
    public boolean isChunkLoaded(final Chunk chunk) {
        return this.isChunkLoaded(chunk.getX(), chunk.getZ());
    }
    
    @Override
    public void loadChunk(final Chunk chunk) {
        this.loadChunk(chunk.getX(), chunk.getZ());
        ((CraftChunk)this.getChunkAt(chunk.getX(), chunk.getZ())).getHandle().bukkitChunk = chunk;
    }
    
    public WorldServer getHandle() {
        return this.world;
    }
    
    @Override
    public Item dropItem(final Location loc, final ItemStack item) {
        Validate.notNull((Object)item, "Cannot drop a Null item.");
        Validate.isTrue(item.getTypeId() != 0, "Cannot drop AIR.");
        final EntityItem entity = new EntityItem(this.world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        entity.delayBeforeCanPickup = 10;
        this.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return new CraftItem(this.world.getServer(), entity);
    }
    
    private static void randomLocationWithinBlock(final Location loc, final double xs, final double ys, final double zs) {
        final double prevX = loc.getX();
        final double prevY = loc.getY();
        final double prevZ = loc.getZ();
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
    
    @Override
    public Item dropItemNaturally(Location loc, final ItemStack item) {
        final double xs = this.world.rand.nextFloat() * 0.7f - 0.35;
        final double ys = this.world.rand.nextFloat() * 0.7f - 0.35;
        final double zs = this.world.rand.nextFloat() * 0.7f - 0.35;
        loc = loc.clone();
        randomLocationWithinBlock(loc, xs, ys, zs);
        return this.dropItem(loc, item);
    }
    
    @Override
    public Arrow spawnArrow(final Location loc, final Vector velocity, final float speed, final float spread) {
        return this.spawnArrow(loc, velocity, speed, spread, Arrow.class);
    }
    
    @Override
    public <T extends Arrow> T spawnArrow(final Location loc, final Vector velocity, final float speed, final float spread, final Class<T> clazz) {
        Validate.notNull((Object)loc, "Can not spawn arrow with a null location");
        Validate.notNull((Object)velocity, "Can not spawn arrow with a null velocity");
        Validate.notNull((Object)clazz, "Can not spawn an arrow with no class");
        EntityArrow arrow;
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = new EntityTippedArrow(this.world);
            ((EntityTippedArrow)arrow).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
        }
        else if (SpectralArrow.class.isAssignableFrom(clazz)) {
            arrow = new EntitySpectralArrow(this.world);
        }
        else {
            arrow = new EntityTippedArrow(this.world);
        }
        arrow.setLocationAndAngles(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.setThrowableHeading(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        this.world.spawnEntityInWorld(arrow);
        return (T)arrow.getBukkitEntity();
    }
    
    @Override
    public org.bukkit.entity.Entity spawnEntity(final Location loc, final EntityType entityType) {
        if (EntityRegistry.entityClassMap.get(entityType.getName()) != null)
        {
            net.minecraft.entity.Entity entity = null;
            entity = getEntity(EntityRegistry.entityClassMap.get(entityType.getName()), world);
            if (entity != null)
            {
                entity.setLocationAndAngles(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
                world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                return entity.getBukkitEntity();
            }
        }
        return this.spawn(loc, entityType.getEntityClass());
    }
    // Cauldron start
    public net.minecraft.entity.Entity getEntity(Class<? extends net.minecraft.entity.Entity> clazz, net.minecraft.world.World world)
    {
        net.minecraft.entity.EntityLiving entity = null;
        try
        {
            entity = (net.minecraft.entity.EntityLiving) clazz.getConstructor(new Class[] { net.minecraft.world.World.class }).newInstance(new Object[] { world });
        }
        catch (Throwable throwable)
        {
        }
        return entity;
    }
    // Cauldron end
    
    @Override
    public LightningStrike strikeLightning(final Location loc) {
        final EntityLightningBolt lightning = new EntityLightningBolt(this.world, loc.getX(), loc.getY(), loc.getZ(), false);
        this.world.addWeatherEffect(lightning);
        return new CraftLightningStrike(this.server, lightning);
    }
    
    @Override
    public LightningStrike strikeLightningEffect(final Location loc) {
        final EntityLightningBolt lightning = new EntityLightningBolt(this.world, loc.getX(), loc.getY(), loc.getZ(), true);
        this.world.addWeatherEffect(lightning);
        return new CraftLightningStrike(this.server, lightning);
    }
    
    @Override
    public boolean generateTree(final Location loc, final TreeType type) {
        final BlockPos pos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        WorldGenerator gen = null;
        switch (type) {
            case BIG_TREE: {
                gen = new WorldGenBigTree(true);
                break;
            }
            case BIRCH: {
                gen = new WorldGenBirchTree(true, false);
                break;
            }
            case REDWOOD: {
                gen = new WorldGenTaiga2(true);
                break;
            }
            case TALL_REDWOOD: {
                gen = new WorldGenTaiga1();
                break;
            }
            case JUNGLE: {
                final IBlockState iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                final IBlockState iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(/*(IProperty<Comparable>)*/BlockLeaves.CHECK_DECAY, false);
                gen = new WorldGenMegaJungle(true, 10, 20, iblockdata1, iblockdata2);
                break;
            }
            case SMALL_JUNGLE: {
                final IBlockState iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                final IBlockState iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(/*(IProperty<Comparable>)*/BlockLeaves.CHECK_DECAY, false);
                gen = new WorldGenTrees(true, 4 + CraftWorld.rand.nextInt(7), iblockdata1, iblockdata2, false);
                break;
            }
            case COCOA_TREE: {
                final IBlockState iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                final IBlockState iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(/*(IProperty<Comparable>)*/BlockLeaves.CHECK_DECAY, false);
                gen = new WorldGenTrees(true, 4 + CraftWorld.rand.nextInt(7), iblockdata1, iblockdata2, true);
                break;
            }
            case JUNGLE_BUSH: {
                final IBlockState iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                final IBlockState iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(/*(IProperty<Comparable>)*/BlockLeaves.CHECK_DECAY, false);
                gen = new WorldGenShrub(iblockdata1, iblockdata2);
                break;
            }
            case RED_MUSHROOM: {
                gen = new WorldGenBigMushroom(Blocks.RED_MUSHROOM_BLOCK);
                break;
            }
            case BROWN_MUSHROOM: {
                gen = new WorldGenBigMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
                break;
            }
            case SWAMP: {
                gen = new WorldGenSwamp();
                break;
            }
            case ACACIA: {
                gen = new WorldGenSavannaTree(true);
                break;
            }
            case DARK_OAK: {
                gen = new WorldGenCanopyTree(true);
                break;
            }
            case MEGA_REDWOOD: {
                gen = new WorldGenMegaPineTree(false, CraftWorld.rand.nextBoolean());
                break;
            }
            case TALL_BIRCH: {
                gen = new WorldGenBirchTree(true, true);
                break;
            }
            case CHORUS_PLANT: {
                BlockChorusFlower.generatePlant(this.world, pos, CraftWorld.rand, 8);
                return true;
            }
            default: {
                gen = new WorldGenTrees(true);
                break;
            }
        }
        return gen.generate(this.world, CraftWorld.rand, pos);
    }
    
    @Override
    public boolean generateTree(final Location loc, final TreeType type, final BlockChangeDelegate delegate) {
        this.world.captureTreeGeneration = true;
        this.world.captureBlockSnapshots = true;
        final boolean grownTree = this.generateTree(loc, type);
        this.world.captureBlockSnapshots = false;
        this.world.captureTreeGeneration = false;
        if (grownTree) {
            for (BlockSnapshot blocksnapshot : this.world.capturedBlockSnapshots) {
                final BlockPos position = blocksnapshot.getPos();
                final int x = position.getX();
                final int y = position.getY();
                final int z = position.getZ();
                final IBlockState oldBlock = this.world.getBlockState(position);
                final int typeId = net.minecraft.block.Block.getIdFromBlock(blocksnapshot.getReplacedBlock().getBlock());
                final int data = blocksnapshot.getMeta();
                final int flag = blocksnapshot.getFlag();
                delegate.setTypeIdAndData(x, y, z, typeId, data);
                final IBlockState newBlock = this.world.getBlockState(position);
                this.world.markAndNotifyBlock(position, null, oldBlock, newBlock, flag);
            }
            this.world.capturedBlockSnapshots.clear();
            return true;
        }
        this.world.capturedBlockSnapshots.clear();
        return false;
    }
    
    public TileEntity getTileEntityAt(final int x, final int y, final int z) {
        return this.world.getTileEntity(new BlockPos(x, y, z));
    }
    
    @Override
    public String getName() {
        return this.world.worldInfo.getWorldName();
    }
    
    @Deprecated
    public long getId() {
        return this.world.worldInfo.getSeed();
    }
    
    @Override
    public UUID getUID() {
        return this.world.getSaveHandler().getUUID();
    }
    
    @Override
    public String toString() {
        return "CraftWorld{name=" + this.getName() + '}';
    }
    
    @Override
    public long getTime() {
        long time = this.getFullTime() % 24000L;
        if (time < 0L) {
            time += 24000L;
        }
        return time;
    }
    
    @Override
    public void setTime(final long time) {
        long margin = (time - this.getFullTime()) % 24000L;
        if (margin < 0L) {
            margin += 24000L;
        }
        this.setFullTime(this.getFullTime() + margin);
    }
    
    @Override
    public long getFullTime() {
        return this.world.getWorldTime();
    }
    
    @Override
    public void setFullTime(final long time) {
        this.world.setWorldTime(time);
        for (final Player p : this.getPlayers()) {
            final CraftPlayer cp = (CraftPlayer)p;
            if (cp.getHandle().connection == null) {
                continue;
            }
            cp.getHandle().connection.sendPacket(new SPacketTimeUpdate(cp.getHandle().worldObj.getTotalWorldTime(), cp.getHandle().getPlayerTime(), cp.getHandle().worldObj.getGameRules().getBoolean("doDaylightCycle")));
        }
    }
    
    @Override
    public boolean createExplosion(final double x, final double y, final double z, final float power) {
        return this.createExplosion(x, y, z, power, false, true);
    }
    
    @Override
    public boolean createExplosion(final double x, final double y, final double z, final float power, final boolean setFire) {
        return this.createExplosion(x, y, z, power, setFire, true);
    }
    
    @Override
    public boolean createExplosion(final double x, final double y, final double z, final float power, final boolean setFire, final boolean breakBlocks) {
        return !this.world.newExplosion(null, x, y, z, power, setFire, breakBlocks).wasCanceled;
    }
    
    @Override
    public boolean createExplosion(final Location loc, final float power) {
        return this.createExplosion(loc, power, false);
    }
    
    @Override
    public boolean createExplosion(final Location loc, final float power, final boolean setFire) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }
    
    @Override
    public Environment getEnvironment() {
        return this.environment;
    }
    
    public void setEnvironment(final Environment env) {
        if (this.environment != env) {
            this.environment = env;
            switch (env) {
                case NORMAL: {
                    this.world.provider = new WorldProviderSurface();
                    break;
                }
                case NETHER: {
                    this.world.provider = new WorldProviderHell();
                    break;
                }
                case THE_END: {
                    this.world.provider = new WorldProviderEnd();
                    break;
                }
            }
        }
    }
    
    @Override
    public Block getBlockAt(final Location location) {
        return this.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    @Override
    public int getBlockTypeIdAt(final Location location) {
        return this.getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    @Override
    public int getHighestBlockYAt(final Location location) {
        return this.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }
    
    @Override
    public Chunk getChunkAt(final Location location) {
        return this.getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        return this.generator;
    }
    
    @Override
    public List<BlockPopulator> getPopulators() {
        return this.populators;
    }
    
    @Override
    public Block getHighestBlockAt(final int x, final int z) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z), z);
    }
    
    @Override
    public Block getHighestBlockAt(final Location location) {
        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }
    
    @Override
    public Biome getBiome(final int x, final int z) {
        return CraftBlock.biomeBaseToBiome(this.world.getBiome(new BlockPos(x, 0, z)));
    }
    
    @Override
    public void setBiome(final int x, final int z, final Biome bio) {
        final net.minecraft.world.biome.Biome bb = CraftBlock.biomeToBiomeBase(bio);
        if (this.world.isBlockLoaded(new BlockPos(x, 0, z))) {
            final net.minecraft.world.chunk.Chunk chunk = this.world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
            if (chunk != null) {
                final byte[] biomevals = chunk.getBiomeArray();
                biomevals[(z & 0xF) << 4 | (x & 0xF)] = (byte)net.minecraft.world.biome.Biome.REGISTRY.getIDForObject(bb);
            }
        }
    }
    
    @Override
    public double getTemperature(final int x, final int z) {
        return this.world.getBiome(new BlockPos(x, 0, z)).getTemperature();
    }
    
    @Override
    public double getHumidity(final int x, final int z) {
        return this.world.getBiome(new BlockPos(x, 0, z)).getRainfall();
    }
    
    @Override
    public List<org.bukkit.entity.Entity> getEntities() {
        final List<org.bukkit.entity.Entity> list = new ArrayList<org.bukkit.entity.Entity>();
        for (final Object o : this.world.loadedEntityList) {
            if (o instanceof Entity) {
                final Entity mcEnt = (Entity)o;
                final org.bukkit.entity.Entity bukkitEntity = mcEnt.getBukkitEntity();
                if (bukkitEntity == null) {
                    continue;
                }
                list.add(bukkitEntity);
            }
        }
        return list;
    }
    
    @Override
    public List<LivingEntity> getLivingEntities() {
        final List<LivingEntity> list = new ArrayList<LivingEntity>();
        for (final Object o : this.world.loadedEntityList) {
            if (o instanceof Entity) {
                final Entity mcEnt = (Entity)o;
                final org.bukkit.entity.Entity bukkitEntity = mcEnt.getBukkitEntity();
                if (bukkitEntity == null || !(bukkitEntity instanceof LivingEntity)) {
                    continue;
                }
                list.add((LivingEntity)bukkitEntity);
            }
        }
        return list;
    }
    
    @Deprecated
    @Override
    public <T extends org.bukkit.entity.Entity> Collection<T> getEntitiesByClass(final Class<T>... classes) {
        return (Collection<T>)this.getEntitiesByClasses((Class<?>[])classes);
    }
    
    @Override
    public <T extends org.bukkit.entity.Entity> Collection<T> getEntitiesByClass(final Class<T> clazz) {
        final Collection<T> list = new ArrayList<T>();
        for (final Object entity : this.world.loadedEntityList) {
            if (entity instanceof Entity) {
                final org.bukkit.entity.Entity bukkitEntity = ((Entity)entity).getBukkitEntity();
                if (bukkitEntity == null) {
                    continue;
                }
                final Class<?> bukkitClass = bukkitEntity.getClass();
                if (!clazz.isAssignableFrom(bukkitClass)) {
                    continue;
                }
                list.add((T)bukkitEntity);
            }
        }
        return list;
    }
    
    @Override
    public Collection<org.bukkit.entity.Entity> getEntitiesByClasses(final Class<?>... classes) {
        final Collection<org.bukkit.entity.Entity> list = new ArrayList<org.bukkit.entity.Entity>();
        for (final Object entity : this.world.loadedEntityList) {
            if (entity instanceof Entity) {
                final org.bukkit.entity.Entity bukkitEntity = ((Entity)entity).getBukkitEntity();
                if (bukkitEntity == null) {
                    continue;
                }
                final Class<?> bukkitClass = bukkitEntity.getClass();
                for (final Class<?> clazz : classes) {
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
    public Collection<org.bukkit.entity.Entity> getNearbyEntities(final Location location, final double x, final double y, final double z) {
        if (location == null || !location.getWorld().equals(this)) {
            return /*(Collection<org.bukkit.entity.Entity>)*/Collections.emptyList();
        }
        final AxisAlignedBB bb = new AxisAlignedBB(location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z);
        final List<Entity> entityList = this.getHandle().getEntitiesInAABBexcluding(null, bb, null);
        final List<org.bukkit.entity.Entity> bukkitEntityList = new ArrayList<org.bukkit.entity.Entity>(entityList.size());
        for (final Object entity : entityList) {
            bukkitEntityList.add(((Entity)entity).getBukkitEntity());
        }
        return bukkitEntityList;
    }
    
    @Override
    public List<Player> getPlayers() {
        final List<Player> list = new ArrayList<Player>(this.world.playerEntities.size());
        for (final EntityPlayer human : this.world.playerEntities) {
            final HumanEntity bukkitEntity = human.getBukkitEntity();
            if (bukkitEntity != null && bukkitEntity instanceof Player) {
                list.add((Player)bukkitEntity);
            }
        }
        return list;
    }
    
    @Override
    public void save() {
        this.server.checkSaveState();
        try {
            final boolean oldSave = this.world.disableLevelSaving;
            this.world.disableLevelSaving = false;
            this.world.saveAllChunks(true, null);
            this.world.disableLevelSaving = oldSave;
        }
        catch (MinecraftException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public boolean isAutoSave() {
        return !this.world.disableLevelSaving;
    }
    
    @Override
    public void setAutoSave(final boolean value) {
        this.world.disableLevelSaving = !value;
    }
    
    @Override
    public void setDifficulty(final Difficulty difficulty) {
        this.getHandle().worldInfo.setDifficulty(EnumDifficulty.getDifficultyEnum(difficulty.getValue()));
    }
    
    @Override
    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }
    
    public BlockMetadataStore getBlockMetadata() {
        return this.blockMetadata;
    }
    
    @Override
    public boolean hasStorm() {
        return this.world.worldInfo.isRaining();
    }
    
    @Override
    public void setStorm(final boolean hasStorm) {
        this.world.worldInfo.setRaining(hasStorm);
    }
    
    @Override
    public int getWeatherDuration() {
        return this.world.worldInfo.getRainTime();
    }
    
    @Override
    public void setWeatherDuration(final int duration) {
        this.world.worldInfo.setRainTime(duration);
    }
    
    @Override
    public boolean isThundering() {
        return this.world.worldInfo.isThundering();
    }
    
    @Override
    public void setThundering(final boolean thundering) {
        this.world.worldInfo.setThundering(thundering);
    }
    
    @Override
    public int getThunderDuration() {
        return this.world.worldInfo.getThunderTime();
    }
    
    @Override
    public void setThunderDuration(final int duration) {
        this.world.worldInfo.setThunderTime(duration);
    }
    
    @Override
    public long getSeed() {
        return this.world.worldInfo.getSeed();
    }
    
    @Override
    public boolean getPVP() {
        return this.world.pvpMode;
    }
    
    @Override
    public void setPVP(final boolean pvp) {
        this.world.pvpMode = pvp;
    }
    
    public void playEffect(final Player player, final Effect effect, final int data) {
        this.playEffect(player.getLocation(), effect, data, 0);
    }
    
    @Override
    public void playEffect(final Location location, final Effect effect, final int data) {
        this.playEffect(location, effect, data, 64);
    }
    
    @Override
    public <T> void playEffect(final Location loc, final Effect effect, final T data) {
        this.playEffect(loc, effect, data, 64);
    }
    
    @Override
    public <T> void playEffect(final Location loc, final Effect effect, final T data, final int radius) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        }
        else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }
        final int datavalue = (data == null) ? 0 : CraftEffect.getDataValue(effect, data);
        this.playEffect(loc, effect, datavalue, radius);
    }
    
    @Override
    public void playEffect(final Location location, final Effect effect, final int data, int radius) {
        Validate.notNull((Object)location, "Location cannot be null");
        Validate.notNull((Object)effect, "Effect cannot be null");
        Validate.notNull((Object)location.getWorld(), "World cannot be null");
        final int packetData = effect.getId();
        final SPacketEffect packet = new SPacketEffect(packetData, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), data, false);
        radius *= radius;
        for (final Player player : this.getPlayers()) {
            if (((CraftPlayer)player).getHandle().connection == null) {
                continue;
            }
            if (!location.getWorld().equals(player.getWorld())) {
                continue;
            }
            final int distance = (int)player.getLocation().distanceSquared(location);
            if (distance > radius) {
                continue;
            }
            ((CraftPlayer)player).getHandle().connection.sendPacket(packet);
        }
    }
    
    @Override
    public <T extends org.bukkit.entity.Entity> T spawn(final Location location, final Class<T> clazz) throws IllegalArgumentException {
        return this.spawn(location, clazz, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
    
    @Override
    public FallingBlock spawnFallingBlock(final Location location, final Material material, final byte data) throws IllegalArgumentException {
        Validate.notNull((Object)location, "Location cannot be null");
        Validate.notNull((Object)material, "Material cannot be null");
        Validate.isTrue(material.isBlock(), "Material must be a block");
        final EntityFallingBlock entity = new EntityFallingBlock(this.world, location.getX(), location.getY(), location.getZ(), CraftMagicNumbers.getBlock(material).getStateFromMeta(data));
        entity.fallTime = 1;
        this.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (FallingBlock)entity.getBukkitEntity();
    }
    
    @Override
    public FallingBlock spawnFallingBlock(final Location location, final int blockId, final byte blockData) throws IllegalArgumentException {
        return this.spawnFallingBlock(location, Material.getMaterial(blockId), blockData);
    }
    
    public Entity createEntity(final Location location, final Class<? extends org.bukkit.entity.Entity> clazz) throws IllegalArgumentException {
        if (location == null || clazz == null) {
            throw new IllegalArgumentException("Location or entity class cannot be null");
        }
        Entity entity = null;
        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();
        final float pitch = location.getPitch();
        final float yaw = location.getYaw();
        if (Boat.class.isAssignableFrom(clazz)) {
            entity = new EntityBoat(this.world, x, y, z);
        }
        else if (FallingBlock.class.isAssignableFrom(clazz)) {
            entity = new EntityFallingBlock(this.world, x, y, z, this.world.getBlockState(new BlockPos(x, y, z)));
        }
        else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = new EntitySnowball(this.world, x, y, z);
            }
            else if (Egg.class.isAssignableFrom(clazz)) {
                entity = new EntityEgg(this.world, x, y, z);
            }
            else if (Arrow.class.isAssignableFrom(clazz)) {
                if (TippedArrow.class.isAssignableFrom(clazz)) {
                    entity = new EntityTippedArrow(this.world);
                    ((EntityTippedArrow)entity).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
                }
                else if (SpectralArrow.class.isAssignableFrom(clazz)) {
                    entity = new EntitySpectralArrow(this.world);
                }
                else {
                    entity = new EntityTippedArrow(this.world);
                }
                entity.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
            }
            else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                entity = new EntityExpBottle(this.world);
                entity.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
            }
            else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderPearl(this.world);
                entity.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
            }
            else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                if (LingeringPotion.class.isAssignableFrom(clazz)) {
                    entity = new EntityPotion(this.world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1)));
                }
                else {
                    entity = new EntityPotion(this.world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
                }
            }
            else if (Fireball.class.isAssignableFrom(clazz)) {
                if (SmallFireball.class.isAssignableFrom(clazz)) {
                    entity = new EntitySmallFireball(this.world);
                }
                else if (WitherSkull.class.isAssignableFrom(clazz)) {
                    entity = new EntityWitherSkull(this.world);
                }
                else if (DragonFireball.class.isAssignableFrom(clazz)) {
                    entity = new EntityDragonFireball(this.world);
                }
                else {
                    entity = new EntityLargeFireball(this.world);
                }
                entity.setLocationAndAngles(x, y, z, yaw, pitch);
                final Vector direction = location.getDirection().multiply(10);
                ((EntityFireball)entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            }
            else if (ShulkerBullet.class.isAssignableFrom(clazz)) {
                entity = new EntityShulkerBullet(this.world);
                entity.setLocationAndAngles(x, y, z, yaw, pitch);
            }
        }
        else if (Minecart.class.isAssignableFrom(clazz)) {
            if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartFurnace(this.world, x, y, z);
            }
            else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartChest(this.world, x, y, z);
            }
            else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartTNT(this.world, x, y, z);
            }
            else if (HopperMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartHopper(this.world, x, y, z);
            }
            else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartMobSpawner(this.world, x, y, z);
            }
            else if (CommandMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartCommandBlock(this.world, x, y, z);
            }
            else {
                entity = new EntityMinecartEmpty(this.world, x, y, z);
            }
        }
        else if (EnderSignal.class.isAssignableFrom(clazz)) {
            entity = new EntityEnderEye(this.world, x, y, z);
        }
        else if (EnderCrystal.class.isAssignableFrom(clazz)) {
            entity = new EntityEnderCrystal(this.world);
            entity.setLocationAndAngles(x, y, z, 0.0f, 0.0f);
        }
        else if (LivingEntity.class.isAssignableFrom(clazz)) {
            if (Chicken.class.isAssignableFrom(clazz)) {
                entity = new EntityChicken(this.world);
            }
            else if (Cow.class.isAssignableFrom(clazz)) {
                if (MushroomCow.class.isAssignableFrom(clazz)) {
                    entity = new EntityMooshroom(this.world);
                }
                else {
                    entity = new EntityCow(this.world);
                }
            }
            else if (Golem.class.isAssignableFrom(clazz)) {
                if (Snowman.class.isAssignableFrom(clazz)) {
                    entity = new EntitySnowman(this.world);
                }
                else if (IronGolem.class.isAssignableFrom(clazz)) {
                    entity = new EntityIronGolem(this.world);
                }
                else if (Shulker.class.isAssignableFrom(clazz)) {
                    entity = new EntityShulker(this.world);
                }
            }
            else if (Creeper.class.isAssignableFrom(clazz)) {
                entity = new EntityCreeper(this.world);
            }
            else if (Ghast.class.isAssignableFrom(clazz)) {
                entity = new EntityGhast(this.world);
            }
            else if (Pig.class.isAssignableFrom(clazz)) {
                entity = new EntityPig(this.world);
            }
            else if (!Player.class.isAssignableFrom(clazz)) {
                if (Sheep.class.isAssignableFrom(clazz)) {
                    entity = new EntitySheep(this.world);
                }
                else if (Horse.class.isAssignableFrom(clazz)) {
                    entity = new EntityHorse(this.world);
                }
                else if (Skeleton.class.isAssignableFrom(clazz)) {
                    entity = new EntitySkeleton(this.world);
                }
                else if (Slime.class.isAssignableFrom(clazz)) {
                    if (MagmaCube.class.isAssignableFrom(clazz)) {
                        entity = new EntityMagmaCube(this.world);
                    }
                    else {
                        entity = new EntitySlime(this.world);
                    }
                }
                else if (Spider.class.isAssignableFrom(clazz)) {
                    if (CaveSpider.class.isAssignableFrom(clazz)) {
                        entity = new EntityCaveSpider(this.world);
                    }
                    else {
                        entity = new EntitySpider(this.world);
                    }
                }
                else if (Squid.class.isAssignableFrom(clazz)) {
                    entity = new EntitySquid(this.world);
                }
                else if (Tameable.class.isAssignableFrom(clazz)) {
                    if (Wolf.class.isAssignableFrom(clazz)) {
                        entity = new EntityWolf(this.world);
                    }
                    else if (Ocelot.class.isAssignableFrom(clazz)) {
                        entity = new EntityOcelot(this.world);
                    }
                }
                else if (PigZombie.class.isAssignableFrom(clazz)) {
                    entity = new EntityPigZombie(this.world);
                }
                else if (Zombie.class.isAssignableFrom(clazz)) {
                    entity = new EntityZombie(this.world);
                }
                else if (Giant.class.isAssignableFrom(clazz)) {
                    entity = new EntityGiantZombie(this.world);
                }
                else if (Silverfish.class.isAssignableFrom(clazz)) {
                    entity = new EntitySilverfish(this.world);
                }
                else if (Enderman.class.isAssignableFrom(clazz)) {
                    entity = new EntityEnderman(this.world);
                }
                else if (Blaze.class.isAssignableFrom(clazz)) {
                    entity = new EntityBlaze(this.world);
                }
                else if (Villager.class.isAssignableFrom(clazz)) {
                    entity = new EntityVillager(this.world);
                }
                else if (Witch.class.isAssignableFrom(clazz)) {
                    entity = new EntityWitch(this.world);
                }
                else if (Wither.class.isAssignableFrom(clazz)) {
                    entity = new EntityWither(this.world);
                }
                else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                    if (EnderDragon.class.isAssignableFrom(clazz)) {
                        entity = new EntityDragon(this.world);
                    }
                }
                else if (Ambient.class.isAssignableFrom(clazz)) {
                    if (Bat.class.isAssignableFrom(clazz)) {
                        entity = new EntityBat(this.world);
                    }
                }
                else if (Rabbit.class.isAssignableFrom(clazz)) {
                    entity = new EntityRabbit(this.world);
                }
                else if (Endermite.class.isAssignableFrom(clazz)) {
                    entity = new EntityEndermite(this.world);
                }
                else if (Guardian.class.isAssignableFrom(clazz)) {
                    entity = new EntityGuardian(this.world);
                }
                else if (ArmorStand.class.isAssignableFrom(clazz)) {
                    entity = new EntityArmorStand(this.world, x, y, z);
                }
                else if (PolarBear.class.isAssignableFrom(clazz)) {
                    entity = new EntityPolarBear(this.world);
                }
            }
            if (entity != null) {
                entity.setPositionAndRotation(x, y, z, yaw, pitch);
            }
        }
        else if (Hanging.class.isAssignableFrom(clazz)) {
            final Block block = this.getBlockAt(location);
            BlockFace face = BlockFace.SELF;
            int width = 16;
            int height = 16;
            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
            }
            else if (LeashHitch.class.isAssignableFrom(clazz)) {
                width = 9;
                height = 9;
            }
            final BlockFace[] faces = { BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH };
            final BlockPos pos = new BlockPos((int)x, (int)y, (int)z);
            BlockFace[] array;
            for (int length = (array = faces).length, i = 0; i < length; ++i) {
                final BlockFace dir = array[i];
                final net.minecraft.block.Block nmsBlock = CraftMagicNumbers.getBlock(block.getRelative(dir));
                if (nmsBlock.getDefaultState().getMaterial().isSolid() || BlockRedstoneDiode.isDiode(nmsBlock.getDefaultState())) {
                    boolean taken = false;
                    final AxisAlignedBB bb = EntityHanging.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height);
                    final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(null, bb);
                    for (Iterator<Entity> it = list.iterator(); !taken && it.hasNext(); taken = true) {
                        final Entity e = it.next();
                        if (e instanceof EntityHanging) {}
                    }
                    if (!taken) {
                        face = dir;
                        break;
                    }
                }
            }
            if (LeashHitch.class.isAssignableFrom(clazz)) {
                entity = new EntityLeashKnot(this.world, new BlockPos((int)x, (int)y, (int)z));
                entity.forceSpawn = true;
            }
            else {
                final EnumFacing dir2 = CraftBlock.blockFaceToNotch(face).getOpposite();
                if (Painting.class.isAssignableFrom(clazz)) {
                    entity = new EntityPainting(this.world, new BlockPos((int)x, (int)y, (int)z), dir2);
                }
                else if (ItemFrame.class.isAssignableFrom(clazz)) {
                    entity = new EntityItemFrame(this.world, new BlockPos((int)x, (int)y, (int)z), dir2);
                }
            }
            if (entity != null && !((EntityHanging)entity).onValidSurface()) {
                throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
            }
        }
        else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = new EntityTNTPrimed(this.world, x, y, z, null);
        }
        else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = new EntityXPOrb(this.world, x, y, z, 0);
        }
        else if (Weather.class.isAssignableFrom(clazz)) {
            if (LightningStrike.class.isAssignableFrom(clazz)) {
                entity = new EntityLightningBolt(this.world, x, y, z, false);
            }
        }
        else if (Firework.class.isAssignableFrom(clazz)) {
            entity = new EntityFireworkRocket(this.world, x, y, z, null);
        }
        else if (AreaEffectCloud.class.isAssignableFrom(clazz)) {
            entity = new EntityAreaEffectCloud(this.world, x, y, z);
        }
        if (entity != null) {
            return entity;
        }
        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
    }
    
    public <T extends org.bukkit.entity.Entity> T addEntity(final Entity entity, final CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, (Object)"Cannot spawn null entity");
        if (entity instanceof EntityLiving) {
            ((EntityLiving)entity).onInitialSpawn(this.getHandle().getDifficultyForLocation(new BlockPos(entity)), null);
        }
        this.world.addEntity(entity, reason);
        return (T)entity.getBukkitEntity();
    }
    
    public <T extends org.bukkit.entity.Entity> T spawn(final Location location, final Class<T> clazz, final CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        final Entity entity = this.createEntity(location, clazz);
        return this.addEntity(entity, reason);
    }
    
    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(final int x, final int z, final boolean includeBiome, final boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }
    
    @Override
    public void setSpawnFlags(final boolean allowMonsters, final boolean allowAnimals) {
        this.world.setAllowedSpawnTypes(allowMonsters, allowAnimals);
    }
    
    @Override
    public boolean getAllowAnimals() {
        return this.world.spawnPeacefulMobs;
    }
    
    @Override
    public boolean getAllowMonsters() {
        return this.world.spawnHostileMobs;
    }
    
    @Override
    public int getMaxHeight() {
        return this.world.getHeight();
    }
    
    @Override
    public int getSeaLevel() {
        return 64;
    }
    
    @Override
    public boolean getKeepSpawnInMemory() {
        return this.world.keepSpawnInMemory;
    }
    
    @Override
    public void setKeepSpawnInMemory(final boolean keepLoaded) {
        this.world.keepSpawnInMemory = keepLoaded;
        final BlockPos chunkcoordinates = this.world.getSpawnPoint();
        final int chunkCoordX = chunkcoordinates.getX() >> 4;
        final int chunkCoordZ = chunkcoordinates.getZ() >> 4;
        for (int x = -12; x <= 12; ++x) {
            for (int z = -12; z <= 12; ++z) {
                if (keepLoaded) {
                    this.loadChunk(chunkCoordX + x, chunkCoordZ + z);
                }
                else if (this.isChunkLoaded(chunkCoordX + x, chunkCoordZ + z)) {
                    this.unloadChunk(chunkCoordX + x, chunkCoordZ + z);
                }
            }
        }
    }
    
    @Override
    public int hashCode() {
        return this.getUID().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftWorld other = (CraftWorld)obj;
        return this.getUID() == other.getUID();
    }
    
    @Override
    public File getWorldFolder() {
        return ((SaveHandler)this.world.getSaveHandler()).getWorldDirectory();
    }
    
    @Override
    public void sendPluginMessage(final Plugin source, final String channel, final byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
        for (final Player player : this.getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }
    
    @Override
    public Set<String> getListeningPluginChannels() {
        final Set<String> result = new HashSet<String>();
        for (final Player player : this.getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }
        return result;
    }
    
    @Override
    public WorldType getWorldType() {
        return WorldType.getByName(this.world.getWorldInfo().getTerrainType().getWorldTypeName());
    }
    
    @Override
    public boolean canGenerateStructures() {
        return this.world.getWorldInfo().isMapFeaturesEnabled();
    }
    
    @Override
    public long getTicksPerAnimalSpawns() {
        return this.world.ticksPerAnimalSpawns;
    }
    
    @Override
    public void setTicksPerAnimalSpawns(final int ticksPerAnimalSpawns) {
        this.world.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
    }
    
    @Override
    public long getTicksPerMonsterSpawns() {
        return this.world.ticksPerMonsterSpawns;
    }
    
    @Override
    public void setTicksPerMonsterSpawns(final int ticksPerMonsterSpawns) {
        this.world.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
    }
    
    @Override
    public void setMetadata(final String metadataKey, final MetadataValue newMetadataValue) {
        (/*(MetadataStoreBase<CraftWorld>)*/this.server.getWorldMetadata()).setMetadata(this, metadataKey, newMetadataValue);
    }
    
    @Override
    public List<MetadataValue> getMetadata(final String metadataKey) {
        return (/*(MetadataStoreBase<CraftWorld>)*/this.server.getWorldMetadata()).getMetadata(this, metadataKey);
    }
    
    @Override
    public boolean hasMetadata(final String metadataKey) {
        return (/*(MetadataStoreBase<CraftWorld>)*/this.server.getWorldMetadata()).hasMetadata(this, metadataKey);
    }
    
    @Override
    public void removeMetadata(final String metadataKey, final Plugin owningPlugin) {
        (/*(MetadataStoreBase<CraftWorld>)*/this.server.getWorldMetadata()).removeMetadata(this, metadataKey, owningPlugin);
    }
    
    @Override
    public int getMonsterSpawnLimit() {
        if (this.monsterSpawn < 0) {
            return this.server.getMonsterSpawnLimit();
        }
        return this.monsterSpawn;
    }
    
    @Override
    public void setMonsterSpawnLimit(final int limit) {
        this.monsterSpawn = limit;
    }
    
    @Override
    public int getAnimalSpawnLimit() {
        if (this.animalSpawn < 0) {
            return this.server.getAnimalSpawnLimit();
        }
        return this.animalSpawn;
    }
    
    @Override
    public void setAnimalSpawnLimit(final int limit) {
        this.animalSpawn = limit;
    }
    
    @Override
    public int getWaterAnimalSpawnLimit() {
        if (this.waterAnimalSpawn < 0) {
            return this.server.getWaterAnimalSpawnLimit();
        }
        return this.waterAnimalSpawn;
    }
    
    @Override
    public void setWaterAnimalSpawnLimit(final int limit) {
        this.waterAnimalSpawn = limit;
    }
    
    @Override
    public int getAmbientSpawnLimit() {
        if (this.ambientSpawn < 0) {
            return this.server.getAmbientSpawnLimit();
        }
        return this.ambientSpawn;
    }
    
    @Override
    public void setAmbientSpawnLimit(final int limit) {
        this.ambientSpawn = limit;
    }
    
    @Override
    public void playSound(final Location loc, final Sound sound, final float volume, final float pitch) {
        if (loc == null || sound == null) {
            return;
        }
        final double x = loc.getX();
        final double y = loc.getY();
        final double z = loc.getZ();
        this.getHandle().playSound(null, x, y, z, CraftSound.getSoundEffect(CraftSound.getSound(sound)), SoundCategory.MASTER, volume, pitch);
    }
    
    @Override
    public void playSound(final Location loc, final String sound, final float volume, final float pitch) {
        if (loc == null || sound == null) {
            return;
        }
        final double x = loc.getX();
        final double y = loc.getY();
        final double z = loc.getZ();
        final SPacketCustomSound packet = new SPacketCustomSound(sound, SoundCategory.MASTER, x, y, z, volume, pitch);
        this.world.getMinecraftServer().getPlayerList().sendToAllNearExcept(null, x, y, z, (volume > 1.0f) ? ((double)(16.0f * volume)) : 16.0, this.world.dimension, packet);
    }
    
    @Override
    public String getGameRuleValue(final String rule) {
        return this.getHandle().getGameRules().getString(rule);
    }
    
    @Override
    public boolean setGameRuleValue(final String rule, final String value) {
        if (rule == null || value == null) {
            return false;
        }
        if (!this.isGameRule(rule)) {
            return false;
        }
        this.getHandle().getGameRules().setOrCreateGameRule(rule, value);
        return true;
    }
    
    @Override
    public String[] getGameRules() {
        return this.getHandle().getGameRules().getRules();
    }
    
    @Override
    public boolean isGameRule(final String rule) {
        return this.getHandle().getGameRules().hasRule(rule);
    }
    
    @Override
    public WorldBorder getWorldBorder() {
        if (this.worldBorder == null) {
            this.worldBorder = new CraftWorldBorder(this);
        }
        return this.worldBorder;
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
        this.getHandle().sendParticles(null, CraftParticle.toNMS(particle), true, x, y, z, count, offsetX, offsetY, offsetZ, extra, CraftParticle.toData(particle, data));
    }
    
    public void processChunkGC() {
        ++this.chunkGCTickCount;
        if (this.chunkLoadCount >= this.server.chunkGCLoadThresh && this.server.chunkGCLoadThresh > 0) {
            this.chunkLoadCount = 0;
        }
        else {
            if (this.chunkGCTickCount < this.server.chunkGCPeriod || this.server.chunkGCPeriod <= 0) {
                return;
            }
            this.chunkGCTickCount = 0;
        }
        final ChunkProviderServer cps = this.world.getChunkProvider();
        for (final net.minecraft.world.chunk.Chunk chunk : cps.id2ChunkMap.values()) {
            if (this.isChunkInUse(chunk.xPosition, chunk.zPosition)) {
                continue;
            }
            if (cps.droppedChunksSet.contains(ChunkPos.asLong(chunk.xPosition, chunk.zPosition))) {
                continue;
            }
            cps.unload(chunk);
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
