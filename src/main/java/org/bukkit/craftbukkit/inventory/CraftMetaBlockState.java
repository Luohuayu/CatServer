// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import java.util.Set;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import java.util.List;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.block.CraftFlowerPot;
import net.minecraft.tileentity.TileEntityFlowerPot;
import org.bukkit.craftbukkit.block.CraftBanner;
import net.minecraft.tileentity.TileEntityBanner;
import org.bukkit.craftbukkit.block.CraftBeacon;
import net.minecraft.tileentity.TileEntityBeacon;
import org.bukkit.craftbukkit.block.CraftCommandBlock;
import net.minecraft.tileentity.TileEntityCommandBlock;
import org.bukkit.craftbukkit.block.CraftSkull;
import net.minecraft.tileentity.TileEntitySkull;
import org.bukkit.craftbukkit.block.CraftBrewingStand;
import net.minecraft.tileentity.TileEntityBrewingStand;
import org.bukkit.craftbukkit.block.CraftJukebox;
import net.minecraft.block.BlockJukebox;
import org.bukkit.craftbukkit.block.CraftNoteBlock;
import net.minecraft.tileentity.TileEntityNote;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import net.minecraft.tileentity.TileEntityMobSpawner;
import org.bukkit.craftbukkit.block.CraftHopper;
import net.minecraft.tileentity.TileEntityHopper;
import org.bukkit.craftbukkit.block.CraftEndGateway;
import net.minecraft.tileentity.TileEntityEndGateway;
import org.bukkit.craftbukkit.block.CraftDropper;
import net.minecraft.tileentity.TileEntityDropper;
import org.bukkit.craftbukkit.block.CraftDispenser;
import net.minecraft.tileentity.TileEntityDispenser;
import org.bukkit.craftbukkit.block.CraftFurnace;
import net.minecraft.tileentity.TileEntityFurnace;
import org.bukkit.craftbukkit.block.CraftChest;
import net.minecraft.tileentity.TileEntityChest;
import org.bukkit.craftbukkit.block.CraftSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import org.bukkit.block.BlockState;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTBase;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.BlockStateMeta;

