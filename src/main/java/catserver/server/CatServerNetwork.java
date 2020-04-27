package catserver.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.network.ForgeMessage;
import net.minecraftforge.common.network.ForgeNetworkHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;

import java.util.Map;

public class CatServerNetwork {
    public static boolean isSendDataSerializers(Map<String, String> modList) {
        String forgeVersion = modList.get("forge");
        if (forgeVersion != null) {
            try {
                if (Integer.parseInt(forgeVersion.split("\\.")[3]) < 2826) {
                    return false;
                }
            } catch (Exception ignored) {}
        }
        return true;
    }

    public static void registerBukkitWorldToClient(EntityPlayerMP player, int dimension) {
        if (DimensionManager.isBukkitDimension(dimension)) {
            FMLEmbeddedChannel serverChannel = ForgeNetworkHandler.getServerChannel();
            serverChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            serverChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            serverChannel.writeOutbound(new ForgeMessage.DimensionRegisterMessage(dimension, DimensionManager.getProviderType(dimension).name()));
        }
    }
}
