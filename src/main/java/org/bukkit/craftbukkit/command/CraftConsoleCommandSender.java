// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.command;

import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.conversations.Conversation;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.command.ConsoleCommandSender;

public class CraftConsoleCommandSender extends ServerCommandSender implements ConsoleCommandSender
{
    protected final ConversationTracker conversationTracker;
    
    protected CraftConsoleCommandSender() {
        this.conversationTracker = new ConversationTracker();
    }
    
    @Override
    public void sendMessage(final String message) {
        this.sendRawMessage(message);
    }
    
    @Override
    public void sendRawMessage(final String message) {
        System.out.println(ChatColor.stripColor(message));
    }
    
    @Override
    public void sendMessage(final String[] messages) {
        for (final String message : messages) {
            this.sendMessage(message);
        }
    }
    
    @Override
    public String getName() {
        return "CONSOLE";
    }
    
    @Override
    public boolean isOp() {
        return true;
    }
    
    @Override
    public void setOp(final boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server console");
    }
    
    @Override
    public boolean beginConversation(final Conversation conversation) {
        return this.conversationTracker.beginConversation(conversation);
    }
    
    @Override
    public void abandonConversation(final Conversation conversation) {
        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }
    
    @Override
    public void abandonConversation(final Conversation conversation, final ConversationAbandonedEvent details) {
        this.conversationTracker.abandonConversation(conversation, details);
    }
    
    @Override
    public void acceptConversationInput(final String input) {
        this.conversationTracker.acceptConversationInput(input);
    }
    
    @Override
    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }
}
