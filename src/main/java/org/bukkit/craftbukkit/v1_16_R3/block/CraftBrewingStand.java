package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.BrewingStandTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class CraftBrewingStand extends CraftContainer<BrewingStandTileEntity> implements BrewingStand {

    public CraftBrewingStand(Block block) {
        super(block, BrewingStandTileEntity.class);
    }

    public CraftBrewingStand(final Material material, final BrewingStandTileEntity te) {
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
        return this.getSnapshot().brewTime;
    }

    @Override
    public void setBrewingTime(int brewTime) {
        this.getSnapshot().brewTime = brewTime;
    }

    @Override
    public int getFuelLevel() {
        return this.getSnapshot().fuel;
    }

    @Override
    public void setFuelLevel(int level) {
        this.getSnapshot().fuel = level;
    }
}
