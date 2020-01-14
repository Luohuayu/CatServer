package catserver.server.command.internal;

import catserver.server.CatServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandCatserver extends Command {
    public CommandCatserver(String name) {
        super(name);
        this.description = "CatServer related commands";
        this.usageMessage = "/catserver reload";
        setPermission("catserver.command.catserver");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if (args[0].equals("reload")) {
            CatServer.getConfig().loadConfig();
            sender.sendMessage(ChatColor.GREEN + "Configuration reload complete.");
        }

        return true;
    }
}
