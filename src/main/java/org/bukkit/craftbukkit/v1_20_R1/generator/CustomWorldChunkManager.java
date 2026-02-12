package org.bukkit.craftbukkit.v1_20_R1.generator;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.BiomeSource;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlock;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

public class CustomWorldChunkManager extends BiomeSource {

    private final WorldInfo worldInfo;
    private final BiomeProvider biomeProvider;
    private final Registry<net.minecraft.world.level.biome.Biome> registry;

    private static List<Holder<net.minecraft.world.level.biome.Biome>> biomeListToBiomeBaseList(List<Biome> biomes, Registry<net.minecraft.world.level.biome.Biome> registry) {
        List<Holder<net.minecraft.world.level.biome.Biome>> biomeBases = new ArrayList<>();

        for (Biome biome : biomes) {
            Preconditions.checkArgument(biome != Biome.CUSTOM, "Cannot use the biome %s", biome);
            biomeBases.add(CraftBlock.biomeToBiomeBase(registry, biome));
        }

        return biomeBases;
    }

    public CustomWorldChunkManager(WorldInfo worldInfo, BiomeProvider biomeProvider, Registry<net.minecraft.world.level.biome.Biome> registry) {
        this.worldInfo = worldInfo;
        this.biomeProvider = biomeProvider;
        this.registry = registry;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        throw new UnsupportedOperationException("Cannot serialize CustomWorldChunkManager");
    }

    @Override
    public Holder<net.minecraft.world.level.biome.Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        Biome biome = biomeProvider.getBiome(worldInfo, x << 2, y << 2, z << 2, CraftBiomeParameterPoint.createBiomeParameterPoint(sampler, sampler.sample(x, y, z)));
        Preconditions.checkArgument(biome != Biome.CUSTOM, "Cannot set the biome to %s", biome);

        return CraftBlock.biomeToBiomeBase(registry, biome);
    }

    @Override
    protected Stream<Holder<net.minecraft.world.level.biome.Biome>> collectPossibleBiomes() {
        return biomeListToBiomeBaseList(biomeProvider.getBiomes(worldInfo), registry).stream();
    }
}
