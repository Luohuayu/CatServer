package catserver.server.command.internal;

import catserver.server.CatServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;

public class CommandCatserver extends Command {
    public CommandCatserver(String name) {
        super(name);
        this.description = "CatServer related commands";
        this.usageMessage = "/catserver worlds|reload|reloadall";
        setPermission("catserver.command.catserver");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if (args[0].equals("worlds")) {
            sender.sendMessage(formatStringLength("Dim", 8) + " " + formatStringLength("Name", 8) + " " + formatStringLength("Type", 8));
            for (Integer dimension : DimensionManager.getStaticDimensionIDs()) {
                World world = DimensionManager.getWorld(dimension, false);
                String name = (world != null ? world.getWorld().getName() : "(Unload)");
                String type = DimensionManager.getProviderType(dimension).toString();
                sender.sendMessage(formatStringLength(String.valueOf(dimension), 8) + " " + formatStringLength(name, 8) + " " + formatStringLength(type, 8));
            }
        } else if (args[0].equals("reload")) {
            CatServer.getConfig().loadConfig();
            sender.sendMessage(ChatColor.GREEN + "Configuration reload complete.");
        } else if (args[0].equals("reloadall")) {
            CatServer.getConfig().loadConfig();
            ((CraftServer) Bukkit.getServer()).reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Configuration reload complete.");
        }

        return true;
    }

    private static String formatStringLength(String str, int size) {
        int formatLength = size - str.length();
        for (int i = 0; i < formatLength; i++) {
            str += " ";
        }
        return str;
    }
}
