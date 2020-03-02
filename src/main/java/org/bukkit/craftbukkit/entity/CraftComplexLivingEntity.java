package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLivingBase;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity {
    public CraftComplexLivingEntity(CraftServer server, EntityLivingBase entity) {
        super(server, entity);
    }

    @Override
    public EntityLivingBase getHandle() {
        return (EntityLivingBase) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
