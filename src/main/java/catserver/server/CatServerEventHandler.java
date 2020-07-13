package catserver.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;

public class CatServerEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        org.bukkit.event.block.BlockBreakEvent bukkitEvent = CraftEventFactory.callBlockBreakEvent(event.getWorld(), event.getPos(), event.getState(), (EntityPlayerMP) event.getPlayer());

        if (bukkitEvent.isCancelled()) {
            event.setCanceled(true);
        } else {
            event.setExpToDrop(bukkitEvent.getExpToDrop());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        BlockPos pos = event.getPos();
        org.bukkit.craftbukkit.block.CraftBlockState blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(event.getWorld(), pos.getX(), pos.getY(), pos.getZ());
        org.bukkit.event.block.BlockPlaceEvent bukkitEvent = CraftEventFactory.callBlockPlaceEvent(event.getWorld(), event.getPlayer(), event.getHand(), blockstate, pos.getX(), pos.getY(), pos.getZ());

        if (bukkitEvent.isCancelled() || !bukkitEvent.canBuild()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.isVanilla) {
            org.bukkit.event.player.PlayerInteractEvent bukkitEvent = CraftEventFactory.callPlayerInteractEvent(event.getEntityPlayer(), Action.LEFT_CLICK_BLOCK, event.getPos(), event.getFace(), event.getItemStack(), false, event.getHand());
            if (bukkitEvent.useInteractedBlock() == Event.Result.DENY) {
                event.setCanceled(true);
                event.setCancellationResult(bukkitEvent.useItemInHand() != Event.Result.ALLOW ? EnumActionResult.SUCCESS : EnumActionResult.PASS);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isVanilla) {
            org.bukkit.event.player.PlayerInteractEvent bukkitEvent = CraftEventFactory.callPlayerInteractEvent(event.getEntityPlayer(), Action.RIGHT_CLICK_BLOCK, event.getPos(), event.getFace(), event.getItemStack(), false, event.getHand());
            if (bukkitEvent.useInteractedBlock() == Event.Result.DENY) {
                event.setCanceled(true);
                event.setCancellationResult(bukkitEvent.useItemInHand() != Event.Result.ALLOW ? EnumActionResult.SUCCESS : EnumActionResult.PASS);
            }
        }
    }
}
