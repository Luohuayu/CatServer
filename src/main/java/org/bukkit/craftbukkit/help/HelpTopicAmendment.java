// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.help;

public class HelpTopicAmendment
{
    private final String topicName;
    private final String shortText;
    private final String fullText;
    private final String permission;
    
    public HelpTopicAmendment(final String topicName, final String shortText, final String fullText, final String permission) {
        this.fullText = fullText;
        this.shortText = shortText;
        this.topicName = topicName;
        this.permission = permission;
    }
    
    public String getFullText() {
        return this.fullText;
    }
    
    public String getShortText() {
        return this.shortText;
    }
    
    public String getTopicName() {
        return this.topicName;
    }
    
    public String getPermission() {
        return this.permission;
    }
}
