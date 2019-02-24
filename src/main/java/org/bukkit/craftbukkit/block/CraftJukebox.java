package org.bukkit.craftbukkit.block;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockEntityState<TileEntityJukebox> implements Jukebox {

    public CraftJukebox(final Block block) {
        super(block, TileEntityJukebox.class);
    }

    public CraftJukebox(final Material material, TileEntityJukebox te) {
        super(material, te);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.JUKEBOX) {
            CraftWorld world = (CraftWorld) this.getWorld();
            Material record = this.getPlaying();
            if (record == Material.AIR) {
                world.getHandle().setBlockState(new BlockPos(this.getX(), this.getY(), this.getZ()),
                    Blocks.JUKEBOX.getDefaultState()
                        .withProperty(BlockJukebox.HAS_RECORD, false), 3);
            } else {
                world.getHandle().setBlockState(new BlockPos(this.getX(), this.getY(), this.getZ()),
                    Blocks.JUKEBOX.getDefaultState()
                        .withProperty(BlockJukebox.HAS_RECORD, true), 3);
            }
            world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record.getId());
        }

        return result;
    }

    @Override
    public Material getPlaying() {
        ItemStack record = this.getSnapshot().getRecord();
        if (record.isEmpty()) {
            return Material.AIR;
        }
        return CraftMagicNumbers.getMaterial(record.getItem());
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
        }

        this.getSnapshot().setRecord(new ItemStack(CraftMagicNumbers.getItem(record), 1));
        if (record == Material.AIR) {
            setRawData((byte) 0);
        } else {
            setRawData((byte) 1);
        }
    }

    @Override
    public boolean isPlaying() {
        return getRawData() == 1;
    }

    @Override
    public boolean eject() {
        requirePlaced();
        TileEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof TileEntityJukebox)) return false;

        TileEntityJukebox jukebox = (TileEntityJukebox) tileEntity;
        boolean result = !jukebox.getRecord().isEmpty();
        CraftWorld world = (CraftWorld) this.getWorld();
        ((BlockJukebox) Blocks.JUKEBOX).dropRecord(world.getHandle(), new BlockPos(getX(), getY(), getZ()), null);
        return result;
    }
}
