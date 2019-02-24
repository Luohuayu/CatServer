package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

public class CraftComplexPart extends CraftEntity implements ComplexEntityPart {
    public CraftComplexPart(CraftServer server, MultiPartEntityPart entity) {
        super(server, entity);
    }

    public ComplexLivingEntity getParent() {
        return (ComplexLivingEntity) ((EntityDragon) getHandle().parent).getBukkitEntity();
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
    public MultiPartEntityPart getHandle() {
        return (MultiPartEntityPart) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexPart";
    }

    public EntityType getType() {
        return EntityType.COMPLEX_PART;
    }
}
