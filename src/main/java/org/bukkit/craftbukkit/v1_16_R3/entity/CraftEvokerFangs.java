package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.EvokerFangsEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;

public class CraftEvokerFangs extends CraftEntity implements EvokerFangs {

    public CraftEvokerFangs(CraftServer server, EvokerFangsEntity entity) {
        super(server, entity);
    }

    @Override
    public EvokerFangsEntity getHandle() {
        return (EvokerFangsEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEvokerFangs";
    }

    @Override
    public EntityType getType() {
        return EntityType.EVOKER_FANGS;
    }

    @Override
    public LivingEntity getOwner() {
        net.minecraft.entity.LivingEntity owner = getHandle().getOwner();

        return (owner == null) ? null : (LivingEntity) owner.getBukkitEntity();
    }

    @Override
    public void setOwner(LivingEntity owner) {
        getHandle().setOwner(owner == null ? null : ((CraftLivingEntity) owner).getHandle());
    }
}
