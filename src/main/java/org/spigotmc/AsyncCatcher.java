package org.spigotmc;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class AsyncCatcher
{

    public static boolean enabled = true;

    public static void catchOp(String reason)
    {
        if ( enabled && Thread.currentThread() != FMLCommonHandler.instance().getMinecraftServerInstance().getServer().primaryThread )
        {
            throw new IllegalStateException( "Asynchronous " + reason + "!" );
        }
    }
}
