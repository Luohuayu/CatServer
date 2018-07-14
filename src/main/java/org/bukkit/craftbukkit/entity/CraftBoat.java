// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.TreeSpecies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Boat;

public class CraftBoat extends CraftVehicle implements Boat
{
    public CraftBoat(final CraftServer server, final EntityBoat entity) {
        super(server, entity);
    }
    
    @Override
    public TreeSpecies getWoodType() {
        return getTreeSpecies(this.getHandle().getBoatType());
    }
    
    @Override
    public void setWoodType(final TreeSpecies species) {
        this.getHandle().setBoatType(getBoatType(species));
    }
    
    @Override
    public double getMaxSpeed() {
        return this.getHandle().maxSpeed;
    }
    
    @Override
    public void setMaxSpeed(final double speed) {
        if (speed >= 0.0) {
            this.getHandle().maxSpeed = speed;
        }
    }
    
    @Override
    public double getOccupiedDeceleration() {
        return this.getHandle().occupiedDeceleration;
    }
    
    @Override
    public void setOccupiedDeceleration(final double speed) {
        if (speed >= 0.0) {
            this.getHandle().occupiedDeceleration = speed;
        }
    }
    
    @Override
    public double getUnoccupiedDeceleration() {
        return this.getHandle().unoccupiedDeceleration;
    }
    
    @Override
    public void setUnoccupiedDeceleration(final double speed) {
        this.getHandle().unoccupiedDeceleration = speed;
    }
    
    @Override
    public boolean getWorkOnLand() {
        return this.getHandle().landBoats;
    }
    
    @Override
    public void setWorkOnLand(final boolean workOnLand) {
        this.getHandle().landBoats = workOnLand;
    }
    
    @Override
    public EntityBoat getHandle() {
        return (EntityBoat)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftBoat";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.BOAT;
    }
    
    public static TreeSpecies getTreeSpecies(final EntityBoat.Type boatType) {
        switch (boatType) {
            case SPRUCE: {
                return TreeSpecies.REDWOOD;
            }
            case BIRCH: {
                return TreeSpecies.BIRCH;
            }
            case JUNGLE: {
                return TreeSpecies.JUNGLE;
            }
            case ACACIA: {
                return TreeSpecies.ACACIA;
            }
            case DARK_OAK: {
                return TreeSpecies.DARK_OAK;
            }
            default: {
                return TreeSpecies.GENERIC;
            }
        }
    }
    
    public static EntityBoat.Type getBoatType(final TreeSpecies species) {
        switch (species) {
            case REDWOOD: {
                return EntityBoat.Type.SPRUCE;
            }
            case BIRCH: {
                return EntityBoat.Type.BIRCH;
            }
            case JUNGLE: {
                return EntityBoat.Type.JUNGLE;
            }
            case ACACIA: {
                return EntityBoat.Type.ACACIA;
            }
            case DARK_OAK: {
                return EntityBoat.Type.DARK_OAK;
            }
            default: {
                return EntityBoat.Type.OAK;
            }
        }
    }
}
