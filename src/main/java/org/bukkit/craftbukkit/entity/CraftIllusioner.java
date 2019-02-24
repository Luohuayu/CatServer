package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EntityIllusionIllager;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Illusioner;

public class CraftIllusioner extends CraftSpellcaster implements Illusioner {

    public CraftIllusioner(CraftServer server, EntityIllusionIllager entity) {
        super(server, entity);
    }

    @Override
    public EntityIllusionIllager getHandle() {
        return (EntityIllusionIllager) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftIllusioner";
    }

    @Override
    public EntityType getType() {
        return EntityType.ILLUSIONER;
    }
}
