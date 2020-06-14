package org.bukkit.craftbukkit.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftBlockEntityState<T extends TileEntity> extends CraftBlockState {

    private final Class<T> tileEntityClass;
    private final T tileEntity;
    private T snapshot; // CatServer - remove final

    // CatServer start
    private boolean isSnapshotInit = false;
    private final NBTTagCompound nbtSnapshot;

    protected void initSnapshotFromNbt() {
        if (!isSnapshotInit) {
            this.snapshot = nbtSnapshot != null ? (T) TileEntity.create(tileEntity.getWorld(), nbtSnapshot) : null;
            load(snapshot);
            isSnapshotInit = true;
        }
    }
    // CatServer end

    public CraftBlockEntityState(Block block, Class<T> tileEntityClass) {
        super(block);

        this.tileEntityClass = tileEntityClass;

        // get tile entity from block:
        CraftWorld world = (CraftWorld) this.getWorld();
        this.tileEntity = tileEntityClass.cast(world.getTileEntityAt(this.getX(), this.getY(), this.getZ()));

        nbtSnapshot = tileEntity != null ? tileEntity.writeToNBT(new NBTTagCompound()) : null; // CatServer
        /*
        // copy tile entity data:
        this.snapshot = this.createSnapshot(tileEntity);
        this.load(snapshot);
        */
    }

    public CraftBlockEntityState(Material material, T tileEntity) {
        super(material);

        this.tileEntityClass = (Class<T>) tileEntity.getClass();
        this.tileEntity = tileEntity;

        nbtSnapshot = tileEntity != null ? tileEntity.writeToNBT(new NBTTagCompound()) : null; // CatServer
        /*
        // copy tile entity data:
        this.snapshot = this.createSnapshot(tileEntity);
        this.load(snapshot);
        */
    }

    private T createSnapshot(T tileEntity) {
        if (tileEntity == null) {
            return null;
        }

        NBTTagCompound nbtTagCompound = tileEntity.writeToNBT(new NBTTagCompound());
        T snapshot = (T) TileEntity.create(tileEntity.getWorld(), nbtTagCompound);

        return snapshot;
    }

    // copies the TileEntity-specific data, retains the position
    private void copyData(T from, T to) {
        BlockPos pos = to.getPos();
        NBTTagCompound nbtTagCompound = from.writeToNBT(new NBTTagCompound());
        to.readFromNBT(nbtTagCompound);

        // reset the original position:
        to.setPos(pos);
    }

    // gets the wrapped TileEntity
    public T getTileEntity() {
        return tileEntity;
    }

    // gets the cloned TileEntity which is used to store the captured data
    protected T getSnapshot() {
        initSnapshotFromNbt(); // CatServer
        return snapshot;
    }

    // gets the current TileEntity from the world at this position
    protected TileEntity getTileEntityFromWorld() {
        requirePlaced();

        return ((CraftWorld) this.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    // gets the NBT data of the TileEntity represented by this block state
    public NBTTagCompound getSnapshotNBT() {
        initSnapshotFromNbt(); // CatServer
        // update snapshot
        applyTo(snapshot);

        return snapshot.writeToNBT(new NBTTagCompound());
    }

    // copies the data of the given tile entity to this block state
    protected void load(T tileEntity) {
        if (tileEntity != null && tileEntity != snapshot) {
            copyData(tileEntity, snapshot);
        }
    }

    // applies the TileEntity data of this block state to the given TileEntity
    protected void applyTo(T tileEntity) {
        initSnapshotFromNbt(); // CatServer
        if (tileEntity != null && tileEntity != snapshot) {
            copyData(snapshot, tileEntity);
        }
    }

    protected boolean isApplicable(TileEntity tileEntity) {
        return tileEntityClass.isInstance(tileEntity);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced()) {
            TileEntity tile = getTileEntityFromWorld();

            if (isApplicable(tile)) {
                applyTo(tileEntityClass.cast(tile));
                tile.markDirty();
            }
        }

        return result;
    }
}
