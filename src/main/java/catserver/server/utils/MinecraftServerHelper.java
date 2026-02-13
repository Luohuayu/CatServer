package catserver.server.utils;

import joptsimple.OptionSet;
import net.minecraft.server.WorldLoader;

public class MinecraftServerHelper {
    private static MinecraftServerHelper instance = new MinecraftServerHelper();
    private OptionSet optionSet;
    private WorldLoader.DataLoadContext worldLoader; // 1.20.1

    public void load(OptionSet optionSet, WorldLoader.DataLoadContext worldLoader) {
        this.optionSet = optionSet;
        this.worldLoader = worldLoader;
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }

    public WorldLoader.DataLoadContext getWorldLoader() {
        return worldLoader;
    }

    public static MinecraftServerHelper getInstance() {
        return instance;
    }
}
