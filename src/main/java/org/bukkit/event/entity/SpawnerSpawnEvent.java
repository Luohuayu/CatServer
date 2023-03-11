package org.bukkit.event.entity;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SpawnerSpawnEvent extends EntitySpawnEvent {

    private final CreatureSpawner spawner;

    public SpawnerSpawnEvent(@NotNull final Entity spawnEntity, @NotNull final CreatureSpawner spawner) {
        super(spawnEntity);
        this.spawner = spawner;
    }

    @NotNull
    public CreatureSpawner getSpawner() {
        return spawner;
    }
}