//@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBlockState extends CraftMetaItem implements BlockStateMeta
{
    static final ItemMetaKey BLOCK_ENTITY_TAG;
    final Material material;
    NBTTagCompound blockEntityTag;
    
    static {
        BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");
    }
    
    CraftMetaBlockState(final CraftMetaItem meta, final Material material) {
        super(meta);
        this.material = material;
        if (!(meta instanceof CraftMetaBlockState) || ((CraftMetaBlockState)meta).material != material) {
            this.blockEntityTag = null;
            return;
        }
        final CraftMetaBlockState te = (CraftMetaBlockState)meta;
        this.blockEntityTag = te.blockEntityTag;
    }
    
    CraftMetaBlockState(final NBTTagCompound tag, final Material material) {
        super(tag);
        this.material = material;
        if (tag.hasKey(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, 10)) {
            this.blockEntityTag = tag.getCompoundTag(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT);
        }
        else {
            this.blockEntityTag = null;
        }
    }
    
    CraftMetaBlockState(final Map<String, Object> map) {
        super(map);
        final String matName = SerializableMeta.getString(map, "blockMaterial", true);
        final Material m = Material.getMaterial(matName);
        if (m != null) {
            this.material = m;
        }
        else {
            this.material = Material.AIR;
        }
    }
    
    @Override
    void applyToItem(final NBTTagCompound tag) {
        super.applyToItem(tag);
        if (this.blockEntityTag != null) {
            tag.setTag(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, this.blockEntityTag);
        }
    }
    
    @Override
    void deserializeInternal(final NBTTagCompound tag) {
        if (tag.hasKey(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, 10)) {
            this.blockEntityTag = tag.getCompoundTag(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT);
        }
    }
    
    @Override
    void serializeInternal(final Map<String, NBTBase> internalTags) {
        if (this.blockEntityTag != null) {
            internalTags.put(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, this.blockEntityTag);
        }
    }
    
    @Override
    ImmutableMap.Builder<String, Object> serialize(final ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put(/*(Object)*/"blockMaterial", /*(Object)*/this.material.name());
        return builder;
    }
    
    @Override
    int applyHash() {
        int hash;
        final int original = hash = super.applyHash();
        if (this.blockEntityTag != null) {
            hash = 61 * hash + this.blockEntityTag.hashCode();
        }
        return (original != hash) ? (CraftMetaBlockState.class.hashCode() ^ hash) : hash;
    }
    
    public boolean equalsCommon(final CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBlockState) {
            final CraftMetaBlockState that = (CraftMetaBlockState)meta;
            return Objects.equal((Object)this.blockEntityTag, (Object)that.blockEntityTag);
        }
        return true;
    }
    
    @Override
    boolean notUncommon(final CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || this.blockEntityTag == null);
    }
    
    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.blockEntityTag == null;
    }
    
    @Override
    boolean applicableTo(final Material type) {
        switch (type) {
            case DISPENSER:
            case NOTE_BLOCK:
            case PISTON_BASE:
            case MOB_SPAWNER:
            case CHEST:
            case FURNACE:
            case JUKEBOX:
            case ENCHANTMENT_TABLE:
            case COMMAND:
            case BEACON:
            case TRAPPED_CHEST:
            case DAYLIGHT_DETECTOR:
            case HOPPER:
            case DROPPER:
            case DAYLIGHT_DETECTOR_INVERTED:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN:
            case SIGN:
            case BREWING_STAND_ITEM:
            case FLOWER_POT_ITEM:
            case REDSTONE_COMPARATOR:
            case SHIELD: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public boolean hasBlockState() {
        return this.blockEntityTag != null;
    }
    
    @Override
    public BlockState getBlockState() {
        if (this.blockEntityTag != null && this.material == Material.SHIELD) {
            this.blockEntityTag.setString("id", "Banner");
        }
        TileEntity te = (this.blockEntityTag == null) ? null : TileEntity.create(null, this.blockEntityTag);
        switch (this.material) {
            case SIGN_POST:
            case WALL_SIGN:
            case SIGN: {
                if (te == null) {
                    te = new TileEntitySign();
                }
                return new CraftSign(this.material, (TileEntitySign)te);
            }
            case CHEST:
            case TRAPPED_CHEST: {
                if (te == null) {
                    te = new TileEntityChest();
                }
                return new CraftChest(this.material, (TileEntityChest)te);
            }
            case FURNACE:
            case BURNING_FURNACE: {
                if (te == null) {
                    te = new TileEntityFurnace();
                }
                return new CraftFurnace(this.material, (TileEntityFurnace)te);
            }
            case DISPENSER: {
                if (te == null) {
                    te = new TileEntityDispenser();
                }
                return new CraftDispenser(this.material, (TileEntityDispenser)te);
            }
            case DROPPER: {
                if (te == null) {
                    te = new TileEntityDispenser();
                }
                return new CraftDropper(this.material, (TileEntityDropper)te);
            }
            case END_GATEWAY: {
                if (te == null) {
                    te = new TileEntityEndGateway();
                }
                return new CraftEndGateway(this.material, (TileEntityEndGateway)te);
            }
            case HOPPER: {
                if (te == null) {
                    te = new TileEntityHopper();
                }
                return new CraftHopper(this.material, (TileEntityHopper)te);
            }
            case MOB_SPAWNER: {
                if (te == null) {
                    te = new TileEntityMobSpawner();
                }
                return new CraftCreatureSpawner(this.material, (TileEntityMobSpawner)te);
            }
            case NOTE_BLOCK: {
                if (te == null) {
                    te = new TileEntityNote();
                }
                return new CraftNoteBlock(this.material, (TileEntityNote)te);
            }
            case JUKEBOX: {
                if (te == null) {
                    te = new BlockJukebox.TileEntityJukebox();
                }
                return new CraftJukebox(this.material, (BlockJukebox.TileEntityJukebox)te);
            }
            case BREWING_STAND: {
                if (te == null) {
                    te = new TileEntityBrewingStand();
                }
                return new CraftBrewingStand(this.material, (TileEntityBrewingStand)te);
            }
            case SKULL: {
                if (te == null) {
                    te = new TileEntitySkull();
                }
                return new CraftSkull(this.material, (TileEntitySkull)te);
            }
            case COMMAND:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN: {
                if (te == null) {
                    te = new TileEntityCommandBlock();
                }
                return new CraftCommandBlock(this.material, (TileEntityCommandBlock)te);
            }
            case BEACON: {
                if (te == null) {
                    te = new TileEntityBeacon();
                }
                return new CraftBeacon(this.material, (TileEntityBeacon)te);
            }
            case STANDING_BANNER:
            case WALL_BANNER:
            case BANNER:
            case SHIELD: {
                if (te == null) {
                    te = new TileEntityBanner();
                }
                return new CraftBanner(this.material, (TileEntityBanner)te);
            }
            case FLOWER_POT_ITEM: {
                if (te == null) {
                    te = new TileEntityFlowerPot();
                }
                return new CraftFlowerPot(this.material, (TileEntityFlowerPot)te);
            }
            default: {
                throw new IllegalStateException("Missing blockState for " + this.material);
            }
        }
    }
    
    @Override
    public void setBlockState(final BlockState blockState) {
        Validate.notNull((Object)blockState, "blockState must not be null");
        final TileEntity te = ((CraftBlockState)blockState).getTileEntity();
        Validate.notNull((Object)te, "Invalid blockState");
        boolean valid = false;
        switch (this.material) {
            case SIGN_POST:
            case WALL_SIGN:
            case SIGN: {
                valid = (te instanceof TileEntitySign);
                break;
            }
            case CHEST:
            case TRAPPED_CHEST: {
                valid = (te instanceof TileEntityChest);
                break;
            }
            case FURNACE:
            case BURNING_FURNACE: {
                valid = (te instanceof TileEntityFurnace);
                break;
            }
            case DISPENSER: {
                valid = (te instanceof TileEntityDispenser);
                break;
            }
            case DROPPER: {
                valid = (te instanceof TileEntityDropper);
                break;
            }
            case END_GATEWAY: {
                valid = (te instanceof TileEntityEndGateway);
                break;
            }
            case HOPPER: {
                valid = (te instanceof TileEntityHopper);
                break;
            }
            case MOB_SPAWNER: {
                valid = (te instanceof TileEntityMobSpawner);
                break;
            }
            case NOTE_BLOCK: {
                valid = (te instanceof TileEntityNote);
                break;
            }
            case JUKEBOX: {
                valid = (te instanceof BlockJukebox.TileEntityJukebox);
                break;
            }
            case BREWING_STAND: {
                valid = (te instanceof TileEntityBrewingStand);
                break;
            }
            case SKULL: {
                valid = (te instanceof TileEntitySkull);
                break;
            }
            case COMMAND:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN: {
                valid = (te instanceof TileEntityCommandBlock);
                break;
            }
            case BEACON: {
                valid = (te instanceof TileEntityBeacon);
                break;
            }
            case STANDING_BANNER:
            case WALL_BANNER:
            case BANNER:
            case SHIELD: {
                valid = (te instanceof TileEntityBanner);
                break;
            }
            case FLOWER_POT_ITEM: {
                valid = (te instanceof TileEntityFlowerPot);
                break;
            }
            default: {
                valid = false;
                break;
            }
        }
        Validate.isTrue(valid, "Invalid blockState for " + this.material);
        te.writeToNBT(this.blockEntityTag = new NBTTagCompound());
    }
}
