package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.PandaEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Panda;

public class CraftPanda extends CraftAnimals implements Panda {

    public CraftPanda(CraftServer server, PandaEntity entity) {
        super(server, entity);
    }

    @Override
    public PandaEntity getHandle() {
        return (PandaEntity) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.PANDA;
    }

    @Override
    public String toString() {
        return "CraftPanda";
    }

    @Override
    public Gene getMainGene() {
        return fromNms(getHandle().getMainGene());
    }

    @Override
    public void setMainGene(Gene gene) {
        getHandle().setMainGene(toNms(gene));
    }

    @Override
    public Gene getHiddenGene() {
        return fromNms(getHandle().getHiddenGene());
    }

    @Override
    public void setHiddenGene(Gene gene) {
        getHandle().setHiddenGene(toNms(gene));
    }

    public static Gene fromNms(PandaEntity.Gene gene) {
        Preconditions.checkArgument(gene != null, "Gene may not be null");

        return Gene.values()[gene.ordinal()];
    }

    public static PandaEntity.Gene toNms(Gene gene) {
        Preconditions.checkArgument(gene != null, "Gene may not be null");

        return PandaEntity.Gene.values()[gene.ordinal()];
    }
}
