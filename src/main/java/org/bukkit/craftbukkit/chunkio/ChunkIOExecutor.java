// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.chunkio;

import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.bukkit.craftbukkit.util.AsynchronousExecutor;

public class ChunkIOExecutor
{
    static final int BASE_THREADS = 1;
    static final int PLAYERS_PER_THREAD = 50;
    private static final AsynchronousExecutor<QueuedChunk, Chunk, Runnable, RuntimeException> instance;
    
    static {
        instance = new AsynchronousExecutor<QueuedChunk, Chunk, Runnable, RuntimeException>(new ChunkIOProvider(), 1);
    }
    
    public static Chunk syncChunkLoad(final World world, final AnvilChunkLoader loader, final ChunkProviderServer provider, final int x, final int z) {
        try {
			return ChunkIOExecutor.instance.getSkipQueue(new QueuedChunk(x, z, loader, world, provider));
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
        return null;
    }
    
    public static void queueChunkLoad(final World world, final AnvilChunkLoader loader, final ChunkProviderServer provider, final int x, final int z, final Runnable runnable) {
        ChunkIOExecutor.instance.add(new QueuedChunk(x, z, loader, world, provider), runnable);
    }
    
    public static void dropQueuedChunkLoad(final World world, final int x, final int z, final Runnable runnable) {
        ChunkIOExecutor.instance.drop(new QueuedChunk(x, z, null, world, null), runnable);
    }
    
    public static void adjustPoolSize(final int players) {
        final int size = Math.max(1, (int)Math.ceil(players / 50));
        ChunkIOExecutor.instance.setActiveThreads(size);
    }
    
    public static void tick() {
        try {
			ChunkIOExecutor.instance.finishActive();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
}
