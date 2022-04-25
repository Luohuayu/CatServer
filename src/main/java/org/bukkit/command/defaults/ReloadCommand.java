package org.bukkit.command.defaults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends BukkitCommand {
    public ReloadCommand(String name) {
        super(name);
        this.description = "Reloads the server configuration and plugins";
        this.usageMessage = "/reload";
        this.setPermission("bukkit.command.reload");
        this.setAliases(Arrays.asList("rl"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        Command.broadcastCommandMessage(sender, ChatColor.RED + "Please note that this command is not supported and may cause issues when using some plugins.");
        Command.broadcastCommandMessage(sender, ChatColor.RED + "If you encounter any issues please use the /stop command to restart your server.");
        // CatServer start+
        if (args.length == 0) {
            Command.broadcastCommandMessage(sender, ChatColor.RED + "------[WARN]------");
            Command.broadcastCommandMessage(sender, ChatColor.RED + catserver.server.utils.LanguageUtils.I18nToString("command.reload.warn"));
            Command.broadcastCommandMessage(sender, ChatColor.RED + catserver.server.utils.LanguageUtils.I18nToString("command.reload.warn.confirm"));
        } else if (Objects.equals(args[0], "confirm")) {
            ((org.bukkit.craftbukkit.CraftServer) Bukkit.getServer()).reloadConfirm();
            Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Reload complete.");
        }
        // CatServer end

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }
}
