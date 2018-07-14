// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.MetadataValue;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockCocoa;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySkull;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.item.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.PistonMoveReaction;
import net.minecraft.block.BlockRedstoneWire;
import java.util.Locale;
import net.minecraft.util.ResourceLocation;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import net.minecraft.util.EnumFacing;
import org.bukkit.block.BlockFace;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockContainer;
import org.bukkit.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Chunk;
import org.bukkit.util.BlockVector;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.block.Block;

public class CraftBlock implements Block
{
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    
    public CraftBlock(final CraftChunk chunk, final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunk = chunk;
    }
    
    private net.minecraft.block.Block getNMSBlock() {
        return CraftMagicNumbers.getBlock(this);
    }
    
    private static net.minecraft.block.Block getNMSBlock(final int type) {
        return CraftMagicNumbers.getBlock(type);
    }
    
    @Override
    public World getWorld() {
        return this.chunk.getWorld();
    }
    
    @Override
    public Location getLocation() {
        return new Location(this.getWorld(), this.x, this.y, this.z);
    }
    
    @Override
    public Location getLocation(final Location loc) {
        if (loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX(this.x);
            loc.setY(this.y);
            loc.setZ(this.z);
            loc.setYaw(0.0f);
            loc.setPitch(0.0f);
        }
        return loc;
    }
    
    public BlockVector getVector() {
        return new BlockVector(this.x, this.y, this.z);
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
        return this.chunk;
    }
    
    @Override
    public void setData(final byte data) {
        this.setData(data, 3);
    }
    
    @Override
    public void setData(final byte data, final boolean applyPhysics) {
        if (applyPhysics) {
            this.setData(data, 3);
        }
        else {
            this.setData(data, 2);
        }
    }
    
    private void setData(final byte data, final int flag) {
        final net.minecraft.world.World world = this.chunk.getHandle().getWorld();
        final BlockPos position = new BlockPos(this.x, this.y, this.z);
        final IBlockState blockData = world.getBlockState(position);
        world.setBlockState(position, blockData.getBlock().getStateFromMeta(data), flag);
    }
    
    private IBlockState getData0() {
        return this.chunk.getHandle().getBlockState(new BlockPos(this.x, this.y, this.z));
    }
    
    @Override
    public byte getData() {
        final IBlockState blockData = this.chunk.getHandle().getBlockState(new BlockPos(this.x, this.y, this.z));
        return (byte)blockData.getBlock().getMetaFromState(blockData);
    }
    
    @Override
    public void setType(final Material type) {
        this.setType(type, true);
    }
    
    @Override
    public void setType(final Material type, final boolean applyPhysics) {
        this.setTypeId(type.getId(), applyPhysics);
    }
    
    @Override
    public boolean setTypeId(final int type) {
        return this.setTypeId(type, true);
    }
    
    @Override
    public boolean setTypeId(final int type, final boolean applyPhysics) {
        final net.minecraft.block.Block block = getNMSBlock(type);
        return this.setTypeIdAndData(type, (byte)block.getMetaFromState(block.getDefaultState()), applyPhysics);
    }
    
    @Override
    public boolean setTypeIdAndData(final int type, final byte data, final boolean applyPhysics) {
        final IBlockState blockData = getNMSBlock(type).getStateFromMeta(data);
        final BlockPos position = new BlockPos(this.x, this.y, this.z);
        if (type != 0 && blockData.getBlock() instanceof BlockContainer && type != this.getTypeId()) {
            this.chunk.getHandle().getWorld().setBlockState(position, Blocks.AIR.getDefaultState(), 0);
        }
        if (applyPhysics) {
            return this.chunk.getHandle().getWorld().setBlockState(position, blockData, 3);
        }
        final IBlockState old = this.chunk.getHandle().getBlockState(position);
        final boolean success = this.chunk.getHandle().getWorld().setBlockState(position, blockData, 2);
        if (success) {
            this.chunk.getHandle().getWorld().notifyBlockUpdate(position, old, blockData, 3);
        }
        return success;
    }
    
    @Override
    public Material getType() {
        return Material.getMaterial(this.getTypeId());
    }
    
    @Deprecated
    @Override
    public int getTypeId() {
        return CraftMagicNumbers.getId(this.chunk.getHandle().getBlockState(new BlockPos(this.x, this.y, this.z)).getBlock());
    }
    
