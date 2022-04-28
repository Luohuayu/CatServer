package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.CraftArt;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, PaintingEntity entity) {
        super(server, entity);
    }

    @Override
    public Art getArt() {
        PaintingType art = getHandle().motive;
        return CraftArt.NotchToBukkit(art);
    }

    @Override
    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    @Override
    public boolean setArt(Art art, boolean force) {
        PaintingEntity painting = this.getHandle();
        PaintingType oldArt = painting.motive;
        painting.motive = CraftArt.BukkitToNotch(art);
        painting.setDirection(painting.getDirection());
        if (!force && !painting.survives()) {
            // Revert painting since it doesn't fit
            painting.motive = oldArt;
            painting.setDirection(painting.getDirection());
            return false;
        }
        this.update();
        return true;
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            update();
            return true;
        }

        return false;
    }

    @Override
    public PaintingEntity getHandle() {
        return (PaintingEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + getArt() + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
