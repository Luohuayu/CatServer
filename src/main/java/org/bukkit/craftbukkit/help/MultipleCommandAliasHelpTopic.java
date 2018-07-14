// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.help;

import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.help.HelpTopic;

public class MultipleCommandAliasHelpTopic extends HelpTopic
{
    private final MultipleCommandAlias alias;
    
    public MultipleCommandAliasHelpTopic(final MultipleCommandAlias alias) {
        this.alias = alias;
        this.name = "/" + alias.getLabel();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < alias.getCommands().length; ++i) {
            if (i != 0) {
                sb.append(ChatColor.GOLD + " > " + ChatColor.WHITE);
            }
            sb.append("/");
            sb.append(alias.getCommands()[i].getLabel());
        }
        this.shortText = sb.toString();
        this.fullText = ChatColor.GOLD + "Alias for: " + ChatColor.WHITE + this.getShortText();
    }
    
    @Override
    public boolean canSee(final CommandSender sender) {
        if (this.amendedPermission != null) {
            return sender.hasPermission(this.amendedPermission);
        }
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        Command[] commands;
        for (int length = (commands = this.alias.getCommands()).length, i = 0; i < length; ++i) {
            final Command command = commands[i];
            if (!command.testPermissionSilent(sender)) {
                return false;
            }
        }
        return true;
    }
}
