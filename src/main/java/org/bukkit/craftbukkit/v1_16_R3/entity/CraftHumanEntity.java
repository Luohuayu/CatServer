package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraft.network.play.server.SOpenWindowPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.*;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private CraftInventoryPlayer inventory;
    private CraftInventory enderChest;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;

    public CraftHumanEntity(final CraftServer server, final PlayerEntity entity) {
        super(server, entity);
        mode = server.getDefaultGameMode();
    }

    @Override
    public PlayerInventory getInventory() {
        if (inventory == null || inventory.getInventory() != ((PlayerEntity) entity).inventory) {
            inventory = new CraftInventoryPlayer(((PlayerEntity) entity).inventory);
        }
        return inventory;
    }

    @Override
    public EntityEquipment getEquipment() {
        if (inventory == null || inventory.getInventory() != ((PlayerEntity) entity).inventory) {
            inventory = new CraftInventoryPlayer(((PlayerEntity) entity).inventory);
        }
        return inventory;
    }

    @Override
    public Inventory getEnderChest() {
        if (enderChest == null || enderChest.getInventory() != ((PlayerEntity) entity).getEnderChestInventory()) {
            enderChest = new CraftInventory(((PlayerEntity) entity).getEnderChestInventory());
        }
        return enderChest;
    }

    @Override
    public MainHand getMainHand() {
        return getHandle().getMainArm() == HandSide.LEFT ? MainHand.LEFT : MainHand.RIGHT;
    }

    @Override
    public ItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        getInventory().setItemInHand(item);
    }

    @Override
    public ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(getHandle().inventory.getCarried());
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        net.minecraft.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        getHandle().inventory.setCarried(stack);
        if (this instanceof CraftPlayer) {
            ((ServerPlayerEntity) getHandle()).broadcastCarriedItem(); // Send set slot for cursor
        }
    }

    @Override
    public int getSleepTicks() {
        return getHandle().sleepCounter;
    }

    @Override
    public boolean sleep(Location location, boolean force) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "Location needs to be in a world");
        Preconditions.checkArgument(location.getWorld().equals(getWorld()), "Cannot sleep across worlds");

        BlockPos blockposition = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        BlockState iblockdata = getHandle().level.getBlockState(blockposition);
        if (!(iblockdata.getBlock() instanceof BedBlock)) {
            return false;
        }

        if (getHandle().sleep(blockposition, force).left().isPresent()) {
            return false;
        }

        // From BlockBed
        iblockdata = (BlockState) iblockdata.setValue(BedBlock.OCCUPIED, true);
        getHandle().level.setBlock(blockposition, iblockdata, 4);

        return true;
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        Preconditions.checkState(isSleeping(), "Cannot wakeup if not sleeping");

        getHandle().stopSleepInBed(true, setSpawnLocation);
    }

    @Override
    public Location getBedLocation() {
        Preconditions.checkState(isSleeping(), "Not sleeping");

        BlockPos bed = getHandle().getSleepingPos().get();
        return new Location(getWorld(), bed.getX(), bed.getY(), bed.getZ());
    }

    @Override
    public String getName() {
        return getHandle().getScoreboardName();
    }

    @Override
    public boolean isOp() {
        return op;
    }

    @Override
    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    @Override
    public void setOp(boolean value) {
        this.op = value;
        perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    @Override
    public GameMode getGameMode() {
        return mode;
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        this.mode = mode;
    }

    @Override
    public PlayerEntity getHandle() {
        return (PlayerEntity) entity;
    }

    public void setHandle(final PlayerEntity entity) {
        super.setHandle(entity);
        this.inventory = new CraftInventoryPlayer(entity.inventory);
        this.enderChest = null; // LoliServer
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + getEntityId() + "name=" + getName() + '}';
    }

    @Override
    public InventoryView getOpenInventory() {
        return getHandle().containerMenu.getBukkitView();
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        if (!(getHandle() instanceof ServerPlayerEntity)) return null;
        ServerPlayerEntity player = (ServerPlayerEntity) getHandle();
        Container formerContainer = getHandle().containerMenu;

        INamedContainerProvider iinventory = null;
        if (inventory instanceof CraftInventoryDoubleChest) {
            iinventory = ((CraftInventoryDoubleChest) inventory).tile;
        } else if (inventory instanceof CraftInventoryLectern) {
            iinventory = ((CraftInventoryLectern) inventory).tile;
        } else if (inventory instanceof CraftInventory) {
            CraftInventory craft = (CraftInventory) inventory;
            if (craft.getInventory() instanceof INamedContainerProvider) {
                iinventory = (INamedContainerProvider) craft.getInventory();
            }
        }

        if (iinventory instanceof INamedContainerProvider) {
            if (iinventory instanceof TileEntity) {
                TileEntity te = (TileEntity) iinventory;
                if (!te.hasLevel()) {
                    te.setLevelAndPosition(getHandle().level, getHandle().blockPosition());
                }
            }
        }

        ContainerType<?> container = CraftContainer.getNotchInventoryType(inventory);
        if (iinventory instanceof INamedContainerProvider) {
            getHandle().openMenu(iinventory);
        } else {
            openCustomInventory(inventory, player, container);
        }

        if (getHandle().containerMenu == formerContainer) {
            return null;
        }
        getHandle().containerMenu.checkReachable = false;
        return getHandle().containerMenu.getBukkitView();
    }

    private void openCustomInventory(Inventory inventory, ServerPlayerEntity player, ContainerType<?> windowType) {
        if (player.connection == null) return;
        Preconditions.checkArgument(windowType != null, "Unknown windowType");
        net.minecraft.inventory.container.Container container = new CraftContainer(inventory, this.getHandle(), player.nextContainerCounterCB());

        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if (container == null) return;

        String title = container.getBukkitView().getTitle();

        player.connection.send(new SOpenWindowPacket(container.containerId, windowType, CraftChatMessage.fromString(title)[0]));
        getHandle().containerMenu = container;
        getHandle().containerMenu.addSlotListener(player);
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        if (location == null) {
            location = getLocation();
        }
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.CRAFTING_TABLE) {
                return null;
            }
        }
        getHandle().openMenu(((CraftingTableBlock) Blocks.CRAFTING_TABLE).getMenuProvider(null, getHandle().level, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        if (force) {
            getHandle().containerMenu.checkReachable = false;
        }
        return getHandle().containerMenu.getBukkitView();
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        if (location == null) {
            location = getLocation();
        }
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.ENCHANTING_TABLE) {
                return null;
            }
        }

        // If there isn't an enchant table we can force create one, won't be very useful though.
        BlockPos pos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        getHandle().openMenu(((EnchantingTableBlock) Blocks.ENCHANTING_TABLE).getMenuProvider(null, getHandle().level, pos));

        if (force) {
            getHandle().containerMenu.checkReachable = false;
        }
        return getHandle().containerMenu.getBukkitView();
    }

    @Override
    public void openInventory(InventoryView inventory) {
        if (!(getHandle() instanceof ServerPlayerEntity)) return; // TODO: NPC support?
        if (((ServerPlayerEntity) getHandle()).connection == null) return;
        if (getHandle().containerMenu != getHandle().inventoryMenu) {
            // fire INVENTORY_CLOSE if one already open
            ((ServerPlayerEntity) getHandle()).connection.handleContainerClose(new CCloseWindowPacket(getHandle().containerMenu.containerId));
        }
        ServerPlayerEntity player = (ServerPlayerEntity) getHandle();
        net.minecraft.inventory.container.Container container;
        if (inventory instanceof CraftInventoryView) {
            container = ((CraftInventoryView) inventory).getHandle();
        } else {
            container = new CraftContainer(inventory, this.getHandle(), player.nextContainerCounterCB());
        }

        // Trigger an INVENTORY_OPEN event
        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if (container == null) {
            return;
        }

        // Now open the window
        ContainerType<?> windowType = CraftContainer.getNotchInventoryType(inventory.getTopInventory());
        String title = inventory.getTitle();
        player.connection.send(new SOpenWindowPacket(container.containerId, windowType, CraftChatMessage.fromString(title)[0]));
        player.containerMenu = container;
        player.containerMenu.addSlotListener(player);
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
        ITextComponent name;
        int level = 1; // note: using level 0 with active 'is-regular-villager'-flag allows hiding the name suffix
        if (merchant instanceof CraftAbstractVillager) {
            mcMerchant = ((CraftAbstractVillager) merchant).getHandle();
            name = ((CraftAbstractVillager) merchant).getHandle().getDisplayName();
            if (merchant instanceof CraftVillager) {
                level = ((CraftVillager) merchant).getHandle().getVillagerData().getLevel();
            }
        } else if (merchant instanceof CraftMerchantCustom) {
            mcMerchant = ((CraftMerchantCustom) merchant).getMerchant();
            name = ((CraftMerchantCustom) merchant).getMerchant().getScoreboardDisplayName();
        } else {
            throw new IllegalArgumentException("Can't open merchant " + merchant.toString());
        }

        mcMerchant.setTradingPlayer(this.getHandle());
        mcMerchant.openTradingScreen(this.getHandle(), name, level);

        return this.getHandle().containerMenu.getBukkitView();
    }

    @Override
    public void closeInventory() {
        getHandle().closeContainer();
    }

    @Override
    public boolean isBlocking() {
        return getHandle().isBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return getHandle().isUsingItem();
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    @Override
    public int getExpToLevel() {
        return getHandle().getXpNeededForNextLevel();
    }

    @Override
    public float getAttackCooldown() {
        return getHandle().getCurrentItemAttackStrengthDelay();
    }

    @Override
    public boolean hasCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");

        return getHandle().getCooldowns().isOnCooldown(CraftMagicNumbers.getItem(material));
    }

    @Override
    public int getCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");

        CooldownTracker.Cooldown cooldown = getHandle().getCooldowns().cooldowns.get(CraftMagicNumbers.getItem(material));
        return (cooldown == null) ? 0 : Math.max(0, cooldown.endTime - getHandle().getCooldowns().tickCount);
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        Preconditions.checkArgument(material != null, "material");
        Preconditions.checkArgument(ticks >= 0, "Cannot have negative cooldown");

        getHandle().getCooldowns().addCooldown(CraftMagicNumbers.getItem(material), ticks);
    }

    @Override
    public boolean discoverRecipe(NamespacedKey recipe) {
        return discoverRecipes(Arrays.asList(recipe)) != 0;
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        return getHandle().awardRecipes(bukkitKeysToMinecraftRecipes(recipes));
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        return undiscoverRecipes(Arrays.asList(recipe)) != 0;
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        return getHandle().awardRecipes(bukkitKeysToMinecraftRecipes(recipes));
    }

    @Override
    public boolean hasDiscoveredRecipe(NamespacedKey recipe) {
        return false;
    }

    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        return ImmutableSet.of();
    }

    private Collection<IRecipe<?>> bukkitKeysToMinecraftRecipes(Collection<NamespacedKey> recipeKeys) {
        Collection<IRecipe<?>> recipes = new ArrayList<>();
        RecipeManager manager = getHandle().level.getServer().getRecipeManager();

        for (NamespacedKey recipeKey : recipeKeys) {
            Optional<? extends IRecipe<?>> recipe = manager.byKey(CraftNamespacedKey.toMinecraft(recipeKey));
            if (!recipe.isPresent()) {
                continue;
            }

            recipes.add(recipe.get());
        }

        return recipes;
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityLeft() {
        if (!getHandle().getShoulderEntityLeft().isEmpty()) {
            Optional<Entity> shoulder = EntityType.create(getHandle().getShoulderEntityLeft(), getHandle().level);

            return (!shoulder.isPresent()) ? null : shoulder.get().getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShoulderEntityLeft(org.bukkit.entity.Entity entity) {
        getHandle().setShoulderEntityLeft(entity == null ? new CompoundNBT() : ((CraftEntity) entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityRight() {
        if (!getHandle().getShoulderEntityRight().isEmpty()) {
            Optional<Entity> shoulder = EntityType.create(getHandle().getShoulderEntityRight(), getHandle().level);

            return (!shoulder.isPresent()) ? null : shoulder.get().getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShoulderEntityRight(org.bukkit.entity.Entity entity) {
        getHandle().setShoulderEntityRight(entity == null ? new CompoundNBT() : ((CraftEntity) entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public boolean dropItem(boolean dropAll) {
        return getHandle().drop(dropAll);
    }

    @Override
    public float getExhaustion() {
        return getHandle().getFoodData().exhaustionLevel;
    }

    @Override
    public void setExhaustion(float value) {
        getHandle().getFoodData().exhaustionLevel = value;
    }

    @Override
    public float getSaturation() {
        return getHandle().getFoodData().saturationLevel;
    }

    @Override
    public void setSaturation(float value) {
        getHandle().getFoodData().saturationLevel = value;
    }

    @Override
    public int getFoodLevel() {
        return getHandle().getFoodData().foodLevel;
    }

    @Override
    public void setFoodLevel(int value) {
        getHandle().getFoodData().foodLevel = value;
    }

    @Override
    public int getSaturatedRegenRate() {
        return getHandle().getFoodData().saturatedRegenRate;
    }

    @Override
    public void setSaturatedRegenRate(int i) {
        getHandle().getFoodData().saturatedRegenRate = i;
    }

    @Override
    public int getUnsaturatedRegenRate() {
        return getHandle().getFoodData().unsaturatedRegenRate;
    }

    @Override
    public void setUnsaturatedRegenRate(int i) {
        getHandle().getFoodData().unsaturatedRegenRate = i;
    }

    @Override
    public int getStarvationRate() {
        return getHandle().getFoodData().starvationRate;
    }

    @Override
    public void setStarvationRate(int i) {
        getHandle().getFoodData().starvationRate = i;
    }
}
