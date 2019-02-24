package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.AbstractIllager;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Illager;

public class CraftIllager extends CraftMonster implements Illager {

    public CraftIllager(CraftServer server, AbstractIllager entity) {
        super(server, entity);
    }

    @Override
    public AbstractIllager getHandle() {
        return (AbstractIllager) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftIllager";
    }
}
