package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.world.WorldServer;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, EntityPainting entity) {
        super(server, entity);
    }

    public Art getArt() {
        EnumArt art = getHandle().art;
        return CraftArt.NotchToBukkit(art);
    }

    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    public boolean setArt(Art art, boolean force) {
        EntityPainting painting = this.getHandle();
        EnumArt oldArt = painting.art;
        painting.art = CraftArt.BukkitToNotch(art);
        painting.updateFacingWithBoundingBox(painting.facingDirection);
        if (!force && !painting.onValidSurface()) {
            // Revert painting since it doesn't fit
            painting.art = oldArt;
            painting.updateFacingWithBoundingBox(painting.facingDirection);
            return false;
        }
        this.update();
        return true;
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            update();
            return true;
        }

        return false;
    }

    private void update() {
        WorldServer world = ((CraftWorld) getWorld()).getHandle();
        EntityPainting painting = new EntityPainting(world);
        painting.hangingPosition = getHandle().getHangingPosition();
        painting.art = getHandle().art;
        painting.updateFacingWithBoundingBox(getHandle().facingDirection);
        getHandle().setDead();
        getHandle().velocityChanged = true; // because this occurs when the painting is broken, so it might be important
        world.spawnEntity(painting);
        this.entity = painting;
    }

    @Override
    public EntityPainting getHandle() {
        return (EntityPainting) entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + getArt() + "}";
    }

    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
