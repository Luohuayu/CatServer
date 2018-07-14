// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.chunkio;

import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.world.chunk.Chunk;
import org.bukkit.craftbukkit.util.AsynchronousExecutor;

class ChunkIOProvider implements AsynchronousExecutor.CallBackProvider<QueuedChunk, Chunk, Runnable, RuntimeException>
{
    private final AtomicInteger threadNumber;
    
    ChunkIOProvider() {
        this.threadNumber = new AtomicInteger(1);
    }
    
    @Override
    public Chunk callStage1(final QueuedChunk queuedChunk) throws RuntimeException {
        try {
            final AnvilChunkLoader loader = queuedChunk.loader;
            final Object[] data = loader.loadChunk__Async(queuedChunk.world, queuedChunk.x, queuedChunk.z);
            if (data != null) {
                queuedChunk.compound = (NBTTagCompound)data[1];
                return (Chunk)data[0];
            }
            return null;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void callStage2(final QueuedChunk queuedChunk, final Chunk chunk) throws RuntimeException {
        if (chunk == null) {
            queuedChunk.provider.getLoadedChunk(queuedChunk.x, queuedChunk.z);
            return;
        }
        queuedChunk.loader.loadEntities(queuedChunk.world, queuedChunk.compound.getCompoundTag("Level"), chunk);
        chunk.setLastSaveTime(queuedChunk.provider.worldObj.getTotalWorldTime());
        queuedChunk.provider.id2ChunkMap.put(ChunkPos.asLong(queuedChunk.x, queuedChunk.z), /*(Object)*/chunk);
        chunk.onChunkLoad();
        if (queuedChunk.provider.chunkGenerator != null) {
            queuedChunk.provider.chunkGenerator.recreateStructures(chunk, queuedChunk.x, queuedChunk.z);
        }
        chunk.loadNearby(queuedChunk.provider, queuedChunk.provider.chunkGenerator, false);
    }
    
    @Override
    public void callStage3(final QueuedChunk queuedChunk, final Chunk chunk, final Runnable runnable) throws RuntimeException {
        runnable.run();
    }
    
    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = new Thread(runnable, "Chunk I/O Executor Thread-" + this.threadNumber.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
