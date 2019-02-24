package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;

import java.util.Set;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private CraftInventoryPlayer inventory;
    private final CraftInventory enderChest;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;

    public CraftHumanEntity(final CraftServer server, final EntityPlayer entity) {
        super(server, entity);
        mode = server.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.inventory);
        enderChest = new CraftInventory(entity.getInventoryEnderChest());
    }

    public String getName() {
        return getHandle().getName();
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public EntityEquipment getEquipment() {
        return inventory;
    }

    public Inventory getEnderChest() {
        return enderChest;
    }

    public MainHand getMainHand() {
        return getHandle().getPrimaryHand()== EnumHandSide.LEFT ? MainHand.LEFT : MainHand.RIGHT;
    }

    public ItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    public void setItemInHand(ItemStack item) {
        getInventory().setItemInHand(item);
    }

    public ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(getHandle().inventory.getItemStack());
    }

    public void setItemOnCursor(ItemStack item) {
        net.minecraft.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        getHandle().inventory.setItemStack(stack);
        if (this instanceof CraftPlayer) {
            ((EntityPlayerMP) getHandle()).updateHeldItem(); // Send set slot for cursor
        }
    }

    public boolean isSleeping() {
        return getHandle().sleeping;
    }

    public int getSleepTicks() {
        return getHandle().sleepTimer;
    }

    public boolean isOp() {
        return op;
    }

    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    public void setOp(boolean value) {
        this.op = value;
        perm.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    public GameMode getGameMode() {
        return mode;
    }

    public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        this.mode = mode;
    }

    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer) entity;
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle(entity);
        this.inventory = new CraftInventoryPlayer(entity.inventory);
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + getEntityId() + "name=" + getName() + '}';
    }

    public InventoryView getOpenInventory() {
        return getHandle().openContainer.getBukkitView();
    }

    public InventoryView openInventory(Inventory inventory) {
        if(!(getHandle() instanceof EntityPlayerMP)) return null;
        EntityPlayerMP player = (EntityPlayerMP) getHandle();
        InventoryType type = inventory.getType();
        Container formerContainer = getHandle().openContainer;

        IInventory iinventory = (inventory instanceof CraftInventory) ? ((CraftInventory) inventory).getInventory() : new org.bukkit.craftbukkit.inventory.InventoryWrapper(inventory);

        switch (type) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
                getHandle().displayGUIChest(iinventory);
                break;
            case DISPENSER:
                if (iinventory instanceof TileEntityDispenser) {
                    getHandle().displayGUIChest((TileEntityDispenser) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:dispenser");
                }
                break;
            case DROPPER:
                if (iinventory instanceof TileEntityDropper) {
                    getHandle().displayGUIChest((TileEntityDropper) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:dropper");
                }
                break;
            case FURNACE:
                if (iinventory instanceof TileEntityFurnace) {
                    getHandle().displayGUIChest((TileEntityFurnace) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:furnace");
                }
                break;
            case WORKBENCH:
                openCustomInventory(inventory, player, "minecraft:crafting_table");
                break;
            case BREWING:
                if (iinventory instanceof TileEntityBrewingStand) {
                    getHandle().displayGUIChest((TileEntityBrewingStand) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:brewing_stand");
                }
                break;
            case ENCHANTING:
                openCustomInventory(inventory, player, "minecraft:enchanting_table");
                break;
            case HOPPER:
                if (iinventory instanceof TileEntityHopper) {
                    getHandle().displayGUIChest((TileEntityHopper) iinventory);
                } else if (iinventory instanceof EntityMinecartHopper) {
                    getHandle().displayGUIChest((EntityMinecartHopper) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:hopper");
                }
                break;
            case BEACON:
                if (iinventory instanceof TileEntityBeacon) {
                    getHandle().displayGUIChest((TileEntityBeacon) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:beacon");
                }
                break;
            case ANVIL:
                if (iinventory instanceof BlockAnvil.Anvil) {
                    getHandle().displayGui((BlockAnvil.Anvil) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:anvil");
                }
                break;
            case SHULKER_BOX:
                if (iinventory instanceof TileEntityShulkerBox) {
                    getHandle().displayGUIChest((TileEntityShulkerBox) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:shulker_box");
                }
                break;
            case CREATIVE:
            case CRAFTING:
                throw new IllegalArgumentException("Can't open a " + type + " inventory!");
        }
        if (getHandle().openContainer == formerContainer) {
            return null;
        }
        getHandle().openContainer.checkReachable = false;
        return getHandle().openContainer.getBukkitView();
    }

    private void openCustomInventory(Inventory inventory, EntityPlayerMP player, String windowType) {
        if (player.connection == null) return;
        Container container = new CraftContainer(inventory, this.getHandle(), player.getNextWindowIdCB());

        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if(container == null) return;

        String title = container.getBukkitView().getTitle();
        int size = container.getBukkitView().getTopInventory().getSize();

        // Special cases
        if (windowType.equals("minecraft:crafting_table") 
                || windowType.equals("minecraft:anvil")
                || windowType.equals("minecraft:enchanting_table")
                ) {
            size = 0;
        }

        player.connection.sendPacket(new SPacketOpenWindow(container.windowId, windowType, new TextComponentString(title), size));
        getHandle().openContainer = container;
        getHandle().openContainer.addListener(player);
    }

    public InventoryView openWorkbench(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.WORKBENCH) {
                return null;
            }
        }
        if (location == null) {
            location = getLocation();
        }
        getHandle().displayGui(new BlockWorkbench.InterfaceCraftingTable(getHandle().world, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        if (force) {
            getHandle().openContainer.checkReachable = false;
        }
        return getHandle().openContainer.getBukkitView();
    }

    public InventoryView openEnchanting(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.ENCHANTMENT_TABLE) {
                return null;
            }
        }
        if (location == null) {
            location = getLocation();
        }

        // If there isn't an enchant table we can force create one, won't be very useful though.
        BlockPos pos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        TileEntity container = getHandle().world.getTileEntity(pos);
        if (container == null && force) {
            container = new TileEntityBrewingStand();
            container.setWorld(getHandle().world);
            container.setPos(pos);
        }
        getHandle().displayGui((TileEntityLockable) container);

        if (force) {
            getHandle().openContainer.checkReachable = false;
        }
        return getHandle().openContainer.getBukkitView();
    }

    public void openInventory(InventoryView inventory) {
        if (!(getHandle() instanceof EntityPlayerMP)) return; // TODO: NPC support?
        if (((EntityPlayerMP) getHandle()).connection == null) return;
        if (getHandle().openContainer != getHandle().inventoryContainer) {
            // fire INVENTORY_CLOSE if one already open
            ((EntityPlayerMP)getHandle()).connection.processCloseWindow(new CPacketCloseWindow(getHandle().openContainer.windowId));
        }
        EntityPlayerMP player = (EntityPlayerMP) getHandle();
        Container container;
        if (inventory instanceof CraftInventoryView) {
            container = ((CraftInventoryView) inventory).getHandle();
        } else {
            container = new CraftContainer(inventory, this.getHandle(), player.getNextWindowIdCB());
        }

        // Trigger an INVENTORY_OPEN event
        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if (container == null) {
            return;
        }

        // Now open the window
        InventoryType type = inventory.getType();
        String windowType = CraftContainer.getNotchInventoryType(type);
        String title = inventory.getTitle();
        int size = inventory.getTopInventory().getSize();
        player.connection.sendPacket(new SPacketOpenWindow(container.windowId, windowType, new TextComponentString(title), size));
        player.openContainer = container;
        player.openContainer.addListener(player);
    }

    @Override
    public InventoryView openMerchant(Villager villager, boolean force) {
        Preconditions.checkNotNull(villager, "villager cannot be null");

        return this.openMerchant((Merchant) villager, force);
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        Preconditions.checkNotNull(merchant, "merchant cannot be null");

        if (!force && merchant.isTrading()) {
            return null;
        } else if (merchant.isTrading()) {
            // we're not supposed to have multiple people using the same merchant, so we have to close it.
            merchant.getTrader().closeInventory();
        }

        IMerchant mcMerchant;
        if (merchant instanceof CraftVillager) {
            mcMerchant = ((CraftVillager) merchant).getHandle();
        } else if (merchant instanceof CraftMerchant) {
            mcMerchant = ((CraftMerchant) merchant).getMerchant();
        } else {
            throw new IllegalArgumentException("Can't open merchant " + merchant.toString());
        }

        mcMerchant.setCustomer(this.getHandle());
        this.getHandle().displayVillagerTradeGui(mcMerchant);

        return this.getHandle().openContainer.getBukkitView();
    }

    public void closeInventory() {
        getHandle().closeScreen();
    }

    public boolean isBlocking() {
        return getHandle().isActiveItemStackBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return getHandle().isHandActive();
    }

    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    public int getExpToLevel() {
        return getHandle().xpBarCap();
    }

    @Override
    public boolean hasCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");

        return getHandle().getCooldownTracker().hasCooldown(CraftMagicNumbers.getItem(material));
    }

    @Override
    public int getCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");

        CooldownTracker.Cooldown cooldown = getHandle().getCooldownTracker().cooldowns.get(CraftMagicNumbers.getItem(material));
        return (cooldown == null) ? 0 : Math.max(0, cooldown.expireTicks - getHandle().getCooldownTracker().ticks);
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        Preconditions.checkArgument(material != null, "material");
        Preconditions.checkArgument(ticks >= 0, "Cannot have negative cooldown");

        getHandle().getCooldownTracker().setCooldown(CraftMagicNumbers.getItem(material), ticks);
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityLeft() {
        if (!getHandle().getLeftShoulderEntity().hasNoTags()) {
            Entity shoulder = EntityList.createEntityFromNBT(getHandle().getLeftShoulderEntity(), getHandle().world);

            return (shoulder == null) ? null : shoulder.getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShoulderEntityLeft(org.bukkit.entity.Entity entity) {
        getHandle().setLeftShoulderEntity(entity == null ? new NBTTagCompound() : ((CraftEntity) entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityRight() {
        if (!getHandle().getRightShoulderEntity().hasNoTags()) {
            Entity shoulder = EntityList.createEntityFromNBT(getHandle().getRightShoulderEntity(), getHandle().world);

            return (shoulder == null) ? null : shoulder.getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShoulderEntityRight(org.bukkit.entity.Entity entity) {
        getHandle().setRightShoulderEntity(entity == null ? new NBTTagCompound() : ((CraftEntity) entity).save());
        if (entity != null) {
            entity.remove();
        }
    }
}
