package org.bukkit.craftbukkit.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftFunctionCommandSender extends ServerCommandSender {

    private final ICommandSender handle;

    public CraftFunctionCommandSender(ICommandSender handle) {
        this.handle = handle;
    }

    @Override
    public void sendMessage(String message) {
        for (ITextComponent component : CraftChatMessage.fromString(message)) {
            handle.sendMessage(component);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server function sender");
    }

    public ICommandSender getHandle() {
        return handle;
    }
}
