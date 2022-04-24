package org.bukkit.craftbukkit.v1_18_R2.entity;

import org.bukkit.TreeSpecies;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;

public class CraftBoat extends CraftVehicle implements Boat {

    public CraftBoat(CraftServer server, net.minecraft.world.entity.vehicle.Boat entity) {
        super(server, entity);
    }

    @Override
    public TreeSpecies getWoodType() {
        return getTreeSpecies(getHandle().getBoatType());
    }

    @Override
    public void setWoodType(TreeSpecies species) {
        getHandle().setType(getBoatType(species));
    }

    @Override
    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    @Override
    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    @Override
    public double getOccupiedDeceleration() {
        return getHandle().occupiedDeceleration;
    }

    @Override
    public void setOccupiedDeceleration(double speed) {
        if (speed >= 0D) {
            getHandle().occupiedDeceleration = speed;
        }
    }

    @Override
    public double getUnoccupiedDeceleration() {
        return getHandle().unoccupiedDeceleration;
    }

    @Override
    public void setUnoccupiedDeceleration(double speed) {
        getHandle().unoccupiedDeceleration = speed;
    }

    @Override
    public boolean getWorkOnLand() {
        return getHandle().landBoats;
    }

    @Override
    public void setWorkOnLand(boolean workOnLand) {
        getHandle().landBoats = workOnLand;
    }

    @Override
    public net.minecraft.world.entity.vehicle.Boat getHandle() {
        return (net.minecraft.world.entity.vehicle.Boat) entity;
    }

    @Override
    public String toString() {
        return "CraftBoat";
    }

    @Override
    public EntityType getType() {
        return EntityType.BOAT;
    }

    public static TreeSpecies getTreeSpecies(net.minecraft.world.entity.vehicle.Boat.Type boatType) {
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

    public static net.minecraft.world.entity.vehicle.Boat.Type getBoatType(TreeSpecies species) {
        switch (species) {
            case REDWOOD:
                return net.minecraft.world.entity.vehicle.Boat.Type.SPRUCE;
            case BIRCH:
                return net.minecraft.world.entity.vehicle.Boat.Type.BIRCH;
            case JUNGLE:
                return net.minecraft.world.entity.vehicle.Boat.Type.JUNGLE;
            case ACACIA:
                return net.minecraft.world.entity.vehicle.Boat.Type.ACACIA;
            case DARK_OAK:
                return net.minecraft.world.entity.vehicle.Boat.Type.DARK_OAK;
            case GENERIC:
            default:
                return net.minecraft.world.entity.vehicle.Boat.Type.OAK;
        }
    }
}
