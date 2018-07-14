// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.command;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.rcon.RConConsoleSource;
import org.bukkit.command.RemoteConsoleCommandSender;

public class CraftRemoteConsoleCommandSender extends ServerCommandSender implements RemoteConsoleCommandSender
{
    private final RConConsoleSource listener;
    
    public CraftRemoteConsoleCommandSender(final RConConsoleSource listener) {
        this.listener = listener;
    }
    
    @Override
    public void sendMessage(final String message) {
        this.listener.addChatMessage(new TextComponentString(String.valueOf(message) + "\n"));
    }
    
    @Override
    public void sendMessage(final String[] messages) {
        for (final String message : messages) {
            this.sendMessage(message);
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
    public void setOp(final boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of remote controller.");
    }
}
