package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Set;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.boss.CraftDragonBattle;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {

    public CraftEnderDragon(CraftServer server, EnderDragonEntity entity) {
        super(server, entity);
    }

    @Override
    public Set<ComplexEntityPart> getParts() {
        Builder<ComplexEntityPart> builder = ImmutableSet.builder();

        for (EnderDragonPartEntity part : getHandle().subEntities) {
            builder.add((ComplexEntityPart) part.getBukkitEntity());
        }

        return builder.build();
    }

    @Override
    public EnderDragonEntity getHandle() {
        return (EnderDragonEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragon";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public Phase getPhase() {
        // LoliServer start
        int id = getHandle().getEntityData().get(EnderDragonEntity.DATA_PHASE);
        return id >= 0 && id < Phase.CUSTOM.ordinal() ? Phase.values()[id] : Phase.CUSTOM;
        // LoliServer end
    }

    @Override
    public void setPhase(Phase phase) {
        getHandle().getPhaseManager().setPhase(getMinecraftPhase(phase));
    }

    public static Phase getBukkitPhase(PhaseType phase) {
        // LoliServer start
        int id = phase.getId();
        return id >= 0 && id < Phase.CUSTOM.ordinal() ? Phase.values()[id] : Phase.CUSTOM;
        // LoliServer end
    }

    public static PhaseType getMinecraftPhase(Phase phase) {
        return PhaseType.getById(phase.ordinal());
    }

    @Override
    public BossBar getBossBar() {
        return getDragonBattle().getBossBar();
    }

    @Override
    public DragonBattle getDragonBattle() {
        return new CraftDragonBattle(getHandle().getDragonFight());
    }

    @Override
    public int getDeathAnimationTicks() {
        return getHandle().dragonDeathTime; // PAIL rename deathAnimationTicks
    }
}
