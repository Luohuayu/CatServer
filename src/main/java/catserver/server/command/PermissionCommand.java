package catserver.server.command;

import catserver.server.CatServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class PermissionCommand extends Command {
    public PermissionCommand(String name) {
        super(name);
        this.description = "Reload fake player permission file";
        this.usageMessage = "/fakefile reload";
        setPermission("catserver.command.fakefile");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1) return false;
        if (! args[0].equals("reload")) return false;
        try {
            CatServer.reloadFakePlayerPermissions();
            sender.sendMessage("SUCCESS");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage("FAIL");
            return true;
        }
    }
}
