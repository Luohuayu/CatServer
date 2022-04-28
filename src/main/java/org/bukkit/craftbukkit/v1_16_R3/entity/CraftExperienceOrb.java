package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.item.ExperienceOrbEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

public class CraftExperienceOrb extends CraftEntity implements ExperienceOrb {
    public CraftExperienceOrb(CraftServer server, ExperienceOrbEntity entity) {
        super(server, entity);
    }

    @Override
    public int getExperience() {
        return getHandle().value;
    }

    @Override
    public void setExperience(int value) {
        getHandle().value = value;
    }

    @Override
    public ExperienceOrbEntity getHandle() {
        return (ExperienceOrbEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftExperienceOrb";
    }

    @Override
    public EntityType getType() {
        return EntityType.EXPERIENCE_ORB;
    }
}
