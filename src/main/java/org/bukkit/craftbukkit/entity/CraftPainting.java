// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.Art;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting
{
    public CraftPainting(final CraftServer server, final EntityPainting entity) {
        super(server, entity);
    }
    
    @Override
    public Art getArt() {
        final EntityPainting.EnumArt art = this.getHandle().art;
        return CraftArt.NotchToBukkit(art);
    }
    
    @Override
    public boolean setArt(final Art art) {
        return this.setArt(art, false);
    }
    
    @Override
    public boolean setArt(final Art art, final boolean force) {
        final EntityPainting painting = this.getHandle();
        final EntityPainting.EnumArt oldArt = painting.art;
        painting.art = CraftArt.BukkitToNotch(art);
        painting.updateFacingWithBoundingBox(painting.facingDirection);
        if (!force && !painting.onValidSurface()) {
            painting.art = oldArt;
            painting.updateFacingWithBoundingBox(painting.facingDirection);
            return false;
        }
        this.update();
        return true;
    }
    
    @Override
    public boolean setFacingDirection(final BlockFace face, final boolean force) {
        if (super.setFacingDirection(face, force)) {
            this.update();
            return true;
        }
        return false;
    }
    
    private void update() {
        final WorldServer world = ((CraftWorld)this.getWorld()).getHandle();
        final EntityPainting painting = new EntityPainting(world);
        painting.hangingPosition = this.getHandle().hangingPosition;
        painting.art = this.getHandle().art;
        painting.updateFacingWithBoundingBox(this.getHandle().facingDirection);
        this.getHandle().setDead();
        this.getHandle().velocityChanged = true;
        world.spawnEntityInWorld(painting);
        this.entity = painting;
    }
    
    @Override
    public EntityPainting getHandle() {
        return (EntityPainting)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftPainting{art=" + this.getArt() + "}";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
