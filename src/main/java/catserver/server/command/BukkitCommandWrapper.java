package catserver.server.command;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender;

import com.google.common.collect.ImmutableList;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;

public class BukkitCommandWrapper implements ICommand {
    private final CommandSender bukkitSender;
    private final String name;
    private final Command command;

    public BukkitCommandWrapper(CommandSender bukkitSender, String name, Command command) {
        this.bukkitSender = bukkitSender;
        this.command = command;
        this.name = name;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getName() {
        return this.command.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return this.command.getDescription();
    }

    @Override
    public List<String> getAliases() {
        return this.command.getAliases();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            this.command.execute(bukkitSender, name, args);
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return this.command.testPermission(bukkitSender);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        try {
            return this.command.tabComplete(bukkitSender, name, args);
        } catch (Exception e) {
            e.printStackTrace();
            return ImmutableList.of();
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Nullable
    public static BukkitCommandWrapper toNMSCommand(ICommandSender sender, String name) {
        Command command;
        CommandSender bukkitSender;
        if ((command = MinecraftServer.getServerInst().server.getCommandMap().getCommand(name)) != null && (bukkitSender = toBukkitSender(sender)) != null) {
            return new BukkitCommandWrapper(bukkitSender, name, command);
        }
        return null;
    }

    @Nullable
    public static CommandSender toBukkitSender(ICommandSender sender) {
        if (sender instanceof MinecraftServer) return MinecraftServer.getServerInst().console;
        if (sender instanceof RConConsoleSource) return new CraftRemoteConsoleCommandSender((RConConsoleSource) sender);
        if (sender instanceof CommandBlockBaseLogic) return new CraftBlockCommandSender(sender);
        if (sender instanceof Entity) return ((Entity) sender).getBukkitEntity();
        return null;
    }
}
