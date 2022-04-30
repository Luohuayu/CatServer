package catserver.server.utils;

public class BukkitWorldSetter {
    private static final ThreadLocal<BukkitWorldSetter> threadLocal = new ThreadLocal<>();
    public static BukkitWorldSetter get() {
        BukkitWorldSetter currentThreadObject = threadLocal.get();
        if (currentThreadObject == null) {
            currentThreadObject = new BukkitWorldSetter();
            threadLocal.set(currentThreadObject);
        }
        return currentThreadObject;
    }

    private org.bukkit.generator.ChunkGenerator generator;
    private org.bukkit.World.Environment environment;

    public void setWorld(org.bukkit.generator.ChunkGenerator gen, org.bukkit.World.Environment env) {
        generator = gen;
        environment = env;
    }

    public org.bukkit.generator.ChunkGenerator getChunkGenerator() {
        return generator;
    }

    public org.bukkit.World.Environment getEnvironment() {
        return environment;
    }

    public void reset() {
        generator = null;
        environment = null;
    }
}
