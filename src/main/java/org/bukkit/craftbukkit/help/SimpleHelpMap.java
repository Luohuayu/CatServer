// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.help;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import java.util.TreeSet;
import java.util.Set;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.command.PluginCommand;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.help.IndexHelpTopic;
import java.util.Collection;
import com.google.common.collect.Collections2;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.bukkit.Server;
import java.util.HashMap;
import java.util.Comparator;
import java.util.TreeMap;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.command.Command;
import org.bukkit.help.HelpTopicFactory;
import java.util.Map;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpMap;

public class SimpleHelpMap implements HelpMap
{
    private HelpTopic defaultTopic;
    private final Map<String, HelpTopic> helpTopics;
    private final Map<Class, HelpTopicFactory<Command>> topicFactoryMap;
    private final CraftServer server;
    private HelpYamlReader yaml;
    
    public SimpleHelpMap(final CraftServer server) {
        this.helpTopics = new TreeMap<String, HelpTopic>(HelpTopicComparator.topicNameComparatorInstance());
        this.topicFactoryMap = new HashMap<Class, HelpTopicFactory<Command>>();
        this.server = server;
        this.yaml = new HelpYamlReader(server);
        Predicate indexFilter = Predicates.not(Predicates.instanceOf((Class)CommandAliasHelpTopic.class));
        if (!this.yaml.commandTopicsInMasterIndex()) {
            indexFilter = Predicates.and(indexFilter, Predicates.not((Predicate)new IsCommandTopicPredicate()));
        }
        this.defaultTopic = new IndexHelpTopic("Index", null, null, Collections2.filter((Collection)this.helpTopics.values(), indexFilter), "Use /help [n] to get page n of help.");
        this.registerHelpTopicFactory(MultipleCommandAlias.class, new MultipleCommandAliasHelpTopicFactory());
    }
    
    @Override
    public synchronized HelpTopic getHelpTopic(final String topicName) {
        if (topicName.equals("")) {
            return this.defaultTopic;
        }
        if (this.helpTopics.containsKey(topicName)) {
            return this.helpTopics.get(topicName);
        }
        return null;
    }
    
    @Override
    public Collection<HelpTopic> getHelpTopics() {
        return this.helpTopics.values();
    }
    
    @Override
    public synchronized void addTopic(final HelpTopic topic) {
        if (!this.helpTopics.containsKey(topic.getName())) {
            this.helpTopics.put(topic.getName(), topic);
        }
    }
    
    @Override
    public synchronized void clear() {
        this.helpTopics.clear();
    }
    
    @Override
    public List<String> getIgnoredPlugins() {
        return this.yaml.getIgnoredPlugins();
    }
    
    public synchronized void initializeGeneralTopics() {
        this.yaml = new HelpYamlReader(this.server);
        for (final HelpTopic topic : this.yaml.getGeneralTopics()) {
            this.addTopic(topic);
        }
        for (final HelpTopic topic : this.yaml.getIndexTopics()) {
            if (topic.getName().equals("Default")) {
                this.defaultTopic = topic;
            }
            else {
                this.addTopic(topic);
            }
        }
    }
    
