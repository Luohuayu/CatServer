package org.bukkit.craftbukkit.v1_16_R3.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import net.minecraft.command.CommandSource;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;

public class BukkitCommandWrapper implements com.mojang.brigadier.Command<CommandSource>, Predicate<CommandSource>, SuggestionProvider<CommandSource> {

    private final CraftServer server;
    private final Command command;

    public BukkitCommandWrapper(CraftServer server, Command command) {
        this.server = server;
        this.command = command;
    }

    public LiteralCommandNode<CommandSource> register(CommandDispatcher<CommandSource> dispatcher, String label) {
        return dispatcher.register(
            LiteralArgumentBuilder.<CommandSource>literal(label).requires(this).executes(this)
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("args", StringArgumentType.greedyString()).suggests(this).executes(this))
        );
    }

    @Override
    public boolean test(CommandSource wrapper) {
        return command.testPermissionSilent(wrapper.getBukkitSender());
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        return server.dispatchCommand(context.getSource().getBukkitSender(), context.getInput()) ? 1 : 0;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        List<String> results = server.tabComplete(context.getSource().getBukkitSender(), builder.getInput(), context.getSource().getLevel(), context.getSource().getPosition(), true);

        // Defaults to sub nodes, but we have just one giant args node, so offset accordingly
        builder = builder.createOffset(builder.getInput().lastIndexOf(' ') + 1);

        for (String s : results) {
            builder.suggest(s);
        }

        return builder.buildFuture();
    }
}
