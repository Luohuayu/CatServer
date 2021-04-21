package org.bukkit.craftbukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class ColouredConsoleSender extends CraftConsoleCommandSender {

    protected ColouredConsoleSender() {}

    public static ConsoleCommandSender getInstance() {
        if (Bukkit.getConsoleSender() != null) {
            return Bukkit.getConsoleSender();
        } else {
            return new ColouredConsoleSender();
        }
    }
}
