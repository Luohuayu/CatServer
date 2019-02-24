package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EntityMob;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster {

    public CraftMonster(CraftServer server, EntityMob entity) {
        super(server, entity);
    }

    @Override
    public EntityMob getHandle() {
        return (EntityMob) entity;
    }

    @Override
    public String toString() {
        return "CraftMonster{name=" + this.entityName + "}"; // CatServer
    }
}
