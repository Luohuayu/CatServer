package org.bukkit.craftbukkit;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.spigotmc.*;
import java.util.*;
import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.*;
import org.bukkit.craftbukkit.scheduler.*;
import net.minecraft.world.World;

public class SpigotTimings
{
    public static final CustomTimingsHandler serverTickTimer;
    public static final CustomTimingsHandler playerListTimer;
    public static final CustomTimingsHandler connectionTimer;
    public static final CustomTimingsHandler tickablesTimer;
    public static final CustomTimingsHandler schedulerTimer;
    public static final CustomTimingsHandler chunkIOTickTimer;
    public static final CustomTimingsHandler timeUpdateTimer;
    public static final CustomTimingsHandler serverCommandTimer;
    public static final CustomTimingsHandler worldSaveTimer;
    public static final CustomTimingsHandler entityMoveTimer;
    public static final CustomTimingsHandler tickEntityTimer;
    public static final CustomTimingsHandler activatedEntityTimer;
    public static final CustomTimingsHandler tickTileEntityTimer;
    public static final CustomTimingsHandler timerEntityBaseTick;
    public static final CustomTimingsHandler timerEntityAI;
    public static final CustomTimingsHandler timerEntityAICollision;
    public static final CustomTimingsHandler timerEntityAIMove;
    public static final CustomTimingsHandler timerEntityTickRest;
    public static final CustomTimingsHandler processQueueTimer;
    public static final CustomTimingsHandler schedulerSyncTimer;
    public static final CustomTimingsHandler playerCommandTimer;
    public static final CustomTimingsHandler entityActivationCheckTimer;
    public static final CustomTimingsHandler checkIfActiveTimer;
    public static final HashMap<String, CustomTimingsHandler> entityTypeTimingMap;
    public static final HashMap<String, CustomTimingsHandler> tileEntityTypeTimingMap;
    public static final HashMap<String, CustomTimingsHandler> pluginTaskTimingMap;

    static {
        serverTickTimer = new CustomTimingsHandler("** Full Server Tick");
        playerListTimer = new CustomTimingsHandler("Player List");
        connectionTimer = new CustomTimingsHandler("Connection Handler");
        tickablesTimer = new CustomTimingsHandler("Tickables");
        schedulerTimer = new CustomTimingsHandler("Scheduler");
        chunkIOTickTimer = new CustomTimingsHandler("ChunkIOTick");
        timeUpdateTimer = new CustomTimingsHandler("Time Update");
        serverCommandTimer = new CustomTimingsHandler("Server Command");
        worldSaveTimer = new CustomTimingsHandler("World Save");
        entityMoveTimer = new CustomTimingsHandler("** entityMove");
        tickEntityTimer = new CustomTimingsHandler("** tickEntity");
        activatedEntityTimer = new CustomTimingsHandler("** activatedTickEntity");
        tickTileEntityTimer = new CustomTimingsHandler("** tickTileEntity");
        timerEntityBaseTick = new CustomTimingsHandler("** livingEntityBaseTick");
        timerEntityAI = new CustomTimingsHandler("** livingEntityAI");
        timerEntityAICollision = new CustomTimingsHandler("** livingEntityAICollision");
        timerEntityAIMove = new CustomTimingsHandler("** livingEntityAIMove");
        timerEntityTickRest = new CustomTimingsHandler("** livingEntityTickRest");
        processQueueTimer = new CustomTimingsHandler("processQueue");
        schedulerSyncTimer = new CustomTimingsHandler("** Scheduler - Sync Tasks", JavaPluginLoader.pluginParentTimer);
        playerCommandTimer = new CustomTimingsHandler("** playerCommand");
        entityActivationCheckTimer = new CustomTimingsHandler("entityActivationCheck");
        checkIfActiveTimer = new CustomTimingsHandler("** checkIfActive");
        entityTypeTimingMap = new HashMap<String, CustomTimingsHandler>();
        tileEntityTypeTimingMap = new HashMap<String, CustomTimingsHandler>();
        pluginTaskTimingMap = new HashMap<String, CustomTimingsHandler>();
    }

    public static CustomTimingsHandler getPluginTaskTimings(final BukkitTask task, final long period) {
        if (!task.isSync()) {
            return null;
        }
        final CraftTask ctask = (CraftTask)task;
        String plugin;
        if (task.getOwner() != null) {
            plugin = task.getOwner().getDescription().getFullName();
        }
        else if (ctask.timingName != null) {
            plugin = "CraftScheduler";
        }
        else {
            plugin = "Unknown";
        }
        final String taskname = ctask.getTaskName();
        String name = "Task: " + plugin + " Runnable: " + taskname;
        if (period > 0L) {
            name = String.valueOf(name) + "(interval:" + period + ")";
        }
        else {
            name = String.valueOf(name) + "(Single)";
        }
        CustomTimingsHandler result = SpigotTimings.pluginTaskTimingMap.get(name);
        if (result == null) {
            result = new CustomTimingsHandler(name, SpigotTimings.schedulerSyncTimer);
            SpigotTimings.pluginTaskTimingMap.put(name, result);
        }
        return result;
    }

