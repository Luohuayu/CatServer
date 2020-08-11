package catserver.server.command.internal;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandChunkStats extends Command {
    private static Map<Chunk, Long> chunks = new HashMap<>();
    private static boolean enable = false;
    private static long lastNanoTime = 0;

    public CommandChunkStats(String name) {
        super(name);
        this.description = "Chunk Stats Command";
        this.usageMessage = "/chunkstats start/stop";
        setPermission("catserver.command.chunkstats");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1) return false;
        if (args[0].equals("start")) {
            chunks = new HashMap<>();
            lastNanoTime = 0;
            enable = true;

            sender.sendMessage("Chunk stats started!");

            return true;
        } else if (args[0].equals("stop")) {
            if (enable) {
                sender.sendMessage("Please wait for analyzing..");

                List<ChunkTime> chunkList = new ArrayList<>();
                for (int i = 0; i < 10 ; i++) {
                    Chunk hight = null;
                    long t = 0;
                    for (Chunk chunkPos : chunks.keySet()) {
                        if (hight == null) {
                            hight = chunkPos;
                            t = chunks.get(chunkPos);
                            continue;
                        }
                        long tt = chunks.get(chunkPos);
                        if (tt > t) {
                            hight = chunkPos;
                            t = tt;
                        }
                    }
                    chunkList.add(new ChunkTime(hight, t));
                    chunks.remove(hight);
                }
                chunks.clear();

                sender.sendMessage("Chunks Time:");
                for (ChunkTime chunkTime : chunkList) {
                    sender.sendMessage(String.format("[%s: %d, %d] has running time: %d ms", chunkTime.chunk.world.getWorld().getName(), chunkTime.chunk.x, chunkTime.chunk.z, chunkTime.time / 1000));
                }
            } else {
                sender.sendMessage("Not start!");
            }

            return true;
        } else {
            return false;
        }
    }

    public static void onTickStart() {
        if (!enable) return;
        lastNanoTime = System.nanoTime();
    }

    public static void onTickEnd(World world, BlockPos pos) {
        if (!enable) return;

        Chunk chunk = world.getChunkIfLoaded(pos.getX() >> 4, pos.getZ() >> 4);

        if (chunk != null && lastNanoTime > 0) {
            Long oldTime = chunks.getOrDefault(chunk, 0L);
            oldTime += System.nanoTime() - lastNanoTime;
            chunks.put(chunk, oldTime);
        }
    }

    static class ChunkTime {
        public final Chunk chunk;
        public final long time;

        public ChunkTime(Chunk chunk, long time) {
            this.chunk = chunk;
            this.time = time;
        }
    }
}
