package catserver.server.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CatCustomInventory implements InventoryHolder {
    private final IInventory inventory;
    private final CraftInventory container;

    public CatCustomInventory(IInventory inventory) {
        this.container = new CraftInventory(inventory);
        this.inventory = inventory;
    }

    public CatCustomInventory(ItemStackHandler handler) {
        this.container = new CraftInventoryCustom(this, handler.stacks);
        this.inventory = this.container.getInventory();
    }

    public CatCustomInventory(PlayerInventory playerInventory) {
        this.container = new CraftInventoryPlayer(playerInventory);
        this.inventory = playerInventory;
    }

    @Override
    public Inventory getInventory() {
        return this.container;
    }

    // TODO: support all types
    @Nullable
    public static InventoryHolder getHolderFromForge(IItemHandler handler) {
        if (handler == null) return null;
        if (handler instanceof ItemStackHandler) return new CatCustomInventory((ItemStackHandler) handler);
        if (handler instanceof SlotItemHandler) return new CatCustomInventory(((SlotItemHandler) handler).container);
        if (handler instanceof InvWrapper) return new CatCustomInventory(((InvWrapper) handler).getInv());
        if (handler instanceof SidedInvWrapper) return new CatCustomInventory(((SidedInvWrapper) handler).inv);
        if (handler instanceof PlayerInvWrapper)  return new CatCustomInventory(getPlayerInv((PlayerInvWrapper) handler));
        return null;
    }

    @Nullable
    public static Inventory getInventoryFromForge(IItemHandler handler) {
        InventoryHolder holder = getHolderFromForge(handler);
        return holder != null ? holder.getInventory() : null;
    }

    public static PlayerInventory getPlayerInv(PlayerInvWrapper handler) {
        IItemHandlerModifiable[] itemHandlers = ObfuscationReflectionHelper.getPrivateValue(CombinedInvWrapper.class, handler, "itemHandler");
        for (IItemHandlerModifiable itemHandler : itemHandlers) {
            if (itemHandler instanceof PlayerMainInvWrapper) {
                return ((PlayerMainInvWrapper) itemHandler).getInventoryPlayer();
            }
            if (itemHandler instanceof PlayerArmorInvWrapper) {
                return ((PlayerArmorInvWrapper) itemHandler).getInventoryPlayer();
            }
        }
        return null;
    }
}
