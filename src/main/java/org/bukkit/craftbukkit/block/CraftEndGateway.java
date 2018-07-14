// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityEndGateway;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.EndGateway;

public class CraftEndGateway extends CraftBlockState implements EndGateway
{
    private final CraftWorld world;
    private final TileEntityEndGateway gateway;
    
    public CraftEndGateway(final Block block) {
        super(block);
        this.world = (CraftWorld)block.getWorld();
        this.gateway = (TileEntityEndGateway)this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftEndGateway(final Material material, final TileEntityEndGateway te) {
        super(material);
        this.world = null;
        this.gateway = te;
    }
    
    @Override
    public Location getExitLocation() {
        final BlockPos pos = this.gateway.exitPortal;
        return (pos == null) ? null : new Location(this.world, pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public void setExitLocation(final Location location) {
        if (location == null) {
            this.gateway.exitPortal = null;
        }
        else {
            if (location.getWorld() != this.world) {
                throw new IllegalArgumentException("Cannot set exit location to different world");
            }
            this.gateway.exitPortal = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }
    }
    
    @Override
    public boolean isExactTeleport() {
        return this.gateway.exactTeleport;
    }
    
    @Override
    public void setExactTeleport(final boolean exact) {
        this.gateway.exactTeleport = exact;
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            this.gateway.markDirty();
        }
        return result;
    }
    
    @Override
    public TileEntityEndGateway getTileEntity() {
        return this.gateway;
    }
}
