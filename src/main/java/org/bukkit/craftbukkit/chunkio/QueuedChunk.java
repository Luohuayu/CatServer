package org.bukkit.craftbukkit.chunkio;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;

class QueuedChunk {
    final int x;
    final int z;
    final AnvilChunkLoader loader;
    final World world;
    final ChunkProviderServer provider;
    NBTTagCompound compound;

    public QueuedChunk(int x, int z, AnvilChunkLoader loader, World world, ChunkProviderServer provider) {
        this.x = x;
        this.z = z;
        this.loader = loader;
        this.world = world;
        this.provider = provider;
    }

    @Override
    public int hashCode() {
        return (x * 31 + z * 29) ^ world.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof QueuedChunk) {
            QueuedChunk other = (QueuedChunk) object;
            return x == other.x && z == other.z && world == other.world;
        }

        return false;
    }
}
