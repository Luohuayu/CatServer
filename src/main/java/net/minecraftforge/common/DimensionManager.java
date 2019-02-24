/*
 * Minecraft Forge
 * Copyright (c) 2016.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multiset;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.DimensionType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;

import javax.annotation.Nullable;

import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;

public class DimensionManager
{
    private static class Dimension
    {
        private final DimensionType type;
        private int ticksWaited;
        private Dimension(DimensionType type)
        {
            this.type = type;
            this.ticksWaited = 0;
        }
    }

    private static boolean hasInit = false;
    private static int lastUsedId = 0;

    private static final Int2ObjectMap<WorldServer> worlds = Int2ObjectMaps.synchronize(new Int2ObjectLinkedOpenHashMap<>());
    private static final Int2ObjectMap<Dimension> dimensions = Int2ObjectMaps.synchronize(new Int2ObjectLinkedOpenHashMap<>());
    private static final IntSet keepLoaded = IntSets.synchronize(new IntOpenHashSet());
    private static final IntSet unloadQueue = IntSets.synchronize(new IntLinkedOpenHashSet());
    private static final IntSet usedIds = new IntOpenHashSet();
    private static final ConcurrentMap<World, World> weakWorldMap = new MapMaker().weakKeys().weakValues().makeMap();
    private static final Multiset<Integer> leakedWorlds = HashMultiset.create();
    private static ArrayList<Integer> bukkitDims = new ArrayList<Integer>(); // used to keep track of Bukkit dimensions

    /**
     * Returns a list of dimensions associated with this DimensionType.
     */
    public static int[] getDimensions(DimensionType type)
    {
        int[] ret = new int[dimensions.size()];
        int x = 0;
        for (Int2ObjectMap.Entry<Dimension> ent : dimensions.int2ObjectEntrySet())
        {
            if (ent.getValue().type == type)
            {
                ret[x++] = ent.getIntKey();
            }
        }

        return Arrays.copyOf(ret, x);
    }

    public static Map<DimensionType, IntSortedSet> getRegisteredDimensions()
    {
        Map<DimensionType, IntSortedSet> map = new IdentityHashMap<>();
        for (Int2ObjectMap.Entry<Dimension> entry : dimensions.int2ObjectEntrySet())
        {
            map.computeIfAbsent(entry.getValue().type, k -> new IntRBTreeSet()).add(entry.getIntKey());
        }
        return map;
    }

    public static void init()
    {
        if (hasInit)
        {
            return;
        }

        hasInit = true;

        registerDimension( 0, DimensionType.OVERWORLD);
        registerDimension(-1, DimensionType.NETHER);
        registerDimension( 1, DimensionType.THE_END);
    }

    public static void registerDimension(int id, DimensionType type)
    {
        DimensionType.getById(type.getId()); //Check if type is invalid {will throw an error} No clue how it would be invalid tho...
        if (dimensions.containsKey(id))
        {
            throw new IllegalArgumentException(String.format("Failed to register dimension for id %d, One is already registered", id));
        }
        dimensions.put(id, new Dimension(type));
        if (id >= 0)
        {
            usedIds.add(id);
        }
        // CatServer - register Environment to Bukkit
        if (id != -1 && id != 0 && id != 1) // ignore vanilla
        {
            registerBukkitDimension(id, type.getName());
        }
    }

    /**
     * For unregistering a dimension when the save is changed (disconnected from a server or loaded a new save
     */
    public static void unregisterDimension(int id)
    {
        if (!dimensions.containsKey(id))
        {
            throw new IllegalArgumentException(String.format("Failed to unregister dimension for id %d; No provider registered", id));
        }
        dimensions.remove(id);
    }

    public static boolean isDimensionRegistered(int dim)
    {
        return dimensions.containsKey(dim);
    }

    public static DimensionType getProviderType(int dim)
    {
        if (!dimensions.containsKey(dim))
        {
            throw new IllegalArgumentException(String.format("Could not get provider type for dimension %d, does not exist", dim));
        }
        return dimensions.get(dim).type;
    }

    public static WorldProvider getProvider(int dim)
    {
        return getWorld(dim).provider;
    }

    public static Integer[] getIDs(boolean check)
    {
        if (check)
        {
            List<World> allWorlds = Lists.newArrayList(weakWorldMap.keySet());
            allWorlds.removeAll(worlds.values());
            for (ListIterator<World> li = allWorlds.listIterator(); li.hasNext(); )
            {
                World w = li.next();
                leakedWorlds.add(System.identityHashCode(w));
            }
            for (World w : allWorlds)
            {
                int leakCount = leakedWorlds.count(System.identityHashCode(w));
                if (leakCount == 5)
                {
                    FMLLog.log.debug("The world {} ({}) may have leaked: first encounter (5 occurrences).\n", Integer.toHexString(System.identityHashCode(w)), w.getWorldInfo().getWorldName());
                }
                else if (leakCount % 5 == 0)
                {
                    FMLLog.log.debug("The world {} ({}) may have leaked: seen {} times.\n", Integer.toHexString(System.identityHashCode(w)), w.getWorldInfo().getWorldName(), leakCount);
                }
            }
        }
        return getIDs();
    }
    public static Integer[] getIDs()
    {
        return worlds.keySet().toArray(new Integer[0]); // Only loaded dims, since usually used to cycle through loaded worlds
    }

    public static void setWorld(int id, @Nullable WorldServer world, MinecraftServer server)
    {
        if (world != null)
        {
            worlds.put(id, world);
            weakWorldMap.put(world, world);
            // handle all world adds here for Bukkit
            if (!FMLCommonHandler.instance().getMinecraftServerInstance().worldServerList.contains(world))
            {
                FMLCommonHandler.instance().getMinecraftServerInstance().worldServerList.add(world);
            }
            server.worldTickTimes.put(id, new long[100]);
            FMLLog.log.info("Loading dimension {} ({}) ({})", id, world.getWorldInfo().getWorldName(), world.getMinecraftServer());
        }
        else
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().worldServerList.remove(getWorld(id)); // CatServer - remove world from our new world arraylist
            worlds.remove(id);
            server.worldTickTimes.remove(id);
            FMLLog.log.info("Unloading dimension {}", id);
        }

        ArrayList<WorldServer> tmp = new ArrayList<WorldServer>();
        if (worlds.get( 0) != null)
            tmp.add(worlds.get( 0));
        if (worlds.get(-1) != null)
            tmp.add(worlds.get(-1));
        if (worlds.get( 1) != null)
            tmp.add(worlds.get( 1));

        for (Int2ObjectMap.Entry<WorldServer> entry : worlds.int2ObjectEntrySet())
        {
            int dim = entry.getIntKey();
            if (dim >= -1 && dim <= 1)
            {
                continue;
            }
            tmp.add(entry.getValue());
        }

        server.worlds = tmp.toArray(new WorldServer[0]);
    }

    public static void initDimension(int dim)
    {
        WorldServer overworld = getWorld(0);
        if (overworld == null)
        {
            throw new RuntimeException("Cannot Hotload Dim: Overworld is not Loaded!");
        }
        try
        {
            DimensionManager.getProviderType(dim);
        }
        catch (Exception e)
        {
            FMLLog.log.error("Cannot Hotload Dim: {}", dim); // CatServer - no output exception
            return; // If a provider hasn't been registered then we can't hotload the dim
        }
        MinecraftServer mcServer = overworld.getMinecraftServer();
        ISaveHandler savehandler = overworld.getSaveHandler();
        WorldSettings worldSettings = new WorldSettings(overworld.getWorldInfo());
        String worldType;
        String name;
        Environment env = Environment.getEnvironment(dim);
        if (dim >= -1 && dim <= 1)
        {
            if ((dim == -1 && !mcServer.getAllowNether()) || (dim == 1 && !mcServer.server.getAllowEnd()))
                return;
            worldType = env.toString().toLowerCase();
            name = "DIM" + dim;
        } else {
            WorldProvider provider = WorldProvider.getProviderForDimension(dim);
            worldType = provider.getClass().getSimpleName().toLowerCase();
            worldType = worldType.replace("worldprovider", "");
            worldType = worldType.replace("provider", "");

            if(Environment.getEnvironment(DimensionManager.getProviderType(dim).getId()) == null){
                env = DimensionManager.registerBukkitDimension(DimensionManager.getProviderType(dim).getId(), worldType);
            }

            name = provider.getSaveFolder();
        }

        ChunkGenerator gen = mcServer.server.getGenerator(name);
        if (mcServer instanceof DedicatedServer) {
            worldSettings.setGeneratorOptions(((DedicatedServer) mcServer).getStringProperty("generator-settings", ""));
        }
        WorldInfo worldInfo = new WorldInfo(worldSettings, name);
        WorldServer world = (dim == 0 ? overworld : (WorldServer)(new WorldServerMulti(mcServer, new AnvilSaveHandler(mcServer.server.getWorldContainer(), name, true, mcServer.getDataFixer()), dim, overworld, mcServer.profiler, worldInfo, env, gen).init()));

        mcServer.getPlayerList().setPlayerManager(mcServer.worldServerList.toArray(new WorldServer[mcServer.worldServerList.size()]));
        world.addEventListener(new ServerWorldEventHandler(mcServer, world));
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        mcServer.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(world.getWorld()));
        if (!mcServer.isSinglePlayer())
        {
            world.getWorldInfo().setGameType(mcServer.getGameType());
        }

        mcServer.setDifficultyForAllWorlds(mcServer.getDifficulty());
    }

    public static WorldServer getWorld(int id)
    {
        return getWorld(id, false);
    }

    public static WorldServer getWorld(int id, boolean resetUnloadDelay)
    {
        if (resetUnloadDelay && unloadQueue.contains(id))
        {
            dimensions.get(id).ticksWaited = 0;
        }
        return worlds.get(id);
    }

    public static WorldServer[] getWorlds()
    {
        return worlds.values().toArray(new WorldServer[0]);
    }

    static
    {
        init();
    }

    /**
     * Not public API: used internally to get dimensions that should load at
     * server startup
     */
    public static Integer[] getStaticDimensionIDs()
    {
        return dimensions.keySet().toArray(new Integer[0]);
    }
    public static WorldProvider createProviderFor(int dim)
    {
        try
        {
            if (dimensions.containsKey(dim))
            {
                WorldProvider ret = getProviderType(dim).createDimension();
                ret.setDimension(dim);
                return ret;
            }
            else
            {
                throw new RuntimeException(String.format("No WorldProvider bound for dimension %d", dim)); //It's going to crash anyway at this point.  Might as well be informative
            }
        }
        catch (Exception e)
        {
            FMLLog.log.error("An error occurred trying to create an instance of WorldProvider {} ({})",
                    dim, getProviderType(dim), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets if a dimension should stay loaded.
     * @param dim  the dimension ID
     * @param keep whether or not the dimension should be kept loaded
     * @return true iff the dimension's status changed
     */
    public static boolean keepDimensionLoaded(int dim, boolean keep)
    {
        return keep ? keepLoaded.add(dim) : keepLoaded.remove(dim);
    }

    private static boolean canUnloadWorld(WorldServer world)
    {
        return ForgeChunkManager.getPersistentChunksFor(world).isEmpty()
                && world.playerEntities.isEmpty()
                && !world.provider.getDimensionType().shouldLoadSpawn()
                && !keepLoaded.contains(world.provider.getDimension());
    }

    /**
     * Queues a dimension to unload, if it can be unloaded.
     * @param id The id of the dimension
     */
    public static void unloadWorld(int id)
    {
        WorldServer world = worlds.get(id);
        if (world == null || !canUnloadWorld(world)) return;

        if (unloadQueue.add(id))
        {
            FMLLog.log.debug("Queueing dimension {} to unload", id);;
        }
    }

    public static boolean isWorldQueuedToUnload(int id)
    {
        return unloadQueue.contains(id);
    }

    /*
     * To be called by the server at the appropriate time, do not call from mod code.
     */
    public static void unloadWorlds(Hashtable<Integer, long[]> worldTickTimes)
    {
        IntIterator queueIterator = unloadQueue.iterator();
        while (queueIterator.hasNext())
        {
            int id = queueIterator.nextInt();
            Dimension dimension = dimensions.get(id);
            if (dimension.ticksWaited < ForgeModContainer.dimensionUnloadQueueDelay)
            {
                dimension.ticksWaited++;
                continue;
            }
            WorldServer w = worlds.get(id);
            queueIterator.remove();
            dimension.ticksWaited = 0;
            // Don't unload the world if the status changed
            if (w == null || !canUnloadWorld(w))
            {
                FMLLog.log.debug("Aborting unload for dimension {} as status changed", id);
                continue;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().server.unloadWorld(w.getWorld(), true); // CatServer
        }
    }

    /**
     * Return the next free dimension ID. Note: you are not guaranteed a contiguous
     * block of free ids. Always call for each individual ID you wish to get.
     * @return the next free dimension ID
     */
    public static int getNextFreeDimId()
    {
        int next = lastUsedId;
        while (usedIds.contains(next) || !checkAvailable(next))
        {
            next++;
        }
        return lastUsedId = next;
    }

    private static boolean checkAvailable(int id)
    {
        if (dimensions.containsKey(id))
        {
            usedIds.add(id);
            return false;
        }
        return true;
    }

    public static NBTTagCompound saveDimensionDataMap()
    {
        NBTTagCompound dimMap = new NBTTagCompound();
        dimMap.setIntArray("UsedIDs", usedIds.toIntArray());
        return dimMap;
    }

    public static void loadDimensionDataMap(@Nullable NBTTagCompound compoundTag)
    {
        usedIds.clear();
        lastUsedId = 0;

        if (compoundTag == null)
        {
            IntIterator iterator = dimensions.keySet().iterator();
            while (iterator.hasNext())
            {
                int id = iterator.nextInt();
                if (id >= 0)
                {
                    usedIds.add(id);
                }
            }
        }
        else
        {
            for (int id : compoundTag.getIntArray("UsedIDs"))
            {
                usedIds.add(id);
            }

            // legacy data (load but don't save)
            int[] intArray = compoundTag.getIntArray("DimensionArray");
            for (int i = 0; i < intArray.length; i++)
            {
                int data = intArray[i];
                if (data == 0) continue;
                for (int j = 0; j < Integer.SIZE; j++)
                {
                    if ((data & (1 << j)) != 0) usedIds.add(i * Integer.SIZE + j);
                }
            }
        }
    }

    /**
     * Return the current root directory for the world save. Accesses getSaveHandler from the overworld
     * @return the root directory of the save
     */
    @Nullable
    public static File getCurrentSaveRootDirectory()
    {
        if (DimensionManager.getWorld(0) != null)
        {
            return DimensionManager.getWorld(0).getSaveHandler().getWorldDirectory();
        }/*
        else if (MinecraftServer.getServer() != null)
        {
            MinecraftServer srv = MinecraftServer.getServer();
            SaveHandler saveHandler = (SaveHandler) srv.getActiveAnvilConverter().getSaveLoader(srv.getFolderName(), false);
            return saveHandler.getWorldDirectory();
        }*/
        else
        {
            return null;
        }
    }

    // CatServer start - new method for handling creation of Bukkit dimensions. Currently supports MultiVerse
    public static WorldServer initDimension(WorldCreator creator, WorldSettings worldSettings) {
        WorldServer overworld = getWorld(0);
        if (overworld == null) {
            throw new RuntimeException("Cannot Hotload Dim: Overworld is not Loaded!");
        }

        MinecraftServer mcServer = overworld.getMinecraftServer();

        DimensionType type = DimensionType.OVERWORLD;
        try {
            if (creator.environment() != null)
                type = DimensionType.getById(creator.environment().getId());
        }
        catch (IllegalArgumentException e)
        {
            // do nothing
        }

        Environment env = creator.environment();
        String name = creator.name();
        int dim = 0;
        // Use saved dimension from level.dat if it exists. This guarantees that after a world is created, the same dimension will be used. Fixes issues with MultiVerse
        AnvilSaveHandler saveHandler = new AnvilSaveHandler(mcServer.server.getWorldContainer(), name, true, mcServer.getDataFixer());

        if (saveHandler.loadWorldInfo() != null)
        {
            int savedDim = saveHandler.loadWorldInfo().getDimension();
            if (savedDim != 0 && savedDim != -1 && savedDim != 1)
            {
                dim = savedDim;
            }
        }
        if (dim == 0)
        {
            dim = getNextFreeDimId();
        }

        if (!isDimensionRegistered(dim)) // handle reloads properly
        {
            registerDimension(dim, type);
            addBukkitDimension(dim);
        }
        ChunkGenerator gen = creator.generator();
        if (mcServer instanceof DedicatedServer) {
            worldSettings.setGeneratorOptions(((DedicatedServer) mcServer).getStringProperty("generator-settings", ""));
        }

        WorldInfo worldinfo = saveHandler.loadWorldInfo();
        if (worldinfo == null) {
            worldinfo = new WorldInfo(worldSettings,name);
        }
        worldinfo.setWorldName(name);

        WorldServer world = (WorldServer) new WorldServer(mcServer, saveHandler, worldinfo, dim, mcServer.profiler, env, gen).init();
        world.initialize(worldSettings);

        world.provider.setDimension(dim); // Fix for TerrainControl injecting their own WorldProvider
        mcServer.getPlayerList().setPlayerManager(mcServer.worldServerList.toArray(new WorldServer[mcServer.worldServerList.size()]));

        world.addEventListener(new ServerWorldEventHandler(mcServer, world));
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        if (!mcServer.isSinglePlayer())
        {
            world.getWorldInfo().setGameType(mcServer.getGameType());
        }

        return world;
    }

    public static Environment registerBukkitDimension(int dim, String worldType) {
        Environment env = Environment.getEnvironment(dim);
        if(env == null){
            worldType = worldType.replace("WorldProvider","");
            env = EnumHelper.addBukkitEnvironment(dim,worldType.toUpperCase());
            Environment.registerEnvironment(env);
        }
        return env;
    }

    public static void addBukkitDimension(int dim)
    {
        if (!bukkitDims.contains(dim))
            bukkitDims.add(dim);
    }

    public static void removeBukkitDimension(int dim)
    {
        if (bukkitDims.contains(dim))
            bukkitDims.remove(bukkitDims.indexOf(dim));
    }

    public static ArrayList<Integer> getBukkitDimensionIDs()
    {
        return bukkitDims;
    }

    public static boolean isBukkitDimension(int dim)
    {
        return bukkitDims.contains(dim);
    }
    // CatServer end
}
