// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.help;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

public class CommandAliasHelpTopic extends HelpTopic
{
    private final String aliasFor;
    private final HelpMap helpMap;
    
    public CommandAliasHelpTopic(final String alias, final String aliasFor, final HelpMap helpMap) {
        this.aliasFor = (aliasFor.startsWith("/") ? aliasFor : ("/" + aliasFor));
        this.helpMap = helpMap;
        this.name = (alias.startsWith("/") ? alias : ("/" + alias));
        Validate.isTrue(!this.name.equals(this.aliasFor), "Command " + this.name + " cannot be alias for itself");
        this.shortText = ChatColor.YELLOW + "Alias for " + ChatColor.WHITE + this.aliasFor;
    }
    
    @Override
    public String getFullText(final CommandSender forWho) {
        final StringBuilder sb = new StringBuilder(this.shortText);
        final HelpTopic aliasForTopic = this.helpMap.getHelpTopic(this.aliasFor);
        if (aliasForTopic != null) {
            sb.append("\n");
            sb.append(aliasForTopic.getFullText(forWho));
        }
        return sb.toString();
    }
    
    @Override
    public boolean canSee(final CommandSender commandSender) {
        if (this.amendedPermission == null) {
            final HelpTopic aliasForTopic = this.helpMap.getHelpTopic(this.aliasFor);
            return aliasForTopic != null && aliasForTopic.canSee(commandSender);
        }
        return commandSender.hasPermission(this.amendedPermission);
    }
}
