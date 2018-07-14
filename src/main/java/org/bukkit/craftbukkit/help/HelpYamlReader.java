// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.help;

import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.ChatColor;
import java.util.LinkedList;
import org.bukkit.help.HelpTopic;
import java.util.List;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.Configuration;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.common.base.Charsets;
import java.io.File;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;

public class HelpYamlReader
{
    private YamlConfiguration helpYaml;
    private final char ALT_COLOR_CODE = '&';
    private final Server server;
    
    public HelpYamlReader(final Server server) {
        this.server = server;
        final File helpYamlFile = new File("help.yml");
        final YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/help.yml"), Charsets.UTF_8));
        try {
            this.helpYaml = YamlConfiguration.loadConfiguration(helpYamlFile);
            this.helpYaml.options().copyDefaults(true);
            this.helpYaml.setDefaults(defaultConfig);
            try {
                if (!helpYamlFile.exists()) {
                    this.helpYaml.save(helpYamlFile);
                }
            }
            catch (IOException ex) {
                server.getLogger().log(Level.SEVERE, "Could not save " + helpYamlFile, ex);
            }
        }
        catch (Exception ex2) {
            server.getLogger().severe("Failed to load help.yml. Verify the yaml indentation is correct. Reverting to default help.yml.");
            this.helpYaml = defaultConfig;
        }
    }
    
    public List<HelpTopic> getGeneralTopics() {
        final List<HelpTopic> topics = new LinkedList<HelpTopic>();
        final ConfigurationSection generalTopics = this.helpYaml.getConfigurationSection("general-topics");
        if (generalTopics != null) {
            for (final String topicName : generalTopics.getKeys(false)) {
                final ConfigurationSection section = generalTopics.getConfigurationSection(topicName);
                final String shortText = ChatColor.translateAlternateColorCodes('&', section.getString("shortText", ""));
                final String fullText = ChatColor.translateAlternateColorCodes('&', section.getString("fullText", ""));
                final String permission = section.getString("permission", "");
                topics.add(new CustomHelpTopic(topicName, shortText, fullText, permission));
            }
        }
        return topics;
    }
    
    public List<HelpTopic> getIndexTopics() {
        final List<HelpTopic> topics = new LinkedList<HelpTopic>();
        final ConfigurationSection indexTopics = this.helpYaml.getConfigurationSection("index-topics");
        if (indexTopics != null) {
            for (final String topicName : indexTopics.getKeys(false)) {
                final ConfigurationSection section = indexTopics.getConfigurationSection(topicName);
                final String shortText = ChatColor.translateAlternateColorCodes('&', section.getString("shortText", ""));
                final String preamble = ChatColor.translateAlternateColorCodes('&', section.getString("preamble", ""));
                final String permission = ChatColor.translateAlternateColorCodes('&', section.getString("permission", ""));
                final List<String> commands = section.getStringList("commands");
                topics.add(new CustomIndexHelpTopic(this.server.getHelpMap(), topicName, shortText, permission, commands, preamble));
            }
        }
        return topics;
    }
    
    public List<HelpTopicAmendment> getTopicAmendments() {
        final List<HelpTopicAmendment> amendments = new LinkedList<HelpTopicAmendment>();
        final ConfigurationSection commandTopics = this.helpYaml.getConfigurationSection("amended-topics");
        if (commandTopics != null) {
            for (final String topicName : commandTopics.getKeys(false)) {
                final ConfigurationSection section = commandTopics.getConfigurationSection(topicName);
                final String description = ChatColor.translateAlternateColorCodes('&', section.getString("shortText", ""));
                final String usage = ChatColor.translateAlternateColorCodes('&', section.getString("fullText", ""));
                final String permission = section.getString("permission", "");
                amendments.add(new HelpTopicAmendment(topicName, description, usage, permission));
            }
        }
        return amendments;
    }
    
    public List<String> getIgnoredPlugins() {
        return this.helpYaml.getStringList("ignore-plugins");
    }
    
    public boolean commandTopicsInMasterIndex() {
        return this.helpYaml.getBoolean("command-topics-in-master-index", true);
    }
}
