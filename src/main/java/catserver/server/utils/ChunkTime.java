package catserver.server.utils;

import net.minecraft.world.chunk.Chunk;

public class ChunkTime {
    public final Chunk chunk;
    public final long time;

    public ChunkTime(Chunk chunk, long time) {
        this.chunk = chunk;
        this.time = time;
    }
}
