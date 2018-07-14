// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.help;

import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.command.CommandSender;
import java.util.Collection;
import org.bukkit.help.HelpTopic;
import java.util.HashSet;
import org.bukkit.help.HelpMap;
import java.util.List;
import org.bukkit.help.IndexHelpTopic;

public class CustomIndexHelpTopic extends IndexHelpTopic
{
    private List<String> futureTopics;
    private final HelpMap helpMap;
    
    public CustomIndexHelpTopic(final HelpMap helpMap, final String name, final String shortText, final String permission, final List<String> futureTopics, final String preamble) {
        super(name, shortText, permission, new HashSet<HelpTopic>(), preamble);
        this.helpMap = helpMap;
        this.futureTopics = futureTopics;
    }
    
    @Override
    public String getFullText(final CommandSender sender) {
        if (this.futureTopics != null) {
            final List<HelpTopic> topics = new LinkedList<HelpTopic>();
            for (final String futureTopic : this.futureTopics) {
                final HelpTopic topic = this.helpMap.getHelpTopic(futureTopic);
                if (topic != null) {
                    topics.add(topic);
                }
            }
            this.setTopicsCollection(topics);
            this.futureTopics = null;
        }
        return super.getFullText(sender);
    }
}
