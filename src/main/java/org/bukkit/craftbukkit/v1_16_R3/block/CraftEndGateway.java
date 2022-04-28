package org.bukkit.craftbukkit.v1_16_R3.block;

import java.util.Objects;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;

public class CraftEndGateway extends CraftBlockEntityState<EndGatewayTileEntity> implements EndGateway {

    public CraftEndGateway(Block block) {
        super(block, EndGatewayTileEntity.class);
    }

    public CraftEndGateway(final Material material, EndGatewayTileEntity te) {
        super(material, te);
    }

    @Override
    public Location getExitLocation() {
        BlockPos pos = this.getSnapshot().exitPortal;
        return pos == null ? null : new Location(this.isPlaced() ? this.getWorld() : null, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void setExitLocation(Location location) {
        if (location == null) {
            this.getSnapshot().exitPortal = null;
        } else if (!Objects.equals(location.getWorld(), this.isPlaced() ? this.getWorld() : null)) {
            throw new IllegalArgumentException("Cannot set exit location to different world");
        } else {
            this.getSnapshot().exitPortal = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }
    }

    @Override
    public boolean isExactTeleport() {
        return this.getSnapshot().exactTeleport;
    }

    @Override
    public void setExactTeleport(boolean exact) {
        this.getSnapshot().exactTeleport = exact;
    }

    @Override
    public long getAge() {
        return this.getSnapshot().age;
    }

    @Override
    public void setAge(long age) {
        this.getSnapshot().age = age;
    }

    @Override
    public void applyTo(EndGatewayTileEntity endGateway) {
        super.applyTo(endGateway);

        if (this.getSnapshot().exitPortal == null) {
            endGateway.exitPortal = null;
        }
    }
}
