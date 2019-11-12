package org.bukkit.craftbukkit.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import com.google.common.collect.Sets;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;

import jline.console.completer.Completer;
import org.bukkit.event.server.TabCompleteEvent;

public class ConsoleCommandCompleter implements Completer {
    private final CraftServer server;

    public ConsoleCommandCompleter(CraftServer server) {
        this.server = server;
    }

    public int complete(final String buffer, final int cursor, final List<CharSequence> candidates) {
        Waitable<List<String>> waitable = new Waitable<List<String>>() {
            @Override
            protected List<String> evaluate() {
                List<String> offers1 = server.getCommandMap().tabComplete(server.getConsoleSender(), buffer);
                List<String> offers2 = server.getCraftCommandMap().tabComplete(server.getConsoleSender(), buffer);
                List<String> offers = new ArrayList<>(Sets.union(offers1 == null ? Collections.EMPTY_SET : Sets.newHashSet(offers1), offers2 == null ? Collections.EMPTY_SET : Sets.newHashSet(offers2)));

                TabCompleteEvent tabEvent = new TabCompleteEvent(server.getConsoleSender(), buffer, offers);
                server.getPluginManager().callEvent(tabEvent);

                return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
            }
        };
        this.server.getServer().processQueue.add(waitable);
        try {
            List<String> offers = waitable.get();
            if (offers == null) {
                return cursor;
            }
            candidates.addAll(offers);

            final int lastSpace = buffer.lastIndexOf(' ');
            if (lastSpace == -1) {
                return cursor - buffer.length();
            } else {
                return cursor - (buffer.length() - lastSpace - 1);
            }
        } catch (ExecutionException e) {
            this.server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return cursor;
    }
}
