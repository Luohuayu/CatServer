package org.bukkit.command;

import org.bukkit.Server;
import org.bukkit.permissions.Permissible;

public interface CommandSender extends Permissible {

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
    public void sendMessage(String message);

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     */
    public void sendMessage(String[] messages);

    /**
     * Returns the server instance that this command is running on
     *
     * @return Server instance
     */
    public Server getServer();

    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
    public String getName();

    // Spigot start
    public class Spigot
    {

        /**
         * Sends this sender a chat component.
         *
         * @param component the components to send
         */
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the sender.
         *
         * @param components the components to send
         */
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    Spigot spigot();
    // Spigot end
}
