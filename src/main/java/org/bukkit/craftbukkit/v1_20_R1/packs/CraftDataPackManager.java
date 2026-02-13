package org.bukkit.craftbukkit.v1_20_R1.packs;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.packs.DataPack;
import org.bukkit.packs.DataPackManager;

public class CraftDataPackManager implements DataPackManager {

    private final PackRepository handle;

    public CraftDataPackManager(PackRepository resourcePackRepository) {
        this.handle = resourcePackRepository;
    }

    public PackRepository getHandle() {
        return this.handle;
    }

    @Override
    public Collection<DataPack> getDataPacks() {
        // Based in the command for datapacks need reload for get the updated list of datapacks
        this.getHandle().reload();

        Collection<Pack> availablePacks = this.getHandle().getAvailablePacks();
        return availablePacks.stream().map(CraftDataPack::new).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public DataPack getDataPack(NamespacedKey namespacedKey) {
        Preconditions.checkArgument(namespacedKey != null, "namespacedKey cannot be null");

        return new CraftDataPack(this.getHandle().getPack(namespacedKey.getKey()));
    }

    @Override
    public Collection<DataPack> getEnabledDataPacks(World world) {
        Preconditions.checkArgument(world != null, "world cannot be null");

        CraftWorld craftWorld = ((CraftWorld) world);
        return craftWorld.getHandle().worldDataServer.getDataConfiguration().dataPacks().getEnabled().stream().map(packName -> { // CatServer - serverLevelData -> worldDataServer
            Pack resourcePackLoader = this.getHandle().getPack(packName);
            if (resourcePackLoader != null) {
                return new CraftDataPack(resourcePackLoader);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<DataPack> getDisabledDataPacks(World world) {
        Preconditions.checkArgument(world != null, "world cannot be null");

        CraftWorld craftWorld = ((CraftWorld) world);
        return craftWorld.getHandle().worldDataServer.getDataConfiguration().dataPacks().getDisabled().stream().map(packName -> { // CatServer - serverLevelData -> worldDataServer
            Pack resourcePackLoader = this.getHandle().getPack(packName);
            if (resourcePackLoader != null) {
                return new CraftDataPack(resourcePackLoader);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isEnabledByFeature(Material material, World world) {
        Preconditions.checkArgument(material != null, "material cannot be null");
        Preconditions.checkArgument(material.isItem() || material.isBlock(), "material need to be a item or block");
        Preconditions.checkArgument(world != null, "world cannot be null");

        CraftWorld craftWorld = ((CraftWorld) world);
        if (material.isItem()) {
            return CraftMagicNumbers.getItem(material).isEnabled(craftWorld.getHandle().enabledFeatures());
        } else if (material.isBlock()) {
            return CraftMagicNumbers.getBlock(material).isEnabled(craftWorld.getHandle().enabledFeatures());
        }
        return false;
    }

    @Override
    public boolean isEnabledByFeature(EntityType entityType, World world) {
        Preconditions.checkArgument(entityType != null, "entityType cannot be null");
        Preconditions.checkArgument(world != null, "world cannot be null");
        Preconditions.checkArgument(entityType != EntityType.UNKNOWN, "EntityType.UNKNOWN its not allowed here");

        CraftWorld craftWorld = ((CraftWorld) world);
        net.minecraft.world.entity.EntityType<?> nmsEntity = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(entityType.getKey().getKey()));
        return nmsEntity.isEnabled(craftWorld.getHandle().enabledFeatures());
    }
}
