package org.bukkit.craftbukkit.v1_18_R2.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Entity;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.boss.CraftBossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;

public class CraftWither extends CraftMonster implements Wither {

    private BossBar bossBar;

    public CraftWither(CraftServer server, net.minecraft.world.entity.boss.wither.WitherBoss entity) {
        super(server, entity);

        if (entity.bossEvent != null) {
            this.bossBar = new CraftBossBar(entity.bossEvent);
        }
    }

    @Override
    public net.minecraft.world.entity.boss.wither.WitherBoss getHandle() {
        return (net.minecraft.world.entity.boss.wither.WitherBoss) entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER;
    }

    @Override
    public BossBar getBossBar() {
        return bossBar;
    }

    @Override
    public void setTarget(Head head, LivingEntity livingEntity) {
        Preconditions.checkArgument(head != null, "head cannot be null");

        int entityId = (livingEntity != null) ? livingEntity.getEntityId() : 0;
        getHandle().setAlternativeTarget(head.ordinal(), entityId);
    }

    @Override
    public LivingEntity getTarget(Head head) {
        Preconditions.checkArgument(head != null, "head cannot be null");

        int entityId = getHandle().getAlternativeTarget(head.ordinal());
        if (entityId == 0) {
            return null;
        }
        Entity target = getHandle().getLevel().getEntity(entityId);
        return (target != null) ? (LivingEntity) target.getBukkitEntity() : null;
    }
}
