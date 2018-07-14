// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.conversations;

import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.Conversation;
import java.util.LinkedList;

public class ConversationTracker
{
    private LinkedList<Conversation> conversationQueue;
    
    public ConversationTracker() {
        this.conversationQueue = new LinkedList<Conversation>();
    }
    
    public synchronized boolean beginConversation(final Conversation conversation) {
        if (!this.conversationQueue.contains(conversation)) {
            this.conversationQueue.addLast(conversation);
            if (this.conversationQueue.getFirst() == conversation) {
                conversation.begin();
                conversation.outputNextPrompt();
                return true;
            }
        }
        return true;
    }
    
    public synchronized void abandonConversation(final Conversation conversation, final ConversationAbandonedEvent details) {
        if (!this.conversationQueue.isEmpty()) {
            if (this.conversationQueue.getFirst() == conversation) {
                conversation.abandon(details);
            }
            if (this.conversationQueue.contains(conversation)) {
                this.conversationQueue.remove(conversation);
            }
            if (!this.conversationQueue.isEmpty()) {
                this.conversationQueue.getFirst().outputNextPrompt();
            }
        }
    }
    
    public synchronized void abandonAllConversations() {
        final LinkedList<Conversation> oldQueue = this.conversationQueue;
        this.conversationQueue = new LinkedList<Conversation>();
        for (final Conversation conversation : oldQueue) {
            try {
                conversation.abandon(new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
            }
            catch (Throwable t) {
                Bukkit.getLogger().log(Level.SEVERE, "Unexpected exception while abandoning a conversation", t);
            }
        }
    }
    
    public synchronized void acceptConversationInput(final String input) {
        if (this.isConversing()) {
            this.conversationQueue.getFirst().acceptInput(input);
        }
    }
    
    public synchronized boolean isConversing() {
        return !this.conversationQueue.isEmpty();
    }
    
    public synchronized boolean isConversingModaly() {
        return this.isConversing() && this.conversationQueue.getFirst().isModal();
    }
}
