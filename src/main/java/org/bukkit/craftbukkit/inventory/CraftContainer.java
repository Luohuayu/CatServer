package org.bukkit.craftbukkit.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;

public class CraftContainer extends Container {

    private final InventoryView view;
    private InventoryType cachedType;
    private String cachedTitle;
    private Container delegate;
    private final int cachedSize;

    public CraftContainer(InventoryView view, EntityPlayer player, int id) {
        this.view = view;
        this.windowId = id;
        // TODO: Do we need to check that it really is a CraftInventory?
        IInventory top = ((CraftInventory) view.getTopInventory()).getInventory();
        InventoryPlayer bottom = (InventoryPlayer) ((CraftInventory) view.getBottomInventory()).getInventory();
        cachedType = view.getType();
        cachedTitle = view.getTitle();
        cachedSize = getSize();
        setupSlots(top, bottom, player);
    }

    public CraftContainer(final Inventory inventory, final EntityPlayer player, int id) {
        this(new InventoryView() {
            @Override
            public Inventory getTopInventory() {
                return inventory;
            }

            @Override
            public Inventory getBottomInventory() {
                return getPlayer().getInventory();
            }

            @Override
            public HumanEntity getPlayer() {
                return player.getBukkitEntity();
            }

            @Override
            public InventoryType getType() {
                return inventory.getType();
            }
        }, player, id);
    }

    @Override
    public InventoryView getBukkitView() {
        return view;
    }

    private int getSize() {
        return view.getTopInventory().getSize();
    }

    @Override
    public boolean getCanCraft(EntityPlayer entityhuman) {
        if (cachedType == view.getType() && cachedSize == getSize() && cachedTitle.equals(view.getTitle())) {
            return true;
        }
        // If the window type has changed for some reason, update the player
        // This method will be called every tick or something, so it's
        // as good a place as any to put something like this.
        boolean typeChanged = (cachedType != view.getType());
        cachedType = view.getType();
        cachedTitle = view.getTitle();
        if (view.getPlayer() instanceof CraftPlayer) {
            CraftPlayer player = (CraftPlayer) view.getPlayer();
            String type = getNotchInventoryType(cachedType);
            IInventory top = ((CraftInventory) view.getTopInventory()).getInventory();
            InventoryPlayer bottom = (InventoryPlayer) ((CraftInventory) view.getBottomInventory()).getInventory();
            this.inventoryItemStacks.clear();
            this.inventorySlots.clear();
            if (typeChanged) {
                setupSlots(top, bottom, player.getHandle());
            }
            int size = getSize();
            player.getHandle().connection.sendPacket(new SPacketOpenWindow(this.windowId, type, new TextComponentString(cachedTitle), size));
            player.updateInventory();
        }
        return true;
    }

    public static String getNotchInventoryType(InventoryType type) {
        switch (type) {
            case WORKBENCH:
                return "minecraft:crafting_table";
            case FURNACE:
                return "minecraft:furnace";
            case DISPENSER:
                return "minecraft:dispenser";
            case ENCHANTING:
                return "minecraft:enchanting_table";
            case BREWING:
                return "minecraft:brewing_stand";
            case BEACON:
                return "minecraft:beacon";
            case ANVIL:
                return "minecraft:anvil";
            case HOPPER:
                return "minecraft:hopper";
            case DROPPER:
                return "minecraft:dropper";
            case SHULKER_BOX:
                return "minecraft:shulker_box";
            default:
                return "minecraft:chest";
        }
    }

    private void setupSlots(IInventory top, InventoryPlayer bottom, EntityPlayer entityhuman) {
        switch (cachedType) {
            case CREATIVE:
                break; // TODO: This should be an error?
            case PLAYER:
            case CHEST:
                delegate = new ContainerChest(bottom, top, entityhuman);
                break;
            case DISPENSER:
            case DROPPER:
                delegate = new ContainerDispenser(bottom, top);
                break;
            case FURNACE:
                delegate = new ContainerFurnace(bottom, top);
                break;
            case CRAFTING: // TODO: This should be an error?
            case WORKBENCH:
                setupWorkbench(top, bottom); // SPIGOT-3812 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case ENCHANTING:
                delegate = new ContainerEnchantment(bottom, entityhuman.world, entityhuman.getPosition());
                break;
            case BREWING:
                delegate = new ContainerBrewingStand(bottom, top);
                break;
            case HOPPER:
                delegate = new ContainerHopper(bottom, top, entityhuman);
                break;
            case ANVIL:
                delegate = new ContainerRepair(bottom, entityhuman.world, entityhuman.getPosition(), entityhuman);
                break;
            case BEACON:
                delegate = new ContainerBeacon(bottom, top);
                break;
            case SHULKER_BOX:
                delegate = new ContainerShulkerBox(bottom, top, entityhuman);
                break;
        }

        if (delegate != null) {
            this.inventoryItemStacks = delegate.inventoryItemStacks;
            this.inventorySlots = delegate.inventorySlots;
        }
    }

    private void setupWorkbench(IInventory top, IInventory bottom) {
        // This code copied from ContainerWorkbench
        this.addSlotToContainer(new Slot(top, 0, 124, 35));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.addSlotToContainer(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(bottom, col, 8 + col * 18, 142));
        }
        // End copy from ContainerWorkbench
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        return (delegate != null) ? delegate.transferStackInSlot(entityhuman, i) : super.transferStackInSlot(entityhuman, i);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entity) {
        return true;
    }
}
