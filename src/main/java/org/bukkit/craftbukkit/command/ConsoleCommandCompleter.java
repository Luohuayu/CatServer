package org.bukkit.craftbukkit.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import com.google.common.collect.Sets;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;

import net.minecraft.server.dedicated.DedicatedServer;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.bukkit.event.server.TabCompleteEvent;

public class ConsoleCommandCompleter implements Completer {
    private final DedicatedServer server;

    public ConsoleCommandCompleter(DedicatedServer server) {
        this.server = server;
    }

    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        final CraftServer server = this.server.server;
        final String buffer = line.line();

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

        server.getServer().processQueue.add(waitable);

        try {
            List<String> offers = waitable.get();
            if (offers == null) {
                return;
            }

            for (String completion : offers) {
                if (completion.isEmpty()) {
                    continue;
                }

                candidates.add(new Candidate(completion));
            }
        } catch (ExecutionException e) {
            server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
