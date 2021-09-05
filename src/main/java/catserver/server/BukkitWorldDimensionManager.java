package catserver.server;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class BukkitWorldDimensionManager {
    private static Map<String, Integer> bukkitWorldDimIds = new LinkedHashMap<>();

    public static int getWorldDimId(String name) {
        return bukkitWorldDimIds.getOrDefault(name, 0);
    }

    public static void putWorldDimId(String name, int id) {
        bukkitWorldDimIds.values().removeIf(loadedId -> loadedId == id);
        bukkitWorldDimIds.put(name, id);
    }

    public static void load(NBTTagCompound tagCompound) {
        if (CatServer.getConfig().saveBukkitWorldDimensionId) {
            if (tagCompound.hasKey("bukkitWorldDimIds")) {
                NBTTagList tagList = tagCompound.getTagList("bukkitWorldDimIds", 10);
                bukkitWorldDimIds.clear();
                for (NBTBase tag : tagList) {
                    if (tag instanceof NBTTagCompound) {
                        bukkitWorldDimIds.put(((NBTTagCompound) tag).getString("name"), ((NBTTagCompound) tag).getInteger("id"));
                    }
                }
            }
        }
    }

    public static void save(NBTTagCompound tagCompound) {
        if (CatServer.getConfig().saveBukkitWorldDimensionId) {
            NBTTagList tagList = new NBTTagList();
            bukkitWorldDimIds.forEach((k, v) -> {
                NBTTagCompound tagCompound1 = new NBTTagCompound();
                tagCompound1.setString("name", k);
                tagCompound1.setInteger("id", v);
                tagList.appendTag(tagCompound1);
            });
            tagCompound.setTag("bukkitWorldDimIds", tagList);
        }
    }
}
