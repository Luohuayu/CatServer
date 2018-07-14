// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import com.google.common.base.Preconditions;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import org.apache.commons.lang.Validate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import java.util.Arrays;
import org.bukkit.inventory.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.PlayerInventory;

public class CraftInventoryPlayer extends CraftInventory implements PlayerInventory, EntityEquipment
{
    public CraftInventoryPlayer(final InventoryPlayer inventory) {
        super(inventory);
    }
    
    @Override
    public InventoryPlayer getInventory() {
        return (InventoryPlayer)this.inventory;
    }
    
    @Override
    public ItemStack[] getStorageContents() {
        return Arrays.copyOfRange(this.getContents(), 0, this.getInventory().mainInventory.length);
    }
    
    @Override
    public ItemStack getItemInMainHand() {
        return CraftItemStack.asCraftMirror(this.getInventory().getCurrentItem());
    }
    
    @Override
    public void setItemInMainHand(final ItemStack item) {
        this.setItem(this.getHeldItemSlot(), item);
    }
    
    @Override
    public ItemStack getItemInOffHand() {
        return CraftItemStack.asCraftMirror(this.getInventory().offHandInventory[0]);
    }
    
    @Override
    public void setItemInOffHand(final ItemStack item) {
        final ItemStack[] extra = this.getExtraContents();
        extra[0] = item;
        this.setExtraContents(extra);
    }
    
    @Override
    public ItemStack getItemInHand() {
        return this.getItemInMainHand();
    }
    
    @Override
    public void setItemInHand(final ItemStack stack) {
        this.setItemInMainHand(stack);
    }
    
    @Override
    public void setItem(int index, final ItemStack item) {
        super.setItem(index, item);
        if (this.getHolder() == null) {
            return;
        }
        final EntityPlayerMP player = ((CraftPlayer)this.getHolder()).getHandle();
        if (player.connection == null) {
            return;
        }
        if (index < InventoryPlayer.getHotbarSize()) {
            index += 36;
        }
        else if (index > 39) {
            index += 5;
        }
        else if (index > 35) {
            index = 8 - (index - 36);
        }
        player.connection.sendPacket(new SPacketSetSlot(player.inventoryContainer.windowId, index, CraftItemStack.asNMSCopy(item)));
    }
    
    @Override
    public int getHeldItemSlot() {
        return this.getInventory().currentItem;
    }
    
    @Override
    public void setHeldItemSlot(final int slot) {
        Validate.isTrue(slot >= 0 && slot < InventoryPlayer.getHotbarSize(), "Slot is not between 0 and 8 inclusive");
        this.getInventory().currentItem = slot;
        ((CraftPlayer)this.getHolder()).getHandle().connection.sendPacket(new SPacketHeldItemChange(slot));
    }
    
    @Override
    public ItemStack getHelmet() {
        return this.getItem(this.getSize() - 2);
    }
    
    @Override
    public ItemStack getChestplate() {
        return this.getItem(this.getSize() - 3);
    }
    
    @Override
    public ItemStack getLeggings() {
        return this.getItem(this.getSize() - 4);
    }
    
    @Override
    public ItemStack getBoots() {
        return this.getItem(this.getSize() - 5);
    }
    
    @Override
    public void setHelmet(final ItemStack helmet) {
        this.setItem(this.getSize() - 2, helmet);
    }
    
    @Override
    public void setChestplate(final ItemStack chestplate) {
        this.setItem(this.getSize() - 3, chestplate);
    }
    
    @Override
    public void setLeggings(final ItemStack leggings) {
        this.setItem(this.getSize() - 4, leggings);
    }
    
    @Override
    public void setBoots(final ItemStack boots) {
        this.setItem(this.getSize() - 5, boots);
    }
    
    @Override
    public ItemStack[] getArmorContents() {
        final int start = this.getInventory().mainInventory.length;
        return Arrays.copyOfRange(this.getContents(), start, start + this.getInventory().armorInventory.length);
    }
    
    private void setSlots(ItemStack[] items, final int baseSlot, final int length) {
        if (items == null) {
            items = new ItemStack[length];
        }
        Preconditions.checkArgument(items.length <= length, "items.length must be < %s", new Object[] { length });
        for (int i = 0; i < length; ++i) {
            if (i >= items.length) {
                this.setItem(baseSlot + i, null);
            }
            else {
                this.setItem(baseSlot + i, items[i]);
            }
        }
    }
    
    @Override
    public void setStorageContents(final ItemStack[] items) throws IllegalArgumentException {
        this.setSlots(items, 0, this.getInventory().mainInventory.length);
    }
    
    @Override
    public void setArmorContents(final ItemStack[] items) {
        this.setSlots(items, this.getInventory().mainInventory.length, this.getInventory().armorInventory.length);
    }
    
    @Override
    public ItemStack[] getExtraContents() {
        final int start = this.getInventory().mainInventory.length + this.getInventory().armorInventory.length;
        return Arrays.copyOfRange(this.getContents(), start, start + this.getInventory().offHandInventory.length);
    }
    
    @Override
    public void setExtraContents(final ItemStack[] items) {
        this.setSlots(items, this.getInventory().mainInventory.length + this.getInventory().armorInventory.length, this.getInventory().offHandInventory.length);
    }
    
    @Override
    public int clear(final int id, final int data) {
        int count = 0;
        final ItemStack[] items = this.getContents();
        for (int i = 0; i < items.length; ++i) {
            final ItemStack item = items[i];
            if (item != null) {
                if (id <= -1 || item.getTypeId() == id) {
                    if (data <= -1 || item.getData().getData() == data) {
                        count += item.getAmount();
                        this.setItem(i, null);
                    }
                }
            }
        }
        return count;
    }
    
    @Override
    public HumanEntity getHolder() {
        return (HumanEntity)this.inventory.getOwner();
    }
    
    @Override
    public float getItemInHandDropChance() {
        return this.getItemInMainHandDropChance();
    }
    
    @Override
    public void setItemInHandDropChance(final float chance) {
        this.setItemInMainHandDropChance(chance);
    }
    
    @Override
    public float getItemInMainHandDropChance() {
        return 1.0f;
    }
    
    @Override
    public void setItemInMainHandDropChance(final float chance) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float getItemInOffHandDropChance() {
        return 1.0f;
    }
    
    @Override
    public void setItemInOffHandDropChance(final float chance) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float getHelmetDropChance() {
        return 1.0f;
    }
    
    @Override
    public void setHelmetDropChance(final float chance) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float getChestplateDropChance() {
        return 1.0f;
    }
    
    @Override
    public void setChestplateDropChance(final float chance) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float getLeggingsDropChance() {
        return 1.0f;
    }
    
    @Override
    public void setLeggingsDropChance(final float chance) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float getBootsDropChance() {
        return 1.0f;
    }
    
    @Override
    public void setBootsDropChance(final float chance) {
        throw new UnsupportedOperationException();
    }
}
