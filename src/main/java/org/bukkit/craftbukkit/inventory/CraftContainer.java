// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import luohuayu.CatServer.inventory.CBContainer;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import net.minecraft.inventory.IInventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import net.minecraft.inventory.Container;

public class CraftContainer extends CBContainer
{
    private final InventoryView view;
    private InventoryType cachedType;
    private String cachedTitle;
    private final int cachedSize;
    
    public CraftContainer(final InventoryView view, final int id) {
        this.view = view;
        this.windowId = id;
        final IInventory top = ((CraftInventory)view.getTopInventory()).getInventory();
        final IInventory bottom = ((CraftInventory)view.getBottomInventory()).getInventory();
        this.cachedType = view.getType();
        this.cachedTitle = view.getTitle();
        this.cachedSize = this.getSize();
        this.setupSlots(top, bottom);
    }
    
    public CraftContainer(final Inventory inventory, final HumanEntity player, final int id) {
        this(new InventoryView() {
            @Override
            public Inventory getTopInventory() {
                return inventory;
            }
            
            @Override
            public Inventory getBottomInventory() {
                return player.getInventory();
            }
            
            @Override
            public HumanEntity getPlayer() {
                return player;
            }
            
            @Override
            public InventoryType getType() {
                return inventory.getType();
            }
        }, id);
    }
    
    @Override
    public InventoryView getBukkitView() {
        return this.view;
    }
    
    private int getSize() {
        return this.view.getTopInventory().getSize();
    }
    
    @Override
    public boolean getCanCraft(final EntityPlayer entityhuman) {
        if (this.cachedType == this.view.getType() && this.cachedSize == this.getSize() && this.cachedTitle.equals(this.view.getTitle())) {
            return true;
        }
        final boolean typeChanged = this.cachedType != this.view.getType();
        this.cachedType = this.view.getType();
        this.cachedTitle = this.view.getTitle();
        if (this.view.getPlayer() instanceof CraftPlayer) {
            final CraftPlayer player = (CraftPlayer)this.view.getPlayer();
            final String type = getNotchInventoryType(this.cachedType);
            final IInventory top = ((CraftInventory)this.view.getTopInventory()).getInventory();
            final IInventory bottom = ((CraftInventory)this.view.getBottomInventory()).getInventory();
            this.inventoryItemStacks.clear();
            this.inventorySlots.clear();
            if (typeChanged) {
                this.setupSlots(top, bottom);
            }
            final int size = this.getSize();
            player.getHandle().connection.sendPacket(new SPacketOpenWindow(this.windowId, type, new TextComponentString(this.cachedTitle), size));
            player.updateInventory();
        }
        return true;
    }
    
    public static String getNotchInventoryType(final InventoryType type) {
        switch (type) {
            case WORKBENCH: {
                return "minecraft:crafting_table";
            }
            case FURNACE: {
                return "minecraft:furnace";
            }
            case DISPENSER: {
                return "minecraft:dispenser";
            }
            case ENCHANTING: {
                return "minecraft:enchanting_table";
            }
            case BREWING: {
                return "minecraft:brewing_stand";
            }
            case BEACON: {
                return "minecraft:beacon";
            }
            case ANVIL: {
                return "minecraft:anvil";
            }
            case HOPPER: {
                return "minecraft:hopper";
            }
            default: {
                return "minecraft:chest";
            }
        }
    }
    
    private void setupSlots(final IInventory top, final IInventory bottom) {
        switch (this.cachedType) {
            case CHEST:
            case PLAYER: {
                this.setupChest(top, bottom);
                break;
            }
            case DISPENSER: {
                this.setupDispenser(top, bottom);
                break;
            }
            case FURNACE: {
                this.setupFurnace(top, bottom);
                break;
            }
            case WORKBENCH:
            case CRAFTING: {
                this.setupWorkbench(top, bottom);
                break;
            }
            case ENCHANTING: {
                this.setupEnchanting(top, bottom);
                break;
            }
            case BREWING: {
                this.setupBrewing(top, bottom);
                break;
            }
            case HOPPER: {
                this.setupHopper(top, bottom);
                break;
            }
            case ANVIL: {
                this.setupAnvil(top, bottom);
                break;
            }
        }
    }
    
    private void setupChest(final IInventory top, final IInventory bottom) {
        final int rows = top.getSizeInventory() / 9;
        final int i = (rows - 4) * 18;
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(top, col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + i));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 161 + i));
        }
    }
    
    private void setupWorkbench(final IInventory top, final IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 124, 35));
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlotToContainer(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 142));
        }
    }
    
    private void setupFurnace(final IInventory top, final IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 56, 17));
        this.addSlotToContainer(new Slot(top, 1, 56, 53));
        this.addSlotToContainer(new Slot(top, 2, 116, 35));
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 142));
        }
    }
    
    private void setupDispenser(final IInventory top, final IInventory bottom) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlotToContainer(new Slot(top, col + row * 3, 61 + col * 18, 17 + row * 18));
            }
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 142));
        }
    }
    
    private void setupEnchanting(final IInventory top, final IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 15, 47));
        this.addSlotToContainer(new Slot(top, 0, 35, 47));
        for (int row = 0; row < 3; ++row) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(bottom, i1 + row * 9 + 9, 8 + i1 * 18, 84 + row * 18));
            }
        }
        for (int row = 0; row < 9; ++row) {
            this.addSlotToContainer(new Slot(bottom, row, 8 + row * 18, 142));
        }
    }
    
    private void setupBrewing(final IInventory top, final IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 56, 46));
        this.addSlotToContainer(new Slot(top, 1, 79, 53));
        this.addSlotToContainer(new Slot(top, 2, 102, 46));
        this.addSlotToContainer(new Slot(top, 3, 79, 17));
        this.addSlotToContainer(new Slot(top, 4, 17, 17));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(bottom, i, 8 + i * 18, 142));
        }
    }
    
    private void setupHopper(final IInventory top, final IInventory bottom) {
        final byte b0 = 51;
        for (int i = 0; i < top.getSizeInventory(); ++i) {
            this.addSlotToContainer(new Slot(top, i, 44 + i * 18, 20));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(bottom, i, 8 + i * 18, 58 + b0));
        }
    }
    
    private void setupAnvil(final IInventory top, final IInventory bottom) {
        this.addSlotToContainer(new Slot(top, 0, 27, 47));
        this.addSlotToContainer(new Slot(top, 1, 76, 47));
        this.addSlotToContainer(new Slot(top, 2, 134, 47));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(bottom, i, 8 + i * 18, 142));
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer entity) {
        return true;
    }
}
