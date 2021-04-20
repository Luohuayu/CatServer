package catserver.server.command;

import java.util.Arrays;
import java.util.regex.Pattern;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.command.CraftFunctionCommandSender;
import org.bukkit.craftbukkit.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.entity.CraftEntity;

public class CraftSimpleCommandMap extends SimpleCommandMap {
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
    private final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

    public CraftSimpleCommandMap(Server server) {
        super(server);
    }

    /**
     * {@inheritDoc}
     */
    public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
        String[] args = PATTERN_ON_SPACE.split(commandLine);

        if (args.length == 0) {
            return false;
        }

        String sentCommandLabel = args[0].toLowerCase();
        Command target = getCommand(sentCommandLabel);

        if (target == null) {
            return false;
        }
        try {
            // CatServer start - if command is a mod command, check permissions and route through vanilla
            if (target instanceof ModCustomCommand) {
                if (!target.testPermission(sender)) return true;
                if (sender instanceof ConsoleCommandSender) {
                    server.getCommandManager().executeCommand(this.server, commandLine);
                } else if (sender instanceof RemoteConsoleCommandSender) {
                    server.getCommandManager().executeCommand(((DedicatedServer)server).rconConsoleSource, commandLine);
                } else if (sender instanceof CraftEntity) {
                    server.getCommandManager().executeCommand(((CraftEntity) sender).getHandle(), commandLine);
                } else if (sender instanceof CraftBlockCommandSender) {
                    server.getCommandManager().executeCommand(((CraftBlockCommandSender) sender).getTileEntity(), commandLine);
                } else if (sender instanceof ProxiedCommandSender) {
                    server.getCommandManager().executeCommand(((ProxiedNativeCommandSender) sender).getHandle(), commandLine);
                } else if (sender instanceof CraftFunctionCommandSender) {
                    server.getCommandManager().executeCommand(((CraftFunctionCommandSender) sender).getHandle(), commandLine);
                } else {
                    throw new CommandException("Unknown sender type: " + sender.getClass().getName());
                }
            } else {
                // CatServer end
                // Note: we don't return the result of target.execute as thats success / failure, we return handled (true) or not handled (false)
                target.execute(sender, sentCommandLabel, Arrays.copyOfRange(args, 1, args.length));
            }
        } catch (CommandException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing '" + commandLine + "' in " + target, ex);
        }

        // return true as command was handled
        return true;
    }

    @Deprecated
    public void setVanillaConsoleSender(ICommandSender console) { }
}