    @Override
    public byte getLightLevel() {
        return (byte)this.chunk.getHandle().getWorld().getLightFromNeighbors(new BlockPos(this.x, this.y, this.z));
    }
    
    @Override
    public byte getLightFromSky() {
        return (byte)this.chunk.getHandle().getWorld().getLightFor(EnumSkyBlock.SKY, new BlockPos(this.x, this.y, this.z));
    }
    
    @Override
    public byte getLightFromBlocks() {
        return (byte)this.chunk.getHandle().getWorld().getLightFor(EnumSkyBlock.BLOCK, new BlockPos(this.x, this.y, this.z));
    }
    
    public Block getFace(final BlockFace face) {
        return this.getRelative(face, 1);
    }
    
    public Block getFace(final BlockFace face, final int distance) {
        return this.getRelative(face, distance);
    }
    
    @Override
    public Block getRelative(final int modX, final int modY, final int modZ) {
        return this.getWorld().getBlockAt(this.getX() + modX, this.getY() + modY, this.getZ() + modZ);
    }
    
    @Override
    public Block getRelative(final BlockFace face) {
        return this.getRelative(face, 1);
    }
    
    @Override
    public Block getRelative(final BlockFace face, final int distance) {
        return this.getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }
    
    @Override
    public BlockFace getFace(final Block block) {
        final BlockFace[] values = BlockFace.values();
        BlockFace[] array;
        for (int length = (array = values).length, i = 0; i < length; ++i) {
            final BlockFace face = array[i];
            if (this.getX() + face.getModX() == block.getX() && this.getY() + face.getModY() == block.getY() && this.getZ() + face.getModZ() == block.getZ()) {
                return face;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "CraftBlock{chunk=" + this.chunk + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",type=" + this.getType() + ",data=" + this.getData() + '}';
    }
    
    public static BlockFace notchToBlockFace(final EnumFacing notch) {
        if (notch == null) {
            return BlockFace.SELF;
        }
        switch (notch) {
            case DOWN: {
                return BlockFace.DOWN;
            }
            case UP: {
                return BlockFace.UP;
            }
            case NORTH: {
                return BlockFace.NORTH;
            }
            case SOUTH: {
                return BlockFace.SOUTH;
            }
            case WEST: {
                return BlockFace.WEST;
            }
            case EAST: {
                return BlockFace.EAST;
            }
            default: {
                return BlockFace.SELF;
            }
        }
    }
    
    public static EnumFacing blockFaceToNotch(final BlockFace face) {
        switch (face) {
            case DOWN: {
                return EnumFacing.DOWN;
            }
            case UP: {
                return EnumFacing.UP;
            }
            case NORTH: {
                return EnumFacing.NORTH;
            }
            case SOUTH: {
                return EnumFacing.SOUTH;
            }
            case WEST: {
                return EnumFacing.WEST;
            }
            case EAST: {
                return EnumFacing.EAST;
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public BlockState getState() {
        final Material material = this.getType();
        switch (material) {
            case SIGN_POST:
            case WALL_SIGN:
            case SIGN: {
                return new CraftSign(this);
            }
            case CHEST:
            case TRAPPED_CHEST: {
                return new CraftChest(this);
            }
            case FURNACE:
            case BURNING_FURNACE: {
                return new CraftFurnace(this);
            }
            case DISPENSER: {
                return new CraftDispenser(this);
            }
            case DROPPER: {
                return new CraftDropper(this);
            }
            case END_GATEWAY: {
                return new CraftEndGateway(this);
            }
            case HOPPER: {
                return new CraftHopper(this);
            }
            case MOB_SPAWNER: {
                return new CraftCreatureSpawner(this);
            }
            case NOTE_BLOCK: {
                return new CraftNoteBlock(this);
            }
            case JUKEBOX: {
                return new CraftJukebox(this);
            }
            case BREWING_STAND: {
                return new CraftBrewingStand(this);
            }
            case SKULL: {
                return new CraftSkull(this);
            }
            case COMMAND:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN: {
                return new CraftCommandBlock(this);
            }
            case BEACON: {
                return new CraftBeacon(this);
            }
            case STANDING_BANNER:
            case WALL_BANNER:
            case BANNER: {
                return new CraftBanner(this);
            }
            case FLOWER_POT: {
                return new CraftFlowerPot(this);
            }
            default: {
                return new CraftBlockState(this);
            }
        }
    }
    
    @Override
    public Biome getBiome() {
        return this.getWorld().getBiome(this.x, this.z);
    }
    
    @Override
    public void setBiome(final Biome bio) {
        this.getWorld().setBiome(this.x, this.z, bio);
    }
    
    public static Biome biomeBaseToBiome(final net.minecraft.world.biome.Biome base) {
        if (base == null) {
            return null;
        }
        return Biome.valueOf(net.minecraft.world.biome.Biome.REGISTRY.getNameForObject(base).getResourcePath().toUpperCase(Locale.ENGLISH));
    }
    
    public static net.minecraft.world.biome.Biome biomeToBiomeBase(final Biome bio) {
        if (bio == null) {
            return null;
        }
        return net.minecraft.world.biome.Biome.REGISTRY.getObject(new ResourceLocation(bio.name().toLowerCase(Locale.ENGLISH)));
    }
    
    @Override
    public double getTemperature() {
        return this.getWorld().getTemperature(this.x, this.z);
    }
    
    @Override
    public double getHumidity() {
        return this.getWorld().getHumidity(this.x, this.z);
    }
    
    @Override
    public boolean isBlockPowered() {
        return this.chunk.getHandle().getWorld().getStrongPower(new BlockPos(this.x, this.y, this.z)) > 0;
    }
    
    @Override
    public boolean isBlockIndirectlyPowered() {
        return this.chunk.getHandle().getWorld().isBlockPowered(new BlockPos(this.x, this.y, this.z));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftBlock)) {
            return false;
        }
        final CraftBlock other = (CraftBlock)o;
        return this.x == other.x && this.y == other.y && this.z == other.z && this.getWorld().equals(other.getWorld());
    }
    
    @Override
    public int hashCode() {
        return this.y << 24 ^ this.x ^ this.z ^ this.getWorld().hashCode();
    }
    
    @Override
    public boolean isBlockFacePowered(final BlockFace face) {
        return this.chunk.getHandle().getWorld().isSidePowered(new BlockPos(this.x, this.y, this.z), blockFaceToNotch(face));
    }
    
    @Override
    public boolean isBlockFaceIndirectlyPowered(final BlockFace face) {
        final int power = this.chunk.getHandle().getWorld().getRedstonePower(new BlockPos(this.x, this.y, this.z), blockFaceToNotch(face));
        final Block relative = this.getRelative(face);
        if (relative.getType() == Material.REDSTONE_WIRE) {
            return Math.max(power, relative.getData()) > 0;
        }
        return power > 0;
    }
    
    @Override
    public int getBlockPower(final BlockFace face) {
        int power = 0;
        final BlockRedstoneWire wire = Blocks.REDSTONE_WIRE;
        final net.minecraft.world.World world = this.chunk.getHandle().getWorld();
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(this.x, this.y - 1, this.z), EnumFacing.DOWN)) {
            power = wire.getMaxCurrentStrength(world, new BlockPos(this.x, this.y - 1, this.z), power);
        }
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(this.x, this.y + 1, this.z), EnumFacing.UP)) {
            power = wire.getMaxCurrentStrength(world, new BlockPos(this.x, this.y + 1, this.z), power);
        }
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(this.x + 1, this.y, this.z), EnumFacing.EAST)) {
            power = wire.getMaxCurrentStrength(world, new BlockPos(this.x + 1, this.y, this.z), power);
        }
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(this.x - 1, this.y, this.z), EnumFacing.WEST)) {
            power = wire.getMaxCurrentStrength(world, new BlockPos(this.x - 1, this.y, this.z), power);
        }
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(this.x, this.y, this.z - 1), EnumFacing.NORTH)) {
            power = wire.getMaxCurrentStrength(world, new BlockPos(this.x, this.y, this.z - 1), power);
        }
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(this.x, this.y, this.z + 1), EnumFacing.SOUTH)) {
            power = wire.getMaxCurrentStrength(world, new BlockPos(this.x, this.y, this.z - 1), power);
        }
        return (power > 0) ? power : (((face == BlockFace.SELF) ? this.isBlockIndirectlyPowered() : this.isBlockFaceIndirectlyPowered(face)) ? 15 : 0);
    }
    
    @Override
    public int getBlockPower() {
        return this.getBlockPower(BlockFace.SELF);
    }
    
    @Override
    public boolean isEmpty() {
        return this.getType() == Material.AIR;
    }
    
    @Override
    public boolean isLiquid() {
        return this.getType() == Material.WATER || this.getType() == Material.STATIONARY_WATER || this.getType() == Material.LAVA || this.getType() == Material.STATIONARY_LAVA;
    }
    
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(this.getNMSBlock().getDefaultState().getMaterial().getMobilityFlag().ordinal());
    }
    
    private boolean itemCausesDrops(final ItemStack item) {
        final net.minecraft.block.Block block = this.getNMSBlock();
        final Item itemType = (item != null) ? Item.getItemById(item.getTypeId()) : null;
        return block != null && (block.getDefaultState().getMaterial().isToolNotRequired() || (itemType != null && itemType.canHarvestBlock(block.getDefaultState())));
    }
    
    @Override
    public boolean breakNaturally() {
        final net.minecraft.block.Block block = this.getNMSBlock();
        final byte data = this.getData();
        boolean result = false;
        if (block != null && block != Blocks.AIR) {
            block.dropBlockAsItemWithChance(this.chunk.getHandle().getWorld(), new BlockPos(this.x, this.y, this.z), block.getStateFromMeta(data), 1.0f, 0);
            result = true;
        }
        this.setTypeId(Material.AIR.getId());
        return result;
    }
    
    @Override
    public boolean breakNaturally(final ItemStack item) {
        if (this.itemCausesDrops(item)) {
            return this.breakNaturally();
        }
        return this.setTypeId(Material.AIR.getId());
    }
    
    @Override
    public Collection<ItemStack> getDrops() {
        final List<ItemStack> drops = new ArrayList<ItemStack>();
        final net.minecraft.block.Block block = this.getNMSBlock();
        if (block != Blocks.AIR) {
            final IBlockState data = this.getData0();
            for (int count = block.quantityDroppedWithBonus(0, this.chunk.getHandle().getWorld().rand), i = 0; i < count; ++i) {
                final Item item = block.getItemDropped(data, this.chunk.getHandle().getWorld().rand, 0);
                if (item != null) {
                    if (Blocks.SKULL == block) {
                        final net.minecraft.item.ItemStack nmsStack = new net.minecraft.item.ItemStack(item, 1, block.damageDropped(data));
                        final TileEntitySkull tileentityskull = (TileEntitySkull)this.chunk.getHandle().getWorld().getTileEntity(new BlockPos(this.x, this.y, this.z));
                        if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null) {
                            nmsStack.setTagCompound(new NBTTagCompound());
                            final NBTTagCompound nbttagcompound = new NBTTagCompound();
                            NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
                            nmsStack.getTagCompound().setTag("SkullOwner", nbttagcompound);
                        }
                        drops.add(CraftItemStack.asBukkitCopy(nmsStack));
                    }
                    else if (Blocks.COCOA == block) {
                        final int age = data.getValue((IProperty<Integer>)BlockCocoa.AGE);
                        for (int dropAmount = (age >= 2) ? 3 : 1, j = 0; j < dropAmount; ++j) {
                            drops.add(new ItemStack(Material.INK_SACK, 1, (short)3));
                        }
                    }
                    else {
                        drops.add(new ItemStack(CraftMagicNumbers.getMaterial(item), 1, (short)block.damageDropped(data)));
                    }
                }
            }
        }
        return drops;
    }
    
    @Override
    public Collection<ItemStack> getDrops(final ItemStack item) {
        if (this.itemCausesDrops(item)) {
            return this.getDrops();
        }
        return /*(Collection<ItemStack>)*/Collections.emptyList();
    }
    
    @Override
    public void setMetadata(final String metadataKey, final MetadataValue newMetadataValue) {
        this.chunk.getCraftWorld().getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }
    
    @Override
    public List<MetadataValue> getMetadata(final String metadataKey) {
        return this.chunk.getCraftWorld().getBlockMetadata().getMetadata(this, metadataKey);
    }
    
    @Override
    public boolean hasMetadata(final String metadataKey) {
        return this.chunk.getCraftWorld().getBlockMetadata().hasMetadata(this, metadataKey);
    }
    
    @Override
    public void removeMetadata(final String metadataKey, final Plugin owningPlugin) {
        this.chunk.getCraftWorld().getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }
}
