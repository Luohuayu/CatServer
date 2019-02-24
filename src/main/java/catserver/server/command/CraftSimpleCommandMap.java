package catserver.server.command;

import java.util.Arrays;
import java.util.regex.Pattern;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.FMLCommonHandler;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class CraftSimpleCommandMap extends SimpleCommandMap {

    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
    private ICommandSender vanillaConsoleSender;

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
            if (target instanceof ModCustomCommand)
            {
                if (!target.testPermission(sender)) return true;
                if (sender instanceof ConsoleCommandSender)
                {
                    FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(this.vanillaConsoleSender, commandLine);
                }
                else FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(((CraftPlayer)sender).getHandle(), commandLine);
            }
            else {
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

    // CatServer start - sets the vanilla console sender
    public void setVanillaConsoleSender(ICommandSender console)
    {
        this.vanillaConsoleSender = console;
    }
    // CatServer end
}