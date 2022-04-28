package io.papermc.paper.event.world.border;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.NotNull;

public abstract class WorldBorderEvent extends WorldEvent {

    private final WorldBorder worldBorder;

    public WorldBorderEvent(@NotNull World world, @NotNull WorldBorder worldBorder) {
        super(world);
        this.worldBorder = worldBorder;
    }

    @NotNull
    public WorldBorder getWorldBorder() {
        return worldBorder;
    }
}
