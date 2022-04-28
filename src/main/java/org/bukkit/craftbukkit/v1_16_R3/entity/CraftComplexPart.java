package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

public class CraftComplexPart extends CraftEntity implements ComplexEntityPart {
    public CraftComplexPart(CraftServer server, EnderDragonPartEntity entity) {
        super(server, entity);
    }

    @Override
    public ComplexLivingEntity getParent() {
        return (ComplexLivingEntity) getHandle().parentMob.getBukkitEntity();
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent cause) {
        getParent().setLastDamageCause(cause);
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return getParent().getLastDamageCause();
    }

    @Override
    public boolean isValid() {
        return getParent().isValid();
    }

    @Override
    public EnderDragonPartEntity getHandle() {
        return (EnderDragonPartEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexPart";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
