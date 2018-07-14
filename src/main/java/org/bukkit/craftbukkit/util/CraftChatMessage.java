// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.util.Locale;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.List;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.util.text.TextComponentString;
import java.util.regex.Matcher;
import net.minecraft.util.text.Style;
import java.util.Iterator;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.ITextComponent;
import java.util.regex.Pattern;

public final class CraftChatMessage
{
    private static final Pattern LINK_PATTERN;
    
    static {
        LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " \\n]|$))))");
    }
    
    public static ITextComponent[] fromString(final String message) {
        return fromString(message, false);
    }
    
    public static ITextComponent[] fromString(final String message, final boolean keepNewlines) {
        return new StringMessage(message, keepNewlines).getOutput();
    }
    
    public static String fromComponent(final ITextComponent component) {
        return fromComponent(component, TextFormatting.BLACK);
    }
    
    public static String fromComponent(final ITextComponent component, final TextFormatting defaultColor) {
        if (component == null) {
            return "";
        }
        final StringBuilder out = new StringBuilder();
        for (final ITextComponent c : component) {
            final Style modi = c.getStyle();
            out.append((modi.getColor() == null) ? defaultColor : modi.getColor());
            if (modi.getBold()) {
                out.append(TextFormatting.BOLD);
            }
            if (modi.getItalic()) {
                out.append(TextFormatting.ITALIC);
            }
            if (modi.getUnderlined()) {
                out.append(TextFormatting.UNDERLINE);
            }
            if (modi.getStrikethrough()) {
                out.append(TextFormatting.STRIKETHROUGH);
            }
            if (modi.getObfuscated()) {
                out.append(TextFormatting.OBFUSCATED);
            }
            out.append(c.getUnformattedComponentText());
        }
        return out.toString().replaceFirst("^(" + defaultColor + ")*", "");
    }
    
    public static ITextComponent fixComponent(final ITextComponent component) {
        final Matcher matcher = CraftChatMessage.LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }
    
    private static ITextComponent fixComponent(ITextComponent component, final Matcher matcher) {
        if (component instanceof TextComponentString) {
            TextComponentString text = (TextComponentString)component;
            final String msg = text.getText();
            if (matcher.reset(msg).find()) {
                matcher.reset();
                final Style modifier = (text.getStyle() != null) ? text.getStyle() : new Style();
                final List<ITextComponent> extras = new ArrayList<ITextComponent>();
                final List<ITextComponent> extrasOld = new ArrayList<ITextComponent>(text.getSiblings());
                text = (TextComponentString)(component = new TextComponentString(""));
                int pos = 0;
                while (matcher.find()) {
                    String match = matcher.group();
                    if (!match.startsWith("http://") && !match.startsWith("https://")) {
                        match = "http://" + match;
                    }
                    final TextComponentString prev = new TextComponentString(msg.substring(pos, matcher.start()));
                    prev.setStyle(modifier);
                    extras.add(prev);
                    final TextComponentString link = new TextComponentString(matcher.group());
                    final Style linkModi = modifier.createShallowCopy();
                    linkModi.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                    link.setStyle(linkModi);
                    extras.add(link);
                    pos = matcher.end();
                }
                final TextComponentString prev2 = new TextComponentString(msg.substring(pos));
                prev2.setStyle(modifier);
                extras.add(prev2);
                extras.addAll(extrasOld);
                for (final ITextComponent c : extras) {
                    text.appendSibling(c);
                }
            }
        }
        final List<ITextComponent> extras2 = component.getSiblings();
        for (int i = 0; i < extras2.size(); ++i) {
            final ITextComponent comp = extras2.get(i);
            if (comp.getStyle() != null && comp.getStyle().getClickEvent() == null) {
                extras2.set(i, fixComponent(comp, matcher));
            }
        }
        if (component instanceof TextComponentTranslation) {
            final Object[] subs = ((TextComponentTranslation)component).getFormatArgs();
            for (int j = 0; j < subs.length; ++j) {
                final Object comp2 = subs[j];
                if (comp2 instanceof ITextComponent) {
                    final ITextComponent c2 = (ITextComponent)comp2;
                    if (c2.getStyle() != null && c2.getStyle().getClickEvent() == null) {
                        subs[j] = fixComponent(c2, matcher);
                    }
                }
                else if (comp2 instanceof String && matcher.reset((CharSequence)comp2).find()) {
                    subs[j] = fixComponent(new TextComponentString((String)comp2), matcher);
                }
            }
        }
        return component;
    }
    
    private static class StringMessage
    {
        private static final Map<Character, TextFormatting> formatMap;
        private static final Pattern INCREMENTAL_PATTERN;
        private final List<ITextComponent> list;
        private ITextComponent currentChatComponent;
        private Style modifier;
        private final ITextComponent[] output;
        private int currentIndex;
        private final String message;
        
        static {
            INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf('§') + "[0-9a-fk-or])|(\\n)|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " \\n]|$))))", 2);
            final ImmutableMap.Builder<Character, TextFormatting> builder = /*(ImmutableMap.Builder<Character, TextFormatting>)*/ImmutableMap.builder();
            TextFormatting[] values;
            for (int length = (values = TextFormatting.values()).length, i = 0; i < length; ++i) {
                final TextFormatting format = values[i];
                builder.put(/*(Object)*/Character.toLowerCase(format.toString().charAt(1)), /*(Object)*/format);
            }
            formatMap = (Map)builder.build();
        }
        
        private StringMessage(final String message, final boolean keepNewlines) {
            this.list = new ArrayList<ITextComponent>();
            this.currentChatComponent = new TextComponentString("");
            this.modifier = new Style();
            this.message = message;
            if (message == null) {
                this.output = new ITextComponent[] { this.currentChatComponent };
                return;
            }
            this.list.add(this.currentChatComponent);
            final Matcher matcher = StringMessage.INCREMENTAL_PATTERN.matcher(message);
            String match = null;
            while (matcher.find()) {
                int groupId = 0;
                while ((match = matcher.group(++groupId)) == null) {}
                this.appendNewComponent(matcher.start(groupId));
                Label_0462: {
                    switch (groupId) {
                        case 1: {
                            final TextFormatting format = StringMessage.formatMap.get(match.toLowerCase(Locale.ENGLISH).charAt(1));
                            if (format == TextFormatting.RESET) {
                                this.modifier = new Style();
                                break;
                            }
                            if (!format.isFancyStyling()) {
                                this.modifier = new Style().setColor(format);
                                break;
                            }
                            switch (format) {
                                case BOLD: {
                                    this.modifier.setBold(Boolean.TRUE);
                                    break Label_0462;
                                }
                                case ITALIC: {
                                    this.modifier.setItalic(Boolean.TRUE);
                                    break Label_0462;
                                }
                                case STRIKETHROUGH: {
                                    this.modifier.setStrikethrough(Boolean.TRUE);
                                    break Label_0462;
                                }
                                case UNDERLINE: {
                                    this.modifier.setUnderlined(Boolean.TRUE);
                                    break Label_0462;
                                }
                                case OBFUSCATED: {
                                    this.modifier.setObfuscated(Boolean.TRUE);
                                    break Label_0462;
                                }
                                default: {
                                    throw new AssertionError((Object)"Unexpected message format");
                                }
                            }
                        }
                        case 2: {
                            if (keepNewlines) {
                                this.currentChatComponent.appendSibling(new TextComponentString("\n"));
                                break;
                            }
                            this.currentChatComponent = null;
                            break;
                        }
                        case 3: {
                            if (!match.startsWith("http://") && !match.startsWith("https://")) {
                                match = "http://" + match;
                            }
                            this.modifier.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                            this.appendNewComponent(matcher.end(groupId));
                            this.modifier.setClickEvent(null);
                            break;
                        }
                    }
                }
                this.currentIndex = matcher.end(groupId);
            }
            if (this.currentIndex < message.length()) {
                this.appendNewComponent(message.length());
            }
            this.output = this.list.toArray(new ITextComponent[this.list.size()]);
        }
        
        private void appendNewComponent(final int index) {
            if (index <= this.currentIndex) {
                return;
            }
            final ITextComponent addition = new TextComponentString(this.message.substring(this.currentIndex, index)).setStyle(this.modifier);
            this.currentIndex = index;
            this.modifier = this.modifier.createShallowCopy();
            if (this.currentChatComponent == null) {
                this.currentChatComponent = new TextComponentString("");
                this.list.add(this.currentChatComponent);
            }
            this.currentChatComponent.appendSibling(addition);
        }
        
        private ITextComponent[] getOutput() {
            return this.output;
        }
    }
}