    public synchronized void initializeCommands() {
        final Set<String> ignoredPlugins = new HashSet<String>(this.yaml.getIgnoredPlugins());
        if (ignoredPlugins.contains("All")) {
            return;
        }
    Label_0230:
        for (final Command command : this.server.getCommandMap().getCommands()) {
            if (this.commandInIgnoredPlugin(command, ignoredPlugins)) {
                continue;
            }
            for (final Class c : this.topicFactoryMap.keySet()) {
                if (c.isAssignableFrom(command.getClass())) {
                    final HelpTopic t = this.topicFactoryMap.get(c).createTopic(command);
                    if (t != null) {
                        this.addTopic(t);
                        continue Label_0230;
                    }
                    continue Label_0230;
                }
                else {
                    if (!(command instanceof PluginCommand) || !c.isAssignableFrom(((PluginCommand)command).getExecutor().getClass())) {
                        continue;
                    }
                    final HelpTopic t = this.topicFactoryMap.get(c).createTopic(command);
                    if (t != null) {
                        this.addTopic(t);
                        continue Label_0230;
                    }
                    continue Label_0230;
                }
            }
            this.addTopic(new GenericCommandHelpTopic(command));
        }
        for (final Command command : this.server.getCommandMap().getCommands()) {
            if (this.commandInIgnoredPlugin(command, ignoredPlugins)) {
                continue;
            }
            for (final String alias : command.getAliases()) {
                if (this.server.getCommandMap().getCommand(alias) == command) {
                    this.addTopic(new CommandAliasHelpTopic("/" + alias, "/" + command.getLabel(), this));
                }
            }
        }
        final Collection<HelpTopic> filteredTopics = (Collection<HelpTopic>)Collections2.filter((Collection)this.helpTopics.values(), Predicates.instanceOf((Class)CommandAliasHelpTopic.class));
        if (!filteredTopics.isEmpty()) {
            this.addTopic(new IndexHelpTopic("Aliases", "Lists command aliases", null, filteredTopics));
        }
        final Map<String, Set<HelpTopic>> pluginIndexes = new HashMap<String, Set<HelpTopic>>();
        this.fillPluginIndexes(pluginIndexes, this.server.getCommandMap().getCommands());
        for (final Map.Entry<String, Set<HelpTopic>> entry : pluginIndexes.entrySet()) {
            this.addTopic(new IndexHelpTopic(entry.getKey(), "All commands for " + entry.getKey(), null, entry.getValue(), "Below is a list of all " + entry.getKey() + " commands:"));
        }
        for (final HelpTopicAmendment amendment : this.yaml.getTopicAmendments()) {
            if (this.helpTopics.containsKey(amendment.getTopicName())) {
                this.helpTopics.get(amendment.getTopicName()).amendTopic(amendment.getShortText(), amendment.getFullText());
                if (amendment.getPermission() == null) {
                    continue;
                }
                this.helpTopics.get(amendment.getTopicName()).amendCanSee(amendment.getPermission());
            }
        }
    }
    
    private void fillPluginIndexes(final Map<String, Set<HelpTopic>> pluginIndexes, final Collection<? extends Command> commands) {
        for (final Command command : commands) {
            final String pluginName = this.getCommandPluginName(command);
            if (pluginName != null) {
                final HelpTopic topic = this.getHelpTopic("/" + command.getLabel());
                if (topic == null) {
                    continue;
                }
                if (!pluginIndexes.containsKey(pluginName)) {
                    pluginIndexes.put(pluginName, new TreeSet<HelpTopic>(HelpTopicComparator.helpTopicComparatorInstance()));
                }
                pluginIndexes.get(pluginName).add(topic);
            }
        }
    }
    
    private String getCommandPluginName(final Command command) {
        if (command instanceof VanillaCommandWrapper) {
            return "Minecraft";
        }
        if (command instanceof BukkitCommand || command instanceof VanillaCommand) {
            return "Bukkit";
        }
        if (command instanceof PluginIdentifiableCommand) {
            return ((PluginIdentifiableCommand)command).getPlugin().getName();
        }
        return null;
    }
    
    private boolean commandInIgnoredPlugin(final Command command, final Set<String> ignoredPlugins) {
        return ((command instanceof BukkitCommand || command instanceof VanillaCommand) && ignoredPlugins.contains("Bukkit")) || (command instanceof PluginIdentifiableCommand && ignoredPlugins.contains(((PluginIdentifiableCommand)command).getPlugin().getName()));
    }
    
    @Override
    public void registerHelpTopicFactory(final Class commandClass, final HelpTopicFactory factory) {
        if (!Command.class.isAssignableFrom(commandClass) && !CommandExecutor.class.isAssignableFrom(commandClass)) {
            throw new IllegalArgumentException("commandClass must implement either Command or CommandExecutor!");
        }
        this.topicFactoryMap.put(commandClass, factory);
    }
    
    private class IsCommandTopicPredicate implements Predicate<HelpTopic>
    {
        public boolean apply(final HelpTopic topic) {
            return topic.getName().charAt(0) == '/';
        }
    }
}
