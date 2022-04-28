package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.monster.SpiderEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;

public class CraftSpider extends CraftMonster implements Spider {

    public CraftSpider(CraftServer server, SpiderEntity entity) {
        super(server, entity);
    }

    @Override
    public SpiderEntity getHandle() {
        return (SpiderEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSpider";
    }

    @Override
    public EntityType getType() {
        return EntityType.SPIDER;
    }
}
