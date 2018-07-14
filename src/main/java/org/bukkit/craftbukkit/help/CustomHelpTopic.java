// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.help;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

public class CustomHelpTopic extends HelpTopic
{
    private final String permissionNode;
    
    public CustomHelpTopic(final String name, final String shortText, final String fullText, final String permissionNode) {
        this.permissionNode = permissionNode;
        this.name = name;
        this.shortText = shortText;
        this.fullText = String.valueOf(shortText) + "\n" + fullText;
    }
    
    @Override
    public boolean canSee(final CommandSender sender) {
        return sender instanceof ConsoleCommandSender || this.permissionNode.equals("") || sender.hasPermission(this.permissionNode);
    }
}
