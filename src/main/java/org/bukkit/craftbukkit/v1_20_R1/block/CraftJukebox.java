package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryJukebox;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.JukeboxInventory;

public class CraftJukebox extends CraftBlockEntityState<JukeboxBlockEntity> implements Jukebox {

    public CraftJukebox(World world, JukeboxBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public JukeboxInventory getSnapshotInventory() {
        return new CraftInventoryJukebox(this.getSnapshot());
    }

    @Override
    public JukeboxInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryJukebox(this.getTileEntity());
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.JUKEBOX) {
            Material record = this.getPlaying();
            getWorldHandle().setBlock(this.getPosition(), data, 3);

            BlockEntity tileEntity = this.getTileEntityFromWorld();
            if (tileEntity instanceof JukeboxBlockEntity jukebox) {
                CraftWorld world = (CraftWorld) this.getWorld();
                if (record.isAir()) {
                    jukebox.setRecordWithoutPlaying(ItemStack.EMPTY);
                    world.playEffect(this.getLocation(), Effect.IRON_DOOR_CLOSE, 0); // TODO: Fix this enum constant. This stops jukeboxes
                } else {
                    world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record);
                }
            }
        }

        return result;
    }

    @Override
    public Material getPlaying() {
        return getRecord().getType();
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
        }

        setRecord(new org.bukkit.inventory.ItemStack(record));
    }

    @Override
    public boolean hasRecord() {
        return getHandle().getValue(JukeboxBlock.HAS_RECORD) && !getPlaying().isAir();
    }

    @Override
    public org.bukkit.inventory.ItemStack getRecord() {
        ItemStack record = this.getSnapshot().getFirstItem();
        return CraftItemStack.asBukkitCopy(record);
    }

    @Override
    public void setRecord(org.bukkit.inventory.ItemStack record) {
        ItemStack nms = CraftItemStack.asNMSCopy(record);

        JukeboxBlockEntity snapshot = this.getSnapshot();
        snapshot.setRecordWithoutPlaying(nms);
        snapshot.recordStartedTick = snapshot.tickCount;
        snapshot.isPlaying = !nms.isEmpty();

        this.data = this.data.setValue(JukeboxBlock.HAS_RECORD, !nms.isEmpty());
    }

    @Override
    public boolean isPlaying() {
        requirePlaced();

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        return tileEntity instanceof JukeboxBlockEntity jukebox && jukebox.isRecordPlaying();
    }

    @Override
    public boolean startPlaying() {
        requirePlaced();

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof JukeboxBlockEntity jukebox)) {
            return false;
        }

        ItemStack record = jukebox.getFirstItem();
        if (record.isEmpty() || isPlaying()) {
            return false;
        }

        jukebox.isPlaying = true;
        jukebox.recordStartedTick = jukebox.tickCount;
        getWorld().playEffect(getLocation(), Effect.RECORD_PLAY, CraftMagicNumbers.getMaterial(record.getItem()));
        return true;
    }

    @Override
    public void stopPlaying() {
        requirePlaced();

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof JukeboxBlockEntity jukebox)) {
            return;
        }

        jukebox.isPlaying = false;
        getWorld().playEffect(getLocation(), Effect.IRON_DOOR_CLOSE, 0); // TODO: Fix this enum constant. This stops jukeboxes
    }

    @Override
    public boolean eject() {
        ensureNoWorldGeneration();

        BlockEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof JukeboxBlockEntity)) return false;

        JukeboxBlockEntity jukebox = (JukeboxBlockEntity) tileEntity;
        boolean result = !jukebox.getFirstItem().isEmpty();
        jukebox.popOutRecord();
        return result;
    }
}
