package catserver.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class CatServerEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockBreakEvent bukkitEvent = CraftEventFactory.callBlockBreakEvent(event.getWorld(), event.getPos(), event.getState(), (EntityPlayerMP) event.getPlayer());

        if (bukkitEvent.isCancelled()) {
            event.setCanceled(true);
        } else {
            event.setExpToDrop(bukkitEvent.getExpToDrop());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        BlockPos pos = event.getPos();
        CraftBlockState blockstate = CraftBlockState.getBlockState(event.getWorld(), pos.getX(), pos.getY(), pos.getZ());
        BlockPlaceEvent bukkitEvent = CraftEventFactory.callBlockPlaceEvent(event.getWorld(), event.getPlayer(), event.getHand(), blockstate, pos.getX(), pos.getY(), pos.getZ());

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

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Bukkit.getPluginManager().callEvent(new PlayerChangedWorldEvent((CraftPlayer) event.player.getBukkitEntity(), MinecraftServer.getServerInst().getWorldServer(event.fromDim).getWorld()));
    }
}
