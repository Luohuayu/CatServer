// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.util.EnumFacing;
import org.bukkit.block.BlockFace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftEntity implements Hanging
{
    public CraftHanging(final CraftServer server, final EntityHanging entity) {
        super(server, entity);
    }
    
    @Override
    public BlockFace getAttachedFace() {
        return this.getFacing().getOppositeFace();
    }
    
    @Override
    public void setFacingDirection(final BlockFace face) {
        this.setFacingDirection(face, false);
    }
    
    @Override
    public boolean setFacingDirection(final BlockFace face, final boolean force) {
        final EntityHanging hanging = this.getHandle();
        final EnumFacing dir = hanging.facingDirection;
        switch (face) {
            default: {
                this.getHandle().updateFacingWithBoundingBox(EnumFacing.SOUTH);
                break;
            }
            case WEST: {
                this.getHandle().updateFacingWithBoundingBox(EnumFacing.WEST);
                break;
            }
            case NORTH: {
                this.getHandle().updateFacingWithBoundingBox(EnumFacing.NORTH);
                break;
            }
            case EAST: {
                this.getHandle().updateFacingWithBoundingBox(EnumFacing.EAST);
                break;
            }
        }
        if (!force && !hanging.onValidSurface()) {
            hanging.updateFacingWithBoundingBox(dir);
            return false;
        }
        return true;
    }
    
    @Override
    public BlockFace getFacing() {
        final EnumFacing direction = this.getHandle().facingDirection;
        if (direction == null) {
            return BlockFace.SELF;
        }
        switch (direction) {
            default: {
                return BlockFace.SOUTH;
            }
            case WEST: {
                return BlockFace.WEST;
            }
            case NORTH: {
                return BlockFace.NORTH;
            }
            case EAST: {
                return BlockFace.EAST;
            }
        }
    }
    
    @Override
    public EntityHanging getHandle() {
        return (EntityHanging)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftHanging";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
