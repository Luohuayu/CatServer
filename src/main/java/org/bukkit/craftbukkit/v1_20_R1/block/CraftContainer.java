package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.LockCode;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage;

public abstract class CraftContainer<T extends BaseContainerBlockEntity> extends CraftBlockEntityState<T> implements Container {

    public CraftContainer(World world, T tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public boolean isLocked() {
        return !this.getSnapshot().lockKey.key.isEmpty();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().lockKey.key;
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().lockKey = (key == null) ? LockCode.NO_LOCK : new LockCode(key);
    }

    @Override
    public String getCustomName() {
        T container = this.getSnapshot();
        return container.getCustomName() != null ? CraftChatMessage.fromComponent(container.getCustomName()) : null; // CatServer - name -> getCustomName()
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(T container) {
        super.applyTo(container);

        if (this.getSnapshot().getCustomName() == null) { // CatServer - name -> getCustomName()
            container.setCustomName(null);
        }
    }
}
