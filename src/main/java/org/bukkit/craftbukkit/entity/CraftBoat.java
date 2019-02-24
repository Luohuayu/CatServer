package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityBoat;
import org.bukkit.TreeSpecies;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;

public class CraftBoat extends CraftVehicle implements Boat {

    public CraftBoat(CraftServer server, EntityBoat entity) {
        super(server, entity);
    }

    @Override
    public TreeSpecies getWoodType() {
        return getTreeSpecies(getHandle().getBoatType());
    }

    @Override
    public void setWoodType(TreeSpecies species) {
        getHandle().setBoatType(getBoatType(species));
    }

    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    public double getOccupiedDeceleration() {
        return getHandle().occupiedDeceleration;
    }

    public void setOccupiedDeceleration(double speed) {
        if (speed >= 0D) {
            getHandle().occupiedDeceleration = speed;
        }
    }

    public double getUnoccupiedDeceleration() {
        return getHandle().unoccupiedDeceleration;
    }

    public void setUnoccupiedDeceleration(double speed) {
        getHandle().unoccupiedDeceleration = speed;
    }

    public boolean getWorkOnLand() {
        return getHandle().landBoats;
    }

    public void setWorkOnLand(boolean workOnLand) {
        getHandle().landBoats = workOnLand;
    }

    @Override
    public EntityBoat getHandle() {
        return (EntityBoat) entity;
    }

    @Override
    public String toString() {
        return "CraftBoat";
    }

    public EntityType getType() {
        return EntityType.BOAT;
    }

    public static TreeSpecies getTreeSpecies(EntityBoat.Type boatType) {
        switch (boatType) {
            case SPRUCE:
                return TreeSpecies.REDWOOD;
            case BIRCH:
                return TreeSpecies.BIRCH;
            case JUNGLE:
                return TreeSpecies.JUNGLE;
            case ACACIA:
                return TreeSpecies.ACACIA;
            case DARK_OAK:
                return TreeSpecies.DARK_OAK;
            case OAK:
            default:
                return TreeSpecies.GENERIC;
        }
    }

    public static EntityBoat.Type getBoatType(TreeSpecies species) {
        switch (species) {
            case REDWOOD:
                return EntityBoat.Type.SPRUCE;
            case BIRCH:
                return EntityBoat.Type.BIRCH;
            case JUNGLE:
                return EntityBoat.Type.JUNGLE;
            case ACACIA:
                return EntityBoat.Type.ACACIA;
            case DARK_OAK:
                return EntityBoat.Type.DARK_OAK;
            case GENERIC:
            default:
                return EntityBoat.Type.OAK;
        }
    }
}
