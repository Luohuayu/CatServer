package org.bukkit.craftbukkit.v1_16_R3.command;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class ColouredConsoleSender extends CraftConsoleCommandSender {

    protected ColouredConsoleSender() {}

    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
    }

    public static ConsoleCommandSender getInstance() {
        if (Bukkit.getConsoleSender() != null) {
            return Bukkit.getConsoleSender();
        } else {
            return new ColouredConsoleSender();
        }
    }
}
