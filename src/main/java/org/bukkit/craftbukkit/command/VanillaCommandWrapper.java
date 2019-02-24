package org.bukkit.craftbukkit.command;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.server.*;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.LoaderExceptionModCrash;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper extends BukkitCommand {
    protected final CommandBase vanillaCommand;

    public VanillaCommandWrapper(CommandBase vanillaCommand, String usage) {
        super(vanillaCommand.getName(), "A Mojang provided command.", usage, vanillaCommand.getAliases());
        this.vanillaCommand = vanillaCommand;
        this.setPermission("minecraft.command." + vanillaCommand.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        ICommandSender icommandlistener = getListener(sender);
        try {
            dispatchVanillaCommand(sender, icommandlistener, args);
        } catch (CommandException commandexception) {
            // Taken from CommandHandler
            TextComponentTranslation chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            chatmessage.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage);
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List<String>) vanillaCommand.getTabCompletions(MinecraftServer.getServerInst(), getListener(sender), args, (location) == null ? null : new BlockPos(location.getX(), location.getY(), location.getZ()));
    }

    public static CommandSender lastSender = null; // Nasty :(

    public final int dispatchVanillaCommand(CommandSender bSender, ICommandSender icommandlistener, String[] as) throws CommandException {
        // Copied from net.minecraft.server.CommandHandler
        int i = getPlayerListSize(as);
        int j = 0;
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        //WorldServer[] prev = MinecraftServer.getServerInst().worlds;
        MinecraftServer server = MinecraftServer.getServerInst();
        //server.worlds = new WorldServer[server.worldServerList.size()];
        //server.worlds[0] = (WorldServer) icommandlistener.getEntityWorld();
        //int bpos = 0;
        //for (int pos = 1; pos < server.worlds.length; pos++) {
            //WorldServer world = server.worldServerList.get(bpos++);
            //if (server.worlds[0] == world) {
                //pos--;
                //continue;
            //}
            //server.worlds[pos] = world;
        //}

        try {
            if (vanillaCommand.checkPermission(server, icommandlistener)) {
                if (i > -1) {
                    List<Entity> list = ((List<Entity>) EntitySelector.matchEntitiesDefault(icommandlistener, as[i], Entity.class));
                    String s2 = as[i];

                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                    Iterator<Entity> iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = iterator.next();

                        CommandSender oldSender = lastSender;
                        lastSender = bSender;
                        try {
                            as[i] = entity.getUniqueID().toString();
                            vanillaCommand.execute(server, icommandlistener, as);
                            j++;
                        } catch (WrongUsageException exceptionusage) {
                            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorObjects())});
                            chatmessage.getStyle().setColor(TextFormatting.RED);
                            icommandlistener.sendMessage(chatmessage);
                        } catch (CommandException commandexception) {
                            CommandBase.notifyCommandListener(icommandlistener, vanillaCommand, 0, commandexception.getMessage(), commandexception.getErrorObjects());
                        } finally {
                            lastSender = oldSender;
                        }
                    }
                    as[i] = s2;
                } else {
                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                    vanillaCommand.execute(server, icommandlistener, as);
                    j++;
                }
            } else {
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                chatmessage.getStyle().setColor(TextFormatting.RED);
                icommandlistener.sendMessage(chatmessage);
            }
        } catch (WrongUsageException exceptionusage) {
            TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorObjects()) });
            chatmessage1.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage1);
        } catch (CommandException commandexception) {
            CommandBase.notifyCommandListener(icommandlistener, vanillaCommand, 0, commandexception.getMessage(), commandexception.getErrorObjects());
        } catch (Throwable throwable) {
            TextComponentTranslation chatmessage3 = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            chatmessage3.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage3);
            if (icommandlistener.getCommandSenderEntity() instanceof EntityMinecartCommandBlock) {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", icommandlistener.getPosition().getX(), icommandlistener.getPosition().getY(), icommandlistener.getPosition().getZ()), throwable);
            } else if(icommandlistener instanceof CommandBlockBaseLogic) {
                CommandBlockBaseLogic listener = (CommandBlockBaseLogic) icommandlistener;
                MinecraftServer.LOGGER.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getPosition().getX(), listener.getPosition().getY(), listener.getPosition().getZ()), throwable);
            } else {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), throwable);
            }
        } finally {
            icommandlistener.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
            //MinecraftServer.getServerInst().worlds = prev;
        }
        return j;
    }

    private ICommandSender getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlockLogic();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer)MinecraftServer.getServerInst()).rconConsoleSource;
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }
        if (sender instanceof CraftFunctionCommandSender) {
            return ((CraftFunctionCommandSender) sender).getHandle();
        }
        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    private int getPlayerListSize(String as[]) throws CommandException {
        for (int i = 0; i < as.length; i++) {
            if (vanillaCommand.isUsernameIndex(as, i) && EntitySelector.matchesMultiplePlayersDefault(as[i])) {
                return i;
            }
        }
        return -1;
    }

    public static String[] dropFirstArgument(String as[]) {
        String as1[] = new String[as.length - 1];
        for (int i = 1; i < as.length; i++) {
            as1[i - 1] = as[i];
        }

        return as1;
    }
}
