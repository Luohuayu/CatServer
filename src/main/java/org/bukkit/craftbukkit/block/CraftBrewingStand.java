package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityBrewingStand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class CraftBrewingStand extends CraftContainer<TileEntityBrewingStand> implements BrewingStand {

    public CraftBrewingStand(Block block) {
        super(block, TileEntityBrewingStand.class);
    }

    public CraftBrewingStand(final Material material, final TileEntityBrewingStand te) {
        super(material, te);
    }

    @Override
    public BrewerInventory getSnapshotInventory() {
        return new CraftInventoryBrewer(this.getSnapshot());
    }

    @Override
    public BrewerInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryBrewer(this.getTileEntity());
    }

    @Override
    public int getBrewingTime() {
        return this.getSnapshot().getField(0);
    }

    @Override
    public void setBrewingTime(int brewTime) {
        this.getSnapshot().setField(0, brewTime);
    }

    @Override
    public int getFuelLevel() {
        return this.getSnapshot().getField(1);
    }

    @Override
    public void setFuelLevel(int level) {
        this.getSnapshot().setField(1, level);
    }

    @Override
    public String getCustomName() {
        TileEntityBrewingStand brewingStand = this.getSnapshot();
        return brewingStand.hasCustomName() ? brewingStand.getName() : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setName(name);
    }

    @Override
    public void applyTo(TileEntityBrewingStand brewingStand) {
        super.applyTo(brewingStand);

        if (!this.getSnapshot().hasCustomName()) {
            brewingStand.setName(null);
        }
    }
}
