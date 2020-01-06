package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import catserver.server.inventory.CraftCustomContainer;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;

import javax.annotation.Resource;

public class CraftBlock implements Block {
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;

    public CraftBlock(CraftChunk chunk, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunk = chunk;
    }

    private net.minecraft.block.Block getNMSBlock() {
        return CraftMagicNumbers.getBlock(this); // TODO: UPDATE THIS
    }

    private static net.minecraft.block.Block getNMSBlock(int type) {
        return CraftMagicNumbers.getBlock(type);
    }

    public World getWorld() {
        return chunk.getWorld();
    }

    public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }

    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(x);
            loc.setY(y);
            loc.setZ(z);
            loc.setYaw(0);
            loc.setPitch(0);
        }

        return loc;
    }

    public BlockVector getVector() {
        return new BlockVector(x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setData(final byte data) {
        setData(data, 3);
    }

    public void setData(final byte data, boolean applyPhysics) {
        if (applyPhysics) {
            setData(data, 3);
        } else {
            setData(data, 2);
        }
    }

    private void setData(final byte data, int flag) {
        net.minecraft.world.World world = chunk.getHandle().getWorld();
        BlockPos position = new BlockPos(x, y, z);
        IBlockState blockData = world.getBlockState(position);
        world.setBlockState(position, blockData.getBlock().getStateFromMeta(data), flag);
    }

    private IBlockState getData0() {
        return chunk.getHandle().getBlockState(new BlockPos(x, y, z));
    }

    public byte getData() {
        IBlockState blockData = chunk.getHandle().getBlockState(new BlockPos(x, y, z));
        return (byte) blockData.getBlock().getMetaFromState(blockData);
    }

    public void setType(final Material type) {
        setType(type, true);
    }

    @Override
    public void setType(Material type, boolean applyPhysics) {
        setTypeId(type.getId(), applyPhysics);
    }

    public boolean setTypeId(final int type) {
        return setTypeId(type, true);
    }

    public boolean setTypeId(final int type, final boolean applyPhysics) {
        net.minecraft.block.Block block = getNMSBlock(type);
        return setTypeIdAndData(type, (byte) block.getMetaFromState(block.getDefaultState()), applyPhysics);
    }

    public boolean setTypeIdAndData(final int type, final byte data, final boolean applyPhysics) {
        IBlockState blockData = getNMSBlock(type).getStateFromMeta(data);
        BlockPos position = new BlockPos(x, y, z);

        // SPIGOT-611: need to do this to prevent glitchiness. Easier to handle this here (like /setblock) than to fix weirdness in tile entity cleanup
        if (type != 0 && blockData.getBlock() instanceof BlockContainer && type != getTypeId()) {
            chunk.getHandle().getWorld().setBlockState(position, Blocks.AIR.getDefaultState(), 0);
        }

        if (applyPhysics) {
            return chunk.getHandle().getWorld().setBlockState(position, blockData, 3);
        } else {
            IBlockState old = chunk.getHandle().getBlockState(position);
            boolean success = chunk.getHandle().getWorld().setBlockState(position, blockData, 18); // NOTIFY | NO_OBSERVER
            if (success) {
                chunk.getHandle().getWorld().notifyBlockUpdate(
                        position,
                        old,
                        blockData,
                        3
                );
            }
            return success;
        }
    }

    public Material getType() {
        return Material.getBlockMaterial(getTypeId());
    }

    @Deprecated
    @Override
    public int getTypeId() {
        return CraftMagicNumbers.getId(chunk.getHandle().getBlockState(new BlockPos(this.x, this.y, this.z)).getBlock());
    }

    public byte getLightLevel() {
        return (byte) chunk.getHandle().getWorld().getLightFromNeighbors(new BlockPos(this.x, this.y, this.z));
    }

    public byte getLightFromSky() {
        return (byte) chunk.getHandle().getWorld().getLightFor(EnumSkyBlock.SKY, new BlockPos(this.x, this.y, this.z));
    }

    public byte getLightFromBlocks() {
        return (byte) chunk.getHandle().getWorld().getLightFor(EnumSkyBlock.BLOCK, new BlockPos(this.x, this.y, this.z));
    }


    public Block getFace(final BlockFace face) {
        return getRelative(face, 1);
    }

    public Block getFace(final BlockFace face, final int distance) {
        return getRelative(face, distance);
    }

    public Block getRelative(final int modX, final int modY, final int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

    public Block getRelative(BlockFace face) {
        return getRelative(face, 1);
    }

    public Block getRelative(BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    public BlockFace getFace(final Block block) {
        BlockFace[] values = BlockFace.values();

        for (BlockFace face : values) {
            if ((this.getX() + face.getModX() == block.getX()) &&
                (this.getY() + face.getModY() == block.getY()) &&
                (this.getZ() + face.getModZ() == block.getZ())
            ) {
                return face;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "CraftBlock{" + "chunk=" + chunk + ",x=" + x + ",y=" + y + ",z=" + z + ",type=" + getType() + ",data=" + getData() + '}';
    }

    public static BlockFace notchToBlockFace(EnumFacing notch) {
        if (notch == null) return BlockFace.SELF;
        switch (notch) {
        case DOWN:
            return BlockFace.DOWN;
        case UP:
            return BlockFace.UP;
        case NORTH:
            return BlockFace.NORTH;
        case SOUTH:
            return BlockFace.SOUTH;
        case WEST:
            return BlockFace.WEST;
        case EAST:
            return BlockFace.EAST;
        default:
            return BlockFace.SELF;
        }
    }

    public static EnumFacing blockFaceToNotch(BlockFace face) {
        switch (face) {
        case DOWN:
            return EnumFacing.DOWN;
        case UP:
            return EnumFacing.UP;
        case NORTH:
            return EnumFacing.NORTH;
        case SOUTH:
            return EnumFacing.SOUTH;
        case WEST:
            return EnumFacing.WEST;
        case EAST:
            return EnumFacing.EAST;
        default:
            return null;
        }
    }

    public BlockState getState() {
        Material material = getType();
        // CatServer start - handle if null
        if (material == null) {
            TileEntity tileEntity = chunk.getCraftWorld().getTileEntityAt(x, y, z);
            if (tileEntity != null) {
                // block with IInventory
                if (tileEntity instanceof IInventory) {
                    return new CraftCustomContainer(this);
                }
                // block with unhandled TileEntity:
                return new CraftBlockEntityState<>(this, tileEntity.getClass());
            } else {
                // Block without TileEntity:
                return new CraftBlockState(this);
            }
        }
        // CatServer end
        switch (material) {
        case SIGN:
        case SIGN_POST:
        case WALL_SIGN:
            return new CraftSign(this);
        case CHEST:
        case TRAPPED_CHEST:
            return new CraftChest(this);
        case BURNING_FURNACE:
        case FURNACE:
            return new CraftFurnace(this);
        case DISPENSER:
            return new CraftDispenser(this);
        case DROPPER:
            return new CraftDropper(this);
        case END_GATEWAY:
            return new CraftEndGateway(this);
        case HOPPER:
            return new CraftHopper(this);
        case MOB_SPAWNER:
            return new CraftCreatureSpawner(this);
        case NOTE_BLOCK:
            return new CraftNoteBlock(this);
        case JUKEBOX:
            return new CraftJukebox(this);
        case BREWING_STAND:
            return new CraftBrewingStand(this);
        case SKULL:
            return new CraftSkull(this);
        case COMMAND:
        case COMMAND_CHAIN:
        case COMMAND_REPEATING:
            return new CraftCommandBlock(this);
        case BEACON:
            return new CraftBeacon(this);
        case BANNER:
        case WALL_BANNER:
        case STANDING_BANNER:
            return new CraftBanner(this);
        case FLOWER_POT:
            return new CraftFlowerPot(this);
        case STRUCTURE_BLOCK:
            return new CraftStructureBlock(this);
        case WHITE_SHULKER_BOX:
        case ORANGE_SHULKER_BOX:
        case MAGENTA_SHULKER_BOX:
        case LIGHT_BLUE_SHULKER_BOX:
        case YELLOW_SHULKER_BOX:
        case LIME_SHULKER_BOX:
        case PINK_SHULKER_BOX:
        case GRAY_SHULKER_BOX:
        case SILVER_SHULKER_BOX:
        case CYAN_SHULKER_BOX:
        case PURPLE_SHULKER_BOX:
        case BLUE_SHULKER_BOX:
        case BROWN_SHULKER_BOX:
        case GREEN_SHULKER_BOX:
        case RED_SHULKER_BOX:
        case BLACK_SHULKER_BOX:
            return new CraftShulkerBox(this);
        case ENCHANTMENT_TABLE:
            return new CraftEnchantingTable(this);
        case ENDER_CHEST:
            return new CraftEnderChest(this);
        case DAYLIGHT_DETECTOR:
        case DAYLIGHT_DETECTOR_INVERTED:
            return new CraftDaylightDetector(this);
        case REDSTONE_COMPARATOR_OFF:
        case REDSTONE_COMPARATOR_ON:
            return new CraftComparator(this);
        case BED_BLOCK:
            return new CraftBed(this);
        default:
            TileEntity tileEntity = chunk.getCraftWorld().getTileEntityAt(x, y, z);
            if (tileEntity != null) {
                // block with IInventory
                if (tileEntity instanceof IInventory) {
                    return new CraftCustomContainer(this);
                }
                // block with unhandled TileEntity:
                return new CraftBlockEntityState<>(this, tileEntity.getClass());
            } else {
                // Block without TileEntity:
                return new CraftBlockState(this);
            }
        }
    }

    public Biome getBiome() {
        return getWorld().getBiome(x, z);
    }

    public void setBiome(Biome bio) {
        getWorld().setBiome(x, z, bio);
    }

    public static Biome biomeBaseToBiome(net.minecraft.world.biome.Biome base) {
        if (base == null) {
            return null;
        }

        return Biome.valueOf(net.minecraft.world.biome.Biome.REGISTRY.getNameForObject(base).getResourcePath().toUpperCase(java.util.Locale.ENGLISH));
    }

    public static net.minecraft.world.biome.Biome biomeToBiomeBase(Biome bio) {
        if (bio == null) {
            return null;
        }

        return net.minecraft.world.biome.Biome.REGISTRY.getObject(new ResourceLocation(bio.name().toLowerCase(java.util.Locale.ENGLISH)));
    }

    public double getTemperature() {
        return getWorld().getTemperature(x, z);
    }

    public double getHumidity() {
        return getWorld().getHumidity(x, z);
    }

    public boolean isBlockPowered() {
        return chunk.getHandle().getWorld().getStrongPower(new BlockPos(x, y, z)) > 0;
    }

    public boolean isBlockIndirectlyPowered() {
        return chunk.getHandle().getWorld().isBlockPowered(new BlockPos(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CraftBlock)) return false;
        CraftBlock other = (CraftBlock) o;

        return this.x == other.x && this.y == other.y && this.z == other.z && this.getWorld().equals(other.getWorld());
    }

    @Override
    public int hashCode() {
        return this.y << 24 ^ this.x ^ this.z ^ this.getWorld().hashCode();
    }

    public boolean isBlockFacePowered(BlockFace face) {
        return chunk.getHandle().getWorld().isSidePowered(new BlockPos(x, y, z), blockFaceToNotch(face));
    }

    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        int power = chunk.getHandle().getWorld().getRedstonePower(new BlockPos(x, y, z), blockFaceToNotch(face));

        Block relative = getRelative(face);
        if (relative.getType() == Material.REDSTONE_WIRE) {
            return Math.max(power, relative.getData()) > 0;
        }

        return power > 0;
    }

    public int getBlockPower(BlockFace face) {
        int power = 0;
        BlockRedstoneWire wire = Blocks.REDSTONE_WIRE;
        net.minecraft.world.World world = chunk.getHandle().getWorld();
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(x, y - 1, z), EnumFacing.DOWN)) power = wire.getMaxCurrentStrength(world, new BlockPos(x, y - 1, z), power);
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(x, y + 1, z), EnumFacing.UP)) power = wire.getMaxCurrentStrength(world, new BlockPos(x, y + 1, z), power);
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(x + 1, y, z), EnumFacing.EAST)) power = wire.getMaxCurrentStrength(world, new BlockPos(x + 1, y, z), power);
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(x - 1, y, z), EnumFacing.WEST)) power = wire.getMaxCurrentStrength(world, new BlockPos(x - 1, y, z), power);
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(x, y, z - 1), EnumFacing.NORTH)) power = wire.getMaxCurrentStrength(world, new BlockPos(x, y, z - 1), power);
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.isSidePowered(new BlockPos(x, y, z + 1), EnumFacing.SOUTH)) power = wire.getMaxCurrentStrength(world, new BlockPos(x, y, z - 1), power);
        return power > 0 ? power : (face == BlockFace.SELF ? isBlockIndirectlyPowered() : isBlockFaceIndirectlyPowered(face)) ? 15 : 0;
    }

    public int getBlockPower() {
        return getBlockPower(BlockFace.SELF);
    }

    public boolean isEmpty() {
        return getType() == Material.AIR;
    }

    public boolean isLiquid() {
        return (getType() == Material.WATER) || (getType() == Material.STATIONARY_WATER) || (getType() == Material.LAVA) || (getType() == Material.STATIONARY_LAVA);
    }

    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getNMSBlock().getMobilityFlag(getNMSBlock().getStateFromMeta(getData())).ordinal());
    }

    private boolean itemCausesDrops(ItemStack item) {
        net.minecraft.block.Block block = this.getNMSBlock();
        net.minecraft.item.Item itemType = item != null ? net.minecraft.item.Item.getItemById(item.getTypeId()) : null;
        return block != null && (block.getDefaultState().getMaterial().isToolNotRequired() || (itemType != null && itemType.canHarvestBlock(block.getDefaultState())));
    }

    public boolean breakNaturally() {
        // Order matters here, need to drop before setting to air so skulls can get their data
        net.minecraft.block.Block block = this.getNMSBlock();
        byte data = getData();
        boolean result = false;

        if (block != null && block != Blocks.AIR) {
            block.dropBlockAsItemWithChance(chunk.getHandle().getWorld(), new BlockPos(x, y, z), block.getStateFromMeta(data), 1.0F, 0);
            result = true;
        }

        setTypeId(Material.AIR.getId());
        return result;
    }

    public boolean breakNaturally(ItemStack item) {
        if (itemCausesDrops(item)) {
            return breakNaturally();
        } else {
            return setTypeId(Material.AIR.getId());
        }
    }

    public Collection<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<ItemStack>();

        net.minecraft.block.Block block = this.getNMSBlock();
        if (block != Blocks.AIR) {
            IBlockState data = getData0();
            // based on nms.Block.dropNaturally
            int count = block.quantityDroppedWithBonus(0, chunk.getHandle().getWorld().rand);
            for (int i = 0; i < count; ++i) {
                Item item = block.getItemDropped(data, chunk.getHandle().getWorld().rand, 0);
                if (item != Items.AIR) {
                    // Skulls are special, their data is based on the tile entity
                    if (Blocks.SKULL == block) {
                        net.minecraft.item.ItemStack nmsStack = new net.minecraft.item.ItemStack(item, 1, block.damageDropped(data));
                        TileEntitySkull tileentityskull = (TileEntitySkull) chunk.getHandle().getWorld().getTileEntity(new BlockPos(x, y, z));

                        if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null) {
                            nmsStack.setTagCompound(new NBTTagCompound());
                            NBTTagCompound nbttagcompound = new NBTTagCompound();

                            NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
                            nmsStack.getTagCompound().setTag("SkullOwner", nbttagcompound);
                        }

                        drops.add(CraftItemStack.asBukkitCopy(nmsStack));
                        // We don't want to drop cocoa blocks, we want to drop cocoa beans.
                    } else if (Blocks.COCOA == block) {
                        int age = (Integer) data.getValue(BlockCocoa.AGE);
                        int dropAmount = (age >= 2 ? 3 : 1);
                        for (int j = 0; j < dropAmount; ++j) {
                            drops.add(new ItemStack(Material.INK_SACK, 1, (short) 3));
                        }
                    } else {
                        drops.add(new ItemStack(CraftMagicNumbers.getMaterial(item), 1, (short) block.damageDropped(data)));
                    }
                }
            }
        }
        return drops;
    }

    public Collection<ItemStack> getDrops(ItemStack item) {
        if (itemCausesDrops(item)) {
            return getDrops();
        } else {
            return Collections.emptyList();
        }
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        chunk.getCraftWorld().getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return chunk.getCraftWorld().getBlockMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return chunk.getCraftWorld().getBlockMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        chunk.getCraftWorld().getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }
}
