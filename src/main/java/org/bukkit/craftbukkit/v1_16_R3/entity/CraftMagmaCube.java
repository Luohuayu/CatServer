package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.MagmaCubeEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftSlime implements MagmaCube {

    public CraftMagmaCube(CraftServer server, MagmaCubeEntity entity) {
        super(server, entity);
    }

    @Override
    public MagmaCubeEntity getHandle() {
        return (MagmaCubeEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftMagmaCube";
    }

    @Override
    public EntityType getType() {
        return EntityType.MAGMA_CUBE;
    }
}
