package org.bukkit.craftbukkit.v1_16_R3.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import org.bukkit.ChatColor;

public final class CraftChatMessage {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(ChatColor.COLOR_CHAR) + " \\n]|$))))");
    private static final Map<Character, TextFormatting> formatMap;
    private static final String COLOR_CHAR_STRING = String.valueOf(ChatColor.COLOR_CHAR);

    static {
        Builder<Character, TextFormatting> builder = ImmutableMap.builder();
        for (TextFormatting format : TextFormatting.values()) {
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        formatMap = builder.build();
    }

    public static TextFormatting getColor(ChatColor color) {
        return formatMap.get(color.getChar());
    }

    public static ChatColor getColor(TextFormatting format) {
        return ChatColor.getByChar(format.code);
    }

    private static final class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf(ChatColor.COLOR_CHAR) + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(ChatColor.COLOR_CHAR) + " \\n]|$))))|(\\n)", Pattern.CASE_INSENSITIVE);
        // Separate pattern with no group 3, new lines are part of previous string
        private static final Pattern INCREMENTAL_PATTERN_KEEP_NEWLINES = Pattern.compile("(" + String.valueOf(ChatColor.COLOR_CHAR) + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(ChatColor.COLOR_CHAR) + " ]|$))))", Pattern.CASE_INSENSITIVE);
        // ChatColor.b does not explicitly reset, its more of empty
        private static final Style RESET = Style.EMPTY.withBold(false).withItalic(false).setUnderlined(false).setStrikethrough(false).setObfuscated(false);

        private final List<ITextComponent> list = new ArrayList<ITextComponent>();
        private IFormattableTextComponent currentChatComponent = new StringTextComponent("");
        private Style modifier = Style.EMPTY;
        private final ITextComponent[] output;
        private int currentIndex;
        private StringBuilder hex;
        private final String message;

        private StringMessage(String message, boolean keepNewlines, boolean plain) {
            this.message = message;
            if (message == null) {
                output = new ITextComponent[]{currentChatComponent};
                return;
            }
            list.add(currentChatComponent);

            Matcher matcher = (keepNewlines ? INCREMENTAL_PATTERN_KEEP_NEWLINES : INCREMENTAL_PATTERN).matcher(message);
            String match = null;
            boolean needsAdd = false;
            while (matcher.find()) {
                int groupId = 0;
                while ((match = matcher.group(++groupId)) == null) {
                    // NOOP
                }
                int index = matcher.start(groupId);
                if (index > currentIndex) {
                    needsAdd = false;
                    appendNewComponent(index);
                }
                switch (groupId) {
                case 1:
                    char c = match.toLowerCase(java.util.Locale.ENGLISH).charAt(1);
                    TextFormatting format = formatMap.get(c);

                    if (c == 'x') {
                        hex = new StringBuilder("#");
                    } else if (hex != null) {
                        hex.append(c);

                        if (hex.length() == 7) {
                            modifier = modifier.withColor(Color.parseColor(hex.toString()));
                            hex = null;
                        }
                    } else if (format.isFormat() && format != TextFormatting.RESET) {
                        switch (format) {
                        case BOLD:
                            modifier = modifier.withBold(Boolean.TRUE);
                            break;
                        case ITALIC:
                            modifier = modifier.withItalic(Boolean.TRUE);
                            break;
                        case STRIKETHROUGH:
                            modifier = modifier.setStrikethrough(Boolean.TRUE);
                            break;
                        case UNDERLINE:
                            modifier = modifier.setUnderlined(Boolean.TRUE);
                            break;
                        case OBFUSCATED:
                            modifier = modifier.setObfuscated(Boolean.TRUE);
                            break;
                        default:
                            throw new AssertionError("Unexpected message format");
                        }
                    } else { // Color resets formatting
                        modifier = RESET.withColor(format);
                    }
                    needsAdd = true;
                    break;
                case 2:
                    if (plain) {
                        appendNewComponent(matcher.end(groupId));
                    } else {
                        if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                            match = "http://" + match;
                        }
                        modifier = modifier.withClickEvent(new ClickEvent(Action.OPEN_URL, match));
                        appendNewComponent(matcher.end(groupId));
                        modifier = modifier.withClickEvent((ClickEvent) null);
                    }
                    break;
                case 3:
                    if (needsAdd) {
                        appendNewComponent(index);
                    }
                    currentChatComponent = null;
                    break;
                }
                currentIndex = matcher.end(groupId);
            }

            if (currentIndex < message.length() || needsAdd) {
                appendNewComponent(message.length());
            }

            output = list.toArray(new ITextComponent[list.size()]);
        }

        private void appendNewComponent(int index) {
            ITextComponent addition = new StringTextComponent(message.substring(currentIndex, index)).setStyle(modifier);
            currentIndex = index;
            if (currentChatComponent == null) {
                currentChatComponent = new StringTextComponent("");
                list.add(currentChatComponent);
            }
            currentChatComponent.append(addition);
        }

        private ITextComponent[] getOutput() {
            return output;
        }
    }

    public static ITextComponent fromStringOrNull(String message) {
        return fromStringOrNull(message, false);
    }

    public static ITextComponent fromStringOrNull(String message, boolean keepNewlines) {
        return (message == null || message.isEmpty()) ? null : fromString(message, keepNewlines)[0];
    }

    public static ITextComponent[] fromString(String message) {
        return fromString(message, false);
    }

    public static ITextComponent[] fromString(String message, boolean keepNewlines) {
        return fromString(message, keepNewlines, false);
    }

    public static ITextComponent[] fromString(String message, boolean keepNewlines, boolean plain) {
        return new StringMessage(message, keepNewlines, plain).getOutput();
    }

    public static String toJSON(ITextComponent component) {
        return ITextComponent.Serializer.toJson(component);
    }

    public static String toJSONOrNull(ITextComponent component) {
        if (component == null) return null;
        return toJSON(component);
    }

    public static ITextComponent fromJSON(String jsonMessage) throws JsonParseException {
        // Note: This also parses plain Strings to text components.
        // Note: An empty message (empty, or only consisting of whitespace) results in null rather than a parse exception.
        return ITextComponent.Serializer.fromJson(jsonMessage);
    }

    public static ITextComponent fromJSONOrNull(String jsonMessage) {
        if (jsonMessage == null) return null;
        try {
            return fromJSON(jsonMessage); // Can return null
        } catch (JsonParseException ex) {
            return null;
        }
    }

    public static ITextComponent fromJSONOrString(String message) {
        return fromJSONOrString(message, false);
    }

    public static ITextComponent fromJSONOrString(String message, boolean keepNewlines) {
        return fromJSONOrString(message, false, keepNewlines);
    }

    private static ITextComponent fromJSONOrString(String message, boolean nullable, boolean keepNewlines) {
        if (message == null) message = "";
        if (nullable && message.isEmpty()) return null;
        if (isLegacy(message)) {
            return fromString(message, keepNewlines)[0];
        } else {
            ITextComponent component = fromJSONOrNull(message);
            if (component != null) {
                return component;
            } else {
                return fromString(message, keepNewlines)[0];
            }
        }
    }

    public static String fromJSONOrStringToJSON(String message) {
        return fromJSONOrStringToJSON(message, false);
    }

    public static String fromJSONOrStringToJSON(String message, boolean keepNewlines) {
        return fromJSONOrStringToJSON(message, false, keepNewlines, Integer.MAX_VALUE, false);
    }

    public static String fromJSONOrStringOrNullToJSON(String message) {
        return fromJSONOrStringOrNullToJSON(message, false);
    }

    public static String fromJSONOrStringOrNullToJSON(String message, boolean keepNewlines) {
        return fromJSONOrStringToJSON(message, true, keepNewlines, Integer.MAX_VALUE, false);
    }

    public static String fromJSONOrStringToJSON(String message, boolean nullable, boolean keepNewlines, int maxLength, boolean checkJsonContentLength) {
        if (message == null) message = "";
        if (nullable && message.isEmpty()) return null;
        if (isLegacy(message)) {
            message = trimMessage(message, maxLength);
            return fromStringToJSON(message, keepNewlines);
        } else {
            // If the input can be parsed as JSON, we use that:
            ITextComponent component = fromJSONOrNull(message);
            if (component != null) {
                if (checkJsonContentLength) {
                    String content = fromComponent(component);
                    String trimmedContent = trimMessage(content, maxLength);
                    if (content != trimmedContent) { // identity comparison is fine here
                        // Note: The resulting text has all non-plain text features stripped.
                        return fromStringToJSON(trimmedContent, keepNewlines);
                    }
                }
                return message;
            } else {
                // Else we interpret the input as legacy text:
                message = trimMessage(message, maxLength);
                return fromStringToJSON(message, keepNewlines);
            }
        }
    }

    public static String trimMessage(String message, int maxLength) {
        if (message != null && message.length() > maxLength) {
            return message.substring(0, maxLength);
        } else {
            return message;
        }
    }

    // Heuristic detection of legacy (plain) text.
    // May produce false-negatives: I.e. a return value of false does not imply that the text represents modern (JSON-based) text.
    // We also consider empty Strings as legacy. The component deserializer cannot parse them (produces null).
    private static boolean isLegacy(String message) {
        // assert: message != null
        return message.trim().isEmpty() || message.contains(COLOR_CHAR_STRING);
    }

    public static String fromStringToJSON(String message) {
        return fromStringToJSON(message, false);
    }

    public static String fromStringToJSON(String message, boolean keepNewlines) {
        ITextComponent component = CraftChatMessage.fromString(message, keepNewlines)[0];
        return CraftChatMessage.toJSON(component);
    }

    public static String fromStringOrNullToJSON(String message) {
        ITextComponent component = CraftChatMessage.fromStringOrNull(message);
        return CraftChatMessage.toJSONOrNull(component);
    }

    public static String fromJSONComponent(String jsonMessage) {
        ITextComponent component = CraftChatMessage.fromJSONOrNull(jsonMessage);
        return CraftChatMessage.fromComponent(component);
    }

    public static String fromComponent(ITextComponent component) {
        if (component == null) return "";
        StringBuilder out = new StringBuilder();

        boolean hadFormat = false;
        for (ITextComponent c : (Iterable<ITextComponent>) component) {
            Style modi = c.getStyle();
            Color color = modi.getColor();
            if (!c.getContents().isEmpty() || color != null) {
                if (color != null) {
                    if (color.format != null) {
                        out.append(color.format);
                    } else {
                        out.append(ChatColor.COLOR_CHAR).append("x");
                        for (char magic : color.serialize().substring(1).toCharArray()) {
                            out.append(ChatColor.COLOR_CHAR).append(magic);
                        }
                    }
                    hadFormat = true;
                } else if (hadFormat) {
                    out.append(ChatColor.RESET);
                    hadFormat = false;
                }
            }
            if (modi.isBold()) {
                out.append(TextFormatting.BOLD);
                hadFormat = true;
            }
            if (modi.isItalic()) {
                out.append(TextFormatting.ITALIC);
                hadFormat = true;
            }
            if (modi.isUnderlined()) {
                out.append(TextFormatting.UNDERLINE);
                hadFormat = true;
            }
            if (modi.isStrikethrough()) {
                out.append(TextFormatting.STRIKETHROUGH);
                hadFormat = true;
            }
            if (modi.isObfuscated()) {
                out.append(TextFormatting.OBFUSCATED);
                hadFormat = true;
            }
            c.visitSelf((x) -> {
                out.append(x);
                return Optional.empty();
            });
        }
        return out.toString();
    }

    public static ITextComponent fixComponent(ITextComponent component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }

    private static ITextComponent fixComponent(ITextComponent component, Matcher matcher) {
        if (component instanceof StringTextComponent) {
            StringTextComponent text = ((StringTextComponent) component);
            String msg = text.getContents();
            if (matcher.reset(msg).find()) {
                matcher.reset();

                Style modifier = text.getStyle();
                List<ITextComponent> extras = new ArrayList<ITextComponent>();
                List<ITextComponent> extrasOld = new ArrayList<ITextComponent>(text.getSiblings());
                component = text = new StringTextComponent("");

                int pos = 0;
                while (matcher.find()) {
                    String match = matcher.group();

                    if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                        match = "http://" + match;
                    }

                    StringTextComponent prev = new StringTextComponent(msg.substring(pos, matcher.start()));
                    prev.setStyle(modifier);
                    extras.add(prev);

                    StringTextComponent link = new StringTextComponent(matcher.group());
                    Style linkModi = modifier.withClickEvent(new ClickEvent(Action.OPEN_URL, match));
                    link.setStyle(linkModi);
                    extras.add(link);

                    pos = matcher.end();
                }

                StringTextComponent prev = new StringTextComponent(msg.substring(pos));
                prev.setStyle(modifier);
                extras.add(prev);
                extras.addAll(extrasOld);

                for (ITextComponent c : extras) {
                    text.append(c);
                }
            }
        }

        List<ITextComponent> extras = component.getSiblings();
        for (int i = 0; i < extras.size(); i++) {
            ITextComponent comp = extras.get(i);
            if (comp.getStyle() != null && comp.getStyle().getClickEvent() == null) {
                extras.set(i, fixComponent(comp, matcher));
            }
        }

        if (component instanceof TranslationTextComponent) {
            Object[] subs = ((TranslationTextComponent) component).getArgs();
            for (int i = 0; i < subs.length; i++) {
                Object comp = subs[i];
                if (comp instanceof ITextComponent) {
                    ITextComponent c = (ITextComponent) comp;
                    if (c.getStyle() != null && c.getStyle().getClickEvent() == null) {
                        subs[i] = fixComponent(c, matcher);
                    }
                } else if (comp instanceof String && matcher.reset((String) comp).find()) {
                    subs[i] = fixComponent(new StringTextComponent((String) comp), matcher);
                }
            }
        }

        return component;
    }

    private CraftChatMessage() {
    }
}
