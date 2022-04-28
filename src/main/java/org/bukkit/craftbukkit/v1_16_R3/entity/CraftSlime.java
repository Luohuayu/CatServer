package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.SlimeEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;

public class CraftSlime extends CraftMob implements Slime {

    public CraftSlime(CraftServer server, SlimeEntity entity) {
        super(server, entity);
    }

    @Override
    public int getSize() {
        return getHandle().getSize();
    }

    @Override
    public void setSize(int size) {
        getHandle().setSize(size, true);
    }

    @Override
    public SlimeEntity getHandle() {
        return (SlimeEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSlime";
    }

    @Override
    public EntityType getType() {
        return EntityType.SLIME;
    }
}
