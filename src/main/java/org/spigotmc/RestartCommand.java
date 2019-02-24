package org.spigotmc;

import java.io.File;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RestartCommand extends Command
{

    public RestartCommand(String name)
    {
        super( name );
        this.description = "Restarts the server";
        this.usageMessage = "/restart";
        this.setPermission( "bukkit.command.restart" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args)
    {
        if ( testPermission( sender ) )
        {
            MinecraftServer.getServerInst().processQueue.add( new Runnable()
            {
                @Override
                public void run()
                {
                    restart();
                }
            } );
        }
        return true;
    }

    public static void restart()
    {
        restart( new File( SpigotConfig.restartScript ) );
    }

    public static void restart(final File script)
    {
        AsyncCatcher.enabled = false; // Disable async catcher incase it interferes with us
        try
        {
            if ( script.isFile() )
            {
                System.out.println( "Attempting to restart with " + SpigotConfig.restartScript );

                // Disable Watchdog
                WatchdogThread.doStop();

                // Kick all players
                for ( EntityPlayerMP p : (List< EntityPlayerMP>) MinecraftServer.getServerInst().getPlayerList().playerEntityList )
                {
                    p.connection.disconnect(SpigotConfig.restartMessage);
                }
                // Give the socket a chance to send the packets
                try
                {
                    Thread.sleep( 100 );
                } catch ( InterruptedException ex )
                {
                }
                // Close the socket so we can rebind with the new process
                MinecraftServer.getServerInst().getNetworkSystem().terminateEndpoints();

                // Give time for it to kick in
                try
                {
                    Thread.sleep( 100 );
                } catch ( InterruptedException ex )
                {
                }

                // Actually shutdown
                try
                {
                    MinecraftServer.getServerInst().stopServer();
                } catch ( Throwable t )
                {
                }

                // This will be done AFTER the server has completely halted
                Thread shutdownHook = new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            String os = System.getProperty( "os.name" ).toLowerCase(java.util.Locale.ENGLISH);
                            if ( os.contains( "win" ) )
                            {
                                Runtime.getRuntime().exec( "cmd /c start " + script.getPath() );
                            } else
                            {
                                Runtime.getRuntime().exec( new String[]
                                {
                                    "sh", script.getPath()
                                } );
                            }
                        } catch ( Exception e )
                        {
                            e.printStackTrace();
                        }
                    }
                };

                shutdownHook.setDaemon( true );
                Runtime.getRuntime().addShutdownHook( shutdownHook );
            } else
            {
                System.out.println( "Startup script '" + SpigotConfig.restartScript + "' does not exist! Stopping server." );

                // Actually shutdown
                try
                {
                    MinecraftServer.getServerInst().stopServer();
                } catch ( Throwable t )
                {
                }
            }
            FMLCommonHandler.instance().exitJava(0, false);
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }
}
