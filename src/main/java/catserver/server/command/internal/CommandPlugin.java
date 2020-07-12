package catserver.server.command.internal;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandPlugin extends Command {
    public CommandPlugin(String name) {
        super(name);
        this.description = "Load or unload plugin";
        this.usageMessage = "/plugin <load|unload|reload> <name>";
        setPermission("catserver.command.plugin");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 2) {
            sender.sendMessage(ChatColor.GOLD + "Usage: " + usageMessage);
            return false;
        }

        String action = args[0].toLowerCase();
        String pluginName = args[1];
        try {
            if (action.equals("unload")) {
                unloadPlugin(pluginName, sender);
            } else if (action.equals("load")) {
                loadPlugin(pluginName, sender);
            } else if (action.equals("reload")) {
                reloadPlugin(pluginName, sender);
            } else {
                sender.sendMessage(ChatColor.GREEN + "Invalid action specified.");
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.GREEN + "Error with " + pluginName + ": " + e.toString());
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> tabs = Lists.newArrayList();
        if (args.length > 1) {
            String action = args[0].toLowerCase();
            if (action.equals("unload")) {
                for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
                    tabs.add(plugin.getName());
                }
            } else if (action.equals("load")) {
                for (File file : new File("plugins").listFiles()) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".jar"))
                        tabs.add(file.getName().substring(0, file.getName().length() - 4));
                }
            }
        }
        return tabs;
    }

    private void unloadPlugin(String pluginName, CommandSender sender) throws Exception {
        SimplePluginManager manager = (SimplePluginManager) Bukkit.getServer().getPluginManager();

        List<Plugin> plugins = ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "plugins");
        Map<String, Plugin> lookupNames = ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "lookupNames");
        SimpleCommandMap commandMap = ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "commandMap");
        Map<String, Command> knownCommands = ReflectionHelper.getPrivateValue(SimpleCommandMap.class, commandMap, "knownCommands");

        for (Plugin plugin : manager.getPlugins()) {
            if (!plugin.getDescription().getName().equalsIgnoreCase(pluginName)) continue;

            manager.disablePlugin(plugin);
            plugins.remove(plugin);
            lookupNames.remove(pluginName);

            Iterator<Map.Entry<String, Command>> it = knownCommands.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Command> entry = (Map.Entry) it.next();
                if (!(entry.getValue() instanceof PluginCommand)) continue;
                PluginCommand command = (PluginCommand) entry.getValue();
                if (command.getPlugin() == plugin) {
                    command.unregister(commandMap);
                    it.remove();
                }
            }

            sender.sendMessage(ChatColor.GREEN + "Unloaded " + pluginName + " successfully!");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Can't found loaded plugin: " + pluginName);
    }


    private void loadPlugin(String pluginName, CommandSender sender) {
        PluginManager manager = Bukkit.getServer().getPluginManager();
        File pluginFile = new File("plugins", pluginName + ".jar");

        if (!pluginFile.exists() || !pluginFile.isFile()) {
            sender.sendMessage(ChatColor.GREEN + "Error loading " + pluginName + ".jar, no plugin with that name was found.");
            return;
        }

        try {
            Plugin plugin = manager.loadPlugin(pluginFile);
            plugin.onLoad();
            manager.enablePlugin(plugin);

            sender.sendMessage(ChatColor.GREEN + "Loaded " + pluginName + " successfully!");
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load '" + pluginFile.getPath() + "' in folder '" + pluginFile.getParentFile().getPath() + "'", ex);
            sender.sendMessage(ChatColor.GREEN + "Error loading " + pluginName + ".jar, this plugin must be reloaded by restarting the server.");
        }
    }

    private void reloadPlugin(String pluginName, CommandSender sender) {
        SimplePluginManager manager = (SimplePluginManager) Bukkit.getServer().getPluginManager();

        List<Plugin> plugins = ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "plugins");
        Map<String, Plugin> lookupNames = ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "lookupNames");
        SimpleCommandMap commandMap = ReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "commandMap");
        Map<String, Command> knownCommands = ReflectionHelper.getPrivateValue(SimpleCommandMap.class, commandMap, "knownCommands");

        for (Plugin plugin : manager.getPlugins()) {
            if (!plugin.getDescription().getName().equalsIgnoreCase(pluginName)) continue;

            manager.disablePlugin(plugin);
            plugins.remove(plugin);
            lookupNames.remove(pluginName);

            Iterator<Map.Entry<String, Command>> it = knownCommands.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Command> entry = (Map.Entry) it.next();
                if (!(entry.getValue() instanceof PluginCommand)) continue;
                PluginCommand command = (PluginCommand) entry.getValue();
                if (command.getPlugin() == plugin) {
                    command.unregister(commandMap);
                    it.remove();
                }
            }

            File pluginFile = ReflectionHelper.getPrivateValue(JavaPlugin.class, (JavaPlugin)plugin, "file");

            try {
                plugin = manager.loadPlugin(pluginFile);
                plugin.onLoad();
                manager.enablePlugin(plugin);

                sender.sendMessage(ChatColor.GREEN + "Reloaded " + pluginName + " successfully!");
            } catch (Exception ex) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not load '" + pluginFile.getPath() + "' in folder '" + pluginFile.getParentFile().getPath() + "'", ex);
                sender.sendMessage(ChatColor.GREEN + "Error loading " + pluginName + ".jar, this plugin must be reloaded by restarting the server.");
            }

            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Can't found loaded plugin: " + pluginName);
    }
}
