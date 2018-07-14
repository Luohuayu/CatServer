// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.command;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.Collection;
import org.bukkit.event.Event;
import org.bukkit.event.server.TabCompleteEvent;

import jline.console.completer.Completer;

import java.util.Collections;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.util.Waitable;
import java.util.List;
import org.bukkit.craftbukkit.CraftServer;

public class ConsoleCommandCompleter implements Completer
{
    private final CraftServer server;
    
    public ConsoleCommandCompleter(final CraftServer server) {
        this.server = server;
    }
    
    @Override
    public int complete(final String buffer, final int cursor, final List<CharSequence> candidates) {
        final Waitable<List<String>> waitable = new Waitable<List<String>>() {
            @Override
            protected List<String> evaluate() {
                final List<String> offers = ConsoleCommandCompleter.this.server.getCommandMap().tabComplete(ConsoleCommandCompleter.this.server.getConsoleSender(), buffer);
                final TabCompleteEvent tabEvent = new TabCompleteEvent(ConsoleCommandCompleter.this.server.getConsoleSender(), buffer, (offers == null) ? Collections.EMPTY_LIST : offers);
                ConsoleCommandCompleter.this.server.getPluginManager().callEvent(tabEvent);
                return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
            }
        };
        this.server.getServer().processQueue.add(waitable);
        try {
            final List<String> offers = waitable.get();
            if (offers == null) {
                return cursor;
            }
            candidates.addAll(offers);
            final int lastSpace = buffer.lastIndexOf(32);
            if (lastSpace == -1) {
                return cursor - buffer.length();
            }
            return cursor - (buffer.length() - lastSpace - 1);
        }
        catch (ExecutionException e) {
            this.server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return cursor;
    }
}
