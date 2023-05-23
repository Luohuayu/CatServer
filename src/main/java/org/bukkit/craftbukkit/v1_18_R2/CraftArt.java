package org.bukkit.craftbukkit.v1_18_R2;

import catserver.server.BukkitInjector;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Motive;
import org.bukkit.Art;
import org.bukkit.NamespacedKey;

public class CraftArt {
    private static final BiMap<Motive, Art> artwork;

    static {
        ImmutableBiMap.Builder<Motive, Art> artworkBuilder = ImmutableBiMap.builder();
        for (ResourceLocation key : Registry.MOTIVE.keySet()) {
            // CatServer start
            if (key.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                artworkBuilder.put(Registry.MOTIVE.get(key), Art.getByName(key.getPath()));
            } else {
                BukkitInjector.artMap.forEach(artworkBuilder::put);
            }
            // CatServer end
        }

        artwork = artworkBuilder.build();
    }

    public static Art NotchToBukkit(Motive art) {
        Art bukkit = artwork.get(art);
        Preconditions.checkArgument(bukkit != null);
        return bukkit;
    }

    public static Motive BukkitToNotch(Art art) {
        Motive nms = artwork.inverse().get(art);
        Preconditions.checkArgument(nms != null);
        return nms;
    }
}
