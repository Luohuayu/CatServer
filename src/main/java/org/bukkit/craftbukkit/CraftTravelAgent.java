// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import net.minecraft.world.WorldServer;
import org.bukkit.TravelAgent;
import net.minecraft.world.Teleporter;

public class CraftTravelAgent extends Teleporter implements TravelAgent
{
    public static TravelAgent DEFAULT;
    private int searchRadius;
    private int creationRadius;
    private boolean canCreatePortal;
    
    static {
        CraftTravelAgent.DEFAULT = null;
    }
    
    public CraftTravelAgent(final WorldServer worldserver) {
        super(worldserver);
        this.searchRadius = 128;
        this.creationRadius = 16;
        this.canCreatePortal = true;
        if (CraftTravelAgent.DEFAULT == null && worldserver.dimension == 0) {
            CraftTravelAgent.DEFAULT = this;
        }
    }
    
    @Override
    public Location findOrCreate(final Location target) {
        ((CraftWorld)target.getWorld()).getHandle();
        Location found = this.findPortal(target);
        if (found == null) {
            if (this.getCanCreatePortal() && this.createPortal(target)) {
                found = this.findPortal(target);
            }
            else {
                found = target;
            }
        }
        return found;
    }
    
    @Override
    public Location findPortal(final Location location) {
        final Teleporter pta = ((CraftWorld)location.getWorld()).getHandle().getDefaultTeleporter();
        final BlockPos found = pta.findPortal(location.getX(), location.getY(), location.getZ(), this.getSearchRadius());
        return (found != null) ? new Location(location.getWorld(), found.getX(), found.getY(), found.getZ(), location.getYaw(), location.getPitch()) : null;
    }
    
    @Override
    public boolean createPortal(final Location location) {
        final Teleporter pta = ((CraftWorld)location.getWorld()).getHandle().getDefaultTeleporter();
        return pta.makePortal(location.getX(), location.getY(), location.getZ(), this.getCreationRadius());
    }
    
    @Override
    public TravelAgent setSearchRadius(final int radius) {
        this.searchRadius = radius;
        return this;
    }
    
    @Override
    public int getSearchRadius() {
        return this.searchRadius;
    }
    
    @Override
    public TravelAgent setCreationRadius(final int radius) {
        this.creationRadius = ((radius < 2) ? 0 : radius);
        return this;
    }
    
    @Override
    public int getCreationRadius() {
        return this.creationRadius;
    }
    
    @Override
    public boolean getCanCreatePortal() {
        return this.canCreatePortal;
    }
    
    @Override
    public void setCanCreatePortal(final boolean create) {
        this.canCreatePortal = create;
    }
}
