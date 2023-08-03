/*
 * Minecraft Forge
 * Copyright (c) 2016-2021.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.items;

import net.minecraft.block.Block;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class VanillaInventoryCodeHooks
{
    /**
     * Copied from TileEntityHopper#captureDroppedItems and added capability support
     * @return Null if we did nothing {no IItemHandler}, True if we moved an item, False if we moved no items
     */
    @Nullable
    public static Boolean extractHook(IHopper dest)
    {
        return getItemHandler(dest, Direction.UP)
                .map(itemHandlerResult -> {
                    IItemHandler handler = itemHandlerResult.getKey();

                    if (itemHandlerResult.getValue() instanceof net.minecraft.inventory.IInventory) return null; // CatServer - handle in vanilla

                    for (int i = 0; i < handler.getSlots(); i++)
                    {
                        ItemStack extractItem = handler.extractItem(i, 1, true);
                        if (!extractItem.isEmpty())
                        {
                            for (int j = 0; j < dest.getContainerSize(); j++)
                            {
                                ItemStack destStack = dest.getItem(j);
                                if (dest.canPlaceItem(j, extractItem) && (destStack.isEmpty() || destStack.getCount() < destStack.getMaxStackSize() && destStack.getCount() < dest.getMaxStackSize() && ItemHandlerHelper.canItemStacksStack(extractItem, destStack)))
                                {
                                    extractItem = handler.extractItem(i, 1, false);
                                    if (destStack.isEmpty())
                                        dest.setItem(j, extractItem);
                                    else
                                    {
                                        destStack.grow(1);
                                        dest.setItem(j, destStack);
                                    }
                                    dest.setChanged();
                                    return true;
                                }
                            }
                        }
                    }

                    return false;
                })
                .orElse(null); // TODO bad null
    }

    /**
     * Copied from BlockDropper#dispense and added capability support
     */
    public static boolean dropperInsertHook(World world, BlockPos pos, DispenserTileEntity dropper, int slot, @Nonnull ItemStack stack)
    {
        Direction enumfacing = world.getBlockState(pos).getValue(DropperBlock.FACING);
        BlockPos blockpos = pos.relative(enumfacing);
        return getItemHandler(world, (double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), enumfacing.getOpposite())
                .map(destinationResult -> {
                    IItemHandler itemHandler = destinationResult.getKey();
                    Object destination = destinationResult.getValue();
                    // CatServer start
                    ItemStack originNMSStack = stack.copy().split(1);
                    ItemStack resultNMSStack = originNMSStack;

                    org.bukkit.event.inventory.InventoryMoveItemEvent event = null;

                    org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack oitemstack = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asCraftMirror(originNMSStack);

                    org.bukkit.inventory.InventoryHolder owner = catserver.server.inventory.CatInventoryUtils.getOwner((TileEntity) destination);
                    org.bukkit.inventory.Inventory destinationInventory = owner != null ? owner.getInventory() : catserver.server.inventory.CatCustomInventory.getInventoryFromForge(itemHandler);

                    if (destinationInventory != null) {
                        event = new org.bukkit.event.inventory.InventoryMoveItemEvent(catserver.server.inventory.CatInventoryUtils.getBukkitInventory(dropper), oitemstack, destinationInventory, true);
                        world.getCBServer().getPluginManager().callEvent(event);

                        if (event.isCancelled()) {
                            return false;
                        }

                        if (event.isCallSetItem) resultNMSStack = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(event.getRawItem());
                    }

                    ItemStack remainder = putStackInInventoryAllSlots(dropper, destination, itemHandler, resultNMSStack);
                    if ((event == null || !event.isCallSetItem || ItemStack.matches(resultNMSStack, originNMSStack)) && remainder.isEmpty())
                    {
                        remainder = stack.copy();
                        remainder.shrink(1);
                    }
                    else
                    {
                        remainder = stack.copy();
                    }
                    // CatServer end
                    dropper.setItem(slot, remainder);
                    return false;
                })
                .orElse(true);
    }

    /**
     * Copied from TileEntityHopper#transferItemsOut and added capability support
     */
    public static boolean insertHook(HopperTileEntity hopper)
    {
        Direction hopperFacing = hopper.getBlockState().getValue(HopperBlock.FACING);
        return getItemHandler(hopper, hopperFacing)
                .map(destinationResult -> {
                    IItemHandler itemHandler = destinationResult.getKey();
                    Object destination = destinationResult.getValue();
                    if (isFull(itemHandler))
                    {
                        return false;
                    }
                    else
                    {
                        boolean foundItem = false; // CatServer
                        for (int i = 0; i < hopper.getContainerSize(); ++i)
                        {
                            if (!hopper.getItem(i).isEmpty())
                            {
                                foundItem = true;
                                ItemStack originalSlotContents = hopper.getItem(i).copy();
                                // CatServer start - Optimized of call event when pushing items into other inventories
                                ItemStack originNMSStack = hopper.removeItem(i, 1); // CatServer
                                ItemStack resultNMSStack = originNMSStack;

                                org.bukkit.event.inventory.InventoryMoveItemEvent event = null;
                                if (true /*!TileEntityHopper.skipHopperEvents*/) {
                                    org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack remainder = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asCraftMirror(originNMSStack);

                                    org.bukkit.inventory.InventoryHolder owner = catserver.server.inventory.CatInventoryUtils.getOwner((TileEntity) destination);
                                    org.bukkit.inventory.Inventory destinationInventory = owner != null ? owner.getInventory() : catserver.server.inventory.CatCustomInventory.getInventoryFromForge(itemHandler);

                                    if (destinationInventory != null) {
                                        event = new org.bukkit.event.inventory.InventoryMoveItemEvent(catserver.server.inventory.CatInventoryUtils.getBukkitInventory(hopper), remainder, destinationInventory, true);
                                        org.bukkit.Bukkit.getPluginManager().callEvent(event); //CatServer

                                        if (event.isCancelled()) {
                                            return true;
                                        }

                                        if (event.isCallSetItem) resultNMSStack = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(event.getRawItem());
                                    }
                                }

                                int origCount = resultNMSStack.getCount();
                                ItemStack itemstack1 = putStackInInventoryAllSlots(hopper, destination, itemHandler, resultNMSStack);

                                if (itemstack1.isEmpty())
                                {
                                    if (event == null || !event.isCallSetItem || ItemStack.matches(resultNMSStack, originNMSStack)) {
                                        ((TileEntity) destination).setChanged();
                                    } else {
                                        hopper.setItem(i, originalSlotContents);
                                    }
                                    return true;
                                }
                                originalSlotContents.shrink(origCount - itemstack1.getCount());
                                // CatServer end
                                hopper.setItem(i, originalSlotContents);
                            }
                        }
                        if (foundItem) hopper.setCooldown(8); // CatServer - Inventory was full - cooldown
                        return false;
                    }
                })
                .orElse(false);
    }

    private static ItemStack putStackInInventoryAllSlots(TileEntity source, Object destination, IItemHandler destInventory, ItemStack stack)
    {
        for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++)
        {
            stack = insertStack(source, destination, destInventory, stack, slot);
        }
        return stack;
    }

    /**
     * Copied from TileEntityHopper#insertStack and added capability support
     */
    private static ItemStack insertStack(TileEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot)
    {
        ItemStack itemstack = destInventory.getStackInSlot(slot);

        if (destInventory.insertItem(slot, stack, true).isEmpty())
        {
            boolean insertedItem = false;
            boolean inventoryWasEmpty = isEmpty(destInventory);

            if (itemstack.isEmpty())
            {
                destInventory.insertItem(slot, stack, false);
                stack = ItemStack.EMPTY;
                insertedItem = true;
            }
            else if (ItemHandlerHelper.canItemStacksStack(itemstack, stack))
            {
                int originalSize = stack.getCount();
                stack = destInventory.insertItem(slot, stack, false);
                insertedItem = originalSize < stack.getCount();
            }

            if (insertedItem)
            {
                if (inventoryWasEmpty && destination instanceof HopperTileEntity)
                {
                    HopperTileEntity destinationHopper = (HopperTileEntity)destination;

                    if (!destinationHopper.isOnCustomCooldown())
                    {
                        int k = 0;
                        if (source instanceof HopperTileEntity)
                        {
                            if (destinationHopper.getLastUpdateTime() >= ((HopperTileEntity) source).getLastUpdateTime())
                            {
                                k = 1;
                            }
                        }
                        destinationHopper.setCooldown(8 - k);
                    }
                }
            }
        }

        return stack;
    }

    private static Optional<Pair<IItemHandler, Object>> getItemHandler(IHopper hopper, Direction hopperFacing)
    {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getItemHandler(hopper.getLevel(), x, y, z, hopperFacing.getOpposite());
    }

    private static boolean isFull(IItemHandler itemHandler)
    {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++)
        {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || stackInSlot.getCount() < itemHandler.getSlotLimit(slot))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(IItemHandler itemHandler)
    {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++)
        {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.getCount() > 0)
            {
                return false;
            }
        }
        return true;
    }

    public static Optional<Pair<IItemHandler, Object>> getItemHandler(World worldIn, double x, double y, double z, final Direction side)
    {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        net.minecraft.block.BlockState state = worldIn.getBlockState(blockpos);

        if (state.hasTileEntity())
        {
            TileEntity tileentity = worldIn.getBlockEntity(blockpos);
            if (tileentity != null)
            {
                return tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)
                    .map(capability -> ImmutablePair.<IItemHandler, Object>of(capability, tileentity));
            }
        }

        return Optional.empty();
    }
}