    public static CustomTimingsHandler getEntityTimings(final Entity entity) {
        final String entityType = entity.getClass().getSimpleName();
        CustomTimingsHandler result = SpigotTimings.entityTypeTimingMap.get(entityType);
        if (result == null) {
            result = new CustomTimingsHandler("** tickEntity - " + entityType, SpigotTimings.activatedEntityTimer);
            SpigotTimings.entityTypeTimingMap.put(entityType, result);
        }
        return result;
    }

    public static CustomTimingsHandler getTileEntityTimings(final TileEntity entity) {
        final String entityType = entity.getClass().getSimpleName();
        CustomTimingsHandler result = SpigotTimings.tileEntityTypeTimingMap.get(entityType);
        if (result == null) {
            result = new CustomTimingsHandler("** tickTileEntity - " + entityType, SpigotTimings.tickTileEntityTimer);
            SpigotTimings.tileEntityTypeTimingMap.put(entityType, result);
        }
        return result;
    }

    public static class WorldTimingsHandler
    {
        public final CustomTimingsHandler mobSpawn;
        public final CustomTimingsHandler doChunkUnload;
        public final CustomTimingsHandler doPortalForcer;
        public final CustomTimingsHandler doTickPending;
        public final CustomTimingsHandler doTickTiles;
        public final CustomTimingsHandler doVillages;
        public final CustomTimingsHandler doChunkMap;
        public final CustomTimingsHandler doChunkGC;
        public final CustomTimingsHandler doSounds;
        public final CustomTimingsHandler entityTick;
        public final CustomTimingsHandler tileEntityTick;
        public final CustomTimingsHandler tileEntityPending;
        public final CustomTimingsHandler tracker;
        public final CustomTimingsHandler doTick;
        public final CustomTimingsHandler tickEntities;
        public final CustomTimingsHandler syncChunkLoadTimer;
        public final CustomTimingsHandler syncChunkLoadDataTimer;
        public final CustomTimingsHandler syncChunkLoadStructuresTimer;
        public final CustomTimingsHandler syncChunkLoadEntitiesTimer;
        public final CustomTimingsHandler syncChunkLoadTileEntitiesTimer;
        public final CustomTimingsHandler syncChunkLoadTileTicksTimer;
        public final CustomTimingsHandler syncChunkLoadPostTimer;

        public WorldTimingsHandler(final World server) {
            final String name = String.valueOf(server.getWorld().getName()) + " - ";
            this.mobSpawn = new CustomTimingsHandler("** " + name + "mobSpawn");
            this.doChunkUnload = new CustomTimingsHandler("** " + name + "doChunkUnload");
            this.doTickPending = new CustomTimingsHandler("** " + name + "doTickPending");
            this.doTickTiles = new CustomTimingsHandler("** " + name + "doTickTiles");
            this.doVillages = new CustomTimingsHandler("** " + name + "doVillages");
            this.doChunkMap = new CustomTimingsHandler("** " + name + "doChunkMap");
            this.doSounds = new CustomTimingsHandler("** " + name + "doSounds");
            this.doChunkGC = new CustomTimingsHandler("** " + name + "doChunkGC");
            this.doPortalForcer = new CustomTimingsHandler("** " + name + "doPortalForcer");
            this.entityTick = new CustomTimingsHandler("** " + name + "entityTick");
            this.tileEntityTick = new CustomTimingsHandler("** " + name + "tileEntityTick");
            this.tileEntityPending = new CustomTimingsHandler("** " + name + "tileEntityPending");
            this.syncChunkLoadTimer = new CustomTimingsHandler("** " + name + "syncChunkLoad");
            this.syncChunkLoadDataTimer = new CustomTimingsHandler("** " + name + "syncChunkLoad - Data");
            this.syncChunkLoadStructuresTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Structures");
            this.syncChunkLoadEntitiesTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Entities");
            this.syncChunkLoadTileEntitiesTimer = new CustomTimingsHandler("** " + name + "chunkLoad - TileEntities");
            this.syncChunkLoadTileTicksTimer = new CustomTimingsHandler("** " + name + "chunkLoad - TileTicks");
            this.syncChunkLoadPostTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Post");
            this.tracker = new CustomTimingsHandler(String.valueOf(name) + "tracker");
            this.doTick = new CustomTimingsHandler(String.valueOf(name) + "doTick");
            this.tickEntities = new CustomTimingsHandler(String.valueOf(name) + "tickEntities");
        }
    }
}
