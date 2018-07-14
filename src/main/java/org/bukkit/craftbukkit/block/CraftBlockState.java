// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import org.bukkit.plugin.Plugin;
import java.util.List;
import org.bukkit.metadata.MetadataValue;
import net.minecraft.tileentity.TileEntity;
import org.bukkit.Location;
import net.minecraft.block.state.IBlockState;
import org.bukkit.material.Attachable;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Chunk;
import net.minecraft.world.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.BlockState;

public class CraftBlockState implements BlockState
{
    private final CraftWorld world;
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    protected int type;
    protected MaterialData data;
    protected int flag;
    
    public CraftBlockState(final Block block) {
        this.world = (CraftWorld)block.getWorld();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.type = block.getTypeId();
        this.chunk = (CraftChunk)block.getChunk();
        this.flag = 3;
        this.createData(block.getData());
    }
    
    public CraftBlockState(final Block block, final int flag) {
        this(block);
        this.flag = flag;
    }
    
    public CraftBlockState(final Material material) {
        this.world = null;
        this.type = material.getId();
        this.chunk = null;
        final boolean x = false;
        this.z = (x ? 1 : 0);
        this.y = (x ? 1 : 0);
        this.x = (x ? 1 : 0);
    }
    
    public static CraftBlockState getBlockState(final World world, final int x, final int y, final int z) {
        return new CraftBlockState(world.getWorld().getBlockAt(x, y, z));
    }
    
    public static CraftBlockState getBlockState(final World world, final int x, final int y, final int z, final int flag) {
        return new CraftBlockState(world.getWorld().getBlockAt(x, y, z), flag);
    }
    
    @Override
    public org.bukkit.World getWorld() {
        this.requirePlaced();
        return this.world;
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    @Override
    public int getZ() {
        return this.z;
    }
    
    @Override
    public Chunk getChunk() {
        this.requirePlaced();
        return this.chunk;
    }
    
    @Override
    public void setData(final MaterialData data) {
        final Material mat = this.getType();
        if (mat == null || mat.getData() == null) {
            this.data = data;
        }
        else {
            if (data.getClass() != mat.getData() && data.getClass() != MaterialData.class) {
                throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
            }
            this.data = data;
        }
    }
    
    @Override
    public MaterialData getData() {
        return this.data;
    }
    
    @Override
    public void setType(final Material type) {
        this.setTypeId(type.getId());
    }
    
    @Override
    public boolean setTypeId(final int type) {
        if (this.type != type) {
            this.type = type;
            this.createData((byte)0);
        }
        return true;
    }
    
    @Override
    public Material getType() {
        return Material.getMaterial(this.getTypeId());
    }
    
    public void setFlag(final int flag) {
        this.flag = flag;
    }
    
    public int getFlag() {
        return this.flag;
    }
    
    @Override
    public int getTypeId() {
        return this.type;
    }
    
    @Override
    public byte getLightLevel() {
        return this.getBlock().getLightLevel();
    }
    
    @Override
    public Block getBlock() {
        this.requirePlaced();
        return this.world.getBlockAt(this.x, this.y, this.z);
    }
    
    @Override
    public boolean update() {
        return this.update(false);
    }
    
    @Override
    public boolean update(final boolean force) {
        return this.update(force, true);
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        this.requirePlaced();
        final Block block = this.getBlock();
        if (block.getType() != this.getType() && !force) {
            return false;
        }
        final BlockPos pos = new BlockPos(this.x, this.y, this.z);
        final IBlockState newBlock = CraftMagicNumbers.getBlock(this.getType()).getStateFromMeta(this.getRawData());
        block.setTypeIdAndData(this.getTypeId(), this.getRawData(), applyPhysics);
        this.world.getHandle().notifyBlockUpdate(pos, CraftMagicNumbers.getBlock(block).getStateFromMeta(block.getData()), newBlock, 3);
        if (applyPhysics && this.getData() instanceof Attachable) {
            this.world.getHandle().notifyNeighborsOfStateChange(pos.offset(CraftBlock.blockFaceToNotch(((Attachable)this.getData()).getAttachedFace())), newBlock.getBlock());
        }
        return true;
    }
    
    private void createData(final byte data) {
        final Material mat = this.getType();
        if (mat == null || mat.getData() == null) {
            this.data = new MaterialData(this.type, data);
        }
        else {
            this.data = mat.getNewData(data);
        }
    }
    
    @Override
    public byte getRawData() {
        return this.data.getData();
    }
    
    @Override
    public Location getLocation() {
        return new Location(this.world, this.x, this.y, this.z);
    }
    
    @Override
    public Location getLocation(final Location loc) {
        if (loc != null) {
            loc.setWorld(this.world);
            loc.setX(this.x);
            loc.setY(this.y);
            loc.setZ(this.z);
            loc.setYaw(0.0f);
            loc.setPitch(0.0f);
        }
        return loc;
    }
    
    @Override
    public void setRawData(final byte data) {
        this.data.setData(data);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftBlockState other = (CraftBlockState)obj;
        return (this.world == other.world || (this.world != null && this.world.equals(other.world))) && this.x == other.x && this.y == other.y && this.z == other.z && this.type == other.type && (this.data == other.data || (this.data != null && this.data.equals(other.data)));
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + ((this.world != null) ? this.world.hashCode() : 0);
        hash = 73 * hash + this.x;
        hash = 73 * hash + this.y;
        hash = 73 * hash + this.z;
        hash = 73 * hash + this.type;
        hash = 73 * hash + ((this.data != null) ? this.data.hashCode() : 0);
        return hash;
    }
    
    public TileEntity getTileEntity() {
        return null;
    }
    
    @Override
    public void setMetadata(final String metadataKey, final MetadataValue newMetadataValue) {
        this.requirePlaced();
        this.chunk.getCraftWorld().getBlockMetadata().setMetadata(this.getBlock(), metadataKey, newMetadataValue);
    }
    
    @Override
    public List<MetadataValue> getMetadata(final String metadataKey) {
        this.requirePlaced();
        return this.chunk.getCraftWorld().getBlockMetadata().getMetadata(this.getBlock(), metadataKey);
    }
    
    @Override
    public boolean hasMetadata(final String metadataKey) {
        this.requirePlaced();
        return this.chunk.getCraftWorld().getBlockMetadata().hasMetadata(this.getBlock(), metadataKey);
    }
    
    @Override
    public void removeMetadata(final String metadataKey, final Plugin owningPlugin) {
        this.requirePlaced();
        this.chunk.getCraftWorld().getBlockMetadata().removeMetadata(this.getBlock(), metadataKey, owningPlugin);
    }
    
    @Override
    public boolean isPlaced() {
        return this.world != null;
    }
    
    protected void requirePlaced() {
        if (!this.isPlaced()) {
            throw new IllegalStateException("The blockState must be placed to call this method");
        }
    }
}
