package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.world.LockCode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;

public abstract class CraftContainer<T extends TileEntityLockable> extends CraftBlockEntityState<T> implements Container {

    public CraftContainer(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftContainer(final Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public boolean isLocked() {
        return this.getSnapshot().isLocked();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().getLockCode().getLock();
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().setLockCode(key == null ? LockCode.EMPTY_CODE : new LockCode(key));
    }
}
