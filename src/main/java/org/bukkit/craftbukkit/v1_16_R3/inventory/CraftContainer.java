package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.BeaconContainer;
import net.minecraft.inventory.container.BlastFurnaceContainer;
import net.minecraft.inventory.container.BrewingStandContainer;
import net.minecraft.inventory.container.CartographyContainer;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.DispenserContainer;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.inventory.container.HopperContainer;
import net.minecraft.inventory.container.LecternContainer;
import net.minecraft.inventory.container.LoomContainer;
import net.minecraft.inventory.container.MerchantContainer;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.container.ShulkerBoxContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.SmokerContainer;
import net.minecraft.inventory.container.StonecutterContainer;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SOpenWindowPacket;
import net.minecraft.util.IntArray;
import net.minecraft.util.text.StringTextComponent;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftContainer extends Container {

    private final InventoryView view;
    private InventoryType cachedType;
    private String cachedTitle;
    private Container delegate;
    private final int cachedSize;

    public CraftContainer(InventoryView view, PlayerEntity player, int id) {
        super(getNotchInventoryType(view.getTopInventory()), id);
        this.view = view;
        // TODO: Do we need to check that it really is a CraftInventory?
        IInventory top = ((CraftInventory) view.getTopInventory()).getInventory();
        PlayerInventory bottom = (PlayerInventory) ((CraftInventory) view.getBottomInventory()).getInventory();
        cachedType = view.getType();
        cachedTitle = view.getTitle();
        cachedSize = getSize();
        setupSlots(top, bottom, player);
    }

    public CraftContainer(final Inventory inventory, final PlayerEntity player, int id) {
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

            @Override
            public String getTitle() {
                return inventory instanceof CraftInventoryCustom ? ((CraftInventoryCustom.MinecraftInventory) ((CraftInventory) inventory).getInventory()).getTitle() : inventory.getType().getDefaultTitle();
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
    public boolean isSynched(PlayerEntity entityhuman) {
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
            ContainerType<?> type = getNotchInventoryType(view.getTopInventory());
            IInventory top = ((CraftInventory) view.getTopInventory()).getInventory();
            PlayerInventory bottom = (PlayerInventory) ((CraftInventory) view.getBottomInventory()).getInventory();
            this.lastSlots.clear();
            this.slots.clear();
            if (typeChanged) {
                setupSlots(top, bottom, player.getHandle());
            }
            int size = getSize();
            player.getHandle().connection.send(new SOpenWindowPacket(this.containerId, type, new StringTextComponent(cachedTitle)));
            player.updateInventory();
        }
        return true;
    }

    public static ContainerType getNotchInventoryType(Inventory inventory) {
        switch (inventory.getType()) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                switch (inventory.getSize()) {
                    case 9:
                        return ContainerType.GENERIC_9x1;
                    case 18:
                        return ContainerType.GENERIC_9x2;
                    case 27:
                        return ContainerType.GENERIC_9x3;
                    case 36:
                    case 41: // PLAYER
                        return ContainerType.GENERIC_9x4;
                    case 45:
                        return ContainerType.GENERIC_9x5;
                    case 54:
                        return ContainerType.GENERIC_9x6;
                    default:
                        throw new IllegalArgumentException("Unsupported custom inventory size " + inventory.getSize());
                }
            case WORKBENCH:
                return ContainerType.CRAFTING;
            case FURNACE:
                return ContainerType.FURNACE;
            case DISPENSER:
                return ContainerType.GENERIC_3x3;
            case ENCHANTING:
                return ContainerType.ENCHANTMENT;
            case BREWING:
                return ContainerType.BREWING_STAND;
            case BEACON:
                return ContainerType.BEACON;
            case ANVIL:
                return ContainerType.ANVIL;
            case SMITHING:
                return ContainerType.SMITHING;
            case HOPPER:
                return ContainerType.HOPPER;
            case DROPPER:
                return ContainerType.GENERIC_3x3;
            case SHULKER_BOX:
                return ContainerType.SHULKER_BOX;
            case BLAST_FURNACE:
                return ContainerType.BLAST_FURNACE;
            case LECTERN:
                return ContainerType.LECTERN;
            case SMOKER:
                return ContainerType.SMOKER;
            case LOOM:
                return ContainerType.LOOM;
            case CARTOGRAPHY:
                return ContainerType.CARTOGRAPHY_TABLE;
            case GRINDSTONE:
                return ContainerType.GRINDSTONE;
            case STONECUTTER:
                return ContainerType.STONECUTTER;
            case CREATIVE:
            case CRAFTING:
            case MERCHANT:
                throw new IllegalArgumentException("Can't open a " + inventory.getType() + " inventory!");
            default:
                // TODO: If it reaches the default case, should we throw an error?
                return ContainerType.GENERIC_9x3;
        }
    }

    private void setupSlots(IInventory top, PlayerInventory bottom, PlayerEntity entityhuman) {
        int windowId = -1;
        switch (cachedType) {
            case CREATIVE:
                break; // TODO: This should be an error?
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                delegate = new ChestContainer(ContainerType.GENERIC_9x3, windowId, bottom, top, top.getContainerSize() / 9);
                break;
            case DISPENSER:
            case DROPPER:
                delegate = new DispenserContainer(windowId, bottom, top);
                break;
            case FURNACE:
                delegate = new FurnaceContainer(windowId, bottom, top, new IntArray(4));
                break;
            case CRAFTING: // TODO: This should be an error?
            case WORKBENCH:
                setupWorkbench(top, bottom); // SPIGOT-3812 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case ENCHANTING:
                delegate = new EnchantmentContainer(windowId, bottom);
                break;
            case BREWING:
                delegate = new BrewingStandContainer(windowId, bottom, top, new IntArray(2));
                break;
            case HOPPER:
                delegate = new HopperContainer(windowId, bottom, top);
                break;
            case ANVIL:
                delegate = new RepairContainer(windowId, bottom);
                break;
            case SMITHING:
                delegate = new SmokerContainer(windowId, bottom);
                break;
            case BEACON:
                delegate = new BeaconContainer(windowId, bottom);
                break;
            case SHULKER_BOX:
                delegate = new ShulkerBoxContainer(windowId, bottom, top);
                break;
            case BLAST_FURNACE:
                delegate = new BlastFurnaceContainer(windowId, bottom, top, new IntArray(4));
                break;
            case LECTERN:
                delegate = new LecternContainer(windowId, top, new IntArray(1), bottom);
                break;
            case SMOKER:
                delegate = new SmokerContainer(windowId, bottom, top, new IntArray(4));
                break;
            case LOOM:
                delegate = new LoomContainer(windowId, bottom);
                break;
            case CARTOGRAPHY:
                delegate = new CartographyContainer(windowId, bottom);
                break;
            case GRINDSTONE:
                delegate = new GrindstoneContainer(windowId, bottom);
                break;
            case STONECUTTER:
                delegate = new StonecutterContainer(windowId, bottom);
                break;
            case MERCHANT:
                delegate = new MerchantContainer(windowId, bottom);
                break;
        }

        if (delegate != null) {
            this.lastSlots = delegate.lastSlots;
            this.slots = delegate.slots;
        }

        // SPIGOT-4598 - we should still delegate the shift click handler
        if (cachedType == InventoryType.WORKBENCH) {
            delegate = new WorkbenchContainer(windowId, bottom);
        }
    }

    private void setupWorkbench(IInventory top, IInventory bottom) {
        // This code copied from WorkbenchContainer
        this.addSlot(new Slot(top, 0, 124, 35));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.addSlot(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(bottom, col, 8 + col * 18, 142));
        }
        // End copy from WorkbenchContainer
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity entityhuman, int i) {
        return (delegate != null) ? delegate.quickMoveStack(entityhuman, i) : super.quickMoveStack(entityhuman, i);
    }

    @Override
    public boolean stillValid(PlayerEntity entity) {
        return true;
    }

    @Override
    public ContainerType<?> getType() {
        return getNotchInventoryType(view.getTopInventory());
    }
}
