package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockEntityState<TileEntityMobSpawner> implements CreatureSpawner {

    public CraftCreatureSpawner(final Block block) {
        super(block, TileEntityMobSpawner.class);
    }

    public CraftCreatureSpawner(final Material material, TileEntityMobSpawner te) {
        super(material, te);
    }

    @Override
    public EntityType getSpawnedType() {
        ResourceLocation key = this.getSnapshot().getSpawnerBaseLogic().getEntityId();
        return (key == null) ? EntityType.PIG : EntityType.fromName(key.getResourcePath());
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }

        this.getSnapshot().getSpawnerBaseLogic().setEntityId(new ResourceLocation(entityType.getName()));
    }

    @Override
    public String getCreatureTypeName() {
        return this.getSnapshot().getSpawnerBaseLogic().getEntityId().getResourcePath();
    }

    @Override
    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        setSpawnedType(type);
    }

    @Override
    public int getDelay() {
        return this.getSnapshot().getSpawnerBaseLogic().spawnDelay;
    }

    @Override
    public void setDelay(int delay) {
        this.getSnapshot().getSpawnerBaseLogic().spawnDelay = delay;
    }

    @Override
    public int getMinSpawnDelay() {
        return this.getSnapshot().getSpawnerBaseLogic().minSpawnDelay;
    }

    @Override
    public void setMinSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay <= getMaxSpawnDelay(), "Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        this.getSnapshot().getSpawnerBaseLogic().minSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return this.getSnapshot().getSpawnerBaseLogic().maxSpawnDelay;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay > 0, "Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument(spawnDelay >= getMinSpawnDelay(), "Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
        this.getSnapshot().getSpawnerBaseLogic().maxSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxNearbyEntities() {
        return this.getSnapshot().getSpawnerBaseLogic().maxNearbyEntities;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        this.getSnapshot().getSpawnerBaseLogic().maxNearbyEntities = maxNearbyEntities;
    }

    @Override
    public int getSpawnCount() {
        return this.getSnapshot().getSpawnerBaseLogic().spawnCount;
    }

    @Override
    public void setSpawnCount(int count) {
        this.getSnapshot().getSpawnerBaseLogic().spawnCount = count;
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.getSnapshot().getSpawnerBaseLogic().activatingRangeFromPlayer;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.getSnapshot().getSpawnerBaseLogic().activatingRangeFromPlayer = requiredPlayerRange;
    }

    @Override
    public int getSpawnRange() {
        return this.getSnapshot().getSpawnerBaseLogic().spawnRange;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        this.getSnapshot().getSpawnerBaseLogic().spawnRange = spawnRange;
    }
}
