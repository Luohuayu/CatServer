/**
 * https://github.com/IzzelAliz/Arclight/blob/1.18/arclight-common/src/main/java/io/izzel/arclight/common/mod/util/BukkitDispatcher.java
 */
package catserver.server.command;

import catserver.server.CatServer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_18_R2.command.VanillaCommandWrapper;

public class CatServerCommandDispatcher extends CommandDispatcher<CommandSourceStack> {

    private final Commands commands;

    public CatServerCommandDispatcher(Commands commands) {
        this.commands = commands;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> register(LiteralArgumentBuilder<CommandSourceStack> command) {
        LiteralCommandNode<CommandSourceStack> node = command.build();
        if (Bukkit.getServer() != null && !(node.getCommand() instanceof BukkitCommandWrapper)) {
            VanillaCommandWrapper wrapper = new VanillaCommandWrapper(this.commands, node);
            ((CraftServer) Bukkit.getServer()).getCommandMap().register("forge", wrapper);
            CatServer.LOGGER.info("Successfully registered CommandWrapper: {}", wrapper);
        }
        getRoot().addChild(node);
        return node;
    }
}
