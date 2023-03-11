/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public final class CraftCaveVinesPlant extends CraftBlockData implements org.bukkit.block.data.type.CaveVinesPlant {

    public CraftCaveVinesPlant() {
        super();
    }

    public CraftCaveVinesPlant(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCaveVinesPlant

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty BERRIES = getBoolean(net.minecraft.world.level.block.CaveVinesPlantBlock.class, "berries");

    @Override
    public boolean isBerries() {
        return get(BERRIES);
    }

    @Override
    public void setBerries(boolean berries) {
        set(BERRIES, berries);
    }
}
