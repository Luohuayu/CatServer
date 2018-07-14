// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.command;

import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.command.ConsoleCommandSender;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.Iterator;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import org.apache.logging.log4j.Level;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.Validate;
import java.util.List;
import net.minecraft.command.ICommandSender;
import org.bukkit.command.CommandSender;
import net.minecraft.command.CommandBase;
import org.bukkit.command.defaults.VanillaCommand;

public final class VanillaCommandWrapper extends VanillaCommand
{
    protected final CommandBase vanillaCommand;
    public static CommandSender lastSender;
    
    static {
        VanillaCommandWrapper.lastSender = null;
    }
    
    public VanillaCommandWrapper(final CommandBase vanillaCommand, final String usage) {
        super(vanillaCommand.getCommandName(), "A Mojang provided command.", usage, vanillaCommand.getCommandAliases());
        this.vanillaCommand = vanillaCommand;
        this.setPermission("minecraft.command." + vanillaCommand.getCommandName());
    }
    
    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        final ICommandSender icommandlistener = this.getListener(sender);
        this.dispatchVanillaCommand(sender, icommandlistener, args);
        return true;
    }
    
    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        Validate.notNull((Object)sender, "Sender cannot be null");
        Validate.notNull((Object)args, "Arguments cannot be null");
        Validate.notNull((Object)alias, "Alias cannot be null");
        return this.vanillaCommand.getTabCompletionOptions(MinecraftServer.getServerInst(), this.getListener(sender), args, new BlockPos(0, 0, 0));
    }
    
    public final int dispatchVanillaCommand(final CommandSender bSender, final ICommandSender icommandlistener, final String[] as) {
        final int i = this.getPlayerListSize(as);
        int j = 0;
        //final WorldServer[] prev = MinecraftServer.getServerInst().worldServers;
        final MinecraftServer server = MinecraftServer.getServerInst();
        //(server.worldServers = new WorldServer[/*server.worlds.size()*/ server.worldServers.length])[0] = (WorldServer)icommandlistener.getEntityWorld();
        //int bpos = 0;
        //for (int pos = 1; pos < server.worldServers.length; ++pos) {
        //    final WorldServer world = server.worlds.get(bpos++);
        //    if (server.worldServer[0] == world) {
        //        --pos;
        //    }
        //    else {
        //        server.worldServer[pos] = world;
        //    }
        //}
        Label_0784: {
            try {
                if (!this.vanillaCommand.checkPermission(server, icommandlistener)) {
                    final TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                    chatmessage.getStyle().setColor(TextFormatting.RED);
                    icommandlistener.addChatMessage(chatmessage);
                    break Label_0784;
                }
                if (i > -1) {
                    final List<Entity> list = EntitySelector.matchEntities(icommandlistener, as[i], (Class<? extends Entity>)Entity.class);
                    final String s2 = as[i];
                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                    for (final Entity entity : list) {
                        final CommandSender oldSender = VanillaCommandWrapper.lastSender;
                        VanillaCommandWrapper.lastSender = bSender;
                        try {
                            as[i] = entity.getUniqueID().toString();
                            this.vanillaCommand.execute(server, icommandlistener, as);
                            ++j;
                        }
                        catch (WrongUsageException exceptionusage) {
                            final TextComponentTranslation chatmessage2 = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorObjects()) });
                            chatmessage2.getStyle().setColor(TextFormatting.RED);
                            icommandlistener.addChatMessage(chatmessage2);
                        }
                        catch (CommandException commandexception) {
                            CommandBase.notifyCommandListener(icommandlistener, this.vanillaCommand, 0, commandexception.getMessage(), commandexception.getErrorObjects());
                        }
                        finally {
                            VanillaCommandWrapper.lastSender = oldSender;
                        }
                        VanillaCommandWrapper.lastSender = oldSender;
                    }
                    as[i] = s2;
                    break Label_0784;
                }
                icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                this.vanillaCommand.execute(server, icommandlistener, as);
                ++j;
            }
            catch (WrongUsageException exceptionusage2) {
                final TextComponentTranslation chatmessage3 = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage2.getMessage(), exceptionusage2.getErrorObjects()) });
                chatmessage3.getStyle().setColor(TextFormatting.RED);
                icommandlistener.addChatMessage(chatmessage3);
            }
            catch (CommandException commandexception2) {
                CommandBase.notifyCommandListener(icommandlistener, this.vanillaCommand, 0, commandexception2.getMessage(), commandexception2.getErrorObjects());
            }
            catch (Throwable throwable) {
                final TextComponentTranslation chatmessage4 = new TextComponentTranslation("commands.generic.exception", new Object[0]);
                chatmessage4.getStyle().setColor(TextFormatting.RED);
                icommandlistener.addChatMessage(chatmessage4);
                if (icommandlistener.getCommandSenderEntity() instanceof EntityMinecartCommandBlock) {
                    MinecraftServer.LOG.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", icommandlistener.getPosition().getX(), icommandlistener.getPosition().getY(), icommandlistener.getPosition().getZ()), throwable);
                }
                else if (icommandlistener instanceof CommandBlockBaseLogic) {
                    final CommandBlockBaseLogic listener = (CommandBlockBaseLogic)icommandlistener;
                    MinecraftServer.LOG.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getPosition().getX(), listener.getPosition().getY(), listener.getPosition().getZ()), throwable);
                }
                else {
                    MinecraftServer.LOG.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command", new Object[0]), throwable);
                }
            }
            //finally {
            //    MinecraftServer.getServerInst().worldServers = prev;
            //}
        }
        //MinecraftServer.getServerInst().worldServer = prev;
        icommandlistener.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
        return j;
    }
    
    private ICommandSender getListener(final CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer)sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender)sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((CraftMinecartCommand)sender).getHandle().getCommandBlockLogic();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer)MinecraftServer.getServerInst()).rconConsoleSource;
        }
        if (sender instanceof ConsoleCommandSender) {
            return (ICommandSender)((CraftServer)sender.getServer()).getServer();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender)sender).getHandle();
        }
        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }
    
    private int getPlayerListSize(final String[] as) {
        for (int i = 0; i < as.length; ++i) {
            if (this.vanillaCommand.isUsernameIndex(as, i) && EntitySelector.matchesMultiplePlayers(as[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public static String[] dropFirstArgument(final String[] as) {
        final String[] as2 = new String[as.length - 1];
        for (int i = 1; i < as.length; ++i) {
            as2[i - 1] = as[i];
        }
        return as2;
    }
}
