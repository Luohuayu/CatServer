// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.boss.dragon.phase.PhaseList;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.boss.EntityDragonPart;
import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.ComplexEntityPart;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderDragon;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon
{
    public CraftEnderDragon(final CraftServer server, final EntityDragon entity) {
        super(server, entity);
    }
    
    @Override
    public Set<ComplexEntityPart> getParts() {
        final ImmutableSet.Builder<ComplexEntityPart> builder = /*(ImmutableSet.Builder<ComplexEntityPart>)*/ImmutableSet.builder();
        EntityDragonPart[] dragonPartArray;
        for (int length = (dragonPartArray = this.getHandle().dragonPartArray).length, i = 0; i < length; ++i) {
            final EntityDragonPart part = dragonPartArray[i];
            builder.add((ComplexEntityPart) /*(Object)*/part.getBukkitEntity());
        }
        return (Set<ComplexEntityPart>)builder.build();
    }
    
    @Override
    public EntityDragon getHandle() {
        return (EntityDragon)this.entity;
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
        return Phase.values()[this.getHandle().getDataManager().get(EntityDragon.PHASE)];
    }
    
    @Override
    public void setPhase(final Phase phase) {
        this.getHandle().getPhaseManager().setPhase(getMinecraftPhase(phase));
    }
    
    public static Phase getBukkitPhase(final PhaseList phase) {
        return Phase.values()[phase.getId()];
    }
    
    public static PhaseList getMinecraftPhase(final Phase phase) {
        return PhaseList.getById(phase.ordinal());
    }
}
