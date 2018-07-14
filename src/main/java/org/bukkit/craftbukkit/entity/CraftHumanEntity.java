// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.IMerchant;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Villager;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import org.bukkit.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Material;
import org.bukkit.Location;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.network.Packet;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import net.minecraft.inventory.Container;
import org.bukkit.event.inventory.InventoryType;
import net.minecraft.world.IInteractionObject;
import luohuayu.CatServer.inventory.CBContainer;
import net.minecraft.block.BlockAnvil;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityDispenser;
import org.bukkit.craftbukkit.inventory.InventoryWrapper;
import org.bukkit.inventory.InventoryView;
import org.bukkit.permissions.PermissionAttachmentInfo;
import java.util.Set;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.permissions.Permission;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.PlayerInventory;
import net.minecraft.inventory.IInventory;
import org.bukkit.permissions.ServerOperator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.GameMode;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.entity.HumanEntity;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity
{
    private CraftInventoryPlayer inventory;
    private final CraftInventory enderChest;
    protected final PermissibleBase perm;
    private boolean op;
    private GameMode mode;
    
    public CraftHumanEntity(final CraftServer server, final EntityPlayer entity) {
        super(server, entity);
        this.perm = new PermissibleBase(this);
        this.mode = server.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.inventory);
        this.enderChest = new CraftInventory(entity.getInventoryEnderChest());
    }
    
    @Override
    public String getName() {
        return this.getHandle().getName();
    }
    
    @Override
    public PlayerInventory getInventory() {
        return this.inventory;
    }
    
    @Override
    public EntityEquipment getEquipment() {
        return this.inventory;
    }
    
    @Override
    public Inventory getEnderChest() {
        return this.enderChest;
    }
    
    @Override
    public MainHand getMainHand() {
        return (this.getHandle().getPrimaryHand() == EnumHandSide.LEFT) ? MainHand.LEFT : MainHand.RIGHT;
    }
    
    @Override
    public ItemStack getItemInHand() {
        return this.getInventory().getItemInHand();
    }
    
    @Override
    public void setItemInHand(final ItemStack item) {
        this.getInventory().setItemInHand(item);
    }
    
    @Override
    public ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(this.getHandle().inventory.getItemStack());
    }
    
    @Override
    public void setItemOnCursor(final ItemStack item) {
        final net.minecraft.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        this.getHandle().inventory.setItemStack(stack);
        if (this instanceof CraftPlayer) {
            ((EntityPlayerMP)this.getHandle()).updateHeldItem();
        }
    }
    
    @Override
    public boolean isSleeping() {
        return this.getHandle().isPlayerSleeping();
    }
    
    @Override
    public int getSleepTicks() {
        return this.getHandle().getSleepTimer();
    }
    
    @Override
    public boolean isOp() {
        return this.op;
    }
    
    @Override
    public boolean isPermissionSet(final String name) {
        return this.perm.isPermissionSet(name);
    }
    
    @Override
    public boolean isPermissionSet(final Permission perm) {
        return this.perm.isPermissionSet(perm);
    }
    
    @Override
    public boolean hasPermission(final String name) {
        return this.perm.hasPermission(name);
    }
    
    @Override
    public boolean hasPermission(final Permission perm) {
        return this.perm.hasPermission(perm);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value, final int ticks) {
        return this.perm.addAttachment(plugin, name, value, ticks);
    }
    
    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
        return this.perm.addAttachment(plugin, ticks);
    }
    
    @Override
    public void removeAttachment(final PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }
    
    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }
    
    @Override
    public void setOp(final boolean value) {
        this.op = value;
        this.perm.recalculatePermissions();
    }
    
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }
    
    @Override
    public GameMode getGameMode() {
        return this.mode;
    }
    
    @Override
    public void setGameMode(final GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }
        this.mode = mode;
    }
    
    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer)this.entity;
    }
    
    public void setHandle(final EntityPlayer entity) {
        super.setHandle(entity);
        this.inventory = new CraftInventoryPlayer(entity.inventory);
    }
    
    @Override
    public String toString() {
        return "CraftHumanEntity{id=" + this.getEntityId() + "name=" + this.getName() + '}';
    }
    
    @Override
    public InventoryView getOpenInventory() {
        return ((CBContainer)this.getHandle().openContainer).getBukkitView();
    }
    
    @Override
    public InventoryView openInventory(final Inventory inventory) {
        if (!(this.getHandle() instanceof EntityPlayerMP)) {
            return null;
        }
        final EntityPlayerMP player = (EntityPlayerMP)this.getHandle();
        final InventoryType type = inventory.getType();
        final Container formerContainer = this.getHandle().openContainer;
        final IInventory iinventory = (inventory instanceof CraftInventory) ? ((CraftInventory)inventory).getInventory() : new InventoryWrapper(inventory);
        switch (type) {
            case CHEST:
            case PLAYER:
            case ENDER_CHEST: {
                this.getHandle().displayGUIChest(iinventory);
                break;
            }
            case DISPENSER: {
                if (iinventory instanceof TileEntityDispenser) {
                    this.getHandle().displayGUIChest(iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:dispenser");
                break;
            }
            case DROPPER: {
                if (iinventory instanceof TileEntityDropper) {
                    this.getHandle().displayGUIChest(iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:dropper");
                break;
            }
            case FURNACE: {
                if (iinventory instanceof TileEntityFurnace) {
                    this.getHandle().displayGUIChest(iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:furnace");
                break;
            }
            case WORKBENCH: {
                this.openCustomInventory(inventory, player, "minecraft:crafting_table");
                break;
            }
            case BREWING: {
                if (iinventory instanceof TileEntityBrewingStand) {
                    this.getHandle().displayGUIChest(iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:brewing_stand");
                break;
            }
            case ENCHANTING: {
                this.openCustomInventory(inventory, player, "minecraft:enchanting_table");
                break;
            }
            case HOPPER: {
                if (iinventory instanceof TileEntityHopper) {
                    this.getHandle().displayGUIChest(iinventory);
                    break;
                }
                if (iinventory instanceof EntityMinecartHopper) {
                    this.getHandle().displayGUIChest(iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:hopper");
                break;
            }
            case BEACON: {
                if (iinventory instanceof TileEntityBeacon) {
                    this.getHandle().displayGUIChest(iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:beacon");
                break;
            }
            case ANVIL: {
                if (iinventory instanceof BlockAnvil.Anvil) {
                    this.getHandle().displayGui((IInteractionObject)iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:anvil");
                break;
            }
            case CRAFTING:
            case CREATIVE: {
                throw new IllegalArgumentException("Can't open a " + type + " inventory!");
            }
        }
        if (this.getHandle().openContainer == formerContainer) {
            return null;
        }
        ((CBContainer)this.getHandle().openContainer).checkReachable = false;
        return ((CBContainer)this.getHandle().openContainer).getBukkitView();
    }
    
    private void openCustomInventory(final Inventory inventory, final EntityPlayerMP player, final String windowType) {
        if (player.connection == null) {
            return;
        }
        CBContainer container = new CraftContainer(inventory, this, player.nextContainerCounter());
        container = (CBContainer) CraftEventFactory.callInventoryOpenEvent(player, container);
        if (container == null) {
            return;
        }
        final String title = container.getBukkitView().getTitle();
        int size = container.getBukkitView().getTopInventory().getSize();
        if (windowType.equals("minecraft:crafting_table") || windowType.equals("minecraft:anvil") || windowType.equals("minecraft:enchanting_table")) {
            size = 0;
        }
        player.connection.sendPacket(new SPacketOpenWindow(container.windowId, windowType, new TextComponentString(title), size));
        (this.getHandle().openContainer = container).addListener(player);
    }
    
    @Override
    public InventoryView openWorkbench(Location location, final boolean force) {
        if (!force) {
            final Block block = location.getBlock();
            if (block.getType() != Material.WORKBENCH) {
                return null;
            }
        }
        if (location == null) {
            location = this.getLocation();
        }
        this.getHandle().displayGui(new BlockWorkbench.InterfaceCraftingTable(this.getHandle().worldObj, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        if (force) {
        	((CBContainer)this.getHandle().openContainer).checkReachable = false;
        }
        return ((CBContainer)this.getHandle().openContainer).getBukkitView();
    }
    
    @Override
    public InventoryView openEnchanting(Location location, final boolean force) {
        if (!force) {
            final Block block = location.getBlock();
            if (block.getType() != Material.ENCHANTMENT_TABLE) {
                return null;
            }
        }
        if (location == null) {
            location = this.getLocation();
        }
        TileEntity container = this.getHandle().worldObj.getTileEntity(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        if (container == null && force) {
            container = new TileEntityEnchantmentTable();
        }
        this.getHandle().displayGui((IInteractionObject)container);
        if (force) {
        	((CBContainer)this.getHandle().openContainer).checkReachable = false;
        }
        return ((CBContainer)this.getHandle().openContainer).getBukkitView();
    }
    
    @Override
    public void openInventory(final InventoryView inventory) {
        if (!(this.getHandle() instanceof EntityPlayerMP)) {
            return;
        }
        if (((EntityPlayerMP)this.getHandle()).connection == null) {
            return;
        }
        if (this.getHandle().openContainer != this.getHandle().inventoryContainer) {
            ((EntityPlayerMP)this.getHandle()).connection.processCloseWindow(new CPacketCloseWindow(this.getHandle().openContainer.windowId));
        }
        final EntityPlayerMP player = (EntityPlayerMP)this.getHandle();
        Container container;
        if (inventory instanceof CraftInventoryView) {
            container = ((CraftInventoryView)inventory).getHandle();
        }
        else {
            container = new CraftContainer(inventory, player.nextContainerCounter());
        }
        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if (container == null) {
            return;
        }
        final InventoryType type = inventory.getType();
        final String windowType = CraftContainer.getNotchInventoryType(type);
        final String title = inventory.getTitle();
        final int size = inventory.getTopInventory().getSize();
        player.connection.sendPacket(new SPacketOpenWindow(container.windowId, windowType, new TextComponentString(title), size));
        (player.openContainer = container).addListener(player);
    }
    
    @Override
    public InventoryView openMerchant(final Villager villager, final boolean force) {
        Preconditions.checkNotNull((Object)villager, (Object)"villager cannot be null");
        if (!force && villager.isTrading()) {
            return null;
        }
        if (villager.isTrading()) {
            villager.getTrader().closeInventory();
        }
        final EntityVillager ev = ((CraftVillager)villager).getHandle();
        ev.setCustomer(this.getHandle());
        this.getHandle().displayVillagerTradeGui(ev);
        return ((CBContainer)this.getHandle().openContainer).getBukkitView();
    }
    
    @Override
    public void closeInventory() {
        this.getHandle().closeScreen();
    }
    
    @Override
    public boolean isBlocking() {
        return this.getHandle().isActiveItemStackBlocking();
    }
    
    @Override
    public boolean setWindowProperty(final InventoryView.Property prop, final int value) {
        return false;
    }
    
    @Override
    public int getExpToLevel() {
        return this.getHandle().xpBarCap();
    }
}
