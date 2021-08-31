package catserver.server;

import java.util.concurrent.ExecutionException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.bukkit.craftbukkit.util.Waitable;

public class CatServerHooks {
    public static PlayerInteractEvent.LeftClickBlock onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face, Vec3d hitVec) {
        PlayerInteractEvent.LeftClickBlock event = new PlayerInteractEvent.LeftClickBlock(player, pos, face, hitVec);
        event.isVanilla = true;
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static PlayerInteractEvent.RightClickBlock onRightClickBlock(EntityPlayer player, EnumHand hand, BlockPos pos, EnumFacing face, Vec3d hitVec) {
        PlayerInteractEvent.RightClickBlock event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, face, hitVec);
        event.isVanilla = true;
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static ITextComponent onServerChatEvent(NetHandlerPlayServer net, String raw, ITextComponent comp) {
        ServerChatEvent event = new ServerChatEvent(net.player, raw, comp);
        if (AsyncCatcher.isMainThread()) {
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return null;
            }
            return event.getComponent();
        } else {
            Waitable<ITextComponent> waitable = new Waitable<ITextComponent>() {
                @Override
                protected ITextComponent evaluate() {
                    if (MinecraftForge.EVENT_BUS.post(event)) {
                        return null;
                    }
                    return event.getComponent();
                }
            };
            MinecraftServer.getServerInst().processQueue.add(waitable);
            if (CatServer.getConfig().waitForgeServerChatEvent) {
                try {
                    return waitable.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return comp;
            }
        }
    }
}
