package catserver.server.hook;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.function.Predicate;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper;

public class CommandNodeHook {
    public static synchronized  /* Thread safety */ boolean canUse(CommandNode inst, Object source, Predicate requirement) {
        if (source instanceof CommandSource) {
            try {
                ((CommandSource) source).currentCommand = inst;
                return requirement.test(source);
            } finally {
                ((CommandSource) source).currentCommand = null;
            }
        }
        return requirement.test(source);
    }

    public static void dispatcher(CommandNode inst, CommandNode node) {
        if (inst.getClass() != RootCommandNode.class) return;
        if (Bukkit.getServer() instanceof CraftServer) {
            CraftServer craftServer = (CraftServer) Bukkit.getServer();
            if (craftServer.isVanillaCommandRegistered && !craftServer.isSyncingCommand && !(node.getCommand() instanceof BukkitCommandWrapper)) {
                Commands dispatcher = craftServer.getServer().getCommands();
                RootCommandNode<CommandSource> vanillaRoot = dispatcher.getDispatcher().getRoot();
                if (inst == vanillaRoot) {
                    craftServer.getCommandMap().register("minecraft", new VanillaCommandWrapper(dispatcher, (CommandNode<CommandSource>)node));
                }
            }
        }
    }
}
