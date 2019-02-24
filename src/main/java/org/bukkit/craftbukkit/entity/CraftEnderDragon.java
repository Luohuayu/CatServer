package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import java.util.Set;

import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {
    public CraftEnderDragon(CraftServer server, EntityDragon entity) {
        super(server, entity);
    }

    public Set<ComplexEntityPart> getParts() {
        Builder<ComplexEntityPart> builder = ImmutableSet.builder();

        for (MultiPartEntityPart part : getHandle().dragonPartArray) {
            builder.add((ComplexEntityPart) part.getBukkitEntity());
        }

        return builder.build();
    }

    @Override
    public EntityDragon getHandle() {
        return (EntityDragon) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragon";
    }

    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public Phase getPhase() {
        return Phase.values()[getHandle().getDataManager().get(EntityDragon.PHASE)];
    }

    @Override
    public void setPhase(Phase phase) {
        getHandle().getPhaseManager().setPhase(getMinecraftPhase(phase));
    }
    
    public static Phase getBukkitPhase(PhaseList phase) {
        return Phase.values()[phase.getId()];
    }
    
    public static PhaseList getMinecraftPhase(Phase phase) {
        return PhaseList.getById(phase.ordinal());
    }
}
