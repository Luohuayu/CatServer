package catserver.server;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.event.block.BlockBreakEvent;

@Mod.EventBusSubscriber(modid = "forge", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CatServerEventHandler {
    {
        CatServer.LOGGER.info("Initialization CatServer EventHandler.");
    }

    public static boolean isDropItems;

    @SubscribeEvent(receiveCanceled = true)
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (!event.getWorld().isClientSide()) {
            CraftBlock craftBlock = CraftBlock.at(event.getWorld(), event.getPos());
            BlockBreakEvent breakEvent = new BlockBreakEvent(craftBlock, ((ServerPlayer) event.getPlayer()).getBukkitEntity());
            breakEvent.setCancelled(event.isCanceled());
            breakEvent.setExpToDrop(event.getExpToDrop());
            Bukkit.getPluginManager().callEvent(breakEvent);
            event.setCanceled(breakEvent.isCancelled());
            event.setExpToDrop(breakEvent.getExpToDrop());
            isDropItems = breakEvent.isDropItems();
        }
    }
}