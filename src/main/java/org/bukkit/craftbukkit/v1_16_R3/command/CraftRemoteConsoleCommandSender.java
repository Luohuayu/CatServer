package org.bukkit.craftbukkit.v1_16_R3.command;

import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import org.bukkit.command.RemoteConsoleCommandSender;

public class CraftRemoteConsoleCommandSender extends ServerCommandSender implements RemoteConsoleCommandSender {

    private final RConConsoleSource listener;

    public CraftRemoteConsoleCommandSender(RConConsoleSource listener) {
        this.listener = listener;
    }

    @Override
    public void sendMessage(String message) {
        listener.sendMessage(new StringTextComponent(message + "\n"), Util.NIL_UUID); // Send a newline after each message, to preserve formatting.
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return "Rcon";
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of remote controller.");
    }
}
