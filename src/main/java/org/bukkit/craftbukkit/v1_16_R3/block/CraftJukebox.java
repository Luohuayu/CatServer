package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockEntityState<JukeboxTileEntity> implements Jukebox {

    public CraftJukebox(final Block block) {
        super(block, JukeboxTileEntity.class);
    }

    public CraftJukebox(final Material material, JukeboxTileEntity te) {
        super(material, te);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.JUKEBOX) {
            CraftWorld world = (CraftWorld) this.getWorld();
            Material record = this.getPlaying();
            if (record == Material.AIR) {
                world.getHandle().setBlock(this.getPosition(), Blocks.JUKEBOX.defaultBlockState().setValue(JukeboxBlock.HAS_RECORD, false), 3);
            } else {
                world.getHandle().setBlock(this.getPosition(), Blocks.JUKEBOX.defaultBlockState().setValue(JukeboxBlock.HAS_RECORD, true), 3);
            }
            world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record);
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
    public org.bukkit.inventory.ItemStack getRecord() {
        ItemStack record = this.getSnapshot().getRecord();
        return CraftItemStack.asBukkitCopy(record);
    }

    @Override
    public void setRecord(org.bukkit.inventory.ItemStack record) {
        ItemStack nms = CraftItemStack.asNMSCopy(record);
        this.getSnapshot().setRecord(nms);
        if (nms.isEmpty()) {
            this.data = this.data.setValue(JukeboxBlock.HAS_RECORD, false);
        } else {
            this.data = this.data.setValue(JukeboxBlock.HAS_RECORD, true);
        }
    }

    @Override
    public boolean isPlaying() {
        return getHandle().getValue(JukeboxBlock.HAS_RECORD);
    }

    @Override
    public void stopPlaying() {
        getWorld().playEffect(getLocation(), Effect.RECORD_PLAY, Material.AIR);
    }

    @Override
    public boolean eject() {
        requirePlaced();
        TileEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof JukeboxTileEntity)) {
            return false;
        }

        JukeboxTileEntity jukebox = (JukeboxTileEntity) tileEntity;
        boolean result = !jukebox.getRecord().isEmpty();
        CraftWorld world = (CraftWorld) this.getWorld();
        ((JukeboxBlock) Blocks.JUKEBOX).dropRecording(world.getHandle(), getPosition());
        return result;
    }
}
