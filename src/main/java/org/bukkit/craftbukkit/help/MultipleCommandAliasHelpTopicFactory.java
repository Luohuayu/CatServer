// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.help;

import org.bukkit.command.Command;
import org.bukkit.help.HelpTopic;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.help.HelpTopicFactory;

public class MultipleCommandAliasHelpTopicFactory implements HelpTopicFactory<MultipleCommandAlias>
{
    @Override
    public HelpTopic createTopic(final MultipleCommandAlias multipleCommandAlias) {
        return new MultipleCommandAliasHelpTopic(multipleCommandAlias);
    }
}
