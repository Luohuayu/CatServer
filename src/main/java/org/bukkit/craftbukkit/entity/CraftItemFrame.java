// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.apache.commons.lang.Validate;
import org.bukkit.Rotation;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.BlockFace;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame extends CraftHanging implements ItemFrame
{
    public CraftItemFrame(final CraftServer server, final EntityItemFrame entity) {
        super(server, entity);
    }
    
    @Override
    public boolean setFacingDirection(final BlockFace face, final boolean force) {
        if (!super.setFacingDirection(face, force)) {
            return false;
        }
        this.update();
        return true;
    }
    
    private void update() {
        final EntityItemFrame old = this.getHandle();
        final WorldServer world = ((CraftWorld)this.getWorld()).getHandle();
        final BlockPos position = old.getHangingPosition();
        final EnumFacing direction = old.getHorizontalFacing();
        final ItemStack item = (old.getDisplayedItem() != null) ? old.getDisplayedItem().copy() : null;
        old.setDead();
        final EntityItemFrame frame = new EntityItemFrame(world, position, direction);
        frame.setDisplayedItem(item);
        world.spawnEntityInWorld(frame);
        this.entity = frame;
    }
    
    @Override
    public void setItem(final org.bukkit.inventory.ItemStack item) {
        if (item == null || item.getTypeId() == 0) {
            this.getHandle().setDisplayedItem(null);
        }
        else {
            this.getHandle().setDisplayedItem(CraftItemStack.asNMSCopy(item));
        }
    }
    
    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getDisplayedItem());
    }
    
    @Override
    public Rotation getRotation() {
        return this.toBukkitRotation(this.getHandle().getRotation());
    }
    
    Rotation toBukkitRotation(final int value) {
        switch (value) {
            case 0: {
                return Rotation.NONE;
            }
            case 1: {
                return Rotation.CLOCKWISE_45;
            }
            case 2: {
                return Rotation.CLOCKWISE;
            }
            case 3: {
                return Rotation.CLOCKWISE_135;
            }
            case 4: {
                return Rotation.FLIPPED;
            }
            case 5: {
                return Rotation.FLIPPED_45;
            }
            case 6: {
                return Rotation.COUNTER_CLOCKWISE;
            }
            case 7: {
                return Rotation.COUNTER_CLOCKWISE_45;
            }
            default: {
                throw new AssertionError((Object)("Unknown rotation " + value + " for " + this.getHandle()));
            }
        }
    }
    
    @Override
    public void setRotation(final Rotation rotation) {
        Validate.notNull((Object)rotation, "Rotation cannot be null");
        this.getHandle().setItemRotation(toInteger(rotation));
    }
    
    static int toInteger(final Rotation rotation) {
        switch (rotation) {
            case NONE: {
                return 0;
            }
            case CLOCKWISE_45: {
                return 1;
            }
            case CLOCKWISE: {
                return 2;
            }
            case CLOCKWISE_135: {
                return 3;
            }
            case FLIPPED: {
                return 4;
            }
            case FLIPPED_45: {
                return 5;
            }
            case COUNTER_CLOCKWISE: {
                return 6;
            }
            case COUNTER_CLOCKWISE_45: {
                return 7;
            }
            default: {
                throw new IllegalArgumentException(rotation + " is not applicable to an ItemFrame");
            }
        }
    }
    
    @Override
    public EntityItemFrame getHandle() {
        return (EntityItemFrame)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftItemFrame{item=" + this.getItem() + ", rotation=" + this.getRotation() + "}";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.ITEM_FRAME;
    }
}
