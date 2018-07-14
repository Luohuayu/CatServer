// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.chunkio;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

class QueuedChunk
{
    final int x;
    final int z;
    final AnvilChunkLoader loader;
    final World world;
    final ChunkProviderServer provider;
    NBTTagCompound compound;
    
    public QueuedChunk(final int x, final int z, final AnvilChunkLoader loader, final World world, final ChunkProviderServer provider) {
        this.x = x;
        this.z = z;
        this.loader = loader;
        this.world = world;
        this.provider = provider;
    }
    
    @Override
    public int hashCode() {
        return this.x * 31 + this.z * 29 ^ this.world.hashCode();
    }
    
    @Override
    public boolean equals(final Object object) {
        if (object instanceof QueuedChunk) {
            final QueuedChunk other = (QueuedChunk)object;
            return this.x == other.x && this.z == other.z && this.world == other.world;
        }
        return false;
    }
}
